package com.notably.view;

import static com.notably.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.notably.MainApp;

import javafx.fxml.FXML;

public class ViewPartTest {

    private static final String MISSING_FILE_PATH = "ViewPartTest/missingFile.fxml";
    private static final String INVALID_FILE_PATH = "ViewPartTest/invalidFile.fxml";
    private static final String VALID_FILE_PATH = "ViewPartTest/validFile.fxml";
    private static final String VALID_FILE_WITH_FX_ROOT_PATH = "ViewPartTest/validFileWithFxRoot.fxml";
    private static final TestFxmlObject VALID_FILE_ROOT = new TestFxmlObject("Hello World!");

    @TempDir
    public Path testFolder;

    @Test
    public void constructor_nullFileUrl_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestViewPart<Object>((URL) null));
        assertThrows(NullPointerException.class, () -> new TestViewPart<Object>((URL) null, new Object()));
    }

    @Test
    public void constructor_missingFileUrl_throwsAssertionError() throws Exception {
        URL missingFileUrl = new URL(testFolder.toUri().toURL(), MISSING_FILE_PATH);
        assertThrows(AssertionError.class, () -> new TestViewPart<Object>(missingFileUrl));
        assertThrows(AssertionError.class, () -> new TestViewPart<Object>(missingFileUrl, new Object()));
    }

    @Test
    public void constructor_invalidFileUrl_throwsAssertionError() {
        URL invalidFileUrl = getTestFileUrl(INVALID_FILE_PATH);
        assertThrows(AssertionError.class, () -> new TestViewPart<Object>(invalidFileUrl));
        assertThrows(AssertionError.class, () -> new TestViewPart<Object>(invalidFileUrl, new Object()));
    }

    @Test
    public void constructor_validFileUrl_loadsFile() {
        URL validFileUrl = getTestFileUrl(VALID_FILE_PATH);
        assertEquals(VALID_FILE_ROOT, new TestViewPart<TestFxmlObject>(validFileUrl).getRoot());
    }

    @Test
    public void constructor_validFileWithFxRootUrl_loadsFile() {
        URL validFileUrl = getTestFileUrl(VALID_FILE_WITH_FX_ROOT_PATH);
        TestFxmlObject root = new TestFxmlObject();
        assertEquals(VALID_FILE_ROOT, new TestViewPart<TestFxmlObject>(validFileUrl, root).getRoot());
    }

    @Test
    public void constructor_nullFileName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestViewPart<Object>((String) null));
        assertThrows(NullPointerException.class, () -> new TestViewPart<Object>((String) null, new Object()));
    }

    @Test
    public void constructor_missingFileName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestViewPart<Object>(MISSING_FILE_PATH));
        assertThrows(NullPointerException.class, () -> new TestViewPart<Object>(MISSING_FILE_PATH, new Object()));
    }

    @Test
    public void constructor_invalidFileName_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new TestViewPart<Object>(INVALID_FILE_PATH));
        assertThrows(AssertionError.class, () -> new TestViewPart<Object>(INVALID_FILE_PATH, new Object()));
    }

    private URL getTestFileUrl(String testFilePath) {
        String testFilePathInView = "/view/" + testFilePath;
        URL testFileUrl = MainApp.class.getResource(testFilePathInView);
        assertNotNull(testFileUrl, testFilePathInView + " does not exist.");
        return testFileUrl;
    }

    /**
     * ViewPart used for testing.
     * It should only be used with invalid FXML files or the valid file located at {@link VALID_FILE_PATH}.
     */
    private static class TestViewPart<T> extends ViewPart<T> {

        @FXML
        private TestFxmlObject validFileRoot; // Check that @FXML annotations work

        TestViewPart(URL fxmlFileUrl, T root) {
            super(fxmlFileUrl, root);
        }

        TestViewPart(String fxmlFileName, T root) {
            super(fxmlFileName, root);
        }

        TestViewPart(URL fxmlFileUrl) {
            super(fxmlFileUrl);
            assertEquals(VALID_FILE_ROOT, validFileRoot);
        }

        TestViewPart(String fxmlFileName) {
            super(fxmlFileName);
            assertEquals(VALID_FILE_ROOT, validFileRoot);
        }

    }

}
