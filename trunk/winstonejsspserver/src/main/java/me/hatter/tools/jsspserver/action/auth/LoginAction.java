package me.hatter.tools.jsspserver.action.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.jsspserver.action.DatabaseAction;
import me.hatter.tools.jsspserver.auth.AuthMark;

public class LoginAction extends DatabaseAction {

    @Override
    protected void doAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (request.getSession().getAttribute(AuthMark.SESSION_AUTH_KEY) != null) {
            doRedirect("/index.jssp");
        }

        if ((username != null) || (password != null)) {
            if (StringUtil.equals(APP_PROPERTIES.getProperty("auth.username", "admin"), username)
                && StringUtil.equals(APP_PROPERTIES.getProperty("auth.password"), password)) {
                // login ok
                request.getSession().setAttribute(AuthMark.SESSION_AUTH_KEY, "admin");
                doRedirect("/index.jssp");
            }
            context.put("message", "用户名或密码错误！");
        }
    }
}
