package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.BigwinerScanPremissionResult;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.handler.BigwinerPermissionHandler;
import com.bigwiner.android.handler.MeetingDetiallHandler;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.receiver.MeetingDetialReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;
import com.bigwiner.android.view.activity.AttdencePersonActivity;
import com.bigwiner.android.view.activity.CompanyDetialActivity;
import com.bigwiner.android.view.activity.MeetingDetialActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.entity.ShareItem;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.scan.ScanUtils;
import xpx.com.toolbar.utils.ToolBarHelper;
import xpx.map.MapUtils;

/**
 * Created by xpx on 2017/8/18.
 */

public class MeetingDetialPresenter implements Presenter {

    public MeetingDetialActivity mMeetingDetialActivity;
    public MeetingDetiallHandler meetingDetiallHandler;
    public MeetingDetialPresenter(MeetingDetialActivity mMeetingDetialActivity)
    {
        this.mMeetingDetialActivity = mMeetingDetialActivity;
        meetingDetiallHandler  = new MeetingDetiallHandler(mMeetingDetialActivity);
        mMeetingDetialActivity.setBaseReceiver(new MeetingDetialReceiver(meetingDetiallHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {

        ToolBarHelper.setSutColor(mMeetingDetialActivity, Color.argb(0, 255, 255, 255));
        BigwinerApplication.mApp.hisupdata2 = true;
        mMeetingDetialActivity.setContentView(R.layout.activity_meetdetial);
        mMeetingDetialActivity.mToolBarHelper.hidToolbar(mMeetingDetialActivity, (RelativeLayout) mMeetingDetialActivity.findViewById(R.id.buttomaciton));
        mMeetingDetialActivity.measureStatubar(mMeetingDetialActivity, (RelativeLayout) mMeetingDetialActivity.findViewById(R.id.stutebar));
        mMeetingDetialActivity.back = mMeetingDetialActivity.findViewById(R.id.back);
        mMeetingDetialActivity.back.setOnClickListener(mMeetingDetialActivity.backListener);
        mMeetingDetialActivity.share = mMeetingDetialActivity.findViewById(R.id.share);
        mMeetingDetialActivity.meeting = mMeetingDetialActivity.getIntent().getParcelableExtra("meeting");


        //mMeetingDetialActivity.share.setOnClickListener(new BigwinerApplication.DoshareListener(mMeetingDetialActivity,shareItem));
        mMeetingDetialActivity.shade = mMeetingDetialActivity.findViewById(R.id.shade);
        mMeetingDetialActivity.headImg = mMeetingDetialActivity.findViewById(R.id.headimg);
        mMeetingDetialActivity.address = mMeetingDetialActivity.findViewById(R.id.addresslayer);
        mMeetingDetialActivity.titleTxt = mMeetingDetialActivity.findViewById(R.id.title);
        mMeetingDetialActivity.companyName = mMeetingDetialActivity.findViewById(R.id.company);
        mMeetingDetialActivity.prise1Txt = mMeetingDetialActivity.findViewById(R.id.price1);
        mMeetingDetialActivity.prise2Txt = mMeetingDetialActivity.findViewById(R.id.price2);
        mMeetingDetialActivity.timeTxt = mMeetingDetialActivity.findViewById(R.id.timevalue);
        mMeetingDetialActivity.addressTxt = mMeetingDetialActivity.findViewById(R.id.addressvalue);
        mMeetingDetialActivity.personTxt = mMeetingDetialActivity.findViewById(R.id.countvalue);
        mMeetingDetialActivity.desTxt = mMeetingDetialActivity.findViewById(R.id.desvalue);
        mMeetingDetialActivity.btnCheck = mMeetingDetialActivity.findViewById(R.id.btncheck);
        mMeetingDetialActivity.btnContact = mMeetingDetialActivity.findViewById(R.id.btncontact);
        mMeetingDetialActivity.companyList = mMeetingDetialActivity.findViewById(R.id.companylist);
        mMeetingDetialActivity.btnJoin = mMeetingDetialActivity.findViewById(R.id.btnattjoin);
        mMeetingDetialActivity.btnRequest = mMeetingDetialActivity.findViewById(R.id.btnrequest);
        mMeetingDetialActivity.btnSign = mMeetingDetialActivity.findViewById(R.id.btnattdence);
        mMeetingDetialActivity.btnCheck.setOnClickListener(mMeetingDetialActivity.checkLisetner);
        mMeetingDetialActivity.btnContact.setOnClickListener(mMeetingDetialActivity.contactLisetner);
        mMeetingDetialActivity.btnJoin.setOnClickListener(mMeetingDetialActivity.joinLisetner);
        mMeetingDetialActivity.btnSign.setOnClickListener(mMeetingDetialActivity.signLisetner);
        mMeetingDetialActivity.btnRequest.setOnClickListener(mMeetingDetialActivity.requestLisetner);
        mMeetingDetialActivity.address.setOnClickListener(mMeetingDetialActivity.addressListener);
        mMeetingDetialActivity.waitDialog.show();
        DetialAsks.getMettingsDetial(mMeetingDetialActivity,meetingDetiallHandler,mMeetingDetialActivity.meeting);
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

//        mMeetingDetialActivity.headImg.setImageResource(R.drawable.temp1);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.meetingtemp);
        GlideApp.with(mMeetingDetialActivity).load(BigwinerApplication.mApp.measureImg(mMeetingDetialActivity.meeting.photo)).apply(options).into(mMeetingDetialActivity.headImg);
        mMeetingDetialActivity.titleTxt.setText(mMeetingDetialActivity.meeting.name);
        mMeetingDetialActivity.companyName.setText(mMeetingDetialActivity.meeting.contractor);
        mMeetingDetialActivity.prise1Txt.setText(mMeetingDetialActivity.getString(R.string.meeting_zaoniao)+mMeetingDetialActivity.meeting.prise1);
        mMeetingDetialActivity.prise2Txt.setText(mMeetingDetialActivity.getString(R.string.meeting_putong)+mMeetingDetialActivity.meeting.prise2);
        mMeetingDetialActivity.prise2Txt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mMeetingDetialActivity.timeTxt.setText(mMeetingDetialActivity.meeting.timebegin+" - "+mMeetingDetialActivity.meeting.timeend);
        mMeetingDetialActivity.addressTxt.setText(mMeetingDetialActivity.meeting.address);
        mMeetingDetialActivity.desTxt.setText(mMeetingDetialActivity.getString(R.string.meeting_other)+mMeetingDetialActivity.meeting.des);
        mMeetingDetialActivity.personTxt.setText(mMeetingDetialActivity.getString(R.string.meeting_count)
                +String.valueOf(mMeetingDetialActivity.meeting.count)+"/"+String.valueOf(mMeetingDetialActivity.meeting.max));
        if(mMeetingDetialActivity.meeting.isjionin == false && !mMeetingDetialActivity.meeting.stute.equals("未开始") && mMeetingDetialActivity.meeting.stute.length() != 0)
        {
            mMeetingDetialActivity.btnJoin.setEnabled(false);
            mMeetingDetialActivity.btnJoin.setText(mMeetingDetialActivity.meeting.stute);
        }
        else if(mMeetingDetialActivity.meeting.isjionin == false && mMeetingDetialActivity.meeting.stute.equals("未开始"))
        {
            mMeetingDetialActivity.btnJoin.setEnabled(true);
            mMeetingDetialActivity.btnJoin.setText(mMeetingDetialActivity.getString(R.string.conversation_meeting_detial_btn_join));
        }
        else
        {
            mMeetingDetialActivity.btnJoin.setEnabled(false);
            mMeetingDetialActivity.btnJoin.setText(mMeetingDetialActivity.getString(R.string.conversation_joined));
        }


        mMeetingDetialActivity.companyList.removeAllViews();
        for(int i = 0 ; i < mMeetingDetialActivity.meeting.companies.size() ; i++)
        {
            if(i ==  mMeetingDetialActivity.meeting.companies.size()-1)
            praseCompanyListView(mMeetingDetialActivity.meeting.companies.get(i),true);
            else
                praseCompanyListView(mMeetingDetialActivity.meeting.companies.get(i),false);
        }
        ShareItem shareItem = new ShareItem();
        shareItem.picurl = BigwinerApplication.mApp.measureImg(mMeetingDetialActivity.meeting.photo);
        shareItem.title = mMeetingDetialActivity.meeting.name;
        shareItem.des = " ";
        shareItem.weburl = ConversationPrase.prseShareUrl("meeting"
                ,mMeetingDetialActivity.meeting.recordid);
        mMeetingDetialActivity.share.setOnClickListener(new BigwinerApplication.DoshareListener(mMeetingDetialActivity,shareItem));
    }

    public void doShare()
    {

    }

    public void doCheck()
    {
        if(mMeetingDetialActivity.meeting.isjionin)
        {
            Intent intent = new Intent(mMeetingDetialActivity, AttdencePersonActivity.class);
            intent.putExtra("meeting",mMeetingDetialActivity.meeting);
            mMeetingDetialActivity.startActivity(intent);
        }
        else
        {
            AppUtils.showMessage(mMeetingDetialActivity,mMeetingDetialActivity.getString(R.string.meeting_join_first));
        }

    }

    public void doContact()
    {
        AppUtils.checkTel(mMeetingDetialActivity,mMeetingDetialActivity.meeting.phone);
    }

    public void showRequest()
    {
        Intent intent = new Intent(mMeetingDetialActivity, WebMessageActivity.class);
        intent.putExtra("item",mMeetingDetialActivity.meeting);
        intent.putExtra("url",DetialAsks.getRequestUrl(mMeetingDetialActivity.meeting.recordid));
        mMeetingDetialActivity.startActivity(intent);
    }

    public void showScan()
    {
        if(mMeetingDetialActivity.meeting.stute.equals("未开始"))
        {
            AppUtils.showMessage(mMeetingDetialActivity,mMeetingDetialActivity.getString(R.string.meeting_nu_start));
        }
        else if((!mMeetingDetialActivity.meeting.stute.equals("未开始")) && mMeetingDetialActivity.meeting.isjionin == false){
            AppUtils.showMessage(mMeetingDetialActivity,mMeetingDetialActivity.getString(R.string.meeting_join_first));
        }
        else if((!mMeetingDetialActivity.meeting.stute.equals("未开始")) && mMeetingDetialActivity.meeting.issign == true && mMeetingDetialActivity.meeting.isjionin == true){
            AppUtils.showMessage(mMeetingDetialActivity,mMeetingDetialActivity.getString(R.string.meeting_signed));
        }
        else{
            mMeetingDetialActivity.permissionRepuest = ScanUtils.getInstance().checkStartScan(mMeetingDetialActivity,"com.bigwiner.android.view.activity.SignResultActivity"
                    ,new BigwinerPermissionHandler(mMeetingDetialActivity,"com.bigwiner.android.view.activity.SignResultActivity",mMeetingDetialActivity.meeting,mMeetingDetialActivity.getString(R.string.attdence_success))
                    , new BigwinerScanPremissionResult(mMeetingDetialActivity,"com.bigwiner.android.view.activity.SignResultActivity",mMeetingDetialActivity.meeting,mMeetingDetialActivity.getString(R.string.attdence_success)));
        }
    }

    public void startJoin() {
        if(BigwinerApplication.mApp.checkConfirm(mMeetingDetialActivity,mMeetingDetialActivity.getString(R.string.confirm_source_meeting)))
        {
            Intent intent = new Intent(mMeetingDetialActivity, AttdenceActivity.class);
            intent.putExtra("meeting",mMeetingDetialActivity.meeting);
            mMeetingDetialActivity.startActivity(intent);
        }
    }

    private void praseCompanyListView(Company company,boolean hid) {
        View view = mMeetingDetialActivity.getLayoutInflater().inflate(R.layout.company_list_item,null);
        TextView title = view.findViewById(R.id.conversation_title);
        TextView type = view.findViewById(R.id.type);
        view.setTag(company);
        view.setOnClickListener(mMeetingDetialActivity.showCompanyListener);
        ImageView imageView = view.findViewById(R.id.conversation_img);
        title.setText(company.name);
        if(company.jointype.equals(mMeetingDetialActivity.getString(R.string.meeting_sponsor)))
        {
            type.setBackgroundResource(R.drawable.conversation_lable_shape_pink);
        }
        else if(company.jointype.equals(mMeetingDetialActivity.getString(R.string.meeting_contractor)))
        {
            type.setBackgroundResource(R.drawable.conversation_lable_shape_lyellow);
        }
        else
        {
            type.setBackgroundResource(R.drawable.conversation_lable_shape_lgray);
        }
        type.setText(company.jointype);
        RelativeLayout line = view.findViewById(R.id.line);
        if(hid)
            line.setVisibility(View.INVISIBLE);
        else
            line.setVisibility(View.VISIBLE);
//        BigwinerApplication.mApp.setContactHead(mMeetingDetialActivity,company.icon,imageView);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.noticetemp).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (30* mMeetingDetialActivity.mBasePresenter.mScreenDefine.density));
        GlideApp.with(mMeetingDetialActivity).load(ContactsAsks.getCompanyIconUrl(company.id,BigwinerApplication.mApp.contactManager.updataKey))
                .apply(options).into(new MySimpleTarget(imageView));
        mMeetingDetialActivity.companyList.addView(view);
    }

    public void showCompany(Company company) {
        Intent intent = new Intent(mMeetingDetialActivity, CompanyDetialActivity.class);
        intent.putExtra("company",company);
        mMeetingDetialActivity.startActivity(intent);
    }

    public void showmap() {
        if(mMeetingDetialActivity.meeting.address.length() == 0)
        {
            AppUtils.showMessage(mMeetingDetialActivity,mMeetingDetialActivity.getString(R.string.address_empty));
        }
        else
        {
            MapUtils.showMap(mMeetingDetialActivity,mMeetingDetialActivity.shade
                    ,mMeetingDetialActivity.findViewById(R.id.activity_meetdetial)
                    ,mMeetingDetialActivity.meeting.address,mMeetingDetialActivity.getString(R.string.app_name));
        }

    }
}
