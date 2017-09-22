package thundrware.com.bistromobile.listeners;

public interface DataLoadingListener {
    void onLoadingFinished();
    void onLoadingProgress(String message);
    void onError(Throwable e);
}
