package me.hatter.tools.rssreader;

import java.util.List;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.rssreader.entity.RssUrl;

public class ImportRssUrl {

    public static void main(String[] args) {
        String db = "/Users/hatterjiang/Code/hatter-source-code/tests/rssreader/rssreader.db";
        DataAccessObject dao = new DataAccessObject(PropertyConfig.createSqliteConfig(db));
        String rssurls = IOUtil.readResourceToString(ImportRssUrl.class, "rssurl.txt");
        List<String> rssurlList = IOUtil.readToList(rssurls);
        for (String url : rssurlList) {
            url = StringUtil.trimToNull(url);
            if (url == null) continue;
            RssUrl ru = new RssUrl();
            ru.setUrl(url);
            ru.setIsValid("Y");
            dao.insertObject(ru);
        }
    }
}
