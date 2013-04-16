package me.hatter.tools.finding;

import me.hatter.tools.commons.resource.Resource;

public interface ResourceFilter {

    boolean accept(Resource resource);
}
