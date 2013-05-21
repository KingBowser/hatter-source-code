package me.hatter.apps.knowledgemanagement.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.apps.knowledgemanagement.util.GlobalSettingUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.jsspserver.action.DatabaseAction;

public class GlobalSettingJsonpAction extends DatabaseAction {

    @Override
    protected void doAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        if ("true".equals(request.getParameter("refresh"))) {
            GlobalSettingUtil.refreshMap();
        }

        String key = request.getParameter("key");
        if (StringUtil.isBlank(key)) {
            doJson(GlobalSettingUtil.cloneMap());
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put(key, GlobalSettingUtil.getValueByStrKey(key));
            doJson(map);
        }
    }
}
