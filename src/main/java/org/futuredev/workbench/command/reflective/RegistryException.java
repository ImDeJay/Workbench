package org.futuredev.workbench.command.reflective;

import org.futuredev.workbench.command.CommandException;

public class RegistryException extends CommandException {

    public RegistryException (String message, Object... params) {
        super(message, params);
    }


}