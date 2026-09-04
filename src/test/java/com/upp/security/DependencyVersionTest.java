package com.upp.security;

import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Guards the minimum dependency versions required to address known vulnerabilities.
 */
class DependencyVersionTest {

    @Test
    void usesPatchedJacksonDatabindVersion() {
        assertEquals("3.2.2", ObjectMapper.class.getPackage().getImplementationVersion());
    }

    @Test
    void usesPatchedLogbackVersion() {
        assertEquals("1.6.3", Logger.class.getPackage().getImplementationVersion());
    }
}
