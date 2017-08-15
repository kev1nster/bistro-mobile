package thundrware.com.bistromobile.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.listeners.DataProcessingListener;

public class DataLoadingActivity extends AppCompatActivity implements DataProcessingListener {

    @BindView(R.id.loadingProgressBar) ProgressBar mLoadingProgressBar;
    @BindView(R.id.loadingProgressTextView) TextView mLoadingProgressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_processing);
        ButterKnife.bind(this);
    }

    @Override
    public void onProcessFinished() {
        
    }

    @Override
    public void onProcessStatusUpdate(String message) {
        mLoadingProgressTextView.setText(message);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
