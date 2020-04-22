package edu.illinois.library.cantaloupe.source;

import edu.illinois.library.cantaloupe.config.Configuration;
import edu.illinois.library.cantaloupe.config.Key;
import edu.illinois.library.cantaloupe.http.Range;
import edu.illinois.library.cantaloupe.http.Response;
import edu.illinois.library.cantaloupe.test.BaseTest;
import edu.illinois.library.cantaloupe.test.S3Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class S3HTTPImageInputStreamClientTest extends BaseTest {

    private static final S3Server S3_SERVER = new S3Server();

    private S3HTTPImageInputStreamClient instance;

    @BeforeAll
    public static void beforeClass() throws Exception {
        BaseTest.beforeClass();
        S3_SERVER.start();
    }

    @AfterAll
    public static void afterClass() throws Exception {
        BaseTest.afterClass();
        S3_SERVER.stop();
    }

    private static void configureS3Source() {
        final Configuration config = Configuration.getInstance();
        config.setProperty(Key.S3SOURCE_ENDPOINT, S3_SERVER.getEndpoint());
        config.setProperty(Key.S3SOURCE_ACCESS_KEY_ID, S3Server.ACCESS_KEY_ID);
        config.setProperty(Key.S3SOURCE_SECRET_KEY, S3Server.SECRET_KEY);
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        configureS3Source();

        S3ObjectInfo info = new S3ObjectInfo("jpg", S3Server.FIXTURES_BUCKET_NAME);
        info.setLength(1584);

        instance = new S3HTTPImageInputStreamClient(info);
    }

    @Test
    void testSendHEADRequest() throws Exception {
        Response actual = instance.sendHEADRequest();
        assertEquals(200, actual.getStatus());
        assertEquals("bytes", actual.getHeaders().getFirstValue("Accept-Ranges"));
        assertEquals("1584", actual.getHeaders().getFirstValue("Content-Length"));
    }

    @Test
    void testSendGETRequest() throws Exception {
        Response actual = instance.sendGETRequest(new Range(10, 50, 1584));
        assertEquals(206, actual.getStatus());
        assertEquals(41, actual.getBody().length);
    }

}
