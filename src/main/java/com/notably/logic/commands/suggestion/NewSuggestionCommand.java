package com.notably.logic.commands.suggestion;

import java.util.Objects;

import com.notably.model.Model;

/**
 * Represents a suggestion command object to create a new note.
 */
public class NewSuggestionCommand implements SuggestionCommand {
    public static final String COMMAND_WORD = "new";
    public static final String COMMAND_SHORTHAND = "n";

    private static final String RESPONSE_MESSAGE = "Create a new note";

    @Override
    public void execute(Model model) {
        // Nullity check
        Objects.requireNonNull(model);

        // Set response text
        model.setResponseText(RESPONSE_MESSAGE);
    }
}
