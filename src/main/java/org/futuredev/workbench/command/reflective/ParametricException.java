package org.futuredev.workbench.command.reflective;

import org.futuredev.workbench.command.CommandException;

public class ParametricException extends CommandException {

    public ParametricException (String message, Object... params) {
        super(message, params);
    }

}