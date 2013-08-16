package me.hatter.tests.imap;

import java.io.File;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.string.StringUtil;

public class EmailAccount {

    public static final String USERNAME  = StringUtil.trim(FileUtil.readFileToString(new File(Environment.USER_HOME,
                                                                                              "robotusername.txt")));
    public static final String PASSWORD  = StringUtil.trim(FileUtil.readFileToString(new File(Environment.USER_HOME,
                                                                                              "robotpassword.txt")));

    public String              urlServer = "gmail.com";
    public String              username  = "";
    public String              password  = "";
    public String              emailAddress;

    public EmailAccount(String username, String password, String urlServer) {
        this.username = username;
        this.password = password;
        this.urlServer = urlServer;
        this.emailAddress = username + "@" + urlServer;
    }
}
