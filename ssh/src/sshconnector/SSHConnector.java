package sshconnector;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSHConnector {

    public static class SSHConfig {

        private String username;
        private String password;
        private String host;
        private String port     = "7070";
        private String beat_sec = "20";
        private String beat_url = "http://www.google.com";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getBeat_sec() {
            return beat_sec;
        }

        public void setBeat_sec(String beat_sec) {
            this.beat_sec = beat_sec;
        }

        public String getBeat_url() {
            return beat_url;
        }

        public void setBeat_url(String beat_url) {
            this.beat_url = beat_url;
        }
    }

    private static final String    SSH_CONN = System.getProperty("user.home") + "/.ssh_conn_cmd";
    private static final SSHConfig CONFIG   = loadConfig();

    public static void main(String[] args) {
        updateSSHConn();
        doCheckAlive();
    }

    private static SSHConfig loadConfig() {

        List<String> fieldList = Arrays.asList("username", "password", "host", "port", "beat_url");

        File p = new File(System.getProperty("user.home") + "/ssh_setting.properties");
        if (!p.exists()) {
            System.err.println("File " + p + " cannot be found!");
            System.err.println("Add field(s) to the file: " + fieldList);
            System.exit(0);
        }
        SSHConfig config = new SSHConfig();
        Properties properties = new Properties();
        try {
            InputStream is = new FileInputStream(p);
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String f : fieldList) {
            String value = properties.getProperty(f);
            if (value != null) {
                try {
                    Field field = SSHConfig.class.getDeclaredField(f);
                    field.setAccessible(true);
                    field.set(config, value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return config;
    }

    private static void updateSSHConn() {
        String cmd = "#!/usr/bin/expect" + "\n" + // NL
                     "set timeout 60" + "\n" + // NL
                     "" + "\n" + // NL
                     "spawn /usr/bin/ssh -f -N " + // NL
                     CONFIG.getUsername() + "@" + CONFIG.getHost() + " -D " + CONFIG.getPort() + "" + "\n" + // NL
                     "expect {" + "\n" + // NL
                     "\"password:\" {" + "\n" + // NL
                     "send \"" + CONFIG.getPassword() + "\r\"" + "\n" + // NL
                     "}" + "\n" + // NL
                     "}" + "\n" + // NL
                     "interact {" + "\n" + // NL
                     "timeout 60 { send \"\"}" + "\n" + // NL
                     "}";
        File sshConn = new File(SSH_CONN);
        if (sshConn.exists()) {
            sshConn.delete();
        }
        try {
            Writer writer = new FileWriter(sshConn);
            writer.write(cmd);
            writer.flush();
            writer.close();
            log("Update " + SSH_CONN);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void doCheckAlive() {
        while (true) {
            try {
                try {
                    dealSshD();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(TimeUnit.SECONDS.toMillis(Integer.valueOf(CONFIG.getBeat_sec())));
            } catch (Exception e) {
                // IGNORE
            }
        }
    }

    private static void log(String message) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + ": " + message);
    }

    private static void dealSshD() {
        Integer sshdPid = findPidOfShhD();
        if (sshdPid != null) {
            if (!checkSshD()) {
                killPid(sshdPid);
                startSshD();
            }
        } else {
            startSshD();
        }
    }

    private static boolean checkSshD() {
        Proxy proxy = new Proxy(Type.SOCKS, new InetSocketAddress("127.0.0.1", Integer.valueOf(CONFIG.getPort())));
        try {
            URL url = new URL(CONFIG.getBeat_url());
            URLConnection conn = url.openConnection(proxy);
            if ((conn.getHeaderFields() == null) || (conn.getHeaderFields().isEmpty())) {
                throw new IOException("Open failed.");
            }
            log("Check SshD ok!");
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            // open google failed!
            log("Check SshD FAILED!");
            return false;
        }
    }

    private static void startSshD() {
        if (findPidOfShhD() == null) {
            String cmd = "/usr/bin/expect " + SSH_CONN;
            ProcessBuilder pb = new ProcessBuilder(cmd.split("\\s"));
            pb.directory(new File(System.getProperty("user.home")));
            log("Start SshD; " + getProcessOutput(pb));
        }
    }

    private static void killPid(Integer pid) {
        if (pid == null) {
            throw new NullPointerException("pid is null.");
        }
        String cmd = "kill -9 " + pid;
        ProcessBuilder pb = new ProcessBuilder(cmd.split("\\s"));
        log("Kill SshD; " + getProcessOutput(pb));
    }

    private static Integer findPidOfShhD() {
        String cmd = "ps -ax";
        ProcessBuilder pb = new ProcessBuilder(cmd.split("\\s"));
        String psAxOut = getProcessOutput(pb);
        BufferedReader br = new BufferedReader(new StringReader(psAxOut));
        Pattern p = Pattern.compile("\\s*(\\d+)\\s+.*" + CONFIG.getHost() + ".*D.*");
        try {
            for (String ln; ((ln = br.readLine()) != null);) {
                Matcher m = p.matcher(ln);
                if (m.matches()) {
                    log("Find SshD = " + m.group(1));
                    return Integer.valueOf(m.group(1));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log("Find SshD FAILED!");
        return null;
    }

    private static String getProcessOutput(ProcessBuilder processBuilder) {
        try {
            Process p = processBuilder.start();
            final InputStream is = p.getInputStream();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Thread it = new Thread() {

                public void run() {
                    int b;
                    try {
                        while ((b = is.read()) != -1) {
                            baos.write(b);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            it.start();
            p.waitFor();

            Thread.sleep(500);
            if (it.isAlive()) {
                it.interrupt();
            }

            return new String(baos.toByteArray(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
