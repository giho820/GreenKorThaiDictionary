package com.ph.greenkorthaidictionary.network.listener;

import com.ph.greenkorthaidictionary.data.network.NetworkResponse;

/**
 * Created by preparkha on 15. 6. 1..
 */
public interface NetworkListener {

    void onNetworkStart(int idx);
    void onNetworkResult(int idx, NetworkResponse json);
    void onNetworkError(int idx, String errorMessage);

}
