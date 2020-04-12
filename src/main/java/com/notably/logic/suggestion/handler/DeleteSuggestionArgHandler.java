package com.notably.logic.suggestion.handler;

import static com.notably.commons.parser.CliSyntax.PREFIX_TITLE;

import java.util.Optional;
import java.util.logging.Logger;

import com.notably.commons.LogsCenter;
import com.notably.commons.parser.ArgumentMultimap;
import com.notably.commons.parser.ArgumentTokenizer;
import com.notably.commons.parser.ParserUtil;
import com.notably.commons.path.AbsolutePath;
import com.notably.logic.correction.CorrectionEngine;
import com.notably.logic.correction.CorrectionResult;
import com.notably.logic.correction.CorrectionStatus;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.logic.suggestion.generator.DeleteSuggestionGenerator;
import com.notably.logic.suggestion.generator.SearchSuggestionGenerator;
import com.notably.model.Model;

/**
 * Represents a Handler for DeleteSuggestionGenerator.
 */
public class DeleteSuggestionArgHandler implements SuggestionArgHandler<DeleteSuggestionGenerator> {
    public static final String COMMAND_WORD = "delete";

    private static final String RESPONSE_MESSAGE = "Delete a note";
    private static final String RESPONSE_MESSAGE_WITH_TITLE = "Delete a note titled \"%s\"";
    private static final String ERROR_MESSAGE_CANNOT_DELETE_NOTE = "Cannot delete \"%s\" as it is an invalid path";

    private static final Logger logger = LogsCenter.getLogger(DeleteSuggestionArgHandler.class);

    private Model model;
    private CorrectionEngine<AbsolutePath> pathCorrectionEngine;

    public DeleteSuggestionArgHandler(Model model, CorrectionEngine<AbsolutePath> pathCorrectionEngine) {
        this.model = model;
        this.pathCorrectionEngine = pathCorrectionEngine;
    }

    /**
     * Handles user input in the context of the DeleteSuggestionGenerator.
     *
     * @param userInput The user's input.
     * @return An optional DeleteSuggestionGenerator object with a corrected absolute path.
     */
    @Override
    public Optional<DeleteSuggestionGenerator> handleArg(String userInput) {
        logger.info("Starting handleArg method inside DeleteSuggestionArgHandler");
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_TITLE);

        String title;
        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_TITLE)
                || !argMultimap.getPreamble().isEmpty()) {
            title = userInput.trim();
        } else {
            title = argMultimap.getValue(PREFIX_TITLE).get();
        }

        if (title.isEmpty()) {
            model.setResponseText(RESPONSE_MESSAGE);
            logger.warning("title is empty");
            return Optional.empty();
        }

        AbsolutePath uncorrectedPath;
        try {
            uncorrectedPath = ParserUtil.createAbsolutePath(title, model.getCurrentlyOpenPath());
        } catch (ParseException pe) {
            logger.warning(String.format(ERROR_MESSAGE_CANNOT_DELETE_NOTE, title));
            model.setResponseText(String.format(ERROR_MESSAGE_CANNOT_DELETE_NOTE, title));
            return Optional.empty();
        }

        model.setResponseText(String.format(RESPONSE_MESSAGE_WITH_TITLE, title));

        CorrectionResult<AbsolutePath> correctionResult = pathCorrectionEngine.correct(uncorrectedPath);
        if (correctionResult.getCorrectionStatus() == CorrectionStatus.FAILED) {
            logger.warning(String.format("Failed to correct \"%s\".", title));
            return Optional.empty();
        }

        return Optional.of(new DeleteSuggestionGenerator(correctionResult.getCorrectedItems(), title));
    }
}
