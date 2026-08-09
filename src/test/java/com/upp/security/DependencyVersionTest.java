package com.upp.security;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Guards the minimum dependency versions required to address known vulnerabilities.
 */
class DependencyVersionTest {

    @Test
    void usesPatchedJacksonDatabindVersion() {
        assertEquals("2.18.9", ObjectMapper.class.getPackage().getImplementationVersion());
    }

    @Test
    void usesPatchedLogbackVersion() {
        assertEquals("1.5.36", Logger.class.getPackage().getImplementationVersion());
    }
}
