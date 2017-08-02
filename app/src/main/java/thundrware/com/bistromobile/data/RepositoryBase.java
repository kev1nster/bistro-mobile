package thundrware.com.bistromobile.data;

import io.realm.Realm;

public class RepositoryBase {

    protected Realm realmInstance;

    public RepositoryBase() {
        realmInstance = Realm.getDefaultInstance();
    }
}
