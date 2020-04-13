package com.notably.model.suggestion;

import java.util.List;

import javafx.collections.ObservableList;

/**
 * Represents the Suggestion Model instance.
 * The UI will directly interact with this model to display the suggestions result.
 */
public interface SuggestionModel {
    /**
     * Gets the list of suggestions saved in the model.
     *
     * @return The Observable List of suggested items.
     */
    ObservableList<SuggestionItem> getSuggestions();

    /**
     * Saves the list of suggestions in the model.
     *
     * @param suggestions The list of paths or notes that match the user input.
     */
    void setSuggestions(List<SuggestionItem> suggestions);

    /**
     * Resets the list of suggestions.
     */
    void clearSuggestions();
}
