package com.notably.model.block;

import static com.notably.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Block's body in Notably.
 * Guarantees: immutable; is non-null
 */
public class Body {
    public static final String MESSAGE_CONSTRAINTS =
        "Body should not be blank";
    public final String blockBody;

    /**
     * Constructs a {@code Body}.
     *
     * @param body A valid body.
     */
    public Body(String body) {
        requireNonNull(body);
        checkArgument(isValidBody(body), MESSAGE_CONSTRAINTS);
        blockBody = body;
    }

    /**
     * Returns true if a given string is a valid body.
     *
     * @param test The body string to test.
     */
    public static boolean isValidBody(String test) {
        return !test.isBlank();
    }

    /**
     * Returns a String of the Body contents.
     *
     * @return Body text string.
     */
    public String getText() {
        return this.blockBody;
    }
}