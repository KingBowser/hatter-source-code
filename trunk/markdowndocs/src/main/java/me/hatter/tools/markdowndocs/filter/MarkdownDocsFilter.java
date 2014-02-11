package me.hatter.tools.markdowndocs.filter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.resource.impl.URLResource;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdowndocs.TestMain;
import me.hatter.tools.markdowndocs.config.Config;
import me.hatter.tools.markdowndocs.config.Parameter;
import me.hatter.tools.markdowndocs.model.Page;
import me.hatter.tools.markdowndocs.template.ConfigParser;
import me.hatter.tools.markdowndocs.template.PageParser;
import me.hatter.tools.markdowndocs.template.ParameterParser;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class MarkdownDocsFilter implements ResourceFilter {

    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        try {
            String ext = StringUtil.substringAfterLast(request.getFPath(), ".");
            if (request.getFPath().endsWith("/")) {
                ext = "html";
            }
            String contentType = ContentTypes.getContentTypeByExt(ext);

            if (request.getFPath().startsWith("/assets")) {
                URL url = MarkdownDocsFilter.class.getResource("/" + ParameterParser.getGlobalParamter().getTemplate()
                                                               + request.getFPath());
                if (url != null) {
                    return makeHttpResponse(null, IOUtil.readToBytesAndClose(url.openStream()), contentType);
                }

                File file = new File(request.getFPath().substring(1));
                if (file.exists()) {
                    return makeHttpResponse(null, FileUtil.readFileToBytes(file), contentType);
                }
            }
            if (!request.getFPath().startsWith("/.")) {
                if (request.getFPath().equals("/") || new File(request.getFPath().substring(1)).isDirectory()) {
                    String dirName = request.getFPath().equals("/") ? null : request.getFPath().substring(1);
                    if ((dirName != null) && (dirName.endsWith("/"))) {
                        dirName = dirName.substring(0, dirName.length() - 1);
                    }

                    Parameter parameter = ParameterParser.getGlobalParamter();
                    Config config = ConfigParser.readConfig(dirName);
                    Page page = PageParser.parsePage(dirName);

                    Map<String, Object> addContext = new HashMap<String, Object>();
                    addContext.put("parameter", parameter);
                    addContext.put("config", config);
                    addContext.put("page", page);

                    BufferWriter bw = new BufferWriter();
                    URLResource resource = new URLResource(
                                                           TestMain.class.getResource("/"
                                                                                      + ParameterParser.getGlobalParamter().getTemplate()
                                                                                      + "/templates/main.template.jssp"),
                                                           "main.template.jssp");
                    JsspExecutor.executeJssp(resource, new HashMap<String, Object>(), addContext, null, bw);

                    return makeHttpResponse(bw.getBufferedString(), null, contentType);
                } else {
                    File file = new File(request.getFPath().substring(1));
                    if (file.exists()) {
                        return makeHttpResponse(null, FileUtil.readFileToBytes(file), contentType);
                    }
                }
            }
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }

        // default
        return chain.next().filter(request, chain);
    }

    private HttpResponse makeHttpResponse(String text, byte[] bytes, String contentType) {
        HttpResponse response = new HttpResponse();
        response.setContentType(contentType);
        response.setCharset(ContentTypes.UTF8_CHARSET);
        response.setStatus(HttpConstants.STATUS_SUCCESS);
        response.setStatusMessage("OK");
        response.getHeaderMap().set(ContentTypes.CONTENT_TYPE, contentType);
        if (bytes != null) {
            response.setBytes(bytes);
        } else {
            response.setString(text);
        }
        return response;
    }
}
