package com.actor.citypicker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actor.citypicker.R;
import com.actor.citypicker.bean.CityInfo;

import java.util.List;

/**
 * Description: 市的Adapter
 * Copyright  : Copyright (c) 2019
 * Company    : 重庆市了赢科技有限公司 http://www.liaoin.com/
 * Author     : 李大发
 * Date       : 2019/8/10 on 21:15
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    private List<CityInfo.CityListBeanX>      citys;
    private OnItemClickListener listener;
    private int selectedPosition = 0;

    public CityAdapter(List<CityInfo.CityListBeanX> citys, int selectedPosition) {
        this.citys = citys;
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_4_city_pick, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.itemView.setTag(position);
        myViewHolder.itemView.setSelected(position == selectedPosition);
        myViewHolder.tvAddress.setText(citys.get(position).name);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(v, (int) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return citys == null ? 0 : citys.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void notifySelectedItemChanged(int selectedItem) {
        this.selectedPosition = selectedItem;
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvAddress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address_for_city_pick);
        }
    }
}
