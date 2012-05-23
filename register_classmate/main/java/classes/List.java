package classes;

import java.util.ArrayList;
import java.util.Map;

import me.hatter.tools.registerclassmate.objects.ClassMate;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class List extends BaseAction {

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        if ("helloworld".equals(request.getQueryValue("pass"))) {
            java.util.List<ClassMate> cms = DataAccessObject.listObjects(ClassMate.class, "1=1", null);
            context.put("cms", cms);
        } else {
            context.put("cms", new ArrayList<ClassMate>());
        }
    }
}
