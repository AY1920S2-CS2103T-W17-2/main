package com.notably.logic.suggestion;

/**
 * Represents the Suggestion Engine instance.
 * This is the class that manages the generation of suggestions.
 */
public interface SuggestionEngine {
    /**
     * Generates suggestions.
     * @param input User's input.
     */
    void suggest(String input);
}
