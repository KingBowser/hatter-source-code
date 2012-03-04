package me.hatter.tools.resourceproxy.jsspserver.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.action.Action;

public class JsspServer {

    public static enum JsspResult {
        SUCCESS, NOT_FOUND;
    }

    private static File JSSP_PATH;
    static {
        String jsspPath = System.getProperty("jssp.path");
        if (jsspPath == null) {
            JSSP_PATH = new File(System.getProperty("user.dir"), "jssp");
        } else {
            JSSP_PATH = new File(jsspPath);
        }
    }

    public static JsspResult process(HttpRequest request, HttpResponse response) {
        String path = request.getUri().getPath();
        System.out.println("[INFO] Query jssp page: " + path);
        if (path.toLowerCase().endsWith(".jssp")) {
            return JsspResult.NOT_FOUND;
        }

        File jsspFile = new File(JSSP_PATH, path);
        if (!jsspFile.exists()) {
            System.out.println("[WARN] Jssp file not found: " + jsspFile);
            return JsspResult.NOT_FOUND;
        }

        Map<String, Object> context = new HashMap<String, Object>();
        String jsspAction = request.getQueryValue("jsspaction");
        if (jsspAction != null) {
            try {
                Class<?> jsspActionClazz = Class.forName(jsspAction);
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
        JsspExecutor.executeJssp(jsspFile, context, addContext, bw);

        response.setContentType("text/html");
        response.setCharset("UTF-8");
        response.setStatus(200);
        response.setStatusMessage("OK");
        response.setString(bw.getBufferedString());

        return JsspResult.SUCCESS;
    }
}
