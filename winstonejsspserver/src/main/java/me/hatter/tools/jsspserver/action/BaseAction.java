package me.hatter.tools.jsspserver.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.jsspserver.auth.AuthMark;
import me.hatter.tools.jsspserver.exception.JSONException;
import me.hatter.tools.jsspserver.exception.RedirectException;
import me.hatter.tools.jsspserver.filter.JSONFilter;

public abstract class BaseAction implements Action {

    private static final LogTool                    logTool       = LogTools.getLogTool(BaseAction.class);

    private static ThreadLocal<HttpServletRequest>  localRequest  = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> localResponse = new ThreadLocal<HttpServletResponse>();
    private static ThreadLocal<Map<String, Object>> localContext  = new ThreadLocal<Map<String, Object>>();

    public Map<String, Object> doAction(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> context = new HashMap<String, Object>();
        try {
            localRequest.set(request);
            localResponse.set(response);
            localContext.set(context);

            // auth check
            AuthMark authMark = this.getClass().getAnnotation(AuthMark.class);
            if (authMark != null) {
                Object sessionAuth = request.getSession().getAttribute(AuthMark.SESSION_AUTH_KEY);
                if ((sessionAuth != null) && (sessionAuth instanceof String)) {
                    if (!Arrays.asList(authMark.value()).contains((String) sessionAuth)) {
                        throw new RedirectException("/noAuth.jssp");
                    }
                } else {
                    throw new RedirectException("/noAuth.jssp");
                }
            }

            // do real action
            doAction(request, response, context);
        } catch (RedirectException e) {
            sendRedirect(e.getUrl());
        } catch (JSONException e) {
            returnJson(e.getObject());
        } catch (Exception e) {
            logTool.error("Invoke action error: " + this.getClass().getName(), e);
            throw new RuntimeException(e);
        } finally {
            localRequest.remove();
            localResponse.remove();
            localContext.remove();
        }
        return context;
    }

    protected HttpServletRequest getRequest() {
        return localRequest.get();
    }

    protected HttpServletResponse getResponse() {
        return localResponse.get();
    }

    protected Map<String, Object> getContext() {
        return localContext.get();
    }

    protected void doRedirect(String url) {
        throw new RedirectException(url);
    }

    protected void doJson(Object json) {
        throw new JSONException(json);
    }

    // can be called in doAction
    protected void sendRedirect(String url) {
        try {
            localResponse.get().sendRedirect(url);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    // can be called in doAction
    protected void returnJson(Object json) {
        localContext.get().put(JSONFilter.JSON_KEY, json);
    }

    abstract protected void doAction(HttpServletRequest request, HttpServletResponse response,
                                     Map<String, Object> context);
}
