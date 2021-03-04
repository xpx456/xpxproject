package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.UserInfoPresenter;
import com.bigwiner.android.view.BigwinerApplication;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.Actions;
import intersky.appbase.BaseActivity;
import intersky.select.entity.Select;

/**
 * Created by xpx on 2017/8/18.
 */

public class UserInfoActivity extends BaseActivity {

    public static final String ACTION_SEX_SELECT = "ACTION_SEX_SELECT";
    public static final String ACTION_CITY_SELECT = "ACTION_CITY_SELECT";
    public static final String ACTION_AREA_SELECT = "ACTION_AREA_SELECT";
    public static final String ACTION_TYPE_SELECT = "ACTION_TYPE_SELECT";
    public static final String ACTION_POSITION_SELECT = "ACTION_POSITION_SELECT";
    public static final String ACTION_COMPANY_SELECT = "ACTION_COMPANY_SELECT";
    public UserInfoPresenter mUserInfoPresenter = new UserInfoPresenter(this);
    public ImageView back;
    public RelativeLayout headbtn;
    public ImageView head;
    public String headid;
    public String cid = "";
    public RelativeLayout sexbtn;
    public RelativeLayout citybtn;
    public RelativeLayout typebtn;
    public RelativeLayout areabtn;
    public RelativeLayout positionbtn;
    public RelativeLayout companybtn;
    public EditText name;
    public EditText mail;
    public TextView mobil;
    public TextView sex;
    public TextView city;
    public TextView type;
    public TextView area;
    public TextView position;
    public TextView cname;
    public TextView save;
    public EditText memo;
    public TextView btnSubmit;
    public Meeting meeting;
    public RelativeLayout shade;
    public PopupWindow popupWindow;
    public ArrayList<Select> sexSelect = new ArrayList<Select>();
    public ArrayList<Select> citySelect = new ArrayList<Select>();
    public ArrayList<Select> typeSelect = new ArrayList<Select>();
    public ArrayList<Select> areaSelect = new ArrayList<Select>();
    public ArrayList<Select> companySelect = new ArrayList<Select>();
    public ArrayList<Select> positionSelect = new ArrayList<Select>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserInfoPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mUserInfoPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener sexSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.startSelectSex();
        }
    };

    public View.OnClickListener citySelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.startSelectCity();
        }
    };

    public View.OnClickListener positionSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.startSelectPosition();
        }
    };

    public View.OnClickListener areaSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.startSelectArea();
        }
    };

    public View.OnClickListener companySelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.startSelectCompany();
        }
    };

    public View.OnClickListener typeSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.startSelectType();
        }
    };

    public View.OnClickListener submintListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.doSubmit();
        }
    };

    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mUserInfoPresenter.mUserInfoActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),MainActivity.TAKE_PHOTO_HEAD);
            mUserInfoPresenter.mUserInfoActivity.popupWindow.dismiss();
        }
    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.mFileUtils.selectPhotos(mUserInfoPresenter.mUserInfoActivity,MainActivity.CHOSE_PICTURE_HEAD);
            mUserInfoPresenter.mUserInfoActivity.popupWindow.dismiss();
        }
    };

    public View.OnClickListener changeHeadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.showSetHead();
        }
    };





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        mUserInfoPresenter.takePhotoResult(requestCode, resultCode, data);

    }
}
