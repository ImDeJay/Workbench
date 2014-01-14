package org.futuredev.workbench.command.reflective.builder;

import org.futuredev.workbench.command.Arguments;
import org.futuredev.workbench.command.CommandException;
import org.futuredev.workbench.command.annotation.*;
import org.futuredev.workbench.command.annotation.instance.FirstMatchInstance;
import org.futuredev.workbench.command.reflective.ParametricException;
import org.futuredev.workbench.session.Session;

import java.util.ArrayList;

/**
 * Compiles a fulfilling set of Objects
 * based on an existing set of parametric
 * requirements.
 *
 * @author afistofirony
 */
@SuppressWarnings("unchecked")
public class ParametricCompiler {

    Session user;
    ArrayList<Object> result;

    public ParametricCompiler (Session user, Arguments base, ParametricRequirements criteria)
            throws CommandException {
        this (user, base, criteria, 0);
    }

    public ParametricCompiler (Session user, Arguments base, ParametricRequirements criteria, int argRoot)
            throws CommandException {

        result = new ArrayList<Object>();
        this.user = user;
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
                    Range range = param.get(Range.class);
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
             *   This needs to be limited to one argument at maximum (of identical inferred (and restricted)
             *   format -- i.e. we can have @Default("5") int value, @Default("true") boolean s), or we
             *   risk ambiguity.
             */
             if (param.hasAnnotation(Default.class)) {
                 // This allows us to verify if it's OK for this sub-arg not to match
                 boolean canRiskMatchFailure = base.length() - (optionalCount + optionalsUsedUp) > 1;


                 if (param.hasAnnotation(FirstMatch.class)) {
                     FirstMatch match = param.get(FirstMatch.class);
                     if (!FirstMatchInstance.valid(value, match)) {
                         if (FirstMatchInstance.valid((param.get(Default.class)).value(), match))
                             value = (param.get(Default.class)).value();
                         else throw new ParametricException("Logical failure: @Default value provided" +
                                 " does not match paired @FirstMatch's values."); // Don't be ignorant, developers.
                     }
               } else if (param.hasAnnotation(Expressed.class)) {
                     Expressed expression = param.get(Expressed.class);
                 }

                 ++optionalsUsedUp;
             }





            argIndex += incrementRate;
        }

        /**
         * The way we work through parameters is pretty simple.
         *
         * There are two types of parameters, those with @Metadata
         * and those without.
         *
         * Parameters with @Metadata are processed as additional information
         * instead of being drawn from the arguments given.
         *
         *
         * Now, the way this actually works is through iteration through each parameter.
         *
         * We check if a parameter has @Metadata.
         *   If it does, we try to fill it in using the Resolvable<T> interface and null as the value.
         *   If it doesn't, we try to fill it in using the Resolvable<T> interface and the appropriate argument
         *     as the value.
         *
         * As for optional arguments:
         *   If an optional argument is missing, the @Default value is given instead.
         *   If there are two or more optional arguments in a row, each must have a unique format.
         *     This can be achieved by doing one of the following things:
         *       - Using @FirstMatch or @Expressed (and NOT having the same value/s for both)
         *       - Using types with different required formats (i.e. boolean and int)
         */
        for (Parameter param : criteria.requirements) {
            int index = 0;

            if (param.hasAnnotation(Metadata.class)) {
                result.add(this.resolve(param, null));
                continue;
            }

            Object value = "";

            if (param.hasAnnotation(Flag.class)) { // clunky
                Flag f = param.get(Flag.class);
                if (boolean.class.isAssignableFrom(param.getType())) {
                    if (param.hasAnnotation(Permission.class)) {
                        Permission perm = param.get(Permission.class);
                        if (base.hasFlag(f.value()))
                            if (!user.hasPermission(perm.value()))
                                if (!param.hasAnnotation(Silent.class))
                                    throw new CommandException("PermissionError");
                                else value = false;
                            else value = true;

                        else value = false;
                    }

                    else value = base.hasFlag(f.value());
              } else {
                    if (param.hasAnnotation(Permission.class)) {
                        Permission perm = param.get(Permission.class);
                        if (base.hasFlag(f.value()) && base.get(f.value()).hasValue())
                            if (!user.hasPermission(perm.value()))
                                if (!param.hasAnnotation(Silent.class))
                                    throw new CommandException("PermissionError");
                                else value = this.resolve(param, param.hasAnnotation(Default.class) ?
                                        param.get(Default.class).value() : null);
                            else if (param.hasAnnotation(Expressed.class))
                                if (!base.get(f.value()).getValue().matches(param.get(Expressed.class).value()))
                                    if (!param.hasAnnotation(Silent.class))
                                        throw new CommandException("CommandException.Expressed",
                                                param.get(Expressed.class).value());
                                    else value = this.resolve(param, param.hasAnnotation(Default.class) ?
                                            param.get(Default.class).value() : null);
                                else value = this.resolve(param, base.get(f.value()).getValue());
                            else if (param.hasAnnotation(FirstMatch.class))
                                if (!FirstMatchInstance.valid(base.get(f.value()).getValue(),
                                        param.get(FirstMatch.class)))
                                    if (!param.hasAnnotation(Silent.class))
                                        throw new CommandException("CommandException.FirstMatch",
                                                param.get(FirstMatch.class).value());
                                    else value = this.resolve(param, param.hasAnnotation(Default.class) ?
                                            param.get(Default.class).value() : null);
                                else value = this.resolve(param, base.get(f.value()).getValue());
                            // todo: finish
                    }
                }
          } else if (param.hasAnnotation(Permission.class)) {

            }


        }


    }

    private <K> K resolve (Parameter param, String data) throws ParametricException {
        return null;
    }

}