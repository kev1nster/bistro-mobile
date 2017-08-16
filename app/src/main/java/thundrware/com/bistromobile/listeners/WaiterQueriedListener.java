package thundrware.com.bistromobile.listeners;

import thundrware.com.bistromobile.models.Waiter;

public interface WaiterQueriedListener {
    void onWaiterQueried(Waiter waiter);
    void onError(Throwable e);
}
