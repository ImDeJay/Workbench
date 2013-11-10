package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.CommandAlias;

import java.lang.annotation.Annotation;

public class CommandAliasInstance implements Annotation, CommandAlias {

    String value;

    public CommandAliasInstance (String value) {
        this.value = value;
    }

    public String value () {
        return value;
    }

    public Class<? extends Annotation> annotationType () {
        return CommandAlias.class;
    }

}