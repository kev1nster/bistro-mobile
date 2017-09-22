package thundrware.com.bistromobile.models;

public class KeyActivationDetails {

    private DeviceInfo DeviceInfo;
    private int WaiterId;
    private String Token;

    public thundrware.com.bistromobile.models.DeviceInfo getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(thundrware.com.bistromobile.models.DeviceInfo deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    public int getWaiterId() {
        return WaiterId;
    }

    public void setWaiterId(int waiterId) {
        WaiterId = waiterId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
