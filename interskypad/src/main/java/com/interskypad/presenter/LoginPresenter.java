package com.interskypad.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.interskypad.R;
import com.interskypad.asks.LoginAsks;
import com.interskypad.database.DBHelper;
import com.interskypad.handler.LoginHandler;
import com.interskypad.prase.LoginPrase;
import com.interskypad.receiver.LoginReceiver;
import com.interskypad.view.InterskyPadApplication;
import com.interskypad.view.activity.LoginActivity;
import com.interskypad.view.activity.MainActivity;
import com.interskypad.view.activity.ServiceListActivity;

import intersky.appbase.Local.LocalData;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.filetools.PathUtils;
import intersky.xpxnet.net.Service;


/**
 * Created by xpx on 2017/8/18.
 */

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
        ImageView bask = mLoginActivity.findViewById(R.id.back);
        bask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginActivity.finish();
            }
        });
        mLoginActivity.mSpinner = mLoginActivity.findViewById(R.id.server_list);
        mLoginActivity.eTxtAccount = mLoginActivity.findViewById(R.id.username_content);
        mLoginActivity.eTxtPassword = mLoginActivity.findViewById(R.id.password_content);
        mLoginActivity.btnLogin = mLoginActivity.findViewById(R.id.button_login2);
        mLoginActivity.btnServiceList = mLoginActivity.findViewById(R.id.server_setting);
        mLoginActivity.eTxtAccount.addTextChangedListener(mLoginActivity.accountChange);
        mLoginActivity.btnLogin.setOnClickListener(mLoginActivity.loginListener);
        mLoginActivity.mSpinner.setOnItemSelectedListener(mLoginActivity.sniperItemClick);
        mLoginActivity.btnServiceList.setOnClickListener(mLoginActivity.serviceListener);
        mLoginActivity.root = mLoginActivity.findViewById(R.id.activity_login);
        getLoginInfo();
        if(InterskyPadApplication.mApp.mService != null&&mLoginActivity.eTxtAccount.getText().toString().length() > 0 )
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

    public void getLoginInfo() {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        String sServer = sharedPre.getString(LocalData.LOGIN_INFO_SERVICE_RECORDID, "");
        mLoginActivity.sAdapter = new ArrayAdapter<Service>(mLoginActivity, android.R.layout.simple_spinner_item,
                DBHelper.getInstance(mLoginActivity).scanServer());
        mLoginActivity.mSpinner.setAdapter(mLoginActivity.sAdapter);
        if(sServer.length() != 0)
        {
            InterskyPadApplication.mApp.mService =  DBHelper.getInstance(mLoginActivity).getServerInfo(sServer);
            if (InterskyPadApplication.mApp.mService != null) {
                for (int i = 0; i < mLoginActivity.sAdapter.getCount(); i++) {
                    if (mLoginActivity.sAdapter.getItem(i).sRecordId.equals(InterskyPadApplication.mApp.mService.sRecordId)) {
                        mLoginActivity.mSpinner.setSelection(i);
                        break;
                    }
                }
            }
        }
        if (InterskyPadApplication.mApp.mService == null)
        {
            if(mLoginActivity.sAdapter.getCount() > 0)
            {
                mLoginActivity.mSpinner.setSelection(0);
                InterskyPadApplication.mApp.mService = mLoginActivity.sAdapter.getItem(0);
            }
        }
        if(InterskyPadApplication.mApp.mService == null)
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
        InterskyPadApplication.mApp.mAccount.mAccountId = sUsername;
        InterskyPadApplication.mApp.mAccount.mPassword = spass;

    }

    public void checkLogin()
    {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        boolean islogin = sharedPre.getBoolean(LocalData.LOGIN_INFO_STATU,false);
        if(islogin)
        {
            doLogin();

        }
    }
    public void doLogin()
    {
        writeImf();
        if(InterskyPadApplication.mApp.mService.sType == true)
        {
            mLoginActivity.waitDialog.show();
            LoginAsks.doLoging(mLoginActivity.eTxtAccount.getText().toString(),mLoginActivity.eTxtPassword.getText().toString(),InterskyPadApplication.mApp.szImei, Build.MODEL,mLoginHandler,mLoginActivity);
        }
        else
        {
            mLoginActivity.waitDialog.show();
            LoginAsks.getIpAddress(InterskyPadApplication.mApp.mService.sCode,mLoginHandler,mLoginActivity);
        }

    }

    public void onItemClick(int position)
    {
        InterskyPadApplication.mApp.mService = (Service) mLoginActivity.mSpinner.getAdapter().getItem(position);
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
        PathUtils.pathUtils.setAppBase("/interskypad/"+InterskyPadApplication.mApp.mService.sAddress
                + "/" + InterskyPadApplication.mApp.mAccount.mRecordId);
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        String name = sharedPre.getString(LocalData.LOGIN_INFO_NAME,"");
        e.putBoolean(LocalData.LOGIN_INFO_STATU, true);
        e.putString(LocalData.LOGIN_INFO_USER_RECORDID, InterskyPadApplication.mApp.mAccount.mRecordId);
        e.putString(LocalData.LOGIN_INFO_SERVICE_RECORDID, InterskyPadApplication.mApp.mService.sRecordId);
        e.commit();
        mLoginActivity.waitDialog.hide();
        Intent intent = new Intent(mLoginActivity, MainActivity.class);
        if(mLoginActivity.getIntent().hasExtra("type"))
        {
            intent.putExtra("type",mLoginActivity.getIntent().getStringExtra("type"));
            intent.putExtra("detialid",mLoginActivity.getIntent().getStringExtra("detialid"));
        }
        mLoginActivity.startActivity(intent);

    }

    public void loginFail(String json)
    {
        mLoginActivity.waitDialog.hide();
        SharedPreferences sharedPre2 = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        SharedPreferences.Editor e2 = sharedPre2.edit();
        e2.putBoolean(LocalData.LOGIN_INFO_STATU, false);
        e2.commit();
        if ((json) != null) {
            if ((json).length() != 0)
                AppUtils.showMessage(mLoginActivity, LoginPrase.getFailMessage(json));
            else
                AppUtils.showMessage(mLoginActivity, mLoginActivity.getString(R.string.error_connect_sercive_fail));
        } else {
            AppUtils.showMessage(mLoginActivity, mLoginActivity.getString(R.string.error_connect_sercive_fail));
        }
    }



}
