package thundrware.com.bistromobile.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.ServerDetailsState;
import thundrware.com.bistromobile.data.DataManager;
import thundrware.com.bistromobile.MyApplication;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.ServerManager;
import thundrware.com.bistromobile.WaiterManager;

public class SplashScreenActivity extends AppCompatActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;

        ServerManager serverManager = new ServerManager();
        WaiterManager waiterManager = new WaiterManager();
        DataManager dataManager = new DataManager();

        if (!MyApplication.isNetworkAvailable()) {

            AlertMessage.showNoInternetConnectionMessage(this);

            } else {


                if (serverManager.detailsAreSet()) {
                    // details about server connection are set, go ahead into the next phase
                    if (serverManager.isAccessible()) {
                        // details are correct, go to the next phase
                        if (waiterManager.isAnyWaiterLoggedIn()) {
                            if (!dataManager.isEmpty())
                                launchMainActivity();
                            else
                                launchDataLoadingActivity();
                        } else {
                            launchLoginActivity();
                        }

                    } else {
                        // server details are set, but it cant connect to server
                        launchServerDetailsActivity(ServerDetailsState.Invalid);
                    }

                } else {
                    // server details are not set
                    launchServerDetailsActivity(ServerDetailsState.Unset);
                }


        }

    }

    private void launchLoginActivity() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchDataLoadingActivity() {
        Intent intent = new Intent(mContext, DataLoadingActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchServerDetailsActivity(ServerDetailsState serverDetailsState) {
        Intent intent = new Intent(mContext, ServerDetailsActivity.class);
        intent.putExtra(getString(R.string.server_details_state_flag), serverDetailsState);
        startActivity(intent);
        finish();
    }


}
