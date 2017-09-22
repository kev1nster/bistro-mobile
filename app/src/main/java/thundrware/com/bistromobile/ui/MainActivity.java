package thundrware.com.bistromobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thundrware.com.bistromobile.ActiveTablesConverter;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.ApplicationPreferences;
import thundrware.com.bistromobile.Message;
import thundrware.com.bistromobile.MyApplication;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.Resources;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.adapters.ActiveTablesAdapter;
import thundrware.com.bistromobile.models.ActiveTable;
import thundrware.com.bistromobile.models.Table;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;
import thundrware.com.bistromobile.ui.presenters.MainActivityPresenter;
import thundrware.com.bistromobile.ui.views.MainActivityView;

public class MainActivity extends AppCompatActivity implements MainActivityView{

    @BindView(R.id.activeTablesRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.mainActivityToolbar)
    Toolbar mToolbar;

    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private DataService mActiveTablesRetrievingService;
    private WaiterManager mWaiterManager;
    private MainActivityPresenter presenter;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        activity = this;
        presenter = new MainActivityPresenter(this);

        mWaiterManager = new WaiterManager();
        ServerConnectionDetailsManager serverConnectionDetailsManager = new ServerConnectionDetailsManager();
        String ipAddress = serverConnectionDetailsManager.getConnectionDetails().toString();
        mActiveTablesRetrievingService = DataServiceProvider.create(ipAddress);

        /*
            Toolbar setup
         */

        mToolbar.setTitle(mWaiterManager.getCurrentWaiter().getName());
        setSupportActionBar(mToolbar);

        /*
            ActiveTablesRecyclerView setup
         */

        fetchActiveTablesData();

        mRefreshLayout.setOnRefreshListener(() -> fetchActiveTablesData());


        /*
            Checking whether the user has seen the guideline or not
         */

        ApplicationPreferences preferences = new ApplicationPreferences(this);

        if (!preferences.wasOrderInfoShown()) {
            TapTargetView.showFor(this, TapTarget.forView(mFloatingActionButton, "Comandă nouă", "Apasă acest buton pentru a crea o comandă nouă")
                    .transparentTarget(true),
                    new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            preferences.setOrderInfoShown();
                        }
                    });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchActiveTablesData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            onToolbarSettingsButtonClicked();
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchActiveTablesData() {
        if (MyApplication.isNetworkAvailable()) {
            mActiveTablesRetrievingService.getActive(mWaiterManager.getCurrentWaiter().getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    List<ActiveTable> activeTablesList = new ArrayList<>();
                    try {
                        String jsonData = response.body().string();
                        activeTablesList = ActiveTablesConverter.from(jsonData).convert();
                    } catch (Exception ex) {
                        runOnUiThread(() -> Message.showError(activity, "A apărut o eroare la convertirea meselor."));
                    }

                    presenter.activeTablesDataReceived(activeTablesList);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    runOnUiThread(() -> Message.showError(activity, t.getMessage()));
                }
            });
        } else {
            Message.showError(activity, "Dispozitivul nu este conectat la internet.");
        }
    }

    public void onToolbarSettingsButtonClicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.floatingActionButton)
    public void onFloatingActionButtonClicked() {
        presenter.launchActivityButtonPressed(TablePickingActivity.class);
    }

    @Override
    public void onBackPressed() {
        final Activity activityToFinish = this;

        new MaterialDialog.Builder(this)
                .title("Ieșire")
                .content("Doriți să închideți aplicația?")
                .positiveText("DA")
                .negativeText("NU")
                .onPositive((dialog, which) -> {
                    activityToFinish.finishAndRemoveTask();
                }).show();
    }


    @Override
    public void launchOtherActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void fillRecyclerView(List<ActiveTable> activeTableList) {
        ActiveTablesAdapter adapter = new ActiveTablesAdapter(this, activeTableList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());

        runOnUiThread(() -> {
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(layoutManager);
            if (!mRefreshLayout.isRefreshing()) {
                mRecyclerView.addItemDecoration(dividerItemDecoration);
            }
            mRefreshLayout.setRefreshing(false);
        });
    }
}
