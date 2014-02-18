package me.hatter.tools.resourceproxy.jsspserver.handler;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public interface HttpResquestProcessor {

    HttpResponse process(HttpRequest request) throws Exception;
}
