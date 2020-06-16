package com.bigwiner.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.handler.SourceCreatHandler;
import com.bigwiner.android.receiver.SourceCreatReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SourceAddListActivity;
import com.bigwiner.android.view.activity.SourceCreatActivity;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Account;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.GlideApp;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.select.SelectManager;
import intersky.select.entity.MapSelect;
import intersky.select.entity.Select;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceCreatPresenter implements Presenter {

    public SourceCreatActivity mSourceCreatActivity;
    public SourceCreatHandler mSourceCreatHandler;
    public SourceCreatPresenter(SourceCreatActivity mSourceCreatActivity)
    {
        mSourceCreatHandler =new SourceCreatHandler(mSourceCreatActivity);
        this.mSourceCreatActivity = mSourceCreatActivity;
        mSourceCreatActivity.setBaseReceiver(new SourceCreatReceiver(mSourceCreatHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
//        ToolBarHelper.setSutColor(mSourceCreatActivity, Color.argb(0, 255, 255, 255));
        mSourceCreatActivity.setContentView(R.layout.activity_source_creat);
        mSourceCreatActivity.mToolBarHelper.hidToolbar2(mSourceCreatActivity);
        ToolBarHelper.setBgColor(mSourceCreatActivity, mSourceCreatActivity.mActionBar, Color.rgb(255, 255, 255));
        mSourceCreatActivity.measureStatubar(mSourceCreatActivity, (RelativeLayout) mSourceCreatActivity.findViewById(R.id.stutebar));
        mSourceCreatActivity.sourceData = mSourceCreatActivity.getIntent().getParcelableExtra("source");
        TextView textView = mSourceCreatActivity.findViewById(R.id.titletext);
        if(mSourceCreatActivity.sourceData.id.length() > 0)
        {
            textView.setText(mSourceCreatActivity.getString(R.string.source_edit_title));
        }
        mSourceCreatActivity.shade = (RelativeLayout) mSourceCreatActivity.findViewById(R.id.shade);
        mSourceCreatActivity.back = mSourceCreatActivity.findViewById(R.id.back);
        mSourceCreatActivity.name = mSourceCreatActivity.findViewById(R.id.namevalue);
        mSourceCreatActivity.portbtn = mSourceCreatActivity.findViewById(R.id.port);
        mSourceCreatActivity.port = mSourceCreatActivity.findViewById(R.id.portvalue);
        mSourceCreatActivity.areabtn = mSourceCreatActivity.findViewById(R.id.area);
        mSourceCreatActivity.area = mSourceCreatActivity.findViewById(R.id.areavalue);
        mSourceCreatActivity.typebtn = mSourceCreatActivity.findViewById(R.id.type);
        mSourceCreatActivity.type = mSourceCreatActivity.findViewById(R.id.typevalue);
        mSourceCreatActivity.memo = mSourceCreatActivity.findViewById(R.id.memovalue);
        mSourceCreatActivity.start = mSourceCreatActivity.findViewById(R.id.startvalue);
        mSourceCreatActivity.end = mSourceCreatActivity.findViewById(R.id.endvalue);
        mSourceCreatActivity.day = mSourceCreatActivity.findViewById(R.id.allvalue);
        mSourceCreatActivity.btnSubmit = mSourceCreatActivity.findViewById(R.id.submit_btn);

        mSourceCreatActivity.port.setOnClickListener(mSourceCreatActivity.portSelectListener);
        mSourceCreatActivity.typebtn.setOnClickListener(mSourceCreatActivity.typeSelectListener);
        mSourceCreatActivity.areabtn.setOnClickListener(mSourceCreatActivity.areaSelectListener);
        mSourceCreatActivity.start.setOnClickListener(mSourceCreatActivity.startListener);
        mSourceCreatActivity.end.setOnClickListener(mSourceCreatActivity.endListener);
        mSourceCreatActivity.back.setOnClickListener(mSourceCreatActivity.backListener);
        mSourceCreatActivity.btnSubmit.setOnClickListener(mSourceCreatActivity.submintListener);
        initData();
    }


    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
    }

    public void setStart() {
        AppUtils.creatDataPicker(mSourceCreatActivity,mSourceCreatActivity.sourceData.start, mSourceCreatActivity.getString(R.string.source_start),mOnBeginSetListener);
    }

    public void setEnd() {
        AppUtils.creatDataPicker(mSourceCreatActivity,mSourceCreatActivity.sourceData.end, mSourceCreatActivity.getString(R.string.source_end),mOnEndSetListener);
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnBeginSetListener = new DoubleDatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d 00:00", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            int count = TimeUtils.measureDayCount4(textString + ":00", mSourceCreatActivity.sourceData.end);
            if (count <= 0) {
                AppUtils.showMessage(mSourceCreatActivity, mSourceCreatActivity.getString(R.string.message_begin_end));
            } else {
                mSourceCreatActivity.sourceData.start = textString+":00";
                mSourceCreatActivity.start.setText(String.format("%02d月%02d日", startMonthOfYear + 1, startDayOfMonth));
                mSourceCreatActivity.day.setText("共"+TimeUtils.measureDayCount4(mSourceCreatActivity.sourceData.start,
                        mSourceCreatActivity.sourceData.end)+"天");
            }
        }
    };

    public DoubleDatePickerDialog.OnDateSetListener mOnEndSetListener = new DoubleDatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d 00:00", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            int count = TimeUtils.measureDayCount4(mSourceCreatActivity.sourceData.start, textString + ":00");
            if (count <= 0) {
                AppUtils.showMessage(mSourceCreatActivity, mSourceCreatActivity.getString(R.string.message_begin_end));
            } else {
                mSourceCreatActivity.sourceData.end = textString+":00";
                mSourceCreatActivity.end.setText(String.format("%02d月%02d日", startMonthOfYear + 1, startDayOfMonth));
                mSourceCreatActivity.day.setText("共"+TimeUtils.measureDayCount4(mSourceCreatActivity.sourceData.start,
                        mSourceCreatActivity.sourceData.end)+"天");
            }
        }
    };

    public void doSubmit() {

        mSourceCreatActivity.waitDialog.show();
        if(mSourceCreatActivity.iscreating == true)
            return;
        mSourceCreatActivity.sourceData.name = mSourceCreatActivity.name.getText().toString();
        mSourceCreatActivity.sourceData.port = mSourceCreatActivity.port.getText().toString();
        if(!mSourceCreatActivity.port.getText().toString().equals(mSourceCreatActivity.getString(R.string.source_port_hit)))
            mSourceCreatActivity.sourceData.port = mSourceCreatActivity.port.getText().toString();
        else
        {
            mSourceCreatActivity.sourceData.area = "";
        }
        if(!mSourceCreatActivity.area.getText().toString().equals(mSourceCreatActivity.getString(R.string.userinfo_area_hit)))
            mSourceCreatActivity.sourceData.area = mSourceCreatActivity.area.getText().toString();
        else
        {
            mSourceCreatActivity.sourceData.area = "";
        }
        if(!mSourceCreatActivity.type.getText().toString().equals(mSourceCreatActivity.getString(R.string.userinfo_type_hit)))
            mSourceCreatActivity.sourceData.type = mSourceCreatActivity.type.getText().toString();
        else
        {
            mSourceCreatActivity.sourceData.type = "";
        }
        mSourceCreatActivity.sourceData.memo = mSourceCreatActivity.memo.getText().toString();
        if(mSourceCreatActivity.sourceData.type.length() == 0 || mSourceCreatActivity.sourceData.area.length() == 0
                || mSourceCreatActivity.sourceData.port.length() == 0 )
        {
            AppUtils.showMessage(mSourceCreatActivity,"请完整填写资料");
            return;
        }
        mSourceCreatActivity.iscreating = true;
        if(mSourceCreatActivity.sourceData.id.length() == 0)
        SourceAsks.getSourceAdd(mSourceCreatActivity,mSourceCreatHandler,mSourceCreatActivity.sourceData);
        else
            SourceAsks.getSourceEdit(mSourceCreatActivity,mSourceCreatHandler,mSourceCreatActivity.sourceData);
    }

    public void initData()
    {
        BigwinerApplication.mApp.ports.reset();
        if(mSourceCreatActivity.sourceData.port.length() > 0)
        BigwinerApplication.mApp.ports.addSelect(mSourceCreatActivity.sourceData.port);
        BigwinerApplication.mApp.businesstypeSelect.reset();
        if(mSourceCreatActivity.sourceData.type.length() > 0)
        BigwinerApplication.mApp.businesstypeSelect.addSelect(mSourceCreatActivity.sourceData.type);
        BigwinerApplication.mApp.businessareaSelect.reset();
        if(mSourceCreatActivity.sourceData.area.length() > 0)
        BigwinerApplication.mApp.businessareaSelect.addSelect(mSourceCreatActivity.sourceData.area);
        if(mSourceCreatActivity.sourceData.id.length() > 0)
        {
            initDetial();
        }
        else
        {
            mSourceCreatActivity.start.setText(
                    mSourceCreatActivity.sourceData.start.substring(5,7)+"月"
                            +mSourceCreatActivity.sourceData.start.substring(8,10)+"日");
            mSourceCreatActivity.end.setText(mSourceCreatActivity.sourceData.end.substring(5,7)+"月"
                    +mSourceCreatActivity.sourceData.end.substring(8,10)+"日");
            mSourceCreatActivity.day.setText("共"+TimeUtils.measureDay(mSourceCreatActivity.sourceData.start,
                    mSourceCreatActivity.sourceData.end)+"天");
        }
    }

    public void initDetial() {
        mSourceCreatActivity.name.setText(mSourceCreatActivity.sourceData.name);
        mSourceCreatActivity.port.setText(mSourceCreatActivity.sourceData.port);
        mSourceCreatActivity.area.setText(mSourceCreatActivity.sourceData.area);
        mSourceCreatActivity.type.setText(mSourceCreatActivity.sourceData.type);
        mSourceCreatActivity.start.setText(
                mSourceCreatActivity.sourceData.start.substring(5,7)+"月"
                +mSourceCreatActivity.sourceData.start.substring(8,10)+"日");
        mSourceCreatActivity.end.setText(mSourceCreatActivity.sourceData.end.substring(5,7)+"月"
                +mSourceCreatActivity.sourceData.end.substring(8,10)+"日");
        mSourceCreatActivity.day.setText("共"+TimeUtils.measureDay(mSourceCreatActivity.sourceData.start,
                mSourceCreatActivity.sourceData.end)+"天");
    }


    public void startSelectPort() {

        BigwinerApplication.mApp.startSelectView(mSourceCreatActivity,BigwinerApplication.mApp.ports,mSourceCreatActivity.getString(R.string.source_port_hit),SourceCreatActivity.ACTION_PORT_SELECT,false,true,1);
    }
    public void startSelectType() {
        BigwinerApplication.mApp.startSelectView(mSourceCreatActivity,BigwinerApplication.mApp.businesstypeSelect,mSourceCreatActivity.getString(R.string.userinfo_type_hit),SourceCreatActivity.ACTION_TYPE_SELECT,false,true,1);
    }
    public void startSelectArea() {
        BigwinerApplication.mApp.startSelectView(mSourceCreatActivity,BigwinerApplication.mApp.businessareaSelect,mSourceCreatActivity.getString(R.string.userinfo_area_hit),SourceCreatActivity.ACTION_AREA_SELECT,false,true,1);
    }



    public void setType(Intent intent)
    {
        ArrayList<Select> selects = intent.getParcelableArrayListExtra("items");
        mSourceCreatActivity.type.setText(SelectManager.praseSelectString(selects));
        BigwinerApplication.mApp.businesstypeSelect.updataSelect(mSourceCreatActivity.type.getText().toString());
        if(mSourceCreatActivity.type.getText().toString().length() == 0)
        {
            mSourceCreatActivity.type.setText(mSourceCreatActivity.getString(R.string.userinfo_type_hit));
        }
    }
    public void setPort(Intent intent)
    {
        ArrayList<Select> selects = intent.getParcelableArrayListExtra("items");
        mSourceCreatActivity.port.setText(SelectManager.praseSelectString(selects));
        BigwinerApplication.mApp.ports.updataSelect(mSourceCreatActivity.port.getText().toString());
        if(mSourceCreatActivity.port.getText().toString().length() == 0)
        {
            mSourceCreatActivity.port.setText(mSourceCreatActivity.getString(R.string.source_port_hit));
        }
    }
    public void setArea(Intent intent)
    {
        ArrayList<Select> selects = intent.getParcelableArrayListExtra("items");
        mSourceCreatActivity.area.setText(SelectManager.praseSelectString(selects));
        BigwinerApplication.mApp.businessareaSelect.updataSelect(mSourceCreatActivity.area.getText().toString());
        if(mSourceCreatActivity.area.getText().toString().length() == 0)
        {
            mSourceCreatActivity.area.setText(mSourceCreatActivity.getString(R.string.userinfo_area_hit));
        }
    }

}
