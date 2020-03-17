package com.notably.logic.suggestion;

import java.util.Optional;

import com.notably.commons.core.path.AbsolutePath;
import com.notably.commons.core.path.Path;
import com.notably.commons.core.path.RelativePath;
import com.notably.commons.core.path.exceptions.InvalidPathException;
import com.notably.logic.suggestion.commands.OpenSuggestionCommand;
import com.notably.logic.suggestion.commands.SearchSuggestionCommand;
import com.notably.logic.suggestion.commands.SuggestionCommand;
import com.notably.model.Model;

public class SuggestionEngineImpl implements SuggestionEngine {
    String input;
    Model model;

    public SuggestionEngineImpl(String input, Model model) {
        this.input = input;
        this.model = model;
    }

    @Override
    public void suggest(String input) {
        Optional<String> correctedInput = getCorrectedInput(input);
        SuggestionCommand suggestionCommand = null;
        try {
            suggestionCommand = getSuggestionCommand(correctedInput);
        } catch (InvalidPathException e) {
            e.printStackTrace();
        }
        suggestionCommand.execute(model);
    }

    /**
     * Converts the input into the correct format if needed.
     * TODO: integrate with CorrectionEngine.
     * @param input The user's input.
     * @return The corrected input.
     */
    private Optional<String> getCorrectedInput(String input) {
        return Optional.of(input);
    }

    private String getPathName(String correctedInput) {
        return correctedInput.split(" ")[1];
    }

    private String getCommandWord(String correctedInput) {
        return correctedInput.split(" ")[0];
    }

    private SuggestionCommand getSuggestionCommand(Optional<String> correctedInput) throws InvalidPathException {
        if (!correctedInput.isPresent()) {
            return new ErrorSuggestionCommand();
        }

        String command = getCommandWord(correctedInput.get());
        String pathName = getPathName(correctedInput.get());
        Path path;
        if (pathName.charAt(0) == '/') {
            path = new AbsolutePath().fromString(pathName);
        } else {
            path = new RelativePath().fromString(pathName);
        }

        SuggestionCommand suggestionCommand;
        switch (command) {
            case "open":
                suggestionCommand = new OpenSuggestionCommand(path);
                break;
            case "search":
                suggestionCommand = new SearchSuggestionCommand(path);
                break;
            default:
                suggestionCommand = new ErrorSuggestionCommand();
        }
        return suggestionCommand;
    }

}
