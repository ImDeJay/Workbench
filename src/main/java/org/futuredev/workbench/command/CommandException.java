package org.futuredev.workbench.command;

import org.futuredev.workbench.localisation.MessageAssist;

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
        if (messagePath.contains(" ")) {
            this.messagePath = "NoL10N";
            this.params = new String[params.length + 1];
            this.params[0] = message;
            for (int i = 0; i < params.length; ++i) {
                this.params[i + 1] = params[i];
            }
      } else {
            this.messagePath = message;
            this.params      = params;
        }

    }

    public boolean showUsage () {
        return showUsage;
    }

    public String getMessage (MessageAssist language) {
        return language.read(messagePath, params);
    }

}