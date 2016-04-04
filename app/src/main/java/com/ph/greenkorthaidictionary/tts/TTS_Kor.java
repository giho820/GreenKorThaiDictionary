//package com.ph.greenkorthaidictionary.tts;
//
//import android.annotation.SuppressLint;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.speech.tts.TextToSpeech;
//
//import com.ph.greenkorthaidictionary.GreenKorThaiDicApplication;
//import com.ph.greenkorthaidictionary.util.DebugUtil;
//import com.ph.greenkorthaidictionary.util.TextUtil;
//
//import java.util.HashMap;
//import java.util.Locale;
//
///**
// * Created by kiho on 2016. 4. 5..
// */
//public class TTS_Kor extends TextToSpeech implements TextToSpeech.OnInitListener {
//    private Context context;
//    private static TTS_Kor tts_kor;
//    private OnInitListener mOnInitListener;
//    private String mEngine = "com.google.android.tts";
//
//    private TTS_Kor(Context context, OnInitListener listener, String engine) {
//        super(context, listener, engine);
//        this.context = context;
//        this.mOnInitListener = listener;
//        this.mEngine = engine;
//    }
//
//    public TTS_Kor getInstance() {
//        if (tts_kor == null) {
//            InitTTs();
//        }
//        return tts_kor;
//    }
//
//    @Override
//    public void onInit(int status) {
//        if (status == TextToSpeech.SUCCESS) {
//        } else if (status == TextToSpeech.ERROR) {
//            DebugUtil.showToast(context, "Sorry! Text To Speech failed...");
//        }
//
//    }
//
//    public void InitTTs() {
//        //tts 객체 생성하기
//
//        tts_kor = new TTS_Kor(context, this, mEngine);
//        tts_kor.setEngineByPackageName("com.google.android.tts");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            DebugUtil.showDebug("faster::MainAct, >= LOLLI");
//            tts_kor.setLanguage(Locale.KOREAN);
//        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            DebugUtil.showDebug("faster::MainAct, > ICE_CREAM_SANDWICH");
//            Locale local = new Locale("ko");
//            tts_kor.setLanguage(local);
//        } else {
//            //Open Android Text-To-Speech Settings
//            context.startActivity(Build.VERSION.SDK_INT >= 14 ?
//                    new Intent().setAction("com.android.settings.TTS_SETTINGS").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) :
//                    new Intent().addCategory(Intent.CATEGORY_LAUNCHER).setComponent(new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
//    }
//
//
//    @SuppressLint("NewApi")
//    public static void speakOutKor(String ThaiStringToSpeak) {
//        if (GreenKorThaiDicApplication.tts_kor == null) {
//            DebugUtil.showDebug("tts_kor is null");
//            GreenKorThaiDicApplication.tts_kor = new TextToSpeech(GreenKorThaiDicApplication.context, GreenKorThaiDicApplication.context);
//            HashMap<String, String> map = new HashMap<>();
//            map = setTTS_Kor(ThaiStringToSpeak);
//            GreenKorThaiDicApplication.tts_kor.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
//            return;
//        } else if (!TextUtil.isNull(ThaiStringToSpeak)) {
////            HashMap<String, String> map = new HashMap<>();
////            map = setTTS_Kor(ThaiStringToSpeak);
////            GreenKorThaiDicApplication.tts_kor.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                GreenKorThaiDicApplication.tts.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
//            } else {
////                GreenKorThaiDicApplication.tts.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, null);
//                HashMap<String, String> map = new HashMap<>();
////                map = setTTS_Kor(ThaiStringToSpeak);
//                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
//                GreenKorThaiDicApplication.tts_kor.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
//                DebugUtil.showDebug("faster::speak < LOLLI");
//            }
//        }
//    }
//
//
//    public static HashMap<String, String> setTTS_Kor(String _ThaiStringToSpeak) {
//        HashMap<String, String> map = new HashMap<>();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            GreenKorThaiDicApplication.tts_kor.speak(_ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
//            GreenKorThaiDicApplication.tts_kor.setEngineByPackageName("com.google.android.tts");
//            GreenKorThaiDicApplication.tts_kor.setLanguage(Locale.KOREAN);
//
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//
//            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
//            GreenKorThaiDicApplication.tts_kor.setEngineByPackageName("com.google.android.tts");
////                tts.setLanguage(Locale.forLanguageTag("th_TH"));
////            GreenKorThaiDicApplication.tts_kor.setLanguage(Locale.KOREA);
//
//            Locale local = new Locale("ko");
//            GreenKorThaiDicApplication.tts_kor.setLanguage(local);
//            DebugUtil.showDebug("MainAct, setTTS_Kor() 한글로 할당 됨");
//
////            Locale[] locales = Locale.getAvailableLocales();
////            List<Locale> localeList = new ArrayList<>();
////            for (Locale locale : locales) {
////                int res = GreenKorThaiDicApplication.tts_kor.isLanguageAvailable(locale);
////                if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
////                    localeList.add(locale);
////                    DebugUtil.showDebug(locale.getLanguage() +"==="+ locale.getCountry());
////                    if (locale.getLanguage().equalsIgnoreCase("ko")) {
////                        GreenKorThaiDicApplication.tts_kor.setLanguage(locale);
////                        DebugUtil.showDebug("MainAct, setTTS_Kor() 한글로 할당 됨");
////                    }
////                }
////            }
//        }
//        return map;
//    }
//
//}
