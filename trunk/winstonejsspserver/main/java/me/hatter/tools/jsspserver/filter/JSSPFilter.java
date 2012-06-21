package me.hatter.tools.jsspserver.filter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
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

import me.hatter.tools.jsspserver.action.Action;
import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.commons.resource.Resources;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.JsspReader;
import me.hatter.tools.resourceproxy.jsspexec.jsspreader.ExplainedJsspReader;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;
import me.hatter.tools.resourceproxy.jsspserver.util.JsspResource;
import me.hatter.tools.resourceproxy.jsspserver.util.JsspResourceManager;
import winstone.WinstoneResponse;

public class JSSPFilter implements Filter {

    public static final boolean JSSP_DEBUG = Boolean.valueOf(System.getProperty("jsspdebug"));

    public static File          JSSP_PATH;
    static {
        String jsspPath = System.getProperty("jssp.path");
        if (jsspPath == null) {
            JSSP_PATH = new File(System.getProperty("user.dir"), "_jssp");
        } else {
            JSSP_PATH = new File(jsspPath);
        }
    }
    public static Resources     RESOURCES  = new Resources(JSSP_PATH);

    static {
        JsspExecutor.initJsspWork();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String fpath = httpRequest.getServletPath();
        if (fpath.toLowerCase().endsWith(".jssp")) {
            JsspResource jsspResource = JsspResourceManager.getJsspResource(getResource(fpath));

            JsspReader jsspReader = new ExplainedJsspReader() {

                @Override
                public String readExplained(String path) {
                    Resource resource = getResource(path);
                    if (!resource.exists()) {
                        throw new RuntimeException("Resource not found: " + path);
                    }
                    JsspResource jsspResource = JsspResourceManager.getJsspResource(resource);
                    return jsspResource.getExplainedContent(JSSP_DEBUG);
                }
            };

            if (jsspResource.exists()) {
                System.out.println("[INFO] Found jssp resource: " + jsspResource.getResource());

                Map<String, Object> context = new HashMap<String, Object>();
                String jsspAction = httpRequest.getParameter(Action.JSSP_ACTION);
                if (jsspAction != null) {
                    try {
                        Class<?> jsspActionClazz = Class.forName(jsspAction);
                        System.out.println("[INFO] Found jssp action: " + jsspActionClazz);
                        if (Action.class.isAssignableFrom(jsspActionClazz)) {
                            Action a = ((Action) jsspActionClazz.newInstance());
                            context = a.doAction(httpRequest, httpResponse);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (httpResponse instanceof WinstoneResponse) {
                    int status = ((WinstoneResponse) httpResponse).getStatus();
                    if ((status == HttpServletResponse.SC_MOVED_PERMANENTLY)
                        || (status == HttpServletResponse.SC_MOVED_TEMPORARILY)) {
                        return; // redirected
                    }
                }

                BufferWriter bw = new BufferWriter();
                Map<String, Object> addContext = new HashMap<String, Object>();
                addContext.put("request", request);
                JsspExecutor.executeExplained(new StringReader(jsspResource.getExplainedContent(JSSP_DEBUG)), context,
                                              addContext, jsspReader, bw);

                httpResponse.setContentType(ContentTypes.HTML_CONTENT_TYPE);
                httpResponse.setCharacterEncoding(ContentTypes.UTF8_CHARSET);
                httpResponse.setStatus(HttpConstants.STATUS_SUCCESS);
                httpResponse.setContentType(ContentTypes.HTML_AND_UTF8);
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

    @Override
    public void destroy() {
    }

    public static Resource getResource(String fpath) {
        return RESOURCES.findResource(Resources.RESOURCE + dealFPath(fpath));
    }

    public static String dealFPath(String fpath) {
        if (fpath == null) {
            return null;
        }
        if (!Resources.isDefClasspath()) {
            return fpath;
        }
        fpath = fpath.replace('\\', '/').trim();
        while (fpath.startsWith("/")) {
            fpath = fpath.substring(1);
        }
        return fpath;
    }
}
