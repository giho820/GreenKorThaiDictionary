package com.ph.greenkorthaidictionary.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.MoveActUtil;
import com.ph.greenkorthaidictionary.util.TextUtil;

public class SettingAct extends ParentAct {

    private TextView titleTv;
    private FrameLayout backFl;
    private FrameLayout settingFl;

    // control apk version

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        titleTv = (TextView) findViewById(R.id.act_header_title_tv);
        titleTv.setText(getResources().getString(R.string.act_header_title_setting));
        backFl = (FrameLayout) findViewById(R.id.act_header_back_fl);
        backFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        settingFl = (FrameLayout) findViewById(R.id.act_header_setting_fl);
        settingFl.setVisibility(View.GONE);
    }

    // about onClick

    public void onClickAppendix(View v) {
        MoveActUtil.chageActivity(this, AppendixAct.class, R.anim.right_in, R.anim.right_out, false, true);
    }

    public void onClickApp(View v) {

        String uri = null;
        switch (v.getId()) {
            case R.id.act_setting_red_btn:
                DebugUtil.showDebug("고급 태한 사전 app");
                uri = "market://details?id="+"com.ph.redkorthaidictionary";
                break;
            case R.id.act_setting_blue_btn:
                DebugUtil.showDebug("고급 한태 사전 app");
                uri = "market://details?id=" + "com.ph.korthaidictionary";
                break;
        }
        if(!TextUtil.isNull(uri)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

}
