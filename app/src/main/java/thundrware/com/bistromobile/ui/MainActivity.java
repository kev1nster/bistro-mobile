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
import thundrware.com.bistromobile.data.DataManager;
import thundrware.com.bistromobile.models.Product;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DataManager d = new DataManager();
        d.emptyDatabase();

    }

    @Override
    public void onBackPressed() {
        return;
    }
}
