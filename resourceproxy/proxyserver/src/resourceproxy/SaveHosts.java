package resourceproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HostConfig;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;
import me.hatter.tools.resourceproxy.proxyserver.util.ResourceProxyDataAccesObjectInstance;

public class SaveHosts extends BaseAction {

    @Override
    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        String hosts = request.getQueryValue("hosts");

        if (StringUtil.isNotEmpty(hosts)) {
            BufferedReader br = new BufferedReader(new StringReader(hosts));
            try {
                for (String line; ((line = br.readLine()) != null);) {
                    String[] items = line.trim().split("\\s+");
                    if (items.length >= 2) {
                        String ip = items[0];
                        if (ip.matches("\\d+(\\.\\d+){3}(:\\d+)?")) {
                            for (int i = 1; i < items.length; i++) {
                                String domain = items[i].toLowerCase().trim();
                                HostConfig hostConfig = new HostConfig();
                                hostConfig.setDomain(domain);
                                hostConfig.setAccessAddress(request.getIp());
                                hostConfig.setTargetIp(ip);
                                if (ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.selectObject(hostConfig) == null) {
                                    ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.insertObject(hostConfig);
                                } else {
                                    ResourceProxyDataAccesObjectInstance.DATA_ACCESS_OBJECT.updateObject(hostConfig);
                                }
                            }
                        } else {
                            System.err.println("[ERROR] Format check failed(not ip): " + line);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String ref = request.getQueryValue("ref");
        response.redirect((ref == null) ? "/hosts.jssp?jsspaction=resourceproxy.HostsConfig" : ref);
    }
}
