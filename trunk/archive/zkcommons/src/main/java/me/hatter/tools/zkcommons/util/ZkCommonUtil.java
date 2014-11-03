package me.hatter.tools.zkcommons.util;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

public class ZkCommonUtil {

    public static void createPersistent(ZkClient zkClient, String path) {
        int checkCount = 0;
        while (!zkClient.exists(path)) {
            checkCount++;
            if (checkCount > 10) {
                throw new RuntimeException("Check count larger than 10.");
            }
            try {
                zkClient.createPersistent(path, true);
            } catch (ZkNodeExistsException nee) {
                // IGNORE
            }
        }
    }
}
