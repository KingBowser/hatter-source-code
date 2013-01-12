package resourceproxy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.objects.UserConfig;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;
import me.hatter.tools.resourceproxy.proxyserver.util.ResourceProxyDataAccesObjectInstance;
import me.hatter.tools.resourceproxy.proxyserver.util.UserAgents;

public class UsersConfig extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        UserConfig userConfig = new UserConfig();
        userConfig.setAccessAddress(request.getIp());
        UserConfig userConfigFromDB = ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.selectObject(userConfig);

        List<String> userAgentNameList = new ArrayList<String>();
        userAgentNameList.add("NONE");
        for (Field field : UserAgents.class.getDeclaredFields()) {
            userAgentNameList.add(field.getName());
        }

        context.put("userAgentNameList", userAgentNameList);
        context.put("userConfig", (userConfigFromDB == null) ? userConfig : userConfigFromDB);
    }
}
