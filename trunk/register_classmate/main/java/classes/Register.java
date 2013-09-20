package classes;

import java.util.Map;

import me.hatter.tools.registerclassmate.objects.ClassMate;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.action.BaseAction;

public class Register extends BaseAction {

    protected void doAction(HttpRequest request, HttpResponse response, Map<String, Object> context) {
        ClassMate cm = new ClassMate();
        cm.setName(request.getQueryValue("p.name"));
        cm.setYear(Integer.valueOf(request.getQueryValue("p.year")));
        cm.setMonth(Integer.valueOf(request.getQueryValue("p.month")));
        cm.setDate(Integer.valueOf(request.getQueryValue("p.date")));
        cm.setTel1(request.getQueryValue("p.tel1"));
        cm.setTel2(request.getQueryValue("p.tel2"));
        cm.setTel3(request.getQueryValue("p.tel3"));
        cm.setQq(request.getQueryValue("p.qq"));
        cm.setWechat(request.getQueryValue("p.wechat"));
        cm.setMsn(request.getQueryValue("p.msn"));
        cm.setTalk(request.getQueryValue("p.talk"));
        cm.setOther(request.getQueryValue("p.other"));
        cm.setSchool(request.getQueryValue("p.school"));
        cm.setWork(request.getQueryValue("p.work"));
        cm.setMemo(request.getQueryValue("p.memo"));
        me.hatter.tools.registerclassmate.main.Main._DAO.insertObject(cm);

        response.redirect("success.jssp");
    }
}
