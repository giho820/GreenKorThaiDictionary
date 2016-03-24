package com.ph.greenkorthaidictionary.network.controller;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ph.greenkorthaidictionary.network.util.NetworkLruBitmapCache;

/**
 * Created by preparkha on 15. 5. 29..
 */
public class NetworkController {

    public static final String TAG = NetworkController.class.getSimpleName();

    private Context context;

    private static NetworkController mNetworkController;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private NetworkController(Context context) {
        this.context = context;
    }

    public static synchronized NetworkController getInstance(Context context) {
        if (mNetworkController == null)
            mNetworkController = new NetworkController(context);
        return mNetworkController;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(this.context);
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new NetworkLruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
