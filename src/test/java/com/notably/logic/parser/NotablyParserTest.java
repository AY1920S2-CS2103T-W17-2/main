package com.notably.logic.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.notably.logic.commands.Command;
import com.notably.logic.commands.DeleteCommand;
import com.notably.logic.commands.NewCommand;
import com.notably.logic.commands.OpenCommand;


public class NotablyParserTest {
    private final NotablyParser parser = new NotablyParser();

    @Test
    public void parseCommand_newCommandInput_newCommand() throws Exception {
        Command command = parser.parseCommand("new -t CS2103 -b Hi");

        assertTrue(command instanceof NewCommand);
    }

    @Test
    public void parseCommand_newCommandInputJump_false() throws Exception {
        NewCommand command = (NewCommand) parser.parseCommand("new -t CS2103 -b Hi");

        assertFalse(command.getJump());
    }

    @Test
    public void parseCommand_newCommandInputJump_true() throws Exception {
        NewCommand command = (NewCommand) parser.parseCommand("new -t CS2103 -b Hi -o");

        assertTrue(command.getJump());
    }

    @Test
    public void parseCommand_openCommandInput_openCommand() throws Exception {
        Command command = parser.parseCommand("open -t CS2103");

        assertTrue(command instanceof OpenCommand);
    }

    @Test
    public void parseCommand_deleteCommandInput_deleteCommand() throws Exception {
        Command command = parser.parseCommand("delete -t CS2103");

        assertTrue(command instanceof DeleteCommand);
    }


}
