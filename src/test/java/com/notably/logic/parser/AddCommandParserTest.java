package com.notably.logic.parser;

import static com.notably.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static com.notably.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static com.notably.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static com.notably.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static com.notably.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static com.notably.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static com.notably.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static com.notably.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static com.notably.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static com.notably.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static com.notably.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static com.notably.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static com.notably.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static com.notably.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static com.notably.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static com.notably.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static com.notably.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static com.notably.logic.parser.CommandParserTestUtil.assertParseFailure;
import static com.notably.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static com.notably.testutil.TypicalPersons.AMY;
import static com.notably.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import com.notably.logic.commands.AddCommand;
import com.notably.model.person.Person;
import com.notably.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format("Todo: field suggestion", AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }
}
