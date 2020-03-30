package com.notably.logic.commands.suggestion;

import java.util.Objects;

import com.notably.model.Model;

/**
 * Represents a suggestion command object to edit a currently open note.
 */
public class EditSuggestionCommand implements SuggestionCommand {
    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_SHORTHAND = "e";

    private static final String RESPONSE_MESSAGE = "Edit a currently open note";

    @Override
    public void execute(Model model) {
        // Nullity check
        Objects.requireNonNull(model);

        // Set response text
        model.setResponseText(RESPONSE_MESSAGE);
    }
}
