package me.hatter.tools.bigpom;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.misc.Base64;
import me.hatter.tools.commons.string.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SvnCheck {

    private static final String U = "aHR0cDovL2FvbmUuYWxpYmFiYS1pbmMuY29tL3BlcnRoL2V4dGVybmFsYXBpL2V4dGVybmFsYXBpLmpzcD9hY3Rpb249cHJvamVjdERldGFpbEJ5Q1JJRDRDSUMmQ1JJRD0=";

    public static void main(String[] args) throws Exception {
        if (UnixArgsutil.ARGS.args().length < 1) {
            System.out.println("crid not assigned");
            return;
        }
        String u = new String(Base64.base64ToByteArray(U), IOUtil.CHARSET_UTF8);
        u = u + UnixArgsutil.ARGS.args()[0];
        URL url = new URL(u);
        String response = IOUtil.readToStringAndClose(url.openStream());
        Object o = JSON.parse(response);
        JSONArray ja = (JSONArray) o;
        JSONObject jo = (JSONObject) ja.get(0);
        String currentBranch = jo.getString("currentBranch");
        String[] branches = currentBranch.split(",");

        List<String> rms = new ArrayList<String>();
// TODO

        List<String> cmds = new ArrayList<String>();
        for (String b : branches) {
            String src = b;
            if (b.matches(".*/intl-[a-zA-Z0-9\\-]+/branches/.*")) {
                System.out.println("Skip: " + b);
                continue;
            }
            for (String rm : rms) {
                b = b.replace(rm, "");
            }
            b = b.replaceAll("branches/.*", "");
            b = b.replace("/", "_");
            while (b.startsWith("_")) {
                b = b.substring(1);
            }
            while (b.endsWith("_")) {
                b = b.substring(0, b.length() - 1);
            }
            cmds.add("if [ ! -d \"" + b + "\" ]; then");
            cmds.add("  svn co " + src + " " + b);
            cmds.add("else");
            cmds.add("  cd " + b);
            cmds.add("  svn sw " + src);
            cmds.add("  svn up");
            cmds.add("  cd ..");
            cmds.add("fi");
            cmds.add("");
        }
        FileUtil.writeStringToFile(new File("checksvn"), StringUtil.join(cmds, "\n"));
        new ProcessBuilder(Arrays.asList("chmod", "+x", "checksvn")).start();
        System.out.println("Finish!");
    }
}
