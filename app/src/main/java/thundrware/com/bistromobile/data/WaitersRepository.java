package thundrware.com.bistromobile.data;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.Waiter;

public class WaitersRepository extends RepositoryBase implements Repository<Waiter> {

    public WaitersRepository() {
        super();
    }

    @Override
    public void add(final Waiter item) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    @Override
    public void addRange(final Collection<Waiter> items) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(items);
            }
        });
    }

    @Override
    public void update(final Waiter itemToUpdate, final int id) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Waiter waiter = realmInstance.where(Waiter.class).equalTo("Id", id).findFirst();
                waiter.setName(itemToUpdate.getName());
                waiter.setPassword(itemToUpdate.getPassword());
            }
        });
    }

    @Override
    public void delete(final int id) {

        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Waiter.class).equalTo("Id", id).findFirst().deleteFromRealm();
            }
        });
    }

    @Override
    public Waiter get(int id) {
        return realmInstance.where(Waiter.class).equalTo("Id", id).findFirst();
    }

    @Override
    public List<Waiter> get() {
        return realmInstance.where(Waiter.class).findAll();
    }
}
