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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                splitStrings = new ArrayList<>(); //소리 재생할 문자들 ('()'로 치환된 것)
                ArrayList<String> tempStrings = new ArrayList<>();//괄호 안에 들어가는 문자들 찾아놓은 거 임시로 보관하는 곳
                //대상 문자열
                String korString = korThaiDicDto.getKor();
                System.out.println("원래 문자들 :: " + korString);

                //정규식 문자열을 컴파일
                String regexr = "\\(.*?\\)";
                Pattern pattern = Pattern.compile(regexr, Pattern.CASE_INSENSITIVE);
                // 검색 결과를 Matcher에 저장
                Matcher matcher = pattern.matcher(korString);

                // 찾을 때마다 처리를 실시
                System.out.println("\n==== 괄호 내부 문자들을 찾을 때마다 ()로 치환");
                StringBuffer replacedString = new StringBuffer();
                int idx = 0;
                while (matcher.find()) {
                    // 찾은 대상을 치환
                    System.out.println(matcher.group() + " => (임시) 로 치환 실행");
                    tempStrings.add(idx, matcher.group());
                    DebugUtil.showDebug("matcher.group() tempStrings에 add 됨 ::" + matcher.group());
                    matcher.appendReplacement(replacedString, "()");
                    DebugUtil.showDebug("tempStrings.get(idx) :: " + tempStrings.get(idx));
                    idx++;
                }
                //찾을 때에 마지막으로 찾은 부분 이후의 검색 대상 문자열을 결합
                matcher.appendTail(replacedString);
                String result3 = replacedString.toString();
                System.out.println("result3 :: " + result3); //치환된 것

                //자르기
                String[] korStrings;
                korStrings = result3.split(",");
                for (int i = 0; i < korStrings.length; i++) {
                    splitStrings.add(korStrings[i]);
                    appendText(itemKorTv, korStrings[i]);
                    ImageSpan imageSpan = appendDrawable(itemKorTv, R.drawable.frag_btn_headset, i);
                    imageSpans.add(imageSpan);
                }

                //잘라진 전체 텍스트 뷰에서 텍스트만 가져오기
                String result4 = itemKorTv.getText().toString();
                DebugUtil.showDebug("text :: " + itemKorTv.getText().toString());

                //붙이기
                System.out.println("\n==== 찾을 때마다 붙이기를 실시");
                Matcher matcher2 = pattern.matcher(result4);
                StringBuffer replacedString2 = new StringBuffer();

                int index = 0;
                while (matcher2.find()) {
                    // 찾은 대상을 치환
                    System.out.println(matcher2.group() + " => 찾을 때마다 붙이기를 실행");
                    matcher2.appendReplacement(replacedString2, tempStrings.get(index));
                    DebugUtil.showDebug("다시 붙은 거 :: " + replacedString2);
                    index++;
                }
                matcher2.appendTail(replacedString2);
                DebugUtil.showDebug("마지막에 붙은 거 :: " + replacedString2);

                //공백으로 구분하여 또 자른다
                String[] strArr = replacedString2.toString().split("  ");
                for (String sss : strArr) {
                    DebugUtil.showDebug("잘라진 것 :: " + sss);
                }

                //이전 내용 없애고
                itemKorTv.setText("");

                //textView에 넣어주기
                String[] addStrings;
                addStrings = replacedString2.toString().split("  ");
                for (int i = 0; i < korStrings.length; i++) {
                    appendText(itemKorTv, addStrings[i]);
                    ImageSpan imageSpan = appendDrawable(itemKorTv, R.drawable.frag_btn_headset, i);
                    imageSpans.add(imageSpan);
                }
                //태국어
            }
            if (!TextUtil.isNull(korThaiDicDto.getThai())) {
//                itemThaiTv.setText(korThaiDicDto.getThai());
                String thaiString = korThaiDicDto.getThai();
                String[] thaiStrings;
                thaiStrings = thaiString.split(",");
                splitStrings_thai = new ArrayList<String>();

                for (String st : thaiStrings) {
                    if (st.contains("(") && st.contains(")")) {
                        st = st.replace(st.substring(st.indexOf("("), st.indexOf(")") + 1), "").trim();
                    }
                    if (st.contains("(")) {
                        st = st.replace(st.substring(st.indexOf("("), st.length()), "").trim();
                    }
                    if (st.contains(")")) {
                        st = st.replace(st.substring(0, st.indexOf(")") + 1), "").trim();
                    }
                    if (!st.equals("")) {
                        splitStrings_thai.add(st);
                    }
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
        String THREE_SPACES = "  ";
        builder.append(THREE_SPACES);
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        final ImageSpan image = new ImageSpan(drawable);

        builder.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            final ArrayList<String> finalSplitStrings = splitStrings;

            @Override
            public void onClick(View widget) {
                DebugUtil.showDebug("DetailItemFrag, 한글 음성이 들어가야할 부분입니다, " + finalSplitStrings.get(index));
                if (GreenKorThaiDicApplication.isKorPronClickable) {
                    speakOutKor(" ," + finalSplitStrings.get(index));
                    DebugUtil.showDebug("한번만, GreenKorThaiDicApplication.isKorPronClickable true여서 발음 나오기 위해 대기 중");

                    GreenKorThaiDicApplication.isKorPronClickable = false;
                    DebugUtil.showDebug("한번만, DetailItemFrag, 한글 음성 들어갈 부분 isKorPronClickable false로 변경 다음 입력 차단을 위해:: " + GreenKorThaiDicApplication.isKorPronClickable);
                }
            }
        }, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (index != splitStrings.size() - 1) {
            builder.append(", ");
        }
        tView.append(builder);
        return image;
    }

    private ImageSpan appendDrawableThai(TextView tView, int drawableId, final int index) {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        String THREE_SPACES = "  ";
        builder.append(THREE_SPACES);
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        final ImageSpan image = new ImageSpan(drawable);

        builder.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            final ArrayList<String> finalSplitStrings = splitStrings_thai;

            @Override
            public void onClick(View widget) {
                DebugUtil.showDebug("DetailItemFrag, 태국 음성이 들어가야할 부분입니다, " + finalSplitStrings.get(index));
                if (GreenKorThaiDicApplication.isThaiPronClickable) {
                    speakOutThai(" ," + finalSplitStrings.get(index));
                    DebugUtil.showDebug("한번만, GreenKorThaiDicApplication.isThaiPronClickable true여서 발음 나오기 위해 대기 중");

                    GreenKorThaiDicApplication.isThaiPronClickable = false;
                    DebugUtil.showDebug("한번만, DetailItemFrag, 태국 음성 들어갈 부분 isThaiPronClickable false로 변경 다음 입력 차단을 위해:: " + GreenKorThaiDicApplication.isThaiPronClickable);
                }

            }
        }, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
