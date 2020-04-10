package com.notably.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.OpenCommand;
import com.notably.logic.commands.exceptions.CommandException;
import com.notably.logic.correction.AbsolutePathCorrectionEngine;
import com.notably.logic.correction.CorrectionEngine;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;
import com.notably.model.ModelManager;
import com.notably.model.block.BlockModel;
import com.notably.model.suggestion.SuggestionModel;
import com.notably.model.suggestion.SuggestionModelImpl;
import com.notably.model.viewstate.ViewStateModel;
import com.notably.model.viewstate.ViewStateModelImpl;
import com.notably.testutil.TypicalBlockModel;

class OpenCommandParserTest {
    private static final int CORRECTION_THRESHOLD = 2;
    private static final boolean USE_FORWARD_MATCHING = true;
    private static AbsolutePath toBlock;
    private static AbsolutePath toAnother;
    private static AbsolutePath toAnotherBlock;
    private static Model model;
    private static OpenCommandParser openCommandParser;
    private static NotablyParser parser;

    @BeforeEach
    public void setUp() {
        // Set up model
        BlockModel blockModel = TypicalBlockModel.getTypicalBlockModel();
        SuggestionModel suggestionModel = new SuggestionModelImpl();
        ViewStateModel viewStateModel = new ViewStateModelImpl();
        model = new ModelManager(blockModel, suggestionModel, viewStateModel);
        CorrectionEngine<AbsolutePath> pathCorrectionEngine = new AbsolutePathCorrectionEngine(model,
                CORRECTION_THRESHOLD, USE_FORWARD_MATCHING);
        openCommandParser = new OpenCommandParser(model, pathCorrectionEngine);

        parser = new NotablyParser(model);
    }

    @Test
    void parse_emptyArgument_throwParseException() throws ParseException {
        assertThrows(ParseException.class, () -> openCommandParser.parse(""));
    }

    @Test
    void parse_emptyTitleArgument_throwParseException() throws ParseException {
        assertThrows(ParseException.class, () -> openCommandParser.parse("-t"));
    }

    @Test
    void parse_invalidPathArgument_throwParseException() throws ParseException {
        assertThrows(ParseException.class, () -> openCommandParser.parse("-t /nonExisting"));
    }

    @Test
    void parse_absolutePathArgument_openCommand() throws ParseException, CommandException {
        final AbsolutePath toOpenPath = TypicalBlockModel.PATH_TO_CS2106;
        final OpenCommand openCommand = openCommandParser.parse(" -t /Y2S2/CS2106").get(0);

        openCommand.execute(model);

        assertEquals(toOpenPath, model.currentlyOpenPathProperty().getValue());
    }

    @Test
    void parse_relativePathArgument_openCommand() throws ParseException, CommandException {
        final AbsolutePath toOpenPath = TypicalBlockModel.PATH_TO_CS2106;
        final OpenCommand openCommand = openCommandParser.parse(" -t CS2106").get(0);

        // Current directory `/Y2S2`
        openCommand.execute(model);

        //Expected directory after executing Command /another/block
        assertEquals(toOpenPath, model.currentlyOpenPathProperty().getValue());
    }
}