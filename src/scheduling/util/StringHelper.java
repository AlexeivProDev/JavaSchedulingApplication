package scheduling.util;

/**
 * This class contains String related utility methods.
 *
 * @author Alexeiv Perez
 */
public class StringHelper {

    /**
     * Returns empty string in case the given input is null.
     *
     * @param text
     * @return
     */
    public static String getNotNullValue(String text) {
        if (text == null) {
            return "";
        } else {
            return text;
        }
    }

}
