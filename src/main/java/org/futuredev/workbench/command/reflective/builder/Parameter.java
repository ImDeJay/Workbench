package org.futuredev.workbench.command.reflective.builder;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a parameter.
 *
 * @author afistofirony
 */
public class Parameter {

    HashMap<Class<?>, Annotation> annotations;
    Class<?> type;
    boolean array;
    String title;

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

    public boolean hasAnnotation (Class<? extends Annotation>... classes) {
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

    @SuppressWarnings("unchecked")
    public <K extends Annotation> K get (Class<K> type) {
        return (K) annotations.get(type);
    }

    public boolean isArray () {
        return this.array;
    }

    public boolean linkedTo (Class<?> type) {
        return type.isAssignableFrom(this.type);
    }

    public Class<?> getType () {
        return this.type;
    }

}