package me.hatter.tools.jsspserver.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthMark {

    public static final String SESSION_AUTH_KEY = "sessionAuthKey";

    String[] value();
}
