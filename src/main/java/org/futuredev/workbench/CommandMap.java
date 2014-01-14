package org.futuredev.workbench;

import org.futuredev.workbench.command.Arguments;
import org.futuredev.workbench.command.CommandBody;
import org.futuredev.workbench.command.annotation.Command;
import org.futuredev.workbench.command.annotation.Documentation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.SimplePluginManager;
import org.futuredev.workbench.command.doc.DynamicDocumentation;
import org.futuredev.workbench.command.reflective.AnnotationCommand;
import org.futuredev.workbench.command.reflective.DynamicCommand;
import org.futuredev.workbench.command.reflective.RegistryException;
import org.futuredev.workbench.helper.ClassMap;
import org.futuredev.workbench.session.Session;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a command map.
 *
 * @author afistofirony
 */
public class CommandMap {

    HashMap<String, DynamicCommand> map = new HashMap<String, DynamicCommand>();
    HashMap<String, List<String>> aliases = new HashMap<String, List<String>>(),
                                  aliasAliases = new HashMap<String, List<String>>();
    HashMap<String, String[]> commandAliases = new HashMap<String, String[]>();
    HashMap<String, CommandBody> instances = new HashMap<String, CommandBody>();

    boolean injected = false;

    /**
     * Gets the most specific method matching this set of arguments.
     * @param args A set of arguments to match.
     * @return The method
     */
    public DynamicCommand getCommand (String... args) {
        if (args.length == 0)
            return null;

        if (args[0].charAt(0) == '/')
            args[0] = args[0].substring(1);

        AnnotationCommand deepest = (AnnotationCommand) map.get(args[0]);
        int i = 1;
        do {
           deepest = deepest.getSubcommand(args[i++]);
      } while (deepest != null && deepest.hasSubcommands());

        return deepest;
    }

    public DynamicCommand getCommandDocs (String match) {
        if (!map.containsKey(match))
            for (String command : aliases.keySet()) {
                if (match.matches("(i?)" + command))
                    match = command;
            }

        return map.get(match);
    }

    /**
     * Registers a method.
     * @param instance The instance to call when executing this command.
     * @param method The method to register.
     * @return This, for stacking.
     */
    public CommandMap registerCommand (CommandBody instance, Method method) throws RegistryException {
        Class[] classes = method.getParameterTypes();
        if (!Session.class.isAssignableFrom(classes[0])
                || !Arguments.class.isAssignableFrom(classes[1])) // We must have those two
            return this;

        if (method.isAnnotationPresent(Command.class)) {
            AnnotationCommand command = new AnnotationCommand(method);
            map.put(command.getName(), command);
            this.instances.put(command.getName(), instance);
        }

        return this;
    }

    /**
     * Registers all @Command methods in a class.
     * @param instances The class instances to check in.
     * @return This, for stacking.
     */
    public CommandMap registerCommands (CommandBody... instances) throws RegistryException {
        for (CommandBody body : instances) {
            if (this.instances.containsValue(body))
                continue;

            Class<? extends CommandBody> clazz = body.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                this.registerCommand(body, method);
            }
        }

        return this;
    }

    public boolean isAlias (String command) {


        return false;
    }

    /**
     * Injects all commands here into Bukkit's command map so consoles can join the fun.
     * @return Whether or not injection was successful.
     */
    public boolean inject () {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            org.bukkit.command.CommandMap map =
                    (org.bukkit.command.CommandMap) field.get(Bukkit.getServer().getPluginManager());

            for (String command : this.map.keySet()) {
                map.register(command, this.map.get(command));
            }

            return injected = true;
      } catch (final Throwable t) {
            System.out.println("Error: Unable to access command map for dynamic registration.");
            t.printStackTrace();
            return false;
        }
    }

    public boolean isInjected () {
        return injected;
    }

}