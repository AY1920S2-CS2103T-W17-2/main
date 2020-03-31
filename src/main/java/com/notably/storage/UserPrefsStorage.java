package com.notably.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import com.notably.commons.exceptions.DataConversionException;
import com.notably.model.userpref.ReadOnlyUserPrefModel;
import com.notably.model.userpref.UserPrefModel;

/**
 * Represents a storage for {@link com.notably.model.UserPrefModel}.
 */
public interface UserPrefsStorage {

    /**
     * Returns the file path of the UserPrefs data file.
     */
    Path getUserPrefsFilePath();

    /**
     * Returns UserPrefs data from storage.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<UserPrefModel> readUserPrefs() throws DataConversionException, IOException;

    /**
     * Saves the given {@link com.notably.model.ReadOnlyUserPrefModel} to the storage.
     * @param userPrefs cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveUserPrefs(ReadOnlyUserPrefModel userPrefs) throws IOException;

}
