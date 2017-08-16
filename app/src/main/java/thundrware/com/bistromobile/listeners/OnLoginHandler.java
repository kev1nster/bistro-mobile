package thundrware.com.bistromobile.listeners;

public interface OnLoginHandler {
    void onSuccess();
    void onError(Throwable e);
}
