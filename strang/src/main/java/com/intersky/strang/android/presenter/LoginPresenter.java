package com.intersky.strang.android.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.ArrayAdapter;

import com.intersky.strang.R;
import com.intersky.strang.android.asks.LoginAsks;
import com.intersky.strang.android.database.DBHelper;
import com.intersky.strang.android.handler.LoginHandler;
import com.intersky.strang.android.prase.LoginPrase;
import com.intersky.strang.android.receiver.LoginReceiver;
import com.intersky.strang.android.view.StrangApplication;
import com.intersky.strang.android.view.activity.MainActivity;
import com.intersky.strang.android.view.activity.LoginActivity;
import com.intersky.strang.android.view.activity.ServiceListActivity;

import intersky.appbase.Local.LocalData;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
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
        mLoginActivity.mSpinner = mLoginActivity.findViewById(R.id.server_list);
        mLoginActivity.eTxtAccount = mLoginActivity.findViewById(R.id.username_content);
        mLoginActivity.eTxtPassword = mLoginActivity.findViewById(R.id.password_content);
        mLoginActivity.btnLogin = mLoginActivity.findViewById(R.id.button_login);
        mLoginActivity.btnServiceList = mLoginActivity.findViewById(R.id.server_setting);
        mLoginActivity.eTxtAccount.addTextChangedListener(mLoginActivity.accountChange);
        mLoginActivity.btnLogin.setOnClickListener(mLoginActivity.loginListener);
        mLoginActivity.mSpinner.setOnItemSelectedListener(mLoginActivity.sniperItemClick);
        mLoginActivity.btnServiceList.setOnClickListener(mLoginActivity.serviceListener);
        ToolBarHelper.setTitle(mLoginActivity.mActionBar,mLoginActivity.getString(R.string.activity_title_login));
        getLoginInfo();
        if(StrangApplication.mApp.mService != null&&mLoginActivity.eTxtAccount.getText().toString().length() > 0 )
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
            StrangApplication.mApp.mService =  DBHelper.getInstance(mLoginActivity).getServerInfo(sServer);
            if (StrangApplication.mApp.mService != null) {
                for (int i = 0; i < mLoginActivity.sAdapter.getCount(); i++) {
                    if (mLoginActivity.sAdapter.getItem(i).sRecordId.equals(StrangApplication.mApp.mService.sRecordId)) {
                        mLoginActivity.mSpinner.setSelection(i);
                        break;
                    }
                }
            }
        }
        if (StrangApplication.mApp.mService == null)
        {
            if(mLoginActivity.sAdapter.getCount() > 0)
            {
                mLoginActivity.mSpinner.setSelection(0);
                StrangApplication.mApp.mService = mLoginActivity.sAdapter.getItem(0);
            }
        }
        if(StrangApplication.mApp.mService == null)
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
        StrangApplication.mApp.mAccount.mAccountId = sUsername;
        StrangApplication.mApp.mAccount.mPassword = spass;

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
            mLoginActivity.autoLogin = false;
            StrangApplication.mApp.upDataManager.checkVersin();
        }
    }
    public void doLogin()
    {
        writeImf();
        if((StrangApplication.mApp.mService.sType == true && StrangApplication.mApp.mService.sAddress.length() > 0) || StrangApplication.mApp.mService.sAddress.length() > 0)
        {
            mLoginActivity.waitDialog.show();
            LoginAsks.doLoging(mLoginActivity.eTxtAccount.getText().toString(),mLoginActivity.eTxtPassword.getText().toString(),StrangApplication.mApp.szImei, Build.MODEL,mLoginHandler,mLoginActivity);
        }
        else
        {
            mLoginActivity.waitDialog.show();
            LoginAsks.getIpAddress(StrangApplication.mApp.mService.sCode,mLoginHandler,mLoginActivity);
        }

    }

    public void onItemClick(int position)
    {
        StrangApplication.mApp.mService = (Service) mLoginActivity.mSpinner.getAdapter().getItem(position);
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(LocalData.LOGIN_INFO_SERVICE_RECORDID, StrangApplication.mApp.mService.sRecordId);
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
//        MobclickAgent.onProfileSignIn(StrangApplication.mApp.mAccount.mAccountId+"/"+StrangApplication.mApp.deviceId
//                +"/"+StrangApplication.mApp.mAccount.mRecordId+"/"+mLoginActivity.eTxtPassword.getText());
        String pass = mLoginActivity.eTxtPassword.getText().toString();
//        PathUtils.init("/intersky/"+StrangApplication.mApp.mService.sAddress
//                + "/" + StrangApplication.mApp.mAccount.mRecordId);
        StrangApplication.mApp.saveApplacation();
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        String name = sharedPre.getString(LocalData.LOGIN_INFO_NAME,"");
        e.putBoolean(LocalData.LOGIN_INFO_STATU, true);
        e.putString(LocalData.LOGIN_INFO_USER_RECORDID, StrangApplication.mApp.mAccount.mRecordId);
        e.putString(LocalData.LOGIN_INFO_SERVICE_RECORDID, StrangApplication.mApp.mService.sRecordId);
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

    public void initModules()
    {

    }
}
