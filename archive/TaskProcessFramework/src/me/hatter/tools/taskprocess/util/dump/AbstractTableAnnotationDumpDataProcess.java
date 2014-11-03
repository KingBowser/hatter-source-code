package me.hatter.tools.taskprocess.util.dump;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.taskprocess.util.dump.annotation.TableOptions;
import me.hatter.tools.taskprocess.util.misc.StringUtils;

public abstract class AbstractTableAnnotationDumpDataProcess extends AbstractAnnotationDumpDataProcess {

    protected String getTable() {
        return getTableOptions().table();
    }

    protected List<String> getFields() {
        String fields = this.getTableOptions().fields();
        String[] fieldas = fields.split(",");
        List<String> fieldList = new ArrayList<String>(fieldas.length);
        for (String field : fieldas) {
            field = field.trim();
            if (StringUtils.isNotEmpty(field)) {
                fieldList.add(field);
            }
        }
        if (fieldList.isEmpty()) {
            throw new RuntimeException("Field list can not be empty.");
        }
        return fieldList;
    }

    protected String getWhere() {
        String where = getTableOptions().where();
        return StringUtils.isEmpty(where) ? null : where;
    }

    protected TableOptions getTableOptions() {
        TableOptions options = this.getClass().getAnnotation(TableOptions.class);
        if (options == null) {
            throw new RuntimeException("Table options is not assigned.");
        }
        return options;
    }
}
