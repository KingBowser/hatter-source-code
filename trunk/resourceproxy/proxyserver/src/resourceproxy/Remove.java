package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;
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
            DataAccessObject.executeSql("delete from " + DBUtil.getTableName(HttpObject.class) + " where id = ?",
                                        DBUtil.objects(id));
        } else if ("all".equals(ip)) {
            // remove by id
            DataAccessObject.executeSql("delete from " + DBUtil.getTableName(HttpObject.class)
                                        + " where access_address = ?", DBUtil.objects(request.getIp()));
        }
        String ref = request.getQueryValue("ref");
        response.redirect((ref == null) ? "/" : ref);
    }
}
