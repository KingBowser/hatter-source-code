package me.hatter.tools.jsspserver.action.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.jsspserver.action.DatabaseAction;
import me.hatter.tools.jsspserver.auth.AuthMark;

public class LogoutAction extends DatabaseAction {

    @Override
    protected void doAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        if (request.getSession().getAttribute(AuthMark.SESSION_AUTH_KEY) != null) {
            request.getSession().setAttribute(AuthMark.SESSION_AUTH_KEY, null);
        }
        doRedirect("/login.jssp");
    }
}
