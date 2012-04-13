package hostsmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.hatter.tools.hostsmanager.hosts.Hosts;
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

    public static final String ETC_HOSTS     = "/etc/hosts";
    public static final String HOSTS_CHARSET = "UTF-8";

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String etchosts = FileUtil.readFileToString(new File(ETC_HOSTS), HOSTS_CHARSET);
        List<String> lines = readToLines(etchosts);

        String status = request.getQueryValue("status");
        List<HostsFilter> filters = new ArrayList<HostsFilter>();
        filters.add(new DefaultFilter());
        if ("valid".equals(status)) {
            filters.add(new EmptyFilter());
            filters.add(new CommentFilter());
        }
        List<String> filteredLines = HostsFilters.filter(filters, lines);

        Hosts hosts = Hosts.parse(lines);

        context.put("filteredLines", filteredLines);
        context.put("lines", lines);
        context.put("filteredEtcHosts", writeLinesToString(filteredLines));
        context.put("etchosts", etchosts);
        context.put("hosts", hosts);
    }

    public static Hosts readHosts() {
        String etchosts = FileUtil.readFileToString(new File(ETC_HOSTS), HOSTS_CHARSET);
        List<String> lines = readToLines(etchosts);
        return Hosts.parse(lines);
    }

    public static void writeHosts(Hosts hosts) {
        String newEtcHosts = Index.writeLinesToString(hosts.toLines());
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(Index.ETC_HOSTS)));
            osw.write(newEtcHosts);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String writeLinesToString(List<String> lines) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        for (String l : lines) {
            pw.println(l);
        }
        pw.flush();
        return sw.toString();
    }

    public static List<String> readToLines(String str) {
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
