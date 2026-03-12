package seedu.address.logic.commands;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.Comparator;

import static java.util.Objects.requireNonNull;

/**
 * Sorts the person list by a specified order.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final Prefix PREFIX_ORDER = new Prefix("o/");
    public static final Prefix PREFIX_REVERSE = new Prefix("r/");

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the persons by their names\n"
            + "Parameters: "
            + PREFIX_ORDER + "ORDER "
            + "[" + PREFIX_REVERSE + "]";

    public static final String MESSAGE_SUCCESS = "Sorted by name.";

    private final String order;
    private final boolean reverse;
    private final Comparator<Person> comparator;

    /**
     * Creates a SortCommand that sorts persons by the given order, optionally reversed.
     */
    public SortCommand(String order, boolean reverse) {
        this.order = order;
        this.reverse = reverse;
        Comparator<Person> cmp = Comparator.comparing(
                p -> p.getName().fullName, String.CASE_INSENSITIVE_ORDER);
        this.comparator = reverse ? cmp.reversed() : cmp;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateSortedPersonList(comparator);
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SortCommand otherCommand)) {
            return false;
        }
        return order.equals(otherCommand.order) && reverse == otherCommand.reverse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("order", order)
                .add("reverse", reverse)
                .toString();
    }
}
