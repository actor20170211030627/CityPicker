package com.example.citypicker.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description: 类的描述
 * Company    : 重庆市了赢科技有限公司 http://www.liaoin.com/
 * Author     : 李大发
 * Date       : 2019-8-8 on 11:09
 *
 * @version 1.0
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder cityViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
