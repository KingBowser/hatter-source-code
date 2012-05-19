package me.hatter.tools.resourceproxy.jsspexec.jsspreader;

import me.hatter.tools.resourceproxy.jsspexec.JsspReader;

public interface ExplainedJsspReader extends JsspReader {

    String readExplained(String path);
}
