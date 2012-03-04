package me.hatter.tools.resourceproxy.jsspserver.action;

import java.util.Map;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public interface Action {

    Map<String, Object> doAction(HttpRequest request, HttpResponse response);

}
