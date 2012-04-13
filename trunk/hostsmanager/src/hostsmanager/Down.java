package hostsmanager;

import java.util.Map;

import me.hatter.tools.hostsmanager.hosts.Group;
import me.hatter.tools.hostsmanager.hosts.Hosts;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Down extends BaseAction {

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        Hosts hosts = Index.readHosts();

        int delindex = Integer.valueOf(request.getQueryValue("index"));
        Group g = hosts.getGroups().remove(delindex);
        hosts.getGroups().add(delindex + 1, g);

        Index.writeHosts(hosts);
        response.redirect("index.jssp?jsspaction=hostsmanager.Index");
    }
}
