package me.hatter.tools.taskprocess.util.dump;

import me.hatter.tools.taskprocess.util.dump.annotation.DumpOptions;

public abstract class AbstractAnnotationDumpDataProcess extends AbstractDumpDataProcess {

    protected String getDriver() {
        return getOptions().driver();
    }

    protected String getUrl() {
        return getOptions().url();
    }

    protected String getUsername() {
        return getOptions().username();
    }

    protected String getPassword() {
        return getOptions().password();
    }

    protected DumpOptions getOptions() {
        DumpOptions options = this.getClass().getAnnotation(DumpOptions.class);
        if (options == null) {
            throw new RuntimeException("Dump options is not assigned.");
        }
        return options;
    }
}
