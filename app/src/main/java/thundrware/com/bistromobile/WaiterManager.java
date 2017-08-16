package thundrware.com.bistromobile;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import thundrware.com.bistromobile.listeners.OnLoginHandler;
import thundrware.com.bistromobile.listeners.WaiterQueriedListener;
import thundrware.com.bistromobile.models.Waiter;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;

public class WaiterManager {

    private ApplicationPreferences mApplicationPreferences;

    public static final int defaultWaiterId = -1;

    public WaiterManager() {
        mApplicationPreferences = new ApplicationPreferences(MyApplication.getContext());
    }

    public void logWaiterIn(String password, OnLoginHandler handler) {

        Single<Waiter> waiterObservable = getWaiterObservableByPassword(password);
        waiterObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(waiter -> {
                    persistWaiterData(waiter);
                    handler.onSuccess();
                }, e -> handler.onError(e));

    }

    public void signWaiterOut() {
        removeWaiterData();
    }

    public Waiter getCurrentWaiter() {
        Waiter waiter = new Waiter();
        waiter.setName(mApplicationPreferences.getWaiterName());
        waiter.setId(mApplicationPreferences.getWaiterId());
        waiter.setPassword(mApplicationPreferences.getWaiterPassword());

        return waiter;
    }

    public void startWaiterAvailabilityChecker(WaiterQueriedListener waiterListener) {
        getWaiterObservableByPassword(getCurrentWaiter().getPassword())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(waiter -> waiterListener.onWaiterQueried(waiter), e -> waiterListener.onError(e));

    }

    private Single<Waiter> getWaiterObservableByPassword(String password) {
        ServerConnectionDetailsManager serverManager = new ServerConnectionDetailsManager();
        DataService waiterRetrieverService = DataServiceProvider.create(serverManager.getConnectionDetails().toString());
        return waiterRetrieverService.getWaiter(password);
    }

    private void persistWaiterData(Waiter waiter) {
        mApplicationPreferences.setWaiterId(waiter.getId());
        mApplicationPreferences.setWaiterName(waiter.getName());
        mApplicationPreferences.setWaiterPassword(waiter.getPassword());
    }

    private void removeWaiterData() {
        mApplicationPreferences.clearWaiterId();
        mApplicationPreferences.clearWaiterName();
        mApplicationPreferences.clearWaiterPassword();
    }

}
