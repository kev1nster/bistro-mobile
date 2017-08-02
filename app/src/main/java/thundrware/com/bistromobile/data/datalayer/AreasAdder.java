package thundrware.com.bistromobile.data.datalayer;

import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.Area;

public class AreasAdder extends DataAdder implements Executable {

    private final List<Area> mAreas;

    public AreasAdder(List<Area> areasList) {
        super();
        mAreas = areasList;
    }

    @Override
    public void execute() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(mAreas);
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
