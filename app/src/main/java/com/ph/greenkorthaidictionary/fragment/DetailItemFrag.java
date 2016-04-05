package com.ph.greenkorthaidictionary.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ph.greenkorthaidictionary.GreenKorThaiDicApplication;
import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.ParentFrag;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.data.dto.KorThaiDicDto;
import com.ph.greenkorthaidictionary.listener.FragListener;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;

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

    //tts
    public TextToSpeech ttsThai;
    public TextToSpeech ttsThaiKor;

    ArrayList<String> splitStrings;
    ArrayList<String> splitStrings_thai;

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

    public void setKorThaiDicDto(KorThaiDicDto korThaiDicDto, Context context) {
        this.korThaiDicDto = korThaiDicDto;
        this.context = context;

        if (ttsThai == null) {
            ttsThai = GreenKorThaiDicApplication.getTtsInstance();
        }
        if (ttsThaiKor == null) {
            ttsThaiKor = GreenKorThaiDicApplication.getTtsKorInstance();
        }
    }

    @Nullable
    @Override
    public View getView() {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_detail_item, null);

        itemKorTv = (TextView) view.findViewById(R.id.frag_detail_item_kor_tv);
        itemKorTv.setMovementMethod(LinkMovementMethod.getInstance());

        itemThaiTv = (TextView) view.findViewById(R.id.frag_detail_item_thai_tv);
        itemThaiTv.setMovementMethod(LinkMovementMethod.getInstance());

        itemProunTv = (TextView) view.findViewById(R.id.frag_detail_item_pronu_tv);

        ArrayList<ImageSpan> imageSpans = new ArrayList<ImageSpan>();

        if (korThaiDicDto != null) {
            if (!TextUtil.isNull(korThaiDicDto.getKor())) {
//                itemKorTv.setText(korThaiDicDto.getKor());
                String korString = korThaiDicDto.getKor();
                String[] korStrings;
                korStrings = korString.split(",");
                splitStrings = new ArrayList<String>();

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
                    splitStrings.add(st);
                }

                for (int i = 0; i < splitStrings.size(); i++) {
                    appendText(itemKorTv, splitStrings.get(i));
                    ImageSpan imageSpan = appendDrawable(itemKorTv, R.drawable.frag_btn_headset, i);
                    imageSpans.add(imageSpan);
                }

            }
            if (!TextUtil.isNull(korThaiDicDto.getThai())) {
//                itemThaiTv.setText(korThaiDicDto.getThai());
                String thaiString = korThaiDicDto.getThai();
                String[] thaiStrings;
                thaiStrings = thaiString.split(",");
                splitStrings_thai = new ArrayList<String>();

                for (String st : thaiStrings) {
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
                    splitStrings_thai.add(st);
                }

                for (int i = 0; i < splitStrings_thai.size(); i++) {
                    appendText(itemThaiTv, splitStrings_thai.get(i));
                    ImageSpan imageSpan = appendDrawableThai(itemThaiTv, R.drawable.frag_btn_headset, i);
                    imageSpans.add(imageSpan);
                }

            }
            if (!TextUtil.isNull(korThaiDicDto.getPronu()))
                itemProunTv.setText(Html.fromHtml(korThaiDicDto.getPronu()));
        }
        return view;
    }

    private ImageSpan appendDrawable(TextView tView, int drawableId, final int index) {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        String THREE_SPACES = "   ";
        builder.append(THREE_SPACES);
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        final ImageSpan image = new ImageSpan(drawable);

        builder.setSpan(image, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            final ArrayList<String> finalSplitStrings = splitStrings;

            @Override
            public void onClick(View widget) {
                DebugUtil.showDebug("DetailItemFrag, 한글 음성이 들어가야할 부분입니다, " + finalSplitStrings.get(index));
                speakOutKor(" ," + finalSplitStrings.get(index));
//                GreenKorThaiDicApplication.setTTS_Kor(" ," + finalSplitStrings.get(index));
            }
        }, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (index != splitStrings.size() - 1) {
            builder.append(", ");
        }
        tView.append(builder);
        return image;
    }

    private ImageSpan appendDrawableThai(TextView tView, int drawableId, final int index) {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        String THREE_SPACES = "   ";
        builder.append(THREE_SPACES);
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        final ImageSpan image = new ImageSpan(drawable);

        builder.setSpan(image, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            final ArrayList<String> finalSplitStrings = splitStrings_thai;

            @Override
            public void onClick(View widget) {
                DebugUtil.showDebug("DetailItemFrag, 태국 음성이 들어가야할 부분입니다, " + finalSplitStrings.get(index));
                speakOutThai(" ," + finalSplitStrings.get(index));
            }
        }, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (parentAct != null) {
            parentAct.hideLoading();
        }

        if (index != splitStrings_thai.size() - 1) {
            builder.append(", ");
        }
        tView.append(builder);
        return image;
    }

    private void appendText(TextView tView, String string) {
        tView.append(string);
    }

    @Override
    public void refreshFrag() {

    }

    @SuppressLint("NewApi")
    public void speakOutThai(String ThaiStringToSpeak) {
        HashMap<String, String> map = new HashMap<>();
        if (ttsThai == null) {
            DebugUtil.showDebug("DetailItemFrag, speakOutThai(), tts is null");
            ttsThai = GreenKorThaiDicApplication.getTtsInstance();
            map = GreenKorThaiDicApplication.setTTS();
            ttsThai.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
            return;
        } else {
            if (!TextUtil.isNull(ThaiStringToSpeak)) {
                DebugUtil.showDebug("DetailItemFrag, speakOutThai(), tts is not null");
                map = GreenKorThaiDicApplication.setTTS();
                ttsThai.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
            }
        }
    }


    @SuppressLint("NewApi")
    public void speakOutKor(String ThaiStringToSpeak) {
        HashMap<String, String> map = new HashMap<>();
        if (ttsThaiKor == null) {
            DebugUtil.showDebug("DetailItemFrag, speakOutKor(), tts_kor is null");
            ttsThaiKor = GreenKorThaiDicApplication.getTtsKorInstance();
            map = GreenKorThaiDicApplication.setTTS_Kor();
            ttsThaiKor.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
            return;
        } else {
            if (!TextUtil.isNull(ThaiStringToSpeak)) {
                DebugUtil.showDebug("DetailItemFrag, speakOutKor(), tts_kor is not null");
                map = GreenKorThaiDicApplication.setTTS_Kor();
                ttsThaiKor.speak(ThaiStringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
            }
        }
    }


}
