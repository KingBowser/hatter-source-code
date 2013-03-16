package me.hatter.tools.rssreader.util;

import java.net.URL;

import me.hatter.tools.commons.io.IOUtil;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RssReaderUtil {

    public static SyndFeed readUrlToFeed(String url) throws Exception {
        XmlReader reader = null;
        SyndFeed feed = null;
        try {
            reader = new XmlReader(new URL(url));
            feed = new SyndFeedInput().build(reader);
        } finally {
            IOUtil.closeQuitely(reader);
        }
        return feed;
    }
}
