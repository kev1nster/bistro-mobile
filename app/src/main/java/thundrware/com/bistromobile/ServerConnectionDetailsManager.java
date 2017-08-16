package thundrware.com.bistromobile;


import java.io.IOException;
import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;
import thundrware.com.bistromobile.networking.ServerAddress;

public class ServerConnectionDetailsManager {

    private ApplicationPreferences mApplicationPreferences;
    private String mAddress;

    public ServerConnectionDetailsManager() {
        mApplicationPreferences = new ApplicationPreferences(MyApplication.getContext());
        mAddress = getConnectionDetails().toString();
    }

    public ServerAddress getConnectionDetails() {
        ServerAddress serverAddress = new ServerAddress();
        serverAddress.setAddress(mApplicationPreferences.getServerIp());
        serverAddress.setPort(mApplicationPreferences.getServerPort());

        return serverAddress;
    }

    public void setConnectionDetails(ServerAddress serverAddress) {
        persistServerDetails(serverAddress);
    }

    public boolean detailsAreSet() {
        if (mApplicationPreferences.getServerIp() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void tryConnection(DataService dataService, Callback<ResponseBody> callback) {
        dataService.checkConnection().enqueue(callback);
    }

    private void persistServerDetails(ServerAddress serverAddress) {
        mApplicationPreferences.setServerIp(serverAddress.getAddress());
        mApplicationPreferences.setServerPort(serverAddress.getPort());
    }
}
