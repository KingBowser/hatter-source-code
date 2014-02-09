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
        boolean replaceLeading_ = Arrays.asList("1", "on", "true", "yes").contains(getLowerValue(params, "rl"));
        out.append("<pre");
        out.append(" class=\"prettyprint");
        if (Arrays.asList("1", "on", "true", "yes").contains(getLowerValue(params, "ln", "line", "linenum", "linenums"))) {
            out.append(" linenums");
        }
        out.append("\"");
        out.append("><code>");
        for (final String s : lines) {
            boolean isAll_ = true;
            for (int i = 0; i < s.length(); i++) {
                final char c = s.charAt(i);
                switch (c) {
                    case '&':
                        isAll_ = false;
                        out.append("&amp;");
                        break;
                    case '<':
                        isAll_ = false;
                        out.append("&lt;");
                        break;
                    case '>':
                        isAll_ = false;
                        out.append("&gt;");
                        break;
                    case '_':
                        out.append((isAll_ && replaceLeading_) ? " " : c);
                        break;
                    default:
                        isAll_ = false;
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
