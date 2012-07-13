package me.hatter.tools.jflag;

public interface JFlagCommandHandler {

    void handle(JFlagCommand command, Boolean isOn, String args);
}
