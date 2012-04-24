package me.hatter.tools.resourceproxy.jsspexec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import me.hatter.tools.resourceproxy.commons.resource.FileResource;
import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.commons.util.IOUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.jsspexec.exception.JsspEvalException;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;

public class JsspExecutor {

    public static String                     DEFAULT_CHARSET      = "UTF-8";
    public static String                     JSSP_EXPLAINED_EXT   = ".jssp_explain";
    private static final String              JSSP_SCRIPT_LANGUAGE = "JavaScript";
    private static final ScriptEngineManager JSSP_ENG_MAN         = new ScriptEngineManager();

    public static void main(String[] a) throws Exception {
        initJsspWork();
        Map<String, Object> context = new HashMap<String, Object>();
        BufferWriter bw = new BufferWriter();
        File f = File.createTempFile("test", "jssp");
        f.deleteOnExit();
        FileWriter fw = new FileWriter(f);
        fw.write("<%=\"hello world\"%>\n<% var i = 0; i++;%><%=i%>");
        fw.flush();
        fw.close();

        executeJssp(new FileResource(f), context, null, bw);

        System.out.println(bw.getBufferedString());
    }

    public static void initJsspWork() {
        File work = getJsspWorkDir();
        if (!(work.exists() && work.isDirectory())) {
            work.mkdirs();
        }
    }

    public static void executeJssp(Resource resource, Map<String, Object> context, Map<String, Object> addContext,
                                   BufferWriter out) {
        executeExplained(new StringReader(explainAndReadJssp(resource)), context, addContext, out);
    }

    public static void executeExplained(Reader reader, Map<String, Object> context, Map<String, Object> addContext,
                                        BufferWriter out) {
        ScriptEngine se = JSSP_ENG_MAN.getEngineByName(JSSP_SCRIPT_LANGUAGE);

        Bindings b = new SimpleBindings();
        // variants (Java -> JSSP)
        if (addContext != null) {
            for (String key : addContext.keySet()) {
                b.put(key, addContext.get(key));
            }
        }
        b.put("stringUtil", StringUtil.INSTANCE);
        b.put("app_context", context);
        b.put("out", out);

        try {
            se.eval(IOUtil.readToString(reader), b);
        } catch (ScriptException e) {
            throw new JsspEvalException(e);
        }
        out.flush();
    }

    public static String explainAndReadJssp(Resource resource) {
        try {
            Resource explained = tryExplainJssp(resource);
            InputStream fis = explained.openInputStream();
            InputStreamReader isr = new InputStreamReader(fis, DEFAULT_CHARSET);
            try {
                return IOUtil.readToString(isr);
            } finally {
                IOUtil.closeQuitely(isr);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Resource tryExplainJssp(Resource resource) {
        try {
            return explainJssp(resource);
        } catch (IOException e) {
            throw new JsspEvalException("Error occured in explain jssp resource: " + resource, e);
        }
    }

    public static Resource explainJssp(Resource resource) throws IOException {
        File explainedFile = getExplainedFile(resource);
        if (explainedFile.exists()) {
            if (explainedFile.lastModified() > resource.lastModified()) {
                return new FileResource(explainedFile);
            }
        }
        InputStream fis = resource.openInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, DEFAULT_CHARSET));
        try {
            PrintWriter pw = new PrintWriter(explainedFile, DEFAULT_CHARSET);
            // support return
            pw.write("(function() {\r\n");
            StringBuilder sb = new StringBuilder();
            boolean inScript = false;
            boolean isStart = false;
            boolean isEnd = false;
            try {
                for (String readLine; ((readLine = br.readLine()) != null);) {
                    String line = readLine + "\\r\\n";

                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        if (inScript) {
                            if ((c == '%') && (i < (line.length() - 1)) && (line.charAt(i + 1) == '>')) {
                                isStart = false;
                                isEnd = true;
                                inScript = false;
                                i += 1;
                            } else {
                                isStart = false;
                                isEnd = false;
                            }
                        } else {
                            if ((c == '<') && (i < (line.length() - 1)) && (line.charAt(i + 1) == '%')) {
                                isStart = true;
                                isEnd = false;
                                inScript = true;
                                i += 1;
                            } else {
                                isStart = false;
                                isEnd = false;
                            }
                        }

                        boolean process = false;

                        if (isStart || isEnd) {
                            process = true;
                        } else {
                            sb.append(c);
                            if (i == (line.length() - 1)) {
                                process = true;
                            }
                        }

                        if (process) {
                            String sbs = sb.toString();
                            sb = new StringBuilder();
                            if (isStart) {
                                if (sbs.length() > 0) {
                                    explainPlain(pw, sbs);
                                }
                            } else if (isEnd) {
                                if (sbs.length() > 0) {
                                    explainScript(pw, sbs);
                                }
                            } else if (i == (line.length() - 1)) {
                                if (!inScript) {
                                    explainPlain(pw, sbs);
                                } else {
                                    explainScript(pw, sbs);
                                }
                            } else if (line.length() == 0) {
                                if (!inScript) {
                                    explainPlain(pw, sbs);
                                } else {
                                    explainScript(pw, sbs);
                                }
                            }
                        }
                    }
                }
                pw.write("})();\r\n");
                pw.flush();
            } finally {
                pw.close();
            }
        } finally {
            IOUtil.closeQuitely(fis);
        }

        return new FileResource(explainedFile);
    }

    private static final File getExplainedFile(Resource resource) {
        String path = resource.getResId() + JSSP_EXPLAINED_EXT;
        path = path.replaceAll("[:\\\\/]", "_");
        return new File(getJsspWorkDir(), path);
    }

    private static final void explainPlain(Writer pw, String plain) throws IOException {
        if (plain.endsWith("\\r\\n")) {
            plain = plain.substring(0, plain.length() - "\\r\\n".length()).replace("\\", "\\\\") + "\\r\\n";
        }
        plain = plain.replace("\"", "\\\"");
        plain = "out.write(\"" + plain + "\");";
        pw.write(plain);
        pw.write("\r\n");
    }

    private static final void explainScript(Writer pw, String script) throws IOException {
        if (script.endsWith("\\r\\n")) {
            script = script.substring(0, (script.length() - "\\r\\n".length()));
        }
        if (script.length() == 0) {
            return;
        }
        if (script.trim().startsWith("=")) {
            int equal = script.indexOf('=');
            String expression = script.substring(equal + 1);
            pw.write("out.writeEscape(");
            pw.write(expression);
            pw.write(");");
        } else if (script.trim().startsWith("!=")) {
            int equal = script.indexOf('!');
            String expression = script.substring(equal + 2);
            pw.write("out.write(");
            pw.write(expression);
            pw.write(");");
        } else if (script.trim().startsWith("#=")) {
            int equal = script.indexOf('#');
            String expression = script.substring(equal + 2);
            pw.write("out.writePreText(");
            pw.write(expression);
            pw.write(");");
        } else {
            pw.write(script);
        }
        pw.write("\r\n");
    }

    private static File getJsspWorkDir() {
        File work = new File(System.getProperty("user.dir"), "_jssp_work_dir");
        return work;
    }
}
