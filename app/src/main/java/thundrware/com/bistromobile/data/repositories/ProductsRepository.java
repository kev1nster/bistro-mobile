package thundrware.com.bistromobile.data.repositories;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import thundrware.com.bistromobile.listeners.DataPersistingListener;
import thundrware.com.bistromobile.models.Group;
import thundrware.com.bistromobile.models.Product;

public class ProductsRepository extends RepositoryBase implements Repository<Product> {

    public ProductsRepository() {
        super();
    }

    @Override
    public void add(final Product item) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    @Override
    public void addRange(final Collection<Product> items) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(items);
            }
        });
    }

    public void addRangeAsync(final Collection<Product> items, DataPersistingListener listener) {
        realmInstance.executeTransactionAsync(realm -> realm.copyToRealm(items),
                () -> listener.onSuccess(),
                error -> listener.onError(error));
    }

    @Override
    public void update(final Product itemToUpdate, final int id) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Product product = realm.where(Product.class).equalTo("Id", id).findFirst();
                product.setGroupId(itemToUpdate.getGroupId());
                product.setName(itemToUpdate.getName());
                product.setPrice(itemToUpdate.getPrice());
            }
        });
    }

    @Override
    public void delete(final int id) {
        realmInstance.executeTransaction(realm -> realm.where(Product.class).equalTo("Id", id).findFirst().deleteFromRealm());
    }

    @Override
    public Product get(@NonNull int id) {
        return realmInstance.where(Product.class).equalTo("Id", id).findFirst();
    }

    public List<Product> get(RealmQuery<Product> realmQuery) {
        return realmInstance.copyFromRealm(realmQuery.findAll());
    }

    public List<Product> getWhereGroupIdEquals(int groupId) {
        RealmQuery<Product> query = realmInstance.where(Product.class).equalTo("GroupId", groupId);
        return realmInstance.copyFromRealm(query.findAll());
    }

    @Override
    public List<Product> get() {
        return realmInstance.where(Product.class).findAll();
    }

    @Override
    public boolean contains(Product item) {
        return false;
    }
}
