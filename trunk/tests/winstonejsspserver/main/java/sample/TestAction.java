package sample;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.commons.local.LocalHostUtil;
import me.hatter.tools.jsspserver.action.BaseAction;

public class TestAction extends BaseAction {

    @Override
    protected void doAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        context.put("ip", LocalHostUtil.getLocalIp());
    }
}
