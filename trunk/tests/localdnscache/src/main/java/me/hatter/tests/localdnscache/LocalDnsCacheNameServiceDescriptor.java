package me.hatter.tests.localdnscache;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

public class LocalDnsCacheNameServiceDescriptor implements NameServiceDescriptor {

    private static final LocalDnsCacheNameService nameService = new LocalDnsCacheNameService();

    public NameService createNameService() throws Exception {
        return nameService;
    }

    public String getType() {
        return "dns";
    }

    public String getProviderName() {
        return "localDnsCache";
    }
}
