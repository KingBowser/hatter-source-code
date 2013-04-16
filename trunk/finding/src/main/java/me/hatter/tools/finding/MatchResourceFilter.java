package me.hatter.tools.finding;

import me.hatter.tools.commons.resource.Resource;

public interface MatchResourceFilter extends ResourceFilter {

    void matchResource(Resource resource);
}
