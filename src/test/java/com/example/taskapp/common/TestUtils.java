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
     * Creates a success message for the test
     */
    public static void testSuccess() {

        log.info("------------------------------------------------------------------------------");
        log.info("------------------------------------------------------------------ TEST PASSED");
        log.info("------------------------------------------------------------------------------\n\n\n");
    }

    /**
     * Base Url
     */
    public static String BASE_URL = "/api/v1/tasks";

    /**
     * The Constructor
     */
    private TestUtils() {

        throw new IllegalStateException("Utility class");
    }
}
