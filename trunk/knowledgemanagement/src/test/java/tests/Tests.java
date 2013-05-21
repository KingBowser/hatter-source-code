package tests;

import me.hatter.apps.knowledgemanagement.entity.GlobalSetting;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;

public class Tests {

    public static void main(String[] args) {
        System.out.println(DBUtil.generateCreateSQL(GlobalSetting.class));
    }
}
