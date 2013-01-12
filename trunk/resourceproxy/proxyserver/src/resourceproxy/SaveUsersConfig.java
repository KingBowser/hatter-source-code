package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.objects.UserConfig;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;
import me.hatter.tools.resourceproxy.proxyserver.util.ResourceProxyDataAccesObjectInstance;

public class SaveUsersConfig extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String userAgent = request.getQueryValue("userAgent");
        UserConfig userConfig = new UserConfig();
        userConfig.setAccessAddress(request.getIp());
        userConfig.setUserAgent((userAgent == null) ? null : userAgent.trim());
        if (ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.selectObject(userConfig) == null) {
            ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.insertObject(userConfig);
        } else {
            ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.updateObject(userConfig);
        }

        response.redirect("/userconfig.jssp?jsspaction=resourceproxy.UsersConfig");
    }
}
