package thundrware.com.bistromobile.data.repositories;

import java.util.Collection;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.realm.Realm;
import thundrware.com.bistromobile.models.Category;

public class CategoriesRepository extends RepositoryBase implements Repository<Category> {

    public CategoriesRepository() {
        super();
    }

    @Override
    public void add(final Category item) {
        realmInstance.executeTransaction(realm -> realm.copyToRealm(item));
    }

    @Override
    public void addRange(final Collection<Category> items) {
        realmInstance.executeTransaction(realm -> realm.copyToRealm(items));
    }

    @Override
    public void update(final Category itemToUpdate, final int id) {
        realmInstance.executeTransaction(realm -> {
            Category category = realm.where(Category.class).equalTo("Id", id).findFirst();
            category.setName(itemToUpdate.getName());
            category.setGroupsList(itemToUpdate.getGroupsList());
        });
    }

    @Override
    public void delete(@NonNull final int id) {
        realmInstance.executeTransaction(realm -> realm.where(Category.class).equalTo("Id", id).findFirst().deleteFromRealm());
    }

    @Override
    public Category get(int id) {
        return realmInstance.where(Category.class).equalTo("Id", id).findFirst();
    }

    public Category get(String name) {
        return realmInstance.where(Category.class).equalTo("Name", name).findFirst();
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
