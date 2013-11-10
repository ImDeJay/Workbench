package org.futuredev.workbench.command.reflective.builder;

import org.futuredev.workbench.command.Arguments;
import org.futuredev.workbench.command.CommandException;
import org.futuredev.workbench.command.annotation.*;
import org.futuredev.workbench.command.annotation.instance.FirstMatchInstance;
import org.futuredev.workbench.command.reflective.ParametricException;

import java.util.ArrayList;

/**
 * Compiles a fulfilling set of Objects
 * based on an existing set of parametric
 * requirements.
 *
 * @author afistofirony
 */
public class ParametricCompiler {

    ArrayList<Object> result;

    public ParametricCompiler (Arguments base, ParametricRequirements criteria) throws CommandException {
        this (base, criteria, 0);
    }

    public ParametricCompiler (Arguments base, ParametricRequirements criteria, int argRoot)
        throws CommandException {

        result = new ArrayList<Object>();
        int argIndex = argRoot;

        int optionalCount = 0, minLength = 0, optionalsUsedUp = 0;
        for (Parameter param : criteria.requirements) {
            if (param.hasAnnotation(Default.class) && !param.hasAnnotation(Flag.class, FirstMatch.class,
                    Expressed.class))
                ++optionalCount;
            else ++minLength;
        }

        if (optionalCount > 0)
            throw new ParametricException("No more than one open-value optional argument can be" +
                    " dynamically processed in order to avoid ambiguity during parametric compilation.");
        if (base.length() < minLength)
            throw new CommandException("Exception.InsufficientArguments", minLength,
                    (minLength == 1 ? "§Plurals.argument" : "§Plurals.arguments"));

        for (Parameter param : criteria.requirements) {

            int incrementRate = 0;

            String value = "";

            /**
             * First things first, let's deal with compound
             * arguments. These should be pretty simple.
             */
            if (param.hasAnnotation(Compound.class)) {
                if (param.hasAnnotation(Range.class)) {
                    Range range = (Range) param.get(Range.class);
                    for (int i = argIndex; i < ((int) range.max()) && i < base.length(); ++i) {
                        value = value + " " + base.get(i);
                    }

                    value = value.substring(1);
                    incrementRate = (int) range.max() - argIndex;
              } else {
                    int i;
                    for (i = argIndex; i < base.length(); ++i) {
                        value = value + " " + base.get(i);
                    }

                    value = value.substring(1);
                    incrementRate = i;
                }
            }

            if (param.hasAnnotation(Complex.class)) {
                ++argIndex;
                continue;
            }

            /**
             * Next issue, and a rather pressing one - optional
             * values. How exactly are we supposed to deal with
             * /command [1] [2] <3>?
             *
             * This can be solved by:
             * - Requiring said arguments to provide a list of
             *   legal formats (i.e. @FirstMatch). If the specified
             *   value is invalid, assume it's not the droid
             *   we're looking for.
             *
             *   FirstMatch allows us to use as many subcommands
             *   as we want because we can restrict input.
             *
             * - Subtracting from the number of required arguments,
             *   i.e.
             *
             *   public void example (Session user, Arguments args,
             *           @Default ("afistofirony") Player moved, Player target) {
             *
             *
             *   }
             *
             *   Valid syntax:
             *       /example player1 player2
             *       /example player2
             *
             *   This needs to be limited to one argument at maximum, or we
             *   risk ambiguity.
             */
             if (param.hasAnnotation(Default.class)) {
                 // This allows us to verify if it's OK for this sub-arg not to match
                 boolean canRiskMatchFailure = base.length() - (optionalCount + optionalsUsedUp) > 1;


                 if (param.hasAnnotation(FirstMatch.class)) {
                     FirstMatch match = (FirstMatch) param.get(FirstMatch.class);
                     if (!FirstMatchInstance.valid(value, match)) {
                         if (FirstMatchInstance.valid(((Default) param.get(Default.class)).value(), match))
                             value = ((Default) param.get(Default.class)).value();
                         else throw new ParametricException("Logical failure: @Default value provided" +
                                 " does not match paired @FirstMatch annotation."); // Don't be ignorant, developers.
                     }
               } else if (param.hasAnnotation(Expressed.class)) {
                     Expressed expression = (Expressed) param.get(Expressed.class);
                 }

                 ++optionalsUsedUp;
             }





            argIndex += incrementRate;
        }


    }



}