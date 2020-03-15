package com.notably.model.suggestion;

/**
 * Represents the instance of the suggested item.
 */
public interface SuggestionItem {
    /**
     * Gets a user-friendly text suggestion to be displayed to the user, e.g. "open -t root/cs2103".
     * @return The suggestion.
     */
    String getDisplayText();

    /**
     * Executes the action taken after the suggestion item is chosen.
     * The action will be different depending on the type of SuggestionCommand.
     * @return The action taken after the suggestion item is chosen.
     */
    Runnable run();
}
