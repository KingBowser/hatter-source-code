package me.hatter.tools.resourceproxy.form.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.hatter.tools.resourceproxy.form.fieldsource.FormFieldSource;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormField {

    String title() default "";

    FormFieldType type() default FormFieldType.TEXT;

    FormFieldEditable editable() default FormFieldEditable.DEFAULT;

    Class<? extends FormFieldSource> source() default FormFieldSource.class;
}
