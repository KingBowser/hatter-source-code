package me.hatter.test.springmvcproject.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * (created at 2010-7-20)
 * 
 * @author <a href="mailto:shuo.qius@alibaba-inc.com">QIU Shuo</a>
 * @author wenfeng.cenwf 2011-4-7
 */
public class TestAction extends SimpleFormController implements InitializingBean {

    @SuppressWarnings("unused")
    private static class Items {

        private int item1;
        private int item2;

        public int getItem1() {
            return item1;
        }

        public void setItem1(int item1) {
            this.item1 = item1;
        }

        public int getItem2() {
            return item2;
        }

        public void setItem2(int item2) {
            this.item2 = item2;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setCommandClass(Items.class);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
                                    BindException errors) throws Exception {

        Items form = (Items) command;
        int sum = form.getItem1() + form.getItem2();

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("sum", sum);

        // response.sendRedirect("");
        return new ModelAndView("test_page", model);
    }
}
