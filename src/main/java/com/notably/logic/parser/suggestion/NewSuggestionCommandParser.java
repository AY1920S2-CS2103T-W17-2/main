package com.notably.logic.parser.suggestion;

import static com.notably.logic.parser.CliSyntax.PREFIX_BODY;
import static com.notably.logic.parser.CliSyntax.PREFIX_JUMP;
import static com.notably.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Optional;

import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.logic.parser.ArgumentMultimap;
import com.notably.logic.parser.ArgumentTokenizer;
import com.notably.logic.parser.ParserUtil;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;

/**
 * Represents a Parser for New Command.
 */
public class NewSuggestionCommandParser implements SuggestionCommandParser<SuggestionCommand> {
    private static final String RESPONSE_MESSAGE = "Create a new note";
    private static final String RESPONSE_MESSAGE_WITH_TITLE = "Create a new note entitled ";

    private Model model;

    public NewSuggestionCommandParser(Model model) {
        this.model = model;
    }
    /**
     * Parses input and displays the appropriate response text.
     * @param userInput .
     * @return List of command to execute.
     * @throws ParseException when input is invalid.
     */
    public Optional<SuggestionCommand> parse(String userInput) {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_TITLE, PREFIX_BODY, PREFIX_JUMP);

        String title;
        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_TITLE)
                || !argMultimap.getPreamble().isEmpty()) {
            title = userInput.trim();
        } else {
            title = argMultimap.getValue(PREFIX_TITLE).get();
        }

        if (title.isBlank()) {
            model.setResponseText(RESPONSE_MESSAGE);
        } else if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_JUMP)) { // If user does NOT type "-o"
            model.setResponseText(RESPONSE_MESSAGE_WITH_TITLE + "\"" + title + "\"");
        } else {
            model.setResponseText(RESPONSE_MESSAGE_WITH_TITLE + "\"" + title + "\"" + " and open this note");
        }

        return Optional.empty();

    }
}