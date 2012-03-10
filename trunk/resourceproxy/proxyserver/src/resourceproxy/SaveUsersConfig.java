package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.objects.UserConfig;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class SaveUsersConfig extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String userAgent = request.getQueryValue("userAgent");
        UserConfig userConfig = new UserConfig();
        userConfig.setAccessAddress(request.getIp());
        userConfig.setUserAgent((userAgent == null) ? null : userAgent.trim());
        if (DataAccessObject.selectObject(userConfig) == null) {
            DataAccessObject.insertObject(userConfig);
        } else {
            DataAccessObject.updateObject(userConfig);
        }

        response.redirect("/userconfig.jssp?jsspaction=resourceproxy.UsersConfig");
    }
}
