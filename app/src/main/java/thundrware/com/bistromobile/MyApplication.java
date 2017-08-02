package thundrware.com.bistromobile;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.realm.Realm;

public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Realm.init(this);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}
