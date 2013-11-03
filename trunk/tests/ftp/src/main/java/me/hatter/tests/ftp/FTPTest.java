package me.hatter.tests.ftp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

public final class FTPTest {

    public static void main(String[] args) throws UnknownHostException {
        String username = "caiwy";
        String password = "123";
        String server = "192.168.110.195";
        boolean error = false;

        final FTPClient ftp = new FTPClient();
        ftp.setCharset(Charset.forName("GBK"));
        ftp.setControlEncoding("GBK");
        ftp.setCopyStreamListener(createListener());
        ftp.setListHiddenFiles(true);

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

        try {
            int reply;
            ftp.connect(server);
            System.out.println("Connected to " + server + " on " + ftp.getDefaultPort());

            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            System.exit(1);
        }

        __main: try {
            if (!ftp.login(username, password)) {
                ftp.logout();
                error = true;
                break __main;
            }

            System.out.println("Remote system is " + ftp.getSystemType());

            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            // if (localActive) {
            // ftp.enterLocalActiveMode();
            // } else {
            ftp.enterLocalPassiveMode();
            // }

            ftp.setUseEPSVwithIPv4(true);

            ftp.cwd("/个税系列_1/测试用/个人所得税代扣代缴系统V1.2.150安装包_110/");

            System.out.println(">>>>>>>>>>>>>>>>>> list directories");
            FTPFile[] directories = ftp.listDirectories();
            // System.out.println(Arrays.asList(directories));

            System.out.println(">>>>>>>>>>>>>>>>>> list files");
            FTPFile[] files = ftp.listFiles();
            // System.out.println(Arrays.asList(files));

            System.out.println(">>>>>>>>>>>>>>>>>> list dirs");
            FTPFile[] dirs = ftp.mlistDir();
            // System.out.println(Arrays.asList(dirs));

            System.out.println(">>>>>>>>>>>>>>>>>> list names");
            String[] names = ftp.listNames();
            // System.out.println(Arrays.asList(names));

            System.out.println(">>>>>>>>>>>>>>>>>> do command: LIST");
            // ftp.setFileType(FTP.ASCII_FILE_TYPE);
//            ftp.pasv();
            boolean r = ftp.doCommand("LIST", "");
            // ftp.getReplyString();
            // r = ftp.doCommand("LIST", "");
            System.out.println(r);
            String[] strings = ftp.getReplyStrings();
            // System.out.println(Arrays.asList(strings));
            
            
            ftp.doCommand("HELP", null);

            // ftp.store

            // if (storeFile) {
            // InputStream input;
            //
            // input = new FileInputStream(local);
            //
            // ftp.storeFile(remote, input);
            //
            // input.close();
            // } else if (listFiles) {
            // if (lenient) {
            // FTPClientConfig config = new FTPClientConfig();
            // config.setLenientFutureDates(true);
            // ftp.configure(config);
            // }
            //
            // for (FTPFile f : ftp.listFiles(remote)) {
            // System.out.println(f.getRawListing());
            // System.out.println(f.toFormattedString());
            // }
            // } else if (mlsd) {
            // for (FTPFile f : ftp.mlistDir(remote)) {
            // System.out.println(f.getRawListing());
            // System.out.println(f.toFormattedString());
            // }
            // } else if (mlst) {
            // FTPFile f = ftp.mlistFile(remote);
            // if (f != null) {
            // System.out.println(f.toFormattedString());
            // }
            // } else if (listNames) {
            // for (String s : ftp.listNames(remote)) {
            // System.out.println(s);
            // }
            // } else if (feat) {
            // // boolean feature check
            // if (remote != null) { // See if the command is present
            // if (ftp.hasFeature(remote)) {
            // System.out.println("Has feature: " + remote);
            // } else {
            // if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            // System.out.println("FEAT " + remote + " was not detected");
            // } else {
            // System.out.println("Command failed: " + ftp.getReplyString());
            // }
            // }
            //
            // // Strings feature check
            // String[] features = ftp.featureValues(remote);
            // if (features != null) {
            // for (String f : features) {
            // System.out.println("FEAT " + remote + "=" + f + ".");
            // }
            // } else {
            // if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            // System.out.println("FEAT " + remote + " is not present");
            // } else {
            // System.out.println("Command failed: " + ftp.getReplyString());
            // }
            // }
            // } else {
            // if (ftp.features()) {
            // // Command listener has already printed the output
            // } else {
            // System.out.println("Failed: " + ftp.getReplyString());
            // }
            // }
            // } else if (doCommand != null) {
            // if (ftp.doCommand(doCommand, remote)) {
            // // Command listener has already printed the output
            // // for(String s : ftp.getReplyStrings()) {
            // // System.out.println(s);
            // // }
            // } else {
            // System.out.println("Failed: " + ftp.getReplyString());
            // }
            // } else {
            // OutputStream output;
            //
            // output = new FileOutputStream(local);
            //
            // ftp.retrieveFile(remote, output);
            //
            // output.close();
            // }

            ftp.noop(); // check that control connection is working OK

            ftp.logout();
        } catch (FTPConnectionClosedException e) {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }

        System.exit(error ? 1 : 0);
    } // end main

    private static CopyStreamListener createListener() {
        return new CopyStreamListener() {

            private long megsTotal = 0;

            // @Override
            public void bytesTransferred(CopyStreamEvent event) {
                bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
            }

            // @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                long megs = totalBytesTransferred / 1000000;
                for (long l = megsTotal; l < megs; l++) {
                    System.err.print("#");
                }
                megsTotal = megs;
            }
        };
    }
}
