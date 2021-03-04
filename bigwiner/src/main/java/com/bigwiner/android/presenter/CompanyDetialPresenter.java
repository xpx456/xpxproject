package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.ViewHelp;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.handler.CompanyDetialHandler;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.receiver.MeetingDetialReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.CompanyDetialActivity;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.PicViewActivity;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.ShareItem;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class CompanyDetialPresenter implements Presenter {

    public CompanyDetialActivity mCompanyDetialActivity;
    public CompanyDetialHandler mCompanyDetialHandler;
    public CompanyDetialPresenter(CompanyDetialActivity mCompanyDetialActivity)
    {
        mCompanyDetialHandler = new CompanyDetialHandler(mCompanyDetialActivity);
        this.mCompanyDetialActivity = mCompanyDetialActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mCompanyDetialActivity, Color.argb(0, 255, 255, 255));
        mCompanyDetialActivity.setContentView(R.layout.activity_company_detial);
        mCompanyDetialActivity.mToolBarHelper.hidToolbar(mCompanyDetialActivity, (RelativeLayout) mCompanyDetialActivity.findViewById(R.id.buttomaciton));
        mCompanyDetialActivity.measureStatubar(mCompanyDetialActivity, (RelativeLayout) mCompanyDetialActivity.findViewById(R.id.stutebar));

        mCompanyDetialActivity.bgImg = mCompanyDetialActivity.findViewById(R.id.bgview);
        mCompanyDetialActivity.company = mCompanyDetialActivity.getIntent().getParcelableExtra("company");
        mCompanyDetialActivity.backBtn = mCompanyDetialActivity.findViewById(R.id.back);
        mCompanyDetialActivity.sharBtn = mCompanyDetialActivity.findViewById(R.id.share);

        mCompanyDetialActivity.mName = mCompanyDetialActivity.findViewById(R.id.name);
        mCompanyDetialActivity.headImg = mCompanyDetialActivity.findViewById(R.id.headimg);
        mCompanyDetialActivity.ctxt1 = mCompanyDetialActivity.findViewById(R.id.cinfirmsubject);
        mCompanyDetialActivity.ctxt2 = mCompanyDetialActivity.findViewById(R.id.cinfirmsubject2);
        mCompanyDetialActivity.cimg1 = mCompanyDetialActivity.findViewById(R.id.confirmimg);
        mCompanyDetialActivity.cimg2 = mCompanyDetialActivity.findViewById(R.id.confirmimg2);
        mCompanyDetialActivity.confirm2 = mCompanyDetialActivity.findViewById(R.id.confirm2);
        mCompanyDetialActivity.count2 = mCompanyDetialActivity.findViewById(R.id.count);
        mCompanyDetialActivity.mTabHeadView = mCompanyDetialActivity.findViewById(intersky.conversation.R.id.head);
        String[] names = new String[5];
        names[0] = mCompanyDetialActivity.getString(R.string.company_detial);
        names[1] = mCompanyDetialActivity.getString(R.string.company_contacts);
        View mView1 = mCompanyDetialActivity.getLayoutInflater().inflate(R.layout.company_detial_page, null);
        mCompanyDetialActivity.city = mView1.findViewById(R.id.cityvalue);
        mCompanyDetialActivity.address = mView1.findViewById(R.id.addressvalue);
        mCompanyDetialActivity.phone = mView1.findViewById(R.id.phonevalue);
        mCompanyDetialActivity.web = mView1.findViewById(R.id.webvalue);
        mCompanyDetialActivity.mail = mView1.findViewById(R.id.mailvalue);
        mCompanyDetialActivity.cahart = mView1.findViewById(R.id.characteristicvalue);
        View mView2 = mCompanyDetialActivity.getLayoutInflater().inflate(R.layout.company_contacts_page, null);
        mCompanyDetialActivity.listView = mView2.findViewById(R.id.contacts);
        mCompanyDetialActivity.listView.setLayoutManager(new LinearLayoutManager(mCompanyDetialActivity));
        mCompanyDetialActivity.contactsAdapter = new ContactsAdapter(mCompanyDetialActivity.company.contacts,mCompanyDetialActivity);
        mCompanyDetialActivity.listView.setAdapter(mCompanyDetialActivity.contactsAdapter);
        mCompanyDetialActivity.contactsAdapter.setOnItemClickListener(mCompanyDetialActivity.contactDetitllistener);
        mCompanyDetialActivity.mViews.add(mView1);
        mCompanyDetialActivity.mViews.add(mView2);
        mCompanyDetialActivity.mViewPager = (NoScrollViewPager) mCompanyDetialActivity.findViewById(intersky.conversation.R.id.load_pager);
        mCompanyDetialActivity.mViewPager.setNoScroll(false);
        mCompanyDetialActivity.mLoderPageAdapter = new ConversationPageAdapter(mCompanyDetialActivity.mViews,names);
        mCompanyDetialActivity.mViewPager.setAdapter(mCompanyDetialActivity.mLoderPageAdapter);
        mCompanyDetialActivity.mViewPager.setCurrentItem(0);
        mCompanyDetialActivity.mTabHeadView.setViewPager(mCompanyDetialActivity.mViewPager);

        mCompanyDetialActivity.stars.add((ImageView) mCompanyDetialActivity.findViewById(R.id.star1));
        mCompanyDetialActivity.stars.add((ImageView) mCompanyDetialActivity.findViewById(R.id.star2));
        mCompanyDetialActivity.stars.add((ImageView) mCompanyDetialActivity.findViewById(R.id.star3));
        mCompanyDetialActivity.stars.add((ImageView) mCompanyDetialActivity.findViewById(R.id.star4));
        mCompanyDetialActivity.stars.add((ImageView) mCompanyDetialActivity.findViewById(R.id.star5));
        mCompanyDetialActivity.backBtn.setOnClickListener(mCompanyDetialActivity.backListener);
        mCompanyDetialActivity.sharBtn.setOnClickListener(mCompanyDetialActivity.doShareListener);
        mCompanyDetialActivity.confirm2.setOnClickListener(mCompanyDetialActivity.confirmListener);
        mCompanyDetialActivity.waitDialog.show();
        ContactsAsks.getCompanyDetial(mCompanyDetialActivity,mCompanyDetialHandler,mCompanyDetialActivity.company);
        ContactsAsks.getCompanyContacts(mCompanyDetialActivity,mCompanyDetialHandler,mCompanyDetialActivity.company
                ,mCompanyDetialActivity.company.moduleDetial.pagesize ,mCompanyDetialActivity.company.moduleDetial.currentpage);
//        initData();
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
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.noticetemp).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (64* mCompanyDetialActivity.mBasePresenter.mScreenDefine.density));
        GlideApp.with(mCompanyDetialActivity).load(ContactsAsks.getCompanyIconUrl(mCompanyDetialActivity.company.id,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mCompanyDetialActivity.headImg));
        RequestOptions options1 = new RequestOptions()
                .placeholder(R.drawable.meetingtemp).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (360*mCompanyDetialActivity.mBasePresenter.mScreenDefine.density), (int) (200 *mCompanyDetialActivity.mBasePresenter.mScreenDefine.density));
        GlideApp.with(mCompanyDetialActivity).load(ContactsAsks.getCompanyBgUrl(mCompanyDetialActivity.company.id,BigwinerApplication.mApp.contactManager.updataKey)).apply(options1).into(new MySimpleTarget(mCompanyDetialActivity.bgImg));
        ViewHelp.praseLeaves(mCompanyDetialActivity.stars,mCompanyDetialActivity.company.leavel);
        mCompanyDetialActivity.mName.setText(mCompanyDetialActivity.company.name);
        if(mCompanyDetialActivity.company.issail.equals(mCompanyDetialActivity.getString(R.string.sail_access)))
        {
            mCompanyDetialActivity.ctxt1.setTextColor( Color.rgb(152,0,1));
            mCompanyDetialActivity.ctxt1.setText(mCompanyDetialActivity.getString(R.string.company_issail)+String.valueOf(mCompanyDetialActivity.company.year)+"年");
            mCompanyDetialActivity.cimg1.setImageResource(R.drawable.confirm);
        }
        else
        {
            mCompanyDetialActivity.ctxt1.setTextColor( Color.rgb(185,185,185));
            mCompanyDetialActivity.ctxt1.setText(mCompanyDetialActivity.getString(R.string.company_nosail));
            mCompanyDetialActivity.cimg1.setImageResource(R.drawable.confirm2);

        }

        if(mCompanyDetialActivity.company.marginamount.length() == 0 || mCompanyDetialActivity.company.marginamount.equals("0")
                || mCompanyDetialActivity.company.issail.length() == 0 ||  !mCompanyDetialActivity.company.issail.equals(mCompanyDetialActivity.getString(R.string.sail_access)))
        {
            mCompanyDetialActivity.ctxt2.setTextColor( Color.rgb(185,185,185));
            mCompanyDetialActivity.cimg2.setImageResource(R.drawable.confirm2);
            mCompanyDetialActivity.count2.setTextColor( Color.rgb(185,185,185));
            mCompanyDetialActivity.count2.setText(mCompanyDetialActivity.company.marginamount);
        }
        else
        {
            mCompanyDetialActivity.ctxt2.setTextColor( Color.rgb(152,0,1));
            mCompanyDetialActivity.cimg2.setImageResource(R.drawable.confirm);
            mCompanyDetialActivity.count2.setTextColor( Color.rgb(152,0,1));
            mCompanyDetialActivity.count2.setText(mCompanyDetialActivity.company.marginamount);
        }

        mCompanyDetialActivity.city.setText(mCompanyDetialActivity.company.city);
        mCompanyDetialActivity.address.setText(mCompanyDetialActivity.company.address);
        mCompanyDetialActivity.phone.setText(mCompanyDetialActivity.company.phone);
        mCompanyDetialActivity.mail.setText(mCompanyDetialActivity.company.mail);
        mCompanyDetialActivity.cahart.setText(mCompanyDetialActivity.company.characteristic);
        mCompanyDetialActivity.web.setText(mCompanyDetialActivity.company.web);
        ShareItem shareItem = new ShareItem();
        shareItem.picurl = ContactsAsks.getCompanyIconUrl(mCompanyDetialActivity.company.id,BigwinerApplication.mApp.contactManager.updataKey);
        shareItem.title = mCompanyDetialActivity.company.name;
        shareItem.des = mCompanyDetialActivity.company.characteristic;
        shareItem.weburl = ConversationPrase.prseShareUrl("company"
                ,mCompanyDetialActivity.company.id);
        mCompanyDetialActivity.sharBtn.setOnClickListener(new BigwinerApplication.DoshareListener(mCompanyDetialActivity,shareItem));
    }

    public void doShare()
    {

    }

    public void showConfrim()
    {
        Intent intent = new Intent(mCompanyDetialActivity, PicViewActivity.class);
        intent.putExtra("id","confirm.jpg");
        intent.putExtra("title",mCompanyDetialActivity.getString(R.string.company_confirm_abuout));
        mCompanyDetialActivity.startActivity(intent);
    }

    public void startContact(Contacts contacts) {
        Intent intent = new Intent(mCompanyDetialActivity, ContactDetialActivity.class);
        intent.putExtra("contacts", contacts);
        mCompanyDetialActivity.startActivity(intent);
    }
}
