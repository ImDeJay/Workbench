package org.futuredev.workbench.helper;

import org.futuredev.workbench.command.CommandException;
import org.futuredev.workbench.localisation.MessageAssist;

import java.util.HashMap;
import java.util.List;

/**
 * Helper class.
 *
 * @author afistofirony
 */
public final class Helper {

    /**
     * Should not be instantiated.
     */
    private Helper () {}

    /**
     * Check if a string matches one of multiple.
     * @param string The string that should be checked against.
     * @param matches The strings that should be checked.
     * @return Whether or not one of the strings matches.
     */
    public static boolean matchesStrings (String string, String... matches) {
        for (String s : matches) {
            if (string.equalsIgnoreCase(s)) return true;
        }

        return false;
    }

    /**
     * Glues an array together.
     * @param parts The array to glue.
     * @return Our glued array.
     */
    public static String glue (String[] parts) {
        String result = parts[0];
        for (int i = 1; i < parts.length; ++i) {
            result += " " + parts[i];
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
        for (int i = ++start; i < (stop > parts.length ? parts.length : stop); ++i) {
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
     * @return Whether it is numeric (INCLUDES DECIMALS!).
     */
    public static boolean isNumber (String input) {
        return input.matches("(-)?([0-9]+)?(\\.)?[0-9]+?");
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

    /**
     * Shorthand for the tedious try { Integer.parseInt(int); } catch (NFE) { handle; }
     * @param value The string value.
     * @return The string as an int.
     * @throws CommandException Thrown if value does not match (-)?[0-9]+
     */
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
     * @return The index of the first matching argument, or -1 if no match
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

    public static <K, V> K hashRoot (HashMap<K, V> from, V value) {
        for (K key : from.keySet()) {
            if (from.get(key).equals(value))
                return key;
        }

        return null;
    }

    /**
     * Using a regular expression, this method compiles the first
     * valid match it can find. For example, if you have an expression
     *
     *     (example|another|yet another),
     *
     * the result will be "example".
     *
     * More examples:
     *
     * [bcr]at -> 'bat'
     * [bcr]?at -> 'at'
     * . -> '0'
     * [abc]? -> 'a'
     * (a|b|c) -> 'a'
     * \\d -> '0'
     * \\w -> 'a'
     * [a-z^[a]] -> 'b'
     *
     * @param exp The expression.
     * @return The first valid match.
     *
     * TODO: Implement
     */
    @Deprecated
    public static String firstMatch (String exp) {
        String result = "";


        return null;
    }

    /**
     * Efficiently generates Roman numerals.
     * @param value The numeric value.
     * @return A roman numeral (no support for over-lined characters).
     */
    public static String romanNumerals (int value) {
        // Example unit: 1792 (MDCCXCII)
        String result = "";

        while (value >= 1000) { // Append M, value = 792
            result += 'M';
            value -= 1000;
        }

        while (value >= 900) {
            result += "CM";
            value -= 900;
        }

        while (value >= 500) { // Append D, value = 292
            result += 'D';
            value -= 500;
        }

        while (value >= 400) {
            result += "CD";
            value -= 400;
        }

        while (value >= 100) { // Append CC, value = 92
            result += 'C';
            value -= 100;
        }

        while (value >= 90) { // Append XC, value = 2
            result += "XC";
            value -= 90;
        }

        while (value >= 50) {
            result += 'L';
            value -= 50;
        }

        while (value >= 40) {
            result += "XL";
            value -= 40;
        }

        while (value >= 10) {
            result += 'X';
            value -= 10;
        }

        while (value >= 9) {
            result += "IX";
            value -= 9;
        }

        while (value >= 5) {
            result += 'V';
            value -= 5;
        }

        while (value >= 4) {
            result += "IV";
            value -= 4;
        }

        while (value >= 1) { // Append II, value = 0
            result += 'I';
            --value;
        }

        return result;
    }

}