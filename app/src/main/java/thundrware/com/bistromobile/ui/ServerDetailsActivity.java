package thundrware.com.bistromobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.ServerDetailsState;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;
import thundrware.com.bistromobile.adapters.ActiveTablesAdapter;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;
import thundrware.com.bistromobile.networking.ServerAddress;

public class ServerDetailsActivity extends AppCompatActivity {

    @BindView(R.id.portEditText) EditText mPortEditText;
    @BindView(R.id.ipAddressEditText) EditText mIpAddressEditText;
    @BindView(R.id.continueServerSettingsButton) Button mContinueButton;

    private ServerConnectionDetailsManager mServerConnectionDetailsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_details);
        ButterKnife.bind(this);

        mServerConnectionDetailsManager = new ServerConnectionDetailsManager();
        fixHelpTextViewMessage();
    }

    @OnClick(R.id.continueServerSettingsButton)
    public void continueButtonClickListener() {

        String address = mIpAddressEditText.getText().toString();
        String port = mPortEditText.getText().toString();

        final Activity activity = this;
        final ServerAddress serverAddress = new ServerAddress();
        serverAddress.setAddress(address);
        serverAddress.setPort(port);

        DataService dataService = DataServiceProvider.create(serverAddress.toString());

        mServerConnectionDetailsManager.tryConnection(dataService, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                // succesfully connected to the server, persist details
                mServerConnectionDetailsManager.setConnectionDetails(serverAddress);

                // open the login activity
                runOnUiThread(() -> launchLoginActivity());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    AlertMessage.showInvalidServerConnectionDetailsMessage(activity);
                    clearInputFields();
                });

            }
        });
    }

    @OnEditorAction(R.id.portEditText)
    public boolean onPortEditTextHandler(TextView textView, int keyId, KeyEvent keyEvent) {
        if (keyId == EditorInfo.IME_ACTION_DONE) {
            continueButtonClickListener();
        }
        return false;
    }

    private void clearInputFields() {
        mIpAddressEditText.setText("");
        mPortEditText.setText("");
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void fixHelpTextViewMessage() {
        Intent intent = getIntent();
       /* ServerDetailsState state = (ServerDetailsState) intent.getSerializableExtra(getString(R.string.server_details_state_flag));
        if (state == ServerDetailsState.Invalid) {
            mHelpMessageTextView.setText(getString(R.string.server_details_invalid));
        } else {
            mHelpMessageTextView.setText(getString(R.string.server_details_unset));
        } */
    }

}
