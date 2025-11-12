package com.upp.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Internationalization manager for AntiIdle application.
 * Handles loading and accessing localized messages.
 */
public class I18nManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(I18nManager.class);
    private static final String BUNDLE_BASE_NAME = "i18n.messages";
    
    private static I18nManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;
    
    private I18nManager() {
        // Load default locale from system or configuration
        this.currentLocale = Locale.getDefault();
        loadBundle();
    }
    
    /**
     * Gets the singleton instance of I18nManager.
     * 
     * @return the I18nManager instance
     */
    public static synchronized I18nManager getInstance() {
        if (instance == null) {
            instance = new I18nManager();
        }
        return instance;
    }
    
    /**
     * Loads the resource bundle for the current locale.
     */
    private void loadBundle() {
        try {
            bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, currentLocale);
            LOGGER.info("Loaded resource bundle for locale: {}", currentLocale);
        } catch (Exception e) {
            LOGGER.error("Failed to load resource bundle for locale: {}, falling back to English", currentLocale, e);
            bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, Locale.ENGLISH);
        }
    }
    
    /**
     * Gets a localized message for the given key.
     * 
     * @param key the message key
     * @return the localized message, or the key itself if not found
     */
    public String getMessage(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            LOGGER.warn("Message not found for key: {}", key);
            return key;
        }
    }
    
    /**
     * Gets a formatted localized message for the given key.
     * 
     * @param key the message key
     * @param args the format arguments
     * @return the formatted localized message
     */
    public String getMessage(String key, Object... args) {
        String message = getMessage(key);
        try {
            return MessageFormat.format(message, args);
        } catch (Exception e) {
            LOGGER.warn("Failed to format message for key: {}", key, e);
            return message;
        }
    }
    
    /**
     * Sets the current locale and reloads the resource bundle.
     * 
     * @param locale the new locale
     */
    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        loadBundle();
        LOGGER.info("Locale changed to: {}", locale);
    }
    
    /**
     * Sets the locale by language code.
     * 
     * @param languageCode the language code (e.g., "en", "de")
     */
    public void setLocale(String languageCode) {
        Locale locale = Locale.forLanguageTag(languageCode);
        setLocale(locale);
    }
    
    /**
     * Gets the current locale.
     * 
     * @return the current locale
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }
    
    /**
     * Gets all available locales for the application.
     * 
     * @return array of available locales
     */
    public Locale[] getAvailableLocales() {
        return new Locale[] {
            Locale.ENGLISH,
            Locale.GERMAN
        };
    }
}
