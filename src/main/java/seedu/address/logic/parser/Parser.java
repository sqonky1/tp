package seedu.address.logic.parser;

import java.util.Optional;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Represents a Parser that is able to parse user input into a {@code Command} of type {@code T}.
 */
public interface Parser<T extends Command> {

    /**
     * Parses {@code userInput} into a command and returns it.
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    T parse(String userInput) throws ParseException;

    /**
     * Returns the first disallowed prefixed token in input order, if any.
     * A prefix is only recognized when preceded by whitespace, matching ArgumentTokenizer behavior.
     */
    static Optional<String> findUnexpectedExtraInput(String args, Prefix[] disallowedPrefixes) {
        int earliestPosition = -1;
        String unexpectedToken = null;

        for (Prefix prefix : disallowedPrefixes) {
            int position = findPrefixPosition(args, prefix);
            if (position != -1 && (earliestPosition == -1 || position < earliestPosition)) {
                earliestPosition = position;
                unexpectedToken = extractToken(args, position);
            }
        }

        return Optional.ofNullable(unexpectedToken);
    }

    private static int findPrefixPosition(String args, Prefix prefix) {
        int prefixIndex = args.indexOf(" " + prefix.getPrefix());
        return prefixIndex == -1 ? -1 : prefixIndex + 1;
    }

    private static String extractToken(String args, int startPosition) {
        int endPosition = args.indexOf(' ', startPosition);
        if (endPosition == -1) {
            return args.substring(startPosition);
        }
        return args.substring(startPosition, endPosition);
    }
}
