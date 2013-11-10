package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Documentation;

import java.lang.annotation.Annotation;

public class DocumentationInstance implements Annotation, Documentation {

    String value;

    public DocumentationInstance (String value) {
        this.value = value;
    }

    public String value () {
        return this.value;
    }

    public Class<? extends Annotation> annotationType () {
        return Documentation.class;
    }

}