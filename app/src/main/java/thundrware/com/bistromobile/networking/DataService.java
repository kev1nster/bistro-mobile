package thundrware.com.bistromobile.networking;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Group;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.models.Waiter;

public interface DataService {

    @GET("api/waiters")
    Call<List<Waiter>> getWaiters();

    @GET("api/products")
    Call<List<Product>> getProducts();

    @GET("api/categories")
    Call<RequestBody> getCategories();

    @GET("api/areas")
    Call<List<Area>> getAreas();

    @GET("api/groups")
    Call<List<Group>> getGroups();
}
