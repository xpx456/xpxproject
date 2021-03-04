package com.intersky.android.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.intersky.R;
import com.intersky.android.entity.IntersakyData;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.SafeActivity;
import com.intersky.android.view.activity.ConversationSearchActivity;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.conversation.ConversationManager;
import intersky.conversation.view.adapter.ConversationAdapter;

public class ConversationSearchPresenter implements Presenter {

	public ConversationSearchActivity mConversationSearchActivity;
	public ConversationSearchPresenter(ConversationSearchActivity mConversationSearchActivity) {
		this.mConversationSearchActivity = mConversationSearchActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mConversationSearchActivity.setContentView(R.layout.activity_search_conversation);
		ImageView back = mConversationSearchActivity.findViewById(R.id.back);
		back.setOnClickListener(mConversationSearchActivity.mBackListener);
		mConversationSearchActivity.searchView = mConversationSearchActivity.findViewById(R.id.search);
		mConversationSearchActivity.searchView.mSetOnSearchListener(mOnSearchActionListener);
		mConversationSearchActivity.conversationList = (ListView)mConversationSearchActivity.findViewById(R.id.conversionList);
		mConversationSearchActivity.conversationList.setAdapter(mConversationSearchActivity.mConversationSearchAdapter);
		mConversationSearchActivity.conversationList.setOnItemClickListener(itemClickListener);
		mConversationSearchActivity.mConversationSearchAdapter = new ConversationAdapter(mConversationSearchActivity.mSearchConversations, mConversationSearchActivity,true,swapFunction);
		mConversationSearchActivity.conversationList.setOnScrollListener(mOnScoll);
	}

	@Override
	public void Create() {
		// TODO Auto-generated method stub
		initView();
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		
	}

	public ConversationAdapter.SwapFunction swapFunction = new ConversationAdapter.SwapFunction() {
		@Override
		public void read(Conversation conversation) {

		}

		@Override
		public void delete(Conversation conversation) {
			AppUtils.creatDialogTowButton(mConversationSearchActivity,mConversationSearchActivity.getString(R.string.remove_conversation),
					mConversationSearchActivity.getString(R.string.button_delete),
					mConversationSearchActivity.getString(R.string.button_no),mConversationSearchActivity.getString(R.string.button_yes),
					null,new  DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ConversationManager.getInstance().doRemove(conversation);
						}
					});
		}
	};

	public void doSearch(String keyword)
	{
		mConversationSearchActivity.mSearchConversations.clear();
		if(keyword.length() > 0)
		{
			for(int i = 0; i < ConversationManager.getInstance().conversationAll.showConversations.size() ; i++ )
			{
				Conversation conversation = ConversationManager.getInstance().conversationAll.showConversations.get(i);
				if(conversation.mTitle.contains(keyword) || conversation.mSubject.contains(keyword))
				{
					mConversationSearchActivity.mSearchConversations.add(conversation);
				}
			}
		}
		mConversationSearchActivity.mConversationSearchAdapter.notifyDataSetChanged();

	}

	public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
	{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
                doSearch(mConversationSearchActivity.searchView.getText());

			}
			return true;
		}
	};

	public AbsListView.OnScrollListener mOnScoll = new AbsListView.OnScrollListener()
	{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(mConversationSearchActivity.searchView.ishow)
            {
                if(mConversationSearchActivity.searchView.getText().length() == 0)
                {
					mConversationSearchActivity.searchView.hidEdit();
                }
            }
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}
	};

	public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Conversation conversation = (Conversation) parent.getAdapter().getItem(position);
			if(conversation.mType.equals(IntersakyData.CONVERSATION_TYPE_MESSAGE))
				InterskyApplication.mApp.chatUtils.startChat(mConversationSearchActivity,conversation);
			else
				ConversationManager.getInstance().doClick(mConversationSearchActivity,conversation);
		}
	};
}
