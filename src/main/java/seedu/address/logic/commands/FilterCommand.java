package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;

/**
 * Filter the list with certain tag(s)
 */
public class FilterCommand extends Command{

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        
        return null;
    }

}
