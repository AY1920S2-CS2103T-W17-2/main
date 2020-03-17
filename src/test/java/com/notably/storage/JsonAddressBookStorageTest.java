//TODO: To be enabled or changed when refactoring is completed
//package com.notably.storage;
//
//import static com.notably.testutil.Assert.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.io.TempDir;
//
//import com.notably.commons.exceptions.DataConversionException;
//import com.notably.model.AddressBook;
//import com.notably.model.ReadOnlyAddressBook;
//
//public class JsonAddressBookStorageTest {
//    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");
//
//    @TempDir
//    public Path testFolder;
//
//    @Test
//    public void readAddressBook_nullFilePath_throwsNullPointerException() {
//        assertThrows(NullPointerException.class, () -> readAddressBook(null));
//    }
//
//    private java.util.Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws Exception {
//        return new JsonAddressBookStorage(Paths.get(filePath)).readAddressBook(addToTestDataPathIfNotNull(filePath));
//    }
//
//    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
//        return prefsFileInTestDataFolder != null
//                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
//                : null;
//    }
//
//    @Test
//    public void read_missingFile_emptyResult() throws Exception {
//        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
//    }
//
//    @Test
//    public void read_notJsonFormat_exceptionThrown() {
//        assertThrows(DataConversionException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
//    }
//
//    @Test
//    public void readAddressBook_invalidPersonAddressBook_throwDataConversionException() {
//        assertThrows(DataConversionException.class, () -> readAddressBook("invalidPersonAddressBook.json"));
//    }
//
//    @Test
//    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataConversionException() {
//        assertThrows(DataConversionException.class, () -> readAddressBook("invalidAndValidPersonAddressBook.json"));
//    }
//
//    @Test
//    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
//
//
//    }
//
//    @Test
//    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
//        assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
//    }
//
//    /**
//     * Saves {@code addressBook} at the specified {@code filePath}.
//     */
//    private void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) {
//        try {
//            new JsonAddressBookStorage(Paths.get(filePath))
//                    .saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
//        } catch (IOException ioe) {
//            throw new AssertionError("There should not be an error writing to the file.", ioe);
//        }
//    }
//
//    @Test
//    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
//        assertThrows(NullPointerException.class, () -> saveAddressBook(new AddressBook(), null));
//    }
//}