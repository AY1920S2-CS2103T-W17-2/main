package com.notably.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.exceptions.CommandException;
import com.notably.model.Model;
import com.notably.model.ModelManager;
import com.notably.model.block.Block;
import com.notably.model.block.BlockImpl;
import com.notably.model.block.BlockModel;
import com.notably.model.block.BlockModelImpl;
import com.notably.model.block.Title;
import com.notably.model.suggestion.SuggestionModel;
import com.notably.model.suggestion.SuggestionModelImpl;
import com.notably.model.viewstate.ViewStateModel;
import com.notably.model.viewstate.ViewStateModelImpl;

class DeleteCommandTest {
    private static Model model;
    private static AbsolutePath toDeletePath;

    @BeforeEach
    public void setUp() {
        // Set up model
        BlockModel blockModel = new BlockModelImpl();
        SuggestionModel suggestionModel = new SuggestionModelImpl();
        ViewStateModel viewStateModel = new ViewStateModelImpl();
        model = new ModelManager(blockModel, suggestionModel, viewStateModel);

        // Populate model with test data
        Block currentBlock = (new BlockImpl(new Title("CS2103")));
        model.addBlockToCurrentPath(currentBlock);
        toDeletePath = AbsolutePath.fromString("/CS2103");
    }
    @Test
    void execute_validAbsolutePath_blockSuccessfullyDeleted() throws CommandException {
        final DeleteCommand deleteCommand = new DeleteCommand(toDeletePath);

        deleteCommand.execute(model);

        assertFalse(model.hasPath(toDeletePath));

    }

    @Test
    void execute_deleteRoot_throwsCommandException() {
        final AbsolutePath rootPath = AbsolutePath.fromString("/");
        final DeleteCommand deleteCommand = new DeleteCommand(rootPath);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    @Test
    void execute_deleteNoneExistingBlock_throwsCommandException() {
        final AbsolutePath rootPath = AbsolutePath.fromString("/NonExisting");
        final DeleteCommand deleteCommand = new DeleteCommand(rootPath);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }
}
