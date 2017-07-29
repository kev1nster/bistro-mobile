package thundrware.com.bistromobile.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Retrofit;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.RandomRetrofitService;
import thundrware.com.bistromobile.models.Waiter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.jsonWaitersTextView) TextView jsonWaitersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.placeNewOrderButton)
    public void loadProductsFromApi()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.1")
                .build();

        RandomRetrofitService service = retrofit.create(RandomRetrofitService.class);

        Call<List<Waiter>> waiters = service.getWaiters();


    }
}
