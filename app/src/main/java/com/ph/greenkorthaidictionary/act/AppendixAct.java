package com.ph.greenkorthaidictionary.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.KorThaiDicConstantUtil;
import com.ph.greenkorthaidictionary.util.MoveActUtil;

public class AppendixAct extends ParentAct {

    private TextView titleTv;
    private FrameLayout backFl;
    private FrameLayout settingFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appendix);

        titleTv = (TextView) findViewById(R.id.act_header_title_tv);
        titleTv.setText(getResources().getString(R.string.act_appendix_title));
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

    public void onClickAppendixContent(View v) {

        Intent intent = new Intent(this, AppendixContentAct.class);

        switch (v.getId()) {
            case R.id.act_appendix_title_first_fl:
                DebugUtil.showDebug("first");
                intent.putExtra("typeAppendix", KorThaiDicConstantUtil.TYPE_APPENDIX.CONSONANT_VOWEL);;
                break;
            case R.id.act_appendix_title_second_fl:
                DebugUtil.showDebug("second");
                intent.putExtra("typeAppendix", KorThaiDicConstantUtil.TYPE_APPENDIX.INTONATION);;
                break;
            case R.id.act_appendix_title_thrid_fl:
                DebugUtil.showDebug("third");
                intent.putExtra("typeAppendix", KorThaiDicConstantUtil.TYPE_APPENDIX.INTONATION_PRAC);;
                break;
            case R.id.act_appendix_title_fourth_fl:
                DebugUtil.showDebug("fourth");
                intent.putExtra("typeAppendix", KorThaiDicConstantUtil.TYPE_APPENDIX.READ_WRITE_KOR);;
                break;

        }

        MoveActUtil.moveActivity(this, intent, R.anim.right_in, R.anim.right_out, false, true);
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

}
