package hostsmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.hatter.tools.hostsmanager.hosts.Hosts;
import me.hatter.tools.resourceproxy.commons.io.StringBufferedReader;
import me.hatter.tools.resourceproxy.commons.io.StringPrintWriter;
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

        Hosts hosts = Hosts.parse(lines);
        context.put("hosts", hosts);
    }

    public static Hosts readHosts() {
        String etchosts = FileUtil.readFileToString(new File(ETC_HOSTS), HOSTS_CHARSET);
        List<String> lines = readToLines(etchosts);
        return Hosts.parse(lines);
    }

    public static void writeHosts(Hosts hosts) {
        String newEtcHosts = Index.writeLinesToString(hosts.toLines());
        FileUtil.writeStringToFile(new File(ETC_HOSTS), newEtcHosts, HOSTS_CHARSET);
    }

    public static String writeLinesToString(List<String> lines) {
        StringPrintWriter writer = new StringPrintWriter();
        for (String l : lines) {
            writer.println(l);
        }
        return writer.toString();
    }

    public static List<String> readToLines(String str) {
        try {
            List<String> lines = new ArrayList<String>();
            if (str != null) {
                StringBufferedReader reader = new StringBufferedReader(str);
                for (String l; ((l = reader.readLine()) != null);) {
                    lines.add(l);
                }
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
