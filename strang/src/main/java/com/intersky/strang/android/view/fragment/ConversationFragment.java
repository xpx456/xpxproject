package com.intersky.strang.android.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.intersky.strang.R;
import com.intersky.strang.android.entity.IntersakyData;
import com.intersky.strang.android.view.StrangApplication;
import com.intersky.strang.android.view.activity.MainActivity;

import intersky.appbase.BaseFragment;
import intersky.apputils.AppUtils;
import intersky.conversation.ConversationManager;
import intersky.appbase.entity.Conversation;
import intersky.mywidget.SearchViewLayout;

public class ConversationFragment extends BaseFragment {

    public ListView conversationList;
    public SearchViewLayout searchView;
    public MainActivity mMainActivity;

    public ConversationFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_conversation, container, false);
        conversationList = (ListView)mView.findViewById(R.id.conversionList);
        conversationList.setAdapter(mMainActivity.mMainPresenter.mConversationAdapter);
        conversationList.setOnItemClickListener(itemClickListener);
        conversationList.setOnItemLongClickListener(longClickListener);
        conversationList.setOnScrollListener(mOnScoll);
        searchView = mView.findViewById(R.id.search);
        searchView.mSetOnSearchListener(mOnSearchActionListener);
        return mView;
    }

    public void doSearch(String keyword)
    {
        if(keyword.length() > 0)
        {
            mMainActivity.mMainPresenter.mSearchConversations.clear();
            for(int i = 0 ; i < ConversationManager.getInstance().conversationAll.showConversations.size() ; i++ )
            {
                Conversation conversation = ConversationManager.getInstance().conversationAll.showConversations.get(i);
                if(conversation.mTitle.contains(keyword) || conversation.mSubject.contains(keyword))
                {
                    mMainActivity.mMainPresenter.mSearchConversations.add(conversation);
                }
            }
            conversationList.setAdapter(mMainActivity.mMainPresenter.mConversationSearchAdapter);
        }
        else
        {
            conversationList.setAdapter(mMainActivity.mMainPresenter.mConversationAdapter);
        }


    }

    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                doSearch(searchView.getText());

            }
            return true;
        }
    };

    public AbsListView.OnScrollListener mOnScoll = new AbsListView.OnScrollListener()
    {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(searchView.ishow)
            {
                if(searchView.getText().length() == 0)
                {
                    searchView.hidEdit();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    public AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            AppUtils.creatDialogTowButton(mMainActivity.mMainPresenter.mMainActivity,mMainActivity.mMainPresenter.mMainActivity.getString(R.string.remove_conversation),
                    mMainActivity.mMainPresenter.mMainActivity.getString(R.string.button_delete),
                    mMainActivity.mMainPresenter.mMainActivity.getString(R.string.button_no),mMainActivity.mMainPresenter.mMainActivity.getString(R.string.button_yes),
                    null,new  DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ConversationManager.getInstance().doRemove((Conversation) parent.getAdapter().getItem(position));
                        }
                    });

            return true;
        }
    };

    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Conversation conversation = (Conversation) parent.getAdapter().getItem(position);
            if(conversation.mType.equals(IntersakyData.CONVERSATION_TYPE_MESSAGE))
                StrangApplication.mApp.chatUtils.startChat(mMainActivity.mMainPresenter.mMainActivity,conversation);
            else
                ConversationManager.getInstance().doClick(mMainActivity.mMainPresenter.mMainActivity,conversation);
        }
    };
}
