package hostsmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.hatter.tools.hostsmanager.hostsfilter.HostsFilter;
import me.hatter.tools.hostsmanager.hostsfilter.HostsFilters;
import me.hatter.tools.hostsmanager.hostsfilter.impl.CommentFilter;
import me.hatter.tools.hostsmanager.hostsfilter.impl.DefaultFilter;
import me.hatter.tools.hostsmanager.hostsfilter.impl.EmptyFilter;
import me.hatter.tools.resourceproxy.commons.util.FileUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Index extends BaseAction {

    private static final String ETC_HOSTS = "/etc/hosts";

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String hosts = FileUtil.readFileToString(new File(ETC_HOSTS));
        List<String> lines = readToLines(hosts);

        String status = request.getQueryValue("status");
        List<HostsFilter> filters = new ArrayList<HostsFilter>();
        filters.add(new DefaultFilter());
        if ("valid".equals(status)) {
            filters.add(new EmptyFilter());
            filters.add(new CommentFilter());
        }
        List<String> filteredLines = HostsFilters.filter(filters, lines);

        context.put("filteredLines", filteredLines);
        context.put("lines", lines);
        context.put("filteredHosts", writeLinesToString(filteredLines));
        context.put("hosts", hosts);
    }

    private static String writeLinesToString(List<String> lines) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        for (String l : lines) {
            pw.println(l);
        }
        pw.flush();
        return sw.toString();
    }

    private static List<String> readToLines(String str) {
        try {
            List<String> lines = new ArrayList<String>();
            if (str != null) {
                BufferedReader br = new BufferedReader(new StringReader(str));
                for (String l; ((l = br.readLine()) != null);) {
                    lines.add(l);
                }
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
