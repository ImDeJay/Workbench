package com.futuredev.utilities.command;

import com.futuredev.utilities.localisation.MessageAssist;

import java.text.MessageFormat;

public class CommandException extends Throwable {

    private static final long serialVersionUID = -4025020263578696713L;

    boolean showUsage;
    String messagePath;
    Object[] params;


    public CommandException(String message, Object... params) {
        this (false, message, params);
    }

    public CommandException(boolean usage, String message, Object... params) {
        this.showUsage = usage;
        this.messagePath = message;
        this.params      = params;
    }

    public boolean showUsage () {
        return showUsage;
    }

    public String getMessage (MessageAssist language) {
        return language.read(messagePath, params);
    }

}