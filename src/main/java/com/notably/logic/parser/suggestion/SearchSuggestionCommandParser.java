package com.notably.logic.parser.suggestion;

import static com.notably.logic.parser.CliSyntax.PREFIX_SEARCH;

import com.notably.logic.commands.suggestion.SearchSuggestionCommand;
import com.notably.logic.parser.ArgumentMultimap;
import com.notably.logic.parser.ArgumentTokenizer;
import com.notably.logic.parser.ParserUtil;
import com.notably.logic.parser.exceptions.ParseException;

/**
 * Represents a Parser for SearchSuggestionCommand.
 */
public class SearchSuggestionCommandParser implements SuggestionCommandParser<SearchSuggestionCommand> {
    /**
     * Parses user input in the context of the SearchSuggestionCommand.
     * @param userInput The user's input.
     * @return A SearchSuggestionCommand object with a corrected absolute path.
     * @throws ParseException if the user input is in a wrong format.
     */
    @Override
    public SearchSuggestionCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_SEARCH);

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_SEARCH)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format("Invalid input"));
        }

        return new SearchSuggestionCommand(userInput);
    }
}
