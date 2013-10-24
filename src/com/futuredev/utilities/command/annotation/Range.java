package com.futuredev.utilities.command.annotation;

public @interface Range {

    double min () default -1;
    double max () default -1;
    double def () default java.lang.Double.MAX_VALUE;

}
