package me.hatter.tools.jsspserver.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.jsspserver.exception.JSONException;
import me.hatter.tools.jsspserver.exception.RedirectException;
import me.hatter.tools.jsspserver.filter.JSONFilter;

public abstract class BaseAction implements Action {

    private static ThreadLocal<HttpServletRequest>  localRequest  = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> localResponse = new ThreadLocal<HttpServletResponse>();
    private static ThreadLocal<Map<String, Object>> localContext  = new ThreadLocal<Map<String, Object>>();

    public Map<String, Object> doAction(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> context = new HashMap<String, Object>();
        try {
            localRequest.set(request);
            localResponse.set(response);
            localContext.set(context);
            doAction(request, response, context);
        } catch (RedirectException e) {
            sendRedirect(e.getUrl());
        } catch (JSONException e) {
            returnJson(e.getObject());
        } catch (Exception e) {
            LogUtil.error("Invoke action error: " + this.getClass().getName(), e);
            throw new RuntimeException(e);
        } finally {
            localRequest.remove();
            localResponse.remove();
            localContext.remove();
        }
        return context;
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
