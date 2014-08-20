package me.hatter.tools.markdownslide.filter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.resource.impl.URLResource;
import me.hatter.tools.markdownslide.config.Configs;
import me.hatter.tools.markdownslide.util.MarkdownSlideUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.util.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class MarkdownSlideFilter implements ResourceFilter {

    private volatile static File inlineCss = null;

    synchronized public static File initInlineCss() {
        if (inlineCss == null) {
            inlineCss = new File(Environment.USER_DIR, "inline.css");
            if (!inlineCss.exists()) {
                URL defaultInlineCss = MarkdownSlideAssetsFilter.class.getResource("/"
                                                                                   + Configs.getConfig().getTemplate()
                                                                                   + "/assets/css/default_inline.css");
                if (defaultInlineCss != null) {
                    try {
                        FileUtil.writeStringToFile(inlineCss,
                                                   IOUtil.readToStringAndClose(defaultInlineCss.openStream()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return inlineCss;
    }

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception {
        if (request.getFPath().equals("/")) {
            initInlineCss();

            File slidesMd = new File(Environment.USER_DIR, "slides.md");
            if (!slidesMd.exists()) {
                throw new RuntimeException("File `slides.md` not found!");
            }

            Map<String, Object> addContext = new HashMap<String, Object>();
            addContext.put("config", Configs.getConfig());
            addContext.put("inline_css", FileUtil.readFileToString(inlineCss));
            addContext.put("slides_md", MarkdownSlideUtil.processMds(FileUtil.readFileToString(slidesMd)));

            BufferWriter bw = new BufferWriter();
            URLResource resource = new URLResource(
                                                   MarkdownSlideFilter.class.getResource("/"
                                                                                         + Configs.getConfig().getTemplate()
                                                                                         + "/templates/template.jssp"),
                                                   "template.jssp");
            JsspExecutor.executeJssp(resource, new HashMap<String, Object>(), addContext, null, bw);

            return makeHttpResponse(bw.getBufferedString(), null, ContentTypes.getContentTypeByExt("html"));
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
