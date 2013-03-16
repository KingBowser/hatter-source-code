package me.hatter.tools.rssreader;

import java.util.Arrays;
import java.util.List;

import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.rssreader.entity.RssChannel;
import me.hatter.tools.rssreader.entity.RssItem;
import me.hatter.tools.rssreader.entity.RssUrl;
import me.hatter.tools.rssreader.util.RssReaderUtil;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class RssReaderUpdater {

    private static final String           DB  = "rssreader.db";
    private static final DataAccessObject DAO = new DataAccessObject(PropertyConfig.createSqliteConfig(DB));

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        List<RssUrl> rssUrlList = DAO.listObjects(RssUrl.class);
        for (RssUrl rssUrl : rssUrlList) {
            LogUtil.info("Processing: " + rssUrl.getUrl());
            try {
                SyndFeed feed = RssReaderUtil.readUrlToFeed(rssUrl.getUrl());
                RssChannel channel = DAO.findObject(RssChannel.class, "id=?", Arrays.<Object> asList(rssUrl.getId()));
                if (channel == null) {
                    channel = new RssChannel();
                    channel.setId(rssUrl.getId());
                    channel.setTitle(feed.getTitle());
                    channel.setLink(feed.getLink());
                    channel.setDescription(feed.getDescription());
                    channel.setLastBuildDate(feed.getPublishedDate());
                    DAO.insertObject(channel);
                }

                List<SyndEntry> entryList = feed.getEntries();
                for (SyndEntry entry : entryList) {
                    String link = entry.getLink();
                    String guid = entry.getUri();
                    if (StringUtil.isEmpty(guid)) {
                        LogUtil.warn("GUID is empty in rss: " + rssUrl.getUrl());
                        if (StringUtil.isEmpty(link)) {
                            LogUtil.error("GUID and LINK both empty in rss: " + rssUrl.getUrl());
                            continue;
                        }
                        if (DAO.findObject(RssItem.class, "link=?", Arrays.<Object> asList(link)) != null) {
                            continue;
                        }
                    }
                    if (DAO.findObject(RssItem.class, "guid=?", Arrays.<Object> asList(guid)) != null) {
                        continue;
                    }
                    RssItem item = new RssItem();
                    item.setRssId(rssUrl.getId());
                    item.setLink(link);
                    item.setGuid(guid);
                    item.setPubDate(entry.getPublishedDate());
                    item.setStatus("new");
                    item.setTitle(entry.getTitle());
                    if (entry.getDescription() != null) {
                        item.setDescription(entry.getDescription().getValue());
                    }
                    DAO.insertObject(item);
                }
            } catch (Exception e) {
                LogUtil.error("Read rss failed: " + rssUrl.getUrl(), e);
            }
        }
    }
}
