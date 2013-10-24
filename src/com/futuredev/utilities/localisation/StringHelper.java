package com.futuredev.utilities.localisation;

import com.futuredev.utilities.command.CommandException;

import java.util.List;

public class StringHelper {

    /**
     * Check if a string matches one of multiple.
     * @param string The string that should be checked against.
     * @param matches The strings that should be checked.
     * @return
     */
    public static boolean matchesStrings (String string, String... matches) {
        for (String s : matches) {
            if (string.equalsIgnoreCase(s)) return true;
        } return false;
    }

    /**
     * Glues an array together.
     * @param parts The array to glue.
     * @return Our glued array.
     */
    public static String glue (String[] parts) {
        String result = parts[0];
        for (int i = 1; i < parts.length; ++i) {
            result+= " " + parts[i];
        }

        return result;
    }

    /**
     * Slices a string.
     * @param slice The string to slice.
     * @param start The index at which to start.
     * @param stop The index at which to stop.
     * @return The part of the string we want.
     */
    public static String slice (String slice, int start, int stop) {
        String[] parts = slice.split(" ");
        String result = parts[start];
        for (int i = start++; i < (stop > parts.length ? parts.length : stop); ++i) {
            result += " " + parts[i];
        }

        return result;
    }

    public static String[] arraySlice (String[] slice, int start, int stop) {
        if (stop == -1)
            stop = slice.length;

        String[] result = new String[stop - start];
        for (int i = start; i < stop; ++i) {
            result[start - i] = slice[i];
        }

        return result;
    }

    /**
     * Creates an elegant list.
     * @param list The list to make elegant.
     * @return The elegant list.
     */
    public static String elegantList (List<?> list) {
        String result = list.get(0).toString();
        switch (list.size()) {
            case 1:
                return result;

            case 2:
                return result + " and " + list.get(1).toString();

            default:
                for (int i = 1; i < list.size() - 1; ++i) {
                    result += ", " + list.get(i).toString();
                }

                return result + ", and " + list.get(list.size() - 1).toString();
        }
    }

    /**
     * Returns the text form of a number. These can be entered manually.
     * @param i The integer to check for.
     * @return The nice number, or the numerical form if it's not found.
     */
    public static String getElegantNumber (MessageAssist lang, int i) {
        if (lang.messageExists("Numbers." + i)) {
            return lang.read("Numbers." + i);
        }

        return str(i);
    }

    /**
     * Capitalises the first letter of a string.
     * @param s The string to capitalise the first letter of.
     * @return The string, with a capitalised letter.
     */
    public static String capitaliseFirstLetter (String s) {
        return Character.toString(s.charAt(0)).toUpperCase() + s.substring(1);
    }

    /**
     * Turns an object into a string.
     * @param obj The object to turn into a string.
     * @return The object as a string.
     */
    public static String str (Object obj) {
        return obj.toString();
    }

    /**
     * Adds thousands separators to a number.
     * @param num The number to add separators to.
     * @param thouChar The thousands character.
     * @return A number with thousands separators.
     */
    public static String thousands (int num, char thouChar) {
        String result = "";
        char[] numbers = str(num).toCharArray(); // sternum
        int nextInsertion = 0;
        for (int i = numbers.length - 1; i >= 0; --i) {
            boolean insert = nextInsertion == 3;
            if (insert) {
                result = str(thouChar) + numbers[i] + result;
                nextInsertion = 1;
            } else {
                result = numbers[i] + result;
                ++nextInsertion;
            }
        }

        return result;
    }

    /**
     * Trims a string array.
     * @param original The original array.
     * @param start The index at which to start.
     * @param stop The index at which to finish.
     * @return The trimmed array.
     */
    public static String[] trim (String[] original, int start, int stop) {
        int size = stop - start;
        String[] result = new String[size];
        for (int i = 0; i < (size > original.length ? original.length : size); ++i) {
            result[i] = original[start + i];
        }

        return result;
    }

    /**
     * Checks if a String contains any of the specified characters.
     * @param compare The String to check.
     * @param to The characters to look for.
     * @return Whether or not the string contains those characters.
     */
    public static boolean containsAny (String compare, char... to) {
        for (char t : to) {
            if (compare.contains(Character.toString(t)))
                return true;
        }

        return false;
    }

    public static boolean endsWithAny (String compare, String... to) {
        for (String s : to) {
            if (compare.endsWith(s)) return true;
        }

        return false;
    }

    /**
     * Returns whether or not a character is numeric.
     * @param input The character to check.
     * @return Whether that character is numeric.
     */
    public static boolean isNumber (char input) {
        return isNumber(Character.toString(input));
    }

    /**
     * Returns whether or not a String is numeric.
     * @param input The string to check.
     * @return Whether it is numeric.
     */
    public static boolean isNumber (String input) {
        return input.matches("(-)?[0-9.]+?");
    }

    /**
     * Gets the amount of occurrences of a specific character in a
     * String.
     * @param check The string to check.
     * @param checkFor The character to look for.
     * @return The amount of occurrences of that character.
     */
    public static int occurrencesOf (String check, char checkFor) {
        int result = 0;
        for (int i = 0; i < check.length(); ++i) {
            if (check.charAt(i) == checkFor)
                ++result;
        }

        return result;
    }

    public static int getNumber (String value) throws CommandException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CommandException("Exception.Parameters.InvalidNumber", value);
        }
    }

    /**
     * Loophole of String-switch in Java 6.
     *
     * Example:
     *   switch (StringHelper.forSwitch(example, "test", "test2", "test3")) {
     *       case -1: // Invalid argument
     *       case 0: // test
     *       case 1: // test2
     *       case 2: // test3
     *   }
     *
     * @param check The value checked.
     * @param possibilities Possible results.
     * @return The index of the
     */
    public static int forSwitch (String check, String... possibilities) {
        for (int i = 0; i < possibilities.length; ++i) {
            if (possibilities[i].equalsIgnoreCase(check) || check.matches(possibilities[i]))
                return i;
        }

        return -1;
    }

    public static int forSwitchContains (String check, String... possibilities) {
        for (int i = 0; i < possibilities.length; ++i) {
            if (check.contains(possibilities[i]))
                return i;
        }

        return -1;
    }

    public static int firstMatch (String check, String... expressions) {
        for (int i = 0; i < expressions.length; ++i) {
            if (check.matches("(i?)" + expressions[i]))
                return i;
        }

        return -1;
    }


}