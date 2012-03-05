package me.hatter.tools.resourceproxy.jsspserver.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.FileUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.action.Action;

public class JsspServer {

    public static enum JsspResult {
        SUCCESS, NOT_FOUND;
    }

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

    public static JsspResult process(HttpRequest request, HttpResponse response) {
        String fpath = request.getFPath();
        System.out.println("[INFO] Query local/ip page: " + fpath + " of: " + request.getUri().getPath());
        if ("/".equals(fpath)) {
            System.out.println("[INFO] Redirect: index.jssp?jsspaction=resourceproxy.Index");
            response.redirect("index.jssp?jsspaction=resourceproxy.Index");
            return null;
        }

        if (fpath.toLowerCase().endsWith(".png")) {
            File tfile = new File(JSSP_PATH, fpath);
            if (!tfile.exists()) {
                System.out.println("[WARN] File not found: " + tfile);
                return JsspResult.NOT_FOUND;
            }
            System.out.println("[INFO] Found file: " + tfile);
            response.setContentType("image/png");
            response.setStatus(200);
            response.setStatusMessage("OK");
            response.set("Content-Type", new ArrayList<String>(Arrays.asList("image/png")));
            response.setBytes(FileUtil.readFileToBytes(tfile));
            return null;
        }

        if (!fpath.toLowerCase().endsWith(".jssp")) {
            return JsspResult.NOT_FOUND;
        }

        File jsspFile = new File(JSSP_PATH, fpath);
        if (!jsspFile.exists()) {
            System.out.println("[WARN] Jssp file not found: " + jsspFile);
            return JsspResult.NOT_FOUND;
        }
        System.out.println("[INFO] Found jssp file: " + jsspFile);

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
        JsspExecutor.executeJssp(jsspFile, context, addContext, bw);

        response.setContentType("text/html");
        response.setCharset("UTF-8");
        response.setStatus(200);
        response.setStatusMessage("OK");
        response.set("Content-Type", new ArrayList<String>(Arrays.asList("text/html;charset=UTF-8")));
        response.setString(bw.getBufferedString());

        return JsspResult.SUCCESS;
    }
}
