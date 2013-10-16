package me.hatter.tools.resourceproxy.form.fieldsource;

public class SimpleFormFieldSourceItem implements FormFieldSourceItem {

    private String label;
    private String value;

    public SimpleFormFieldSourceItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
