package com.ph.greenkorthaidictionary;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.ph.greenkorthaidictionary.util.DebugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by preparkha on 15. 6. 15..
 */
public class GreenKorThaiDicApplication extends Application implements TextToSpeech.OnInitListener {

    public static GreenKorThaiDicApplication context;

    //tts
    public static TextToSpeech tts;
    public static TextToSpeech tts_kor;
    public static boolean doesTtsExists = false; //구글 tts 패키지가 설치되어있는지의 여부를 확인

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


    @Override
    public void onInit(int status) {
        DebugUtil.showDebug("DetailItemFrag, onInit()");
        if (status == TextToSpeech.SUCCESS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                GreenKorThaiDicApplication.tts.setEngineByPackageName("com.google.android.tts");
                GreenKorThaiDicApplication.tts.setLanguage(Locale.forLanguageTag("th"));

                GreenKorThaiDicApplication.tts_kor.setEngineByPackageName("com.google.android.tts");
//                GreenKorThaiDicApplication.tts_kor.setLanguage(Locale.KOREAN);
                GreenKorThaiDicApplication.tts_kor.setLanguage(Locale.forLanguageTag("ko"));

                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onDone(String utteranceId) {
                        DebugUtil.showDebug("GreenKorThaiDicApplication, tts is done()");
                    }

                    @Override
                    public void onError(String utteranceId) {
                    }

                    @Override
                    public void onStart(String utteranceId) {
                    }
                });

                tts_kor.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onDone(String utteranceId) {
                        DebugUtil.showDebug("GreenKorThaiDicApplication, tts_kor is done()");
                    }

                    @Override
                    public void onError(String utteranceId) {
                    }

                    @Override
                    public void onStart(String utteranceId) {
                    }
                });

            } else if (status == TextToSpeech.ERROR) {
                DebugUtil.showToast(this, "Sorry! Text To Speech failed...");
                GreenKorThaiDicApplication.tts.setEngineByPackageName("com.google.android.tts");
                GreenKorThaiDicApplication.tts_kor.setEngineByPackageName("com.google.android.tts");
//                tts.setLanguage(Locale.forLanguageTag("th"));

                Locale[] locales = Locale.getAvailableLocales();
                List<Locale> localeList = new ArrayList<>();
                for (Locale locale : locales) {
                    int res = GreenKorThaiDicApplication.tts.isLanguageAvailable(locale);
                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        localeList.add(locale);
                        DebugUtil.showDebug(locale.getLanguage());
                        if (locale.getLanguage().equalsIgnoreCase("th")) {
                            GreenKorThaiDicApplication.tts.setLanguage(locale);
                            DebugUtil.showDebug("MainAct, onInit() th 태국어로 할당 됨");
                        }
                        if (locale.getLanguage().equalsIgnoreCase("kr")) {
                            GreenKorThaiDicApplication.tts_kor.setLanguage(locale);
                            DebugUtil.showDebug("MainAct, onInit() kr 한글로 할당 됨");
                        }
                    }
                }

                //Open Android Text-To-Speech Settings
                startActivity(Build.VERSION.SDK_INT >= 14 ?
                        new Intent().setAction("com.android.settings.TTS_SETTINGS").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) :
                        new Intent().addCategory(Intent.CATEGORY_LAUNCHER).setComponent(new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            } else {
            }

        }

    }
}
