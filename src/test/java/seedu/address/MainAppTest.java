package seedu.address;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.StorageManager;

public class MainAppTest {

    // No EV or BVA
    @Test
    public void stop_savesAddressBook() throws Exception {
        MainApp mainApp = new MainApp();
        StorageStub storageStub = new StorageStub();
        ModelStub modelStub = new ModelStub();

        mainApp.storage = storageStub;
        mainApp.model = modelStub;

        mainApp.stop();

        assertTrue(storageStub.isAddressBookSaved());
        assertTrue(storageStub.isUserPrefsSaved());
    }

    @Test
    public void stop_saveUserPrefsFails_addressBookStillSaved() throws Exception {
        MainApp mainApp = new MainApp();
        StorageStub storageStub = new StorageStub(true, false);
        ModelStub modelStub = new ModelStub();

        mainApp.storage = storageStub;
        mainApp.model = modelStub;

        mainApp.stop();

        assertTrue(storageStub.isAddressBookSaved());
    }

    @Test
    public void stop_saveAddressBookFails_doesNotThrow() throws Exception {
        MainApp mainApp = new MainApp();
        StorageStub storageStub = new StorageStub(false, true);
        ModelStub modelStub = new ModelStub();

        mainApp.storage = storageStub;
        mainApp.model = modelStub;

        mainApp.stop();
    }

    private static class StorageStub extends StorageManager {
        private boolean addressBookSaved = false;
        private boolean userPrefsSaved = false;
        private final boolean failUserPrefs;
        private final boolean failAddressBook;

        StorageStub() {
            this(false, false);
        }

        StorageStub(boolean failUserPrefs, boolean failAddressBook) {
            super(null, null);
            this.failUserPrefs = failUserPrefs;
            this.failAddressBook = failAddressBook;
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            if (failUserPrefs) {
                throw new IOException("forced failure");
            }
            userPrefsSaved = true;
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            if (failAddressBook) {
                throw new IOException("forced failure");
            }
            addressBookSaved = true;
        }

        public boolean isAddressBookSaved() {
            return addressBookSaved;
        }

        public boolean isUserPrefsSaved() {
            return userPrefsSaved;
        }
    }

    private static class ModelStub extends ModelManager {
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return new UserPrefs();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
