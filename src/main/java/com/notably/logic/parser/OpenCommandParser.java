package com.notably.logic.parser;

import static com.notably.logic.parser.CliSyntax.PREFIX_TITLE;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import com.notably.commons.LogsCenter;
import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.OpenCommand;
import com.notably.logic.correction.CorrectionEngine;
import com.notably.logic.correction.CorrectionResult;
import com.notably.logic.correction.CorrectionStatus;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;

/**
 * Represent a Parser for OpenCommand.
 */
public class OpenCommandParser implements CommandParser<OpenCommand> {
    private Model notablyModel;
    private CorrectionEngine<AbsolutePath> pathCorrectionEngine;
    private final Logger logger = LogsCenter.getLogger(getClass());

    public OpenCommandParser(Model notablyModel, CorrectionEngine<AbsolutePath> pathCorrectionEngine) {
        this.notablyModel = notablyModel;
        this.pathCorrectionEngine = pathCorrectionEngine;
    }
    /**
     * Creates OpenCommand with user input.
     * @param args to be parse by into CorrectionEngine.
     * @return List of command containing OpenCammnd.
     * @throws ParseException
     */
    public List<OpenCommand> parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TITLE);

        String titles;
        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_TITLE)
                || !argMultimap.getPreamble().isEmpty()) {
            titles = args.trim();
            if (titles.isEmpty()) {
                throw new ParseException("Path cannot be empty");
            }
        } else {
            titles = argMultimap.getValue(PREFIX_TITLE).get();
        }

        AbsolutePath uncorrectedPath = ParserUtil.createAbsolutePath(titles, notablyModel.getCurrentlyOpenPath());
        CorrectionResult<AbsolutePath> correctionResult = pathCorrectionEngine.correct(uncorrectedPath);
        if (correctionResult.getCorrectionStatus() == CorrectionStatus.FAILED) {
            throw new ParseException("Invalid Path");
        }

        logger.info("OpenCommand created");
        return List.of(new OpenCommand(correctionResult.getCorrectedItems().get(0)));
    }
}
