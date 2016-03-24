package com.ph.greenkorthaidictionary.act;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.KorThaiDicConstantUtil;

public class AppendixContentAct extends ParentAct {

    private TextView titleTv;
    private FrameLayout backFl;
    private FrameLayout settingFl;
    private WebView wv;

    private String urlFirst = "";
    private String urlSecond = "";
    private String urlThird = "";
    private String urlFourth = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appendix_content);

        int typeAppendix = getIntent().getIntExtra("typeAppendix", -1);

        titleTv = (TextView) findViewById(R.id.act_header_title_tv);
        //titleTv.setText(getResources().getString(R.string.act_appendix_title));
        backFl = (FrameLayout) findViewById(R.id.act_header_back_fl);
        backFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        settingFl = (FrameLayout) findViewById(R.id.act_header_setting_fl);
        settingFl.setVisibility(View.GONE);

        wv = (WebView) findViewById(R.id.act_appendix_content_wv);

        initializeUI(typeAppendix, titleTv, wv);
    }

    private void initializeUI(int typeAppendix, TextView titleTv, WebView wv) {

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv.getSettings().setJavaScriptEnabled(true);
        //wv.getSettings().setBuiltInZoomControls(true);
        String filePath = null;

        switch (typeAppendix) {
            case KorThaiDicConstantUtil.TYPE_APPENDIX.CONSONANT_VOWEL:
                DebugUtil.showDebug("자음 모음");
                titleTv.setText(getResources().getText(R.string.act_appendix_title_first));
                filePath = "file:///android_asset/thaidic-publish/files/etc0.html";
                break;
            case KorThaiDicConstantUtil.TYPE_APPENDIX.INTONATION:
                DebugUtil.showDebug("성죠 규칙");
                titleTv.setText(getResources().getText(R.string.act_appendix_title_second));
                filePath = "file:///android_asset/thaidic-publish/files/etc1.html";
                break;
            case KorThaiDicConstantUtil.TYPE_APPENDIX.INTONATION_PRAC:
                DebugUtil.showDebug("자음 모음");
                titleTv.setText(getResources().getText(R.string.act_appendix_title_third));
                filePath = "file:///android_asset/thaidic-publish/files/etc2.html";
                break;
            case KorThaiDicConstantUtil.TYPE_APPENDIX.READ_WRITE_KOR:
                DebugUtil.showDebug("자음 모음");
                titleTv.setText(getResources().getText(R.string.act_appendix_title_fourth));
                filePath = "file:///android_asset/thaidic-publish/files/etc3.html";
                break;
        }
        wv.loadUrl(filePath);
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
