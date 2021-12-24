package scheduling.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class contains Locale related utility methods.
 *
 * @author Alexeiv Perez
 */
public class LocaleHelper {

    /**
     * The properties file containing locale text key and values.
     */
    private final static String RESOURCE_BUNDLE = "scheduling.locale.langText";

    /**
     * The current Locale.
     */
    private Locale locale = null;

    /**
     * Constructor.
     *
     * @param locale
     */
    public LocaleHelper(Locale locale) {
        this.locale = locale;
    }

    /**
     * Returns the localized language text for the given key.
     *
     * @param key
     * @return
     */
    public String getLocalizedText(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE,
                locale, this.getClass().getClassLoader());
        String value = bundle.getString(key);
        if (value == null) {
            return key;
        } else {
            return value;
        }
    }
}
