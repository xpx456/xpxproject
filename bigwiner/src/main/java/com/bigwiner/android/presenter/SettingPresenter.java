package com.bigwiner.android.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.handler.SettingHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AboutActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.PasswordActivity;
import com.bigwiner.android.view.activity.SettingActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.conversation.database.DBHelper;
import intersky.function.presenter.BusinessWarnPresenter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SettingPresenter implements Presenter {

    public SettingActivity mSettingActivity;
    public SettingHandler settingHandler;

    public SettingPresenter(SettingActivity mSettingActivity)
    {
        this.mSettingActivity = mSettingActivity;
        this.settingHandler = new SettingHandler(mSettingActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSettingActivity, Color.argb(0, 255, 255, 255));
        mSettingActivity.setContentView(R.layout.activity_setting);
        mSettingActivity.mToolBarHelper.hidToolbar(mSettingActivity, (RelativeLayout) mSettingActivity.findViewById(R.id.buttomaciton));
        mSettingActivity.measureStatubar(mSettingActivity, (RelativeLayout) mSettingActivity.findViewById(R.id.stutebar));
        mSettingActivity.back = mSettingActivity.findViewById(R.id.back);
        mSettingActivity.back.setOnClickListener(mSettingActivity.backListener);
        mSettingActivity.shade = (RelativeLayout) mSettingActivity.findViewById(R.id.shade);
        mSettingActivity.userinfoBtn = mSettingActivity.findViewById(R.id.userinfo);
        mSettingActivity.passwordBtn = mSettingActivity.findViewById(R.id.password);
        mSettingActivity.cleanBtn = mSettingActivity.findViewById(R.id.cleanchat);
        mSettingActivity.aboutBtn = mSettingActivity.findViewById(R.id.about);
        mSettingActivity.existBtn = mSettingActivity.findViewById(R.id.exist);
        mSettingActivity.confirmBtn = mSettingActivity.findViewById(R.id.confirm);

        mSettingActivity.userinfoBtn.setOnClickListener(mSettingActivity.userinfoListener);
        mSettingActivity.passwordBtn.setOnClickListener(mSettingActivity.passwordListener);
        mSettingActivity.cleanBtn.setOnClickListener(mSettingActivity.cleanListener);
        mSettingActivity.aboutBtn.setOnClickListener(mSettingActivity.aboutListener);
        mSettingActivity.existBtn.setOnClickListener(mSettingActivity.existListener);
        mSettingActivity.confirmBtn.setOnClickListener(mSettingActivity.confrimListener);

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

    public void userinfo()
    {
        Intent intent = new Intent(mSettingActivity, UserInfoActivity.class);
        mSettingActivity.startActivity(intent);
    }

    public void password()
    {
        Intent intent = new Intent(mSettingActivity, PasswordActivity.class);
        mSettingActivity.startActivity(intent);
    }

    public void clean()
    {
        AppUtils.creatDialogTowButton(mSettingActivity,mSettingActivity.getString(R.string.setting_cleanchat_title),mSettingActivity.getString(R.string.setting_cleanchat)
                ,mSettingActivity.getString(R.string.button_no),mSettingActivity.getString(R.string.button_yes),null, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.setAction(MainActivity.ACTION_SET_CLEANMESSAGE);
                        intent.setPackage(BigwinerApplication.mApp.getPackageName());
                        mSettingActivity.sendBroadcast(intent);
                        AppUtils.showMessage(mSettingActivity,mSettingActivity.getString(R.string.setting_cleanchat_success));
                    }
                });

    }

    public void about()
    {
        Intent intent = new Intent(mSettingActivity, AboutActivity.class);
        mSettingActivity.startActivity(intent);
    }

    public void exist()
    {
        AppUtils.creatDialogTowButton(mSettingActivity,mSettingActivity.getString(R.string.setting_exast_title),mSettingActivity.getString(R.string.setting_exist)
                ,mSettingActivity.getString(R.string.button_no),mSettingActivity.getString(R.string.button_yes),null, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.ACTION_LOGIN_OUT);
                        intent.setPackage(BigwinerApplication.mApp.getPackageName());
                        mSettingActivity.sendBroadcast(intent);
                    }
                });

    }


    public void confirm() {
        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = mSettingActivity.getString(R.string.my_take_photo);
        item1.mListener = mSettingActivity.mTakePhotoListenter;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = mSettingActivity.getString(R.string.my_photo_select);
        item1.mListener = mSettingActivity.mAddPicListener;
        items.add(item1);
        mSettingActivity.popupWindow = AppUtils.creatButtomMenu(mSettingActivity, mSettingActivity.shade, items, mSettingActivity.findViewById(R.id.activity_about));
    }

    public void takePhoto() {
        BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mSettingActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("businesscard"),SettingActivity.TAKE_PHOTO_CONFRIM);
        mSettingActivity.popupWindow.dismiss();
    }

    public void pickPhoto() {
        BigwinerApplication.mApp.mFileUtils.selectPhotos(mSettingActivity,SettingActivity.CHOSE_PHOTO_CONFRIM);
        mSettingActivity.popupWindow.dismiss();
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SettingActivity.TAKE_PHOTO_CONFRIM:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(BigwinerApplication.mApp.mFileUtils.takePhotoPath);
                    LoginAsks.setUploadConfirm(mSettingActivity,mSettingActivity.mSettingPresenter.settingHandler,file);
                }
                break;
            case SettingActivity.CHOSE_PHOTO_CONFRIM:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mSettingActivity, data.getData());
                    File file = new File(path);
                    LoginAsks.setUploadConfirm(mSettingActivity,mSettingActivity.mSettingPresenter.settingHandler,file);
                }
                break;

        }
    }
}
