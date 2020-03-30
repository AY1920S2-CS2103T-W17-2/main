package com.notably.logic.commands;

import static java.util.Objects.requireNonNull;

import com.notably.commons.path.AbsolutePath;
import com.notably.commons.path.Path;
import com.notably.commons.path.RelativePath;
import com.notably.model.Model;

/**
 * Represents a command that deletes a block.
 */
public class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_SHORTHAND = "d";
    private Path targetPath;

    public DeleteCommand(Path targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public void execute(Model notablyModel) {
        requireNonNull(notablyModel);
        if (this.targetPath instanceof RelativePath) {
            RelativePath toConvert = (RelativePath) this.targetPath;
            this.targetPath = toConvert.toAbsolutePath(notablyModel.getCurrentlyOpenPath());
        }
        notablyModel.removeBlock((AbsolutePath) this.targetPath);
    }



}
