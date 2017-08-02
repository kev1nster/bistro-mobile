package thundrware.com.bistromobile.data.datalayer;

import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.Product;

public class ProductsAdder extends DataAdder implements Executable {

    final List<Product> mProducts;
    public ProductsAdder(List<Product> productList) {
        super();
        mProducts = productList;
    }

    @Override
    public void execute() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(mProducts);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });
    }
}
