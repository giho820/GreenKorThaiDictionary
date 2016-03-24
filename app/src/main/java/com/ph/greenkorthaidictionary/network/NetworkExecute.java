package com.ph.greenkorthaidictionary.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.android.volley.toolbox.StringRequest;
import com.ph.greenkorthaidictionary.network.controller.NetworkController;
import com.ph.greenkorthaidictionary.network.listener.NetworkListener;
import com.ph.greenkorthaidictionary.network.util.NetworkConstantUtil;
import com.ph.greenkorthaidictionary.network.util.NetworkRequestUtil;
import com.ph.greenkorthaidictionary.util.DebugUtil;

import java.util.HashMap;

/**
 * Created by preparkha on 15. 6. 21..
 */
public class NetworkExecute extends NetworkRequestUtil {

    private Context context = null;

    private int api_idx = -1;
    private String url = null;
    private HashMap<String, String> params = null;
    private NetworkListener networkListener = null;
    private NetworkHandler handler;


    public NetworkExecute() {
    }

    public NetworkExecute(Context context) {
        this.context = context;
    }

    public void setInfoNetwork(int api_idx, String url, HashMap<String, String> params, NetworkListener networkListener) {
        this.api_idx = api_idx;
        this.url = url;
        this.params = params;
        this.networkListener = networkListener;
        handler = new NetworkHandler(this.api_idx, this.networkListener);
        DebugUtil.showDebug(this.toString());
    }

    public void executeNetwork() {
        if (api_idx == -1 || url == null) {
            return;
        }

        if (networkListener == null) {
            return;
        }

        if (context == null) {
            Bundle bundle = new Bundle();
            //bundle.putString(NetworkConstantUtil.KEY_HANDLER_ERROR.HANDLER_HAS_MESSAGE, KorThaiDicConstantUtil.MSG_INTERNAL_ERROR._1_CONTEXT_IS_NULL);
            Message message = Message.obtain(handler, NetworkConstantUtil.NETWORK.ERROR);
            message.setData(bundle);
            handler.sendMessage(message);
            return;
        }

        StringRequest stringRequest = null;

        switch (api_idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                stringRequest = requestGET(this.api_idx, this.url, this.params, this.handler, this.networkListener);
//                stringRequest = requesetPost(this.api_idx, this.url, this.params, this.handler, this.networkListener);
                break;
            default:
                break;
        }

        if (stringRequest == null)
            return;

        //MultipartRequest multipartRequest = new MultipartRequest();

        NetworkController.getInstance(this.context).addToRequestQueue(stringRequest);
    }

    @Override
    public String toString() {

        return "NetworkExecute{" +
                " api_idx=" + api_idx +
                ", url='" + url +
                '}';
    }
}
