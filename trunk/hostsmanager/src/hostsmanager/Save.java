package hostsmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.hatter.tools.hostsmanager.hosts.Group;
import me.hatter.tools.hostsmanager.hosts.Hosts;
import me.hatter.tools.hostsmanager.hosts.Line;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Save extends BaseAction {

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String index = request.getQueryValue("index");
        String group = request.getQueryValue("group");
        String hosts = request.getQueryValue("hosts");

        Hosts etcHosts = Index.readHosts();
        boolean isAdd = ((index == null) || (StringUtil.isEmpty(index.trim())));

        Group g;
        if (isAdd) {
            g = new Group();
            etcHosts.getGroups().add(g);
        } else {
            g = etcHosts.getGroups().get(Integer.valueOf(index));
        }

        g.setGroup(group);
        g.setLines(new ArrayList<Line>());
        List<String> ls = Index.readToLines(hosts.trim());
        g.getLines().add(Line.parse(Line.GROUP_START + " " + group));
        for (String l : ls) {
            if (!group.trim().startsWith("X")) {
                l = l.trim().replaceAll("\\s+", " ").replaceFirst(" ", "\t");
            }
            g.getLines().add(Line.parse(l.trim()));
        }
        g.getLines().add(Line.parse(Line.GROUP_END));
        Index.writeHosts(etcHosts);

        if (isAdd) {
            response.redirect("index.jssp?jsspaction=hostsmanager.Index");
        } else {
            response.redirect("group.jssp?jsspaction=hostsmanager.AddEdit&save=ok&index=" + index);
        }
    }
}
