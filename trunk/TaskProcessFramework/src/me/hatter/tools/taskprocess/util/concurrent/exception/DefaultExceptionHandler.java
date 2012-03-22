package me.hatter.tools.taskprocess.util.concurrent.exception;

import me.hatter.tools.taskprocess.util.misc.ExceptionUtils;

public class DefaultExceptionHandler implements ExceptionHandler {

    public void handle(Exception e) {
        System.out.println("[ERROR] DefaultExceptionHandler; Unknow exception occured thread:"
                           + Thread.currentThread().getName() + " #" + Thread.currentThread().getId() + ", exception: "
                           + ExceptionUtils.getStackTrace(e));
    }
}
