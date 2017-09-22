package thundrware.com.bistromobile;

import android.content.Context;

public class ActivationKeyManager {

    private ApplicationPreferences preferences;

    public ActivationKeyManager(Context context) {
        preferences = new ApplicationPreferences(context);
    }

    public void save(String token) {
        preferences.setActivationToken(token);
    }

    public String getToken()
    {
        return preferences.getActivationToken();
    }


}
