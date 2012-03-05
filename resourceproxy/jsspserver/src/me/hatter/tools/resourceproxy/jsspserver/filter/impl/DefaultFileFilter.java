package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import me.hatter.tools.resourceproxy.commons.util.FileUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class DefaultFileFilter implements ResourceFilter {

    private static final String     DEFAULT_CONTENT_TYE     = "application/octet-stream";
    private static final Properties CONTENT_TYPE_PROPERTIES = new Properties();
    static {
        try {
            CONTENT_TYPE_PROPERTIES.load(DefaultFileFilter.class.getResourceAsStream("/content-type.properties"));
        } catch (IOException e) {
            System.err.println("[ERROR] Error in load content-type.properties: ");
            e.printStackTrace();
        }
    }

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String fpath = request.getFPath();
        File tfile = new File(JsspFilter.JSSP_PATH, fpath);
        if ((!fpath.equals("/")) && tfile.exists()) {
            int indexOfLastDot = fpath.lastIndexOf('.');
            String tfileExt = (indexOfLastDot < 0) ? fpath : fpath.substring(indexOfLastDot + 1);
            String contentType = CONTENT_TYPE_PROPERTIES.getProperty(tfileExt.toLowerCase());
            if (contentType == null) {
                contentType = DEFAULT_CONTENT_TYE; // unknow type
            }
            System.out.println("[INFO] Found file: " + tfile + ", content-type: " + contentType);
            HttpResponse response = new HttpResponse();
            response.setContentType(contentType);
            response.setStatus(200);
            response.setStatusMessage("OK");
            response.getHeaderMap().set("Content-Type", contentType);
            response.setBytes(FileUtil.readFileToBytes(tfile));
            return response;
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
