package com.example.taskapp.common;

import lombok.extern.slf4j.Slf4j;

/**
 * Test Utilities class
 */
@Slf4j
public class TestUtils {

    /**
     * Creates a header of the test in the Logs
     *
     * @param name the name of the test
     */
    public static void testHeader(String name) {

        log.info("------------------------------------------------------------------------------");
        log.info("--- TEST: {}", name);
        log.info("------------------------------------------------------------------------------");
    }

    /**
     * Creates a message for the test end
     */
    public static void testEnd() {

        log.info("------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------- TEST FINISHED");
        log.info("------------------------------------------------------------------------------\n\n\n");
    }

    /**
     * Base Url
     */
    public static String BASE_URL = "/api/v1/tasks";

    /**
     * Authenticate Url
     */
    public static String AUTHENTICATE_URL = "/api/authenticate";

    /**
     * The Constructor
     */
    private TestUtils() {

        throw new IllegalStateException("Utility class");
    }
}
