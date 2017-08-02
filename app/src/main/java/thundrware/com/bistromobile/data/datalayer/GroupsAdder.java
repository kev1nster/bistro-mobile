package thundrware.com.bistromobile.data.datalayer;

import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.Group;

public class GroupsAdder extends DataAdder implements Executable {

    private final List<Group> mGroups;


    public GroupsAdder(List<Group> groupsList) {
        super();
        mGroups = groupsList;
    }

    @Override
    public void execute() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(mGroups);
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
