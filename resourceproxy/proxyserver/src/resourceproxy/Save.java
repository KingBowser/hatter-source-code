package resourceproxy;

import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;
import me.hatter.tools.resourceproxy.dbutils.util.SQL;
import me.hatter.tools.resourceproxy.dbutils.util.SQL.Cmd;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpResponseUtil;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;
import me.hatter.tools.resourceproxy.proxyserver.util.ResourceProxyDataAccesObjectInstance;

public class Save extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {

        String id = request.getQueryValue("id");
        String charset = request.getQueryValue("charset");
        String source = request.getQueryValue("source");

        HttpObject httpObject = CollUtil.firstObject(ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.listObjects(HttpObject.class,
                                                                                                                         SQL.sql(Cmd.SELECT).table(HttpObject.class).where("id = ?").get(),
                                                                                                                         DBUtil.objects(id)));
        if ((httpObject != null) && HttpResponseUtil.isTextContentType(httpObject.getContentType())) {
            if ((httpObject.getCharset() == null) && (StringUtil.isNotEmpty(charset))) {
                httpObject.setCharset(charset);
            }
            httpObject.setBytes(null);
            httpObject.setString(source);
            httpObject.setIsUpdated("Y");
            ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.updateObject(httpObject);
        }

        response.redirect("/show.jssp?jsspaction=resourceproxy.Show&id=" + id);
    }
}
