package thundrware.com.bistromobile;

import android.graphics.Color;
import android.support.annotation.IdRes;

public class Resources {

    public static String getString(int resId) {
        return MyApplication.getContext().getResources().getString(resId);
    }

    public static int getColor(int resId) {
        return MyApplication.getContext().getResources().getColor(resId);
    }
}
