package com.accesscontrol.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accesscontrol.R;
import com.accesscontrol.asks.MqttAsks;
import com.accesscontrol.database.DBHelper;
import com.accesscontrol.entity.AccessRecord;
import com.accesscontrol.entity.Guest;

import intersky.apputils.TimeUtils;
import intersky.mywidget.PopView;


public class SuccessView extends PopView {

    public TextView text;
    public RelativeLayout input;


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
        text = mainView.findViewById(R.id.imf);
        input.setOnClickListener(hidInputListener);
    }

    public void creatView(View location, Guest guest,String mode) {
        updataView(guest,mode);
        super.creatView(location);
    }

    private void updataView(Guest guest,String mode) {
        String time = TimeUtils.getDateAndTime();
        text.setText(context.getString(R.string.success_name_title)+guest.name+"\n"+
                context.getString(R.string.success_time_title)+time+"\n");
        AccessRecord accessRecord = new AccessRecord();
        guest.rid = time;
        accessRecord.empAuthInfoId = guest.rid;
        accessRecord.accessRecordTime = time;
        accessRecord.authModeCode = mode;
        accessRecord.licence = guest.licence;
        accessRecord.name = guest.name;
        accessRecord.entEqptNo = AccessControlApplication.mApp.clidenid;
        if(DBHelper.getInstance(context).addRecord(accessRecord) > 0)
        {
            MqttAsks.uploadRecord(context,accessRecord,AccessControlApplication.mApp.aPublic);
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



