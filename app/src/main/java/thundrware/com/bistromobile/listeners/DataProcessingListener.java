package thundrware.com.bistromobile.listeners;

public interface DataProcessingListener {
    void onProcessFinished();
    void onProcessStatusUpdate(String message);
}
