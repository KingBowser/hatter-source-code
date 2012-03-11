package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;
import me.hatter.tools.resourceproxy.dbutils.util.SQL;
import me.hatter.tools.resourceproxy.dbutils.util.SQL.Cmd;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class SetIsUpdate extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String id = request.getQueryValue("id");
        if (StringUtil.isNotEmpty(id)) {
            HttpObject httpObject = CollUtil.firstObject(DataAccessObject.listObjects(HttpObject.class,
                                                                                      "id=?",
                                                                                      DBUtil.objects(Integer.parseInt(id))));
            if (httpObject != null) {
                String isUpdate = ("Y".equals(httpObject.getIsUpdated())) ? null : "Y";
                DataAccessObject.executeSql(SQL.sql(Cmd.UPDATE).update("is_updated=?").table(HttpObject.class).where("id=?").get(),
                                            DBUtil.objects(isUpdate, Integer.parseInt(id)));
                StringBuilder js = new StringBuilder();
                js.append("document.getElementById(\"isUpdate\").innerHTML = \"" + ((isUpdate == null) ? "" : isUpdate)
                          + "\";");

                response.setContentType(ContentTypes.JAVASCRIPT_CONTENT_TYPE);
                response.setCharset(ContentTypes.UTF8_CHARSET);
                response.setStatus(HttpConstants.STATUS_SUCCESS);
                response.setStatusMessage("OK");
                response.getHeaderMap().set(ContentTypes.CONTENT_TYPE, ContentTypes.JAVASCRIPT_AND_UTF8);
                response.setString(js.toString());
                response.setFinish(true);
            }
        }
    }
}
