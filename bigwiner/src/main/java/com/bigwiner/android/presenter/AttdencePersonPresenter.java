package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.handler.AttdencePersonHandler;
import com.bigwiner.android.view.activity.AttdencePersonActivity;
import com.bigwiner.android.view.activity.ContactsListActivity;
import com.bigwiner.android.view.activity.MeetingContactsListActivity;
import com.bigwiner.android.view.activity.SourceSelectActivity;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class AttdencePersonPresenter implements Presenter {

    public AttdencePersonActivity mAttdencePersonActivity;
    public AttdencePersonHandler mAttdencePersonHandler;
    public AttdencePersonPresenter(AttdencePersonActivity mAttdencePersonActivity)
    {
        mAttdencePersonHandler =new AttdencePersonHandler(mAttdencePersonActivity);
        this.mAttdencePersonActivity = mAttdencePersonActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mAttdencePersonActivity, Color.argb(0, 255, 255, 255));
        mAttdencePersonActivity.setContentView(R.layout.activity_attdence_person);
        mAttdencePersonActivity.mToolBarHelper.hidToolbar(mAttdencePersonActivity, (RelativeLayout) mAttdencePersonActivity.findViewById(R.id.buttomaciton));
        mAttdencePersonActivity.measureStatubar(mAttdencePersonActivity, (RelativeLayout) mAttdencePersonActivity.findViewById(R.id.stutebar));
        mAttdencePersonActivity.back = mAttdencePersonActivity.findViewById(R.id.back);
        mAttdencePersonActivity.meeting = mAttdencePersonActivity.getIntent().getParcelableExtra("meeting");
        mAttdencePersonActivity.wantBtn = mAttdencePersonActivity.findViewById(R.id.wantsrc);
        mAttdencePersonActivity.myBtn = mAttdencePersonActivity.findViewById(R.id.mysrc);
        mAttdencePersonActivity.personBtn = mAttdencePersonActivity.findViewById(R.id.person);
        mAttdencePersonActivity.chatBtn = mAttdencePersonActivity.findViewById(R.id.chat);
        mAttdencePersonActivity.myBtn.setOnClickListener(mAttdencePersonActivity.myListener);
        mAttdencePersonActivity.wantBtn.setOnClickListener(mAttdencePersonActivity.wantListener);
        mAttdencePersonActivity.personBtn.setOnClickListener(mAttdencePersonActivity.personListener);
        mAttdencePersonActivity.chatBtn.setOnClickListener(mAttdencePersonActivity.chatListener);
        mAttdencePersonActivity.back.setOnClickListener(mAttdencePersonActivity.backListener);
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

    public void startSelect(int type) {
        Intent intent = new Intent(mAttdencePersonActivity, ContactsListActivity.class);
        intent.putExtra("type",type);
        if(type == ContactsListActivity.TYPE_MY)
        intent.putExtra("title",mAttdencePersonActivity.getString(R.string.attdence_mysrc));
        if(type == ContactsListActivity.TYPE_WANT)
            intent.putExtra("title",mAttdencePersonActivity.getString(R.string.attdence_wantsrc));
        if(type == ContactsListActivity.TYPE_ALL)
            intent.putExtra("title",mAttdencePersonActivity.getString(R.string.attdence_p_title));
        intent.putExtra("meeting",mAttdencePersonActivity.meeting);
        mAttdencePersonActivity.startActivity(intent);
    }

    public void showContactList() {
        Intent intent = new Intent(mAttdencePersonActivity, MeetingContactsListActivity.class);
        intent.putExtra("meeting",mAttdencePersonActivity.meeting);
        mAttdencePersonActivity.startActivity(intent);
    }
}
