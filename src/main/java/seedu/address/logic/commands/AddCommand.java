package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a person to the address book. \n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_EMAIL + "EMAIL "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_TELEGRAM_HANDLE + "TELEGRAM_HANDLE].\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_TELEGRAM_HANDLE + "johndoe123";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "A person with this email or Telegram handle already exists in the address book";
    public static final String MESSAGE_NON_NUS_EMAIL = "Warning: Email is not an NUS domain.";
    public static final String NUS_STUDENT_EMAIL_DOMAIN = "@u.nus.edu";
    public static final String NUS_STAFF_EMAIL_DOMAIN = "@nus.edu.sg";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);

        String resultMessage = String.format(MESSAGE_SUCCESS, Messages.format(toAdd));
        if (!toAdd.getEmail().toString().endsWith(NUS_STUDENT_EMAIL_DOMAIN)
                && !toAdd.getEmail().toString().endsWith(NUS_STAFF_EMAIL_DOMAIN)) {
            resultMessage += "\n" + MESSAGE_NON_NUS_EMAIL;
        }
        return new CommandResult(resultMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
