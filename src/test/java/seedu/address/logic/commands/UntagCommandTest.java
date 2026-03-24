package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UntagCommandTest}.
 */
public class UntagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("friend", TagType.GENERAL));

        assertThrows(NullPointerException.class, () -> new UntagCommand(null, tagsToRemove));
    }

    @Test
    public void constructor_nullTagsToRemove_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UntagCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_emptyTagSet_throwsCommandException() {
        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, Set.of());

        assertCommandFailure(untagCommand, model, UntagCommand.MESSAGE_NO_TAGS_FOUND);
    }

    @Test
    public void execute_validIndexRemoveOneTag_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // remove ONE existing tag
        Tag tagToRemove = personToEdit.getTags().iterator().next();

        Set<Tag> tagsToRemove = Set.of(tagToRemove);
        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.remove(tagToRemove);

        Person editedPerson = personToEdit.withTags(expectedTags);

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, tagsToRemove);
        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexRemoveAllTags_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tagsToRemove = new HashSet<>(personToEdit.getTags());

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person editedPerson = personToEdit.withTags(new HashSet<>());

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, tagsToRemove);
        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexRemovePartialTag_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag existingTag = personToEdit.getTags().iterator().next();
        Tag nonExistingTag = new Tag("notfound", TagType.GENERAL);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(existingTag);
        tagsToRemove.add(nonExistingTag);

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.remove(existingTag);

        Person editedPerson = personToEdit.withTags(expectedTags);

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(
                UntagCommand.MESSAGE_PARTIAL_SUCCESS,
                Set.of(existingTag),
                Set.of(nonExistingTag)
        );
        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_tagNotFound_throwsCommandException() {
        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("notfound", TagType.GENERAL));

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        assertCommandFailure(untagCommand, model, UntagCommand.MESSAGE_NO_TAGS_FOUND);
    }

    @Test
    public void execute_allTagsNotFound_throwsCommandException() {
        Set<Tag> tagsToRemove = Set.of(
                new Tag("aaa", TagType.GENERAL),
                new Tag("bbb", TagType.ROLE)
        );

        UntagCommand command = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        assertCommandFailure(command, model, UntagCommand.MESSAGE_NO_TAGS_FOUND);
    }

    @Test
    public void execute_personWithNoTags_throwsCommandException() {
        // INDEX_THIRD_PERSON: a person with no tags
        Set<Tag> tagsToRemove = Set.of(new Tag("friend", TagType.GENERAL));

        UntagCommand untagCommand = new UntagCommand(INDEX_THIRD_PERSON, tagsToRemove);

        assertCommandFailure(untagCommand, model, UntagCommand.MESSAGE_NO_TAGS_FOUND);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("friend", TagType.GENERAL));

        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, tagsToRemove);
        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("friend", TagType.GENERAL));

        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("tutor", TagType.ROLE));

        UntagCommand command = new UntagCommand(INDEX_FIRST_PERSON, tags1);

        // same object -> true
        assertTrue(command.equals(command));

        // same values -> true
        UntagCommand commandCopy = new UntagCommand(INDEX_FIRST_PERSON, tags1);
        assertTrue(command.equals(commandCopy));

        // different index -> false
        UntagCommand differentIndex = new UntagCommand(INDEX_SECOND_PERSON, tags1);
        assertFalse(command.equals(differentIndex));

        // different tags -> false
        UntagCommand differentTags = new UntagCommand(INDEX_FIRST_PERSON, tags2);
        assertFalse(command.equals(differentTags));

        // different type -> false
        assertFalse(command.equals(1));

        // null -> false
        assertFalse(command.equals(null));
    }

    @Test
    public void toStringMethod_containsFields() {
        Set<Tag> tagsToRemove = Set.of(
                new Tag("friend", TagType.GENERAL),
                new Tag("tutor", TagType.ROLE)
        );

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        String expected = UntagCommand.class.getCanonicalName()
                + "{tagsToRemove=" + tagsToRemove + "}";

        assertEquals(expected, untagCommand.toString());
    }
}
