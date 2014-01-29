package org.markdown4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        if ((params == null) || params.isEmpty()) {
            out.append(" class=\"table table-bordered\"");
        } else {
            for (Entry<String, String> entry : params.entrySet()) {
                out.append(" " + entry.getKey() + "=\"" + entry.getValue().replace("___", " ") + "\"");
            }
        }
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

            int colspan = 1;
            int rowspan = 1;
            boolean isTh = false;
            StringBuilder cont = new StringBuilder();
            for (int i = 0; i < nline.length(); i++) {
                char c = nline.charAt(i);
                char nc = ((i + 1) < nline.length()) ? nline.charAt(i + 1) : (char) 0;
                char nnc = ((i + 2) < nline.length()) ? nline.charAt(i + 2) : (char) 0;

                if (c == '|') {
                    if (cont.length() > 0) {
                        String _span = "";
                        if (colspan > 1) {
                            _span += " colspan=\"" + colspan + "\"";
                        }
                        if (rowspan > 1) {
                            _span += " rowspan=\"" + rowspan + "\"";
                        }
                        out.append(isTh ? "<th class=\"active\"" + _span + ">" : "<td" + _span + ">");
                        emitterCallback.recursiveEmitLineNone(out, cont.toString());
                        out.append(isTh ? "</th>" : "</td>");
                        cont.delete(0, cont.length());
                    }
                    rowspan = 1;
                    colspan = 1;
                    isTh = false;
                    if (nc == '|') {
                        isTh = true;
                        i++; // skip nc
                    }
                    if (((!isTh) && (nc == '*')) || (isTh && (nnc == '*'))) {
                        i += 2;
                        StringBuilder sb = new StringBuilder();
                        char _c = nline.charAt(i);
                        L09C: while ((_c >= '0' && _c <= '9') || (_c == ':')) {
                            sb.append(_c);
                            i++;
                            if (i >= nline.length()) {
                                break L09C;
                            }
                            _c = nline.charAt(i);
                        }
                        if (i < nline.length()) {
                            i--; // back 1 char
                        }
                        String _s = sb.toString();
                        // System.out.println("S:" + _s);
                        int indexOfC = _s.indexOf(":");
                        String cols = (indexOfC >= 0) ? _s.substring(0, indexOfC) : _s;
                        String rows = (indexOfC >= 0) ? _s.substring(indexOfC + 1) : "";
                        // System.out.println("C:" + cols);
                        // System.out.println("R:" + rows);
                        if (!cols.isEmpty()) {
                            colspan = Integer.parseInt(cols);
                        }
                        if (!rows.isEmpty()) {
                            rowspan = Integer.parseInt(rows);
                        }
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
