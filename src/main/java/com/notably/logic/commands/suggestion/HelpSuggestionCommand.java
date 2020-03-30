package com.notably.logic.commands.suggestion;

import java.util.Objects;

import com.notably.model.Model;

/**
 * Represents a suggestion command object to display a list of available commands.
 */
public class HelpSuggestionCommand implements SuggestionCommand {
    public static final String COMMAND_WORD = "help";
    private static final String RESPONSE_MESSAGE = "Display a list of available commands";
    private static final String COMMAND_SHORTHAND = "h";

    @Override
    public void execute(Model model) {
        // Nullity check
        Objects.requireNonNull(model);

        // Set response text
        model.setResponseText(RESPONSE_MESSAGE);
    }
}
