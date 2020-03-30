package com.notably.logic.parser;

import static com.notably.logic.parser.CliSyntax.PREFIX_BODY;

import java.util.ArrayList;
import java.util.List;

import com.notably.logic.commands.EditCommand;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.block.Body;

/**
 * Represent a parser for EditCommand.
 */
public class EditCommandParser implements CommandParser<EditCommand> {

    /**
     * Parse input and create NewCommand and OpenCommand.
     * @param args parse userInput used to create block.
     * @return List of command to execute.
     * @throws ParseException when input is invalid.
     */
    public List<EditCommand> parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_BODY);

        String body;
        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BODY)
                || !argMultimap.getPreamble().isEmpty()) {
            body = args.trim();
        } else {
            body = argMultimap.getValue(PREFIX_BODY).get();
        }

        List<EditCommand> command = new ArrayList<>();
        command.add(new EditCommand(new Body(body)));

        return command;
    }
}
