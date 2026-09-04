package com.upp.i18n;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link I18nManager}.
 *
 * <p>{@link I18nManager} is a singleton, so every test resets the locale back to English
 * before and after running to avoid leaking state into other tests that also touch it
 * (e.g. GUI classes calling {@code I18nManager.getInstance()}).</p>
 */
class I18nManagerTest {

    private I18nManager manager;

    @BeforeEach
    void setUp() {
        manager = I18nManager.getInstance();
        manager.setLocale(Locale.ENGLISH);
    }

    @AfterEach
    void tearDown() {
        manager.setLocale(Locale.ENGLISH);
    }

    @Test
    void testGetInstanceReturnsSingleton() {
        I18nManager other = I18nManager.getInstance();

        assertSame(manager, other);
    }

    @Test
    void testGetMessageReturnsLocalizedValue() {
        assertEquals("Settings", manager.getMessage("menu.settings"));
    }

    @Test
    void testGetMessageWithUnknownKeyReturnsKeyItself() {
        String key = "no.such.key.exists";

        assertEquals(key, manager.getMessage(key));
    }

    @Test
    void testGetMessageWithArgsFormatsPlaceholders() {
        String formatted = manager.getMessage("dialog.init.failed", "boom");

        assertTrue(formatted.contains("boom"));
    }

    @Test
    void testGetMessageWithArgsOnMessageWithoutPlaceholdersReturnsOriginal() {
        String result = manager.getMessage("menu.settings", "unused");

        assertEquals("Settings", result);
    }

    @Test
    void testSetLocaleWithLocaleObjectSwitchesBundle() {
        manager.setLocale(Locale.GERMAN);

        assertEquals(Locale.GERMAN, manager.getCurrentLocale());
        assertEquals("Einstellungen", manager.getMessage("menu.settings"));
    }

    @Test
    void testSetLocaleWithLanguageCodeSwitchesBundle() {
        manager.setLocale("de");

        assertEquals("de", manager.getCurrentLocale().getLanguage());
        assertEquals("Einstellungen", manager.getMessage("menu.settings"));
    }

    @Test
    void testSetLocaleWithUnsupportedLocaleFallsBackToEnglish() {
        // ResourceBundle.getBundle() also falls back to the JVM default locale before giving
        // up, so force the default locale to something unsupported too, guaranteeing that no
        // bundle can be resolved and loadBundle() must catch the MissingResourceException and
        // fall back to English explicitly.
        Locale originalDefault = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("xx"));
            manager.setLocale(Locale.forLanguageTag("xx"));

            assertEquals("Settings", manager.getMessage("menu.settings"));
        } finally {
            Locale.setDefault(originalDefault);
        }
    }

    @Test
    void testGetAvailableLocalesContainsEnglishAndGerman() {
        Locale[] locales = manager.getAvailableLocales();

        assertNotNull(locales);
        assertEquals(2, locales.length);
        assertEquals(Locale.ENGLISH, locales[0]);
        assertEquals(Locale.GERMAN, locales[1]);
    }
}
