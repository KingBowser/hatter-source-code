package me.hatter.tools.commons.converter;

public @interface ConverterMark {

    Class<?> type() default Object.class;
}
