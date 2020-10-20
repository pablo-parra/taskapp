package com.example.taskapp.common.config;

/**
 * Spring Profiles
 */
public class SpringProfiles {

    /**
     * This constant applies to all tests.
     */
    public static final String TEST = "test";

    /**
     * This constant applies to local environment (development)
     */
    public static final String LOCAL = "local";

    /**
     * This constant applies to production remote environment.
     */
    public static final String REMOTE = "remote";

    private SpringProfiles() {

    }
}
