package thundrware.com.bistromobile.networking;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Group;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.models.Waiter;

public interface DataService {

    @GET("api/waiters/{id}")
    Call<Waiter> getWaiter(@Path("id") int id);

    @GET("api/waiters/{password}")
    Call<Waiter> getWaiter(@Path("password") String password);

    @GET("api/products")
    Observable<List<Product>> getProducts();

    @GET("api/categories")
    Observable<List<Category>> getCategories();

    @GET("api/areas")
    Observable<List<Area>> getAreas();

    @GET("api/groups")
    Observable<List<Group>> getGroups();

    @GET("api/connection/check")
    Call<ResponseBody> checkConnection();
}
