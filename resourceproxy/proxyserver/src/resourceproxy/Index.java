package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Index extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        context.put("ip", request.getIp());
    }
}
