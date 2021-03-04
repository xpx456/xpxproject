package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.prase.SourcePrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SourceCreatActivity;
import com.bigwiner.android.view.activity.SourceDetialActivity;

import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SourceDetialHandler extends Handler {

    public static final int SOURCE_DETIAL_UPDATA = 301700;

    public SourceDetialActivity theActivity;
    public SourceDetialHandler(SourceDetialActivity theActivity) {
        this.theActivity = theActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SourceAsks.SOURCE_DETIAL_RESULT:
                theActivity.waitDialog.hide();
                SourcePrase.praseSourceDetial(theActivity, (NetObject) msg.obj);
                theActivity.mSourceDetialPresenter.initDetial();
                break;
            case SourceAsks.SOURCE_COLLECT_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    if(theActivity.sourceData.iscollcet == 0)
                    {
                        theActivity.sourceData.iscollcet = 1;
                        theActivity.sourceData.collectcount = String.valueOf(Integer.valueOf(theActivity.sourceData.collectcount)+1);
                        theActivity.collecttxt.setText(theActivity.getString(R.string.source_collect_cancle));
                        theActivity.collect.setText(theActivity.sourceData.collectcount);
                    }
                    else
                    {
                        theActivity.sourceData.iscollcet = 0;
                        theActivity.sourceData.collectcount = String.valueOf(Integer.valueOf(theActivity.sourceData.collectcount)-1);
                        theActivity.collecttxt.setText(theActivity.getString(R.string.source_collect));
                        theActivity.collect.setText(theActivity.sourceData.collectcount);
                    }
//                    theActivity.collectbtn.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(SourceAsks.ACIONT_SOURCE_EDIT);
                    intent.putExtra("source",theActivity.sourceData);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                }
                break;
            case ContactsAsks.CONTACTS_ADD_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    theActivity.sourceData.isfriend = true;
                    theActivity.mAddimg.setImageResource(R.drawable.delimg);
                    theActivity.btnAdd.setOnClickListener(theActivity.delFriendListener);
                    Intent intent = new Intent(SourceAsks.ACIONT_SOURCE_EDIT);
                    intent.putExtra("source",theActivity.sourceData);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                    Intent intent2 = new Intent(ContactsAsks.ACTION_FRIEND_CHANGE);
                    Contacts contacts = new Contacts();
                    contacts.mRecordid = theActivity.sourceData.userid;
                    contacts.mName = theActivity.sourceData.username;
                    contacts.mPosition = theActivity.sourceData.position;
                    intent2.putExtra("contacts",contacts);
                    intent2.putExtra("add",true);
                    intent2.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent2);
                }
                break;
            case ContactsAsks.CONTACTS_DEL_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {

                    theActivity.sourceData.isfriend = false;
                    theActivity.mAddimg.setImageResource(R.drawable.addimg);
                    theActivity.btnAdd.setOnClickListener(theActivity.addFriendListener);
                    Intent intent = new Intent(SourceAsks.ACIONT_SOURCE_EDIT);
                    intent.putExtra("source",theActivity.sourceData);
                    intent.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent);
                    Intent intent2 = new Intent(ContactsAsks.ACTION_FRIEND_CHANGE);
                    Contacts contacts = new Contacts();
                    contacts.mRecordid = theActivity.sourceData.userid;
                    contacts.mName = theActivity.sourceData.username;
                    contacts.mPosition = theActivity.sourceData.position;
                    intent2.putExtra("contacts",contacts);
                    intent2.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent2);
                }
                break;
            case SOURCE_DETIAL_UPDATA:
                theActivity.mSourceDetialPresenter.updataSourceDetial((Intent) msg.obj);
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
