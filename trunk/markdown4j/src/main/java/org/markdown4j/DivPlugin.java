package org.markdown4j;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author hatterjiang
 */
public class DivPlugin extends Plugin {

    public DivPlugin() {
        super("div");
    }

    @Override
    public void emit(StringBuilder out, List<String> lines, Map<String, String> params,
                     final EmitterCallback emitterCallback) {
        out.append("<div");
        if (params != null) {
            for (Entry<String, String> entry : params.entrySet()) {
                out.append(" " + entry.getKey() + "=\"" + entry.getValue().replace("___", " ") + "\"");
            }
        }
        out.append(">\n");

        StringBuilder sb = new StringBuilder();
        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                sb.append(lines.get(i));
                if (i < (lines.size() - 1)) {
                    sb.append("\n");
                }
            }
        }

        emitterCallback.recursiveEmitLineNone(out, sb.toString());

        out.append("</div>");
    }
}
