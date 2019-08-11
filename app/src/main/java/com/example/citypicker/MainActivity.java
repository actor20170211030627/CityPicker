package com.example.citypicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actor.citypicker.bean.CityInfo;
import com.actor.citypicker.utils.CityListLoader;
import com.actor.citypicker.widget.CityPickerBottomDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private int                    titleHeight;//标题栏高度
    private int                    itemHeight;//item高度
    private CityPickerBottomDialog dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CityListLoader.getInstance().init(this);

        titleHeight = getResources().getDimensionPixelSize(R.dimen.title_height_for_city_pick_bottom_sheet_dialog_fragment);
        itemHeight = getResources().getDimensionPixelSize(R.dimen.item_height_for_city_pick_bottom_sheet_dialog_fragment);

        dialogFragment = new CityPickerBottomDialog(this);
        dialogFragment.setTitle("选择地址");
        dialogFragment.setDimAmount(0.5F);
        dialogFragment.setOnSubmitClickListener(new CityPickerBottomDialog.OnSubmitClickListener() {
            @Override
            public void onSubmitClick(CityInfo province, CityInfo.CityListBeanX city,
                                      CityInfo.CityListBeanX.CityListBean district) {
                String address = String.format("省市区:%s-%s-%s", province.name, city.name, district.name);
                Log.e(TAG, "onSubmitClick: address=".concat(address));
                toast(address);
            }
        });
//        dialogFragment.setPeekHeight(titleHeight + itemHeight * 5);
//        dialogFragment.setMaxHeight(titleHeight + itemHeight * 5);

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show();
            }
        });
    }

    private Toast toast;
    private void toast(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else toast.setText(text);
        toast.show();
    }
}
