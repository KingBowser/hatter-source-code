package hostsmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.hatter.tools.hostsmanager.hostsfilter.HostsFilter;
import me.hatter.tools.hostsmanager.hostsfilter.HostsFilters;
import me.hatter.tools.hostsmanager.hostsfilter.impl.CommentFilter;
import me.hatter.tools.hostsmanager.hostsfilter.impl.DefaultFilter;
import me.hatter.tools.hostsmanager.hostsfilter.impl.EmptyFilter;
import me.hatter.tools.hostsmanager.hostsfilter.impl.EmptyGroupFilter;
import me.hatter.tools.resourceproxy.commons.util.FileUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Valid extends BaseAction {

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String etchosts = FileUtil.readFileToString(new File(Index.ETC_HOSTS), Index.HOSTS_CHARSET);
        List<String> lines = Index.readToLines(etchosts);

        List<HostsFilter> filters = new ArrayList<HostsFilter>();
        filters.add(new DefaultFilter());
        filters.add(new EmptyFilter());
        filters.add(new CommentFilter());
        filters.add(new EmptyGroupFilter());
        List<String> filteredLines = HostsFilters.filter(filters, lines);

        context.put("validhosts", Index.writeLinesToString(filteredLines));
    }
}
