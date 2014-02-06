package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.util.Calendar;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpDate;

public class CacheImgFilter implements ResourceFilter {

    private static int DEFAULT_CACHE_MINUTE = Integer.parseInt(System.getProperty("cache.minute", "5"));

    // @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        HttpResponse response = chain.next().filter(request, chain);
        String fpath = request.getFPath().trim().toLowerCase();
        int indexOfLastDot = fpath.lastIndexOf('.');
        String tfileExt = (indexOfLastDot < 0) ? fpath : fpath.substring(indexOfLastDot + 1);
        String contentType = ContentTypes.getContentTypeByExt(tfileExt);
        if ((contentType != null) && (contentType.toLowerCase().startsWith("image/"))) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, DEFAULT_CACHE_MINUTE);
            response.getHeaderMap().set("Expires", HttpDate.formatDate(cal.getTime()));
        }
        return response;
    }
}
