package thundrware.com.bistromobile.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.ServerDetailsState;
import thundrware.com.bistromobile.ServerManager;
import thundrware.com.bistromobile.networking.ServerAddress;
import thundrware.com.bistromobile.utils.StringUtils;

public class ServerDetailsActivity extends AppCompatActivity {

    @BindView(R.id.portEditText) EditText mPortEditText;
    @BindView(R.id.ipAddressEditText) EditText mIpAddressEditText;
    @BindView(R.id.continueServerSettingsButton) Button mContinueButton;
    @BindView(R.id.serverDetailsHelpMessageTextView) TextView mHelpMessageTextView;

    private ServerManager mServerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_details);

        mServerManager = new ServerManager();

        Intent intent = getIntent();
        ServerDetailsState state = (ServerDetailsState) intent.getSerializableExtra(getString(R.string.server_details_state_flag));
        if (state == ServerDetailsState.Invalid) {
            mHelpMessageTextView.setText(getString(R.string.server_details_invalid));
        } else {
            mHelpMessageTextView.setText(getString(R.string.server_details_unset));
        }

    }

    @OnClick(R.id.continueServerSettingsButton)
    public void continueButtonClickListener() {

        String address = mIpAddressEditText.getText().toString();
        String port = mPortEditText.getText().toString();

        ServerAddress serverAddress = new ServerAddress();
        serverAddress.setAddress(address);
        serverAddress.setPort(port);

        mServerManager.setConnectionDetails(serverAddress);

        if (mServerManager.isAccessible()) {
            launchLoginActivity();
        } else {
            AlertMessage.showInvalidServerConnectionDetailsMessage(this);
            clearInputFields();
        }
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
}
