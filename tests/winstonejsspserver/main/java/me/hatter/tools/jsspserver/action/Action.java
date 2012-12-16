package me.hatter.tools.jsspserver.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {

    public static final String JSSP_ACTION = "jsspaction";

    Map<String, Object> doAction(HttpServletRequest request, HttpServletResponse response);
}
