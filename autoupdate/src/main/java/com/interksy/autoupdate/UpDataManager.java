package com.interksy.autoupdate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import intersky.appbase.AppActivityManager;
import intersky.appbase.GetProvideGetPath;
import intersky.apputils.AppUtils;
import intersky.filetools.FileUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.NetTask;

public class UpDataManager {

    public String CHECK_VERSION_URL = "http://yimi.intersky.com.cn/static/yimi.txt";
    public String UPDATA_APP_URL = "http://yimi.intersky.com.cn/static/yimi.apk";
    public static final String UPDATE_INFO = "update_info";
    public static final String UPDATE_VNAME = "update_vname";
    public static final String UPDATE_VCODE = "update_vcode";
    public static final int UPDATA_DOWNLOAD = 1;
    public static final int UPDATA_FINISH = 2;
    public static final int UPDATA_NONE = 3;
    public static final int UPDATA_LATER = 4;
    public String oldVersionName = "";
    public int oldVersionCode = 0;
    public String updataVersionName = "";
    public int updataVersionCode = 0;
    public long updataSize = 0;
    public String udataMsg = "";
    public volatile static UpDataManager mUpDataManager = null;
    public String path;
    public Context context;
    public UpdateHandler handler = new UpdateHandler();
    public UpdataDownloadThread mDownloadTask;
    public NotificationOperation updataOperation;
    public int state = UPDATA_NONE;
    public boolean docheck = false;
    public GetProvideGetPath getProvideGetPath;
    public static UpDataManager init(Context context,String url1,String url2,String versionname,int versioncode,String path,NotificationOperation updataOperation,GetProvideGetPath getProvideGetPath) {

        if (mUpDataManager == null) {
            synchronized (UpDataManager.class) {
                if (mUpDataManager == null) {
                    mUpDataManager = new UpDataManager(context,url1,url2,versionname,versioncode,path,updataOperation,getProvideGetPath);
                }
                else
                {
                    mUpDataManager.CHECK_VERSION_URL = url1;
                    mUpDataManager.UPDATA_APP_URL = url2;
                    mUpDataManager.path = path;
                    mUpDataManager.getProvideGetPath = getProvideGetPath;
                    mUpDataManager.oldVersionCode = versioncode;
                    mUpDataManager.oldVersionName = versionname;
                    mUpDataManager.context = context;
                    mUpDataManager.updataOperation = updataOperation;
                }
            }
        }
        return mUpDataManager;
    }


    public UpDataManager(Context context,String url1,String url2,String versionname,int versioncode,String path,NotificationOperation updataOperation,GetProvideGetPath getProvideGetPath) {
        CHECK_VERSION_URL = url1;
        UPDATA_APP_URL = url2;
        this.path = path;
        this.getProvideGetPath = getProvideGetPath;
        this.oldVersionCode = versioncode;
        this.oldVersionName = versionname;
        this.context = context;
        this.updataOperation = updataOperation;
    }

    public void checkVersin() {
        if(docheck == false && state == UPDATA_NONE)
        {
            docheck = true;
            NetTask mNetTask = new NetTask(mUpDataManager.CHECK_VERSION_URL, mUpDataManager.handler, UpdateHandler.EVENT_CHECK_UPDATA,
                    mUpDataManager.context);
            NetTaskManager.getInstance().addNetTask(mNetTask);

        }

    }


    public void initJson(NetObject netObject) {
        try {
            String json = netObject.result;
            JSONObject jObject = new JSONObject(json);
            UpDataModel mUpDataModel = new UpDataModel(jObject.getInt("versioncode")
                    , jObject.getString("version"), jObject.getString("msg"));
            updataVersionName = jObject.getString("version");
            updataVersionCode = jObject.getInt("versioncode");
            updataSize = jObject.getInt("size");
            udataMsg = jObject.getString("msg");
            if (updataVersionCode > oldVersionCode) {

                if(checkDownloadApkImf(context,updataVersionName,updataVersionCode))
                {
                    state = UPDATA_FINISH;
                    doUpDataAppView();

                }
                else
                {
                    if(NetUtils.checkNetWorkState(context))
                    {
                        if(NetUtils.getNetType(context) == ConnectivityManager.TYPE_WIFI)
                        {
                            doUpDataApp();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AppActivityManager.getInstance().getCurrentActivity(),5);
                            builder.setMessage(context.getString(R.string.net_network_mobil)+updataVersionName+"\r\n");
                            builder.setNegativeButton(context.getString(R.string.keyword_updata), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    doUpDataApp();
                                }
                            });
                            builder.setPositiveButton(context.getString(R.string.keyword_updata_later), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }
                            });
                            builder.create().show();
                            state = UPDATA_LATER;
                        }
                    }


                }

            }
            state =  UPDATA_NONE;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            state =  UPDATA_NONE;
        }
    }

    private boolean canDownloadState(Context ctx) {
        try {
            int state = ctx.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void doUpDataApp() {

        if(mDownloadTask == null)
        {
            state = UPDATA_DOWNLOAD;
            mDownloadTask = new UpdataDownloadThread(path,UPDATA_APP_URL,updataSize);
            saveDownloadApkImf();
            mDownloadTask.start();
        }
    }

    public  boolean checkDownloadApkImf(Context mContext, String versionname, int versioncode)
    {
        SharedPreferences sharedPre = mContext.getSharedPreferences(UPDATE_INFO, 0);
        int vc = sharedPre.getInt(UPDATE_VCODE,0);
        String vn = sharedPre.getString(UPDATE_VNAME,"");
        if(versioncode == vc)
        {
            if(versionname.equals(vn))
            {
                File file = new File(path);
                if(file.exists())
                {
                    long a = file.length();
                    if(a != updataSize)
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                else
                    return false;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public void saveDownloadApkImf()
    {
        SharedPreferences sharedPre = context.getSharedPreferences(UPDATE_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(UPDATE_VNAME,updataVersionName);
        e.putInt(UPDATE_VCODE,updataVersionCode);
        e.commit();
    }

    public void doUpDataAppView() {
        if(AppActivityManager.getInstance().activityStack.size() > 0 && state != UPDATA_LATER)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(AppActivityManager.getInstance().activityStack.peek(),5);
            builder.setMessage(context.getString(R.string.keyword_version)+updataVersionName+"\r\n");
            builder.setTitle(context.getString(R.string.keyword_newversion));
            builder.setNegativeButton(context.getString(R.string.keyword_updata), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    File file1 = new File(path);
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri contentUri = getProvideGetPath.getProvideGetPath(file1);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        intent.setAction(Intent.ACTION_VIEW);
                    } else {
                        intent.setDataAndType(Uri.fromFile(file1), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                    }
                    context.startActivity(intent);


                }
            });
            builder.setPositiveButton(context.getString(R.string.keyword_updata_later), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    state = UPDATA_LATER;
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            builder.create().show();
        }
    }

    public interface NotificationOperation {
        void showProgress(String title,String content,long max,long min);
        void showMesage(String title,String content);
        void showCancle();
    }
}
