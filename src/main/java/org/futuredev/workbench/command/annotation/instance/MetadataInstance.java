package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Metadata;

import java.lang.annotation.Annotation;

public class MetadataInstance implements Annotation, Metadata {

    public MetadataInstance () {}

    public Class<? extends Annotation> annotationType () {
        return Metadata.class;
    }

}