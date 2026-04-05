package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Normalizes the input string by converting it to lowercase, removing trailing whitespaces
     * and special characters such as punctuation.
     *
     * <p>This method standardizes strings for comparison and storage by:
     * <ul>
     *   <li>Converting all characters to lowercase for case-insensitive operations</li>
     *   <li>Removing any trailing whitespaces</li>
     *   <li>Removing all special characters (punctuation, symbols, etc.)</li>
     *   <li>Preserving alphanumeric characters (a-z, 0-9) and whitespace</li>
     * </ul>
     * </p>
     *
     * <p>This is useful for fuzzy matching, search preprocessing, or comparing names
     * that may contain special characters or inconsistent casing.</p>
     *
     * @param s the string to normalize (cannot be null)
     * @return the normalized string with lowercase letters, digits, and spaces only
     * @throws NullPointerException if {@code s} is {@code null}
     */
    public static String normalize(String s) {
        requireNonNull(s);

        return s.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s]", "");
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //====================== Fuzzy String Matching ============================/

    /**
     * Computes the Damerau–Levenshtein distance (Optimal String Alignment variant)
     * between two strings.
     *
     * <p>This distance measures the minimum number of edits
     * required to transform one string into the other. Supported operations are:
     * <ul>
     *   <li>Insertion</li>
     *   <li>Deletion</li>
     *   <li>Substitution</li>
     *   <li>Transposition of adjacent characters (e.g. "alxe" → "alex")</li>
     * </ul>
     *
     * <p>This implementation uses a dynamic programming approach with O(n × m) time and space complexity,
     * where n and m are the lengths of the input strings.</p>
     *
     * <p>Note: This implementation uses the Optimal String Alignment (OSA) variant,
     * which allows only non-overlapping transpositions.</p>
     *
     * Preconditions:
     * <ul>
     *   <li>{@code query} and {@code candidate} must be normalized
     *       (e.g. trimmed, lowercased and with non-alphanumeric characters handled consistently)</li>
     * </ul>
     *
     * @param query the input string (e.g. user search query)
     * @param candidate the string to compare against (e.g. contact name)
     * @return the edit distance between {@code query} and {@code candidate}
     * @throws NullPointerException if either input string is {@code null}
     */
    static int damerauLevenshteinDistance(String query, String candidate) {
        requireNonNull(query);
        requireNonNull(candidate);

        int n = query.length();
        int m = candidate.length();

        // dynamic programming matrix
        int[][] dp = new int[n + 1][m + 1];

        // initialize the first row of the matrix
        for (int col = 0; col <= m; col++) {
            dp[0][col] = col;
        }

        // initialize the first col of the matrix
        for (int row = 0; row <= n; row++) {
            dp[row][0] = row;
        }

        // Fill up the matrix using dynamic programming
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char chQ = query.charAt(i - 1);
                char chC = candidate.charAt(j - 1);

                int cost = (chQ == chC) ? 0 : 1;
                dp[i][j] = Math.min(
                        dp[i - 1][j] + 1, // deletion
                        Math.min(dp[i][j - 1] + 1, // insertion
                                dp[i - 1][j - 1] + cost) // substitution/match
                );


                // Handles transpositions (swapping adjacent characters)
                if (i > 1 && j > 1) {
                    char prevQ = query.charAt(i - 2);
                    char prevC = candidate.charAt(j - 2);

                    if (chQ == prevC && prevQ == chC) {
                        dp[i][j] = Math.min(
                                dp[i][j],
                                dp[i - 2][j - 2] + 1 // transposition
                        );
                    }
                }
            }
        }

        return dp[n][m];
    }

    /**
     * Returns true if the {@code query} matches the {@code candidate} using fuzzy matching.
     *
     * <p>Fuzzy matching uses Damerau–Levenshtein distance to allow approximate matches.
     * The method returns true if the Damerau–Levenshtein distance between the query and candidate
     * is less than or equal to the specified threshold.</p>
     *
     * <p>Preconditions:
     *   <ul>
     *       <li>{@code query} and {@code candidate} must be non-null</li>
     *       <li>{@code query} and {@code candidate} must already be normalized
     *       (trimmed, lowercased, and with non-alphanumeric characters handled consistently)</li>
     *       <li>{@code threshold} must be non-negative</li>
     *   </ul>
     *
     * @param query the search query string (will be trimmed)
     * @param candidate the word to match against (will be trimmed)
     * @param threshold the maximum Levenshtein distance allowed for a match
     * @return true if the words match within the threshold, false otherwise
     * @throws NullPointerException if {@code query} or {@code word} is {@code null}
     * @throws IllegalArgumentException if {@code threshold} is negative
     */
    public static boolean matchesFuzzy(String query, String candidate, int threshold) {
        requireNonNull(query);
        requireNonNull(candidate);
        checkArgument(threshold >= 0, "Threshold cannot be negative");

        // Handle empty strings after trimming
        if (query.isEmpty() || candidate.isEmpty()) {
            // Empty query should not match non-empty word and vice versa
            // except when both are empty (distance = 0)
            return query.isEmpty() && candidate.isEmpty();
        }

        // Calculate Damerau Levenshtein Distance and check if within threshold
        int distance = damerauLevenshteinDistance(query, candidate);
        return distance <= threshold;
    }

}
