package com.futuredev.utilities.command;

import com.futuredev.utilities.command.annotation.*;
import com.futuredev.utilities.command.annotation.Flag;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Does the processing for us.
 *
 * @author afistofirony
 */
public class CommandProcessor {

    public static Object[] parse (Arguments args, Method method) throws CommandException {
        Class[] params = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();

        ArrayList<Object> result = new ArrayList<Object>();

        int argIndex = 0;

        for (int i = 2; i < params.length; ++i) {
            Class type = params[i];
            Annotation[] paramAnnotations = annotations[i];
            if (paramAnnotations.length < 1)
                continue;

            HashMap<Class<? extends Annotation>, Annotation> classes
                    = new HashMap<Class<? extends Annotation>, Annotation>();
            for (Annotation a : paramAnnotations)
                if (!classes.containsKey(a.getClass()))
                    classes.put(a.getClass(), a);

            boolean increment = false;
            String arg = args.get(argIndex);
            com.futuredev.utilities.command.Flag flag = null; // used if we need to talk about a flag

            if (classes.containsKey(Flag.class)) {
                Flag f = (Flag) classes.get(Flag.class);
                if (args.hasFlag(f.value())) {
                    flag = args.get(f.value());
                    if (flag.hasValue()) {
                        arg = flag.getValue();
                  } else {
                        if (classes.containsKey(Optional.class)) {
                            result.add(parseMethod(((Optional) classes.get(Optional.class)).value(),
                                    paramAnnotations));
                      } else {
                            if (!type.isInstance(boolean.class))
                                throw new CommandException("Exception.Flag.ValueMissing", flag.getKey());

                            result.add(true);
                        }


                    }
              } else if (type.isInstance(boolean.class)) {
                    result.add(false);
                }
            }

            if (classes.containsKey(Optional.class) && !classes.containsKey(Flag.class)) {
                increment = true;

                if (int.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
                    try {
                        double value = Double.parseDouble(arg);
                        if (classes.containsKey(Range.class)) {
                            Range range = (Range) classes.get(Range.class);
                            if (range.max() != -1 && value > range.max())
                                throw new CommandException("Exception.Range.ValueTooHigh", range.max(), value);
                            if (range.min() != -1 && value > range.min())
                                throw new CommandException("Exception.Range.ValueTooLow", range.min(), value);
                            result.add(type.isInstance(int.class) ? (int) value : value);
                        }
                  } catch (final NumberFormatException e) {
                        throw new CommandException("Exception.NumberRequired", arg);
                    }
              } else {

                }
            }

            if (increment)
                ++argIndex;
        }

        return result.toArray(new Object[result.size()]);
    }

    public static Arguments buildArgs (Method method, String[] args) throws CommandException {
        Class[] types = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();

        String valued = "", booleans = "";
        String[] illegal = null;

        for (Annotation a : method.getAnnotations())
            if (a instanceof Command)
                illegal = ((Command) a).illegal();

        for (int i = 2; i < types.length; ++i) {
            Annotation[] annots = annotations[i];
            Class type = types[i];
            HashMap<Class<? extends Annotation>, Annotation> map =
                    new HashMap<Class<? extends Annotation>, Annotation>();

            for (Annotation annot2 : annots)
                if (!map.containsKey(annot2.getClass()))
                    map.put(annot2.getClass(), annot2);

            if (map.containsKey(Flag.class)) {
                char key = ((Flag) map.get(Flag.class)).value();
                if (boolean.class.isAssignableFrom(type))
                    booleans += key;

                else {
                    valued += map.containsKey(Optional.class) ? Character.toLowerCase(key)
                            : Character.toUpperCase(key);
                }
            }
        }

        int x = 1;

        Arguments result = new Arguments(args, booleans, valued);

        if (illegal != null)
            result.blockIllegalCombinations(illegal);

        return result;
    }


    private static Object parseMethod (String value, Annotation... annotations) throws CommandException {
        for (Annotation a : annotations) {
            if (a instanceof Range) {
                Range d = (Range) a;
                try {
                    double result = java.lang.Double.parseDouble(value);
                    if (d.max() != -1 && result > d.max())
                        throw new CommandException("Exception.Range.ValueTooHigh", d.max(), result);
                    if (d.min() != -1 && result > d.min())
                        throw new CommandException("Exception.Range.ValueTooLow", d.min(), result);
                    return result;
                } catch (final NumberFormatException e) {
                    throw new CommandException("Exception.NumberRequired", value); // TODO: Give more context.
                }
            }

            if (a instanceof Expressed) {
                Expressed ex = (Expressed) a;
                if (!value.matches(ex.value()))
                    throw new CommandException("Exception.Expression.InvalidInput", ex.value(), value);

                return value;
            }


        }

        return value;
    }

}