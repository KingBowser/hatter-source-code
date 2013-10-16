package me.hatter.tools.resourceproxy.form.fieldsource;

import java.util.List;

public interface FormFieldSource {

    List<? extends FormFieldSourceItem> getItems();
}
