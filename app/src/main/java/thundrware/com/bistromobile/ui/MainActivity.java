package thundrware.com.bistromobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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

        mToolbar.setTitle(Resources.getString(R.string.active_tables_toolbar_title));
        setSupportActionBar(mToolbar);

        /*
            ActiveTablesRecyclerView setup
         */

        mActiveTablesRetrievingService.getActive(mWaiterManager.getCurrentWaiter().getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                List<ActiveTable> activeTablesList = new ArrayList<>();
                try {
                    String jsonData = response.body().string();
                    activeTablesList = ActiveTablesConverter.from(jsonData).convert();
                } catch (Exception ex) {
                    runOnUiThread(() -> AlertMessage.showMessage(activity, "Eroare", "ResponseBody could not be converted"));
                }

                presenter.activeTablesDataReceived(activeTablesList);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> AlertMessage.showMessage(activity, "Eroare", t.getMessage()));
            }
        });


        /*
            Checking whether the user has seen the guideline or not
         */

        ApplicationPreferences preferences = new ApplicationPreferences(this);

        if (!preferences.wasOrderInfoShown()) {
            TapTargetView.showFor(this, TapTarget.forView(mRecyclerView, "Lista cu comenzi active", "Apasă pe oricare dintre ele pentru a deschide o comandă activă")
                    .transparentTarget(true),
                    new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            onFloatingActionButtonClicked();
                        }
                    });
        }




    }

    @OnClick(R.id.floatingActionButton)
    public void onFloatingActionButtonClicked() {
        presenter.launchActivityButtonPressed(TablePickingActivity.class);
    }

    @Override
    public void onBackPressed() {
        return;
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
            mRecyclerView.addItemDecoration(dividerItemDecoration);
        });
    }
}
