package thundrware.com.bistromobile.data.datalayer;

import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.Waiter;

public class WaitersAdder extends DataAdder implements Executable {

    private final List<Waiter> mWaiters;

    public WaitersAdder(List<Waiter> waitersList) {
        super();
        mWaiters = waitersList;
    }

    @Override
    public void execute() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // TODO
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });
    }
}
