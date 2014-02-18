package me.hatter.tools.resourceproxy.jsspserver.filter;

import java.util.ArrayList;
import java.util.List;

public class DefaultResourceFilterChainFactory implements ResourceFilterChainFactory {

    private List<ResourceFilter> filterList = new ArrayList<ResourceFilter>();

    public class ResourceFilterChainImpl implements ResourceFilterChain {

        private int index = 0;

        public ResourceFilter next() {
            return (index >= filterList.size()) ? null : filterList.get(index++);
        }
    }

    public DefaultResourceFilterChainFactory(List<ResourceFilter> filterList) {
        this.filterList = filterList;
    }

    public ResourceFilterChain makeChain() {
        return new ResourceFilterChainImpl();
    }
}
