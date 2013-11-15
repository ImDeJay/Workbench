package org.futuredev.workbench.command.reflective;

import org.futuredev.workbench.Session;
import org.futuredev.workbench.command.Arguments;
import org.futuredev.workbench.command.CommandException;
import org.futuredev.workbench.command.annotation.Command;
import org.futuredev.workbench.command.annotation.CommandAlias;
import org.futuredev.workbench.command.reflective.builder.ParametricRequirements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AnnotationCommand extends DynamicCommand {

    /* COMMENTED: Annoying when programming. // final */ Method root;
    boolean deprecated = false;
    int rootIndex;
    ParametricRequirements defaultArgs;
    HashMap<String, AnnotationCommand> subcommands;
    String pathTo;

    public AnnotationCommand (Method rt) throws RegistryException {
        this.root = rt;

        if (root.isAnnotationPresent(Deprecated.class)) {
            deprecated = true;
            return;
        }

        String[] aliases, illegal = {};
        String simple = null;
        boolean console = true;

        if (!root.isAnnotationPresent(Command.class)) {
            if (root.isAnnotationPresent(CommandAlias.class)) { // Define value by name of method
                pathTo = root.getAnnotation(CommandAlias.class).value();
                aliases = new String[] { root.getName().toLowerCase() };
                console = true;
            }

            else throw new RegistryException("This is not a valid command. To be recognised as" +
                    " such, a command must either have the @Command or the " +
                    "@CommandAlias annotation.");
      } else {
            Command data = root.getAnnotation(Command.class);
            aliases = data.aliases();
            illegal = data.illegal();
            simple  = data.simple();
            console = data.console();
        }

        if (aliases.length == 0) // Just @Command, nothing else?
            aliases = new String[] { root.getName().toLowerCase() };

        Annotation[][] annotations = root.getParameterAnnotations();
        Class[] parameters         = root.getParameterTypes();

        if (!Session.class.isAssignableFrom(parameters[0]))
            throw new RegistryException("Unable to register command - it does not accept a sender.");

        if (!Arguments.class.isAssignableFrom(parameters[1]))
            throw new RegistryException("Unable to register command - it does not accept arguments.");




    }

    private ParametricRequirements match (String[] args) {


        return null;
    }


    public void handle (Session user, Arguments args) throws CommandException {
        if (this.deprecated)
            throw new CommandException("This command is currently disabled.");



    }

    public void document (Session user) {

    }

    public void inject () {

    }


}