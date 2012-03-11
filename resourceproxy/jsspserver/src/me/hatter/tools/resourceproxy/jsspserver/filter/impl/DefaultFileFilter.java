package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.io.File;

import me.hatter.tools.resourceproxy.commons.util.FileUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class DefaultFileFilter implements ResourceFilter {

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String fpath = request.getFPath();
        File tfile = new File(JsspFilter.JSSP_PATH, fpath);
        if ((!fpath.equals("/")) && tfile.exists()) {
            int indexOfLastDot = fpath.lastIndexOf('.');
            String tfileExt = (indexOfLastDot < 0) ? fpath : fpath.substring(indexOfLastDot + 1);
            String contentType = ContentTypes.getContentTypeByExt(tfileExt);
            System.out.println("[INFO] Found file: " + tfile + ", content-type: " + contentType);
            HttpResponse response = new HttpResponse();
            response.setContentType(contentType);
            response.setStatus(HttpConstants.STATUS_SUCCESS);
            response.setStatusMessage("OK");
            response.getHeaderMap().set(ContentTypes.CONTENT_TYPE, contentType);
            response.setBytes(FileUtil.readFileToBytes(tfile));
            return response;
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
