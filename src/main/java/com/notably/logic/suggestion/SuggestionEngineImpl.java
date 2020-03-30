package com.notably.logic.suggestion;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.notably.logic.commands.suggestion.DeleteSuggestionCommand;
import com.notably.logic.commands.suggestion.EditSuggestionCommand;
import com.notably.logic.commands.suggestion.ErrorSuggestionCommand;
import com.notably.logic.commands.suggestion.ExitSuggestionCommand;
import com.notably.logic.commands.suggestion.HelpSuggestionCommand;
import com.notably.logic.commands.suggestion.NewSuggestionCommand;
import com.notably.logic.commands.suggestion.OpenSuggestionCommand;
import com.notably.logic.commands.suggestion.SearchSuggestionCommand;
import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.logic.correction.StringCorrectionEngine;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.logic.parser.suggestion.DeleteSuggestionCommandParser;
import com.notably.logic.parser.suggestion.OpenSuggestionCommandParser;
import com.notably.logic.parser.suggestion.SearchSuggestionCommandParser;
import com.notably.model.Model;

import javafx.beans.property.StringProperty;

/**
 * An implementation class of SuggestionEngine.
 */
public class SuggestionEngineImpl implements SuggestionEngine {
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final List<String> COMMAND_LIST = List.of("new", "edit", "delete", "exit", "open", "help");
    private static final int DISTANCE_THRESHOLD = 2;

    private Model model;
    private StringCorrectionEngine correctionEngine;

    public SuggestionEngineImpl(Model model) {
        this.model = model;
        correctionEngine = new StringCorrectionEngine(COMMAND_LIST, DISTANCE_THRESHOLD);

        autoUpdateInput(model.inputProperty());
    }

    @Override
    public void suggest(String userInput) {
        SuggestionCommand suggestionCommand = parseCommand(userInput);
        suggestionCommand.execute(model);
    }

    /**
     * Parses the user input.
     * @param userInput The user's input.
     * @return The corresponding SuggestionCommand.
     */
    private SuggestionCommand parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new ErrorSuggestionCommand(
                "Invalid command format. To see the list of available commands, type: help");
        }

        String commandWord = matcher.group("commandWord");

        if (commandWord.length() > 1) {
            Optional<String> correctedCommand = correctionEngine.correct(commandWord).getCorrectedItem();
            if (correctedCommand.equals(Optional.empty())) {
                return new ErrorSuggestionCommand(
                    "Invalid command. To see the list of available commands, type: help");
            }
            commandWord = correctedCommand.get();
        }

        final String arguments = matcher.group("arguments");

        switch (commandWord) {
        case OpenSuggestionCommand.COMMAND_WORD:
        case OpenSuggestionCommand.COMMAND_SHORTHAND:
            try {
                return new OpenSuggestionCommandParser(model).parse(arguments);
            } catch (ParseException e) {
                return new ErrorSuggestionCommand(e.getMessage());
            }

        case DeleteSuggestionCommand.COMMAND_WORD:
        case DeleteSuggestionCommand.COMMAND_SHORTHAND:
            try {
                return new DeleteSuggestionCommandParser(model).parse(arguments);
            } catch (ParseException e) {
                return new ErrorSuggestionCommand(e.getMessage());
            }

        case SearchSuggestionCommand.COMMAND_WORD:
        case SearchSuggestionCommand.COMMAND_SHORTHAND:
            try {
                return new SearchSuggestionCommandParser().parse(arguments);
            } catch (ParseException e) {
                return new ErrorSuggestionCommand(e.getMessage());
            }

        case NewSuggestionCommand.COMMAND_WORD:
        case NewSuggestionCommand.COMMAND_SHORTHAND:
            return new NewSuggestionCommand();

        case EditSuggestionCommand.COMMAND_WORD:
        case EditSuggestionCommand.COMMAND_SHORTHAND:
            return new EditSuggestionCommand();

        case HelpSuggestionCommand.COMMAND_WORD:
        case HelpSuggestionCommand.COMMAND_SHORTHAND:
            return new HelpSuggestionCommand();

        case ExitSuggestionCommand.COMMAND_WORD:
            return new ExitSuggestionCommand();

        default:
            return new ErrorSuggestionCommand(
                "Invalid command. To see the list of available commands, type: help");
        }
    }

    private void autoUpdateInput(StringProperty inputProperty) {
        inputProperty.addListener((observable, oldValue, newValue) -> {
            suggest(newValue);
        });
    }
}
