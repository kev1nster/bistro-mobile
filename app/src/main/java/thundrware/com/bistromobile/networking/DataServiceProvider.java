package thundrware.com.bistromobile.networking;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;

public class DataServiceProvider {

    public static DataService create(String string) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(string)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(DataService.class);
    }

    public static DataService getDefault() {
        ServerConnectionDetailsManager manager = new ServerConnectionDetailsManager();
        return create(manager.getConnectionDetails().toString());
    }
}
