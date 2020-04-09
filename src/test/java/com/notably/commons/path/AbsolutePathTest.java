package com.notably.commons.path;

import static com.notably.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.notably.commons.path.exceptions.InvalidPathException;


class AbsolutePathTest {

    @Test
    public void fromString_validInputString_generateAbsolutePath() {
        final AbsolutePath testInput = AbsolutePath.fromString("/CS2103 Happy!/notes/#8.4");

        List<String> paths = new ArrayList<>();
        paths.add("CS2103 Happy!");
        paths.add("notes");
        paths.add("#8.4");

        assertEquals(paths, testInput.getComponents());
    }

    @Test
    public void fromString_validRootString_generateAbsolutePath() {
        final AbsolutePath testInput = AbsolutePath.fromString("/");

        List<String> paths = new ArrayList<>();

        assertEquals(paths, testInput.getComponents());
    }

    @Test
    public void fromString_invalidInputString1_exceptionThrown() {
        assertThrows(InvalidPathException.class, () -> AbsolutePath.fromString("CS2103/notes"));
    }

    @Test
    public void toRelativePath_validInput_correctedPath() {
        final AbsolutePath inputAbsolutePath = AbsolutePath.fromString("/&CS2103/notes/hello");
        final AbsolutePath inputCurrPath = AbsolutePath.fromString("/&CS2103");

        final RelativePath expectedOutput = RelativePath.fromString("notes/hello");

        assertEquals(expectedOutput, inputAbsolutePath.toRelativePath(inputCurrPath));
    }

    @Test
    public void fromRelativePath_validInput_convertedAbsolutePath() {
        final RelativePath inputRelPath = RelativePath.fromString("../../hello??");
        final AbsolutePath inputCurrPath = AbsolutePath.fromString("/CS.2103/filler");

        List<String> expectedPaths = new ArrayList<>();
        expectedPaths.add("hello??");

        assertEquals(expectedPaths, AbsolutePath.fromRelativePath(inputRelPath, inputCurrPath).getComponents());
    }

    @Test
    public void fromRelativePath_invalidInput_exceptionThrown() {
        final RelativePath inputRelPath = RelativePath.fromString("../../hello");
        final AbsolutePath inputCurrPath = AbsolutePath.fromString("/CS2103");
        assertThrows(InvalidPathException.class, () -> AbsolutePath.fromRelativePath(inputRelPath, inputCurrPath));
    }

    @Test
    public void equals_similarPathWithDifferentCasing_pathAreEqual() {
        final AbsolutePath inputRelativePath1 = AbsolutePath.fromString("/cs2103");
        final AbsolutePath inputRelativePath2 = AbsolutePath.fromString("/CS2103");

        assertEquals(inputRelativePath2, inputRelativePath1);
    }
}
