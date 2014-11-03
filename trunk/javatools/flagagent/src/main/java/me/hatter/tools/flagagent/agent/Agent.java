package me.hatter.tools.flagagent.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

import me.hatter.tools.flagagent.management.HotSpotFlag;

public class Agent {

    public static final String     HOTSPOT_FLAG_MXBEAN_PROPERTY_KEY = "me.hatter.management.init.HotSpotFlag";

    private static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        if (instrumentation != null) {
            System.out.println("[WARN] Areadly invoked: premain");
            return;
        }
        invokemain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        if (instrumentation != null) {
            System.out.println("[WARN] Areadly invoked: agentmain");
            return;
        }
        instrumentation = inst;
        invokemain(agentArgs, inst);
    }

    public static void invokemain(String agentArgs, Instrumentation inst) throws Exception {
        instrumentation = inst;
        System.out.println("void " + Agent.class.getName() + "#invokemain(String, Instrumentation), with: " + agentArgs);
        if (System.getProperty(HOTSPOT_FLAG_MXBEAN_PROPERTY_KEY) == null) {
            final File tempFlagManagement = File.createTempFile("flagmanagement", ".jar");
            tempFlagManagement.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempFlagManagement);
            copy(Agent.class.getResourceAsStream("/flagmanagement.jar"), fos);
            fos.close();
            System.out.println("[INFO] Append to bootstrap class loader search: " + tempFlagManagement);
            instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(tempFlagManagement));
            callRegisterMXBean();
            System.setProperty(HOTSPOT_FLAG_MXBEAN_PROPERTY_KEY, Boolean.TRUE.toString());
        }
    }

    private static long copy(InputStream is, OutputStream os) throws IOException {
        long total = 0;
        for (int b = 0; ((b = is.read()) != -1);) {
            os.write(b);
            total++;
        }
        return total;
    }

    private static void callRegisterMXBean() {
        HotSpotFlag.registerMXBean();
    }
}
