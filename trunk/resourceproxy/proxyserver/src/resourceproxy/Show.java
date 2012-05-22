package resourceproxy;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpResponseUtil;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Show extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String id = request.getQueryValue("id");
        String charset = request.getQueryValue("charset");
        HttpObject httpObject = null;
        if (StringUtil.isNotEmpty(id)) {
            httpObject = CollUtil.firstObject(DataAccessObject.listObjects(HttpObject.class, "id = ?",
                                                                           DBUtil.objects(id)));
        }
        if (StringUtil.isNotEmpty(charset) && HttpResponseUtil.isTextContentType(httpObject.getContentType())
            && (httpObject.getBytes() != null)) {
            byte[] bytes = StringUtil.stringToByteArray(httpObject.getBytes());
            try {
                httpObject.setString(new String(bytes, charset));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        context.put("id", id);
        context.put("charset", charset);
        context.put("isTextContentType", HttpResponseUtil.isTextContentType(httpObject.getContentType()));
        context.put("httpObject", httpObject);
    }
}
