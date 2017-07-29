package thundrware.com.bistromobile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import thundrware.com.bistromobile.models.Waiter;

public interface RandomRetrofitService {

    @GET("api/waiters")
    Call<List<Waiter>> getWaiters();
}
