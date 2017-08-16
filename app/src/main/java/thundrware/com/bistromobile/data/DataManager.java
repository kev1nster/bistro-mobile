package thundrware.com.bistromobile.data;

import io.realm.Realm;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;
import thundrware.com.bistromobile.listeners.DataProcessingListener;
import thundrware.com.bistromobile.models.Waiter;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;
import thundrware.com.bistromobile.networking.ServerAddress;

public class DataManager implements DataProcessingListener {

    private DataService mDataService;
    private Realm mRealm;
    private DataProcessingListener mDataProcessingListener;

    public DataManager() {
        mRealm = Realm.getDefaultInstance();
        ServerConnectionDetailsManager serverConnectionDetailsManager = new ServerConnectionDetailsManager();
        ServerAddress serverAddress = serverConnectionDetailsManager.getConnectionDetails();
        mDataService = DataServiceProvider.create(serverAddress.toString());
    }

    public void setDataProcessingListener(DataProcessingListener processingListener) {
        mDataProcessingListener = processingListener;
    }

    public void initialize() {

        DataLoader dataLoader = new DataLoader(mDataService, this);

        dataLoader.loadAreas()
                .loadCategories()
                .loadGroups()
                .loadProducts();
    }

    public boolean isEmpty() {
        if (mRealm.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void emptyDatabase() {
        mRealm.executeTransaction(realm -> realm.deleteAll());
    }


    @Override
    public void onProcessFinished() {
        mDataProcessingListener.onProcessFinished();
    }

    @Override
    public void onProcessStatusUpdate(String message) {
        mDataProcessingListener.onProcessStatusUpdate(message);
    }
}
