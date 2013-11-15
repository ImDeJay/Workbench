package org.futuredev.workbench.localisation;

import org.bukkit.ChatColor;

/**
 * Represents a colour scheme.
 * @author afistofirony
 */
public class Scheme {

    ChatColor primary, secondary, tertiary, completed, error, mod;

    /**
     * Creates a colour scheme with the specified colours.
     * @param p Primary colour character.
     * @param s Secondary colour character.
     * @param t Tertiary colour character.
     * @param c Action-complete colour character.
     * @param e The error colour character.
     * @param m The moderator colour character.
     */
    public Scheme (char p, char s, char t, char c, char e, char m /*ing*/) {

        primary   = ChatColor.getByChar(p);
        secondary = ChatColor.getByChar(s);
        tertiary  = ChatColor.getByChar(t);
        completed = ChatColor.getByChar(c);
        error     = ChatColor.getByChar(e);
        mod       = ChatColor.getByChar(m);

    }

    /**
     * Turns a string into a colour scheme.
     * @param scheme The string to turn into a scheme.
     * @return A new scheme made out of the characters.
     */
    public static Scheme fromString (String scheme) {
        while (scheme.length() < 6)
            scheme += "f";

        char[] codes = scheme.toLowerCase().toCharArray();
        return new Scheme(codes[0], codes[1], codes[2], codes[3], codes[4], codes[5]);
    }

    public ChatColor getPrimary ()   { return primary;   }
    public ChatColor getSecondary () { return secondary; }
    public ChatColor getTertiary ()  { return tertiary;  }
    public ChatColor getCompleted () { return completed; }
    public ChatColor getError ()     { return error;     }
    public ChatColor getMod ()       { return mod;       }

    /**
     * Returns the scheme as a string.
     * @return A string containing the characters of the scheme.
     */
    public String toString () {
        return "" + primary.getChar() + secondary.getChar() + tertiary.getChar() + completed.getChar() + error.getChar() + mod.getChar();
    }


}