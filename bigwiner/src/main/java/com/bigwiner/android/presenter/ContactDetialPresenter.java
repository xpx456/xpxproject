package com.bigwiner.android.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.ViewHelp;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.handler.ContactsDetialHandler;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.activity.CompanyDetialActivity;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.activity.SailApplyActivity;
import com.bigwiner.android.view.activity.SettingActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.entity.ShareItem;
import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ContactDetialPresenter implements Presenter {

    public ContactDetialActivity mContactDetialActivity;
    public ContactsDetialHandler contactsDetialHandler;
    public ContactDetialPresenter(ContactDetialActivity mContactDetialActivity)
    {
        contactsDetialHandler = new ContactsDetialHandler(mContactDetialActivity);
        this.mContactDetialActivity = mContactDetialActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mContactDetialActivity, Color.argb(0, 255, 255, 255));
        mContactDetialActivity.setContentView(R.layout.activity_contacts_detial);
        mContactDetialActivity.shade = (RelativeLayout) mContactDetialActivity.findViewById(R.id.shade);
        mContactDetialActivity.complaint = mContactDetialActivity.findViewById(R.id.startxtset);
        mContactDetialActivity.mToolBarHelper.hidToolbar(mContactDetialActivity, (RelativeLayout) mContactDetialActivity.findViewById(R.id.buttomaciton));
        mContactDetialActivity.measureStatubar(mContactDetialActivity, (RelativeLayout) mContactDetialActivity.findViewById(R.id.stutebar));
        mContactDetialActivity.bgImg = mContactDetialActivity.findViewById(R.id.bgview);
        mContactDetialActivity.contacts = mContactDetialActivity.getIntent().getParcelableExtra("contacts");
        mContactDetialActivity.company = BigwinerApplication.mApp.hashCompany.get(mContactDetialActivity.contacts.companyid);
        mContactDetialActivity.backBtn = mContactDetialActivity.findViewById(R.id.back);
        mContactDetialActivity.sharBtn = mContactDetialActivity.findViewById(R.id.setting);
        mContactDetialActivity.complaint = mContactDetialActivity.findViewById(R.id.startxtset);
        mContactDetialActivity.headImg = mContactDetialActivity.findViewById(R.id.headimg);
        mContactDetialActivity.sexImg = mContactDetialActivity.findViewById(R.id.sex);
        mContactDetialActivity.stars.add((ImageView) mContactDetialActivity.findViewById(R.id.star1));
        mContactDetialActivity.stars.add((ImageView) mContactDetialActivity.findViewById(R.id.star2));
        mContactDetialActivity.stars.add((ImageView) mContactDetialActivity.findViewById(R.id.star3));
        mContactDetialActivity.stars.add((ImageView) mContactDetialActivity.findViewById(R.id.star4));
        mContactDetialActivity.stars.add((ImageView) mContactDetialActivity.findViewById(R.id.star5));
        mContactDetialActivity.mName = mContactDetialActivity.findViewById(R.id.name);
        mContactDetialActivity.addBtn = mContactDetialActivity.findViewById(R.id.addlayer);
        if(mContactDetialActivity.contacts.mRecordid.equals(BigwinerApplication.mApp.mAccount.mRecordId))
        {
            mContactDetialActivity.addBtn.setVisibility(View.INVISIBLE);
        }
        mContactDetialActivity.mAdd = mContactDetialActivity.findViewById(R.id.addtxt);
        mContactDetialActivity.vip = mContactDetialActivity.findViewById(R.id.vip);
        mContactDetialActivity.mAddimg = mContactDetialActivity.findViewById(R.id.add);
        mContactDetialActivity.chatBtn = mContactDetialActivity.findViewById(R.id.chatlayer);
        mContactDetialActivity.itemLayers = mContactDetialActivity.findViewById(R.id.lable);
        mContactDetialActivity.desTxt = mContactDetialActivity.findViewById(R.id.des);
        mContactDetialActivity.typeValue = mContactDetialActivity.findViewById(R.id.typevalue);
        mContactDetialActivity.areaValue = mContactDetialActivity.findViewById(R.id.areavalue);
        mContactDetialActivity.mailValue = mContactDetialActivity.findViewById(R.id.mailvalue);
        mContactDetialActivity.mobil = mContactDetialActivity.findViewById(R.id.mobiltxt);
        mContactDetialActivity.companyValue = mContactDetialActivity.findViewById(R.id.companyvalue);
        mContactDetialActivity.addressValue = mContactDetialActivity.findViewById(R.id.addressvalue);
        mContactDetialActivity.dertyValue = mContactDetialActivity.findViewById(R.id.dertyvalue);
        mContactDetialActivity.companyBtn = mContactDetialActivity.findViewById(R.id.detialbtn);
        mContactDetialActivity.companyBtn.setVisibility(View.INVISIBLE);
        mContactDetialActivity.waitDialog.show();
        ContactsAsks.getContactDetial(mContactDetialActivity,contactsDetialHandler,mContactDetialActivity.contacts);
//        initData();
        mContactDetialActivity.backBtn.setOnClickListener(mContactDetialActivity.backListener);
        //mContactDetialActivity.sharBtn.setOnClickListener(new BigwinerApplication.DoshareListener(mContactDetialActivity));
        mContactDetialActivity.chatBtn.setOnClickListener(mContactDetialActivity.startChatListener);
        mContactDetialActivity.companyBtn.setOnClickListener(mContactDetialActivity.startCompanyListener);

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
        if(mContactDetialActivity.contacts.icon.length() == 0)
        {
            if(mContactDetialActivity.contacts.sex == 0)
            {
                mContactDetialActivity.headImg.setImageResource(R.drawable.default_user);
            }
            else
            {
                mContactDetialActivity.headImg.setImageResource(R.drawable.default_wuser);
            }
        }
        else
        {
            if(BigwinerApplication.mApp.chatUtils == null)
            {
                AppActivityManager.getInstance().AppExit(mContactDetialActivity);
            }
            File file = BigwinerApplication.mApp.chatUtils.getHead(mContactDetialActivity.contacts.mRecordid);
            if(mContactDetialActivity.contacts.sex == 0)
            {

                RequestOptions options = new RequestOptions()
                        .placeholder(com.bigwiner.R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((int) (64* mContactDetialActivity.mBasePresenter.mScreenDefine.density));
                Glide.with(mContactDetialActivity).load(ContactsAsks.getContactIconUrl(mContactDetialActivity.contacts.mRecordid,
                        BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mContactDetialActivity.headImg));

            }
            else
            {
                RequestOptions options = new RequestOptions()
                        .placeholder(com.bigwiner.R.drawable.default_wuser).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((int) (64* mContactDetialActivity.mBasePresenter.mScreenDefine.density));
                Glide.with(mContactDetialActivity).load(ContactsAsks.getContactIconUrl(mContactDetialActivity.contacts.mRecordid,
                        BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mContactDetialActivity.headImg));
            }

        }
        RequestOptions options1 = (RequestOptions) new RequestOptions()
                .placeholder(R.drawable.meetingtemp).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (360*mContactDetialActivity.mBasePresenter.mScreenDefine.density), (int) (200 *mContactDetialActivity.mBasePresenter.mScreenDefine.density));
        Glide.with(mContactDetialActivity).load(ContactsAsks.getContactBgUrl(mContactDetialActivity.contacts.mRecordid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options1).into(new MySimpleTarget(mContactDetialActivity.bgImg));
        mContactDetialActivity.complaint.setText(mContactDetialActivity.contacts.complaint);
        if(mContactDetialActivity.contacts.sex == 0)
        {
            mContactDetialActivity.sexImg.setImageResource(R.drawable.male);
        }
        else
        {
            mContactDetialActivity.sexImg.setImageResource(R.drawable.female);
        }
        ViewHelp.praseLeaves(mContactDetialActivity.stars,mContactDetialActivity.contacts.leaves);
        if(mContactDetialActivity.contacts.mRName.length() == 0)
        mContactDetialActivity.mName.setText(mContactDetialActivity.contacts.mName);
        else
            mContactDetialActivity.mName.setText(mContactDetialActivity.contacts.mRName);
        if(mContactDetialActivity.contacts.isadd == false)
        {
            mContactDetialActivity.mAdd.setText(mContactDetialActivity.getString(R.string.my_btn_add));
            mContactDetialActivity.mAddimg.setImageResource(R.drawable.addimg);
            mContactDetialActivity.addBtn.setOnClickListener(mContactDetialActivity.addFriendListener);
        }
        else
        {
            mContactDetialActivity.mAdd.setText(mContactDetialActivity.getString(R.string.my_btn_del));
            mContactDetialActivity.mAddimg.setImageResource(R.drawable.delimg);
            mContactDetialActivity.addBtn.setOnClickListener(mContactDetialActivity.delFriendListener);
        }
        mContactDetialActivity.itemLayers.removeAllViews();
        if(mContactDetialActivity.contacts.confrim.equals(mContactDetialActivity.getString(R.string.contacts_un_confrim)) || mContactDetialActivity.contacts.confrim.length() == 0)
        {
            View view = ViewHelp.measureConversationLable(mContactDetialActivity.itemLayers,mContactDetialActivity.getString(R.string.contacts_un_confrim)
                    ,R.drawable.confirm2,R.drawable.contact_btn_shape_yellow_empty,mContactDetialActivity.getLayoutInflater(), Color.rgb(185,185,1));
            view.setOnClickListener(confirmDialogListener);
        }
        else
        {
            ViewHelp.measureConversationLable(mContactDetialActivity.itemLayers,mContactDetialActivity.contacts.confrim
                    ,R.drawable.confirm,R.drawable.contact_btn_shape_yellow_empty,mContactDetialActivity.getLayoutInflater(), Color.rgb(152,0,1));
        }

        if(mContactDetialActivity.contacts.city.length() > 0)
        {
            ViewHelp.measureConversationLable(mContactDetialActivity.itemLayers,mContactDetialActivity.contacts.city
                    ,R.drawable.locationicon,R.drawable.contact_btn_shape_yellow_empty,mContactDetialActivity.getLayoutInflater(),Color.rgb(152,0,1));
        }
        mContactDetialActivity.company = new Company();
        mContactDetialActivity.company.id = mContactDetialActivity.contacts.companyid;
        if(mContactDetialActivity.company.id.length() > 0)
        {
            mContactDetialActivity.companyBtn.setVisibility(View.VISIBLE);
        }
        if(mContactDetialActivity.contacts.issail)
        {
            ViewHelp.measureConversationLable(mContactDetialActivity.itemLayers,mContactDetialActivity.getString(R.string.company_issail)
                    ,R.drawable.confirm,R.drawable.contact_btn_shape_yellow_empty,mContactDetialActivity.getLayoutInflater(),Color.rgb(152,0,1));
        }
        else
        {
            View view = ViewHelp.measureConversationLable(mContactDetialActivity.itemLayers,mContactDetialActivity.getString(R.string.company_nosail)
                    ,R.drawable.confirm2,R.drawable.contact_btn_shape_gray_empty,mContactDetialActivity.getLayoutInflater(),Color.rgb(185,185,1));
            view.setOnClickListener(sailListener);
        }
        mContactDetialActivity.vip.setText(mContactDetialActivity.contacts.vip);
        mContactDetialActivity.mobil.setText(mContactDetialActivity.contacts.mPhone2+mContactDetialActivity.contacts.mMobile);
        mContactDetialActivity.desTxt.setText(mContactDetialActivity.getString(R.string.my_des)+mContactDetialActivity.contacts.des);
        mContactDetialActivity.typeValue.setText(mContactDetialActivity.contacts.typevalue);
        mContactDetialActivity.mailValue.setText(mContactDetialActivity.contacts.eMail);
        mContactDetialActivity.areaValue.setText(mContactDetialActivity.contacts.typearea);
        mContactDetialActivity.companyValue.setText(mContactDetialActivity.contacts.company);
        mContactDetialActivity.addressValue.setText(mContactDetialActivity.contacts.address);
        mContactDetialActivity.dertyValue.setText(mContactDetialActivity.contacts.mPosition);
        if(mContactDetialActivity.complaint.length() > 0)
        {
            mContactDetialActivity.complaint.setText(mContactDetialActivity.contacts.complaint);
        }
        ShareItem shareItem = new ShareItem();
        shareItem.title = mContactDetialActivity.contacts.getmRName();
        shareItem.picurl = ContactsAsks.getContactIconUrl(mContactDetialActivity.contacts.mRecordid,BigwinerApplication.mApp.contactManager.updataKey);
        shareItem.des = mContactDetialActivity.contacts.des;
        shareItem.weburl = ConversationPrase.prseShareUrl("user"
                ,mContactDetialActivity.contacts.mRecordid);
        mContactDetialActivity.sharBtn.setOnClickListener(new BigwinerApplication.DoshareListener(mContactDetialActivity,shareItem));
    }

    public View.OnClickListener sailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContactDetialActivity, SailApplyActivity.class);
            mContactDetialActivity.startActivity(intent);
        }
    };

    public View.OnClickListener confirmDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            BigwinerApplication.mApp.logout(mMainPresenter.mMainHandler,mMainPresenter.mMainActivity);
            AppUtils.creatDialogTowButton(mContactDetialActivity, mContactDetialActivity.getString(R.string.setting_confirm_upload)
                    , mContactDetialActivity.getString(R.string.title_tip), mContactDetialActivity.getString(R.string.button_word_cancle), mContactDetialActivity.getString(R.string.setting_confirm_upload_btn)
                    , null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirm();
                        }
                    });
        }
    };

    public void confirm() {
        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = mContactDetialActivity.getString(R.string.my_take_photo);
        item1.mListener = mContactDetialActivity.mTakePhotoListenter;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = mContactDetialActivity.getString(R.string.my_photo_select);
        item1.mListener = mContactDetialActivity.mAddPicListener;
        items.add(item1);
        mContactDetialActivity.popupWindow = AppUtils.creatButtomMenu(mContactDetialActivity, mContactDetialActivity.shade, items, mContactDetialActivity.findViewById(R.id.activity_contacts_detial));
    }

    public void takePhoto() {
        BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mContactDetialActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("businesscard"), SettingActivity.TAKE_PHOTO_CONFRIM);
        mContactDetialActivity.popupWindow.dismiss();
    }

    public void pickPhoto() {
        BigwinerApplication.mApp.mFileUtils.selectPhotos(mContactDetialActivity,SettingActivity.CHOSE_PHOTO_CONFRIM);
        mContactDetialActivity.popupWindow.dismiss();
    }


    public void addFriend() {
        mContactDetialActivity.waitDialog.show();
        ContactsAsks.getContactAdd(mContactDetialActivity,contactsDetialHandler,mContactDetialActivity.contacts);
    }

    public void delFriend() {
        mContactDetialActivity.waitDialog.show();
        ContactsAsks.getContactDel(mContactDetialActivity,contactsDetialHandler,mContactDetialActivity.contacts);
    }

    public void startChat()
    {
        if(!mContactDetialActivity.contacts.mRecordid.equals(BigwinerApplication.mApp.mAccount.mRecordId))
        {
            Intent intent = new Intent(mContactDetialActivity, ChatActivity.class);
            intent.putExtra("isow",true);
            intent.putExtra("contacts",mContactDetialActivity.contacts);
            mContactDetialActivity.startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(mContactDetialActivity, ConversationListActivity.class);
            mContactDetialActivity.startActivity(intent);
        }
    }

    public void startCompany()
    {
        Intent intent = new Intent(mContactDetialActivity, CompanyDetialActivity.class);
        intent.putExtra("company",mContactDetialActivity.company);
        mContactDetialActivity.startActivity(intent);
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SettingActivity.TAKE_PHOTO_CONFRIM:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(BigwinerApplication.mApp.mFileUtils.takePhotoPath);
                    LoginAsks.setUploadConfirm(mContactDetialActivity,mContactDetialActivity.mContactDetialPresenter.contactsDetialHandler,file);
                }
                break;
            case SettingActivity.CHOSE_PHOTO_CONFRIM:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mContactDetialActivity, data.getData());
                    File file = new File(path);
                    LoginAsks.setUploadConfirm(mContactDetialActivity,mContactDetialActivity.mContactDetialPresenter.contactsDetialHandler,file);
                }
                break;

        }
    }
}
