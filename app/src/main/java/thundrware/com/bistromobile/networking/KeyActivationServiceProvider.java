package thundrware.com.bistromobile.networking;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;

public class KeyActivationServiceProvider {

    public static KeyActivationService create() {
        ServerConnectionDetailsManager manager = new ServerConnectionDetailsManager();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(manager.getConnectionDetails().toString())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(KeyActivationService.class);
    }
}
