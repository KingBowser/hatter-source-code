package me.hatter.tools.jsspserver.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;

public class WelcomeFilter implements Filter {

    private static final LogTool logTool              = LogTools.getLogTool(WelcomeFilter.class);

    public static final String   DEFAULT_WELCOME_PAGE = "/index.jssp";
    private String               welcomePage          = DEFAULT_WELCOME_PAGE;
    private FilterConfig         filterConfig;

    public void destroy() {
        filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
                                                                                                   ServletException {
        request.setCharacterEncoding(FilterTool.DEFAULT_CHARACTER_ENCODING);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if ("/".equals(httpRequest.getServletPath())) {
            httpResponse.sendRedirect(welcomePage);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String wp = this.filterConfig.getInitParameter("welcome-page");
        if (StringUtil.isNotEmpty(wp)) {
            this.welcomePage = wp;
        }
        if (logTool.isInfoEnable()) {
            logTool.info("Welcome page: " + this.welcomePage);
        }
    }
}
