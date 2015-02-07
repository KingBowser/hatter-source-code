package me.hatter.tests.harlib;

import java.io.File;

import edu.umass.cs.benchlab.har.HarEntries;
import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.tools.HarFileReader;

// https://sites.google.com/site/frogthinkerorg/projects/harlib
public class TestHarLib {

    public static void main(String[] args) throws Exception {
        File f = new File(System.getProperty("user.dir"), "/src/main/resources/f.har");
        HarFileReader har = new HarFileReader();
        HarLog log = har.readHarFile(f);
        // HarBrowser browser = log.getBrowser();
        HarEntries entries = log.getEntries();
        // List<HarPage> pages = log.getPages().getPages();

        // System.out.println(browser);
        // System.out.println(entries);
        // for (HarPage page : pages) {
        // System.out.println(page.getTitle());
        // }
        for (HarEntry entry : entries.getEntries()) {
            System.out.println(entry.getRequest().getUrl());
        }
    }
}
