package thundrware.com.bistromobile.data;

import io.realm.Realm;
import thundrware.com.bistromobile.ServerManager;
import thundrware.com.bistromobile.listeners.DataProcessingListener;
import thundrware.com.bistromobile.models.Waiter;
import thundrware.com.bistromobile.networking.DataService;

public class DataManager implements DataProcessingListener {

    private DataService mDataService;
    private Realm mRealm;
    private DataProcessingListener mDataProcessingListener;

    public DataManager() {
        mRealm = Realm.getDefaultInstance();
        ServerManager serverManager = new ServerManager();
        mDataService = serverManager.getService();
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
        mRealm.deleteAll();
    }

    public Waiter getWaiterByPassword(String password) {

        Waiter waiter;
        try {
            waiter = mDataService.getWaiter(password).execute().body();
        } catch (Exception ex) {
            waiter = null;
        }

        return waiter;

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
