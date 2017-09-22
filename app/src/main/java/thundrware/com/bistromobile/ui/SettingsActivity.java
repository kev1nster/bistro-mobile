package thundrware.com.bistromobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.data.DataManager;

import static android.R.attr.settingsActivity;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.settingsActivityToolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        toolbar.setTitle("Setări");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.preferenceScreen, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void restartApplication() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);

            WaiterManager waiterManager = new WaiterManager();
            DataManager dataManager = new DataManager();


            SettingsActivity settingsActivity = (SettingsActivity)getActivity();

            Preference disconnectPreference = getPreferenceScreen().findPreference("logout");
            disconnectPreference.setOnPreferenceClickListener(preference -> {
                waiterManager.signWaiterOut();
                settingsActivity.restartApplication();
                return true;
            });

            Preference resetDataPreference = getPreferenceScreen().findPreference("data_reload");
            resetDataPreference.setOnPreferenceClickListener(preference -> {
                dataManager.emptyDatabase();
                settingsActivity.restartApplication();
                return true;
            });

        }
    }
}
