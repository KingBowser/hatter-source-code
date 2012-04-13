package hostsmanager;

import java.util.Map;

import me.hatter.tools.hostsmanager.hosts.Hosts;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Delete extends BaseAction {

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        Hosts hosts = Index.readHosts();

        int delindex = Integer.valueOf(request.getQueryValue("index"));
        hosts.getGroups().remove(delindex);

        Index.writeHosts(hosts);
        response.redirect("index.jssp?jsspaction=hostsmanager.Index");
    }
}
