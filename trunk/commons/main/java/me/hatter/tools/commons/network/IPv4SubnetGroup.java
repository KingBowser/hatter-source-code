package me.hatter.tools.commons.network;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class IPv4SubnetGroup {

    private Set<IPv4Subnet> subnets = new LinkedHashSet<IPv4Subnet>();

    public IPv4SubnetGroup() {
    }

    public IPv4SubnetGroup(Collection<IPv4Subnet> subnets) {
        if (subnets != null) {
            this.subnets.addAll(subnets);
        }
    }

    public boolean matches(String ip) {
        for (IPv4Subnet subnet : subnets) {
            if ((subnet != null) && subnet.matches(ip)) {
                return true;
            }
        }
        return false;
    }
}
