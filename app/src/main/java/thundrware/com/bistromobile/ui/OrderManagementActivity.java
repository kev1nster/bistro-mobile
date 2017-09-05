package thundrware.com.bistromobile.ui;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.OrderDetailsEditor;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.adapters.OrderActivityPagerAdapter;
import thundrware.com.bistromobile.adapters.OrderItemsAdapter;
import thundrware.com.bistromobile.data.repositories.AreasRepository;
import thundrware.com.bistromobile.data.repositories.CategoriesRepository;
import thundrware.com.bistromobile.interfaces.OrderSendingHandler;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Order;
import thundrware.com.bistromobile.models.OrderDataTransferObject;
import thundrware.com.bistromobile.models.OrderItem;
import thundrware.com.bistromobile.models.Table;
import thundrware.com.bistromobile.ui.presenters.OrderManagementActivityPresenter;
import thundrware.com.bistromobile.ui.views.OrderManagementActivityView;

public class OrderManagementActivity extends FragmentActivity implements OrderManagementActivityView {

    @BindView(R.id.bottomLayout)
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

        } else {

            /*
                It means that there's a new order that has to be processed.
                Therefore, if there's no product that has already been ordered, we'll provide the user the list of products he/she can choose from.
             */
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

       /* bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // The productsRecyclerView can be functional
                        setViewAndChildrenEnabled(mProductsRecyclerView, true);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        // The productsRecyclerView can't be functional
                        setViewAndChildrenEnabled(mProductsRecyclerView, false);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                hideKeyboard();
            }
        });*/

        fillOrderItemsRecyclerView();
    }

    @OnClick(R.id.sendOrderImageView)
    public void sendOrderImageViewButtonHandler() {

        OrderDataTransferObject orderObject = new OrderDataTransferObject();
        if (getNewOrderItems().size() != 0) {
            orderObject.setNewItems(getNewOrderItems());
        } else {
            AlertMessage.showMessage(this, "Eroare", "Nu ați adăugat articole pentru a efectua altă comandă!");
        }
        orderObject.setTable(OrderDetailsEditor.getTable());
        orderObject.setWaiterId(new WaiterManager().getCurrentWaiter().getId());

        Order order = new Order(orderObject);

        order.send(new OrderSendingHandler() {

            @Override
            public void onSuccess() {
                runOnUiThread(() -> Toast.makeText(context, "A mers, ba", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Throwable error) {
                runOnUiThread(() -> Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

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

    private void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    @Override
    public void changeTotalAmount(Double value) {

    }

    @Override
    public void reloadProductsRecyclerView(int categoryId) {

    }

    @Override
    public void changeTableDetails(Table table) {
        String tableDetailsText = "(" + table.getDisplayTableNumber() + ") " + mAreasRepository.get(table.getAreaId()).getName().toUpperCase();
        mTableDetailsTextView.setText(tableDetailsText);
    }
}
