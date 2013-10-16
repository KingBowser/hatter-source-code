package me.hatter.tools.resourceproxy.form.fieldsource.impl;

import java.util.Arrays;
import java.util.List;

import me.hatter.tools.resourceproxy.form.fieldsource.FormFieldSource;
import me.hatter.tools.resourceproxy.form.fieldsource.FormFieldSourceItem;
import me.hatter.tools.resourceproxy.form.fieldsource.SimpleFormFieldSourceItem;

public class YNoFormFieldSourceImpl implements FormFieldSource {

    public List<? extends FormFieldSourceItem> getItems() {
        return Arrays.asList(new SimpleFormFieldSourceItem("Yes", "Y"), //
                             new SimpleFormFieldSourceItem("No", "N"));
    }
}
