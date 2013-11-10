package org.futuredev.workbench.command.reflective;

import org.futuredev.workbench.Session;
import org.futuredev.workbench.command.Arguments;
import org.futuredev.workbench.command.CommandException;

public abstract class DynamicCommand {

    abstract void handle (Session user, Arguments args) throws CommandException;

    abstract void document (Session user);

}