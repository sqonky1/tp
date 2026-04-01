package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_TAG_TEAMMATE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateEmailPersons_throwsDuplicatePersonException() {
        // Two persons with the same email
        Person editedAlice = new PersonBuilder(ALICE).withRoleTags(VALID_ROLE_TAG_TEAMMATE)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateTelegramHandlePersons_throwsDuplicatePersonException() {
        Person aliceWithTelegram = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        Person bobWithTelegram = new PersonBuilder()
                .withName("Bob Choo")
                .withEmail("bob@example.com")
                .withTelegramHandle("alice123")
                .build();
        List<Person> newPersons = Arrays.asList(aliceWithTelegram, bobWithTelegram);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameEmailInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withRoleTags(VALID_ROLE_TAG_TEAMMATE)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void hasPerson_personWithSameTelegramHandleInAddressBook_returnsTrue() {
        Person aliceWithTelegram = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        Person editedAlice = new PersonBuilder(aliceWithTelegram)
                .withEmail("different@example.com")
                .build();

        addressBook.addPerson(aliceWithTelegram);
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void hasEmailConflict_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasEmailConflict(null));
    }

    @Test
    public void hasEmailConflictExcluding_nullTarget_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasEmailConflictExcluding(null, ALICE));
    }

    @Test
    public void hasEmailConflictExcluding_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasEmailConflictExcluding(ALICE, null));
    }

    @Test
    public void hasEmailConflict_personWithSameEmailInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withRoleTags(VALID_ROLE_TAG_TEAMMATE).build();
        assertTrue(addressBook.hasEmailConflict(editedAlice));
    }

    @Test
    public void hasEmailConflict_personWithDifferentEmailInAddressBook_returnsFalse() {
        addressBook.addPerson(ALICE);
        assertFalse(addressBook.hasEmailConflict(BOB));
    }

    @Test
    public void hasEmailConflictExcluding_sameTarget_returnsFalse() {
        addressBook.addPerson(ALICE);
        assertFalse(addressBook.hasEmailConflictExcluding(ALICE, ALICE));
    }

    @Test
    public void hasEmailConflictExcluding_otherPersonWithSameEmail_returnsTrue() {
        addressBook.addPerson(ALICE);
        addressBook.addPerson(BOB);

        Person editedBob = new PersonBuilder(BOB).withEmail(ALICE.getEmail().value).build();
        assertTrue(addressBook.hasEmailConflictExcluding(BOB, editedBob));
    }

    @Test
    public void hasTelegramHandleConflict_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasTelegramHandleConflict(null));
    }

    @Test
    public void hasTelegramHandleConflictExcluding_nullTarget_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> addressBook.hasTelegramHandleConflictExcluding(null, ALICE));
    }

    @Test
    public void hasTelegramHandleConflictExcluding_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> addressBook.hasTelegramHandleConflictExcluding(ALICE, null));
    }

    @Test
    public void hasTelegramHandleConflict_personWithSameTelegramHandleInAddressBook_returnsTrue() {
        Person aliceWithTelegram = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        Person editedAlice = new PersonBuilder(aliceWithTelegram).withEmail("different@example.com").build();

        addressBook.addPerson(aliceWithTelegram);
        assertTrue(addressBook.hasTelegramHandleConflict(editedAlice));
    }

    @Test
    public void hasTelegramHandleConflict_personWithDifferentTelegramHandleInAddressBook_returnsFalse() {
        Person aliceWithTelegram = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        Person bobWithTelegram = new PersonBuilder(BOB).withTelegramHandle("bob123").build();

        addressBook.addPerson(aliceWithTelegram);
        assertFalse(addressBook.hasTelegramHandleConflict(bobWithTelegram));
    }

    @Test
    public void hasTelegramHandleConflictExcluding_sameTarget_returnsFalse() {
        Person aliceWithTelegram = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        addressBook.addPerson(aliceWithTelegram);

        assertFalse(addressBook.hasTelegramHandleConflictExcluding(aliceWithTelegram, aliceWithTelegram));
    }

    @Test
    public void hasTelegramHandleConflictExcluding_otherPersonWithSameTelegramHandle_returnsTrue() {
        Person aliceWithTelegram = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        Person bobWithTelegram = new PersonBuilder(BOB).withTelegramHandle("bob123").build();
        Person editedBob = new PersonBuilder(bobWithTelegram).withTelegramHandle("alice123").build();

        addressBook.addPerson(aliceWithTelegram);
        addressBook.addPerson(bobWithTelegram);

        assertTrue(addressBook.hasTelegramHandleConflictExcluding(bobWithTelegram, editedBob));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
