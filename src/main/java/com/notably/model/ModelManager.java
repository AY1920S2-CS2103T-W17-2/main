package com.notably.model;

import static com.notably.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.notably.commons.core.GuiSettings;
import com.notably.commons.core.LogsCenter;

import com.notably.model.suggestion.SuggestionItem;
import com.notably.model.suggestion.SuggestionModel;

import javafx.beans.property.Property;

import com.notably.model.commandinput.CommandInputModel;

import javafx.beans.property.StringProperty;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

// TODO: to update according to our Model classes.
/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Object> filteredPersons;
    private final SuggestionModel suggestionModel;
    private final CommandInputModel commandInputModel;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.filteredPersons = null;
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    // TODO: to update constructor according to our Model classes.
    public ModelManager(SuggestionModel suggestionModel, CommandInputModel commandInputModel) {
        this.suggestionModel = suggestionModel;
        this.commandInputModel = commandInputModel;
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public void hasPerson(Object person) {
    }

    @Override
    public void deletePerson(Object target) {
    }

    @Override
    public void addPerson(Object person) {
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Object target, Object editedPerson) {
        requireAllNonNull(target, editedPerson);

    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Object> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Object> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons);
    }

    //=========== Suggestion Model =============================================================
    @Override
    public ObservableList<SuggestionItem> getSuggestions() {
        return suggestionModel.getSuggestions();
    }

    @Override
    public void setSuggestions(List<SuggestionItem> suggestions) {
        suggestionModel.setSuggestions(suggestions);
    }

    @Override
    public Property<Optional<String>> getCommandTextProperty() {
        return suggestionModel.getCommandTextProperty();
    }

    @Override
    public void setCommandInputText(String commandInputText) {
        suggestionModel.setCommandInputText(commandInputText);
    }

    @Override
    public String getCommandInputText() {
        return suggestionModel.getCommandInputText();
    }

    @Override
    public void clearCommandInputText() {
        suggestionModel.clearCommandInputText();
    }

    @Override
    public void clearSuggestions() {
        suggestionModel.clearSuggestions();

    //========= CommandInputModel==================================================================

    @Override
    public StringProperty inputProperty() {
        return commandInputModel.inputProperty();
    }

    @Override
    public String getInput() {
        return commandInputModel.getInput();
    }

    @Override
    public void setInput(String input) {
        commandInputModel.setInput(input);
    }
}
