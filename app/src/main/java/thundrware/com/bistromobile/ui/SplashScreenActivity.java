package thundrware.com.bistromobile.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.ServerDetailsState;
import thundrware.com.bistromobile.data.DataManager;
import thundrware.com.bistromobile.MyApplication;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.listeners.WaiterQueriedListener;
import thundrware.com.bistromobile.models.Waiter;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;

public class SplashScreenActivity extends AppCompatActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;

        ServerConnectionDetailsManager serverConnectionDetailsManager = new ServerConnectionDetailsManager();
        final WaiterManager waiterManager = new WaiterManager();
        final DataManager dataManager = new DataManager();

        if (!MyApplication.isNetworkAvailable()) {

            AlertMessage.showNoInternetConnectionMessage(this);

            } else {

            DataService connectionCheckerDataService = DataServiceProvider.create(serverConnectionDetailsManager.getConnectionDetails().toString());

            if (serverConnectionDetailsManager.detailsAreSet()) {

                // it means that the server details are set, check whtether they are correct

                serverConnectionDetailsManager.tryConnection(connectionCheckerDataService, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // onResponse means that the IP input by the user is correct and therefore could connect to the server
                        if (response.isSuccessful()) {
                            waiterManager.startWaiterAvailabilityChecker(new WaiterQueriedListener() {

                                @Override
                                public void onWaiterQueried(Waiter waiter) {
                                    // A waiter was found, check whether there's any data in the database or not
                                    if (!dataManager.isEmpty()) {
                                        // Database is loaded already, go to MainActivity
                                        runOnUiThread(() -> launchMainActivity());
                                    } else {
                                        // Database is empty, it means we have to load it with data
                                        runOnUiThread(() -> launchDataLoadingActivity());
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // No waiter was found, the user needs to login
                                    runOnUiThread(() -> launchLoginActivity());
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // server details are set, but it cant connect to server
                        runOnUiThread(() -> launchServerDetailsActivity(ServerDetailsState.Invalid));
                    }
                });


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
