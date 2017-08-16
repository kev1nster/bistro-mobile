package thundrware.com.bistromobile.networking;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Group;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.models.Waiter;

public interface DataService {
    @GET("api/waiters/get")
    Single<Waiter> getWaiter(@Query("password") String password);

    @GET("api/products")
    Single<List<Product>> getProducts();

    @GET("api/categories")
    Single<List<Category>> getCategories();

    @GET("api/areas")
    Single<List<Area>> getAreas();

    @GET("api/groups")
    Single<List<Group>> getGroups();

    @GET("api/connection/check")
    Call<ResponseBody> checkConnection();
}
