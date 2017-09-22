package thundrware.com.bistromobile.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import thundrware.com.bistromobile.Message;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.data.DataLoader;
import thundrware.com.bistromobile.listeners.DataLoadingListener;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;

public class DataLoadingActivity extends AppCompatActivity implements DataLoadingListener {

    @BindView(R.id.loadingProgressTextView)
    TextView mLoadingProgressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_processing);
        ButterKnife.bind(this);

        DataService dataService = DataServiceProvider.getDefault();
        DataLoader dataLoader = new DataLoader(dataService, this);

        dataLoader.load();
    }


    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onLoadingFinished() {

        // The data has finished being loaded, therefore we need to launch the MainActivity activity;
        runOnUiThread(() -> launchMainActivity());
    }

    @Override
    public void onLoadingProgress(String message) {
        runOnUiThread(() -> mLoadingProgressTextView.setText(message));
    }

    @Override
    public void onError(Throwable e) {
        Message.showError(this, e.getMessage());
        // TODO let the user click on something to reset the process?
    }

    @Override
    public void onBackPressed() {

    }
}
