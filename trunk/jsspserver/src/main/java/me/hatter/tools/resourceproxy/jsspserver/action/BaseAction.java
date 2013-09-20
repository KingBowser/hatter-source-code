package me.hatter.tools.resourceproxy.jsspserver.action;

import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public abstract class BaseAction implements Action {

    // @Override
    public Map<String, Object> doAction(HttpRequest request, HttpResponse response) {
        Map<String, Object> context = new HashMap<String, Object>();
        doAction(request, response, context);
        return context;
    }

    abstract protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context);
}
