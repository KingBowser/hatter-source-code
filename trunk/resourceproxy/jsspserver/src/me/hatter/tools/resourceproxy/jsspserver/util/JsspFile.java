package me.hatter.tools.resourceproxy.jsspserver.util;

import java.io.File;

import me.hatter.tools.resourceproxy.commons.util.FileUtil;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;

public class JsspFile {

    private File    file;
    private Boolean exists = null;
    private File    explainedFile;
    private String  explainedContent;

    synchronized public boolean exists() {
        if (exists == null) {
            exists = file.exists();
        }
        return exists.booleanValue();
    }

    public JsspFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getExplainedFile() {
        return explainedFile;
    }

    public void setExplainedFile(File explainedFile) {
        this.explainedFile = explainedFile;
    }

    public String getExplainedContent() {
        return getExplainedContent(false);
    }

    synchronized public String getExplainedContent(boolean update) {
        if (update) {
            explainedFile = null;
            explainedContent = null;
        }
        if (explainedContent == null) {
            if (explainedFile == null) {
                explainedFile = JsspExecutor.tryExplainJssp(file);
            }
            explainedContent = FileUtil.readFileToString(explainedFile, ContentTypes.UTF8_CHARSET);
        }
        return explainedContent;
    }
}
