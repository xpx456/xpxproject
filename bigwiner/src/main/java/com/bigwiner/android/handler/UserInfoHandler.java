package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;
import com.bumptech.glide.Glide;

import intersky.appbase.entity.Account;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideCacheUtil;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.GlideUtils;
import intersky.apputils.SystemUtil;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class UserInfoHandler extends Handler {

    public static final int EVENT_SET_SEX = 3010000;
    public static final int EVENT_SET_TYPE = 3010001;
    public static final int EVENT_SET_CIYT = 3010002;
    public static final int EVENT_SET_AREA= 3010003;
    public static final int EVENT_SET_POSITION= 3010004;
    public static final int EVENT_SET_COMPANY= 3010005;
    public UserInfoActivity theActivity;
    public UserInfoHandler(UserInfoActivity theActivity) {
        this.theActivity = theActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case EVENT_SET_SEX:
                theActivity.mUserInfoPresenter.setSex((Intent) msg.obj);
                break;
            case EVENT_SET_TYPE:
                theActivity.mUserInfoPresenter.setType((Intent) msg.obj);
                break;
            case EVENT_SET_CIYT:
                theActivity.mUserInfoPresenter.setCity((Intent) msg.obj);
                break;
            case EVENT_SET_AREA:
                theActivity.mUserInfoPresenter.setArea((Intent) msg.obj);
                break;
            case EVENT_SET_POSITION:
                theActivity.mUserInfoPresenter.setPostion((Intent) msg.obj);
                break;
            case EVENT_SET_COMPANY:
                theActivity.mUserInfoPresenter.setCompany((Intent) msg.obj);
                break;
            case LoginAsks.UPLOAD_HEAD_RESULT:

                String headid = LoginPrase.praseHeadex(theActivity, (NetObject) msg.obj);
                if(headid.length() > 0)
                {
                    theActivity.headid = headid;
                    Account account = new Account();
                    GlideConfiguration.deleteCachedFile(ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.mAccount.modify),theActivity);
                    account.copy(BigwinerApplication.mApp.mAccount);
                    account.modify = String.valueOf(System.currentTimeMillis());
                    account.mRealName = theActivity.name.getText().toString();
                    account.icon = theActivity.headid;
                    if(!theActivity.sex.getText().toString().equals(theActivity.getString(R.string.userinfo_sex_hit)))
                        account.mSex = theActivity.sex.getText().toString();
                    else
                    {
                        account.mSex = "";
                    }
                    if(!theActivity.area.getText().toString().equals(theActivity.getString(R.string.userinfo_area_hit)))
                        account.typeArea = theActivity.area.getText().toString();
                    else
                    {
                        account.typeArea = "";
                    }
                    if(!theActivity.type.getText().toString().equals(theActivity.getString(R.string.userinfo_type_hit)))
                        account.typeBusiness = theActivity.type.getText().toString();
                    else
                    {
                        account.typeBusiness = "";
                    }
                    if(!theActivity.position.getText().toString().equals(theActivity.getString(R.string.userinfo_position_hit)))
                        account.mPosition = theActivity.position.getText().toString();
                    else
                    {
                        account.mPosition = "";
                    }
                    if(!theActivity.cname.getText().toString().equals(theActivity.getString(R.string.userinfo_cname_hit)) && theActivity.cname.length() > 0)
                    {
                        account.mCompanyName = theActivity.cname.getText().toString();
                        account.mUCid = theActivity.cid;
                    }
                    else
                    {
                        account.mCompanyName = "";
                        account.mUCid = "";
                    }
                    account.des = theActivity.memo.getText().toString();
                    LoginAsks.doEditUserinfo(theActivity,theActivity.mUserInfoPresenter.mUserInfoHandler,account);
                }
                break;
            case LoginAsks.EDIT_RESULT:
                theActivity.waitDialog.hide();
                if(LoginPrase.praseUseData(theActivity, (NetObject) msg.obj))
                {
                    Intent intent = new Intent(MainActivity.ACTION_UPDATE_MY);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                    theActivity.finish();
                }
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                break;
            case NetUtils.TOKEN_ERROR:
                if(BigwinerApplication.mApp.mAccount.islogin == true) {
                    BigwinerApplication.mApp.logout(BigwinerApplication.mApp.mAppHandler,BigwinerApplication.mApp.appActivityManager.getCurrentActivity());
                    NetUtils.getInstance().cleanTasks();
                }
                break;
        }

    }
}
