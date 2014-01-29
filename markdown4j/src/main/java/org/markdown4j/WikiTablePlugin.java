package org.markdown4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * || TH || TH ||
 * | TD | TD |
 * </pre>
 * 
 * @author hatterjiang
 */
public class WikiTablePlugin extends Plugin {

    public WikiTablePlugin() {
        super("wikiTable");
    }

    @Override
    public void emit(StringBuilder out, List<String> lines, Map<String, String> params,
                     final EmitterCallback emitterCallback) {
        out.append("<table");
        out.append(" class=\"table table-bordered\"");
        out.append(">");

        List<String> nLines = preProcessLines(lines);

        for (String nline : nLines) {
            nline = nline.trim();
            if (nline.isEmpty()) {
                continue;
            }

            out.append("<tr>");
            if (!nline.startsWith("|")) {
                nline = "|" + nline;
            }
            if (!nline.endsWith("|")) {
                nline = nline + "|";
            }

            boolean isTh = false;
            StringBuilder cont = new StringBuilder();
            for (int i = 0; i < nline.length(); i++) {
                char c = nline.charAt(i);
                char nc = ((i + 1) < nline.length()) ? nline.charAt(i + 1) : (char) 0;

                if (c == '|') {
                    if (cont.length() > 0) {
                        out.append(isTh ? "<th>" : "<td>");
                        emitterCallback.recursiveEmitLineNone(out, cont.toString());
                        out.append(isTh ? "</th>" : "</td>");
                        cont.delete(0, cont.length());
                    }
                    isTh = false;
                    if (nc == '|') {
                        isTh = true;
                        i++; // skip nc
                    }
                } else {
                    cont.append(c);
                }
            }
            out.append("</tr>\n");
        }

        out.append("</table>");
    }

    private List<String> preProcessLines(List<String> lines) {
        List<String> nLines = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                nLines.add(lines.get(i));
            } else {
                String lastLine = lines.get(i - 1);
                if (lastLine.endsWith("\\") && (!lastLine.endsWith("\\\\"))) {
                    int lastLineNoOfNL = nLines.size() - 1;
                    String lastLineOfNL = nLines.get(lastLineNoOfNL);
                    nLines.set(lastLineNoOfNL, lastLineOfNL.substring(0, lastLineOfNL.length() - 1) + lines.get(i));
                } else {
                    nLines.add(lines.get(i));
                }
            }
        }
        return nLines;
    }
}
