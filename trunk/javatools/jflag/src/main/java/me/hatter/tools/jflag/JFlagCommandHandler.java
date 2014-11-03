package me.hatter.tools.jflag;

public interface JFlagCommandHandler {

    String handle(JFlagCommand command, Boolean isOn, String args);
}
