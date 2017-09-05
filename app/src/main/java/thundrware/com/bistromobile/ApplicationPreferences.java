package thundrware.com.bistromobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ApplicationPreferences {

    private SharedPreferences mSharedPreferences;

    private final String WAITER_ID = "WAITER_ID";
    private final String WAITER_NAME = "WAITER_NAME";
    private final String WAITER_PASSWORD = "WAITER_PASSWORD";
    private final String SERVER_IP = "SERVER_IP";
    private final String SERVER_PORT = "SERVER_PORT";
    private final String ORDER_ADD_INFO_SHOWN = "ORDER_ADD_INFO_SHOWN";

    public ApplicationPreferences(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setOrderInfoShown(boolean isSet) {
        mSharedPreferences.edit()
                .putBoolean(ORDER_ADD_INFO_SHOWN, isSet)
                .apply();
    }

    public boolean wasOrderInfoShown() {
        return mSharedPreferences.getBoolean(ORDER_ADD_INFO_SHOWN, false);
    }

    public int getWaiterId() {
        return mSharedPreferences.getInt(WAITER_ID, WaiterManager.defaultWaiterId);
    }

    public void setWaiterId(int waiterId) {
        mSharedPreferences.edit()
                .putInt(WAITER_ID, waiterId)
                .apply();
    }

    public void clearWaiterId() {
        mSharedPreferences.edit()
                .remove(WAITER_ID)
                .apply();
    }


    public String getWaiterName() {
        return mSharedPreferences.getString(WAITER_NAME, "Ospătar");
    }

    public void setWaiterName(String waiterName) {
        mSharedPreferences.edit()
                .putString(WAITER_NAME, waiterName)
                .apply();
    }

    public void clearWaiterName() {
        mSharedPreferences.edit()
                .remove(WAITER_NAME)
                .apply();
    }

    public String getWaiterPassword() {
        return mSharedPreferences.getString(WAITER_PASSWORD, null);
    }

    public void setWaiterPassword(String waiterPassword) {
        mSharedPreferences.edit()
                .putString(WAITER_PASSWORD, waiterPassword)
                .apply();
    }

    public void clearWaiterPassword() {
        mSharedPreferences.edit()
                .remove(WAITER_PASSWORD)
                .apply();
    }

    public String getServerIp() {
        return mSharedPreferences.getString(SERVER_IP, null);
    }

    public void setServerIp(String serverIp) {
        mSharedPreferences.edit()
                .putString(SERVER_IP, serverIp)
                .apply();
    }

    public String getServerPort() {
        return mSharedPreferences.getString(SERVER_PORT, null);
    }

    public void setServerPort(String serverPort) {
        mSharedPreferences.edit()
                .putString(SERVER_PORT, serverPort)
                .apply();
    }
}
