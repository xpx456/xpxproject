package com.exhibition.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.exhibition.R;
import com.exhibition.entity.Guest;
import com.exhibition.view.adapter.QueryListAdapter;

public class BaseSettingView {

    public Context context;
    public View mainView;
    public TextView btnCancle;
    public TextView btnPrint;
    public TextView btnSubmit;

    public EditText name;
    public TextView sex;
    public EditText address;
    public EditText licence;
    public EditText mobil;
    public EditText type;
    public EditText count;

    public EditText items;
    public EditText car;
    public EditText time;
    public EditText card;



    public BaseSettingView(Context context) {
        this.context = context;
    }

    public View creatView() {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_register,null);
        btnCancle = mainView.findViewById(R.id.btn_cancle);
        btnPrint = mainView.findViewById(R.id.btn_print);
        btnSubmit = mainView.findViewById(R.id.btn_submit);

        name = mainView.findViewById(R.id.namevalue);
        sex = mainView.findViewById(R.id.sexvalue);
        address = mainView.findViewById(R.id.addressvalue);
        licence = mainView.findViewById(R.id.licensevalue);
        mobil = mainView.findViewById(R.id.mobilvalue);
        type = mainView.findViewById(R.id.typevalue);
        count = mainView.findViewById(R.id.countvalue);
        items = mainView.findViewById(R.id.itemsvalue);
        car = mainView.findViewById(R.id.carvalue);
        time = mainView.findViewById(R.id.timevalue);
        card = mainView.findViewById(R.id.cardvalue);

        return mainView;
    }

    public void setData(Guest guest) {
    }


    public QueryListAdapter.OnItemClickListener queryItemClickListener = new QueryListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Guest guest, int position, View view) {

        }

    };

    public View.OnClickListener pageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
