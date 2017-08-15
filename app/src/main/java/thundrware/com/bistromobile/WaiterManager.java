package thundrware.com.bistromobile;

import retrofit2.Response;
import thundrware.com.bistromobile.models.Waiter;
import thundrware.com.bistromobile.networking.DataService;

public class WaiterManager {

    private ApplicationPreferences mApplicationPreferences;

    public static final int defaultWaiterId = -1;

    public WaiterManager() {
        mApplicationPreferences = new ApplicationPreferences(MyApplication.getContext());
    }

    public void logWaiterIn(String password) throws InvalidWaiterPasswordException {

        Waiter waiter = getWaiterByPassword(password);

        if (waiter != null) {
            persistWaiterData(waiter);
        } else {
            throw new InvalidWaiterPasswordException();
        }
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

    public boolean isAnyWaiterLoggedIn() {
        return (getWaiterByPassword(getCurrentWaiter().getPassword()) != null);
    }

    private Waiter getWaiterByPassword(String password) {
        DataService waiterRetrieverService = new ServerManager().getService();
        try {
           Response<Waiter> waiterResponse = waiterRetrieverService.getWaiter(password).execute();

           if (waiterResponse.isSuccessful()) {
               return waiterResponse.body();
           } else {
               return null;
           }
        } catch (Exception ex) {
            return null;
        }
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
