package me.hatter.tools.resourceproxy.httpobjects.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public class HttpResponseUtil {

    private static final LogTool logTool                   = LogTools.getLogTool(HttpResponseUtil.class);

    private static Set<Integer>  NO_CONTENT_STATUS         = new HashSet<Integer>(Arrays.asList(301, 302, 304));
    private static Set<String>   IGNORE_HEADER_SET         = new HashSet<String>(
                                                                                 CollUtil.toUpperCase(Arrays.asList("Transfer-Encoding", // ,
                                                                                                                    "Content-Encoding", // ,
                                                                                                                    "Content-Length", // ,
                                                                                                                    "Cache-Control", // ,
                                                                                                                    "Expires")));
    private static Set<String>   STRINGFY_CONTENT_TYPE_SET = new HashSet<String>(
                                                                                 CollUtil.toUpperCase(Arrays.asList("application/javascript",
                                                                                                                    "application/x-javascript",
                                                                                                                    "application/json",
                                                                                                                    "application/xml")));

    public static HttpResponse build(HttpURLConnection httpURLConnection) throws IOException {
        if (logTool.isInfoEnable()) {
            logTool.info("Response conent length: " + httpURLConnection.getContentLength());
        }
        HttpResponse response = new HttpResponse();
        response.setEncoding(httpURLConnection.getContentEncoding());
        Map<String, List<String>> headFields = httpURLConnection.getHeaderFields();
        for (String key : headFields.keySet()) {
            String firstValue = headFields.get(key).get(0);
            if (key == null) {
                Matcher m = Pattern.compile("HTTP/((0\\.9)|(1\\.0)|(1\\.1))\\s+(\\d+)\\s+(.*)").matcher(firstValue);
                if (m.matches()) {
                    response.setStatus(Integer.parseInt(m.group(5)));
                    response.setStatusMessage(m.group(6));
                }
            } else {
                if ("Content-Type".equalsIgnoreCase(key)) {
                    Matcher m = Pattern.compile("(.*);\\s*charset\\s*=\\s*([a-zA-Z\\-\\d]+)", Pattern.CASE_INSENSITIVE).matcher(firstValue);
                    if (m.matches()) {
                        response.setContentType(m.group(1));
                        response.setCharset(m.group(2));
                    } else {
                        response.setContentType(firstValue);
                    }
                }
                // if ("Content-Encoding".equalsIgnoreCase(key)) {
                // response.setEncoding(firstValue);
                // }
                if (!IGNORE_HEADER_SET.contains(key.toUpperCase())) {
                    response.set(key, headFields.get(key));
                }
            }
        }

        if (NO_CONTENT_STATUS.contains(response.getStatus())) {
            if (logTool.isInfoEnable()) {
                logTool.info("Response status code is: " + response.getStatus() + " " + response.getStatusMessage());
            }
        }

        InputStream inputStream = httpURLConnection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtil.copy(inputStream, baos);
        byte[] bytes = baos.toByteArray();
        if (response.getEncoding() != null) {
            if ("gzip".equalsIgnoreCase(response.getEncoding())) {
                ByteArrayOutputStream decodebaos = new ByteArrayOutputStream();
                IOUtil.copy(new GZIPInputStream(new ByteArrayInputStream(bytes)), decodebaos);
                bytes = decodebaos.toByteArray();
            } else if ("deflate".equalsIgnoreCase(response.getEncoding())) {
                ByteArrayOutputStream decodebaos = new ByteArrayOutputStream();
                IOUtil.copy(new InflaterInputStream(new ByteArrayInputStream(bytes), new Inflater(true)), decodebaos);
                bytes = decodebaos.toByteArray();
            } else {
                if (logTool.isWarnEnable()) {
                    logTool.warn("Unknow content encoding: " + response.getEncoding());
                }
            }
        }
        response.setBytes(bytes);
        if ((response.getContentType() != null) && (response.getCharset() != null)) {
            if (isTextContentType(response.getContentType())) {
                String charset = (response.getCharset() == null) ? "UTF-8" : response.getCharset();
                Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes), charset);
                String string = IOUtil.readToString(reader);
                response.setString(string);
            }
        }

        return response;
    }

    public static boolean isTextContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        return contentType.toUpperCase().startsWith("TEXT/")
               || STRINGFY_CONTENT_TYPE_SET.contains(contentType.toUpperCase());
    }
}
