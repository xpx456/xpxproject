package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SearchActivity;
import com.bigwiner.android.view.activity.SearchResultActivity;

import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//05

public class SearchResultHandler extends Handler {

    public SearchResultActivity theActivity;
    public SearchResultHandler(SearchResultActivity mSearchResultActivity) {
        theActivity = mSearchResultActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ConversationAsks.SEARCH_RESULT:
                theActivity.waitDialog.hide();
                String type = (String) ((NetObject) msg.obj).item;
                if(type.equals("notice"))
                {
                    theActivity.noticeflg = true;
                    ConversationPrase.praseSearch(theActivity.notices,theActivity, (NetObject) msg.obj);
                }
                else if(type.equals("new"))
                {
                    theActivity.newsflg = true;
                    ConversationPrase.praseSearch(theActivity.news,theActivity, (NetObject) msg.obj);
                }
                else if(type.equals("conference"))
                {
                    theActivity.meetingflg = true;
                    ConversationPrase.praseSearch(theActivity.meetings,theActivity, (NetObject) msg.obj);
                }
                if(theActivity.meetingflg && theActivity.noticeflg&&theActivity.newsflg) {
                    theActivity.mSearchResultPresenter.praseSearchResult();
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
