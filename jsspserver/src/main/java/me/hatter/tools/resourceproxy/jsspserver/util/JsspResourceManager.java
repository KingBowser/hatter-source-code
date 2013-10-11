package me.hatter.tools.resourceproxy.jsspserver.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.resource.Resource;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;

public class JsspResourceManager {

    private static final LogTool                         logTool     = LogTools.getLogTool(JsspResourceManager.class);

    private static ConcurrentMap<Resource, JsspResource> jsspFileMap = new ConcurrentHashMap<Resource, JsspResource>();

    static {
        new Thread(new Runnable() {

            // @Override
            public void run() {
                String checkJsspUpdate = System.getProperty("checkjsspupdate");
                long defaultChkJsspUpdateMillis = TimeUnit.MINUTES.toMillis(5);
                long chkJsspUpdate = (StringUtil.isEmpty(checkJsspUpdate)) ? defaultChkJsspUpdateMillis : TimeUnit.SECONDS.toMillis(Long.parseLong(checkJsspUpdate));
                try {
                    for (int i = 0; i < Integer.MAX_VALUE; i++) {
                        if (logTool.isInfoEnable()) {
                            logTool.info("Update jssp resource manager round: " + i);
                        }
                        List<Resource> pendingRemoveList = new ArrayList<Resource>();
                        for (Entry<Resource, JsspResource> entry : jsspFileMap.entrySet()) {
                            try {
                                JsspResource jsspResource = entry.getValue();
                                if (!jsspResource.getResource().exists()) {
                                    pendingRemoveList.add(jsspResource.getResource());
                                    continue;
                                }
                                if ((jsspResource.getExplained() == null)
                                    || (jsspResource.getExplained().lastModified() < jsspResource.getResource().lastModified())) {
                                    jsspResource.getExplainedContent(true);// update the cache
                                }
                            } catch (Exception ex) {
                                logTool.error("Error occured in check jssp resource: " + entry.getValue().getResource(),
                                              ex);
                            }
                        }
                        if (pendingRemoveList.size() > 0) {
                            for (Resource r : pendingRemoveList) {
                                jsspFileMap.remove(r);
                            }
                        }
                        Thread.sleep(chkJsspUpdate);
                    }
                } catch (Exception e) {
                    logTool.error("Error occured in check jssp file.", e);
                }
            }
        }).start();
    }

    public static JsspResource getJsspResource(Resource resource) {
        JsspResource jsspResource = jsspFileMap.get(resource);
        if (jsspResource == null) {
            jsspResource = new JsspResource(resource);
            jsspFileMap.put(resource, jsspResource);
        }
        return jsspResource;
    }
}
