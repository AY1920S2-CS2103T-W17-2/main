package com.notably.logic.parser.suggestion;

import static com.notably.logic.parser.suggestion.SuggestionCommandParserTestUtil.assertParseFailure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.suggestion.SearchSuggestionCommand;
import com.notably.logic.commands.suggestion.SuggestionCommand;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;
import com.notably.model.ModelManager;
import com.notably.model.block.Block;
import com.notably.model.block.BlockImpl;
import com.notably.model.block.BlockModel;
import com.notably.model.block.BlockModelImpl;
import com.notably.model.block.Body;
import com.notably.model.block.Title;
import com.notably.model.suggestion.SuggestionItem;
import com.notably.model.suggestion.SuggestionItemImpl;
import com.notably.model.suggestion.SuggestionModel;
import com.notably.model.suggestion.SuggestionModelImpl;
import com.notably.model.viewstate.ViewStateModel;
import com.notably.model.viewstate.ViewStateModelImpl;

public class SearchSuggestionCommandParserTest {
    private static AbsolutePath toRoot;
    private static AbsolutePath toCs2103;
    private static AbsolutePath toCs3230;
    private static AbsolutePath toCs2103Week1;
    private static AbsolutePath toCs2103Week2;
    private static AbsolutePath toCs2103Week3;
    private static AbsolutePath toCs2103Week1Lecture;
    private static Model model;
    private static Body toCs2103Week1LectureBody = new Body("Week 1 is ezpz. I am falling asleep.");
    private static Body toCs2103Week2Body = new Body("I was wrong. This mod is so time consuming");
    private static Body toCs2103Week3Body = new Body("Will I fail this mod?");
    private static Body toCs3230Body = new Body("I confirm fail this mod. Fail lah fail lah.");
    private static SearchSuggestionCommandParser searchSuggestionCommandParser;

    private static final String COMMAND_WORD = "search";
    private static final String RESPONSE_MESSAGE = "Search through all notes based on keyword";

    @BeforeAll
    public static void setUp() {
        // Set up paths
        toRoot = AbsolutePath.fromString("/");
        toCs2103 = AbsolutePath.fromString("/CS2103");
        toCs3230 = AbsolutePath.fromString("/CS3230");
        toCs2103Week1 = AbsolutePath.fromString("/CS2103/Week1");
        toCs2103Week2 = AbsolutePath.fromString("/CS2103/Week2");
        toCs2103Week3 = AbsolutePath.fromString("/CS2103/Week3");
        toCs2103Week1Lecture = AbsolutePath.fromString("/CS2103/Week1/Lecture");

        // Set up model
        BlockModel blockModel = new BlockModelImpl();
        SuggestionModel suggestionModel = new SuggestionModelImpl();
        ViewStateModel viewStateModel = new ViewStateModelImpl();
        model = new ModelManager(blockModel, suggestionModel, viewStateModel);

        // Add test data to model
        Block cs2103 = new BlockImpl(new Title("CS2103"));
        Block cs3230 = new BlockImpl(new Title("CS3230"), toCs3230Body);
        model.addBlockToCurrentPath(cs2103);
        model.addBlockToCurrentPath(cs3230);

        Block week1 = new BlockImpl(new Title("Week1"));
        Block week2 = new BlockImpl(new Title("Week2"), toCs2103Week2Body);
        Block week3 = new BlockImpl(new Title("Week3"), toCs2103Week3Body);
        model.setCurrentlyOpenBlock(toCs2103);
        model.addBlockToCurrentPath(week1);
        model.addBlockToCurrentPath(week2);
        model.addBlockToCurrentPath(week3);

        Block lecture = new BlockImpl(new Title("Lecture"), toCs2103Week1LectureBody);
        model.setCurrentlyOpenBlock(toCs2103Week1);
        model.addBlockToCurrentPath(lecture);

        // initialize parser
        searchSuggestionCommandParser = new SearchSuggestionCommandParser();
    }

    @Test
    public void parse_emptyPath_throwsParseException() {
        assertParseFailure(searchSuggestionCommandParser, " ",
                "Keyword cannot be empty");
    }

    @Test
    public void parse_keywordFound_returnsOpenCommand() throws ParseException {
        SuggestionCommand command = searchSuggestionCommandParser.parse("fail");
        assertTrue(command instanceof SearchSuggestionCommand);

        command.execute(model);

        // Test responseText
        String expectedResponseText = "Search through all notes based on keyword";
        assertEquals(Optional.of(expectedResponseText), model.responseTextProperty().getValue());

        // Expected suggestions
        List<SuggestionItem> expectedSuggestions = new ArrayList<>();
        SuggestionItem suggestionItem = new SuggestionItemImpl(toCs3230.getStringRepresentation(), 3,
                null);
        SuggestionItem suggestionItem1 = new SuggestionItemImpl(toCs2103Week3.getStringRepresentation(), 1,
                null);
        expectedSuggestions.add(suggestionItem);
        expectedSuggestions.add(suggestionItem1);

        List<SuggestionItem> suggestions = model.getSuggestions();

        // Test displayText and frequency
        for (int i = 0; i < expectedSuggestions.size(); i++) {
            SuggestionItem suggestion = suggestions.get(i);
            SuggestionItem expectedSuggestion = expectedSuggestions.get(i);
            assertEquals(expectedSuggestion.getProperty("displayText"), suggestion.getProperty("displayText"));
            assertEquals(expectedSuggestion.getProperty("frequency"), suggestion.getProperty("frequency"));
        }

        // Expected currently open paths
        List<AbsolutePath> expectedCurrentlyOpenPaths = new ArrayList<>();
        expectedCurrentlyOpenPaths.add(toCs3230);
        expectedCurrentlyOpenPaths.add(toCs2103Week3);

        // Test Runnable action and check currentlyOpenPath
        for (int i = 0; i < expectedCurrentlyOpenPaths.size(); i++) {
            SuggestionItem suggestion = suggestions.get(i);
            suggestion.getAction().run();

            assertEquals(expectedCurrentlyOpenPaths.get(i), model.getCurrentlyOpenPath());
        }
    }

    @Test
    public void parse_keywordNotFound_returnsEmptyList() throws ParseException {
        SuggestionCommand command = searchSuggestionCommandParser.parse("dayum");
        assertTrue(command instanceof SearchSuggestionCommand);

        command.execute(model);

        // Test responseText
        String expectedResponseText = "Search through all notes based on keyword";
        assertEquals(Optional.of(expectedResponseText), model.responseTextProperty().getValue());

        List<SuggestionItem> suggestions = model.getSuggestions();
        assertTrue(suggestions.size() == 0);
    }
}
