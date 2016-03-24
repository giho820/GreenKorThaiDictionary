package com.ph.greenkorthaidictionary;

import android.app.Application;

import com.ph.greenkorthaidictionary.util.DebugUtil;

/**
 * Created by preparkha on 15. 6. 15..
 */
public class GreenKorThaiDicApplication extends Application {

    public static GreenKorThaiDicApplication context;

    public GreenKorThaiDicApplication() {
        super();
        this.context = this;
    }

    public static GreenKorThaiDicApplication getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DebugUtil.showDebug("GreenKorThaiDicApplication onCreate()");

    }

}
