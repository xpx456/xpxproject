package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.ImPrase;
import com.bigwiner.android.prase.SourcePrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactsListActivity;
import com.bigwiner.android.view.activity.SourceCollectListActivity;

import java.util.HashMap;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class SourceCollectListHandler extends Handler {

    public static final int SOURCE_LIST_UPDATA = 301800;

    public SourceCollectListActivity theActivity;
    public SourceCollectListHandler(SourceCollectListActivity mSourceCollectListActivity) {
        theActivity = mSourceCollectListActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SourceAsks.SOURCE_COLLECT_LIST_RESULT:
                theActivity.waitDialog.hide();
                SourcePrase.praseSourceSCollect(theActivity, (NetObject) msg.obj,theActivity.collectdetial,theActivity.collectDatas,theActivity.collectHashData);
                theActivity.collectAdapter.notifyDataSetChanged();
                break;
            case SOURCE_LIST_UPDATA:
                theActivity.waitDialog.hide();
                theActivity.mSourceCollectListPresenter.updataSourceList((Intent) msg.obj);
                break;
            case SourceAsks.SOURCE_COLLECT_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    SourceData sourceData = (SourceData) ((NetObject) msg.obj).item;
                    if(sourceData.iscollcet == 1)
                    {
                        sourceData.collectcount = String.valueOf(Integer.valueOf(sourceData.collectcount)-1);
                        sourceData.iscollcet = 0;
                        theActivity.collectdetial.currentszie--;
                        theActivity.collectdetial.totleszie--;
                        if(theActivity.collectdetial.totleszie/theActivity.collectdetial.pagesize+1 < theActivity.collectdetial.totlepage)
                        {
                            theActivity.collectdetial.totlepage--;
                        }
                        if(theActivity.collectdetial.currentszie/theActivity.collectdetial.pagesize+1 < theActivity.collectdetial.currentpage)
                        {
                            theActivity.collectdetial.currentpage--;
                        }
                        theActivity.collectDatas.remove(sourceData);
                    }
                }
                theActivity.collectAdapter.notifyDataSetChanged();
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
