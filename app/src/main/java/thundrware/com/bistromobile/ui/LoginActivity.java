package thundrware.com.bistromobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.github.florent37.viewanimator.ViewAnimator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.Message;
import thundrware.com.bistromobile.MyApplication;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.listeners.OnLoginHandler;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.passwordEditText)
    EditText mPasswordEditText;

    @BindView(R.id.loginActivityTextView)
    TextView mLoginTextView;

    @BindView(R.id.loginActivityLayout)
    ConstraintLayout mLoginConstraintLayout;

    @BindView(R.id.loginButton)
    Button loginButton;

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mActivity = this;
    }

    @OnEditorAction(R.id.passwordEditText)
    public boolean onEditorActionHandler(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            onLoginButtonClickedHandler();
        }
        return false;
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClickedHandler() {
        WaiterManager waiterManager = new WaiterManager();

        disableLoginButton();

        String passwordEditTextInput = mPasswordEditText.getText().toString();

        if (MyApplication.isNetworkAvailable()) {
            waiterManager.logWaiterIn(passwordEditTextInput, new OnLoginHandler() {
                @Override
                public void onError(Throwable e) {
                    runOnUiThread(() -> {
                        Message.showError(mActivity, "Parola introdusă nu corespunde niciunui ospătar. Reîncercați.");
                        enableLoginButton();
                    });
                }

                @Override
                public void onSuccess() {
                    runOnUiThread(() -> launchDataLoadingActivity());
                }
            });
        } else {
            Message.showError(this, "Dispozitivul nu este conectat la internet.");
        }
    }

    private void disableLoginButton() {
        loginButton.setEnabled(false);
        loginButton.setText("SE ÎNCARCĂ...");
    }

    private void enableLoginButton() {
        loginButton.setEnabled(true);
        loginButton.setText("LOGIN");
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void launchDataLoadingActivity() {
        Intent intent = new Intent(this, DataLoadingActivity.class);
        startActivity(intent);
        finish();
    }
}
