package resourceproxy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HostConfig;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class HostsConfig extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        int count = DataAccessObject.countObject(HostConfig.class, "access_address = ?",
                                                 Arrays.asList((Object) request.getIp()));
        String page = request.getQueryValue("page");
        page = (page == null) ? "1" : page;
        String page_size = request.getQueryValue("page_size");
        int pageSize = (page_size == null) ? 20 : Integer.parseInt(page_size);
        int from = (Integer.parseInt(page) - 1) * pageSize;

        List<HostConfig> hostConfigList = DataAccessObject.listObjects(HostConfig.class,
                                                                       "access_address = ? order by id desc limit ?, ?",
                                                                       Arrays.asList((Object) request.getIp(), from,
                                                                                     pageSize));
        context.put("ip", request.getIp());
        context.put("count", count);
        context.put("pageSize", pageSize);
        context.put("hostConfigList", hostConfigList);
    }
}
