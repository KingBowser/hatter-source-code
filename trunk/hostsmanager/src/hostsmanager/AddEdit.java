package hostsmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.hatter.tools.hostsmanager.hosts.Group;
import me.hatter.tools.hostsmanager.hosts.Hosts;
import me.hatter.tools.hostsmanager.hosts.Line;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class AddEdit extends BaseAction {

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        Hosts hosts = Index.readHosts();

        Group group;
        if (request.getQueryValueMap().containsKey("index")) {
            int selindex = Integer.valueOf(request.getQueryValue("index"));
            group = hosts.getGroups().get(selindex);
        } else {
            group = new Group();
        }
        List<String> lines = new ArrayList<String>();
        if (group.getLines() != null) {
            for (Line l : group.getLines()) {
                if (!l.isGroup()) {
                    lines.add(l.getLine());
                }
            }
        }

        context.put("group", group.getGroup());
        context.put("newhosts", Index.writeLinesToString(lines));
    }
}
