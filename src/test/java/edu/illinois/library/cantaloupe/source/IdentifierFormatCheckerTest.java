package edu.illinois.library.cantaloupe.source;

import edu.illinois.library.cantaloupe.image.Format;
import edu.illinois.library.cantaloupe.image.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierFormatCheckerTest {

    @Test
    void testCheckWithKnownFormat() {
        var instance = new IdentifierFormatChecker(new Identifier("cats.jpg"));
        assertEquals(Format.JPG, instance.check());
    }

    @Test
    void testCheckWithUnknownFormat() {
        var instance = new IdentifierFormatChecker(new Identifier("cats"));
        assertEquals(Format.UNKNOWN, instance.check());
    }

}
