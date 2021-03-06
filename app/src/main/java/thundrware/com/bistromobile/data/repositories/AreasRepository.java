package thundrware.com.bistromobile.data.repositories;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.listeners.DataPersistingListener;
import thundrware.com.bistromobile.models.Area;


public class AreasRepository extends RepositoryBase implements Repository<Area> {

    public AreasRepository() {
        super();
    }

    @Override
    public void add(final Area item) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    @Override
    public void addRange(final Collection<Area> items) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(items);
            }
        });
    }

    public void addRangeAsync(final Collection<Area> items, DataPersistingListener listener) {
        realmInstance.executeTransactionAsync(realm -> realm.copyToRealm(items),
                () -> listener.onSuccess(),
                error -> listener.onError(error));
    }

    @Override
    public void update(final Area itemToUpdate, final int id) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Area area = realm.where(Area.class).equalTo("Id", id).findFirst();
                area.setName(itemToUpdate.getName());
                area.setNumberOfTables(itemToUpdate.getNumberOfTables());
            }
        });
    }

    @Override
    public void delete(final int id) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Area.class).equalTo("Id", id).findFirst().deleteFromRealm();
            }
        });
    }

    @Override
    public Area get(int id) {
        return realmInstance.where(Area.class).equalTo("Id", id).findFirst();
    }

    @Override
    public List<Area> get() {
        return realmInstance.where(Area.class).distinct("id");
    }

    @Override
    public boolean contains(Area item) {
        return false;
    }
}
