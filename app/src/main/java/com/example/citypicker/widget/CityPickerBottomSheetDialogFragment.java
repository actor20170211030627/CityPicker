package com.example.citypicker.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.citypicker.R;
import com.example.citypicker.bean.CityInfo;
import com.example.citypicker.utils.CityListLoader;

import java.util.List;

/**
 * Description: 类的描述
 * Company    : 重庆市了赢科技有限公司 http://www.liaoin.com/
 * Author     : 李大发
 * Date       : 2019-8-8 on 10:45
 *
 * @version 1.0
 */
public class CityPickerBottomSheetDialogFragment extends BaseBottomSheetDialogFragment {

    private TextView tvTitle;
    private RecyclerView rvProvince;
    private RecyclerView rvCity;
    private RecyclerView rvCounty;
    private CityAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CityAdapter countyAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_city_picker_bottom_sheet_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View v) {

            }
        });
        tvTitle = view.findViewById(R.id.tv_title);
        rvProvince = view.findViewById(R.id.rv_province);
        rvCity = view.findViewById(R.id.rv_city);
        rvCounty = view.findViewById(R.id.rv_county);
        provinceAdapter = new CityAdapter();
        cityAdapter = new CityAdapter();
        countyAdapter = new CityAdapter();
        new LinearSnapHelper().attachToRecyclerView(rvProvince);
        new LinearSnapHelper().attachToRecyclerView(rvCity);
        new LinearSnapHelper().attachToRecyclerView(rvCounty);
        List<CityInfo> citys = CityListLoader.getInstance().getCitys();
    }
}
