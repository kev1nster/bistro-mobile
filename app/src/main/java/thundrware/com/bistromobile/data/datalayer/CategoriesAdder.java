package thundrware.com.bistromobile.data.datalayer;

import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.Category;

public class CategoriesAdder extends DataAdder implements Executable {

    private final List<Category> mCategories;
    public CategoriesAdder(List<Category> categoryList) {
        super();
        mCategories = categoryList;
    }
    @Override
    public void execute() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(mCategories);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // TODO
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // TODO
            }
        });
    }
}
