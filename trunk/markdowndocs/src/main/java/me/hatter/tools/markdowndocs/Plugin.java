package me.hatter.tools.markdowndocs;

import me.hatter.tools.commons.args.KArgs;

public interface Plugin {

    void init();

    boolean matches(String plg);

    void main(KArgs args);
}
