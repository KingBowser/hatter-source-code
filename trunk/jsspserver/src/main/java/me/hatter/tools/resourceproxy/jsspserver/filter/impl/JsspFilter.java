package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.commons.resource.Resources;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.JsspReader;
import me.hatter.tools.resourceproxy.jsspexec.jsspreader.ExplainedJsspReader;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.action.Action;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;
import me.hatter.tools.resourceproxy.jsspserver.util.JsspResource;
import me.hatter.tools.resourceproxy.jsspserver.util.JsspResourceManager;

public class JsspFilter implements ResourceFilter {

    public static final boolean JSSP_DEBUG = Boolean.valueOf(System.getProperty("jsspdebug"));

    public static File          JSSP_PATH;
    static {
        String jsspPath = System.getProperty("jssp.path");
        if (jsspPath == null) {
            JSSP_PATH = new File(System.getProperty("user.dir"), "jssp");
        } else {
            JSSP_PATH = new File(jsspPath);
        }
    }
    public static Resources     RESOURCES  = new Resources(JSSP_PATH);

    static {
        JsspExecutor.initJsspWork();
    }

    // @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String fpath = request.getFPath();
        if (fpath.toLowerCase().endsWith(".jssp")) {
            JsspResource jsspResource = JsspResourceManager.getJsspResource(getResource(fpath));

            JsspReader jsspReader = new ExplainedJsspReader() {

                // @Override
                public String readExplained(String path) {
                    Resource resource = getResource(path);
                    if (!resource.exists()) {
                        throw new RuntimeException("Resource not found: " + path);
                    }
                    JsspResource jsspResource = JsspResourceManager.getJsspResource(resource);
                    return jsspResource.getExplainedContent(JSSP_DEBUG);
                }
            };

            if (jsspResource.exists()) {
                System.out.println("[INFO] Found jssp resource: " + jsspResource.getResource());

                HttpResponse response = new HttpResponse();
                Map<String, Object> context = new HashMap<String, Object>();
                String jsspAction = request.getQueryValue(Action.JSSP_ACTION);
                if (jsspAction != null) {
                    try {
                        Class<?> jsspActionClazz = Class.forName(jsspAction);
                        System.out.println("[INFO] Found jssp action: " + jsspActionClazz);
                        if (Action.class.isAssignableFrom(jsspActionClazz)) {
                            Action a = ((Action) jsspActionClazz.newInstance());
                            context = a.doAction(request, response);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (response.isFinish()) {
                    return response;
                }
                if (response.isRedirect()) {
                    return response;
                }

                BufferWriter bw = new BufferWriter();
                Map<String, Object> addContext = new HashMap<String, Object>();
                addContext.put("request", request);
                JsspExecutor.executeExplained(new StringReader(jsspResource.getExplainedContent(JSSP_DEBUG)), context,
                                              addContext, jsspReader, bw, jsspResource.getResource());

                response.setContentType(ContentTypes.HTML_CONTENT_TYPE);
                response.setCharset(ContentTypes.UTF8_CHARSET);
                response.setStatus(HttpConstants.STATUS_SUCCESS);
                response.setStatusMessage("OK");
                response.getHeaderMap().set(ContentTypes.CONTENT_TYPE, ContentTypes.HTML_AND_UTF8);
                response.setString(bw.getBufferedString());
                return response;
            } else {
                return Default404Filter.INSTANCE.filter(request, chain);
            }
        } else {
            return chain.next().filter(request, chain);
        }
    }

    public static Resource getResource(String fpath) {
        return RESOURCES.findResource(Resources.RESOURCE + dealFPath(fpath));
    }

    public static String dealFPath(String fpath) {
        if (fpath == null) {
            return null;
        }
        if (!Resources.isDefClasspath()) {
            return fpath;
        }
        fpath = fpath.replace('\\', '/').trim();
        while (fpath.startsWith("/")) {
            fpath = fpath.substring(1);
        }
        return fpath;
    }
}
