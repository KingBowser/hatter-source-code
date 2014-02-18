package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class ListingFileFilter implements ResourceFilter {

    private String dir;

    public ListingFileFilter() {
        this(Environment.USER_DIR);
    }

    public ListingFileFilter(String dir) {
        this.dir = dir;
    }

    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception {
        String fpath = request.getFPath();

        File file = new File(dir, fpath);
        if (!file.exists()) {
            return chain.next().filter(request, chain);
        }
        if (file.isDirectory()) {
            if (!fpath.endsWith("/")) {
                HttpResponse response = new HttpResponse();
                response.redirect(fpath + "/");
                return response;
            }

            List<String> list = new ArrayList<String>();
            if (!fpath.equals("/")) {
                list.add("<a href=\"/../\">..</a> " + (file.isDirectory() ? "&lt;DIR&gt;" : "&lt;FILE&gt;"));
            }
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().startsWith(".")) {
                    continue;
                }
                list.add("<a href=\"" + fpath + f.getName() + "\">" + f.getName() + "</a> "
                         + (f.isDirectory() ? "&lt;DIR&gt;" : (f.length() + " bytes")));
            }

            return makeHttpResponse(("<html><body><h1>Listing " + fpath + ":</h1>" + StringUtil.join(list, "<br>") + "</body></html>").getBytes("UTF-8"),
                                    "text/html");
        }

        String contentType = ContentTypes.getContentTypeByExt(StringUtil.substringAfterLast(fpath, "."));
        byte[] bytes = FileUtil.readFileToBytes(file);
        return makeHttpResponse(bytes, contentType);
    }

    private HttpResponse makeHttpResponse(byte[] bytes, String contentType) {
        HttpResponse response = new HttpResponse();
        response.setContentType(contentType);
        response.setStatus(HttpConstants.STATUS_SUCCESS);
        response.setStatusMessage("OK");
        response.getHeaderMap().set(ContentTypes.CONTENT_TYPE, contentType);
        response.setBytes(bytes);
        return response;
    }
}
