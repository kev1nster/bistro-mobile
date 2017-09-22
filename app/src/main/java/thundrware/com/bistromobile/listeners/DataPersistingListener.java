package thundrware.com.bistromobile.listeners;

public interface DataPersistingListener {

    void onSuccess();
    void onError(Throwable e);
}
