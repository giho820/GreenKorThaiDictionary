package com.ph.greenkorthaidictionary.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ph.greenkorthaidictionary.data.network.NetworkResponse;
import com.ph.greenkorthaidictionary.network.listener.NetworkListener;
import com.ph.greenkorthaidictionary.network.util.NetworkConstantUtil;

/**
 * Created by preparkha on 15. 6. 10..
 */
public class NetworkHandler extends Handler {

    private int api_idx = 0;
    private NetworkListener networkListener = null;
    private NetworkResponse networkResponse = null;

    public NetworkHandler(int api_idx, NetworkListener networkListener) {
        this.api_idx = api_idx;
        this.networkListener = networkListener;
    }

    public NetworkResponse getNetworkResponse() {
        return networkResponse;
    }

    public void setNetworkResponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    @Override
    public void handleMessage(Message msg) {

        if (msg == null)
            return;

        switch (msg.what) {
            case NetworkConstantUtil.NETWORK.START:
                if (networkListener != null)
                    networkListener.onNetworkStart(api_idx);
                break;
            case NetworkConstantUtil.NETWORK.RESULT:
                if (networkListener != null && networkResponse != null)
                    networkListener.onNetworkResult(api_idx, networkResponse);
                break;
            case NetworkConstantUtil.NETWORK.ERROR:
                String errorMessage = null;
                if (msg != null) {
                    Bundle bundle = msg.getData();
                    errorMessage = bundle.getString(NetworkConstantUtil.KEY_HANDLER_ERROR.HANDLER_HAS_MESSAGE);
                }
                if (networkListener != null)
                    networkListener.onNetworkError(api_idx, errorMessage);
                break;
        }

    }
}
