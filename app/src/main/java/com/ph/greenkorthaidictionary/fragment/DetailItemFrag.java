package com.ph.greenkorthaidictionary.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.ParentFrag;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.data.dto.KorThaiDicDto;
import com.ph.greenkorthaidictionary.listener.FragListener;
import com.ph.greenkorthaidictionary.util.TextUtil;

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

        if (korThaiDicDto != null) {
            if (!TextUtil.isNull(korThaiDicDto.getKor()))
                itemKorTv.setText(korThaiDicDto.getKor());
            if (!TextUtil.isNull(korThaiDicDto.getThai()))
                itemThaiTv.setText(korThaiDicDto.getThai());
            if (!TextUtil.isNull(korThaiDicDto.getPronu()))
                itemProunTv.setText(Html.fromHtml(korThaiDicDto.getPronu()));
        }

        return view;
    }

    @Override
    public void refreshFrag() {

    }
}
