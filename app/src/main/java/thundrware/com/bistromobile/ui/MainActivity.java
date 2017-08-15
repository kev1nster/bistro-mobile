package thundrware.com.bistromobile.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.adapters.ActiveTablesAdapter;
import thundrware.com.bistromobile.models.Product;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activeOrdersRecyclerView) RecyclerView mActiveOrdersRecyclerView;
    @BindView(R.id.mainActivityToolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i=0; i < 160; i++) {
                    Product product = new Product();
                    product.setId(i);
                    product.setName("Tuica de " + i + " ml");
                    product.setPrice(0.56 * i);

                    realm.copyToRealm(product);
                }
            }
        });*/

        RealmResults<Product> products = Realm.getDefaultInstance().where(Product.class).findAll();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ActiveTablesAdapter activeTablesAdapter = new ActiveTablesAdapter(this, products);

        mActiveOrdersRecyclerView.setLayoutManager(linearLayoutManager);
        mActiveOrdersRecyclerView.setAdapter(activeTablesAdapter);
        mActiveOrdersRecyclerView.setHasFixedSize(true);

        mToolbar.setTitle("Mese deschise");
        mToolbar.setSubtitle("Ospatar: CĂLIN");

    }

    @OnClick(R.id.mainActivityToolbar)
    public void toolbarClickedHandler() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
