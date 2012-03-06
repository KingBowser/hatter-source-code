package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;
import me.hatter.tools.resourceproxy.dbutils.util.SQL;
import me.hatter.tools.resourceproxy.dbutils.util.SQL.Cmd;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Remove extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String id = request.getQueryValue("id");
        String ip = request.getQueryValue("ip");
        if (StringUtil.isNotEmpty(id)) {
            // remove by id
            DataAccessObject.executeSql(SQL.sql(Cmd.DELETE).table(HttpObject.class).where("id = ?").get(),
                                        DBUtil.objects(id));
        } else if ("all".equals(ip)) {
            // remove by id
            DataAccessObject.executeSql(SQL.sql(Cmd.DELETE).table(HttpObject.class).where("access_address = ?").get(),
                                        DBUtil.objects(request.getIp()));
        }
        String ref = request.getQueryValue("ref");
        response.redirect((ref == null) ? "/" : ref);
    }
}
