package thundrware.com.bistromobile.data.datalayer;

import io.realm.Realm;

public class DataAdder {

    protected Realm mRealm;

    public DataAdder() {
        mRealm = Realm.getDefaultInstance();
    }

}
