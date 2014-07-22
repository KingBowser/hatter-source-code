package me.hatter.tests.dnsjava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.hatter.tools.commons.collection.CollectionUtil.Group;
import me.hatter.tools.commons.string.StringUtil;

public class DomainTest {

    public static void main(String[] args) throws IOException {
        String[] domains = new String[]{"hongsengroup.com", "lizhong-hardware.com", "mx.hongsengroup.com",
                "mx.zhuguang.com.cn", "www.hongsengroup.com", "www.zhuguang.com.cn", "zhuguang.com.cn"};
        Group<String> table = new Group<String>();
        table.add("<table>");
        table.add("<tr>");
        table.add("<th>-</th>");
        table.add("<th>IP</th>");
        table.add("<th>NS</th>");
        table.add("<th>A</th>");
        table.add("<th>CNAME</th>");
        table.add("<th>MX</th>");
        table.add("</tr>");
        for (String domain : domains) {
            table.add("<tr>");
            table.add("<td>" + domain + "</td>");
            NSTool nsTool = new NSTool(domain, new String[]{"IP", "NS", "A", "CNAME", "MX"});
            Map<String, Object> result = nsTool.lookup();
            for (Entry<String, Object> e : result.entrySet()) {
                table.add("<td>" + join(e.getValue()) + "</td>");
            }
            table.add("</tr>");
        }
        table.add("</table>");

        System.out.println(StringUtil.join(table, "\n"));
    }

    @SuppressWarnings({"rawtypes"})
    public static String join(Object o) {
        if (o == null) {
            return "N/A";
        }
        if (o instanceof Collection) {
            List<String> l = new ArrayList<String>();
            for (Object ob : ((Collection) o)) {
                l.add(String.valueOf(ob));
            }
            return StringUtil.join(l, "<br/>");
        }
        return o.toString();
    }
}
