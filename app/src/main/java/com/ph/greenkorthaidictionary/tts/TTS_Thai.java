package com.ph.greenkorthaidictionary.tts;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by kiho on 2016. 4. 5..
 */
public class TTS_Thai extends TextToSpeech implements TextToSpeech.OnInitListener {
    private Context context;
    private static TTS_Thai tts_thai;
    private static OnInitListener mOnInitListener;
    private static String mEngine = "com.google.android.tts";

    private TTS_Thai(Context context, OnInitListener listener, String engine) {
        super(context, listener, engine);
        this.context = context;
        this.mOnInitListener = listener;
        this.mEngine = engine;
    }

    public static TTS_Thai getInstance(Context context) {
        if (tts_thai == null) {
            InitTTs(context);
        }
        return tts_thai;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            DebugUtil.showDebug("여기 들어와서 호출해야한다고???");
        } else if (status == TextToSpeech.ERROR) {
            DebugUtil.showToast(context, "Sorry! Text To Speech failed...");
        }

    }

    public static void InitTTs(Context context) {
        //tts 객체 생성하기

        tts_thai = new TTS_Thai(context, mOnInitListener, mEngine);
        tts_thai.setEngineByPackageName("com.google.android.tts");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DebugUtil.showDebug("faster::MainAct, >= LOLLI");
            tts_thai.setLanguage(Locale.forLanguageTag("th"));
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            DebugUtil.showDebug("faster::MainAct, > ICE_CREAM_SANDWICH");
            Locale[] locales = Locale.getAvailableLocales();
            List<Locale> localeList = new ArrayList<>();
            for (Locale locale : locales) {
                int res = tts_thai.isLanguageAvailable(locale);
                if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    localeList.add(locale);
                    DebugUtil.showDebug(locale.getLanguage());
                    if (locale.getLanguage().equalsIgnoreCase("th")) {
                        tts_thai.setLanguage(locale);
                        DebugUtil.showDebug("tts_thai 태국어로 할당 됨");
                    }
                }
            }
//            Locale local = new Locale("th");
//            tts_thai.setLanguage(local);
        } else {
            //Open Android Text-To-Speech Settings
            context.startActivity(Build.VERSION.SDK_INT >= 14 ?
                    new Intent().setAction("com.android.settings.TTS_SETTINGS").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) :
                    new Intent().addCategory(Intent.CATEGORY_LAUNCHER).setComponent(new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    @SuppressLint("NewApi")
    public static void speakOutThai(String ThaiStringToSpeak) {
        if (!TextUtil.isNull(ThaiStringToSpeak)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DebugUtil.showDebug("faster::speak >= LOLLIPOP");
                tts_thai.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                DebugUtil.showDebug("faster::speak < LOLLI");
                HashMap<String, String> map = new HashMap<>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                tts_thai.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
            }

        }
    }


}
