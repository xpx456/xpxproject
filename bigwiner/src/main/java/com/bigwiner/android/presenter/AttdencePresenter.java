package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.handler.AttdenceHandler;
import com.bigwiner.android.receiver.AttdenceReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;
import com.bigwiner.android.view.activity.SourceSelectActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class AttdencePresenter implements Presenter {

    public AttdenceActivity mAttdenceActivity;
    public AttdenceHandler mAttdenceHandler;
    public AttdencePresenter(AttdenceActivity mAttdenceActivity)
    {
        mAttdenceHandler =new AttdenceHandler(mAttdenceActivity);
        this.mAttdenceActivity = mAttdenceActivity;
        mAttdenceActivity.setBaseReceiver(new AttdenceReceiver(mAttdenceHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mAttdenceActivity, Color.argb(0, 255, 255, 255));
        mAttdenceActivity.setContentView(R.layout.activity_attdence);
        mAttdenceActivity.mToolBarHelper.hidToolbar(mAttdenceActivity, (RelativeLayout) mAttdenceActivity.findViewById(R.id.buttomaciton));
        mAttdenceActivity.measureStatubar(mAttdenceActivity, (RelativeLayout) mAttdenceActivity.findViewById(R.id.stutebar));
        mAttdenceActivity.meeting = mAttdenceActivity.getIntent().getParcelableExtra("meeting");

        mAttdenceActivity.back = mAttdenceActivity.findViewById(R.id.back);
        mAttdenceActivity.wantBtn = mAttdenceActivity.findViewById(R.id.wantsrc);
        mAttdenceActivity.myBtn = mAttdenceActivity.findViewById(R.id.mysrc);
        mAttdenceActivity.btnSubmit = mAttdenceActivity.findViewById(R.id.submit_btn);
        mAttdenceActivity.wantvalue = mAttdenceActivity.findViewById(R.id.wantvalue);
        mAttdenceActivity.myvalue = mAttdenceActivity.findViewById(R.id.mytxtvalue);
        mAttdenceActivity.wantBtn.setOnClickListener(mAttdenceActivity.wantListener);
        mAttdenceActivity.myBtn.setOnClickListener(mAttdenceActivity.myListener);
        mAttdenceActivity.back.setOnClickListener(mAttdenceActivity.backListener);
        mAttdenceActivity.btnSubmit.setOnClickListener(mAttdenceActivity.submintListener);
        measureJoinData();
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

    public void doJoinin()
    {
        if(BigwinerApplication.mApp.my.positions.mName.length() == 0) {
            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
            return;
        }
        if(BigwinerApplication.mApp.my.ports.list.size() == 0) {
            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
            return;
        }
        if(BigwinerApplication.mApp.my.businesstypeSelect.list.size() == 0) {
            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
            return;
        }
        if(BigwinerApplication.mApp.my.businessareaSelect.list.size() == 0) {
            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
            return;
        }
        if(BigwinerApplication.mApp.want.positions.mName.length() == 0) {
            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
            return;
        }
        if(BigwinerApplication.mApp.want.ports.list.size() == 0) {
            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
            return;
        }
        if(BigwinerApplication.mApp.want.businessareaSelect.list.size() == 0) {
            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
            return;
        }
        if(BigwinerApplication.mApp.want.businesstypeSelect.list.size() == 0) {
            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
            return;
        }



//        if(BigwinerApplication.mApp.my.positions.mName.length() == 0) {
//            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
//            return;
//        }
//        if(BigwinerApplication.mApp.my.ports.mName.length() == 0) {
//            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
//            return;
//        }
//        if(BigwinerApplication.mApp.my.serices.mName.length() == 0) {
//            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
//            return;
//        }
//        if(BigwinerApplication.mApp.my.routes.mName.length() == 0) {
//            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
//            return;
//        }
//        if(BigwinerApplication.mApp.want.positions.mName.length() == 0) {
//            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
//            return;
//        }
//        if(BigwinerApplication.mApp.want.ports.mName.length() == 0) {
//            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
//            return;
//        }
//        if(BigwinerApplication.mApp.want.serices.mName.length() == 0) {
//            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
//            return;
//        }
//        if(BigwinerApplication.mApp.want.routes.mName.length() == 0) {
//            AppUtils.showMessage(mAttdenceActivity,mAttdenceActivity.getString(R.string.meeting_join_source));
//            return;
//        }
        mAttdenceActivity.waitDialog.show();
        DetialAsks.getMettingsJoin(mAttdenceActivity,mAttdenceHandler,mAttdenceActivity.meeting.recordid);
    }

    public void startSelect(int type) {
        Intent intent = new Intent(mAttdenceActivity, SourceSelectActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("edit",true);
        mAttdenceActivity.startActivity(intent);
    }

    public void measureJoinData() {
        String a1 = "";
        String a2 = "";
        if(BigwinerApplication.mApp.my.positions.mName.length() != 0)
        {
            a1 = addString(a1,BigwinerApplication.mApp.my.positions.mName);
        }
        if(BigwinerApplication.mApp.my.ports.list.size() != 0)
        {
            for(int i = 0 ; i < BigwinerApplication.mApp.my.ports.list.size() ; i++)
            {
                a1 = addString(a1,BigwinerApplication.mApp.my.ports.list.get(i).mName);
            }
        }
        if(BigwinerApplication.mApp.my.businesstypeSelect.list.size() != 0)
        {
            for(int i = 0 ; i < BigwinerApplication.mApp.my.businesstypeSelect.list.size() ; i++)
            {
                a1 = addString(a1,BigwinerApplication.mApp.my.businesstypeSelect.list.get(i).mName);
            }
        }
        if(BigwinerApplication.mApp.my.businesstypeSelect.list.size() != 0)
        {
            for(int i = 0 ; i < BigwinerApplication.mApp.my.businesstypeSelect.list.size() ; i++)
            {
                a1 = addString(a1,BigwinerApplication.mApp.my.businesstypeSelect.list.get(i).mName);
            }
        }
        if(BigwinerApplication.mApp.want.positions.mName.length() != 0)
        {
            a2 = addString(a2,BigwinerApplication.mApp.want.positions.mName);
        }
        if(BigwinerApplication.mApp.want.ports.list.size() != 0)
        {
            for(int i = 0 ; i < BigwinerApplication.mApp.want.ports.list.size() ; i++)
            {
                a2 = addString(a2,BigwinerApplication.mApp.want.ports.list.get(i).mName);
            }
        }
        if(BigwinerApplication.mApp.want.businesstypeSelect.list.size() != 0)
        {
            for(int i = 0 ; i < BigwinerApplication.mApp.want.businesstypeSelect.list.size() ; i++)
            {
                a2 = addString(a2,BigwinerApplication.mApp.want.businesstypeSelect.list.get(i).mName);
            }
        }
        if(BigwinerApplication.mApp.want.businesstypeSelect.list.size() != 0)
        {
            for(int i = 0 ; i < BigwinerApplication.mApp.want.businesstypeSelect.list.size() ; i++)
            {
                a2 = addString(a2,BigwinerApplication.mApp.want.businesstypeSelect.list.get(i).mName);
            }
        }


//        if(BigwinerApplication.mApp.my.positions.mName.length() != 0)
//        {
//            a1 = addString(a1,BigwinerApplication.mApp.my.positions.mName);
//        }
//        if(BigwinerApplication.mApp.my.ports.mName.length() != 0)
//        {
//            a1 = addString(a1,BigwinerApplication.mApp.my.ports.mName);
//        }
//        if(BigwinerApplication.mApp.my.serices.mName.length() != 0)
//        {
//            a1 = addString(a1,BigwinerApplication.mApp.my.serices.mName);
//        }
//        if(BigwinerApplication.mApp.my.routes.mName.length() != 0)
//        {
//            a1 = addString(a1,BigwinerApplication.mApp.my.routes.mName);
//        }
//        if(BigwinerApplication.mApp.want.positions.mName.length() != 0)
//        {
//            a2 = addString(a2,BigwinerApplication.mApp.want.positions.mName);
//        }
//        if(BigwinerApplication.mApp.want.ports.mName.length() != 0)
//        {
//            a2 = addString(a2,BigwinerApplication.mApp.want.ports.mName);
//        }
//        if(BigwinerApplication.mApp.want.serices.mName.length() != 0)
//        {
//            a2 = addString(a2,BigwinerApplication.mApp.want.serices.mName);
//        }
//        if(BigwinerApplication.mApp.want.routes.mName.length() != 0)
//        {
//            a2 = addString(a2,BigwinerApplication.mApp.want.routes.mName);
//        }
        mAttdenceActivity.myvalue.setText(a1);
        mAttdenceActivity.wantvalue.setText(a2);
    }

    public String addString(String a,String add)
    {
        String b= "";
        if(a.length() == 0)
        {
            b = add;
        }
        else
        {
            b = a+","+add;
        }
        return b;
    }

}
