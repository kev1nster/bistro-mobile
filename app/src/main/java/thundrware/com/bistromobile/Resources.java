package thundrware.com.bistromobile;

import android.support.annotation.IdRes;

public class Resources {

    public static String getString(int resId) {
        return MyApplication.getContext().getResources().getString(resId);
    }
}
