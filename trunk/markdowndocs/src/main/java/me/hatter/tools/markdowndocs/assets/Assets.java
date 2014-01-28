package me.hatter.tools.markdowndocs.assets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Assets {

    public static final List<String> CSSES;
    public static final List<String> JSES;
    public static final List<String> IMGS;

    static {
        List<String> csses = new ArrayList<String>();
        csses.add("bootstrap.min.css");
        csses.add("docs.css");
        csses.add("pygments-manni.css");
        CSSES = Collections.unmodifiableList(csses);

        List<String> jses = new ArrayList<String>();
        jses.add("application.js");
        jses.add("bootstrap.js");
        jses.add("holder.js");
        jses.add("html5shiv.js");
        jses.add("ie8-responsive-file-warning.js");
        jses.add("jquery.min.js");
        jses.add("respond.min.js");
        JSES = Collections.unmodifiableList(jses);

        List<String> imgs = new ArrayList<String>();
        imgs.add("apple-touch-icon-144-precomposed.png");
        imgs.add("favicon.png");
        IMGS = Collections.unmodifiableList(imgs);
    }
}
