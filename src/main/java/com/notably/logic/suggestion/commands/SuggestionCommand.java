package com.notably.logic.suggestion.commands;

import com.notably.model.Model;

/**
 * Represents a particular strategy in generating suggestions.
 */
public interface SuggestionCommand {
    /**
     * Adds a list of generated suggestions to the app's model.
     * @param model The app's model.
     */
    void execute(Model model);
}
