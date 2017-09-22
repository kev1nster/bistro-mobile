package thundrware.com.bistromobile.ui;

import android.app.Activity;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thundrware.com.bistromobile.ActivationKeyManager;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.Message;
import thundrware.com.bistromobile.MyApplication;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.models.DeviceInfo;
import thundrware.com.bistromobile.models.KeyActivationDetails;
import thundrware.com.bistromobile.networking.KeyActivationService;
import thundrware.com.bistromobile.networking.KeyActivationServiceProvider;
import thundrware.com.bistromobile.utils.StringUtils;

public class KeyActivationActivity extends AppCompatActivity {

    @BindView(R.id.keyActivationActivityToolbar)
    Toolbar toolbar;

    @BindView(R.id.keyActivationEditText)
    EditText keyEditText;

    @BindView(R.id.keyActivationLayout)
    ConstraintLayout keyLayout;

    @BindView(R.id.keyActivationButton)
    Button keyActivationButton;

    @BindView(R.id.keyActivatedLayout)
    ConstraintLayout keyActivatedLayout;

    @BindView(R.id.keyNotActivatedLayout)
    ConstraintLayout keyNotActivatedLayout;

    @BindView(R.id.keyStatusProgressBar)
    ProgressBar keyStatusProgressBar;

    @BindView(R.id.tokenTextView)
    TextView tokenTextView;

    Activity activity;
    KeyActivationService keyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_activation);
        ButterKnife.bind(this);

        activity = this;
        keyService = KeyActivationServiceProvider.create();
        ActivationKeyManager keyManager = new ActivationKeyManager(this);



        /*
        Toolbar stuff
         */

        toolbar.setTitle("Activare cheie");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        keyEditText.setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        /*
        In order to connect to the server, the device must be connected to the internet first.
         */


        if (MyApplication.isNetworkAvailable()) {

             /*
                Checking whether a key is set or if it is working
             */
            if (!StringUtils.isNullOrEmpty(keyManager.getToken())) {
                keyService.checkKey(keyManager.getToken()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        runOnUiThread(() -> {
                            hideProgressBar();
                            if (response.isSuccessful()) {
                                showKeyActivatedLayout();
                                tokenTextView.setText(keyManager.getToken());
                            } else {
                                showKeyNotActivatedLayout();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            } else {
                showKeyNotActivatedLayout();
            }
        } else {
            Message.showError(this, "Dispozitivul nu este conectat la internet!");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @OnEditorAction(R.id.keyActivationEditText)
    public boolean onPortEditTextHandler(TextView textView, int keyId, KeyEvent keyEvent) {
        if (keyId == EditorInfo.IME_ACTION_DONE) {
            onKeyActivationButtonClicked();
        }
        return false;
    }

    @OnClick(R.id.keyActivationButton)
    public void onKeyActivationButtonClicked() {

        String editTextInput = keyEditText.getText().toString();
        int waiterId = new WaiterManager().getCurrentWaiter().getId();

        DeviceInfo info = new DeviceInfo();
        info.setBrand(Build.BRAND);
        info.setHardware(Build.HARDWARE);
        info.setManufacturer(Build.MANUFACTURER);
        info.setModel(Build.MODEL);

        KeyActivationDetails details = new KeyActivationDetails();
        ActivationKeyManager keyManager = new ActivationKeyManager(this);

        details.setToken(editTextInput);
        details.setDeviceInfo(info);
        details.setWaiterId(waiterId);

        if (MyApplication.isNetworkAvailable()) {
            switchButtonIntoLoadingMode();
            keyService.activateKey(details).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            Snackbar.make(findViewById(android.R.id.content),
                                "Cheie activată cu succes!",
                                Snackbar.LENGTH_SHORT)
                                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        activity.finish();
                                    }
                                }).show();

                            keyManager.save(details.getToken());
                        });
                    } else {
                        runOnUiThread(() -> {
                            switch (response.code()) {
                                case 400:
                                    Message.showError(activity, "Cheia a fost activată deja de alt dispozitiv");
                                    break;

                                case 404:
                                    Message.showError(activity, "Token-ul introdus nu este asociat cu o cheie.");
                                    break;

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    runOnUiThread(() -> Message.showError(activity, "A eșuat comunicarea cu serviciul de activare"));
                }
            });
        } else {
            Message.showError(activity, "Dispozitivul nu este conectat la internet.");
        }
    }

    private void switchButtonIntoLoadingMode() {
        keyActivationButton.setText("SE TRIMITE...");
        keyActivationButton.setEnabled(false);
    }

    private void showKeyActivatedLayout() {
        hideProgressBar();
        keyActivatedLayout.setVisibility(View.VISIBLE);
    }

    private void showKeyNotActivatedLayout() {
        hideProgressBar();
        keyNotActivatedLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        keyStatusProgressBar.setVisibility(View.GONE);
    }
}
