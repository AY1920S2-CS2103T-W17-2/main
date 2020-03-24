package com.notably.logic;

import java.nio.file.Path;
import java.util.logging.Logger;

import com.notably.commons.core.GuiSettings;
import com.notably.commons.core.LogsCenter;
import com.notably.logic.commands.exceptions.CommandException;
import com.notably.logic.parser.NotablyParser;
import com.notably.logic.parser.exceptions.ParseException;
import com.notably.model.Model;
import com.notably.storage.Storage;


/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final NotablyParser notablyParser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        notablyParser = new NotablyParser();
    }

    @Override
    public void execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        // TODO: Save data to storage
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
