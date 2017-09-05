package thundrware.com.bistromobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.OrderDetailsEditor;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.data.repositories.AreasRepository;
import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.Table;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;

public class TablePickingActivity extends AppCompatActivity {

    @BindView(R.id.areaPickingSpinner)
    MaterialSpinner mAreaPickingSpinner;

    @BindView(R.id.tablePickingSpinner)
    MaterialSpinner mTablePickingSpinner;

    private Activity mActivity;
    private DataService mDataService;
    private AreasRepository mAreasRepository;
    private WaiterManager mWaiterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_picking);
        ButterKnife.bind(this);
        mActivity = this;

        ServerConnectionDetailsManager serverConnectionDetailsManager = new ServerConnectionDetailsManager();
        mDataService = DataServiceProvider.create(serverConnectionDetailsManager.getConnectionDetails().toString());
        mWaiterManager = new WaiterManager();

        /*
            Areas spinner set up
        */

        mAreasRepository = new AreasRepository();
        List<String> areasNames = new ArrayList<>();

        List<Area> areas = mAreasRepository.get();

        for (Area area : areas) {
            areasNames.add(area.getName());
        }

        mAreaPickingSpinner.setItems(areasNames);
        mAreaPickingSpinner.setOnItemSelectedListener((view, position, id, item) -> loadTablesSpinnerWithTablesFromArea(areas.get(position).getId()));

        loadTablesSpinnerWithTablesFromArea(areas.get(0).getId());
    }

    private void loadTablesSpinnerWithTablesFromArea(int areaId) {
        mDataService.getAvailableTablesFor(areaId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Integer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Integer> integers) {
                        runOnUiThread(() -> {
                            if (integers.size() > 0) {
                                mTablePickingSpinner.setItems(integers);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        AlertMessage.showMessage(mActivity, "Eroare", e.getMessage());
                    }
                });
    }

    @OnClick(R.id.createTableButton)
    public void onCreateTableButtonClickListener() {

        // Returns a raw list of areas, then gets them by their order, not by their Id, as they are actually processed in the onCreate() method.
        WaiterManager waiterManager = new WaiterManager();

        int waiterId = waiterManager.getCurrentWaiter().getId();
        int selectedAreaId = mAreasRepository.get().get(mAreaPickingSpinner.getSelectedIndex()).getId();
        int tableNumber = (int) mTablePickingSpinner.getItems().get(mTablePickingSpinner.getSelectedIndex());


        final Table table = new Table();
        table.setAreaId(selectedAreaId);
        table.setTableNumber(tableNumber);

        mDataService.createNewTable(table, waiterId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {

                        OrderDetailsEditor.createNew(table);

                        Intent intent = new Intent(mActivity, OrderManagementActivity.class);
                        intent.putExtra("CREATED", "CREATED");
                        startActivity(intent);

                    });
                } else {
                    runOnUiThread(() -> {
                        AlertMessage.showMessage(mActivity, "Eroare", "Masa a fost creată deja. Reîncercați.");
                        int spinnerSelectedItemIndex = mAreasRepository.get().get(mTablePickingSpinner.getSelectedIndex()).getId();
                        loadTablesSpinnerWithTablesFromArea(spinnerSelectedItemIndex);
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> AlertMessage.showMessage(mActivity, "Eroare", "Deschiderea mesei a eșuat (" + t.getMessage() + ")."));
            }
        });
    }

    @Override
    public void onBackPressed() {

        final Activity activityToBeClosed = this;

        new MaterialDialog.Builder(this)
                .title("Anulare comandă")
                .content("Ești pe cale să anulezi crearea unei comenzi. Ești sigur că vrei să faci asta?")
                .positiveText("DA")
                .onPositive((dialog, which) -> activityToBeClosed.finish())
                .negativeText("ANULARE")
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }
}
