package thundrware.com.bistromobile.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;
import thundrware.com.bistromobile.data.DataLoader;
import thundrware.com.bistromobile.listeners.DataProcessingListener;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;

public class DataLoadingActivity extends AppCompatActivity implements DataProcessingListener {

    @BindView(R.id.loadingProgressTextView) TextView mLoadingProgressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_processing);
        ButterKnife.bind(this);

        ServerConnectionDetailsManager serverConnectionDetailsManager = new ServerConnectionDetailsManager();

        DataService dataService = DataServiceProvider.create(serverConnectionDetailsManager.getConnectionDetails().toString());
        DataLoader dataLoader = new DataLoader(dataService, this);

        dataLoader.loadAreas()
                .loadCategories()
                .loadGroups()
                .loadProducts();
    }

    @Override
    public void onProcessFinished() {
        launchMainActivity();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onProcessStatusUpdate(String message) {
        mLoadingProgressTextView.setText(message);
    }

    @Override
    public void onBackPressed() {

    }
}
