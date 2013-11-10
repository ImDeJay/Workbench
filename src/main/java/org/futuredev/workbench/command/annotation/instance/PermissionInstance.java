package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Permission;

import java.lang.annotation.Annotation;

public class PermissionInstance implements Annotation, Permission {

    String value;

    public PermissionInstance (String value) {
        this.value = value;
    }

    public String value () {
        return this.value;
    }

    public Class<? extends Annotation> annotationType () {
        return Permission.class;
    }

}