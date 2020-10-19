package com.restaurant.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.restaurant.R;
import com.restaurant.asks.MqttAsks;
import com.restaurant.database.DBHelper;
import com.restaurant.entity.AccessRecord;
import com.restaurant.entity.Guest;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.PopView;


public class SuccessView extends PopView {

    public RelativeLayout input;
    public TextView user;
    public TextView type;
    public TextView cast;
    public TextView last;


    public SuccessView(Context context) {
        super(context);
        initView();
    }



    @Override
    public void initView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_success, null);
        close = mainView.findViewById(R.id.success);
        input = mainView.findViewById(R.id.input);
        user = mainView.findViewById(R.id.uservalue);
        type = mainView.findViewById(R.id.typevalue);
        cast = mainView.findViewById(R.id.castvalue);
        last = mainView.findViewById(R.id.lastvalue);
        input.setOnClickListener(hidInputListener);
    }

    public void creatView(View location,String[] data)
    {
        user.setText(data[0]);
        last.setText(data[1]);
        cast.setText(data[2]);
        type.setText(data[3]);
        super.creatView(location);
    }

    public void creatView(View location, Guest guest, String mode) {
        updataView(guest,mode);
        super.creatView(location);
    }

    private void updataView(Guest guest,String mode) {
        String time = TimeUtils.getDateAndTime();
        user.setText(guest.name);
        type.setText(mode);

        AccessRecord accessRecord = new AccessRecord();
        accessRecord.empAuthInfoId = guest.rid;
        accessRecord.accessRecordTime = time;
        accessRecord.authModeCode = mode;
        accessRecord.licence = guest.licence;
        accessRecord.name = guest.name;
        accessRecord.entEqptNo = RestaurantApplication.mApp.clidenid;
        if(DBHelper.getInstance(context).addRecord(accessRecord) == 1)
        {
            MqttAsks.uploadRecord(context,accessRecord,RestaurantApplication.mApp.aPublic);
        }
    }

    private View.OnClickListener hidInputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm2.hideSoftInputFromWindow(name.getWindowToken(), 0);

        }
    };
}



