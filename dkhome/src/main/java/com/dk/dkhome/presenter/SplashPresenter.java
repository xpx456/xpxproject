package com.dk.dkhome.presenter;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;


import com.dk.dkhome.R;
import com.dk.dkhome.handler.SplashHandler;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.RegisterActivity;
import com.dk.dkhome.view.activity.SplashActivity;

import java.util.List;

import intersky.appbase.Presenter;
import intersky.apputils.UtilBitmap;
import intersky.apputils.UtilScreenCapture;
import intersky.guide.GuideUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

import static android.content.Context.ACTIVITY_SERVICE;


public class SplashPresenter implements Presenter {

    public SplashActivity mSplashActivity;
    public SplashHandler mSplashHandler;
    public SplashPresenter(SplashActivity mSplashActivity) {
        this.mSplashActivity = mSplashActivity;
        mSplashHandler = new SplashHandler(mSplashActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mSplashActivity, Color.parseColor("#00000000"));
        mSplashActivity.setContentView(R.layout.activity_splash);
        initFenView();
        mSplashHandler.sendEmptyMessageDelayed(SplashHandler.EVENT_START_CHECK,1000);


    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mSplashHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }


    public void startMain() {
        if (DkhomeApplication.mApp.mAccount.islogin == false) {
            Intent intent = makeIntent();
            intent.putExtra(RegisterActivity.IS_REGISTER,true);
            if (DkhomeApplication.mApp.showGuid == true) {
                GuideUtils.getInstance().startGuide2(DkhomeApplication.mApp.guidePics, mSplashActivity, "com.dk.dkhome.view.activity.RegisterActivity", intent);
            } else {
                intent.setClass(mSplashActivity, RegisterActivity.class);
                mSplashActivity.startActivity(intent);

            }
        } else {
            Intent intent =  mSplashActivity.mSplashPresenter.makeIntent();
            if(DkhomeApplication.mApp.showGuid == true)
                GuideUtils.getInstance().startGuide2(DkhomeApplication.mApp.guidePics, mSplashActivity,"com.dk.dkhome.view.activity.MainActivity",intent);
            else
            {
                intent.setClass(mSplashActivity,MainActivity.class);
                mSplashActivity.startActivity(intent);
            }
        }
        mSplashActivity.finish();
    }

    public Intent makeIntent() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        if (mSplashActivity.val != null) {
//            if(measureType(mSplashActivity.val[0]).length() > 0)
//            {
//                intent.putExtra("shareopen", true);
//                intent.putExtra("type", measureType(mSplashActivity.val[0]));
//                intent.putExtra("detialid", mSplashActivity.val[1]);
//                if(mSplashActivity.val[0].equals("news") || mSplashActivity.val[0].equals("notice"))
//                    intent.putExtra("typemodule","NewsManage");
//            }
//        }
//        else
        {
            if(mSplashActivity.getIntent().hasExtra("type"))
            {
//                intent.putExtra("shareopen", true);
//                intent.putExtra("type", mSplashActivity.getIntent().getStringExtra("type"));
//                intent.putExtra("detialid", mSplashActivity.getIntent().getStringExtra("detialid"));
//                intent.putExtra("typemodule", mSplashActivity.getIntent().getStringExtra("typemodule"));
            }
        }
        return intent;
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

    public void initFenView() {
        Bitmap bitmap = UtilScreenCapture.getDrawing(mSplashActivity.findViewById(R.id.fen));
        ImageView mImageView = (ImageView) mSplashActivity.findViewById(R.id.fen);
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
            UtilBitmap.blurImageView(mSplashActivity, mImageView, 25, 0xaaffffff);
        } else
            mImageView.setBackgroundColor(0xaaffffff);
    }

}
