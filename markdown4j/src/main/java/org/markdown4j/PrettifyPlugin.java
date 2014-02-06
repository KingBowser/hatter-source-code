package org.markdown4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * https://code.google.com/p/google-code-prettify/
 * 
 * @author hatterjiang
 */
public class PrettifyPlugin extends Plugin {

    public PrettifyPlugin() {
        super("prettify");
    }

    @Override
    public void emit(StringBuilder out, List<String> lines, Map<String, String> params,
                     final EmitterCallback emitterCallback) {
        out.append("<pre");
        out.append(" class=\"prettyprint");
        if (Arrays.asList("1", "on", "true", "yes").contains(getLowerValue(params, "ln", "line", "linenum", "linenums"))) {
            out.append(" linenums");
        }
        out.append("\"");
        out.append("><code>");
        for (final String s : lines) {
            for (int i = 0; i < s.length(); i++) {
                final char c = s.charAt(i);
                switch (c) {
                    case '&':
                        out.append("&amp;");
                        break;
                    case '<':
                        out.append("&lt;");
                        break;
                    case '>':
                        out.append("&gt;");
                        break;
                    default:
                        out.append(c);
                        break;
                }
            }
            out.append('\n');
        }
        out.append("</code></pre>\n");
    }

    public String getLowerValue(Map<String, String> params, String... keys) {
        for (String key : keys) {
            String value = params.get(key);
            if (value != null) {
                return value.trim().toLowerCase();
            }
        }
        return null;
    }
}
