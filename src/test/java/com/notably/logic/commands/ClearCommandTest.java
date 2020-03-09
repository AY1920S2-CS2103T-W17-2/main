package com.notably.logic.commands;

import static com.notably.logic.commands.CommandTestUtil.assertCommandSuccess;
import static com.notably.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import com.notably.model.AddressBook;
import com.notably.model.Model;
import com.notably.model.ModelManager;
import com.notably.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
