package org.futuredev.workbench.math;

import java.util.Arrays;
import java.util.List;

/**
 * A very basic relevancy booster.
 *
 * For example, "block-craft" and "block-shift"
 * both have a Levenshtein distance of 3 from "block-swfrt",
 * but the user probably wants "block-shift", not "block-craft".
 *
 * @author afistofirony
 */
public class RelevantSearch {

    public RelevantSearch (String input, String... possibilities) {
        this (input, Arrays.asList(possibilities));
    }

    public RelevantSearch (String input, List<String> possibilities) {



    }

    private int relevancy (String original, String a) {
        int dist = 0;

        Levenshtein lev = new Levenshtein(original.toLowerCase(), a.toLowerCase());

        if (lev.getDistance() == 0)
            return 0;

        for (int i = 0; i < original.length() && i < a.length(); ++i) {
            Alphabet at = Alphabet.match(a.charAt(i));
            Alphabet or = Alphabet.match(original.charAt(i));
            if (at != or)
                dist += or.getDistance(at);
        }

        return 0;
    }

    enum Alphabet {
        A ('a', 'A', 1), B ('b', 'B', 2), C ('c', 'C', 3),
        D ('d', 'D', 4), E ('e', 'E', 5), F ('f', 'F', 6),
        G ('g', 'G', 7), H ('h', 'H', 8), I ('i', 'I', 9),
        J ('j', 'J', 10), K ('k', 'K', 11), L ('l', 'L', 12),
        M ('m', 'M', 13), N ('n', 'N', 14), O ('o', 'O', 15),
        P ('p', 'P', 16), Q ('q', 'Q', 17), R ('r', 'R', 18),
        S ('s', 'S', 19), T ('t', 'T', 20), U ('u', 'U', 21),
        V ('v', 'V', 22), W ('w', 'W', 23), X ('x', 'X', 24),
        Y ('y', 'Y', 25), Z ('z', 'Z', 26);

        final char lowercase, uppercase;
        final int index;
        Alphabet (char lc, char uc, int index) {
            this.lowercase = lc;
            this.uppercase = uc;
            this.index     = index;
        }

        public static Alphabet match (char value) {
            for (Alphabet a : Alphabet.values())
                if (a.lowercase == value || a.uppercase == value)
                    return a;

            return null;
        }

        public int getDistance (Alphabet other) {
            return Math.abs(this.index - other.index);
        }

        public int getDistance (char other) {
            return getDistance(match(other));
        }

    }

}