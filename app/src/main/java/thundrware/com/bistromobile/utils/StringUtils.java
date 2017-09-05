package thundrware.com.bistromobile.utils;

public class StringUtils {

    public static boolean isNullOrEmpty(String string) {
        if (string == null || string.isEmpty() || string.trim() == "") {
            return true;
        }
        return false;
    }

    public static String uppercaseFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
