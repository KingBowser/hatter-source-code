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
import java.util.zip.InflaterInputStream;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public class HttpResponseUtil {

    private static Set<String> IGNORE_HEADER_SET = new HashSet<String>(Arrays.asList("Transfer-Encoding", // ,
                                                                                     "Content-Encoding", // ,
                                                                                     "Content-Length", // ,
                                                                                     "Cache-Control", // ,
                                                                                     "Expires"));

    public static HttpResponse build(HttpURLConnection httpURLConnection) throws IOException {
        HttpResponse response = new HttpResponse();
        response.setEncoding(httpURLConnection.getContentEncoding());
        Map<String, List<String>> headFields = httpURLConnection.getHeaderFields();
        for (String key : headFields.keySet()) {
            String firstValue = headFields.get(null).get(0);
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
                if (!IGNORE_HEADER_SET.contains(key)) {
                    response.set(key, headFields.get(key));
                }
            }
        }
        response.set("XX-Server", Arrays.asList("ResourceProxy_By_Hatter/0.1"));

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
                IOUtil.copy(new InflaterInputStream(new ByteArrayInputStream(bytes)), decodebaos);
                bytes = decodebaos.toByteArray();
            } else {
                System.out.println("Unknow content encoding: " + response.getEncoding());
            }
        }
        response.setBytes(bytes);
        String charset = (response.getCharset() == null) ? "UTF-8" : response.getCharset();
        Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes), charset);
        String string = IOUtil.copyToString(reader);
        response.setString(string);

        return response;
    }
}
