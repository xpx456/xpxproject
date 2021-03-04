package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.CompanyInfoPresenter;
import com.bigwiner.android.view.BigwinerApplication;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.BaseActivity;
import intersky.select.entity.MapSelect;
import intersky.select.entity.Select;

/**
 * Created by xpx on 2017/8/18.
 */

public class CompanyInfoActivity extends BaseActivity {

    public static final String ACTION_COMPANY_CITY_SELECT = "ACTION_COMPANY_CITY_SELECT";
    public static final String ACTION_PROVIENCE_SELECT = "ACTION_PROVIENCE_SELECT";
    public CompanyInfoPresenter mCompanyInfoPresenter = new CompanyInfoPresenter(this);
    public Company company;
    public ImageView back;
    public RelativeLayout logobtn;
    public ImageView logo;
    public RelativeLayout bgbtn;
    public ImageView bg;
    public String logoid;
    public String bgid;
    public RelativeLayout proviencebtn;
    public RelativeLayout citybtn;
    public TextView provience;
    public TextView city;

    public EditText taxno;
    public EditText sailno;
    public EditText phone;
    public EditText fax;
    public EditText mail;
    public EditText web;
    public EditText name;
    public EditText ename;
    public EditText address;
    public EditText better;

    public TextView btnSubmit;
    public Meeting meeting;
    public RelativeLayout shade;
    public PopupWindow popupWindow;
    public MapSelect citySelect = new MapSelect();
    public MapSelect provienceSelect = new MapSelect();
    public Select scity;
    public Select sprovience;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompanyInfoPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mCompanyInfoPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    public View.OnClickListener citySelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCompanyInfoPresenter.startSelectCity();
        }
    };


    public View.OnClickListener provienceSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCompanyInfoPresenter.startSelectProvience();
        }
    };

    public View.OnClickListener submintListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCompanyInfoPresenter.doSubmit();
        }
    };

    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mCompanyInfoPresenter.mCompanyInfoActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),MainActivity.TAKE_PHOTO_HEAD);
            mCompanyInfoPresenter.mCompanyInfoActivity.popupWindow.dismiss();
        }
    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.mFileUtils.selectPhotos(mCompanyInfoPresenter.mCompanyInfoActivity,MainActivity.CHOSE_PICTURE_HEAD);
            mCompanyInfoPresenter.mCompanyInfoActivity.popupWindow.dismiss();
        }
    };

    public View.OnClickListener mTakePhotoListenter2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mCompanyInfoPresenter.mCompanyInfoActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),MainActivity.TAKE_PHOTO_BG);
            mCompanyInfoPresenter.mCompanyInfoActivity.popupWindow.dismiss();
        }
    };

    public View.OnClickListener mAddPicListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.mFileUtils.selectPhotos(mCompanyInfoPresenter.mCompanyInfoActivity,MainActivity.CHOSE_PICTURE_BG);
            mCompanyInfoPresenter.mCompanyInfoActivity.popupWindow.dismiss();
        }
    };

    public View.OnClickListener changeHeadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCompanyInfoPresenter.showSetHead();
        }
    };


    public View.OnClickListener changeBgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCompanyInfoPresenter.showSetBg();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mCompanyInfoPresenter.takePhotoResult(requestCode,resultCode,data);

    }
}
