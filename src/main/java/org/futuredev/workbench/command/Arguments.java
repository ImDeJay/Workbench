package org.futuredev.workbench.command;

import org.futuredev.workbench.localisation.StringHelper;

import java.util.*;

public class Arguments implements Iterable<String> {

    protected String[] args;
    protected char[] bools;
    protected int length;
    protected Set<Character> strings, optionals;
    protected final List<String> parsed = new ArrayList<String>();
    protected final List<Flag> flags =  new ArrayList<Flag>();
    protected final List<Flag> valued = new ArrayList<Flag>();

    protected int subIndex;

    protected enum Type { BOOLEAN, STRING, OPTIONAL, UNREGISTERED }

    public Arguments (String[] args, String booleans, String values) throws CommandException {

        this.args     = args;
        this.bools    = ("?" + booleans).toCharArray();
        this.subIndex = 0;

        this.strings = new HashSet<Character>();
        this.optionals = new HashSet<Character>();

        for (int i = 0; i < values.length(); ++i) {
            char value = values.charAt(i);
            if (Character.isUpperCase(value))
                strings.add(Character.toLowerCase(value));
            else optionals.add(value);
        }

        List<String> preprocessed  = new ArrayList<String>();
        List<String> stageTwo      = new ArrayList<String>();

        if (args.length == 0) {
            this.length = 0;
            return;
        }

        for (int i = 0; i < args.length; ++i) {

            String arg = args[i];

            if (arg.length() == 0)
                continue;

            if (arg.length() > 1)

                switch (arg.charAt(0)) {
                    case '\'':
                    case '"':
                        String result = "";
                        char closer = arg.charAt(0);
                        int j, lastWord = i;
                        for (j = i; j < args.length; ++j) {
                            String part = args[j];
                            if (part.length() == 0) {
                                args[lastWord] += " ";
                                continue;
                            }

                            if (part.charAt(part.length() - 1) == closer && part.length() > 1)
                                break;

                            lastWord = j;
                        }

                        if (j < args.length) {
                            for (int k = i; k < j; ++k) {
                                result += args[k];
                            }

                            arg = result.substring(1, result.length() - 1);
                        }
                }

            preprocessed.add(arg);
        }

        /* Combine subsequent flags so we definitely process them.
         * The reason we do this is because we skip over other flags whilst processing
         * and then use i = j so the values of those flags are not processed as regular arguments.
         * Therefore, it is important to get the flags in between flags and their values so they are
         * not ignored.
         *
         * Before: -p -s afistofirony 10
         * After: -ps afistofirony 10
         */
        loop: for (int i = 0; i < preprocessed.size(); ++i) {
            String arg = preprocessed.get(i);
            int j;

            if (arg.charAt(0) != '-' || arg.length() < 2 || !arg.toLowerCase().matches("^-[-a-z?^]+$")) {
                stageTwo.add(arg);
                continue;
            }

            // Handle the flag escape (i.e. --?)
            if (arg.length() > 2 && arg.charAt(1) == '-') {
                stageTwo.add(arg);
                continue;
            }

            String concatenated = arg.toLowerCase();
            for (j = i + 1; j < preprocessed.size(); ++j) {
                String arg2 = preprocessed.get(j);
                if (arg2.charAt(0) == '-' && arg2.length() > 1 && arg2.matches("^-[a-zA-Z?^]+$")) {
                    concatenated += arg2.substring(1);
                } else {
                    stageTwo.add(concatenated);
                    stageTwo.add(arg2);
                    i = j;
                    continue loop;
                }
            }

            // In case the else statement above is never reached
            stageTwo.add(concatenated);
            break;
        }

        for (int i = 0; i < stageTwo.size(); ++i) {
            String arg = stageTwo.get(i);
            int j = 0;
            int extra = 0;

            if (arg.charAt(0) != '-' || arg.length() < 2 || !arg.matches("^-[-a-zA-Z?^]+$")) {
                this.parsed.add(arg);
                continue;
            }

            // It's definitely a flag. If a user wants to ignore a flag, they'll just use
            //   a double-hyphen flag (for example, --? will be parsed as -?)
            if (arg.length() > 2 && arg.charAt(1) == '-') {
                this.parsed.add("-" + arg.substring(2));
                continue;
            }

            char[] flags = arg.toLowerCase().toCharArray();
            for (int k = 1; k < flags.length; ++k) {
                Type type = getType(flags[k]);
                switch (type) {
                    case BOOLEAN:
                        this.flags.add(new Flag(flags[k]));
                        break;

                    case STRING:
                    case OPTIONAL:
                        if (!hasFlag(flags[k])) {
                            try {
                                int l;
                                for (l = i; l < stageTwo.size(); ++l) {
                                    String s = stageTwo.get(l);
                                    if (s.charAt(0) != '-' || s.length() < 2 || !s.matches("^-[a-zA-Z?^]+$")) {
                                        break;
                                    } else {
                                        char[] flags2 = s.toLowerCase().toCharArray();
                                        for (int m = 1; m < flags2.length; ++m) {
                                            switch (getType(flags2[m])) {
                                                case BOOLEAN:
                                                case OPTIONAL:
                                                    this.flags.add(new Flag(flags2[m]));
                                                    break;

                                                case STRING:
                                                    throw new
                                                            CommandException("Exceptions.Flags.NoRandomValuedFlags");
                                            }
                                        }

                                        ++extra;
                                    }
                                }

                                if ((l + j) > stageTwo.size()) {
                                    if (type != Type.OPTIONAL)
                                        throw new CommandException("Exceptions.Flags.NoValue");

                                    // Value was optional, so no exceptions will be thrown.
                                    this.flags.add(new Flag(flags[k]));
                                    continue;
                                }

                                String value = stageTwo.get(l + j);
                                if (value.length() > 2 && value.charAt(0) == '-' && value.charAt(1) == '-')
                                    value = "-" + value.substring(2);
                                this.valued.add(new Flag(flags[k], value));
                                ++j;
                            } catch (Exception e) {
                                throw new CommandException("Exceptions.Flags.NoValue", flags[k]);
                            }
                        } else {
                            throw new CommandException("Exceptions.Flags.DuplicateValue", flags[k]);
                        }
                }
            }

            i += j + extra;

        }

        this.length = parsed.size();

    }

    public final int length () {
        return (length - subIndex < 0 ? 0 : length - subIndex);
    }

    public String[] original () {
        return args;
    }

    public List<Flag> valuedFlags () {
        return valued;
    }

    public String[] slice (int start, int stop) {
        int size = stop - start;
        String[] array = new String[size];
        for (int i = 0; i < (parsed.size() > stop ? parsed.size() : size); ++i) {
            array[i] = parsed.get(start + i);
        }

        return array;
    }

    public String get (int index) {
        return parsed.get(subIndex + index);
    }

    public String get (int index, String def) {
        if (parsed.size() > subIndex + index)
            return def;
        return get(index);
    }

    public Flag get (char flag) {
        for (Flag f : this.valued) {
            if (f.getKey() == flag)
                return f;
        }

        return null;
    }

    public Flag get (char flag, String def) {
        if (this.hasFlag(flag)) {
            return get(flag);
        }

        return new Flag(flag, def);
    }

    public String getRaw (int index) {
        return parsed.get(index);
    }

    public boolean hasFlag (char... flags) {
        for (char flag : flags) {
            for (Flag f : this.valued) {
                if (f.getKey() == flag)
                    return true;
            }
        }

        return false;
    }

    public boolean hasFlags () {
        return !flags.isEmpty() || !valued.isEmpty();
    }

    public Arguments setSubIndex (int subIndex) {
        this.subIndex = subIndex;
        return this;
    }



    public Iterator<String> iterator () {
        return parsed.iterator();
    }

    /**
     * Handles illegal flag combinations.
     * There are two types of illegal combinations - AND combinations
     * and OR combinations. An AND combinations requires all specified
     * flags to be given for an exception to be thrown. An OR combination
     * only needs two of the given flags to throw an exception. For example:
     *
     * &abc - The arguments explicitly needs -abc to throw an error.
     * |abc - Either -ab, -ac, or -bc will throw an error.
     *
     * If an exception is thrown, the illegal combinations are listed:
     * "The 'a' and 'b' flags cannot be used together."
     * "The 'a', 'b', and 'c' flags cannot be used together."
     *
     * @param illegal An array of illegal combinations.
     * @throws CommandException Thrown if an illegal flag combination is encountered.
     */
    public void blockIllegalCombinations (String... illegal) throws CommandException {
        for (String s : illegal) {
            switch (s.charAt(0)) {
                case '&':
                    String result = "";
                    for (int i = 1; i < s.length(); ++i)
                        result += this.hasFlag(s.charAt(i)) ? "1" : "0";

                    if (StringHelper.occurrencesOf(result, '0') < 1) {
                        ArrayList<String> flags = new ArrayList<String>();
                        for (int i = 0; i < s.length(); ++i)
                            flags.add(Character.toString(s.charAt(i)));
                        throw new CommandException("Parameters.IllegalCombination",
                                StringHelper.elegantList(flags));
                    }

                    break;

                case '|':
                    result = "";
                    for (int i = 1; i < s.length(); ++i)
                        result += this.hasFlag(s.charAt(i)) ? "1" : "0";

                    if (StringHelper.occurrencesOf(result, '1') > 1) {
                        ArrayList<String> flags = new ArrayList<String>();
                        for (int i = 0; i < s.length(); ++i)
                            if (this.hasFlag(s.charAt(i)))
                                flags.add("'" + Character.toString(s.charAt(i)) + "'");
                        throw new CommandException("Parameters.IllegalCombination",
                                StringHelper.elegantList(flags));
                    }
            }
        }
    }

    /**
     * Internal method: Gets the type of the flag registered.
     * @param flag The flag we're checking.
     * @return The type of 'flag'. Returns UNREGISTERED if we don't know what kind it is.
     */
    private Type getType (char flag) {
        for (char c : bools) {
            if (c == flag) return Type.BOOLEAN;
        } for (char c : strings) {
            if (c == flag) return Type.STRING;
        } return Type.UNREGISTERED;
    }


}