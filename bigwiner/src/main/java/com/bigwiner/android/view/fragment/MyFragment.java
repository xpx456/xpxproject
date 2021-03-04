package com.bigwiner.android.view.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.view.activity.CompanyInfoActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.activity.SailApplyActivity;
import com.bigwiner.android.view.activity.SettingActivity;
import com.bigwiner.android.view.activity.ShowCodeActivity;
import com.bigwiner.R;
import com.bigwiner.android.ViewHelp;
import com.bigwiner.android.presenter.MainPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactsListActivity;
import com.bigwiner.android.view.activity.HistoryActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;

import intersky.appbase.BaseFragment;
import intersky.appbase.MySimpleTarget;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.mywidget.CircleImageView;
import intersky.mywidget.MyLinearLayout;

@SuppressLint("ValidFragment")
public class MyFragment extends BaseFragment {

    public ImageView backBtn;
    public ImageView settingBtn;
    public TextView mName;
    public RelativeLayout chatBtn;
    public RelativeLayout sexlayer;
    public ImageView sexImg;
    public ImageView code;
    public MyLinearLayout itemLayers;
    public ArrayList<ImageView> stars = new ArrayList<ImageView>();
    public TextView complaint;
    public TextView desTxt;
    public TextView typeValue;
    public TextView areaValue;
    public TextView companyValue;
    public TextView addressValue;
    public TextView dertyValue;
    public TextView companyBtn;
    public TextView mobilValue;
    public TextView mailValue;
    public TextView editBtn;
    public TextView vip;
    public RelativeLayout change;
    public RelativeLayout btnCycle;
    public RelativeLayout btnHistroy;
    public MainActivity mMainActivity;
	public CircleImageView headImg;
    public ImageView bgImg;
    public SwipeRefreshLayout swipeRefreshLayout;
    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_my, container, false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        swipeRefreshLayout = mView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        this.backBtn = mView.findViewById(R.id.back);
        this.settingBtn = mView.findViewById(R.id.setting);
        headImg = mView.findViewById(R.id.headimg);
        bgImg = mView.findViewById(R.id.bgview);
        this.code = mView.findViewById(R.id.code);
        this.sexImg = mView.findViewById(R.id.sex);
        this.vip = mView.findViewById(R.id.vip);
        this.complaint = mView.findViewById(R.id.startxt);
        this.stars.add((ImageView) mView.findViewById(R.id.star1));
        this.stars.add((ImageView) mView.findViewById(R.id.star2));
        this.stars.add((ImageView) mView.findViewById(R.id.star3));
        this.stars.add((ImageView) mView.findViewById(R.id.star4));
        this.stars.add((ImageView) mView.findViewById(R.id.star5));
        this.mName = mView.findViewById(R.id.name);
        this.sexlayer = mView.findViewById(R.id.sexlayer);
        this.chatBtn = mView.findViewById(R.id.chatlayer);
        this.itemLayers = mView.findViewById(R.id.lable);
        this.desTxt = mView.findViewById(R.id.des);
        this.mailValue = mView.findViewById(R.id.mailvalue);
        this.typeValue = mView.findViewById(R.id.typevalue);
        this.areaValue = mView.findViewById(R.id.areavalue);
        this.mobilValue = mView.findViewById(R.id.mobiltxt);
        this.companyValue = mView.findViewById(R.id.companyvalue);
        this.addressValue = mView.findViewById(R.id.addressvalue);
        this.dertyValue = mView.findViewById(R.id.dertyvalue);
        this.companyBtn = mView.findViewById(R.id.detialbtn);
        this.btnCycle = mView.findViewById(R.id.mycycle);
        this.editBtn = mView.findViewById(R.id.settingtxt);
        this.btnHistroy = mView.findViewById(R.id.myhistory);
        this.change = mView.findViewById(R.id.change);
        this.backBtn.setOnClickListener(backListener);
        this.settingBtn.setOnClickListener(doSettingListener);
        this.btnCycle.setOnClickListener(cycleListener);
        this.btnHistroy.setOnClickListener(historyListener);
        this.chatBtn.setOnClickListener(startChatListener);
        this.companyBtn.setOnClickListener(startCompanyEditListener);
        this.change.setOnClickListener(changeBgListener);
        this.editBtn.setOnClickListener(doSettingListener);
        headImg.setOnClickListener(showCodeListener);
        this.code.setOnClickListener(showCodeListener);
        initData();
        return mView;
    }

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            LoginAsks.getUserInfo(mMainActivity,mMainActivity.mMainPresenter.mMainHandler);
        }
    };

    public void updataView() {
        if(headImg != null)
        {
            if(BigwinerApplication.mApp.mAccount.mSex.equals("男"))
                BigwinerApplication.mApp.mAccount.sex = 0;
            else if(BigwinerApplication.mApp.mAccount.mSex.equals("女"))
                BigwinerApplication.mApp.mAccount.sex = 1;
            else
                BigwinerApplication.mApp.mAccount.sex = 2;
            initData();
        }
    }

    public void initData() {


        if(BigwinerApplication.mApp.mAccount.sex == 0)
        {
            RequestOptions options = new RequestOptions()
                    .placeholder(com.bigwiner.R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override((int) (64* mMainActivity.mBasePresenter.mScreenDefine.density));
            Glide.with(mMainActivity).load(ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.mAccount.modify)).apply(options).into(new MySimpleTarget(headImg));
        }
        else
        {//1590394264771
            if(BigwinerApplication.mApp.mAccount.mRecordId.length() > 0)
            {
                RequestOptions options = new RequestOptions()
                        .placeholder(com.bigwiner.R.drawable.default_wuser).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((int) (64* mMainActivity.mBasePresenter.mScreenDefine.density));
                Glide.with(mMainActivity).load(ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.mAccount.modify)).apply(options).into(new MySimpleTarget(headImg));
            }

        }
        if(BigwinerApplication.mApp.mAccount.mRecordId.length() > 0)
        {
            RequestOptions options1 = new RequestOptions()
                    .placeholder(R.drawable.meetingtemp).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override((int) (360*mMainActivity.mBasePresenter.mScreenDefine.density), (int) (200 *mMainActivity.mBasePresenter.mScreenDefine.density));
            Glide.with(mMainActivity).load(ContactsAsks.getContactBgUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.mAccount.modify)).apply(options1).into(new MySimpleTarget(bgImg));
        }

        mobilValue.setText("+"+BigwinerApplication.mApp.mAccount.mCloundAdminId+" "+BigwinerApplication.mApp.mAccount.mMobile);
        if(BigwinerApplication.mApp.mAccount.sex == 0)
        {
            this.sexlayer.setVisibility(View.VISIBLE);
            this.sexImg.setImageResource(R.drawable.male);
        }
        else if(BigwinerApplication.mApp.mAccount.sex == 1)
        {
            this.sexlayer.setVisibility(View.VISIBLE);
            this.sexImg.setImageResource(R.drawable.female);
        }
        else
        {
            this.sexlayer.setVisibility(View.INVISIBLE);
        }
        ViewHelp.praseLeaves(this.stars, BigwinerApplication.mApp.mAccount.leavel);
        this.mName.setText(BigwinerApplication.mApp.mAccount.getName());
        itemLayers.removeAllViews();
        if(BigwinerApplication.mApp.mAccount.confrim.equals(mMainActivity.getString(R.string.contacts_un_confrim)))
        {
            View view = ViewHelp.measureConversationLable(this.itemLayers, BigwinerApplication.mApp.mAccount.confrim
                    ,R.drawable.confirm2,R.drawable.contact_btn_shape_gray_empty,mMainActivity.getLayoutInflater(), Color.rgb(185,185,1));
            view.setOnClickListener(confirmDialogListener);
        }
        else
        {
            ViewHelp.measureConversationLable(this.itemLayers, BigwinerApplication.mApp.mAccount.confrim
                    ,R.drawable.confirm,R.drawable.contact_btn_shape_yellow_empty,mMainActivity.getLayoutInflater(), Color.rgb(152,0,1));
        }

        if(BigwinerApplication.mApp.mAccount.city.length() > 0)
        {
            ViewHelp.measureConversationLable(this.itemLayers, BigwinerApplication.mApp.mAccount.city
                    ,R.drawable.locationicon,R.drawable.contact_btn_shape_yellow_empty,mMainActivity.getLayoutInflater(),Color.rgb(152,0,1));
        }

        if(BigwinerApplication.mApp.mAccount.issail)
        {
            ViewHelp.measureConversationLable(this.itemLayers,mMainActivity.getString(R.string.company_issail)
                    ,R.drawable.confirm,R.drawable.contact_btn_shape_yellow_empty,mMainActivity.getLayoutInflater(),Color.rgb(152,0,1));
        }
        else
        {
            View view = ViewHelp.measureConversationLable(this.itemLayers,mMainActivity.getString(R.string.company_nosail)
                    ,R.drawable.confirm2,R.drawable.contact_btn_shape_gray_empty,mMainActivity.getLayoutInflater(),Color.rgb(185,185,1));
            view.setOnClickListener(sailListener);
        }
        this.vip.setText(BigwinerApplication.mApp.mAccount.vip);
        this.desTxt.setText( BigwinerApplication.mApp.mAccount.des);
        this.typeValue.setText(BigwinerApplication.mApp.mAccount.typeBusiness);
        this.mailValue.setText(BigwinerApplication.mApp.mAccount.mEmail);
        this.areaValue.setText(BigwinerApplication.mApp.mAccount.typeArea);
        this.companyValue.setText(BigwinerApplication.mApp.company.name);
        this.addressValue.setText(BigwinerApplication.mApp.company.address);
        this.dertyValue.setText(BigwinerApplication.mApp.mAccount.mPosition);
        if(BigwinerApplication.mApp.mAccount.complaint.length() > 0)
        {
            this.complaint.setText(BigwinerApplication.mApp.mAccount.complaint);
        }
        if(BigwinerApplication.mApp.company.name.length() > 0)
        {
            this.companyBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            this.companyBtn.setVisibility(View.INVISIBLE);
        }
    }

    public View.OnClickListener sailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, SailApplyActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener confirmDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            BigwinerApplication.mApp.logout(mMainHandler,mMainActivity);
            AppUtils.creatDialogTowButton(mMainActivity, mMainActivity.getString(R.string.setting_confirm_upload)
                    , mMainActivity.getString(R.string.title_tip), mMainActivity.getString(R.string.button_word_cancle), mMainActivity.getString(R.string.setting_confirm_upload_btn)
                    , null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirm();
                        }
                    });
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.setContent(mMainActivity.lastpage);
        }
    };

    public View.OnClickListener doSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, SettingActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener cycleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, ContactsListActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener historyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, HistoryActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener startChatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, ConversationListActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener startCompanyEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startCompany();
        }
    };

    public View.OnClickListener changeBgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showSetBg(true);
        }
    };

    public View.OnClickListener showCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Intent intent = new Intent(mMainActivity, ShowCodeActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isbg = (boolean) v.getTag();
            if(isbg)
            BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mMainActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("bg/photo"),MainActivity.TAKE_PHOTO_BG);
            else
                BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mMainActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),MainActivity.TAKE_PHOTO_HEAD);
            mMainActivity.popupWindow.dismiss();
        }
    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isbg = (boolean) v.getTag();
            if(isbg)
            BigwinerApplication.mApp.mFileUtils.selectPhotos(mMainActivity,MainActivity.CHOSE_PICTURE_BG);
            else
                BigwinerApplication.mApp.mFileUtils.selectPhotos(mMainActivity,MainActivity.CHOSE_PICTURE_HEAD);
            mMainActivity.popupWindow.dismiss();
        }
    };

    public void showSetBg(boolean isbg) {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = mMainActivity.getString(R.string.my_take_photo);
        item1.item = isbg;
        item1.mListener = mTakePhotoListenter;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = mMainActivity.getString(R.string.my_photo_select);
        item1.item = isbg;
        item1.mListener = mAddPicListener;
        items.add(item1);
        mMainActivity.popupWindow = AppUtils.creatButtomMenu(mMainActivity, mMainActivity.shade, items, mMainActivity.findViewById(R.id.headlayer));
    }

    public void confirm() {
        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = mMainActivity.getString(R.string.my_take_photo);
        item1.mListener = mMainActivity.mTakePhotoListenter;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = mMainActivity.getString(R.string.my_photo_select);
        item1.mListener = mMainActivity.mAddPicListener;
        items.add(item1);
        mMainActivity.popupWindow = AppUtils.creatButtomMenu(mMainActivity, mMainActivity.shade, items, mMainActivity.findViewById(R.id.headlayer));
    }

    public void startCompany() {
        if(BigwinerApplication.mApp.company.id.length() > 0)
        {
            Intent intent = new Intent(mMainActivity, CompanyInfoActivity.class);
            intent.putExtra("company",BigwinerApplication.mApp.company);
            mMainActivity.startActivity(intent);
        }
        else
        {
            AppUtils.showMessage(mMainActivity,"您还未加入任何公司");
        }
    }
}
