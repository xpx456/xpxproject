package com.intersky.android.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.ArrayAdapter;

import com.intersky.R;
import com.intersky.android.asks.LoginAsks;
import com.intersky.android.database.DBHelper;
import com.intersky.android.handler.LoginHandler;
import com.intersky.android.prase.LoginPrase;
import com.intersky.android.receiver.LoginReceiver;
import com.intersky.android.tools.AppTool;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.MainActivity;
import com.intersky.android.view.activity.LoginActivity;
import com.intersky.android.view.activity.ServiceListActivity;
import com.umeng.analytics.MobclickAgent;

import intersky.appbase.Local.LocalData;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.chat.ContactManager;
import intersky.filetools.FileUtils;
import intersky.filetools.PathUtils;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.Service;
import xpx.com.toolbar.utils.ToolBarHelper;


/**
 * Created by xpx on 2017/8/18.
 */
//flametestaccount7913_4826
//flametestcode4396_2700
public class LoginPresenter implements Presenter {

    public LoginHandler mLoginHandler;
    public LoginActivity mLoginActivity;
    public LoginPresenter(LoginActivity mLoginActivity)
    {
        this.mLoginActivity = mLoginActivity;
        this.mLoginHandler = new LoginHandler(mLoginActivity);
        mLoginActivity.setBaseReceiver(new LoginReceiver(mLoginHandler));
    }

    @Override
    public void Create() {
        initView();

    }

    @Override
    public void initView() {
        mLoginActivity.setContentView(R.layout.activity_login);
        AppTool.setViewBase(mLoginActivity);
        mLoginActivity.mSpinner = mLoginActivity.findViewById(R.id.server_list);
        mLoginActivity.eTxtAccount = mLoginActivity.findViewById(R.id.username_content);
        mLoginActivity.eTxtPassword = mLoginActivity.findViewById(R.id.password_content);
        mLoginActivity.btnLogin = mLoginActivity.findViewById(R.id.button_login);
        mLoginActivity.btnServiceList = mLoginActivity.findViewById(R.id.server_setting);
        mLoginActivity.firstLayer = mLoginActivity.findViewById(R.id.first);
        mLoginActivity.eTxtAccount.addTextChangedListener(mLoginActivity.accountChange);
        mLoginActivity.btnLogin.setOnClickListener(mLoginActivity.loginListener);
        mLoginActivity.mSpinner.setOnItemSelectedListener(mLoginActivity.sniperItemClick);
        mLoginActivity.btnServiceList.setOnClickListener(mLoginActivity.serviceListener);

        getLoginInfo();
        if(InterskyApplication.mApp.mService != null&&mLoginActivity.eTxtAccount.getText().toString().length() > 0 )
        checkLogin();
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
        writeImf();
    }

    private void writeImf()
    {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(LocalData.LOGIN_INFO_NAME, mLoginActivity.eTxtAccount.getText().toString());
        e.putString(LocalData.LOGIN_INFO_PASSWORD, mLoginActivity.eTxtPassword.getText().toString());
        e.commit();
    }

    public void getLoginInfo()
    {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        String sServer = sharedPre.getString(LocalData.LOGIN_INFO_SERVICE_RECORDID, "");
        mLoginActivity.sAdapter = new ArrayAdapter<Service>(mLoginActivity, android.R.layout.simple_spinner_item,
                DBHelper.getInstance(mLoginActivity).scanServer());
        mLoginActivity.mSpinner.setAdapter(mLoginActivity.sAdapter);
        if(sServer.length() != 0)
        {
            InterskyApplication.mApp.mService =  DBHelper.getInstance(mLoginActivity).getServerInfo(sServer);
            if (InterskyApplication.mApp.mService != null) {
                for (int i = 0; i < mLoginActivity.sAdapter.getCount(); i++) {
                    if (mLoginActivity.sAdapter.getItem(i).sRecordId.equals(InterskyApplication.mApp.mService.sRecordId)) {
                        mLoginActivity.mSpinner.setSelection(i);
                        break;
                    }
                }
            }
        }
        if (InterskyApplication.mApp.mService == null)
        {
            if(mLoginActivity.sAdapter.getCount() > 0)
            {
                mLoginActivity.mSpinner.setSelection(0);
                InterskyApplication.mApp.mService = mLoginActivity.sAdapter.getItem(0);
            }
        }
        if(InterskyApplication.mApp.mService == null)
        {
            mLoginActivity.btnLogin.setEnabled(false);
        }

        String sUsername = sharedPre.getString(LocalData.LOGIN_INFO_NAME, "");
        if (sUsername != null) {
            mLoginActivity.eTxtAccount.setText(sUsername);
        }
        String spass = sharedPre.getString(LocalData.LOGIN_INFO_PASSWORD, "");
        sUsername = ((null == sUsername ? new String() : sUsername));

        if (spass != null) {
            mLoginActivity.eTxtPassword.setText(spass);

        }
        InterskyApplication.mApp.mAccount.mAccountId = sUsername;
        InterskyApplication.mApp.mAccount.mPassword = spass;

    }

    public void checkLogin()
    {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        boolean islogin = sharedPre.getBoolean(LocalData.LOGIN_INFO_STATU,false);
        if(islogin)
        {
            doLogin();

        }
        else
        {
            mLoginActivity.firstLayer.setVisibility(View.INVISIBLE);
            mLoginActivity.autoLogin = false;
            InterskyApplication.mApp.upDataManager.checkVersin();
        }
    }
    public void doLogin()
    {
        writeImf();
        if((InterskyApplication.mApp.mService.sType == true && InterskyApplication.mApp.mService.sAddress.length() > 0) || InterskyApplication.mApp.mService.sAddress.length() > 0)
        {
            mLoginActivity.waitDialog.show();
            LoginAsks.doLoging(mLoginActivity.eTxtAccount.getText().toString(),mLoginActivity.eTxtPassword.getText().toString(),InterskyApplication.mApp.szImei, Build.MODEL,mLoginHandler,mLoginActivity);
        }
        else
        {
            mLoginActivity.waitDialog.show();
            LoginAsks.getIpAddress(InterskyApplication.mApp.mService.sCode,mLoginHandler,mLoginActivity);
        }

    }

    public void onItemClick(int position)
    {
        InterskyApplication.mApp.mService = (Service) mLoginActivity.mSpinner.getAdapter().getItem(position);
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(LocalData.LOGIN_INFO_SERVICE_RECORDID, InterskyApplication.mApp.mService.sRecordId);
        e.commit();

        if(mLoginActivity.btnLogin.isEnabled() == false)
        {
            mLoginActivity.btnLogin.setEnabled(true);
        }
    }

    public void upDataList(Intent intent) {
        Service service = intent.getParcelableExtra("service");
        if(intent.getBooleanExtra("isnew",false) == true)
        {
            mLoginActivity.sAdapter.add(service);
        }
        else
        {
            for(int i = 0 ; i < mLoginActivity.sAdapter.getCount() ; i++)
            {
                if(mLoginActivity.sAdapter.getItem(i).sRecordId.equals(service.sRecordId))
                {
                    mLoginActivity.sAdapter.getItem(i).sName = service.sName;
                    mLoginActivity.sAdapter.getItem(i).sAddress = service.sAddress;
                    mLoginActivity.sAdapter.getItem(i).sType = service.sType;
                    mLoginActivity.sAdapter.getItem(i).sPort = service.sPort;
                    mLoginActivity.sAdapter.getItem(i).sCode = service.sCode;
                    mLoginActivity.sAdapter.getItem(i).https = service.https;
                    break;
                }
            }
        }

        mLoginActivity.sAdapter.notifyDataSetChanged();
    }

    public void deleteList(Intent intent) {
        Service service = intent.getParcelableExtra("service");
        for(int i = 0 ; i < mLoginActivity.sAdapter.getCount() ; i++)
        {
            if(mLoginActivity.sAdapter.getItem(i).sRecordId.equals(service.sRecordId))
            {
                mLoginActivity.sAdapter.remove(mLoginActivity.sAdapter.getItem(i));
                break;
            }
        }
        mLoginActivity.sAdapter.notifyDataSetChanged();
    }

    public void doService()
    {
        Intent intent = new Intent(mLoginActivity, ServiceListActivity.class);
        mLoginActivity.startActivity(intent);
    }

    public void startMain()
    {
//        MobclickAgent.onProfileSignIn(InterskyApplication.mApp.mAccount.mAccountId+"/"+InterskyApplication.mApp.deviceId
//                +"/"+InterskyApplication.mApp.mAccount.mRecordId+"/"+mLoginActivity.eTxtPassword.getText());
        String pass = mLoginActivity.eTxtPassword.getText().toString();
//        PathUtils.init("/intersky/"+InterskyApplication.mApp.mService.sAddress
//                + "/" + InterskyApplication.mApp.mAccount.mRecordId);
        InterskyApplication.mApp.saveApplacation();
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        String name = sharedPre.getString(LocalData.LOGIN_INFO_NAME,"");
        e.putBoolean(LocalData.LOGIN_INFO_STATU, true);
        e.putString(LocalData.LOGIN_INFO_USER_RECORDID, InterskyApplication.mApp.mAccount.mRecordId);
        e.putString(LocalData.LOGIN_INFO_SERVICE_RECORDID, InterskyApplication.mApp.mService.sRecordId);
        e.commit();

        mLoginActivity.waitDialog.hide();
        Intent intent = new Intent(mLoginActivity, MainActivity.class);
        if(mLoginActivity.getIntent().hasExtra("type"))
        {
            intent.putExtra("type",mLoginActivity.getIntent().getStringExtra("type"));
            intent.putExtra("detialid",mLoginActivity.getIntent().getStringExtra("detialid"));
            intent.putExtra("json",mLoginActivity.getIntent().getStringExtra("json"));
        }
        intent.putExtra("auto",mLoginActivity.autoLogin);
        mLoginActivity.startActivity(intent);

    }

}
