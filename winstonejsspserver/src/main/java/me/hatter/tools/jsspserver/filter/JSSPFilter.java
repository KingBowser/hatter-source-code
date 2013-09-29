package me.hatter.tools.jsspserver.filter;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
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

import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.HtmlEscapeUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.jsspserver.action.Action;
import me.hatter.tools.jsspserver.action.Action.DoAction;
import me.hatter.tools.jsspserver.util.ResponseUtil;
import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.JsspReader;
import me.hatter.tools.resourceproxy.jsspexec.jsspreader.ExplainedJsspReader;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;
import me.hatter.tools.resourceproxy.jsspserver.util.JsspResource;
import me.hatter.tools.resourceproxy.jsspserver.util.JsspResourceManager;

public class JSSPFilter implements Filter {

    private static final LogTool logTool = LogTools.getLogTool(JSSPFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        FilterTool.initDefaultInstance(filterConfig.getServletContext().getRealPath("jssp"));
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
        request.setCharacterEncoding(FilterTool.DEFAULT_CHARACTER_ENCODING);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String fpath = httpRequest.getServletPath();
        if (fpath.toLowerCase().endsWith(".jssp")) {
            try {
                JsspResource jsspResource = JsspResourceManager.getJsspResource(FilterTool.defaultInstance().getResource(fpath));

                JsspReader jsspReader = new ExplainedJsspReader() {

                    public String readExplained(String path) {
                        Resource resource = FilterTool.defaultInstance().getResource(path);
                        if (!resource.exists()) {
                            throw new RuntimeException("Resource not found: " + path);
                        }
                        JsspResource jsspResource = JsspResourceManager.getJsspResource(resource);
                        return jsspResource.getExplainedContent(FilterTool.JSSP_DEBUG);
                    }
                };

                if (jsspResource.exists()) {
                    if (logTool.isInfoEnable()) {
                        logTool.info("Found jssp resource: " + jsspResource.getResource());
                    }

                    Map<String, Object> context = new HashMap<String, Object>();
                    String jsspAction = httpRequest.getParameter(Action.JSSP_ACTION);
                    if (jsspAction != null) {
                        try {
                            Class<?> jsspActionClazz = Class.forName(jsspAction);
                            if (logTool.isInfoEnable()) {
                                logTool.info("Found jssp action: " + jsspActionClazz);
                            }
                            if (Action.class.isAssignableFrom(jsspActionClazz)) {
                                Action a = ((Action) jsspActionClazz.newInstance());
                                context = a.doAction(httpRequest, httpResponse);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    int status = ResponseUtil.getIntResponseStatus(httpResponse);
                    if ((status == HttpServletResponse.SC_MOVED_PERMANENTLY)
                        || (status == HttpServletResponse.SC_MOVED_TEMPORARILY)) {
                        return; // redirected
                    }

                    String explainedContent = jsspResource.getExplainedContent(FilterTool.JSSP_DEBUG);

                    BufferWriter bw = new BufferWriter();
                    Map<String, Object> addContext = new HashMap<String, Object>();
                    addContext.put("request", httpRequest);
                    addContext.put("response", httpResponse);
                    addContext.put("action", new DoAction(httpRequest, httpResponse, context));

                    JsspExecutor.executeExplained(new StringReader(explainedContent), context, addContext, jsspReader,
                                                  bw, jsspResource.getResource());

                    String jsspType = StringUtil.toLowerCase(request.getParameter(Action.JSSP_TYPE));

                    httpResponse.setCharacterEncoding(ContentTypes.UTF8_CHARSET);
                    httpResponse.setStatus(HttpConstants.STATUS_SUCCESS);
                    httpResponse.setContentType(("text".equals(jsspType) || "txt".equals(jsspType)) ? ContentTypes.PLAIN_AND_UTF8 : ContentTypes.HTML_AND_UTF8);
                    httpResponse.getWriter().print(bw.getBufferedString());
                } else {
                    // response.set
                    httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    httpResponse.setContentType("text/plain");
                    httpResponse.getWriter().print("Resource not found: " + fpath);
                }
            } catch (Throwable t) {
                logTool.error("JSSPFilter error:", t);
                StringBuilder sb = new StringBuilder();
                sb.append("<html>\n<head>\n\t<title>Error 500</title>\n</head>\n");
                sb.append("<body bgcolor=\"#ffffff\">\n<h1>Status Code: 500</h1>\nException: <br>\nStacktrace: \n<pre>");
                sb.append(HtmlEscapeUtil.escapeTags(ExceptionUtil.printStackTrace(t)));
                sb.append("</pre>\n<br>\n<hr size=\"1\" width=\"90%\">\n<i>Generated by Winstone Servlet Engine v0.9.10 at "
                          + new Date() + "</i>\n</body>\n</html>");
                httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                httpResponse.setContentType(ContentTypes.HTML_AND_UTF8);
                httpResponse.getWriter().print(sb.toString());
            }
        } else {
            chain.doFilter(httpRequest, response);
        }
    }

    public void destroy() {
    }

}
