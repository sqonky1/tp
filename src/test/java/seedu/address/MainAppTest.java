package seedu.address;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.*;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

public class MainAppTest {

    /// No EV or BVA
    @Test
    public void stop_savesAddressBook() throws Exception {
        MainApp mainApp = new MainApp();
        StorageStub storageStub = new StorageStub();
        ModelStub modelStub = new ModelStub();

        mainApp.storage = storageStub;
        mainApp.model = modelStub;

        mainApp.stop();

        assertTrue(storageStub.addressBookSaved);
        assertTrue(storageStub.userPrefsSaved);
    }

    private static class StorageStub extends StorageManager {
        boolean addressBookSaved = false;
        boolean userPrefsSaved = false;

        StorageStub() {
            super(null, null);
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            addressBookSaved = true;
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            userPrefsSaved = true;
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
