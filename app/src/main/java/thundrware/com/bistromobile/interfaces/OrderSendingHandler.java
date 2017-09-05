package thundrware.com.bistromobile.interfaces;

public interface OrderSendingHandler {
    void onSuccess();
    void onFailure(Throwable error);
}
