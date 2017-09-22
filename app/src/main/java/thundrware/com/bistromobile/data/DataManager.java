package thundrware.com.bistromobile.data;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;
import thundrware.com.bistromobile.listeners.DataLoadingListener;
import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Group;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;
import thundrware.com.bistromobile.networking.ServerAddress;

public class DataManager {

    private DataService mDataService;
    private Realm mRealm;
    private DataLoadingListener mDataProcessingListener;

    public DataManager() {
        mRealm = Realm.getDefaultInstance();
        ServerConnectionDetailsManager serverConnectionDetailsManager = new ServerConnectionDetailsManager();
        ServerAddress serverAddress = serverConnectionDetailsManager.getConnectionDetails();
        mDataService = DataServiceProvider.create(serverAddress.toString());
    }

    public void setDataProcessingListener(DataLoadingListener processingListener) {
        mDataProcessingListener = processingListener;
    }

    public boolean isEmpty() {
        List<RealmQuery<? extends RealmObject>> realmEntities = new ArrayList<>();

        realmEntities.add(mRealm.where(Area.class));
        realmEntities.add(mRealm.where(Product.class));
        realmEntities.add(mRealm.where(Group.class));
        realmEntities.add(mRealm.where(Category.class));

        boolean dbIsEmpty = true;

        for (RealmQuery query : realmEntities) {
            if (query.count() != 0) {
                dbIsEmpty = false;
                break;
            }
        }

        return dbIsEmpty;
    }

    public void emptyDatabase() {
        mRealm.executeTransaction(realm -> realm.deleteAll());
    }
}
