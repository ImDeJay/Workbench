package org.futuredev.workbench.command.annotation.instance;

import org.futuredev.workbench.command.annotation.FirstMatch;

import java.lang.annotation.Annotation;

public class FirstMatchInstance implements Annotation, FirstMatch {

    String[] value;

    public FirstMatchInstance (String[] value) {
        this.value = value;
    }

    public String[] value () {
        return this.value;
    }

    public Class<? extends Annotation> annotationType () {
        return FirstMatch.class;
    }

    public static boolean valid (String validate, FirstMatch validator) {
        for (String possibility : validator.value()) {
            if (possibility.charAt(0) == '^' ? validate.matches(possibility.substring(1))
                    : validate.equalsIgnoreCase(possibility)) // Expression match as well
                return true;
        }

        return false;
    }

}