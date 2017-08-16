package thundrware.com.bistromobile.data.repositories;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.Category;

public class CategoriesRepository extends RepositoryBase implements Repository<Category> {

    public CategoriesRepository() {
        super();
    }

    @Override
    public void add(final Category item) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    @Override
    public void addRange(final Collection<Category> items) {
        realmInstance.executeTransaction(realm -> realm.copyToRealm(items));
    }

    @Override
    public void update(final Category itemToUpdate, final int id) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Category category = realm.where(Category.class).equalTo("Id", id).findFirst();
                category.setName(itemToUpdate.getName());
                category.setGroupsList(itemToUpdate.getGroupsList());
            }
        });
    }

    @Override
    public void delete(final int id) {
        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Category.class).equalTo("Id", id).findFirst().deleteFromRealm();
            }
        });
    }

    @Override
    public Category get(int id) {
        return realmInstance.where(Category.class).equalTo("Id", id).findFirst();
    }

    @Override
    public List<Category> get() {
        return realmInstance.where(Category.class).findAll();
    }

    @Override
    public boolean contains(Category item) {
        return false;
    }
}
