package com.exhibition.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.exhibition.R;
import com.exhibition.database.DBHelper;
import com.exhibition.entity.Guest;

import intersky.apputils.AppUtils;

public class RegisterView {

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

    public Guest guest;
    public PopupWindow popupWindow;

    public RegisterView(Context context) {
        this.context = context;
    }

    public RegisterView(Context context,Guest guest) {
        this.context = context;
        this.guest = guest;
    }

    public AppUtils.InitView initView = new AppUtils.InitView() {
        @Override
        public void initView(View view) {
            mainView = view;
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
            setViewData();

            btnSubmit.setOnClickListener(saveListener);
        }
    };


    public void creatView(View location) {
        popupWindow = AppUtils.creatPopView(context,R.layout.view_register,R.id.view_register,location,initView);
    }

    public void hidView() {
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
    }

    public void setData(Guest guest) {
        this.guest = guest;
    }

    public void setViewData()
    {
        if(this.guest != null)
        {
            name.setText(guest.name);
            sex.setText(guest.sex);
            address.setText(guest.address);
            licence.setText(guest.licence);
            type.setText(guest.type);

            mobil.setText(guest.mobil);
            count.setText(guest.count);
            items.setText(guest.items);
            car.setText(guest.car);
            time.setText(guest.time);
            card.setText(guest.card);
        }
    }

    public void mesureViewData()
    {
        if(this.guest == null)
        {
            this.guest = new Guest();
        }
        guest.name = name.getText().toString();
        guest.sex = sex.getText().toString();
        guest.address = address.getText().toString();
        guest.licence = licence.getText().toString();
        guest.type = type.getText().toString();
        guest.mobil = mobil.getText().toString();
        guest.count = count.getText().toString();
        guest.items = items.getText().toString();
        guest.car = car.getText().toString();
        guest.time = time.getText().toString();
        guest.card = card.getText().toString();

    }

    public View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mesureViewData();
            if(guest.rid.length() > 0)
            {
                DBHelper.getInstance(context).addGuest(guest);
            }

        }
    };
}
