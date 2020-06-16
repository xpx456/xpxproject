package com.bigwiner.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.handler.UserInfoHandler;
import com.bigwiner.android.receiver.UserInfoReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.CompanyInfoActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;
import com.bigwiner.android.view.activity.SourceSelectActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Account;
import intersky.apputils.AppUtils;
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

public class UserInfoPresenter implements Presenter {

    public UserInfoActivity mUserInfoActivity;
    public UserInfoHandler mUserInfoHandler;

    public UserInfoPresenter(UserInfoActivity mUserInfoActivity) {
        mUserInfoHandler = new UserInfoHandler(mUserInfoActivity);
        this.mUserInfoActivity = mUserInfoActivity;
        mUserInfoActivity.setBaseReceiver(new UserInfoReceiver(mUserInfoHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
//        ToolBarHelper.setSutColor(mUserInfoActivity, Color.argb(0, 255, 255, 255));
        mUserInfoActivity.setContentView(R.layout.activity_userinfo);
        mUserInfoActivity.mToolBarHelper.hidToolbar2(mUserInfoActivity);
        ToolBarHelper.setBgColor(mUserInfoActivity, mUserInfoActivity.mActionBar, Color.rgb(255, 255, 255));
//        mUserInfoActivity.mToolBarHelper.hidToolbar(mUserInfoActivity, (RelativeLayout) mUserInfoActivity.findViewById(R.id.buttomaciton));
//        mUserInfoActivity.measureStatubar(mUserInfoActivity, (RelativeLayout) mUserInfoActivity.findViewById(R.id.stutebar));
        mUserInfoActivity.shade = (RelativeLayout) mUserInfoActivity.findViewById(R.id.shade);
        mUserInfoActivity.back = mUserInfoActivity.findViewById(R.id.back);

        mUserInfoActivity.headbtn = mUserInfoActivity.findViewById(R.id.head);
        mUserInfoActivity.head = mUserInfoActivity.findViewById(R.id.headimg);
        mUserInfoActivity.name = mUserInfoActivity.findViewById(R.id.namevalue);
        mUserInfoActivity.mail = mUserInfoActivity.findViewById(R.id.mailvalue);
        mUserInfoActivity.mobil = mUserInfoActivity.findViewById(R.id.mobilvalue);
        mUserInfoActivity.sexbtn = mUserInfoActivity.findViewById(R.id.sex);
        mUserInfoActivity.sex = mUserInfoActivity.findViewById(R.id.sexvalue);
        mUserInfoActivity.citybtn = mUserInfoActivity.findViewById(R.id.city);
        mUserInfoActivity.city = mUserInfoActivity.findViewById(R.id.cityvalue);
        mUserInfoActivity.areabtn = mUserInfoActivity.findViewById(R.id.area);
        mUserInfoActivity.area = mUserInfoActivity.findViewById(R.id.areavalue);
        mUserInfoActivity.typebtn = mUserInfoActivity.findViewById(R.id.type);
        mUserInfoActivity.type = mUserInfoActivity.findViewById(R.id.typevalue);
        mUserInfoActivity.positionbtn = mUserInfoActivity.findViewById(R.id.position);
        mUserInfoActivity.position = mUserInfoActivity.findViewById(R.id.positionvalue);
        mUserInfoActivity.companybtn = mUserInfoActivity.findViewById(R.id.cname);
        mUserInfoActivity.cname = mUserInfoActivity.findViewById(R.id.cnamevalue);
        mUserInfoActivity.memo = mUserInfoActivity.findViewById(R.id.memovalue);
        mUserInfoActivity.save = mUserInfoActivity.findViewById(R.id.save);
        mUserInfoActivity.headbtn.setOnClickListener(mUserInfoActivity.changeHeadListener);
        mUserInfoActivity.sexbtn.setOnClickListener(mUserInfoActivity.sexSelectListener);
        mUserInfoActivity.city.setOnClickListener(mUserInfoActivity.citySelectListener);
        mUserInfoActivity.typebtn.setOnClickListener(mUserInfoActivity.typeSelectListener);
        mUserInfoActivity.areabtn.setOnClickListener(mUserInfoActivity.areaSelectListener);
        mUserInfoActivity.positionbtn.setOnClickListener(mUserInfoActivity.positionSelectListener);
//        mUserInfoActivity.companybtn.setOnClickListener(mUserInfoActivity.companySelectListener);
        mUserInfoActivity.btnSubmit = mUserInfoActivity.findViewById(R.id.submit_btn);
        mUserInfoActivity.back.setOnClickListener(mUserInfoActivity.backListener);
        mUserInfoActivity.btnSubmit.setOnClickListener(mUserInfoActivity.submintListener);
        mUserInfoActivity.save.setOnClickListener(mUserInfoActivity.submintListener);
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

    public void doSubmit() {

        mUserInfoActivity.waitDialog.show();
        if (!BigwinerApplication.mApp.mAccount.icon.equals(mUserInfoActivity.headid)) {
            LoginAsks.setUploadHead(mUserInfoActivity, mUserInfoHandler, new File(mUserInfoActivity.headid));
        } else {
            Account account = new Account();
            account.copy(BigwinerApplication.mApp.mAccount);
            account.mRealName = mUserInfoActivity.name.getText().toString();
            account.mEmail = mUserInfoActivity.mail.getText().toString();
            account.city = mUserInfoActivity.city.getText().toString();
            if(account.mEmail.length() != 0)
            {
                if(AppUtils.checkEmail(mUserInfoActivity.mail.getText().toString()) == false)
                {
                    AppUtils.showMessage(mUserInfoActivity,mUserInfoActivity.getString(R.string.mail_error));
                    return;
                }
            }

            if (!mUserInfoActivity.sex.getText().toString().equals(mUserInfoActivity.getString(R.string.userinfo_sex_hit)))
                account.mSex = mUserInfoActivity.sex.getText().toString();
            else {
                account.mSex = "";
            }
            if (!mUserInfoActivity.area.getText().toString().equals(mUserInfoActivity.getString(R.string.userinfo_area_hit)))
                account.typeArea = mUserInfoActivity.area.getText().toString();
            else {
                account.typeArea = "";
            }
            if (!mUserInfoActivity.type.getText().toString().equals(mUserInfoActivity.getString(R.string.userinfo_type_hit)))
                account.typeBusiness = mUserInfoActivity.type.getText().toString();
            else {
                account.typeBusiness = "";
            }
            if (!mUserInfoActivity.position.getText().toString().equals(mUserInfoActivity.getString(R.string.userinfo_position_hit)))
                account.mPosition = mUserInfoActivity.position.getText().toString();
            else {
                account.mPosition = "";
            }
            if (!mUserInfoActivity.cname.getText().toString().equals(mUserInfoActivity.getString(R.string.userinfo_cname_hit)) && mUserInfoActivity.cname.length() > 0) {
                account.mCompanyName = mUserInfoActivity.cname.getText().toString();
                account.mUCid = mUserInfoActivity.cid;
            } else {
                account.mCompanyName = "";
                account.mUCid = "";
            }
            account.des = mUserInfoActivity.memo.getText().toString();
            LoginAsks.doEditUserinfo(mUserInfoActivity, mUserInfoHandler, account);
        }

    }

    public void initData() {
        RequestOptions options = new RequestOptions()
                .placeholder(com.bigwiner.R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (64 * mUserInfoActivity.mBasePresenter.mScreenDefine.density));
        GlideApp.with(mUserInfoActivity).load(ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.mAccount.modify)).apply(options).into(new MySimpleTarget(mUserInfoActivity.head));
        mUserInfoActivity.name.setText(BigwinerApplication.mApp.mAccount.mRealName);
        mUserInfoActivity.mobil.setText("+"+BigwinerApplication.mApp.mAccount.mCloundAdminId+" "+BigwinerApplication.mApp.mAccount.mMobile);
        mUserInfoActivity.headid = BigwinerApplication.mApp.mAccount.icon;
        mUserInfoActivity.position.setText(BigwinerApplication.mApp.mAccount.mPosition);
        mUserInfoActivity.mail.setText(BigwinerApplication.mApp.mAccount.mEmail);
        mUserInfoActivity.memo.setText(BigwinerApplication.mApp.mAccount.des);
        initCompany();
        initSex();
//        initCity();
//        initType();
//        initArea();

        BigwinerApplication.mApp.ports.reset();
        if(BigwinerApplication.mApp.mAccount.city.length() > 0)
        {
            BigwinerApplication.mApp.ports.addSelect(BigwinerApplication.mApp.mAccount.city);
            mUserInfoActivity.city.setText(BigwinerApplication.mApp.mAccount.city);
        }
        BigwinerApplication.mApp.businesstypeSelect.reset();
        if(BigwinerApplication.mApp.mAccount.typeBusiness.length() > 0)
        {
            BigwinerApplication.mApp.businesstypeSelect.addSelect(BigwinerApplication.mApp.mAccount.typeBusiness);
            mUserInfoActivity.type.setText(BigwinerApplication.mApp.mAccount.typeBusiness);
        }
        BigwinerApplication.mApp.businessareaSelect.reset();
        if(BigwinerApplication.mApp.mAccount.typeArea.length() > 0)
        {
            BigwinerApplication.mApp.businessareaSelect.addSelect(BigwinerApplication.mApp.mAccount.typeArea);
            mUserInfoActivity.area.setText(BigwinerApplication.mApp.mAccount.typeArea);
        }
        initposition();
    }

    public void initSex() {
        mUserInfoActivity.sex.setText(mUserInfoActivity.getString(R.string.userinfo_sex_hit));
        for (int i = 0; i < BigwinerApplication.mApp.sexSelect.list.size(); i++) {
            Select select = new Select(BigwinerApplication.mApp.sexSelect.list.get(i).mId, BigwinerApplication.mApp.sexSelect.list.get(i).mName);
            select.iselect = false;
            mUserInfoActivity.sexSelect.add(select);
            if (BigwinerApplication.mApp.mAccount.mSex.equals(select.mName)) {
                select.iselect = true;
                mUserInfoActivity.sex.setText(select.mName);
            }
        }
    }

    //    public void initCity() {
//        mUserInfoActivity.city.setText(mUserInfoActivity.getString(R.string.userinfo_city_hit));
//        boolean selected = false;
//        for(int i = 0 ; i < BigwinerApplication.mApp.allprovience.list.size() ; i++)
//        {
//            MapSelect mapSelect = BigwinerApplication.mApp.allcity.get(BigwinerApplication.mApp.allprovience.list.get(i).mId);
//            for(int j = 0 ; j < mapSelect.list.size() ; j++)
//            {
//                Select select = new Select(mapSelect.list.get(j).mId,mapSelect.list.get(j).mName);
//                mUserInfoActivity.citySelect.add(select);
//                select.iselect = false;
//                if(select.mName.equals(BigwinerApplication.mApp.mAccount.city)&& selected == false)
//                {
//                    select.iselect = true;
//                    selected = true;
//                    mUserInfoActivity.city.setText(select.mName);
//                }
//            }
//
//        }
//
//
//    }
    public void initCity() {
        mUserInfoActivity.city.setText(mUserInfoActivity.getString(R.string.userinfo_city_hit));
        for (int i = 0; i < BigwinerApplication.mApp.ports.list.size(); i++) {
            Select select = new Select(BigwinerApplication.mApp.ports.list.get(i).mId, BigwinerApplication.mApp.ports.list.get(i).mName);
            mUserInfoActivity.citySelect.add(select);
            select.iselect = false;
            if (AppUtils.check(select.mName, BigwinerApplication.mApp.mAccount.city)) {
                select.iselect = true;
            }
            if (BigwinerApplication.mApp.mAccount.city.length() > 0)
                mUserInfoActivity.city.setText(BigwinerApplication.mApp.mAccount.city);
        }
    }

    public void initCompany() {
        mUserInfoActivity.area.setText("");
        for (int i = 0; i < BigwinerApplication.mApp.companyslelct.list.size(); i++) {
            Select select = new Select(BigwinerApplication.mApp.companyslelct.list.get(i).mId, BigwinerApplication.mApp.companyslelct.list.get(i).mName);
            mUserInfoActivity.companySelect.add(select);
            select.iselect = false;
            if (select.mId.equals(BigwinerApplication.mApp.mAccount.mUCid)) {
                select.iselect = true;
                mUserInfoActivity.cname.setText(select.mName);
                mUserInfoActivity.cid = select.mId;
            }
        }
    }

    public void initType() {
        mUserInfoActivity.area.setText(mUserInfoActivity.getString(R.string.userinfo_type_hit));
        for (int i = 0; i < BigwinerApplication.mApp.businesstypeSelect.list.size(); i++) {
            Select select = new Select(BigwinerApplication.mApp.businesstypeSelect.list.get(i).mId, BigwinerApplication.mApp.businesstypeSelect.list.get(i).mName);
            mUserInfoActivity.typeSelect.add(select);
            select.iselect = false;
            if (AppUtils.check(select.mName, BigwinerApplication.mApp.mAccount.typeBusiness)) {
                select.iselect = true;
            }
            if (BigwinerApplication.mApp.mAccount.typeBusiness.length() > 0)
                mUserInfoActivity.type.setText(BigwinerApplication.mApp.mAccount.typeBusiness);
        }
    }

    public void initArea() {
        mUserInfoActivity.area.setText(mUserInfoActivity.getString(R.string.userinfo_area_hit));
        for (int i = 0; i < BigwinerApplication.mApp.businessareaSelect.list.size(); i++) {
            Select select = new Select(BigwinerApplication.mApp.businessareaSelect.list.get(i).mId, BigwinerApplication.mApp.businessareaSelect.list.get(i).mName);
            mUserInfoActivity.areaSelect.add(select);
            select.iselect = false;
            if (AppUtils.check(select.mName, BigwinerApplication.mApp.mAccount.typeArea)) {
                select.iselect = true;
            }
            if (BigwinerApplication.mApp.mAccount.typeArea.length() > 0)
                mUserInfoActivity.area.setText(BigwinerApplication.mApp.mAccount.typeArea);
        }
    }


    public void initposition() {
        mUserInfoActivity.position.setText(mUserInfoActivity.getString(R.string.userinfo_position_hit));
        for (int i = 0; i < BigwinerApplication.mApp.positions.list.size(); i++) {
            Select select = new Select(BigwinerApplication.mApp.positions.list.get(i).mId, BigwinerApplication.mApp.positions.list.get(i).mName);
            mUserInfoActivity.positionSelect.add(select);
            select.iselect = false;
            if (select.mName.equals(BigwinerApplication.mApp.mAccount.mPosition)) {
                select.iselect = true;
                mUserInfoActivity.position.setText(select.mName);
            }
        }
    }

    public void startSelectSex() {
        BigwinerApplication.mApp.startSelectView(mUserInfoActivity, mUserInfoActivity.sexSelect, mUserInfoActivity.getString(R.string.userinfo_sex_hit), UserInfoActivity.ACTION_SEX_SELECT, true, false);
    }

    public void startSelectCity() {
//        BigwinerApplication.mApp.startSelectViewCity(mUserInfoActivity, mUserInfoActivity.citySelect, mUserInfoActivity.getString(R.string.company_edit_city_hit), UserInfoActivity.ACTION_CITY_SELECT, true, true);
        BigwinerApplication.mApp.startSelectView(mUserInfoActivity, BigwinerApplication.mApp.ports, mUserInfoActivity.getString(R.string.company_edit_city_hit), UserInfoActivity.ACTION_CITY_SELECT, true, true,1);
    }

    public void startSelectType() {
        BigwinerApplication.mApp.startSelectView(mUserInfoActivity, BigwinerApplication.mApp.businesstypeSelect, mUserInfoActivity.getString(R.string.userinfo_type_hit), UserInfoActivity.ACTION_TYPE_SELECT, false, true, 3);
    }

    public void startSelectArea() {
        BigwinerApplication.mApp.startSelectView(mUserInfoActivity, BigwinerApplication.mApp.businessareaSelect, mUserInfoActivity.getString(R.string.userinfo_area_hit), UserInfoActivity.ACTION_AREA_SELECT, false, true, 3);
    }

    public void startSelectCompany() {
        BigwinerApplication.mApp.startSelectView(mUserInfoActivity, mUserInfoActivity.companySelect, mUserInfoActivity.getString(R.string.userinfo_cname_hit), UserInfoActivity.ACTION_COMPANY_SELECT, true, true, true);
    }

    public void startSelectPosition() {
        BigwinerApplication.mApp.startSelectView(mUserInfoActivity, mUserInfoActivity.positionSelect, mUserInfoActivity.getString(R.string.userinfo_position_hit), UserInfoActivity.ACTION_POSITION_SELECT, true, false);
    }

    public void setSex(Intent intent) {
        Select select = intent.getParcelableExtra("item");
        mUserInfoActivity.sex.setText(select.mName);
    }

    public void setType(Intent intent) {
        ArrayList<Select> selects = intent.getParcelableArrayListExtra("items");
        mUserInfoActivity.type.setText(SelectManager.praseSelectString(selects));

        BigwinerApplication.mApp.businesstypeSelect.updataSelect(mUserInfoActivity.type.getText().toString());
        if(mUserInfoActivity.type.getText().toString().length() == 0)
        {
            mUserInfoActivity.type.setText(mUserInfoActivity.getString(R.string.userinfo_type_hit));
        }
    }

//    public void setCity(Intent intent) {
//        Select select = null;
//        if (intent.hasExtra("item"))
//            select = intent.getParcelableExtra("item");
//        if (select != null)
//            mUserInfoActivity.city.setText(select.mName);
//        else if (BigwinerApplication.mApp.city != null)
//            mUserInfoActivity.city.setText(BigwinerApplication.mApp.city.mName);
//    }

    public void setCity(Intent intent) {
//        Select select = null;
//        if (intent.hasExtra("item"))
//            select = intent.getParcelableExtra("item");
//        if (select != null)
//            mUserInfoActivity.city.setText(select.mName);
//        else if (BigwinerApplication.mApp.city != null)
//            mUserInfoActivity.city.setText(BigwinerApplication.mApp.city.mName);

        Select select = intent.getParcelableExtra("item");
        mUserInfoActivity.city.setText(select.mName);
        BigwinerApplication.mApp.ports.updataSelect(mUserInfoActivity.city.getText().toString());
        if(mUserInfoActivity.city.getText().toString().length() == 0)
        {
            mUserInfoActivity.city.setText(mUserInfoActivity.getString(R.string.userinfo_city_hit));
        }
    }

    public void setArea(Intent intent) {
        ArrayList<Select> selects = intent.getParcelableArrayListExtra("items");
        mUserInfoActivity.area.setText(SelectManager.praseSelectString(selects));
        BigwinerApplication.mApp.businessareaSelect.updataSelect(mUserInfoActivity.area.getText().toString());
        if(mUserInfoActivity.area.getText().toString().length() == 0)
        {
            mUserInfoActivity.area.setText(mUserInfoActivity.getString(R.string.userinfo_area_hit));
        }
    }

    public void setPostion(Intent intent) {
        Select select = intent.getParcelableExtra("item");
        mUserInfoActivity.position.setText(select.mName);
    }

    public void setCompany(Intent intent) {
        Select select = intent.getParcelableExtra("item");
        if (select.iselect == true) {
            mUserInfoActivity.cname.setText(select.mName);
            mUserInfoActivity.cid = select.mId;
        } else {
            mUserInfoActivity.cname.setText(mUserInfoActivity.getString(R.string.userinfo_cname_hit));
            mUserInfoActivity.cid = "";
        }

    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.TAKE_PHOTO_HEAD:
            case MainActivity.TAKE_PHOTO_BG:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(BigwinerApplication.mApp.mFileUtils.takePhotoPath);
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        if (requestCode == MainActivity.TAKE_PHOTO_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            BigwinerApplication.mApp.mFileUtils.cropPhoto(mUserInfoActivity, 1, 1, file.getPath(), BigwinerApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"), name).getPath()
                                    , (int) (60 * mUserInfoActivity.mBasePresenter.mScreenDefine.density), (int) (60 * mUserInfoActivity.mBasePresenter.mScreenDefine.density), MainActivity.CROP_HEAD);
                        } else {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            BigwinerApplication.mApp.mFileUtils.cropPhoto(mUserInfoActivity, 9, 5, file.getPath(), BigwinerApplication.mApp.mFileUtils.getOutputBgMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("bg/photo"), name).getPath()
                                    , mUserInfoActivity.mBasePresenter.mScreenDefine.ScreenWidth, (int) (200 * mUserInfoActivity.mBasePresenter.mScreenDefine.density), MainActivity.CROP_BG);
                        }
                    }
                }
                break;
            case MainActivity.CHOSE_PICTURE_HEAD:
            case MainActivity.CHOSE_PICTURE_BG:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mUserInfoActivity, data.getData());
                    File file = new File(path);
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        // 设置可缩放
                        if (requestCode == MainActivity.CHOSE_PICTURE_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            BigwinerApplication.mApp.mFileUtils.cropPhoto(mUserInfoActivity, 1, 1, path, BigwinerApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"), name).getPath()
                                    , (int) (60 * mUserInfoActivity.mBasePresenter.mScreenDefine.density), (int) (60 * mUserInfoActivity.mBasePresenter.mScreenDefine.density), MainActivity.CROP_HEAD);
                        } else {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            BigwinerApplication.mApp.mFileUtils.cropPhoto(mUserInfoActivity, 9, 5, path, BigwinerApplication.mApp.mFileUtils.getOutputBgMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("bg/photo"), name).getPath()
                                    , mUserInfoActivity.mBasePresenter.mScreenDefine.ScreenWidth, (int) (200 * mUserInfoActivity.mBasePresenter.mScreenDefine.density), MainActivity.CROP_BG);

                        }
                    }
                }
                break;
            case MainActivity.CROP_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> paths = BigwinerApplication.mApp.mFileUtils.getImage(data);
                    File mFile = new File(paths.get(0));
//                    LoginAsks.setUploadHead(mUserInfoActivity,mUserInfoHandler,mFile);
                    mUserInfoActivity.headid = mFile.getPath();
                    BigwinerApplication.mApp.setContactHead(mUserInfoActivity, mFile, mUserInfoActivity.head);
                }
                break;

        }
    }

    public void showSetHead() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = mUserInfoActivity.getString(R.string.my_take_photo);
        item1.mListener = mUserInfoActivity.mTakePhotoListenter;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = mUserInfoActivity.getString(R.string.my_photo_select);
        item1.mListener = mUserInfoActivity.mAddPicListener;
        items.add(item1);
        mUserInfoActivity.popupWindow = AppUtils.creatButtomMenu(mUserInfoActivity, mUserInfoActivity.shade, items, mUserInfoActivity.findViewById(R.id.activity_about));
    }
}
