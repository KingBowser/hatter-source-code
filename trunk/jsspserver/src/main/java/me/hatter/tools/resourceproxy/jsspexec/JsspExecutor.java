package me.hatter.tools.resourceproxy.jsspexec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.resource.Resource;
import me.hatter.tools.commons.resource.impl.FileResource;
import me.hatter.tools.commons.resource.impl.TextResource;
import me.hatter.tools.resourceproxy.commons.util.JavaUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.jsspexec.exception.JsspEvalException;
import me.hatter.tools.resourceproxy.jsspexec.jsspreader.SimpleJsspReader;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspexec.utl.LineNumberWriter;

public class JsspExecutor {

    private static final LogTool             logTool              = LogTools.getLogTool(JsspExecutor.class);

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
        fw.write("<%=\"hello world\"%>\n<% var i = 0; i++;%>\r\n<%=i%>\r\n <%=control.jssp(\"\").param(\"num\", 111)%>");
        fw.flush();
        fw.close();

        JsspReader jr = new SimpleJsspReader() {

            // @Override
            public Resource readResource(String path) {
                return new TextResource("test 1234 |<%=app_context.get(\"num\")%>|", "res/test");
            }
        };

        executeJssp(new FileResource(f), context, null, jr, bw);

        logTool.info(bw.getBufferedString());
    }

    public static void initJsspWork() {
        File work = getJsspWorkDir();
        if (!(work.exists() && work.isDirectory())) {
            work.mkdirs();
        }
    }

    public static void executeJssp(Resource resource, Map<String, Object> context, Map<String, Object> addContext,
                                   BufferWriter out) {
        executeJssp(resource, context, addContext, null, out);
    }

    public static void executeJssp(Resource resource, Map<String, Object> context, Map<String, Object> addContext,
                                   JsspReader jsspReader, BufferWriter out) {
        executeExplained(new StringReader(explainAndReadJssp(resource)), context, addContext, jsspReader, out, resource);
    }

    public static void executeExplained(Reader reader, Map<String, Object> context, Map<String, Object> addContext,
                                        BufferWriter out, Resource source) {
        executeExplained(reader, context, addContext, null, out, source);
    }

    public static void executeExplained(Reader reader, Map<String, Object> context, Map<String, Object> addContext,
                                        JsspReader jsspReader, BufferWriter out, Resource source) {
        ScriptEngine se = JSSP_ENG_MAN.getEngineByName(JSSP_SCRIPT_LANGUAGE);

        Bindings b = new SimpleBindings();
        // variants (Java -> JSSP)
        if (addContext != null) {
            for (String key : addContext.keySet()) {
                b.put(key, addContext.get(key));
            }
        }
        if (jsspReader != null) {
            JsspControl control = new JsspControl(jsspReader, addContext);
            b.put("control", control);
        }
        b.put("stringUtil", StringUtil.INSTANCE);
        b.put("javaUtil", JavaUtil.INSTANCE);
        b.put("app_context", context);
        b.put("ac", context);
        b.put("out", out);

        String explainedSource = IOUtil.readToString(reader);
        try {
            se.eval(explainedSource, b);
        } catch (ScriptException e) {
            String msg = e.getMessage();
            Matcher m = Pattern.compile(".*at line number\\s+(\\d+).*").matcher(msg);
            if (!m.matches()) {
                throw new JsspEvalException(e);
            }
            int errorLineCount = Integer.parseInt(m.group(1));
            int lastIndexOfDoubleSlash = explainedSource.lastIndexOf("//");
            Pattern pstt = Pattern.compile("\\[\\s*(\\d+),\\s*(\\d+)\\s*\\]");
            if (lastIndexOfDoubleSlash > 0) {
                String sttml = explainedSource.substring(lastIndexOfDoubleSlash + 2).trim();
                String[] sttms = sttml.split(";");
                for (int i = (sttms.length - 1); i >= 0; i--) {
                    String stt = sttms[i].trim();
                    Matcher mm = pstt.matcher(stt);
                    if (mm.matches()) {
                        int tc = Integer.parseInt(mm.group(2));
                        if (errorLineCount >= tc) {
                            int sc = Integer.parseInt(mm.group(1));
                            int brln = 0;
                            String theSourceLine = null;
                            if (source != null) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(source.openInputStream()));
                                try {
                                    for (String ln; ((ln = br.readLine()) != null);) {
                                        if (brln == sc) {
                                            theSourceLine = ln;
                                            break;
                                        }
                                        brln++;
                                    }
                                } catch (IOException ex) {
                                    // IGNORE
                                }
                                IOUtil.closeQuietly(br);
                            }
                            throw new JsspEvalException("Jssp execute error at line: " + sc + ", the source is: "
                                                        + theSourceLine, e);
                        }
                    }
                }
            }
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
                IOUtil.closeQuietly(isr);
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
            LineNumberWriter pw = new LineNumberWriter(explainedFile, DEFAULT_CHARSET);
            // support return
            pw.write("(function() {\r\n");
            StringBuilder sb = new StringBuilder();
            boolean inScript = false;
            boolean isStart = false;
            boolean isEnd = false;
            try {
                List<String> sourceTargetLineMappingList = new ArrayList<String>();
                int sourceLine = 0;
                for (String readLine; ((readLine = br.readLine()) != null);) {
                    sourceTargetLineMappingList.add("[" + sourceLine + ", " + pw.getCurrentLine() + "]");

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
                    sourceLine++;
                }
                pw.write("})();\r\n");
                pw.write("// " + StringUtil.join(sourceTargetLineMappingList, ";") + "\r\n");
                pw.flush();
            } finally {
                pw.close();
            }
        } finally {
            IOUtil.closeQuietly(fis);
        }

        return new FileResource(explainedFile);
    }

    private static final File getExplainedFile(Resource resource) {
        String path = resource.getResId() + JSSP_EXPLAINED_EXT;
        path = path.replaceAll("[:\\\\/]", "_");
        return new File(getJsspWorkDir(), path);
    }

    private static final void explainPlain(LineNumberWriter pw, String plain) throws IOException {
        if (plain.endsWith("\\r\\n")) {
            plain = plain.substring(0, plain.length() - "\\r\\n".length()).replace("\\", "\\\\") + "\\r\\n";
        }
        plain = plain.replace("\"", "\\\"");
        plain = "out.write(\"" + plain + "\");";
        pw.write(plain);
        pw.write("\r\n");
    }

    private static final void explainScript(LineNumberWriter pw, String script) throws IOException {
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
        } else if (script.trim().startsWith("U=")) {
            int equal = script.indexOf('U');
            String expression = script.substring(equal + 2);
            pw.write("out.writeEncodeUrl(");
            pw.write(expression);
            pw.write(");");
        } else {
            pw.write(script);
        }
        pw.write("\r\n");
    }

    private static File __jsspWorkDir = null;

    synchronized private static File getJsspWorkDir() {
        if (__jsspWorkDir != null) {
            return __jsspWorkDir;
        }
        String tmpdir = System.getProperty("java.io.tmpdir");
        String jsspExplainDir = System.getProperty("jssp.explain");
        File jsspWorkPath = (jsspExplainDir == null) ? new File(tmpdir, "_jssp_work_dir") : new File(jsspExplainDir);
        __jsspWorkDir = new File(jsspWorkPath, getProcessSpecialId());
        if (logTool.isInfoEnable()) {
            logTool.info("JSSP work dir: " + __jsspWorkDir);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                if (logTool.isInfoEnable()) {
                    logTool.info("Clear JSSP work dir: " + __jsspWorkDir);
                }
                File[] files = __jsspWorkDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        f.delete();
                    }
                }
                __jsspWorkDir.delete();
            }
        });
        return __jsspWorkDir;
    }

    private static String getProcessSpecialId() {
        try {
            String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
            int p = nameOfRunningVM.indexOf('@');
            return nameOfRunningVM.substring(0, p);
        } catch (Exception e) {
            return String.valueOf(System.currentTimeMillis());
        }
    }
}
