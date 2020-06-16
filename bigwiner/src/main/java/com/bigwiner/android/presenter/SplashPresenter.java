package com.bigwiner.android.presenter;


import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.handler.SplashHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SplashActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.interksy.autoupdate.UpDataManager;

import java.io.File;
import java.util.List;

import intersky.appbase.AppActivityManager;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.TimeUtils;
import intersky.guide.GuideUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

import static android.content.Context.ACTIVITY_SERVICE;

public class SplashPresenter implements Presenter {

    private SplashActivity mSplashActivity;
    public SplashHandler mSplashHandler;

    public SplashPresenter(SplashActivity mSplashActivity) {
        this.mSplashActivity = mSplashActivity;
        mSplashHandler = new SplashHandler(mSplashActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mSplashActivity, Color.argb(0, 255, 255, 255));
        if (Build.VERSION.SDK_INT < 21)
        {
            AppUtils.showMessage(mSplashActivity,"系统版本过低，无法使用该软件");
            BigwinerApplication.mApp.exist();
            return;
        }
        mSplashActivity.setContentView(R.layout.activity_splash);
        mSplashActivity.mToolBarHelper.hidToolbar(mSplashActivity, (RelativeLayout) mSplashActivity.findViewById(R.id.buttomaciton));
        mSplashActivity.measureStatubar(mSplashActivity, (RelativeLayout) mSplashActivity.findViewById(R.id.stutebar));

        if(isExistMainActivity(MainActivity.class))
        {
            Intent intent = makeIntent();
            intent.setClass(mSplashActivity,MainActivity.class);
            mSplashActivity.startActivity(intent);
        }
        else
        {
            AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mSplashActivity, SplashHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mSplashHandler);
        }

    }


    public void initdata() {

        ImageView imageView = mSplashActivity.findViewById(R.id.bg);
        float a = AppActivityManager.getInstance().mScreenDefine.ScreenHeight / AppActivityManager.getInstance().mScreenDefine.ScreenWidth;
        long maxMemory = Runtime.getRuntime().maxMemory()/1024/1024;//获取系统分配给应用的总内存大小
        long mem = 1024*2;
        if(maxMemory <= mem)
        {
            imageView.setBackgroundResource(R.drawable.guide720);

        }
        else
        {
            if (a > 1.9) {
                //imageView.setImageResource(R.drawable.guide21);
                imageView.setBackgroundResource(R.drawable.guide21);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.guide21).signature(new MediaStoreSignature(SplashActivity.key, TimeUtils.getDateId(), UpDataManager.mUpDataManager.oldVersionCode)).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mSplashActivity).load(SplashActivity.BG_PATH_21).apply(options).into(imageView);
            } else if (a > 1.77) {
                //imageView.setImageResource(R.drawable.guide);
                imageView.setBackgroundResource(R.drawable.guide);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.guide).signature(new MediaStoreSignature(SplashActivity.key, TimeUtils.getDateId(), UpDataManager.mUpDataManager.oldVersionCode)).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mSplashActivity).load(SplashActivity.BG_PATH_16).apply(options).into(imageView);

            } else {
                //imageView.setImageResource(R.drawable.guide_s);
                imageView.setBackgroundResource(R.drawable.guide_s);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.guide_s).signature(new MediaStoreSignature(SplashActivity.key, TimeUtils.getDateId(), UpDataManager.mUpDataManager.oldVersionCode)).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mSplashActivity).load(SplashActivity.BG_PATH_S).apply(options).into(imageView);
            }
        }



    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
//		MobclickAgent.onResume(mSplashActivity);
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
//		MobclickAgent.onPause(mSplashActivity);
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        AppActivityManager.getInstance().finishActivity(mSplashActivity);
        mSplashHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        AppActivityManager.getInstance().addActivity(mSplashActivity);
    }


    public void startMain() {
        if (BigwinerApplication.mApp.mAccount.islogin == false) {
            mSplashActivity.finish();
            Intent intent = makeIntent();
            if (BigwinerApplication.mApp.GUIDE_OPEN == true) {
                GuideUtils.getInstance().startGuide(BigwinerApplication.mApp.guidePics, mSplashActivity, "com.bigwiner.android.view.activity.LoginActivity", intent);
            } else {
                intent.setClass(mSplashActivity,LoginActivity.class);
                mSplashActivity.startActivity(intent);

            }
        } else {
//            getUserNet(BigwinerApplication.mApp.mAccount.mUserName, BigwinerApplication.mApp.mAccount.mPassword);
            Intent intent =  mSplashActivity.mSplashPresenter.makeIntent();
            if(BigwinerApplication.GUIDE_OPEN == true)
                GuideUtils.getInstance().startGuide(BigwinerApplication.mApp.guidePics, mSplashActivity,"com.bigwiner.android.view.activity.MainActivity",intent);
            else
            {
                intent.setClass(mSplashActivity,MainActivity.class);
                mSplashActivity.startActivity(intent);
            }
            LoginAsks.doPushlogin(mSplashActivity.mSplashPresenter.mSplashHandler,mSplashActivity);
        }
    }

    private void getUserNet(String username, String password) {
        SharedPreferences sharedPre = mSplashActivity.getSharedPreferences(UserDefine.LAST_USER, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(UserDefine.USER_NAME, username);
        e.commit();
        SharedPreferences sharedPre1 = mSplashActivity.getSharedPreferences(username, 0);
        SharedPreferences.Editor e1 = sharedPre1.edit();
        e1.putString(UserDefine.USER_PASSWORD, password);
        e1.commit();
        LoginAsks.getUserInfo(mSplashActivity, mSplashHandler);

    }

    public void praseIntent(Intent intent) {
        Uri schemeUri = intent.getData();
        if (schemeUri != null) {
            String a = schemeUri.toString();
            int b = a.indexOf("oia.cargounions.com/");
            String url = a.substring(b + 20, a.length());
            mSplashActivity.val = url.split("/");
        } else {
            mSplashActivity.val = null;
        }
    }

    public Intent makeIntent() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (mSplashActivity.val != null) {
            if(measureType(mSplashActivity.val[0]).length() > 0)
            {
                intent.putExtra("shareopen", true);
                intent.putExtra("type", measureType(mSplashActivity.val[0]));
                intent.putExtra("detialid", mSplashActivity.val[1]);
                if(mSplashActivity.val[0].equals("news") || mSplashActivity.val[0].equals("notice"))
                    intent.putExtra("typemodule","NewsManage");
            }
        }
        else
        {
            if(mSplashActivity.getIntent().hasExtra("type"))
            {
                intent.putExtra("shareopen", true);
                intent.putExtra("type", mSplashActivity.getIntent().getStringExtra("type"));
                intent.putExtra("detialid", mSplashActivity.getIntent().getStringExtra("detialid"));
                intent.putExtra("typemodule", mSplashActivity.getIntent().getStringExtra("typemodule"));
            }
        }
        return intent;
    }

    public String measureType(String type) {
        if(type.equals("news"))
        {
            return Conversation.CONVERSATION_TYPE_NEWS;
        }
        else if(type.equals("notice"))
        {
            return Conversation.CONVERSATION_TYPE_NOTICE;
        }
        else if(type.equals("meeting"))
        {
            return Conversation.CONVERSATION_TYPE_MEETING;
        }
        else if(type.equals("user"))
        {
            return Conversation.CONVERSATION_TYPE_CONTACT;
        }
        else if(type.equals("resources"))
        {
            return Conversation.CONVERSATION_TYPE_RESOURCE;
        }
        else if(type.equals("company"))
        {
            return Conversation.CONVERSATION_TYPE_COMPANY;
        }
        else
        {
            return "";
        }
    }

    private boolean isExistMainActivity(Class<?> activity) {
        Intent intent = new Intent(mSplashActivity, activity);
        ComponentName cmpName = intent.resolveActivity(mSplashActivity.getPackageManager());
        boolean flag = false;
        if (cmpName != null) {// 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) mSplashActivity.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);//获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) {// 说明它已经启动了
                    flag = true;
                    break;//跳出循环，优化效率
                }
            }
        }
        return flag;//true 存在 falese 不存在
    }
}
