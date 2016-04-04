package com.ph.greenkorthaidictionary.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.ParentFrag;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.act.MainAct;
import com.ph.greenkorthaidictionary.data.dto.KorThaiDicDto;
import com.ph.greenkorthaidictionary.listener.FragListener;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by preparkha on 15. 6. 24..
 */
public class DetailItemFrag extends ParentFrag {

    // view
    private LayoutInflater inflater;
    private View view;

    // basic info
    private Context context;
    private ParentAct parentAct;
    private FragListener fragListener;

    private KorThaiDicDto korThaiDicDto;

    private TextView itemKorTv;
    private TextView itemThaiTv;
    private TextView itemProunTv;

    private LinearLayout m_vwJokeLayout;

    private ImageView itemKorPronuBtn;
    private ImageView itemThaiPronuBtn;

    private TextToSpeech tts_thai;

    public DetailItemFrag() {

    }

    public DetailItemFrag newInstance(Context context, ParentAct parentAct, FragListener fragListener) {
        DetailItemFrag frag = new DetailItemFrag();
        frag.context = context;
        frag.parentAct = parentAct;
        frag.fragListener = fragListener;

        return frag;
    }

    public KorThaiDicDto getKorThaiDicDto() {
        return korThaiDicDto;
    }

    public void setKorThaiDicDto(KorThaiDicDto korThaiDicDto) {
        this.korThaiDicDto = korThaiDicDto;
    }

    @Nullable
    @Override
    public View getView() {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_detail_item, null);

        itemKorTv = (TextView) view.findViewById(R.id.frag_detail_item_kor_tv);
        itemThaiTv = (TextView) view.findViewById(R.id.frag_detail_item_thai_tv);
        itemProunTv = (TextView) view.findViewById(R.id.frag_detail_item_pronu_tv);

        m_vwJokeLayout = (LinearLayout) view.findViewById(R.id.m_vwJokeLayout);
        itemKorPronuBtn = (ImageView) view.findViewById(R.id.frag_detail_item_kor_pronu_btn);
        itemThaiPronuBtn = (ImageView) view.findViewById(R.id.frag_detail_item_thai_pronu_btn);

        if (korThaiDicDto != null) {
            if (!TextUtil.isNull(korThaiDicDto.getKor()))
                itemKorTv.setText(korThaiDicDto.getKor());
            if (!TextUtil.isNull(korThaiDicDto.getThai()))
                itemThaiTv.setText(korThaiDicDto.getThai());
            if (!TextUtil.isNull(korThaiDicDto.getPronu()))
                itemProunTv.setText(Html.fromHtml(korThaiDicDto.getPronu()));
        }

        //        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
//        tv.setLayoutParams(lparams);
        tv.setText("test1");
        m_vwJokeLayout.addView(tv);

        TextView tv2 = new TextView(context);
//        tv2.setLayoutParams(lparams);
        tv2.setText("test2");
        m_vwJokeLayout.addView(tv2);


        itemKorPronuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String korString = itemKorTv.getText().toString();
                String resultString = "";
                String[] korStrings;
                korStrings = korString.split(",");
                for (String st : korStrings) {
                    DebugUtil.showDebug(st);
                    if (st.contains("(") && st.contains(")")) {
                        st = st.replace(st.substring(st.indexOf("("), st.indexOf(")") + 1), "").trim();
                    }

                    if (st.contains("(")) {
                        st = st.replace(st.substring(st.indexOf("("), st.length()), "").trim();
                    }
                    if (st.contains(")")) {
                        st = st.replace(st.substring(0, st.indexOf(")") + 1), "").trim();
                    }

                    resultString += st + " ,";
//                    if(st.contains("(")){
//                        DebugUtil.showDebug(st.replace(st.substring(st.indexOf("("), st.indexOf(")")), ""));
//                        korString = st.replace(st.substring(st.indexOf("("), st.indexOf(")")+1), "");
//                        resultString += korString +" ,";
//                    } else {
//                        resultString += st;
//                    }

                }
                DebugUtil.showDebug("DetailItemFrag, 한글 음성이 들어가야할 부분입니다, " + resultString);
//                speakOutKor(resultString);
            }
        });

        itemThaiPronuBtn.setOnClickListener(new View.OnClickListener() {
            MainAct.TtsThaiListener ttsThaiListener;
            String thaiStringToSpeak = itemThaiTv.getText().toString();

            public void onClick(View v) {
                DebugUtil.showDebug("DetailItemFrag, 태국어 음성이 들어가야할 부분입니다, " + thaiStringToSpeak);
                if (ttsThaiListener == null) {
                    ttsThaiListener = new MainAct.TtsThaiListener();
                }
                if (tts_thai == null) {
                    tts_thai = new TextToSpeech(context, ttsThaiListener, "com.google.android.tts");
                    HashMap<String, String> map = new HashMap<>();
                    map = setTTS(itemThaiTv.getText().toString());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        DebugUtil.showDebug("faster::MainAct, >= LOLLI");
                        tts_thai.setLanguage(Locale.forLanguageTag("th"));
                    } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tts_thai.setEngineByPackageName("com.google.android.tts");
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

                        tts_thai.speak(thaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
                    } else {
                        //Open Android Text-To-Speech Settings
                        context.startActivity(Build.VERSION.SDK_INT >= 14 ?
                                new Intent().setAction("com.android.settings.TTS_SETTINGS").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) :
                                new Intent().addCategory(Intent.CATEGORY_LAUNCHER).setComponent(new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
                if (ttsThaiListener.status == TextToSpeech.SUCCESS) {
                    tts_thai.speak(itemThaiTv.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        return view;
    }

    public HashMap<String, String> setTTS(String _ThaiStringToSpeak) {
        HashMap<String, String> map = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts_thai.speak(_ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            tts_thai.setEngineByPackageName("com.google.android.tts");
            tts_thai.setLanguage(Locale.forLanguageTag("th"));

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            tts_thai.setEngineByPackageName("com.google.android.tts");
//                tts.setLanguage(Locale.forLanguageTag("th_TH"));

            Locale[] locales = Locale.getAvailableLocales();
            List<Locale> localeList = new ArrayList<>();
            for (Locale locale : locales) {
                int res = tts_thai.isLanguageAvailable(locale);
                if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    localeList.add(locale);
                    DebugUtil.showDebug(locale.getLanguage());
                    if (locale.getLanguage().equalsIgnoreCase("th")) {
                        tts_thai.setLanguage(locale);
                        DebugUtil.showDebug("DetailItemFrag, speakOut() th 태국어로 할당 됨");
                    }
                }
            }
        }
        return map;
    }

    @Override
    public void refreshFrag() {

    }
}
