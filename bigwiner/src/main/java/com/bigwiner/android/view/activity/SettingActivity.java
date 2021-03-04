package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bigwiner.android.presenter.SettingPresenter;

import intersky.appbase.BaseActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class SettingActivity extends BaseActivity {

    public static final String ACTION_SETTING_APPLY_SUCCESS = "ACTION_SETTING_APPLY_SUCCESS";
    public static final int TAKE_PHOTO_CONFRIM = 0x10;
    public static final int CHOSE_PHOTO_CONFRIM = 0x11;
    public SettingPresenter mSettingPresenter = new SettingPresenter(this);
    public ListView listView;
    public ImageView back;
    public RelativeLayout userinfoBtn;
    public RelativeLayout passwordBtn;
    public RelativeLayout cleanBtn;
    public RelativeLayout existBtn;
    public RelativeLayout aboutBtn;
    public RelativeLayout confirmBtn;
    public PopupWindow popupWindow;
    public RelativeLayout shade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSettingPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener userinfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSettingPresenter.userinfo();
        }
    };

    public View.OnClickListener passwordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSettingPresenter.password();
        }
    };

    public View.OnClickListener cleanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSettingPresenter.clean();
        }
    };

    public View.OnClickListener aboutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSettingPresenter.about();
        }
    };

    public View.OnClickListener existListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSettingPresenter.exist();
        }
    };

    public View.OnClickListener confrimListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSettingPresenter.confirm();
        }
    };

    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSettingPresenter.takePhoto();
        }
    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSettingPresenter.pickPhoto();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mSettingPresenter.takePhotoResult(requestCode,resultCode,data);

    }
}
