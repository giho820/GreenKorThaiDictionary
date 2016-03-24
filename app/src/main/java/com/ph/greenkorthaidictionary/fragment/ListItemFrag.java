package com.ph.greenkorthaidictionary.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.ParentFrag;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.adapter.SearchListAdapter;
import com.ph.greenkorthaidictionary.data.dto.KorThaiDicDto;
import com.ph.greenkorthaidictionary.data.frag.FragData;
import com.ph.greenkorthaidictionary.listener.FragListener;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.KorThaiDicConstantUtil;

import java.util.List;

/**
 * Created by preparkha on 15. 6. 24..
 */
public class ListItemFrag extends ParentFrag implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    // view
    private LayoutInflater inflater;
    private View view;

    // basic info
    private Context context;
    private ParentAct parentAct;
    private FragListener fragListener;

    private ListView lv;
    private SearchListAdapter searchListAdapter;
    private List<KorThaiDicDto> korThaiDicDtoList;

    // page
    private boolean lockUpdateListView;

    public ListItemFrag() {

    }

    public ListItemFrag newInstance(Context context, ParentAct parentAct, FragListener fragListener) {
        ListItemFrag frag = new ListItemFrag();
        frag.context = context;
        frag.parentAct = parentAct;
        frag.fragListener = fragListener;
        return frag;
    }

    public void setKorThaiDicDtoList(List<KorThaiDicDto> korThaiDicDtoList) {
        if (korThaiDicDtoList == null)
            return;
        this.korThaiDicDtoList = korThaiDicDtoList;
    }

    public void addKorThaiDicDtoList(List<KorThaiDicDto> korThaiDicDtoList) {

        if (korThaiDicDtoList == null)
            return;

        this.korThaiDicDtoList = korThaiDicDtoList;

        if (this.searchListAdapter != null) {
            //DebugUtil.showDebug("size : " + korThaiDicDtoList.size() + " / " + this.korThaiDicDtoList.size());

            // notifiDataChanged() 가 어디서 일어나는지!!!!! 확인해야한다.
            // adapter 내부적으로 어떻게 프로세스가 돌아가는지도.
            // 여기서 궁금한거는 adapter 안에 있는 data가 새로 갱신이 안되었는데, listview 가 갱신이 왜 되는가 이다.
            // notifyDataSetChanged();

            this.searchListAdapter.setAdapterArrayList(this.korThaiDicDtoList);

        }

        lockUpdateListView = false;
    }

    @Override
    public View getView() {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_list_item, null);

        lv = (ListView) view.findViewById(R.id.frag_lv);
        lv.setOnItemClickListener(this);
        searchListAdapter = new SearchListAdapter(context);
        if (korThaiDicDtoList != null) {
            searchListAdapter.setAdapterArrayList(korThaiDicDtoList);
        }
        lv.setOnScrollListener(this);
        lv.setAdapter(searchListAdapter);

        lockUpdateListView = false;

        return view;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DebugUtil.showDebug("onItemClick / " + korThaiDicDtoList.get(position).getKor());

        FragData fragData = new FragData();
        fragData.setFragType(KorThaiDicConstantUtil.TYPE_FRAG_MAIN.DETAIL_ITEM);
        fragData.setKorThaiDicDto(korThaiDicDtoList.get(position));
        fragListener.onSendFragData(fragData);

    }

    @Override
    public void refreshFrag() {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        DebugUtil.showDebug("onScroll");

        int currentItemCount = firstVisibleItem + visibleItemCount;

        if (visibleItemCount != 0) {
            if (totalItemCount >= 10 && (totalItemCount == currentItemCount) && !lockUpdateListView) {
                FragData fragData = new FragData();
                fragData.setFragType(KorThaiDicConstantUtil.TYPE_FRAG_MAIN.UPDATE_LIST_ITEM);
                fragListener.onSendFragData(fragData);
                lockUpdateListView = true;
            }
        }
    }

}
