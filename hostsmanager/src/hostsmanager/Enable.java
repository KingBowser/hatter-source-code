package hostsmanager;

import java.util.Map;

import me.hatter.tools.hostsmanager.hosts.Group;
import me.hatter.tools.hostsmanager.hosts.Hosts;
import me.hatter.tools.hostsmanager.hosts.Line;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Enable extends BaseAction {

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        Hosts hosts = Index.readHosts();

        int delindex = Integer.valueOf(request.getQueryValue("index"));
        Group g = hosts.getGroups().get(delindex);
        for (Line l : g.getLines()) {
            if (!l.isGroup()) {
                if (l.getLine().startsWith(Line.COMMENT)) {
                    l.setLine(l.getLine().substring(Line.COMMENT.length()));
                }
            }
        }

        Index.writeHosts(hosts);
        response.redirect("index.jssp?jsspaction=hostsmanager.Index");
    }
}
