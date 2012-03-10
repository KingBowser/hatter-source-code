package me.hatter.tools.resourceproxy.jsspserver.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;

public class JsspFileManager {

    private static ConcurrentMap<File, JsspFile> jsspFileMap = new ConcurrentHashMap<File, JsspFile>();

    static {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String checkJsspUpdate = System.getProperty("checkJsspUpdate");
                long chkJsspUpdate = (StringUtil.isEmpty(checkJsspUpdate)) ? TimeUnit.SECONDS.toMillis(30) : TimeUnit.SECONDS.toMillis(Long.parseLong(checkJsspUpdate));
                try {
                    for (int i = 0; i < Integer.MAX_VALUE; i++) {
                        System.out.println("[INFO] Update jssp file manager round: " + i);
                        List<File> pendingRemoveList = new ArrayList<File>();
                        for (Entry<File, JsspFile> entry : jsspFileMap.entrySet()) {
                            try {
                                JsspFile jsspFile = entry.getValue();
                                if (!jsspFile.getFile().exists()) {
                                    pendingRemoveList.add(jsspFile.getFile());
                                    continue;
                                }
                                if ((jsspFile.getExplainedFile() == null)
                                    || (jsspFile.getExplainedFile().lastModified() < jsspFile.getFile().lastModified())) {
                                    jsspFile.getExplainedContent(true);// update the cache
                                }
                            } catch (Exception ex) {
                                System.out.println("[ERROR] Error occured in check jssp file: "
                                                   + entry.getValue().getFile());
                                ex.printStackTrace();
                            }
                        }
                        if (pendingRemoveList.size() > 0) {
                            for (File f : pendingRemoveList) {
                                jsspFileMap.remove(f);
                            }
                        }
                        Thread.sleep(chkJsspUpdate);
                    }
                } catch (Exception e) {
                    System.out.println("[ERROR] Error occured in check jssp file.");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static JsspFile getJsspFile(File file) {
        JsspFile jsspFile = jsspFileMap.get(file);
        if (jsspFile == null) {
            jsspFile = new JsspFile(file);
            jsspFileMap.put(file, jsspFile);
        }
        return jsspFile;
    }
}
