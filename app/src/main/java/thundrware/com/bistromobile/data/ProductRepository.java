package thundrware.com.bistromobile.data;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.models.Waiter;

public class ProductRepository extends RepositoryBase implements Repository<Product> {

    public ProductRepository() {
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
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Product.class).equalTo("Id", id).findFirst().deleteFromRealm();
            }
        });
    }

    @Override
    public Product get(int id) {
        return realmInstance.where(Product.class).equalTo("Id", id).findFirst();
    }

    @Override
    public List<Product> get() {
        return realmInstance.where(Product.class).findAll();
    }
}
