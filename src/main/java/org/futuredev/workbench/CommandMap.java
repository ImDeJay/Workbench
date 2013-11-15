package org.futuredev.workbench;

import org.futuredev.workbench.command.Arguments;
import org.futuredev.workbench.command.CommandBody;
import org.futuredev.workbench.command.annotation.Command;
import org.futuredev.workbench.command.annotation.Documentation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.SimplePluginManager;
import org.futuredev.workbench.command.reflective.AnnotationCommand;
import org.futuredev.workbench.command.reflective.DynamicCommand;
import org.futuredev.workbench.command.reflective.RegistryException;

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
                                  aliasAliases = new HashMap<String, List<String>>(); // alias-ception!
    HashMap<String, String[]> commandAliases = new HashMap<String, String[]>();

    boolean injected = false;

    public DynamicCommand getCommand (String match) {
        if (!map.containsKey(match))
            for (String command : aliases.keySet()) {
                if (match.matches("(i?)" + command))
                    match = command;
            }

        return map.get(match);
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
     * @param method
     * @return
     */
    public CommandMap registerCommand (Method method) throws RegistryException {
        Command command = null;
        for (Annotation a : method.getAnnotations())
            if (a instanceof Command)
                command = (Command) a;

        if (command == null)
            return this;

        String[] names = command.aliases();

        if (names.length == 0) // No name? Use the method!
            names = new String[] { method.getName().toLowerCase() };

        String main = names[0];

        Method doc = null;

        loop: for (Method m : method.getClass().getDeclaredMethods())
            for (Annotation a : m.getAnnotations())
                if (a instanceof Documentation && ((Documentation) a).value().equalsIgnoreCase(main)) {
                    doc = m;
                    break loop;
                }


        if (!this.map.containsKey(names[0])) {
            this.map.put(names[0], new AnnotationCommand(method));
            this.aliases.put(names[0], new ArrayList<String>());
            for (int i = 1; i < names.length; ++i) {
                if (!this.aliases.get(names[0]).contains(names[i]))
                    this.aliases.get(names[0]).add(names[i]);
            }
        }

        return this;
    }

    /**
     * Registers all @Command methods in a class.
     * @param clazz The class to check for.
     * @return This, for stacking.
     */
    public CommandMap registerCommands (Class<? extends CommandBody> clazz) throws RegistryException {
        loop: for (Method method : clazz.getDeclaredMethods()) {

            Class[] classes = method.getParameterTypes();
            if (!Session.class.isAssignableFrom(classes[0])
                    || !Arguments.class.isAssignableFrom(classes[1])) // We must have those two
                continue;

            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof Deprecated) // Allow temporary deprecation
                    continue loop;

                if (annotation instanceof Command) {
                    Command cmd = (Command) annotation;
                    if (cmd.aliases().length < 1)
                        continue loop;

                    this.registerCommand(method);
                }
            }
        }

        return this;
    }


    public boolean isAlias (String command) {


        return false;
    }

    /**
     * Injects all commands here into Bukkit's command map so consoles can join the fun.
     * @return This, for stacking.
     */
    public CommandMap inject () {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            org.bukkit.command.CommandMap map =
                    (org.bukkit.command.CommandMap) field.get(Bukkit.getServer().getPluginManager());

            for (String command : this.map.keySet()) {
                map.register(command, this.map.get(command));
            }

            injected = true;
      } catch (final Throwable t) {
            System.out.println("Error: Unable to access command map for dynamic registration.");
            t.printStackTrace();
        }

        return this;
    }

    public boolean isInjected () {
        return injected;
    }

}