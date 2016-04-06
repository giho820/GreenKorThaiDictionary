package com.ph.greenkorthaidictionary;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.ph.greenkorthaidictionary.util.DebugUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by preparkha on 15. 6. 15..
 */
public class GreenKorThaiDicApplication extends Application implements TextToSpeech.OnInitListener {

    public static GreenKorThaiDicApplication context;

    //tts
    private static TextToSpeech tts;
    private static TextToSpeech tts_kor;
    private static Locale localeThai;
    private static Locale localeKor;

    public static float SPEACH_RATE = 0.6f;

    public static TextToSpeech getTtsInstance() {
        DebugUtil.showDebug("GreenKorThaiDicApplication, getTtsInstance() 호출");
        if (tts == null) {
            tts = new TextToSpeech(context, context,"com.google.android.tts");

        }
        return tts;
    }

    public static TextToSpeech getTtsKorInstance() {
        DebugUtil.showDebug("GreenKorThaiDicApplication, getTtsKorInstance() 호출");
        if (tts_kor == null) {
            tts_kor = new TextToSpeech(context, context,"com.google.android.tts");
        }
        return tts;
    }

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

        tts = getTtsInstance();
        tts_kor = getTtsKorInstance();
    }


    @Override
    public void onInit(int status) {
        DebugUtil.showDebug("GreenKorThaiDicApplication, onInit()");
        if (status == TextToSpeech.SUCCESS) {
            DebugUtil.showDebug("GreenKorThaiDicApplication, onInit(), status == TextToSpeech.SUCCESS");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                GreenKorThaiDicApplication.tts.setEngineByPackageName("com.google.android.tts");
                GreenKorThaiDicApplication.tts.setLanguage(Locale.forLanguageTag("th"));
                //피치 조절 들어갈 부분
                tts.setSpeechRate(SPEACH_RATE);

                GreenKorThaiDicApplication.tts_kor.setEngineByPackageName("com.google.android.tts");
                GreenKorThaiDicApplication.tts_kor.setLanguage(Locale.KOREAN);
                //피치 조절 들어갈 부분
                tts_kor.setSpeechRate(SPEACH_RATE);

                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onDone(String utteranceId) {
                        DebugUtil.showDebug("say hoho done " + utteranceId);
                    }

                    @Override
                    public void onError(String utteranceId) {
                        DebugUtil.showDebug("say hohoho onError" + utteranceId);
                    }

                    @Override
                    public void onStart(String utteranceId) {
                        DebugUtil.showDebug("say hohoho onStart" + utteranceId);
                    }
                });

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

                int listenerResult = tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener()
                {
                    @Override
                    public void onUtteranceCompleted(String utteranceId)
                    {
                        DebugUtil.showDebug("say hoho done 아이스크림 샌드위치 아래 다 내꺼야" + utteranceId);
                    }
                });

//                setTTS();
//                setTTS_Kor();

//                GreenKorThaiDicApplication.tts.setEngineByPackageName("com.google.android.tts");
//                GreenKorThaiDicApplication.tts_kor.setEngineByPackageName("com.google.android.tts");

//                Locale[] locales = Locale.getAvailableLocales();
//                List<Locale> localeList = new ArrayList<>();
//                for (Locale locale : locales) {
//                    int res = GreenKorThaiDicApplication.tts.isLanguageAvailable(locale);
//                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
//                        localeList.add(locale);
//                        DebugUtil.showDebug(locale.getLanguage());
//                        if (locale.getLanguage().equalsIgnoreCase("th")) {
//                            GreenKorThaiDicApplication.tts.setLanguage(locale);
//                            DebugUtil.showDebug("MainAct, onInit() th 태국어로 할당 됨");
//                        }
//                        if (locale.getLanguage().equalsIgnoreCase("ko")) {
//                            GreenKorThaiDicApplication.tts_kor.setLanguage(locale);
//                            DebugUtil.showDebug("MainAct, onInit() kr 한글로 할당 됨");
//                        }
//                    }
//                }
            } else {
                DebugUtil.showToast(this, "Sorry! Text To Speech failed...");
                //Open Android Text-To-Speech Settings
                startActivity(Build.VERSION.SDK_INT >= 14 ?
                        new Intent().setAction("com.android.settings.TTS_SETTINGS").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) :
                        new Intent().addCategory(Intent.CATEGORY_LAUNCHER).setComponent(new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }

        }
    }



    public static HashMap<String, String> setTTS() {
        HashMap<String, String> hashmap = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            GreenKorThaiDicApplication.tts.speak(_ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            GreenKorThaiDicApplication.tts.setEngineByPackageName("com.google.android.tts");
            GreenKorThaiDicApplication.tts.setLanguage(Locale.forLanguageTag("th"));
            //피치 조절 들어갈 부분
            tts.setSpeechRate(SPEACH_RATE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            GreenKorThaiDicApplication.tts.speak(_ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
            hashmap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            GreenKorThaiDicApplication.tts.setEngineByPackageName("com.google.android.tts");

            if(localeThai == null) {
                Locale[] locales = Locale.getAvailableLocales();
                List<Locale> localeList = new ArrayList<>();
                for (Locale locale : locales) {
                    int res = GreenKorThaiDicApplication.tts.isLanguageAvailable(locale);
                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        localeList.add(locale);
                        DebugUtil.showDebug(locale.getLanguage());
                        if (locale.getLanguage().equalsIgnoreCase("th")) {
                            GreenKorThaiDicApplication.tts.setLanguage(locale);
                            localeThai = locale;
                            DebugUtil.showDebug("GreenKorThaiDicApp, setTTS() th 태국어로 할당 됨");
                            break;
                        }
                    }
                }
            } else {
                GreenKorThaiDicApplication.tts.setLanguage(localeThai);
                DebugUtil.showDebug("GreenKorThaiDicApp, setTTS() 이미 Locale 태국어로 할당되어서 저장되어있음");
            }
            //피치 조절 들어갈 부분
            tts.setSpeechRate(SPEACH_RATE);
        }
        return hashmap;
    }

    public static HashMap<String, String> setTTS_Kor() {
        HashMap<String, String> hashmap = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            GreenKorThaiDicApplication.tts_kor.speak(_ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            GreenKorThaiDicApplication.tts_kor.setEngineByPackageName("com.google.android.tts");
            GreenKorThaiDicApplication.tts_kor.setLanguage(Locale.KOREAN);
//피치 조절 들어갈 부분
            tts_kor.setSpeechRate(SPEACH_RATE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            GreenKorThaiDicApplication.tts_kor.speak(_ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
            hashmap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            GreenKorThaiDicApplication.tts_kor.setEngineByPackageName("com.google.android.tts");

            if(localeKor == null) {
                Locale[] locales = Locale.getAvailableLocales();
                List<Locale> localeList = new ArrayList<>();
                for (Locale locale : locales) {
                    int res = GreenKorThaiDicApplication.tts_kor.isLanguageAvailable(locale);
                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        localeList.add(locale);
                        DebugUtil.showDebug(locale.getLanguage());
                        if (locale.getLanguage().equalsIgnoreCase("ko")) {
                            GreenKorThaiDicApplication.tts_kor.setLanguage(locale);
                            localeKor = locale;
                            DebugUtil.showDebug("GreenKorThaiDicApp, setTTS() ko 한글로 할당 됨");
                            break;
                        }
                    }
                }
            } else{
                GreenKorThaiDicApplication.tts_kor.setLanguage(localeKor);
                DebugUtil.showDebug("GreenKorThaiDicApp, setTTS_Kor() 이미 Locale 한글로 할당되어서 저장되어있음");
            }

            //피치 조절 들어갈 부분
            tts_kor.setSpeechRate(SPEACH_RATE);

        }
        return hashmap;
    }
}
