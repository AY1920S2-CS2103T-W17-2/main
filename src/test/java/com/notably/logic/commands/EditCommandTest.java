package com.notably.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.model.Model;
import com.notably.model.ModelManager;
import com.notably.model.block.Block;
import com.notably.model.block.BlockImpl;
import com.notably.model.block.BlockModel;
import com.notably.model.block.BlockModelImpl;
import com.notably.model.block.Body;
import com.notably.model.block.Title;
import com.notably.model.suggestion.SuggestionModel;
import com.notably.model.suggestion.SuggestionModelImpl;
import com.notably.model.viewstate.ViewStateModel;
import com.notably.model.viewstate.ViewStateModelImpl;

class EditCommandTest {
    private static Model model;
    private static AbsolutePath currentPath;

    @BeforeAll
    public static void setUp() {
        // Set up model
        BlockModel blockModel = new BlockModelImpl();
        SuggestionModel suggestionModel = new SuggestionModelImpl();
        ViewStateModel viewStateModel = new ViewStateModelImpl();
        model = new ModelManager(blockModel, suggestionModel, viewStateModel);

        // Populate model with test data
        Block currentBlock = (new BlockImpl(new Title("CS2103")));
        model.addBlockToCurrentPath(currentBlock);
        currentPath = AbsolutePath.fromString("/CS2103");
        model.setCurrentlyOpenBlock(AbsolutePath.fromString("/CS2103"));
    }
    @Test
    void execute() {
        final EditCommand editCommand = new EditCommand(new Body("Expected Body"));

        final String expectedBody = "Expected Body";

        editCommand.execute(model);

        assertEquals(expectedBody, model.getBlockTree().get(currentPath).getBlock().getBody().getText());


    }
}