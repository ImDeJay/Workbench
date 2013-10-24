package com.futuredev.utilities.math;

/**
 * Calculates Levenshtein distance, for action,
 * username, and other String approximation.
 *
 * Instead of Prism's implementation (suggesting close matches),
 * we will use Levenshtein distance to actually *correct* errors.
 * It will be limited to a certain (configurable) threshold, so typing
 * "p:wDSLFKja" cannot somehow result in "p:afistofirony".
 * Of course, it can be disabled entirely if a server's
 * administrators don't like it.
 *
 * @author afistofirony
 */
public class Levenshtein {

    int distance;

    /**
     * Calculates the Levenshtein distance between two Strings.
     *
     * The way this works is it scans a 2D array to determine a
     * proper Levenshtein distance. We could technically use
     * a far simpler implementation of
     *
     *     if (a.charAt(i) != b.charAt(i)) ++distance;
     *
     * but that doesn't cover mid-string insertions.
     * This does, at least to some degree.
     *
     *
     * @param a The first String.
     * @param b The String to compare it to.
     */
    public Levenshtein (String a, String b) {

        this.distance = 0;

        if (a == null || a.isEmpty()) {
            distance = b.length();
            return;
      } else if (b == null || b.isEmpty()) {
            distance = a.length();
            return;
        }

        int[][] dist = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++)
            dist[i][0] = i;
        for (int j = 1; j <= b.length(); j++)
            dist[0][j] = j;

        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                dist[i][j] = Math.min(Math.min(
                        dist[i - 1][j] + 1,
                        dist[i][j - 1] + 1),
                        dist[i - 1][j - 1]
                                + ((a.charAt(i - 1) == b.charAt(j - 1)) ? 0
                                : 1));

        distance = dist[a.length()][b.length()];

    }

    /**
     * Gets the distance calculated in the constructor.
     * @return The Levenshtein distance.
     */
    public int getDistance () {
        return distance;
    }

}