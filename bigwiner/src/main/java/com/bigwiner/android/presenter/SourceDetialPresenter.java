package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.handler.SourceDetialHandler;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.SourceDetialActivity;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;


import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.ShareItem;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import mining.app.zxing.decoding.Intents;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceDetialPresenter implements Presenter {

    public SourceDetialActivity mSourceDetialActivity;
    public SourceDetialHandler mSourceDetialHandler;
    public SourceDetialPresenter(SourceDetialActivity mSourceDetialActivity)
    {
        mSourceDetialHandler =new SourceDetialHandler(mSourceDetialActivity);
        this.mSourceDetialActivity = mSourceDetialActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSourceDetialActivity, Color.rgb(255, 255, 255));
        mSourceDetialActivity.setContentView(R.layout.activity_source_detial);
        mSourceDetialActivity.mToolBarHelper.hidToolbar(mSourceDetialActivity, (RelativeLayout) mSourceDetialActivity.findViewById(R.id.buttomaciton));
        mSourceDetialActivity.measureStatubar(mSourceDetialActivity, (RelativeLayout) mSourceDetialActivity.findViewById(R.id.stutebar));
        mSourceDetialActivity.sourceData = mSourceDetialActivity.getIntent().getParcelableExtra("source");
        mSourceDetialActivity.shade = (RelativeLayout) mSourceDetialActivity.findViewById(R.id.shade);
        mSourceDetialActivity.collectbtn = (RelativeLayout) mSourceDetialActivity.findViewById(R.id.contact_btn);
        mSourceDetialActivity.back = mSourceDetialActivity.findViewById(R.id.back);
        mSourceDetialActivity.shareBtn = mSourceDetialActivity.findViewById(R.id.share);
        mSourceDetialActivity.name = mSourceDetialActivity.findViewById(R.id.title);
        mSourceDetialActivity.publictime = mSourceDetialActivity.findViewById(R.id.timevalue);
        mSourceDetialActivity.collect = mSourceDetialActivity.findViewById(R.id.collectcount);
        mSourceDetialActivity.view = mSourceDetialActivity.findViewById(R.id.viewcount);
        mSourceDetialActivity.port = mSourceDetialActivity.findViewById(R.id.portvalue);
        mSourceDetialActivity.area = mSourceDetialActivity.findViewById(R.id.areavalue);
        mSourceDetialActivity.type = mSourceDetialActivity.findViewById(R.id.typevalue);
        mSourceDetialActivity.memo = mSourceDetialActivity.findViewById(R.id.desvalue);
        mSourceDetialActivity.start = mSourceDetialActivity.findViewById(R.id.timestart);
        mSourceDetialActivity.end = mSourceDetialActivity.findViewById(R.id.timeend);
        mSourceDetialActivity.head = mSourceDetialActivity.findViewById(R.id.conversation_img);
        mSourceDetialActivity.username = mSourceDetialActivity.findViewById(R.id.name);
        mSourceDetialActivity.position = mSourceDetialActivity.findViewById(R.id.position);
        mSourceDetialActivity.comapny = mSourceDetialActivity.findViewById(R.id.company);
        mSourceDetialActivity.collecttxt = mSourceDetialActivity.findViewById(R.id.collecttxt);
        mSourceDetialActivity.btnAdd = mSourceDetialActivity.findViewById(R.id.addlayer);
        mSourceDetialActivity.mAddimg = mSourceDetialActivity.findViewById(R.id.add);
        mSourceDetialActivity.contact = mSourceDetialActivity.findViewById(R.id.contact);
        mSourceDetialActivity.collectbtn.setOnClickListener(mSourceDetialActivity.collectListener);
        mSourceDetialActivity.back.setOnClickListener(mSourceDetialActivity.backListener);
        mSourceDetialActivity.contact.setOnClickListener(mSourceDetialActivity.showContactListener);

        mSourceDetialActivity.waitDialog.show();
        SourceAsks.getSourceDetial(mSourceDetialActivity,mSourceDetialHandler,mSourceDetialActivity.sourceData);
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

    public void initData() {

    }

    public void initDetial() {
        mSourceDetialActivity.name.setText(mSourceDetialActivity.sourceData.name);
        mSourceDetialActivity.publictime.setText(mSourceDetialActivity.sourceData.publicetime.substring(0,10));
        mSourceDetialActivity.collect.setText(mSourceDetialActivity.sourceData.collectcount);
        mSourceDetialActivity.view.setText(mSourceDetialActivity.sourceData.viewcount);
        mSourceDetialActivity.port.setText(mSourceDetialActivity.sourceData.port);
        mSourceDetialActivity.area.setText(mSourceDetialActivity.sourceData.area);
        mSourceDetialActivity.type.setText(mSourceDetialActivity.sourceData.type);
        mSourceDetialActivity.memo.setText(mSourceDetialActivity.sourceData.memo);
        mSourceDetialActivity.start.setText(mSourceDetialActivity.sourceData.start.substring(2,4)+"年"
                +mSourceDetialActivity.sourceData.start.substring(5,7)+"月"
                +mSourceDetialActivity.sourceData.start.substring(8,10)+"日");
        mSourceDetialActivity.end.setText(mSourceDetialActivity.sourceData.end.substring(2,4)+"年"+mSourceDetialActivity.sourceData.end.substring(5,7)+"月"
                +mSourceDetialActivity.sourceData.end.substring(8,10)+"日");
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (55* AppActivityManager.getInstance().mScreenDefine.density));
        GlideApp.with(mSourceDetialActivity).load(ContactsAsks.getContactIconUrl(mSourceDetialActivity.sourceData.userid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mSourceDetialActivity.head));
        mSourceDetialActivity.username.setText(mSourceDetialActivity.sourceData.username);
        mSourceDetialActivity.position.setText(mSourceDetialActivity.sourceData.position);
        mSourceDetialActivity.comapny.setText(mSourceDetialActivity.sourceData.company);
        if(mSourceDetialActivity.sourceData.userid.equals(BigwinerApplication.mApp.mAccount.mRecordId))
        {
            mSourceDetialActivity.collectbtn.setVisibility(View.INVISIBLE);
        }
        if(mSourceDetialActivity.sourceData.isfriend == true )
        {
            mSourceDetialActivity.mAddimg.setImageResource(R.drawable.delimg);
            mSourceDetialActivity.btnAdd.setOnClickListener(mSourceDetialActivity.delFriendListener);
        }
        else
        {
            mSourceDetialActivity.mAddimg.setImageResource(R.drawable.addimg);
            mSourceDetialActivity.btnAdd.setOnClickListener(mSourceDetialActivity.addFriendListener);
        }

        if(mSourceDetialActivity.sourceData.iscollcet == 0)
        {
            mSourceDetialActivity.collecttxt.setText(mSourceDetialActivity.getString(R.string.source_collect));
        }
        else
        {
            mSourceDetialActivity.collecttxt.setText(mSourceDetialActivity.getString(R.string.source_collect_cancle));
        }

        ShareItem shareItem = new ShareItem();
        shareItem.title = mSourceDetialActivity.sourceData.name;
        shareItem.des = mSourceDetialActivity.sourceData.memo;
        shareItem.weburl = ConversationPrase.prseShareUrl("resources"
                ,mSourceDetialActivity.sourceData.id);
        mSourceDetialActivity.shareBtn.setOnClickListener(new BigwinerApplication.DoshareListener(mSourceDetialActivity,shareItem));
    }


    public void doCollect(){
        mSourceDetialActivity.waitDialog.show();
        SourceAsks.getSourceCollect(mSourceDetialActivity,mSourceDetialHandler,mSourceDetialActivity.sourceData);
    }

    public void doAddfriend(){
        Contacts contacts = new Contacts();
        contacts.mRecordid = mSourceDetialActivity.sourceData.userid;
        mSourceDetialActivity.waitDialog.show();
        ContactsAsks.getContactAdd(mSourceDetialActivity,mSourceDetialHandler,contacts);
    }

    public void doDelfriend(){
        Contacts contacts = new Contacts();
        contacts.mRecordid = mSourceDetialActivity.sourceData.userid;
        mSourceDetialActivity.waitDialog.show();
        ContactsAsks.getContactDel(mSourceDetialActivity,mSourceDetialHandler,contacts);
    }

    public void showContact(){
        Contacts contacts = new Contacts();
        contacts.mRecordid = mSourceDetialActivity.sourceData.userid;
        Intent intent = new Intent(mSourceDetialActivity, ContactDetialActivity.class);
        intent.putExtra("contacts",contacts);
        mSourceDetialActivity.startActivity(intent);
    }

    public void updataSourceDetial(Intent intent) {
        SourceData sourceData = intent.getParcelableExtra("source");
        if(sourceData.id.equals(mSourceDetialActivity.sourceData))
        {
            mSourceDetialActivity.sourceData.updata(sourceData);
            initDetial();
        }
    }
}
