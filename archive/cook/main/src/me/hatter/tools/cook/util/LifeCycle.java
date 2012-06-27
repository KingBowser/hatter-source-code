package me.hatter.tools.cook.util;

public interface LifeCycle {

    void fail(String errorMessage, String deailMessage);

    void fail(Throwable t);
}
