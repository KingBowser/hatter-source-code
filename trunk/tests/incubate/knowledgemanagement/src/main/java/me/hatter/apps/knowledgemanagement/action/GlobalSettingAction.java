package me.hatter.apps.knowledgemanagement.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.apps.knowledgemanagement.util.GlobalSettingUtil.SettingKey;
import me.hatter.tools.jsspserver.action.DatabaseAction;

public class GlobalSettingAction extends DatabaseAction {

    @Override
    protected void doAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        List<String> settingNameList = new ArrayList<String>();

        for (SettingKey key : SettingKey.values()) {
            settingNameList.add(key.name());
        }
    }
}
