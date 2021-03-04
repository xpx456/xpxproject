package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.ImPrase;
import com.bigwiner.android.prase.SourcePrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SourceAddListActivity;

import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SourceAddListHandler extends Handler {

    public SourceAddListActivity theActivity;
    public static final int UPDATA_SOURCE_LIST = 301600;


    public SourceAddListHandler(SourceAddListActivity mSourceAddListActivity) {
        theActivity = mSourceAddListActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SourceAsks.SOURCE_LIST_MY_RESULT:
                theActivity.waitDialog.hide();
                NetObject netObject = (NetObject) msg.obj;
                String type = (String) netObject.item;
                if (type.equals("1")) {
                    SourcePrase.praseSourceMyList(theActivity, (NetObject) msg.obj, theActivity.sourceWantDetial, theActivity.wantData);
                    theActivity.mWantSourceListAdapter.notifyDataSetChanged();
                } else if (type.equals("2")) {
                    SourcePrase.praseSourceMyList(theActivity, (NetObject) msg.obj, theActivity.sourceOfferDetial, theActivity.offerData);
                    theActivity.mOfferSourceListAdapter.notifyDataSetChanged();
                }
                break;
            case SourceAsks.SOURCE_DELETE_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj)) {
                    NetObject netObject2 = (NetObject) msg.obj;
                     SourceData sourceData =  (SourceData) netObject2.item;
                    theActivity.mSourceAddListPresenter.updataSourceList(sourceData,SourceAsks.ACIONT_SOURCE_DELETE);
                }
                break;
            case UPDATA_SOURCE_LIST:
                theActivity.mSourceAddListPresenter.updataSourceList((Intent) msg.obj);
                break;
            case SourceAsks.SOURCE_ADD_CHECK_RESULT:
                theActivity.waitDialog.hide();
                if(SourcePrase.praseAddCheck(theActivity, (NetObject) msg.obj)) {
                    Intent intent = (Intent) ((NetObject) msg.obj).item;
                    theActivity.startActivity(intent);
                }
                else
                {
                    AppUtils.showMessage(theActivity,"超出发布数量");
                }
                break;
            case SourceAsks.SOURCE_OPEN_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    SourceData sourceData = (SourceData) ((NetObject) msg.obj).item;
                    sourceData.state = theActivity.getString(R.string.source_stute_open_t);
                    theActivity.mOfferSourceListAdapter.notifyDataSetChanged();
                    theActivity.mWantSourceListAdapter.notifyDataSetChanged();
                }
                break;
            case SourceAsks.SOURCE_CLOSE_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    SourceData sourceData = (SourceData) ((NetObject) msg.obj).item;
                    sourceData.state = theActivity.getString(R.string.source_stute_close);
                    theActivity.mOfferSourceListAdapter.notifyDataSetChanged();
                    theActivity.mWantSourceListAdapter.notifyDataSetChanged();
                }
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
