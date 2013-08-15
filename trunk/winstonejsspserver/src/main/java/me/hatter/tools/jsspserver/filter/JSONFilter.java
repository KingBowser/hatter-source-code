package me.hatter.tools.jsspserver.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.jsspserver.action.Action;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;
import me.hatter.tools.resourceproxy.jsspserver.util.JsspResource;
import me.hatter.tools.resourceproxy.jsspserver.util.JsspResourceManager;
import winstone.WinstoneResponse;

import com.alibaba.fastjson.JSON;

public class JSONFilter implements Filter {

    public static final String JSON_KEY = "__json_key__";

    public void init(FilterConfig filterConfig) throws ServletException {
        FilterTool.initDefaultInstance(filterConfig.getServletContext().getRealPath("jssp"));
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
        request.setCharacterEncoding(FilterTool.DEFAULT_CHARACTER_ENCODING);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String fpath = httpRequest.getServletPath();
        if (fpath.toLowerCase().endsWith(".jsonp") || fpath.toLowerCase().endsWith(".ajax")) {
            JsspResource jsonpResource = JsspResourceManager.getJsspResource(FilterTool.defaultInstance().getResource(fpath));

            if (jsonpResource.exists()) {
                System.out.println("[INFO] Found jsonp/ajax resource: " + jsonpResource.getResource());

                Map<String, Object> context = new HashMap<String, Object>();
                String jsonpAction = StringUtil.trimToNull(IOUtil.readToString(jsonpResource.getResource().openInputStream()));

                if (jsonpAction != null) {
                    try {
                        Class<?> jsonpActionClazz = Class.forName(jsonpAction);
                        System.out.println("[INFO] Found jsonp/ajax action: " + jsonpActionClazz);
                        if (Action.class.isAssignableFrom(jsonpActionClazz)) {
                            Action a = ((Action) jsonpActionClazz.newInstance());
                            context = a.doAction(httpRequest, httpResponse);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    httpResponse.setContentType("text/plain");
                    httpResponse.getWriter().print("Resource is empty: " + fpath);
                    return;
                }

                if (httpResponse instanceof WinstoneResponse) {
                    int status = ((WinstoneResponse) httpResponse).getStatus();
                    if ((status == HttpServletResponse.SC_MOVED_PERMANENTLY)
                        || (status == HttpServletResponse.SC_MOVED_TEMPORARILY)) {
                        return; // redirected
                    }
                }

                String callback = StringUtil.trimToNull(httpRequest.getParameter("callback"));
                if (callback != null) {
                    callback = callback.replaceAll("[^a-zA-Z0-9_.]", "_");
                }
                BufferWriter bw = new BufferWriter();
                Object jsonp = context.get(JSON_KEY);
                if (jsonp == null) {
                    bw.write((callback == null ? "" : callback + "(") + JSON.toJSONString(context)
                             + (callback == null ? "" : ");"));
                } else {
                    bw.write((callback == null ? "" : callback + "(") + JSON.toJSONString(jsonp)
                             + (callback == null ? "" : ");"));
                }

                httpResponse.setCharacterEncoding(ContentTypes.UTF8_CHARSET);
                httpResponse.setStatus(HttpConstants.STATUS_SUCCESS);
                httpResponse.setContentType(ContentTypes.JAVASCRIPT_AND_UTF8);
                httpResponse.getWriter().print(bw.getBufferedString());
            } else {
                // response.set
                httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                httpResponse.setContentType("text/plain");
                httpResponse.getWriter().print("Resource not found: " + fpath);
            }
        } else {
            chain.doFilter(httpRequest, response);
        }
    }

    public void destroy() {
    }
}
