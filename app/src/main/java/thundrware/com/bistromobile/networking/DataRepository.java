package thundrware.com.bistromobile.networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import thundrware.com.bistromobile.data.datalayer.WaitersAdder;
import thundrware.com.bistromobile.models.Waiter;

public class DataRepository {

    private final String mIpAddress;
    private final DataService mDataService;

    public DataRepository() {
        mIpAddress = "http://192.168.0.1";
        mDataService = createService();
    }

    private DataService createService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mIpAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(DataService.class);
    }

    public void addWaiters() {

        mDataService.getWaiters().enqueue(new Callback<List<Waiter>>() {
            @Override
            public void onResponse(Call<List<Waiter>> call, Response<List<Waiter>> response) {
                WaitersAdder adder = new WaitersAdder(response.body());
                adder.execute();
            }

            @Override
            public void onFailure(Call<List<Waiter>> call, Throwable t) {
                // show error
            }
        });
    }
}
