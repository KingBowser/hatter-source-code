package me.hatter.tools.jsspserver.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.jsspserver.dto.Page;
import me.hatter.tools.jsspserver.util.RequestUtil;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;

public abstract class AbstractPagingAction<Q extends Page, I> extends DatabaseAction {

    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface PagingActionParam {

        int pageSize() default 10;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void doAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {

        PagingActionParam pagingActionParam = this.getClass().getAnnotation(PagingActionParam.class);

        Class<Q> classQ = getGenericTypeQ();
        Class<I> classI = getGenericTypeI();

        Page query = RequestUtil.parse(request, classQ);
        query.setPageSize((pagingActionParam == null) ? 10 : pagingActionParam.pageSize());

        List<String> where = new ArrayList<String>();
        List<Object> params = new ArrayList<Object>();
        StringBuilder orderby = new StringBuilder();

        processParams((Q) query, where, params, orderby);

        String whereStr = StringUtil.join(where, " and ");

        int count = getDao().countObject(classI, whereStr, params);
        query.setTotalCount(count);

        String orderbyAndLimit = getOrderbyAndLimit((Q) query, orderby, params);
        List<I> iList = (List) getDao().listObjects(classI, whereStr, orderbyAndLimit, params);

        context.put("query", query);
        context.put("evaluationList", iList);

        postAction(request, response, context, (Q) query, iList);
    }

    @SuppressWarnings("unchecked")
    protected Class<Q> getGenericTypeQ() {
        return (Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    protected Class<I> getGenericTypeI() {
        return (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected DataAccessObject getDao() {
        return DAO;
    }

    protected String getOrderbyAndLimit(Q query, StringBuilder orderby, List<Object> params) {
        params.add(query.getPageIndex());
        params.add(query.getPageSize());
        if (orderby.length() == 0) {
            return "id desc limit ?,?";
        } else {
            return orderby + " limit ?,?";
        }
    }

    protected void postAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context,
                              Q query, List<I> iList) {
    }

    abstract protected void processParams(Q query, List<String> where, List<Object> params, StringBuilder orderby);
}
