package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.Resolver;
import org.futuredev.workbench.command.reflective.Resolvable;

import java.lang.annotation.Annotation;

public class ResolverInstance implements Annotation, Resolver {

    Class<? extends Resolvable> resolver;

    public ResolverInstance (Class<? extends Resolvable> resolver) {
        this.resolver = resolver;
    }

    public Class<? extends Resolvable> value () {
        return this.resolver;
    }

    public Class<? extends Annotation> annotationType () {
        return Resolver.class;
    }

}