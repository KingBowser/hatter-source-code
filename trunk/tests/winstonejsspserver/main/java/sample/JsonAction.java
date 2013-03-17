package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.jsspserver.action.BaseAction;

public class JsonAction extends BaseAction {

    @Override
    protected void doAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("3");
        list.add("5");
        returnJson(list);
    }
}
