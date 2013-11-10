package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Command;

import java.lang.annotation.Annotation;

public class CommandInstance implements Annotation, Command {

    String[] aliases = {};
    String simple = "";
    boolean console = true;
    String[] illegal = {};

    public CommandInstance (String[] aliases, String simple, boolean console, String... illegal) {
        this.aliases = aliases;
        this.simple  = simple;
        this.console = console;
        this.illegal = illegal;
    }

    public String[] aliases () {
        return this.aliases;
    }

    public String simple () {
        return this.simple;
    }

    public boolean console () {
        return console;
    }

    public String[] illegal () {
        return illegal;
    }

    public Class<? extends Annotation> annotationType () {
        return Command.class;
    }

}