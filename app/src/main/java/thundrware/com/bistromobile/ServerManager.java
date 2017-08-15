package thundrware.com.bistromobile;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.ServerAddress;

public class ServerManager {

    private ApplicationPreferences mApplicationPreferences;
    private final String mAddress;
    private final DataService mDataService;

    public ServerManager() {
        mApplicationPreferences = new ApplicationPreferences(MyApplication.getContext());
        mAddress = getConnectionDetails().toString();
        mDataService = getService();
    }

    private ServerAddress getConnectionDetails() {
        ServerAddress serverAddress = new ServerAddress();
        serverAddress.setAddress(mApplicationPreferences.getServerIp());
        serverAddress.setPort(mApplicationPreferences.getServerPort());

        return serverAddress;
    }

    public void setConnectionDetails(ServerAddress serverAddress) {
        mApplicationPreferences.setServerIp(serverAddress.getAddress());
        mApplicationPreferences.setServerPort(serverAddress.getPort());
    }

    public boolean detailsAreSet() {
        if (mApplicationPreferences.getServerIp() == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAccessible() {
        try {
            return tryConnectingToServer();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public DataService getService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(DataService.class);
    }

    private boolean tryConnectingToServer() throws IOException {
        Response<ResponseBody> response = mDataService.checkConnection().execute();
        if (response.isSuccessful()) {
            return true;
        } else {
            return false;
        }
    }
}
