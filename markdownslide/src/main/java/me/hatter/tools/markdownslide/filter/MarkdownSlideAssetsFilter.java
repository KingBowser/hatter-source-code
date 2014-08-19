package me.hatter.tools.markdownslide.filter;

import java.io.File;
import java.net.URL;

import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdownslide.config.Configs;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class MarkdownSlideAssetsFilter implements ResourceFilter {

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception {
        String ext = StringUtil.substringAfterLast(request.getFPath(), ".");
        if (request.getFPath().endsWith("/")) {
            ext = "html";
        }
        String contentType = ContentTypes.getContentTypeByExt(ext);
        if (request.getFPath().startsWith("/assets")) {
            URL url = MarkdownSlideAssetsFilter.class.getResource("/" + Configs.getConfig().getTemplate()
                                                                  + request.getFPath());
            if (url != null) {
                return makeHttpResponse(null, IOUtil.readToBytesAndClose(url.openStream()), contentType);
            }

            File file = new File(request.getFPath().substring(1));
            if (file.exists()) {
                return makeHttpResponse(null, FileUtil.readFileToBytes(file), contentType);
            }
        }
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
