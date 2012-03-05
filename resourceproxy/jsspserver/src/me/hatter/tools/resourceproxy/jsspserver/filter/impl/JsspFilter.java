package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.action.Action;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class JsspFilter implements ResourceFilter {

    public static File JSSP_PATH;
    static {
        String jsspPath = System.getProperty("jssp.path");
        if (jsspPath == null) {
            JSSP_PATH = new File(System.getProperty("user.dir"), "jssp");
        } else {
            JSSP_PATH = new File(jsspPath);
        }
    }

    static {
        JsspExecutor.initJsspWork();
    }

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String fpath = request.getFPath();
        if (fpath.toLowerCase().endsWith(".jssp")) {
            File tfile = new File(JSSP_PATH, fpath);
            if (tfile.exists()) {
                System.out.println("[INFO] Found jssp file: " + tfile);

                HttpResponse response = new HttpResponse();
                Map<String, Object> context = new HashMap<String, Object>();
                String jsspAction = request.getQueryValue("jsspaction");
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

                BufferWriter bw = new BufferWriter();
                Map<String, Object> addContext = new HashMap<String, Object>();
                addContext.put("request", request);
                JsspExecutor.executeJssp(tfile, context, addContext, bw);

                response.setContentType("text/html");
                response.setCharset("UTF-8");
                response.setStatus(200);
                response.setStatusMessage("OK");
                response.getHeaderMap().set("Content-Type", "text/html;charset=UTF-8");
                response.setString(bw.getBufferedString());
                return response;
            } else {
                return Default404Filter.INSTANCE.filter(request, chain);
            }
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
