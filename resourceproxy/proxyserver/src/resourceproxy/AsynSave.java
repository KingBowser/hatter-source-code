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
import me.hatter.tools.resourceproxy.httpobjects.util.HttpResponseUtil;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class AsynSave extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {

        String id = request.getQueryValue("id");
        String charset = request.getQueryValue("charset");
        String source = request.getQueryValue("source");

        boolean success = false;
        HttpObject httpObject = CollUtil.firstObject(DataAccessObject.listObjects(HttpObject.class,
                                                                                  SQL.sql(Cmd.SELECT).table(HttpObject.class).where("id = ?").get(),
                                                                                  DBUtil.objects(id)));
        if ((httpObject != null) && HttpResponseUtil.isTextContentType(httpObject.getContentType())) {
            if ((httpObject.getCharset() == null) && (StringUtil.isNotEmpty(charset))) {
                httpObject.setCharset(charset);
            }
            httpObject.setBytes(null);
            httpObject.setString(source);
            httpObject.setIsUpdated("Y");
            DataAccessObject.updateObject(httpObject);
            success = true;
        }
        response.setContentType(ContentTypes.JAVASCRIPT_CONTENT_TYPE);
        response.setCharset(ContentTypes.UTF8_CHARSET);
        response.setStatus(HttpConstants.STATUS_SUCCESS);
        response.setStatusMessage("OK");
        response.getHeaderMap().set(ContentTypes.CONTENT_TYPE, ContentTypes.JAVASCRIPT_AND_UTF8);
        response.setString(success ? "\"success\"" : "\"failed\"");
        response.setFinish(true);
    }
}
