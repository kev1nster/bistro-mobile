package thundrware.com.bistromobile.networking;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import thundrware.com.bistromobile.models.KeyActivationDetails;

public interface KeyActivationService {

    @GET("api/keys/check")
    Call<ResponseBody> checkKey(@Query("token") String token);

    @POST("api/keys/activate")
    Call<ResponseBody> activateKey(@Body KeyActivationDetails details);
}
