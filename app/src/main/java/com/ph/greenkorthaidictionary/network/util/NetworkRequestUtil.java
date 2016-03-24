package com.ph.greenkorthaidictionary.network.util;

import android.os.Bundle;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.ph.greenkorthaidictionary.data.network.NetworkResponse;
import com.ph.greenkorthaidictionary.network.NetworkHandler;
import com.ph.greenkorthaidictionary.network.listener.NetworkListener;
import com.ph.greenkorthaidictionary.network.request.MultipartRequest;
import com.ph.greenkorthaidictionary.util.DebugUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by preparkha on 15. 6. 1..
 */
public class NetworkRequestUtil {

    private static NetworkHandler handler = null;
    private NetworkResponse networkResponse = null;
    private HashMap<String, String> requestParams = null;

    /*************************
     * request GET method
     *************************/

    /**
     * to request url that is using GET Method
     *
     * @param requestParams
     * @return
     */
    public String createParams(HashMap<String, String> requestParams) {

        if (requestParams == null)
            return "";

        int index = 0;
        String params = null;
        StringBuilder builder = new StringBuilder();

        String key = null;
        String value = null;
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            key = null;
            value = null;

            key = entry.getKey();
            value = entry.getValue();

            if (index == 0)
                builder.append("?");
            else
                builder.append("&");

            DebugUtil.showDebug("key::" + key + "======" + "value::" + value);
            builder.append(key);
            builder.append("=");
            builder.append(value);
            index++;

        }
        params = builder.toString();
        builder.delete(0, builder.capacity());
        builder = null;
        return params;
    }

    /**
     * @param api_idx
     * @param url
     * @param params
     * @param handler
     * @param networkListener
     * @return
     */
    public StringRequest requestGET(int api_idx, String url, HashMap<String, String> params, NetworkHandler handler, final NetworkListener networkListener) {

        StringRequest stringRequest = null;
        this.requestParams = params;
        NetworkRequestUtil.handler = handler;

        if (networkListener == null) {
            Bundle bundle = new Bundle();
            //bundle.putString(NetworkConstantUtil.KEY_HANDLER_ERROR.HANDLER_HAS_MESSAGE, KorThaiDicConstantUtil.MSG_INTERNAL_ERROR._3_NETWORK_LISTENER_IS_NULL);
            Message message = Message.obtain(handler, NetworkConstantUtil.NETWORK.ERROR);
            message.setData(bundle);
            handler.sendMessage(message);
            return stringRequest;
        }

        if (params != null) {
            url += createParams(this.requestParams);
        }

        handler.sendMessage(Message.obtain(handler, NetworkConstantUtil.NETWORK.START));

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                networkResponse = gson.fromJson(response, NetworkResponse.class);
                NetworkRequestUtil.handler.setNetworkResponse(networkResponse);
                NetworkRequestUtil.handler.sendMessage(Message.obtain(NetworkRequestUtil.handler, NetworkConstantUtil.NETWORK.RESULT));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Bundle bundle = new Bundle();
                bundle.putString(NetworkConstantUtil.KEY_HANDLER_ERROR.HANDLER_HAS_MESSAGE, volleyError.toString());
                Message message = Message.obtain(NetworkRequestUtil.handler, NetworkConstantUtil.NETWORK.ERROR);
                message.setData(bundle);
                NetworkRequestUtil.handler.sendMessage(message);
            }
        });

        return stringRequest;
    }


    /**
     * **********************
     * request POST method
     * ***********************
     */

    /**
     * @param api_idx
     * @param url
     * @param params
     * @param handler
     * @param networkListener
     * @return
     */
    public StringRequest requesetPost(int api_idx, String url, HashMap<String, String> params, NetworkHandler handler, final NetworkListener networkListener) {

        StringRequest stringRequest = null;
        this.requestParams = params;
        NetworkRequestUtil.handler = handler;

        if (networkListener == null) {
            Bundle bundle = new Bundle();
            // bundle.putString(NetworkConstantUtil.KEY_HANDLER_ERROR.HANDLER_HAS_MESSAGE, KorThaiDicConstantUtil.MSG_INTERNAL_ERROR._3_NETWORK_LISTENER_IS_NULL);
            Message message = Message.obtain(handler, NetworkConstantUtil.NETWORK.ERROR);
            message.setData(bundle);
            handler.sendMessage(message);
            return stringRequest;
        }

        handler.sendMessage(Message.obtain(handler, NetworkConstantUtil.NETWORK.START));

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                networkResponse = gson.fromJson(response, NetworkResponse.class);
                NetworkRequestUtil.handler.setNetworkResponse(networkResponse);
                NetworkRequestUtil.handler.sendMessage(Message.obtain(NetworkRequestUtil.handler, NetworkConstantUtil.NETWORK.RESULT));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Bundle bundle = new Bundle();
                bundle.putString(NetworkConstantUtil.KEY_HANDLER_ERROR.HANDLER_HAS_MESSAGE, volleyError.toString());
                Message message = Message.obtain(NetworkRequestUtil.handler, NetworkConstantUtil.NETWORK.ERROR);
                message.setData(bundle);
                NetworkRequestUtil.handler.sendMessage(message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> tempParams = requestParams;
                return tempParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> tempParams = new HashMap<String, String>();
                tempParams.put("Content-Type", "application/x-www-form-urlencoded");
                return tempParams;
            }
        };

        return stringRequest;
    }

    public MultipartRequest requestUploadFile(int api_idx, String url, String filePath, NetworkHandler handler, final NetworkListener networkListener) {

        MultipartRequest multipartRequest = null;

        if (networkListener == null) {
            Bundle bundle = new Bundle();
            // bundle.putString(NetworkConstantUtil.KEY_HANDLER_ERROR.HANDLER_HAS_MESSAGE, KorThaiDicConstantUtil.MSG_INTERNAL_ERROR._3_NETWORK_LISTENER_IS_NULL);
            Message message = Message.obtain(handler, NetworkConstantUtil.NETWORK.ERROR);
            message.setData(bundle);
            handler.sendMessage(message);
            return multipartRequest;
        }

        handler.sendMessage(Message.obtain(handler, NetworkConstantUtil.NETWORK.START));

        String fileName = "/storage/sdcard0/DCIM/Camera/20150602_153552.jpg";
        File file = new File(fileName);
        String routeId = "p";

//        multipartRequest = new MultipartRequest(url, file, routeId, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String respnose) {
//                DebugUtil.showDebug("what?");
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });

        return multipartRequest;
    }

}
