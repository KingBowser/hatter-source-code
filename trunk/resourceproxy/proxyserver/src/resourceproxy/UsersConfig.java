package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.objects.UserConfig;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class UsersConfig extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        UserConfig userConfig = new UserConfig();
        userConfig.setAccessAddress(request.getIp());
        UserConfig userConfigFromDB = DataAccessObject.selectObject(userConfig);

        context.put("userConfig", (userConfigFromDB == null) ? userConfig : userConfigFromDB);
    }
}
