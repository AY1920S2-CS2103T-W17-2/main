package com.notably.logic.commands;

import static java.util.Objects.requireNonNull;

import com.notably.model.Model;

/**
 * Represent a command that enable/disables the help window.
 */
public class HelpCommand extends Command {
    public static final String COMMAND_WORD = "help";
    public static final String COMMAND_SHORTHAND = "h";

    @Override
    public void execute(Model notablyModel) {
        requireNonNull(notablyModel);
        notablyModel.setHelpOpen(true);
    }
}
