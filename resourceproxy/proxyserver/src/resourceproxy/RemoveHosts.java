package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;
import me.hatter.tools.resourceproxy.dbutils.util.SQL;
import me.hatter.tools.resourceproxy.dbutils.util.SQL.Cmd;
import me.hatter.tools.resourceproxy.httpobjects.objects.HostConfig;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class RemoveHosts extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String id = request.getQueryValue("id");
        String host = request.getQueryValue("host");
        if (StringUtil.isNotEmpty(id)) {
            // remove by id
            DataAccessObject.executeSql(SQL.sql(Cmd.DELETE).table(HostConfig.class).where("id = ?").get(),
                                        DBUtil.objects(id));
        } else if ("all".equals(host)) {
            // remove by id
            DataAccessObject.executeSql(SQL.sql(Cmd.DELETE).table(HostConfig.class).where("access_address = ?").get(),
                                        DBUtil.objects(request.getIp()));
        }
        String ref = request.getQueryValue("ref");
        response.redirect((ref == null) ? "/hosts.jssp?jsspaction=resourceproxy.HostsConfig" : ref);
    }
}
