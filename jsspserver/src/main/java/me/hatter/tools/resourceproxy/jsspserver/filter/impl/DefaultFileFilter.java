package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.ResourceCacheManager;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class DefaultFileFilter implements ResourceFilter {

    private static final LogTool logTool = LogTools.getLogTool(DefaultFileFilter.class);

    // @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String fpath = request.getFPath();
        if (fpath.equals("/")) {
            return chain.next().filter(request, chain);
        }
        Resource resource = JsspFilter.getResource(fpath);
        AtomicBoolean isFromCache = new AtomicBoolean(false);
        byte[] bytes = ResourceCacheManager.readCacheFile(resource, isFromCache);
        if (bytes != null) {
            int indexOfLastDot = fpath.lastIndexOf('.');
            String tfileExt = (indexOfLastDot < 0) ? fpath : fpath.substring(indexOfLastDot + 1);
            String contentType = ContentTypes.getContentTypeByExt(tfileExt);
            if (logTool.isInfoEnable()) {
                logTool.info("Found resource: " + resource + ", content-type: " + contentType + ", size: "
                             + bytes.length + ", cache: " + isFromCache);
            }
            HttpResponse response = new HttpResponse();
            response.setContentType(contentType);
            response.setStatus(HttpConstants.STATUS_SUCCESS);
            response.setStatusMessage("OK");
            response.getHeaderMap().set(ContentTypes.CONTENT_TYPE, contentType);
            response.setBytes(bytes);
            return response;
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
