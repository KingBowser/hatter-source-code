package me.hatter.tools.resourceproxy.jsspexec.jsspreader;

import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.jsspexec.JsspReader;

public interface SimpleJsspReader extends JsspReader {

    Resource readResource(String path);
}
