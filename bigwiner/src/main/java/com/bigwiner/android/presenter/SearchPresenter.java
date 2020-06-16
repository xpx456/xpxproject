package com.bigwiner.android.presenter;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.handler.SearchHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.activity.MeetingDetialActivity;
import com.bigwiner.android.view.activity.SearchActivity;
import com.bigwiner.android.view.activity.SearchResultActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bigwiner.android.view.adapter.SearchWordAdapter;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.filetools.PathUtils;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import xpx.com.toolbar.utils.ToolBarHelper;

public class SearchPresenter implements Presenter {

    public SearchActivity mSearchActivity;
    public SearchHandler mMainHandler;

    public SearchPresenter(SearchActivity mSearchActivity) {
        this.mSearchActivity = mSearchActivity;
        this.mMainHandler = new SearchHandler(mSearchActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mSearchActivity, Color.argb(0, 255, 255, 255));
        mSearchActivity.setContentView(R.layout.activity_search);
        mSearchActivity.mToolBarHelper.hidToolbar(mSearchActivity, (RelativeLayout) mSearchActivity.findViewById(R.id.buttomaciton));
        mSearchActivity.measureStatubar(mSearchActivity, (RelativeLayout) mSearchActivity.findViewById(R.id.stutebar));
        mSearchActivity.btnBach = mSearchActivity.findViewById(R.id.backlayer);
        mSearchActivity.searchView = mSearchActivity.findViewById(R.id.search);
        mSearchActivity.history = mSearchActivity.findViewById(R.id.lable);
        mSearchActivity.cleanHis = mSearchActivity.findViewById(R.id.cleanhis);
        mSearchActivity.newsAdapter = new SearchWordAdapter(mSearchActivity.news,mSearchActivity);
        mSearchActivity.noticeAdapter = new SearchWordAdapter(mSearchActivity.notices,mSearchActivity);
        mSearchActivity.meetingAdapter = new SearchWordAdapter(mSearchActivity.meeting,mSearchActivity);
        mSearchActivity.friendAdapter = new SearchWordAdapter(mSearchActivity.friend,mSearchActivity);
        SharedPreferences info = BigwinerApplication.mApp.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
        mSearchActivity.hisKeyword = info.getString(UserDefine.USER_SEARCH_HISTORY,"");
        praseHistoryView();
        mSearchActivity.searchView.mSetOnSearchListener(mSearchActivity.mOnSearchActionListener);
        mSearchActivity.btnBach.setOnClickListener(mSearchActivity.backListener);
        mSearchActivity.cleanHis.setOnClickListener(mSearchActivity.cleanHistroyListener);
    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
//		MobclickAgent.onResume(mSplashActivity);
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
//		MobclickAgent.onPause(mSplashActivity);
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mMainHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    public void doSearch(String keword,String type) {
        if(keword.length() > 0)
        {

            if(check(mSearchActivity.hisKeyword,keword))
            {
                SharedPreferences info = BigwinerApplication.mApp.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
                SharedPreferences.Editor e = info.edit();
                mSearchActivity.hisKeyword = AppUtils.measureStringBefore(mSearchActivity.hisKeyword,keword,",");
                e.putString(UserDefine.USER_SEARCH_HISTORY,mSearchActivity.hisKeyword);
                e.commit();
                Intent intent = new Intent(mSearchActivity, SearchResultActivity.class);
                intent.putExtra("keyword",keword);
                mSearchActivity.startActivity(intent);
                praseHistoryView();
            }
            else
            {
                SharedPreferences info = BigwinerApplication.mApp.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
                SharedPreferences.Editor e = info.edit();
                mSearchActivity.hisKeyword = AppUtils.addStringBefore(mSearchActivity.hisKeyword,keword,",",SearchActivity.MAXHISTORY);
                e.putString(UserDefine.USER_SEARCH_HISTORY,mSearchActivity.hisKeyword);
                e.commit();
                Intent intent = new Intent(mSearchActivity, SearchResultActivity.class);
                intent.putExtra("keyword",keword);
                mSearchActivity.startActivity(intent);
                praseHistoryView();
            }

        }
    }

    public boolean check(String all,String key)
    {
        String[] alls = all.split(",");
        boolean has = false;
        for(int i = 0 ; i < alls.length ; i++)
        {
            if(alls[i].equals(key))
            {
                has = true;
                break;
            }
        }
        return has;
    }

    public void praseHistoryView() {

        mSearchActivity.history.removeAllViews();

        if(mSearchActivity.hisKeyword.length() > 0)
        {
            String[] s= mSearchActivity.hisKeyword.split(",");
            if(s.length < SearchActivity.MAXHISTORY)
            {
                for(int i = 0 ; i < s.length ; i++)
                {
                    View view = mSearchActivity.getLayoutInflater().inflate(R.layout.search_labe_item,null);
                    TextView textView = view.findViewById(R.id.history_lable);
                    textView.setText(s[i]);
                    view.setOnClickListener(mSearchActivity.searchLableListener);
                    mSearchActivity.history.addView(view);
                }
            }
            else
            {
                for(int i = 0 ; i < SearchActivity.MAXHISTORY ; i++)
                {
                    View view = mSearchActivity.getLayoutInflater().inflate(R.layout.search_labe_item,null);
                    TextView textView = view.findViewById(R.id.history_lable);
                    textView.setText(s[i]);
                    view.setOnClickListener(mSearchActivity.searchLableListener);
                    mSearchActivity.history.addView(view);
                }
            }
        }

    }

    public void doSearchLabke(View view) {
        TextView textView = view.findViewById(R.id.history_lable);
        doSearch(textView.getText().toString(),"");
    }

    public void cleanHistory() {
        mSearchActivity.hisKeyword = "";
        SharedPreferences info = BigwinerApplication.mApp.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
        SharedPreferences.Editor e = info.edit();
        e.putString(UserDefine.USER_SEARCH_HISTORY,mSearchActivity.hisKeyword);
        e.commit();
        praseHistoryView();
    }


}
