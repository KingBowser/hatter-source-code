package org.markdown4j;

import java.util.List;
import java.util.Map;

public abstract class Plugin {

    // Add by Hatter Jiang
    public static interface EmitterCallback {

        int recursiveEmitLineNone(final StringBuilder out, final String in);
    }

    protected String idPlugin;

    public Plugin(String idPlugin) {
        this.idPlugin = idPlugin;
    }

    public abstract void emit(final StringBuilder out, final List<String> lines, final Map<String, String> params, final EmitterCallback emitterCallback);

    public String getIdPlugin() {
        return idPlugin;
    }
}
