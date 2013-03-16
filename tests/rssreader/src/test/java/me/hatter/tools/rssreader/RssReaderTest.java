package me.hatter.tools.rssreader;

import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RssReaderTest {

    private static final String rss = "http://blog.yufeng.info/feed";

    @SuppressWarnings("rawtypes")
    public static void main(String[] args) throws Exception {
        URL url = new URL(rss);
        XmlReader reader = null;

        try {
            reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);
            System.out.println("Feed Title: " + feed.getAuthor());

            for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
                SyndEntry entry = (SyndEntry) i.next();
                System.out.println(entry.getTitle());
            }
        } finally {
            if (reader != null) reader.close();
        }
    }
}
