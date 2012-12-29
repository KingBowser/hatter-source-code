package me.hatter.test.springmvcproject.web.ajax;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.HttpRequestHandler;

public class TestAjax implements HttpRequestHandler, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                       IOException {
        response.setHeader("Content-Type", "text/json; charset=utf-8");
        OutputStream out = response.getOutputStream();
        out.write("[\"item1\", \"item2\", \"item3\"]".getBytes("utf-8"));
        out.flush();
    }

}
