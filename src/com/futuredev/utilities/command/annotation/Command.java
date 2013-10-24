package com.futuredev.utilities.command.annotation;

public @interface Command {

    String[] aliases () default {};
    String simple () default "";

    boolean console () default true;

    String[] illegal () default {};

}
