package thundrware.com.bistromobile.data.repositories;

import io.realm.Realm;

class RepositoryBase {

    protected Realm realmInstance;

    RepositoryBase() {
        realmInstance = Realm.getDefaultInstance();
    }
}
