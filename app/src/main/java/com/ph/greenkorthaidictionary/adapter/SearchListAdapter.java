package com.ph.greenkorthaidictionary.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.data.dto.KorThaiDicDto;
import com.ph.greenkorthaidictionary.util.TextUtil;

import java.util.List;

/**
 * Created by preparkha on 15. 6. 24..
 */
public class SearchListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<KorThaiDicDto> adapterArrayList;

    public SearchListAdapter(Context context) {
        this.context = context;
    }

    public List<KorThaiDicDto> getAdapterArrayList() {
        return adapterArrayList;
    }

    public void setAdapterArrayList(List<KorThaiDicDto> adapterArrayList) {
        this.adapterArrayList = adapterArrayList;
        this.notifyDataSetChanged();
    }

    public void addItem(KorThaiDicDto korThaiDicDto) {
        if (korThaiDicDto == null)
            return;
        if (this.adapterArrayList != null)
            this.adapterArrayList.add(korThaiDicDto);
        notifyDataSetChanged();
    }

    public void clearAll() {
        if (adapterArrayList != null)
            this.adapterArrayList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (this.adapterArrayList != null && this.adapterArrayList.size() > 0)
            return this.adapterArrayList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (this.adapterArrayList != null && this.adapterArrayList.size() > 0)
            return this.adapterArrayList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        KorThaiDicDto korThaiDicDto;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.pronuTv = (TextView) convertView.findViewById(R.id.item_pronu_tv);
            viewHolder.thaiTv = (TextView) convertView.findViewById(R.id.item_thai_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        korThaiDicDto = adapterArrayList.get(position);

        if(!TextUtil.isNull(korThaiDicDto.getKor()))
            viewHolder.pronuTv.setText(Html.fromHtml(korThaiDicDto.getPronu()));
        if(!TextUtil.isNull(korThaiDicDto.getThai()))
            viewHolder.thaiTv.setText(korThaiDicDto.getThai());

        return convertView;
    }

    public class ViewHolder {
        TextView pronuTv;
        TextView thaiTv;
    }
}
