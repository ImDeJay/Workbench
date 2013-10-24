package com.futuredev.utilities.command.annotation;

import com.futuredev.utilities.Session;
import com.futuredev.utilities.command.Arguments;

public class Example {

    @Command ( aliases = { "example", "ex" }, simple = "Just an example!",
               console = true, illegal = {})
    @Permission ("example.node")
    public void execute (Session user, Arguments args, @Expressed("(i?)") String value,
                         @Range(min = 5.0, max = 1.0) double radius) {

    }


}