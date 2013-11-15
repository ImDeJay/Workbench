package org.futuredev.workbench.math;

import java.util.Arrays;
import java.util.List;

/**
 * A very basic relevancy booster for the QWERTY keyboard.
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
        A ('a', 'A', 1, 2), B ('b', 'B', 5, 3), C ('c', 'C', 3, 3),
        D ('d', 'D', 3, 2), E ('e', 'E', 3, 1), F ('f', 'F', 4, 2),
        G ('g', 'G', 5, 2), H ('h', 'H', 6, 2), I ('i', 'I', 8, 1),
        J ('j', 'J', 7, 2), K ('k', 'K', 8, 2), L ('l', 'L', 9, 2),
        M ('m', 'M', 7, 3), N ('n', 'N', 6, 3), O ('o', 'O', 9, 1),
        P ('p', 'P', 10, 1), Q ('q', 'Q', 1, 1), R ('r', 'R', 4, 1),
        S ('s', 'S', 2, 2), T ('t', 'T', 5, 1), U ('u', 'U', 7, 1),
        V ('v', 'V', 4, 3), W ('w', 'W', 2, 1), X ('x', 'X', 2, 3),
        Y ('y', 'Y', 6, 1), Z ('z', 'Z', 1, 3);

        final char lowercase, uppercase;
        final int x, y;
        Alphabet (char lc, char uc, int x, int y) {
            this.lowercase = lc;
            this.uppercase = uc;
            this.x = x;
            this.y = y;
        }

        public static Alphabet match (char value) {
            for (Alphabet a : Alphabet.values())
                if (a.lowercase == value || a.uppercase == value)
                    return a;

            return null;
        }

        public double getDistance (Alphabet other) {
            return Math.abs(Math.sqrt(x + y) - Math.sqrt(other.x - other.y));
        }

        public double getDistance (char other) {
            return getDistance(match(other));
        }

    }

}