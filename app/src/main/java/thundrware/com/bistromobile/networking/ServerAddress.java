package thundrware.com.bistromobile.networking;

import thundrware.com.bistromobile.utils.StringUtils;

public class ServerAddress {

    private String mAddress;
    private String mPort;

    public String getPort() {
        return mPort;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setPort(String port) {
        if (!StringUtils.isNullOrEmpty(port)) {
            mPort = port;
        }
    }

    public void setAddress(String address) {
        if (!StringUtils.isNullOrEmpty(address)) {
            mAddress = address;
        }
    }

    @Override
    public String toString() {
        if (StringUtils.isNullOrEmpty(mPort)) {
            return String.format("http://%s", mAddress);
        } else {
            return String.format("http://%s:%d", mAddress, mPort);
        }
    }
}
