package thundrware.com.bistromobile.networking;

import java.util.List;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Group;
import thundrware.com.bistromobile.models.OrderDataTransferObject;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.models.Table;
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

    @GET("api/tables/getAvailableForArea")
    Single<List<Integer>> getAvailableTablesFor(@Query("areaId") int areaId);

    @GET("api/tables/getActive")
    Call<ResponseBody> getActive(@Query("waiterId") int waiterId);

    @GET("api/tables/getItems")
    Call<ResponseBody> getItemsFor(@Query("areaId") int areaId, @Query("tableNumber") int tableNumber, @Query("waiterId") int waiterId);

    @POST("api/tables/create")
    Call<ResponseBody> createNewTable(@Header("TOKEN") String authenticationToken, @Body Table table, @Query("waiterId") int waiterId);

    @POST("api/tables/insertNewItems")
    Call<ResponseBody> sendItems(@Header("TOKEN") String authenticationToken, @Body OrderDataTransferObject orderObject);



}
