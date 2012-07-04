package me.hatter.tools.jtop.agent;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Pattern;

public class AgentInitialization {

    private static final Pattern JAR_REGEX = Pattern.compile(".*jtop[-.\\w]*.jar");

    public void initializeAccordingToJDKVersion() {
        String jarFilePath = findPathToJarFileFromClasspath();

        if (Agent.jdk6OrLater) {
            new JDK6AgentLoader(jarFilePath).loadAgent();
        } else if ("1.5".equals(Agent.javaSpecVersion)) {
            throw new IllegalStateException(
                                            "JTop has not been initialized. Check that your Java 5 VM has been started "
                                                    + "with the -javaagent:" + jarFilePath + " command line option.");
        } else {
            throw new IllegalStateException("JTop requires a Java 5 VM or later.");
        }
    }

    public String findPathToJarFileFromClasspath() {
        URLClassLoader urlc = (URLClassLoader) AgentInitialization.class.getClassLoader();
        URL[] urls = urlc.getURLs();
        for (URL u : urls) {
            String f = u.getFile();
            if (JAR_REGEX.matcher(f).matches()) {
                return f;
            }
        }
        throw new IllegalStateException(
                                        "No jar file with name ending in \"jtop.jar\" or \"jtop-nnn.jar\" (where \"nnn\" "
                                                + "is a version number) found in the classpath");
    }
}
