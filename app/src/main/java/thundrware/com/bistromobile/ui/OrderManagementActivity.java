package thundrware.com.bistromobile.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thundrware.com.bistromobile.ActivationKeyManager;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.Message;
import thundrware.com.bistromobile.MyApplication;
import thundrware.com.bistromobile.OrderDetailsEditor;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.adapters.OrderActivityPagerAdapter;
import thundrware.com.bistromobile.adapters.OrderItemsAdapter;
import thundrware.com.bistromobile.data.repositories.AreasRepository;
import thundrware.com.bistromobile.data.repositories.CategoriesRepository;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.OrderDataTransferObject;
import thundrware.com.bistromobile.models.OrderItem;
import thundrware.com.bistromobile.models.Table;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;
import thundrware.com.bistromobile.ui.presenters.OrderManagementActivityPresenter;
import thundrware.com.bistromobile.ui.views.OrderManagementActivityView;

public class OrderManagementActivity extends FragmentActivity implements OrderManagementActivityView {

    @BindView(R.id.bottomSheetLayoutIncluded)
    ConstraintLayout mBottomLayout;

    @BindView(R.id.orderItemsRecyclerView)
    RecyclerView mOrderItemsRecyclerView;

    @BindView(R.id.tableDetailsTextView)
    TextView mTableDetailsTextView;

    @BindView(R.id.totalOrderAmountTextView)
    TextView mTotalOrderAmountTextView;

    @BindView(R.id.orderActivityViewPager)
    ViewPager mViewPager;

    @BindView(R.id.orderActivityTabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.handleImageView)
    ImageView mHandleImageView;

    @BindView(R.id.sendOrderTextView)
    TextView mSendOrderTextView;

    @BindView(R.id.sendOrderProgressBar)
    ProgressBar mSendOrderProgressBar;

    private Context context;
    private OrderActivityPagerAdapter pagerAdapter;
    private OrderDetailsEditor mOrderManager;
    private CategoriesRepository mCategoriesRepository;
    private AreasRepository mAreasRepository;
    private List<Category> mCategories;
    private Realm mRealm;

    private OrderManagementActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);
        ButterKnife.bind(this);
        mRealm = Realm.getDefaultInstance();

        context = this;
        mAreasRepository = new AreasRepository();
        mCategoriesRepository = new CategoriesRepository();
        mCategories = mCategoriesRepository.get();

        pagerAdapter = new OrderActivityPagerAdapter(this, mCategoriesRepository.get(), getFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        presenter = new OrderManagementActivityPresenter(this);

        presenter.tableDetailsLoaded(OrderDetailsEditor.getTable());

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mBottomLayout);

        if (!getIntent().hasExtra("CREATED"))
        {
            /*
                It means that the order isn't new, it has been already sent and has at least one ordered product in it.
                Therefore, we provide the user the list with existing products.
            */
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mHandleImageView.setImageResource(R.drawable.ic_chevron_down);

        } else {
            /*
                It means that there's a new order that has to be processed.
                Therefore, if there's no product that has already been ordered, we'll provide the user the list of products he/she can choose from.
             */
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mHandleImageView.setImageResource(R.drawable.ic_chevron_up);
        }

        mRealm.addChangeListener(realm -> {
           runOnUiThread(() -> {

               double sum = 0;
               for (OrderItem item : realm.where(OrderItem.class).findAll()) {
                   sum += item.getProduct().getPrice() * item.getQuantity();
               }
               mTotalOrderAmountTextView.setText(String.valueOf(sum));
           });
        });

        updateTotalOrderAmountTextView();

       bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mHandleImageView.setImageResource(R.drawable.ic_chevron_up);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        mHandleImageView.setImageResource(R.drawable.ic_chevron_down);
                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        mHandleImageView.setImageResource(R.drawable.ic_chevron_stale);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                hideKeyboard();
            }
        });

        fillOrderItemsRecyclerView();
    }

    private void updateTotalOrderAmountTextView() {
        double sum = 0;
        for (OrderItem item : mRealm.where(OrderItem.class).findAll()) {
            sum += item.getProduct().getPrice() * item.getQuantity();
        }

        DecimalFormat formatter = new DecimalFormat("#.##");
        mTotalOrderAmountTextView.setText(formatter.format(sum));
    }

    @OnClick(R.id.sendOrderTextView)
    public void sendOrderImageViewButtonHandler() {

        mSendOrderTextView.setVisibility(View.INVISIBLE);
        mSendOrderProgressBar.setVisibility(View.VISIBLE);


        OrderDataTransferObject orderObject = new OrderDataTransferObject();
        if (getNewOrderItems().size() != 0) {
            orderObject.setNewItems(getNewOrderItems());
        } else {
            AlertMessage.showMessage(this, "Eroare", "Nu ați adăugat articole pentru a efectua altă comandă!");
        }
        orderObject.setTable(OrderDetailsEditor.getTable());
        orderObject.setWaiterId(new WaiterManager().getCurrentWaiter().getId());

        DataService service = DataServiceProvider.getDefault();

        ActivationKeyManager manager = new ActivationKeyManager(this);

        /*
        Checking whether the device is connected to internet
         */

        String orderObjectAsJson = new Gson().toJson(orderObject);

        if (MyApplication.isNetworkAvailable()) {
            service.sendItems(manager.getToken(), orderObject).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            hideProgressBar();
                            Toast.makeText(context, "Comandă trimisă cu succes!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    } else {
                        if (response.code() == 401) {
                            runOnUiThread(() -> {
                                hideProgressBar();
                                Message.showUnauthorizedAccess(context);
                            });
                        } else {
                            runOnUiThread(() -> {
                                hideProgressBar();
                                Message.showError(context, response.message());
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Message.showError(this, "Dispozitivul nu este conectat la internet!");
        }

    }

    private void fillOrderItemsRecyclerView() {
        OrderedRealmCollection<OrderItem> orderItemsRealmCollection = mRealm.where(OrderItem.class).findAll();
        OrderItemsAdapter adapter = new OrderItemsAdapter(this, orderItemsRealmCollection, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mOrderItemsRecyclerView.setAdapter(adapter);
        mOrderItemsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private List<OrderItem> getNewOrderItems() {
        return mRealm.copyFromRealm(mRealm.where(OrderItem.class).equalTo("IsNew", true).findAll());
    }

    @Override
    public void changeTableDetails(Table table) {
        String tableDetailsText = "(" + table.getDisplayTableNumber() + ") " + mAreasRepository.get(table.getAreaId()).getName().toUpperCase();
        mTableDetailsTextView.setText(tableDetailsText);
    }

    private void hideProgressBar() {
        mSendOrderProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        final Activity activityToBeClosed = this;

        if (mRealm.where(OrderItem.class).equalTo("IsNew", true).findAll().size() > 0) {
            new MaterialDialog.Builder(this)
                    .title("Anulare comandă")
                    .content("Dacă renunți la comandă produsele vor fi șterse. Vrei să faci asta?")
                    .positiveText("DA")
                    .onPositive((dialog, which) -> activityToBeClosed.finish())
                    .negativeText("NU")
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            finish();
        }
    }
}
