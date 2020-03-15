package com.notably.model.suggestion;

import java.util.List;
import java.util.Optional;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;

/**
 * Represents the Suggestion Model instance.
 * The UI will directly interact with this model to display the suggestions result.
 */
public interface SuggestionModel {
    /**
     * Gets the list of suggestions saved in the model.
     * @return The Observable List of suggested items.
     */
    ObservableList<SuggestionItem> getSuggestions();

    /**
     * Saves the list of suggestions in the model.
     * @param suggestions The list of paths or notes that match the user input.
     */
    void setSuggestions(List<SuggestionItem> suggestions);

    /**
     * Shows user the meaning of their input, e.g. "Create a new note".
     * When input cannot get corrected, the "error" message will be shown in commandTextProperty.
     * TODO: add logo.
     * @return The meaning of the user input.
     */
    Property<Optional<String>> commandTextProperty();

    /**
     * Sets the command text.
     * @param commandText The "command" that the user inputs, e.g. open, search, etc.
     */
    void setCommandInputText(String commandText);

    String getCommandInputText();

    /**
     * Resets the command text when the user clears the input text field.
     */
    void clearCommandInputText();

    /**
     * Resets the list of suggestions.
     */
    void clearSuggestions();
}
