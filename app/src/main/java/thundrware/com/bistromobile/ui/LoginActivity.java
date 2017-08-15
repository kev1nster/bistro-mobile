package thundrware.com.bistromobile.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.InvalidWaiterPasswordException;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.WaiterManager;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.passwordEditText) EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClickedHandler() {
        WaiterManager waiterManager = new WaiterManager();

        String passwordEditTextInput = mPasswordEditText.getText().toString();
        try {
            waiterManager.logWaiterIn(passwordEditTextInput);
        } catch (InvalidWaiterPasswordException e) {
            AlertMessage.showInvalidWaiterPasswordMessage(this);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
