package resourceproxy;

import java.lang.reflect.Field;
import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;
import me.hatter.tools.resourceproxy.proxyserver.util.UserAgents;

public class GetUserAgent extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String name = request.getQueryValue("name");
        if (StringUtil.isNotEmpty(name)) {
            String theUserAgent = "";
            if ("NONE".equalsIgnoreCase(name)) {
                theUserAgent = "";
            } else {
                try {
                    Field f = UserAgents.class.getDeclaredField(name);
                    String ua = (String) f.get(null);
                    theUserAgent = ua;
                } catch (Exception e) {
                    throw new RuntimeException("Cannot get user agent: " + name, e);
                }
            }
            StringBuilder js = new StringBuilder();
            js.append("document.getElementById(\"userAgent\").value = \"" + theUserAgent.replaceAll("\"", "\\\"")
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
