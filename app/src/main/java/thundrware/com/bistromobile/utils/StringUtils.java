package thundrware.com.bistromobile.utils;

public class StringUtils {

    public static boolean isNullOrEmpty(String string) {
        if (string == null || string.isEmpty() || string.trim() == "") {
            return true;
        }
        return false;
    }
}
