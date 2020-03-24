package com.notably.model;

import static com.notably.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.notably.commons.core.GuiSettings;
import com.notably.commons.core.LogsCenter;
import com.notably.commons.core.path.AbsolutePath;
import com.notably.model.block.Block;
import com.notably.model.block.BlockModel;
import com.notably.model.block.BlockTree;
import com.notably.model.block.Body;
import com.notably.model.suggestion.SuggestionItem;
import com.notably.model.suggestion.SuggestionModel;
import com.notably.model.viewstate.ViewStateModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Represents the in-memory model of the application data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private UserPrefs userPrefs;
    private SuggestionModel suggestionModel;
    private ViewStateModel viewStateModel;
    private BlockModel blockModel;

    /**
     * Initializes a ModelManager with the given model parts.
     */
    public ModelManager(BlockModel blockModel, SuggestionModel suggestionModel, ViewStateModel viewStateModel,
            ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(blockModel, suggestionModel, viewStateModel, userPrefs);

        this.suggestionModel = suggestionModel;
        this.viewStateModel = viewStateModel;
        this.blockModel = blockModel;
        this.userPrefs = new UserPrefs(userPrefs);
    }

    /**
     * Shorthand for initializing a {@link ModelManager} without {@link UserPrefs}.
     */
    public ModelManager(BlockModel blockModel, SuggestionModel suggestionModel, ViewStateModel viewStateModel) {
        this(blockModel, suggestionModel, viewStateModel, new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== Suggestion Model =============================================================

    @Override
    public ObservableList<SuggestionItem> getSuggestions() {
        return suggestionModel.getSuggestions();
    }

    @Override
    public void setSuggestions(List<SuggestionItem> suggestions) {
        suggestionModel.setSuggestions(suggestions);
    }

    @Override
    public Property<Optional<String>> responseTextProperty() {
        return suggestionModel.responseTextProperty();
    }

    @Override
    public void setResponseText(String responseText) {
        suggestionModel.setResponseText(responseText);
    }

    @Override
    public void clearResponseText() {
        suggestionModel.clearResponseText();
    }

    @Override
    public void clearSuggestions() {
        suggestionModel.clearSuggestions();
    }

    //=========== ViewStateModel ==================================================================

    @Override
    public StringProperty inputProperty() {
        return viewStateModel.inputProperty();
    }

    @Override
    public String getInput() {
        return viewStateModel.getInput();
    }

    @Override
    public void setInput(String input) {
        viewStateModel.setInput(input);
    }

    @Override
    public BooleanProperty helpOpenProperty() {
        return viewStateModel.helpOpenProperty();
    }

    @Override
    public Boolean isHelpOpen() {
        return viewStateModel.isHelpOpen();
    }

    @Override
    public void setHelpOpen(Boolean bool) {
        viewStateModel.setHelpOpen(bool);
    }

    //========= BlockModel========================================================================

    @Override
    public BlockTree getBlockTree() {
        return blockModel.getBlockTree();
    }

    @Override
    public AbsolutePath getCurrentlyOpenPath() {
        return blockModel.getCurrentlyOpenPath();
    }

    @Override
    public Property<AbsolutePath> currentlyOpenPathProperty() {
        return blockModel.currentlyOpenPathProperty();
    }

    @Override
    public boolean hasPath(AbsolutePath p) {
        return blockModel.hasPath(p);
    }

    @Override
    public void setCurrentlyOpenBlock(AbsolutePath p) {
        blockModel.setCurrentlyOpenBlock(p);
    }

    @Override
    public void addBlockToCurrentPath(Block b) {
        blockModel.addBlockToCurrentPath(b);
    }

    @Override
    public void removeBlock(AbsolutePath p) {
        blockModel.removeBlock(p);
    }

    @Override
    public void updateCurrentlyOpenBlockBody(Body newBody) {
        blockModel.updateCurrentlyOpenBlockBody(newBody);
    }
}
