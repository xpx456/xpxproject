package com.bigwiner.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.handler.CompanyInfoHandler;
import com.bigwiner.android.receiver.CompanyInfoReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.CompanyInfoActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideApp;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.conversation.receiver.ConversationReceiver;
import intersky.select.entity.MapSelect;
import intersky.select.entity.Select;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class CompanyInfoPresenter implements Presenter {

    public CompanyInfoActivity mCompanyInfoActivity;
    public CompanyInfoHandler mCompanyInfoHandler;
    public CompanyInfoPresenter(CompanyInfoActivity mCompanyInfoActivity)
    {
        mCompanyInfoHandler =new CompanyInfoHandler(mCompanyInfoActivity);
        this.mCompanyInfoActivity = mCompanyInfoActivity;
        mCompanyInfoActivity.setBaseReceiver(new CompanyInfoReceiver(mCompanyInfoHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
//        ToolBarHelper.setSutColor(mCompanyInfoActivity, Color.argb(0, 255, 255, 255));
        mCompanyInfoActivity.setContentView(R.layout.activity_companyinfo);
        mCompanyInfoActivity.mToolBarHelper.hidToolbar2(mCompanyInfoActivity);
        ToolBarHelper.setBgColor(mCompanyInfoActivity, mCompanyInfoActivity.mActionBar, Color.rgb(255, 255, 255));
//        mCompanyInfoActivity.mToolBarHelper.hidToolbar(mCompanyInfoActivity, (RelativeLayout) mCompanyInfoActivity.findViewById(R.id.buttomaciton));
//        mCompanyInfoActivity.measureStatubar(mCompanyInfoActivity, (RelativeLayout) mCompanyInfoActivity.findViewById(R.id.stutebar));
        mCompanyInfoActivity.company = mCompanyInfoActivity.getIntent().getParcelableExtra("company");
        mCompanyInfoActivity.shade = (RelativeLayout) mCompanyInfoActivity.findViewById(R.id.shade);
        mCompanyInfoActivity.back = mCompanyInfoActivity.findViewById(R.id.back);

        mCompanyInfoActivity.logobtn = mCompanyInfoActivity.findViewById(R.id.logo);
        mCompanyInfoActivity.logo = mCompanyInfoActivity.findViewById(R.id.logoimg);
        mCompanyInfoActivity.bgbtn = mCompanyInfoActivity.findViewById(R.id.bg);
        mCompanyInfoActivity.bg = mCompanyInfoActivity.findViewById(R.id.bgimg);
        mCompanyInfoActivity.citybtn = mCompanyInfoActivity.findViewById(R.id.city);
        mCompanyInfoActivity.city = mCompanyInfoActivity.findViewById(R.id.cityvalue);
        mCompanyInfoActivity.proviencebtn = mCompanyInfoActivity.findViewById(R.id.provience);
        mCompanyInfoActivity.provience = mCompanyInfoActivity.findViewById(R.id.proviencevalue);

        mCompanyInfoActivity.sailno = mCompanyInfoActivity.findViewById(R.id.sailnovalue);
        mCompanyInfoActivity.taxno = mCompanyInfoActivity.findViewById(R.id.taxnovalue);
        mCompanyInfoActivity.phone = mCompanyInfoActivity.findViewById(R.id.telvalue);
        mCompanyInfoActivity.fax = mCompanyInfoActivity.findViewById(R.id.faxvalue);
        mCompanyInfoActivity.mail = mCompanyInfoActivity.findViewById(R.id.mailvalue);
        mCompanyInfoActivity.web = mCompanyInfoActivity.findViewById(R.id.webvalue);
        mCompanyInfoActivity.name = mCompanyInfoActivity.findViewById(R.id.namevalue);
        mCompanyInfoActivity.ename = mCompanyInfoActivity.findViewById(R.id.enamevalue);
        mCompanyInfoActivity.address = mCompanyInfoActivity.findViewById(R.id.addressvalue);
        mCompanyInfoActivity.better = mCompanyInfoActivity.findViewById(R.id.bettervalue);

        mCompanyInfoActivity.logobtn.setOnClickListener(mCompanyInfoActivity.changeHeadListener);
        mCompanyInfoActivity.bgbtn.setOnClickListener(mCompanyInfoActivity.changeBgListener);
        mCompanyInfoActivity.citybtn.setOnClickListener(mCompanyInfoActivity.citySelectListener);
//        mCompanyInfoActivity.proviencebtn.setOnClickListener(mCompanyInfoActivity.provienceSelectListener);

        mCompanyInfoActivity.btnSubmit = mCompanyInfoActivity.findViewById(R.id.submit_btn);
        mCompanyInfoActivity.back.setOnClickListener(mCompanyInfoActivity.backListener);
        mCompanyInfoActivity.btnSubmit.setOnClickListener(mCompanyInfoActivity.submintListener);
        mCompanyInfoActivity.waitDialog.show();
        ContactsAsks.getCompanyDetial(mCompanyInfoActivity,mCompanyInfoHandler,mCompanyInfoActivity.company);
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
        Company company = new Company();
        company.copy(mCompanyInfoActivity.company);

        if(!company.icon.equals(mCompanyInfoActivity.logoid))
        {
            mCompanyInfoActivity.waitDialog.show();
            LoginAsks.setUploadCompanyHead(mCompanyInfoActivity,mCompanyInfoHandler,new File(mCompanyInfoActivity.logoid),company);
        }
        else if(!company.bg.equals(mCompanyInfoActivity.bgid))
        {
            mCompanyInfoActivity.waitDialog.show();
            LoginAsks.setUploadCompanyBg(mCompanyInfoActivity,mCompanyInfoHandler,new File(mCompanyInfoActivity.bgid),company);
        }
        else
        {
            mCompanyInfoActivity.waitDialog.show();
            company.taxno = mCompanyInfoActivity.taxno.getText().toString();
            company.sailno = mCompanyInfoActivity.sailno.getText().toString();
            company.city = mCompanyInfoActivity.city.getText().toString();
            company.province = mCompanyInfoActivity.provience.getText().toString();
            company.phone = mCompanyInfoActivity.phone.getText().toString();
            company.fax = mCompanyInfoActivity.fax.getText().toString();
            company.mail = mCompanyInfoActivity.mail.getText().toString();
            company.web = mCompanyInfoActivity.web.getText().toString();
            company.name = mCompanyInfoActivity.name.getText().toString();
            company.ename = mCompanyInfoActivity.ename.getText().toString();
            company.address = mCompanyInfoActivity.address.getText().toString();
            company.characteristic = mCompanyInfoActivity.better.getText().toString();
            LoginAsks.doEditCompanyInfo(mCompanyInfoActivity,mCompanyInfoHandler,company);
        }


    }


    public void doSubmit(Company company) {

            mCompanyInfoActivity.waitDialog.show();
            company.taxno = mCompanyInfoActivity.taxno.getText().toString();
            company.sailno = mCompanyInfoActivity.sailno.getText().toString();
            company.city = mCompanyInfoActivity.city.getText().toString();
            company.province = mCompanyInfoActivity.provience.getText().toString();
            company.phone = mCompanyInfoActivity.phone.getText().toString();
            company.fax = mCompanyInfoActivity.fax.getText().toString();
            company.mail = mCompanyInfoActivity.mail.getText().toString();
            company.web = mCompanyInfoActivity.web.getText().toString();
            company.name = mCompanyInfoActivity.name.getText().toString();
            company.ename = mCompanyInfoActivity.ename.getText().toString();
            company.address = mCompanyInfoActivity.address.getText().toString();
            company.characteristic = mCompanyInfoActivity.better.getText().toString();
            LoginAsks.doEditCompanyInfo(mCompanyInfoActivity,mCompanyInfoHandler,company);

    }

    public void initData()
    {

        mCompanyInfoActivity.logoid = mCompanyInfoActivity.company.icon;
        mCompanyInfoActivity.bgid = mCompanyInfoActivity.company.bg;
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.noticetemp).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (64* mCompanyInfoActivity.mBasePresenter.mScreenDefine.density));
        GlideApp.with(mCompanyInfoActivity).load(ContactsAsks.getCompanyIconUrl(mCompanyInfoActivity.company.id,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mCompanyInfoActivity.logo));
        RequestOptions options1 = new RequestOptions()
                .placeholder(R.drawable.meetingtemp).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (360*mCompanyInfoActivity.mBasePresenter.mScreenDefine.density), (int) (200 *mCompanyInfoActivity.mBasePresenter.mScreenDefine.density));
        GlideApp.with(mCompanyInfoActivity).load(ContactsAsks.getCompanyBgUrl(mCompanyInfoActivity.company.id,BigwinerApplication.mApp.contactManager.updataKey)).apply(options1).into(new MySimpleTarget(mCompanyInfoActivity.bg));
        mCompanyInfoActivity.sailno.setText(mCompanyInfoActivity.company.sailno);
        mCompanyInfoActivity.taxno.setText(mCompanyInfoActivity.company.taxno);
//        mCompanyInfoActivity.city.setText(mCompanyInfoActivity.company.city);
        mCompanyInfoActivity.provience.setText(mCompanyInfoActivity.company.province);
//        initProvience();
        BigwinerApplication.mApp.ports.reset();
        if(mCompanyInfoActivity.company.city.length() > 0 )
        {
            BigwinerApplication.mApp.ports.addSelect(mCompanyInfoActivity.company.city);
            if(BigwinerApplication.mApp.ports.selectlist.size() > 0)
            mCompanyInfoActivity.city.setText(BigwinerApplication.mApp.company.city);
        }




        mCompanyInfoActivity.phone.setText(mCompanyInfoActivity.company.phone);
        mCompanyInfoActivity.fax.setText(mCompanyInfoActivity.company.fax);
        mCompanyInfoActivity.mail.setText(mCompanyInfoActivity.company.mail);
        mCompanyInfoActivity.web.setText(mCompanyInfoActivity.company.web);
        mCompanyInfoActivity.name.setText(mCompanyInfoActivity.company.name);
        mCompanyInfoActivity.ename.setText(mCompanyInfoActivity.company.ename);
        mCompanyInfoActivity.address.setText(mCompanyInfoActivity.company.address);
        mCompanyInfoActivity.better.setText(mCompanyInfoActivity.company.characteristic);
    }

    public void initProvience()
    {
        for(int i = 0 ; i < BigwinerApplication.mApp.allprovience.list.size() ; i++)
        {
            Select select = new Select(BigwinerApplication.mApp.allprovience.list.get(i).mId,BigwinerApplication.mApp.allprovience.list.get(i).mName);
            mCompanyInfoActivity.provienceSelect.list.add(select);
            mCompanyInfoActivity.provienceSelect.hashMap.put(select.mId,select);
            if(select.mName.equals(mCompanyInfoActivity.company.province))
            {
                select.iselect = true;
                mCompanyInfoActivity.sprovience = select;
            }
        }
    }

//    public void initCity() {
//        if(mCompanyInfoActivity.sprovience != null)
//        {
//            mCompanyInfoActivity.citySelect.clear();
//            MapSelect mapSelect = BigwinerApplication.mApp.allcity.get(mCompanyInfoActivity.sprovience.mName);
//            if(mapSelect != null)
//            {
//                for (int i = 0 ; i < mapSelect.list.size() ; i++)
//                {
//                    Select select = new Select(mapSelect.list.get(i).mId,mapSelect.list.get(i).mName);
//                    mCompanyInfoActivity.citySelect.list.add(select);
//                    mCompanyInfoActivity.citySelect.hashMap.put(select.mId,select);
//                    if(select.mName.equals(mCompanyInfoActivity.company.city))
//                    {
//                        select.iselect = true;
//                        mCompanyInfoActivity.scity = select;
//                    }
//                }
//            }
//        }
//    }

    public void initCity() {
        mCompanyInfoActivity.city.setText(mCompanyInfoActivity.getString(R.string.company_edit_city_hit));
        for (int i = 0; i < BigwinerApplication.mApp.ports.list.size(); i++) {
            Select select = new Select(BigwinerApplication.mApp.ports.list.get(i).mId, BigwinerApplication.mApp.ports.list.get(i).mName);
            mCompanyInfoActivity.citySelect.add(select);
            select.iselect = false;
            if (AppUtils.check(select.mName, BigwinerApplication.mApp.mAccount.city)) {
                select.iselect = true;
            }
            if (BigwinerApplication.mApp.mAccount.city.length() > 0)
                mCompanyInfoActivity.city.setText(BigwinerApplication.mApp.mAccount.city);
        }
    }

    public void startSelectCity() {
//        if(mCompanyInfoActivity.sprovience != null)
//        BigwinerApplication.mApp.startSelectViewCity(mCompanyInfoActivity,mCompanyInfoActivity.citySelect.list,mCompanyInfoActivity.getString(R.string.company_edit_city_hit),CompanyInfoActivity.ACTION_COMPANY_CITY_SELECT,true,true);
//        else
//            AppUtils.showMessage(mCompanyInfoActivity,mCompanyInfoActivity.getString(R.string.company_select_provience_before));
//        BigwinerApplication.mApp.startSelectView(mCompanyInfoActivity,mCompanyInfoActivity.citySelect.list,mCompanyInfoActivity.getString(R.string.company_edit_city_hit),CompanyInfoActivity.ACTION_COMPANY_CITY_SELECT,true,true);
        BigwinerApplication.mApp.startSelectView(mCompanyInfoActivity, BigwinerApplication.mApp.ports, mCompanyInfoActivity.getString(R.string.company_edit_city_hit), CompanyInfoActivity.ACTION_COMPANY_CITY_SELECT, true, true,1);
    }

    public void startSelectProvience() {
        BigwinerApplication.mApp.startSelectViewCity(mCompanyInfoActivity,mCompanyInfoActivity.provienceSelect.list,mCompanyInfoActivity.getString(R.string.company_edit_provience_hit),CompanyInfoActivity.ACTION_PROVIENCE_SELECT,true,true);
    }



    public void setCity(Intent intent)
    {
//        Select select = null;
//        if(intent.hasExtra("item"))
//            select = intent.getParcelableExtra("item");
//        if(select != null)
//        {
//            mCompanyInfoActivity.scity = select;
//            mCompanyInfoActivity.city.setText(select.mName);
//        }
//        else if(BigwinerApplication.mApp.city != null)
//        {
//            locationsss();
//        }
        Select select = intent.getParcelableExtra("item");
        mCompanyInfoActivity.city.setText(select.mName);
        BigwinerApplication.mApp.ports.updataSelect(mCompanyInfoActivity.city.getText().toString());
        if(mCompanyInfoActivity.city.getText().toString().length() == 0)
        {
            mCompanyInfoActivity.city.setText(mCompanyInfoActivity.getString(R.string.userinfo_city_hit));
        }

    }
    public void setProvience(Intent intent)
    {
        Select select = null;
        if(intent.hasExtra("item"))
            select = intent.getParcelableExtra("item");
        if(select != null)
        {
            mCompanyInfoActivity.sprovience = select;
            if(mCompanyInfoActivity.provience.getText().toString().equals(select.mName))
            {

            }
            else
            {
                mCompanyInfoActivity.provience.setText(select.mName);
                mCompanyInfoActivity.city.setText(mCompanyInfoActivity.getString(R.string.company_edit_city_hit));
                initCity();
            }
        }
        else
        {
            location();
        }

    }

    public void location()
    {
        if(mCompanyInfoActivity.sprovience != null)
        {
            mCompanyInfoActivity.sprovience.iselect = false;
        }
        mCompanyInfoActivity.sprovience = mCompanyInfoActivity.provienceSelect.hashMap.get(BigwinerApplication.mApp.provience.mId);
        mCompanyInfoActivity.sprovience.iselect = true;

        if(!mCompanyInfoActivity.provience.getText().toString().equals(mCompanyInfoActivity.sprovience.mName))
        {
            mCompanyInfoActivity.provience.setText(mCompanyInfoActivity.sprovience.mName);
            initCity();
            mCompanyInfoActivity.city.setText(mCompanyInfoActivity.scity.mName);
        }
        else
        {
            if(mCompanyInfoActivity.scity  != null)
            {
                mCompanyInfoActivity.scity.iselect = false;
            }
            mCompanyInfoActivity.scity = mCompanyInfoActivity.citySelect.hashMap.get(BigwinerApplication.mApp.city.mId);
            mCompanyInfoActivity.scity.iselect = true;
            mCompanyInfoActivity.city.setText(mCompanyInfoActivity.scity.mName);
        }
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.TAKE_PHOTO_HEAD:
            case MainActivity.TAKE_PHOTO_BG:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(BigwinerApplication.mApp.mFileUtils.takePhotoPath);
                    String name = file.getName().substring(0,file.getName().lastIndexOf("."));
                    if (requestCode == MainActivity.TAKE_PHOTO_HEAD) {
                        // 开启裁剪,设置requestCode为CROP_PHOTO
                        BigwinerApplication.mApp.mFileUtils.cropPhoto(mCompanyInfoActivity,1,1, BigwinerApplication.mApp.mFileUtils.takePhotoPath, BigwinerApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("chead/photo"),name).getPath()
                                ,(int) (60 * mCompanyInfoActivity.mBasePresenter.mScreenDefine.density), (int) (60 * mCompanyInfoActivity.mBasePresenter.mScreenDefine.density),MainActivity.CROP_HEAD);
                    } else {
                        // 开启裁剪,设置requestCode为CROP_PHOTO
                        BigwinerApplication.mApp.mFileUtils.cropPhoto(mCompanyInfoActivity,9,5, BigwinerApplication.mApp.mFileUtils.takePhotoPath, BigwinerApplication.mApp.mFileUtils.getOutputBgMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("cbg/photo"),name).getPath()
                                ,mCompanyInfoActivity.mBasePresenter.mScreenDefine.ScreenWidth, (int) (200*mCompanyInfoActivity.mBasePresenter.mScreenDefine.density),MainActivity.CROP_BG);
                    }
                }
                break;
            case MainActivity.CHOSE_PICTURE_HEAD:
            case MainActivity.CHOSE_PICTURE_BG:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mCompanyInfoActivity, data.getData());
                    File file = new File(path);
                    String name = file.getName().substring(0,file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        // 设置可缩放
                        if (requestCode == MainActivity.CHOSE_PICTURE_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            BigwinerApplication.mApp.mFileUtils.cropPhoto(mCompanyInfoActivity,1,1,path, BigwinerApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("chead/photo"),name).getPath()
                                    ,(int) (60 * mCompanyInfoActivity.mBasePresenter.mScreenDefine.density), (int) (60 * mCompanyInfoActivity.mBasePresenter.mScreenDefine.density),MainActivity.CROP_HEAD);
                        } else {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            BigwinerApplication.mApp.mFileUtils.cropPhoto(mCompanyInfoActivity,9,5,path, BigwinerApplication.mApp.mFileUtils.getOutputBgMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("cbg/photo"),name).getPath()
                                    ,mCompanyInfoActivity.mBasePresenter.mScreenDefine.ScreenWidth, (int) (200*mCompanyInfoActivity.mBasePresenter.mScreenDefine.density),MainActivity.CROP_BG);

                        }
                    }
                }
                break;
            case MainActivity.CROP_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> paths = BigwinerApplication.mApp.mFileUtils.getImage(data);
                    File mFile = new File(paths.get(0));
                    mCompanyInfoActivity.logoid = mFile.getPath();
                    BigwinerApplication.mApp.setContactHead(mCompanyInfoActivity,mFile,mCompanyInfoActivity.logo);
                }
                break;
            case MainActivity.CROP_BG:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> paths = BigwinerApplication.mApp.mFileUtils.getImage(data);
                    File mFile = new File(paths.get(0));
                    mCompanyInfoActivity.bgid = mFile.getPath();
                    BigwinerApplication.mApp.setContactBg(mCompanyInfoActivity,mFile,mCompanyInfoActivity.bg);
                }
                break;

        }
    }

    public void showSetHead() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = mCompanyInfoActivity.getString(R.string.my_take_photo);
        item1.mListener = mCompanyInfoActivity.mTakePhotoListenter;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = mCompanyInfoActivity.getString(R.string.my_photo_select);
        item1.mListener = mCompanyInfoActivity.mAddPicListener;
        items.add(item1);
        mCompanyInfoActivity.popupWindow = AppUtils.creatButtomMenu(mCompanyInfoActivity, mCompanyInfoActivity.shade, items, mCompanyInfoActivity.findViewById(R.id.activity_about));
    }

    public void showSetBg() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = mCompanyInfoActivity.getString(R.string.my_take_photo);
        item1.mListener = mCompanyInfoActivity.mTakePhotoListenter2;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = mCompanyInfoActivity.getString(R.string.my_photo_select);
        item1.mListener = mCompanyInfoActivity.mAddPicListener2;
        items.add(item1);
        mCompanyInfoActivity.popupWindow = AppUtils.creatButtomMenu(mCompanyInfoActivity, mCompanyInfoActivity.shade, items, mCompanyInfoActivity.findViewById(R.id.activity_about));
    }
}
