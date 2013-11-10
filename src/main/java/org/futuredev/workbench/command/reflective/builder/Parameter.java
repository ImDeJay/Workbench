package org.futuredev.workbench.command.reflective.builder;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

public class Parameter {

    HashMap<Class<?>, Annotation> annotations;
    Class<?> type;
    boolean array;

    public Parameter () {
        this.annotations = new HashMap<Class<?>, Annotation>();
        this.type        = String.class;
        this.array       = false;
    }

    public Parameter (Object derive) {
        this();
        this.type  = derive.getClass();
        this.array = derive.getClass().isArray();
    }

    public HashMap<Class<?>, Annotation> getAnnotations () {
        return annotations;
    }

    public boolean hasAnnotation (Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (!Annotation.class.isAssignableFrom(clazz))
                continue;

            if (annotations.containsKey(clazz))
                return true;
        }

        return false;
    }

    public Parameter append (Annotation annot) {
        if (!this.annotations.containsKey(annot.annotationType()))
            annotations.put(annot.annotationType(), annot);
        return this;
    }

    public Annotation get (Class<? extends Annotation> type) {
        return annotations.get(type);
    }

    public boolean isArray () {
        return this.array;
    }

    public boolean linkedTo (Class type) {
        return this.type.isAssignableFrom(type);
    }

}