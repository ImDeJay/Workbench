package org.futuredev.workbench.command;

public class Flag {

    char key;
    String value;
    boolean hasValue;

    public Flag (char key) {
        this (key, null);
    }

    public Flag (char key, String value) {
        this.key = key;
        this.value = value == null ? "" : value;
        this.hasValue = value != null;
    }

    public Flag append (String value) {
        this.value += value;
        return this;
    }

    public Flag validate (String expression) throws CommandException {
        if (this.hasValue && !value.matches(expression))
            throw new CommandException("Exception.Lexer.InvalidFlagFormat", this.getKey(), expression);
        return this;
    }

    public char getKey () { return key; }
    public String getValue () { return value; }

    public String getValue (String def) {
        return value.isEmpty() ? def : value;
    }

    public boolean hasValue () {
        return hasValue;
    }

    public boolean isMultiword () {
        return hasValue && value.contains(" ");
    }

    public int intValue () {
        return this.intValue(0);
    }

    public int intValue (int def) {
        if (!this.hasValue())
            return def;

        try {
            return Integer.parseInt(this.value);
      } catch (final NumberFormatException e) {
            return def;
        }
    }

}