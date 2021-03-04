package com.bigwiner.android.presenter;


import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.handler.PopPushHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.PopPushActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.interksy.autoupdate.UpDataManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import intersky.appbase.AppActivityManager;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.TimeUtils;
import intersky.conversation.NotifictionManager;
import intersky.guide.GuideUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

import static android.content.Context.ACTIVITY_SERVICE;

public class PopPushPresenter implements Presenter {

    private PopPushActivity mPopPushActivity;
    public PopPushHandler mPopPushHandler;

    public PopPushPresenter(PopPushActivity mPopPushActivity) {
        this.mPopPushActivity = mPopPushActivity;
        mPopPushHandler = new PopPushHandler(mPopPushActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mPopPushActivity.setContentView(R.layout.activity_pup);
        if (Build.VERSION.SDK_INT < 21)
        {
            AppUtils.showMessage(mPopPushActivity,"系统版本过低，无法使用该软件");
            BigwinerApplication.mApp.exist();
            return;
        }

        if(isExistMainActivity(MainActivity.class))
        {
            Intent intent = makeIntent();
            intent.setClass(mPopPushActivity,MainActivity.class);
            mPopPushActivity.startActivity(intent);
        }
        else
        {
            AppUtils.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mPopPushActivity, mPopPushHandler.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE, mPopPushHandler);
        }

    }


    public void initdata() {
        ImageView imageView = mPopPushActivity.findViewById(R.id.bg);
        float a = AppActivityManager.getInstance().mScreenDefine.ScreenHeight / AppActivityManager.getInstance().mScreenDefine.ScreenWidth;
        if (a > 1.9) {
            imageView.setImageResource(R.drawable.guide21);
            imageView.setBackgroundResource(R.drawable.guide21);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.guide21).signature(new MediaStoreSignature(PopPushActivity.key, TimeUtils.getDateId(), UpDataManager.mUpDataManager.oldVersionCode)).diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mPopPushActivity).load(PopPushActivity.BG_PATH_21).apply(options).into(imageView);
        } else if (a > 1.77) {
            imageView.setImageResource(R.drawable.guide);
            imageView.setBackgroundResource(R.drawable.guide);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.guide).signature(new MediaStoreSignature(PopPushActivity.key, TimeUtils.getDateId(), UpDataManager.mUpDataManager.oldVersionCode)).diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mPopPushActivity).load(PopPushActivity.BG_PATH_16).apply(options).into(imageView);

        } else {
            imageView.setImageResource(R.drawable.guide_s);
            imageView.setBackgroundResource(R.drawable.guide_s);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.guide_s).signature(new MediaStoreSignature(PopPushActivity.key, TimeUtils.getDateId(), UpDataManager.mUpDataManager.oldVersionCode)).diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mPopPushActivity).load(PopPushActivity.BG_PATH_S).apply(options).into(imageView);
        }

    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
//		MobclickAgent.onResume(mPopPushActivity);
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
//		MobclickAgent.onPause(mPopPushActivity);
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        AppActivityManager.getInstance().finishActivity(mPopPushActivity);
        mPopPushHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        AppActivityManager.getInstance().addActivity(mPopPushActivity);
    }


    public void startMain() {
        if (BigwinerApplication.mApp.mAccount.islogin == false) {
            Intent intent = makeIntent();
            if (BigwinerApplication.mApp.GUIDE_OPEN == true) {
                GuideUtils.getInstance().startGuide(BigwinerApplication.mApp.guidePics, mPopPushActivity, "com.bigwiner.android.view.activity.LoginActivity", intent);
            } else {

                intent.setClass(mPopPushActivity,LoginActivity.class);
                mPopPushActivity.startActivity(intent);

            }
            mPopPushActivity.finish();
        } else {
//            getUserNet(BigwinerApplication.mApp.mAccount.mUserName, BigwinerApplication.mApp.mAccount.mPassword);
            Intent intent =  mPopPushActivity.mPopPushPresenter.makeIntent();
            if(BigwinerApplication.GUIDE_OPEN == true)
                GuideUtils.getInstance().startGuide(BigwinerApplication.mApp.guidePics, mPopPushActivity,"com.bigwiner.android.view.activity.MainActivity",intent);
            else
            {
                intent.setClass(mPopPushActivity,MainActivity.class);
                mPopPushActivity.startActivity(intent);
            }

            LoginAsks.doPushlogin(mPopPushActivity.mPopPushPresenter.mPopPushHandler,mPopPushActivity);
        }
    }

    private void getUserNet(String username, String password) {
        SharedPreferences sharedPre = mPopPushActivity.getSharedPreferences(UserDefine.LAST_USER, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(UserDefine.USER_NAME, username);
        e.commit();
        SharedPreferences sharedPre1 = mPopPushActivity.getSharedPreferences(username, 0);
        SharedPreferences.Editor e1 = sharedPre1.edit();
        e1.putString(UserDefine.USER_PASSWORD, password);
        e1.commit();
        LoginAsks.getUserInfo(mPopPushActivity, mPopPushHandler);

    }

    public void praseIntent(Intent intent) {
        Uri schemeUri = intent.getData();
        if (schemeUri != null) {
            String a = schemeUri.toString();
            int b = a.indexOf("oia.cargounions.com/");
            String url = a.substring(b + 20, a.length());
            mPopPushActivity.val = url.split("/");
        } else {
            mPopPushActivity.val = null;
        }
    }

    public Intent makeIntent() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (mPopPushActivity.val != null) {
            if(measureType(mPopPushActivity.val[0]).length() > 0)
            {
                intent.putExtra("shareopen", true);
                intent.putExtra("type", measureType(mPopPushActivity.val[0]));
                intent.putExtra("detialid", mPopPushActivity.val[1]);
                if(mPopPushActivity.val[0].equals("news") || mPopPushActivity.val[0].equals("notice"))
                intent.putExtra("typemodule","NewsManage");
            }
        }
        else
        {
            if(mPopPushActivity.getIntent().hasExtra("type"))
            {
                intent.putExtra("shareopen", true);
                intent.putExtra("type", mPopPushActivity.getIntent().getStringExtra("type"));
                intent.putExtra("detialid", mPopPushActivity.getIntent().getStringExtra("detialid"));
                intent.putExtra("typemodule", mPopPushActivity.getIntent().getStringExtra("typemodule"));
            }
            else if(mPopPushActivity.map != null)
            {
                intent.putExtra("shareopen", true);
                String a = mPopPushActivity.map.get("data");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(a);
                    JSONObject msg = jsonObject.getJSONObject("message");
                    intent.putExtra("id", msg.getString("message_id"));
                    String type = jsonObject.getString("module");
                    if(jsonObject.getString("module").equals("MemberManager"))
                    {
                        intent.putExtra("detialid",msg.getString("source_id"));
                    }
                    else
                    {
                        intent.putExtra("detialid",jsonObject.getString("module_id"));
                    }
                    if(type.toLowerCase().contains("membermanager"))
                    {
                        intent.putExtra("type","msg_message");
                    }
                    else if(type.toLowerCase().contains("newsmanage"))
                    {
                        intent.putExtra("type","msg_news");
                    }
                    else if(type.toLowerCase().contains("conferencemanage"))
                    {
                        intent.putExtra("type","msg_meeting");
                    }
                    else if(type.toLowerCase().contains("resourcesinfo"))
                    {
                        intent.putExtra("type","msg_resource");
                    }
                    intent.putExtra("typemodule",type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        Intent intent = new Intent(mPopPushActivity, activity);
        ComponentName cmpName = intent.resolveActivity(mPopPushActivity.getPackageManager());
        boolean flag = false;
        if (cmpName != null) {// 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) mPopPushActivity.getSystemService(ACTIVITY_SERVICE);
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
