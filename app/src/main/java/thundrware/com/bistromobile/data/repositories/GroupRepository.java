package thundrware.com.bistromobile.data.repositories;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.listeners.DataPersistingListener;
import thundrware.com.bistromobile.models.Group;

public class GroupRepository extends RepositoryBase implements Repository<Group> {

    public GroupRepository() {
        super();
    }

    @Override
    public void add(final Group item) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    @Override
    public void addRange(final Collection<Group> items) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(items);
            }
        });
    }

    public void addRangeAsync(final Collection<Group> items, DataPersistingListener listener) {
        realmInstance.executeTransactionAsync(realm -> realm.copyToRealm(items),
                () -> listener.onSuccess(),
                error -> listener.onError(error));
    }

    @Override
    public void update(final Group itemToUpdate, final int id) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Group group = realm.where(Group.class).equalTo("Id", id).findFirst();
                group.setName(itemToUpdate.getName());
            }
        });
    }

    @Override
    public void delete(final int id) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Group.class).equalTo("Id", id).findFirst().deleteFromRealm();
            }
        });
    }

    @Override
    public Group get(int id) {
        return realmInstance.where(Group.class).equalTo("Id", id).findFirst();
    }

    public List<Group> getWhereGroupIsIn(String groupIdStringList) {

        String[] groupsIdsAsString = groupIdStringList.split(",");
        Integer[] groupsIds = new Integer[groupsIdsAsString.length];

        for (int i=0; i < groupsIds.length; i++) {
            groupsIds[i] = Integer.parseInt(groupsIdsAsString[i]);
        }

        return realmInstance.where(Group.class).in("Id", groupsIds).findAll();
    }

    @Override
    public List<Group> get() {
        return realmInstance.where(Group.class).findAll();
    }

    @Override
    public boolean contains(Group item) {
        return false;
    }
}
