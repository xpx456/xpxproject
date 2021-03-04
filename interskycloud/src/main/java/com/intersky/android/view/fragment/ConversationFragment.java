package com.intersky.android.view.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intersky.R;
import com.intersky.android.entity.IntersakyData;
import com.intersky.android.presenter.MainPresenter;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.ConversationSearchActivity;
import com.intersky.android.view.activity.MainActivity;
import com.umeng.commonsdk.debug.I;

import intersky.appbase.BaseFragment;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.conversation.ConversationManager;
import intersky.appbase.entity.Conversation;
import intersky.conversation.view.adapter.ConversationAdapter2;
import intersky.function.entity.Function;
import intersky.mywidget.SearchViewLayout;

public class ConversationFragment extends BaseFragment {

    public RecyclerView conversationList;

    public MainActivity mMainActivity;
    public ImageView btnScan;
    public RelativeLayout btnSch;
    public RelativeLayout btnWait;
    public RelativeLayout btnReport;
    public ImageView btnSearch;
    public ConversationFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_conversation, container, false);
        btnScan = mView.findViewById(R.id.scan);
        btnSch = mView.findViewById(R.id.schedule);
        btnWait = mView.findViewById(R.id.wait);
        btnReport = mView.findViewById(R.id.report);
        btnSearch = mView.findViewById(R.id.search);
        conversationList = (RecyclerView)mView.findViewById(R.id.conversionList);
        conversationList.setLayoutManager(new LinearLayoutManager(mMainActivity));
        conversationList.setAdapter(mMainActivity.mMainPresenter.mConversationAdapter);
        mMainActivity.mMainPresenter.mConversationAdapter.setOnItemClickListener(itemClickListener);
        mMainActivity.mMainPresenter.mConversationSearchAdapter.setOnItemClickListener(itemClickListener);
        btnSearch.setOnClickListener(startSearchConversationListener);

        btnScan.setOnClickListener(mMainActivity.mScanListener);
        btnSch.setOnClickListener(startSchListener);
        btnWait.setOnClickListener(startWaitListener);
        btnReport.setOnClickListener(startReportListener);
        return mView;
    }


    public ConversationAdapter2.OnItemClickListener itemClickListener = new ConversationAdapter2.OnItemClickListener()
    {

        @Override
        public void onItemClick(Conversation conversation, int position, View view) {
            if(conversation.mType.equals(IntersakyData.CONVERSATION_TYPE_MESSAGE))
                InterskyApplication.mApp.chatUtils.startChat(mMainActivity.mMainPresenter.mMainActivity,conversation);
            else
                ConversationManager.getInstance().doClick(mMainActivity.mMainPresenter.mMainActivity,conversation);
        }

    };

    public View.OnClickListener startSchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bus.callData(mMainActivity,"schedule/startEvent");
        }
    };

    public View.OnClickListener startWaitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InterskyApplication.mApp.functionUtils.startFunction(mMainActivity,
                    InterskyApplication.mApp.functionUtils.mFunctions.get(1),InterskyApplication.mApp.functionUtils.account.iscloud);
        }
    };

    public View.OnClickListener startReportListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bus.callData(mMainActivity,"workreport/startReportMain");
        }
    };


    public View.OnClickListener startSearchConversationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, ConversationSearchActivity.class);
            mMainActivity.startActivity(intent);
        }
    };
}
