package com.bigwiner.android.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.MeetingDetialActivity;
import com.bigwiner.android.view.activity.SearchActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bigwiner.android.view.adapter.ConversationAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseFragment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.TabHeadView;

public class ConversationFragment extends BaseFragment {

    public RelativeLayout searchView;
    public MainActivity mMainActivity;
    public TabHeadView mTabHeadView;
    public ArrayList<RecyclerView> listViews = new ArrayList<RecyclerView>();
    public ArrayList<View> mViews = new ArrayList<View>();
    public NoScrollViewPager mViewPager;
    public ConversationPageAdapter mLoderPageAdapter;
    public TextView nologinBtn;

    public ConversationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_conversation, container, false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));

        mTabHeadView = mView.findViewById(R.id.head);
        String[] names = new String[5];
        names[0] = mMainActivity.getString(R.string.conversation_all);
        names[1] = mMainActivity.getString(R.string.conversation_notice);
        names[2] = mMainActivity.getString(R.string.conversation_news);
        names[3] = mMainActivity.getString(R.string.conversation_meeting);
        names[4] = mMainActivity.getString(R.string.conversation_friend);
        for (int i = 0; i < names.length; i++) {
            View mView1 = mMainActivity.getLayoutInflater().inflate(intersky.conversation.R.layout.conversation_pager, null);
            RecyclerView listView = mView1.findViewById(R.id.busines_List);
            listView.setLayoutManager(new LinearLayoutManager(mMainActivity));
            PullToRefreshView pullToRefreshView = mView1.findViewById(R.id.headview);
            pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            pullToRefreshView.onFooterRefreshComplete();
            pullToRefreshView.onHeaderRefreshComplete();
            pullToRefreshView.setOnFooterRefreshListener(onFooterRefreshListener);
            pullToRefreshView.setOnHeaderRefreshListener(onHeadRefreshListener);
            switch (i) {
                case 0:
                    listView.setAdapter(mMainActivity.allAdapter);
                    mMainActivity.allAdapter.setConversationFunction(swipClickListener);
                    mMainActivity.allAdapter.setOnItemClickListener(itemClickListener);
                    break;
                case 1:
                    listView.setAdapter(mMainActivity.noticeAdapter);
                    mMainActivity.allAdapter.setConversationFunction(swipClickListener);
                    mMainActivity.noticeAdapter.setOnItemClickListener(itemClickListener);
                    break;
                case 2:
                    listView.setAdapter(mMainActivity.newsAdapter);
                    mMainActivity.allAdapter.setConversationFunction(swipClickListener);
                    mMainActivity.newsAdapter.setOnItemClickListener(itemClickListener);
                    break;
                case 3:
                    listView.setAdapter(mMainActivity.meetingAdapter);
                    mMainActivity.allAdapter.setConversationFunction(swipClickListener);
                    mMainActivity.meetingAdapter.setOnItemClickListener(itemClickListener);
                    break;
                case 4:
                    listView.setAdapter(mMainActivity.meessageAdapter);
                    mMainActivity.allAdapter.setConversationFunction(swipClickListener);
                    mMainActivity.meessageAdapter.setOnItemClickListener(itemClickListener);
                    nologinBtn = mView1.findViewById(R.id.nologin);
                    nologinBtn.setOnClickListener(startLoginleListener);
                    break;
            }
            listViews.add(listView);
            mViews.add(mView1);
        }
        mViewPager = (NoScrollViewPager) mView.findViewById(intersky.conversation.R.id.load_pager);
        mViewPager.setNoScroll(false);
        mLoderPageAdapter = new ConversationPageAdapter(mViews, names);
        mViewPager.setAdapter(mLoderPageAdapter);
        mViewPager.setCurrentItem(0);
        mTabHeadView.setViewPager(mViewPager);
        mTabHeadView.setmOnTabLisener(onTabLisener);
        searchView = mView.findViewById(R.id.nomal);
        searchView.setOnClickListener(showSearchListener);
        updataReadHit();
        return mView;
    }

    public void updataReadHit() {
        if(mTabHeadView != null)
        {
            mTabHeadView.setHit(BigWinerDBHelper.getInstance(mMainActivity).getAllUnReadCount(Conversation.CONVERSATION_TYPE_MESSAGE),4);
        }
        mMainActivity.mMainPresenter.updataConversationHit();
    }

    public void doSearch() {
        Intent intent = new Intent(mMainActivity, SearchActivity.class);
        mMainActivity.startActivity(intent);
    }

    public View.OnClickListener showSearchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doSearch();
        }
    };

    public ConversationAdapter.ConversationFunction swipClickListener = new ConversationAdapter.ConversationFunction() {

        @Override
        public void read(Conversation conversation) {
            doRead(conversation);
        }

        @Override
        public void delete(Conversation conversation) {
            doDelete(conversation);
        }

    };

    public ConversationAdapter.OnItemClickListener itemClickListener = new ConversationAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Conversation conversation, int position, View view) {
            if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_MEETING)) {
                Meeting meeting = new Meeting();
                meeting.recordid = conversation.detialId;
                Intent intent = new Intent(mMainActivity, MeetingDetialActivity.class);
                intent.putExtra("meeting", meeting);
                mMainActivity.startActivity(intent);
            } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
                Contacts contacts = BigwinerApplication.mApp.contactManager.contactsHashMap.get(conversation.detialId);
                if (contacts == null) {
                    contacts = BigwinerApplication.mApp.contactManager.friendHashMap.get(conversation.detialId);
                }
                if (contacts == null) {
                    contacts = new Contacts();
                    contacts.mRecordid = conversation.detialId;
                    contacts.mRName = conversation.mTitle;
                    contacts.mName = conversation.mTitle;
                }
                Intent intent = new Intent(mMainActivity, ChatActivity.class);
                intent.putExtra("isow", true);
                intent.putExtra("contacts", contacts);
                mMainActivity.startActivity(intent);
            } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE)) {
                Intent intent = new Intent(mMainActivity, WebMessageActivity.class);
                if (conversation.sourcePath.length() == 0)
                {
                    intent.putExtra("detialid",conversation.detialId);
                    intent.putExtra("type",conversation.mType);
                    intent.putExtra("url", DetialAsks.getNoticeNewUrl(conversation.detialId));
                    intent.putExtra("title",conversation.mTitle);
                    intent.putExtra("des",conversation.mSubject);
                    intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
                }

                else
                {
                    intent.putExtra("url", conversation.sourcePath);
                    intent.putExtra("detialid",conversation.detialId);
                    intent.putExtra("type",conversation.mType);
                    intent.putExtra("title",conversation.mTitle);
                    intent.putExtra("des",conversation.mSubject);
                    intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
                    ConversationAsks.getUpdata(conversation.detialId);
                }

                intent.putExtra("showshare", true);
                mMainActivity.startActivity(intent);
            } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_NEWS)) {
                Intent intent = new Intent(mMainActivity, WebMessageActivity.class);
                if (conversation.sourcePath.length() == 0){
                    intent.putExtra("url", DetialAsks.getNoticeNewUrl(conversation.detialId));
                    intent.putExtra("type",conversation.mType);
                    intent.putExtra("title",conversation.mTitle);
                    intent.putExtra("detialid",conversation.detialId);
                    intent.putExtra("des",conversation.mSubject);
                    intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
                }
                else
                {
                    intent.putExtra("url", conversation.sourcePath);
                    intent.putExtra("type",conversation.mType);
                    intent.putExtra("title",conversation.mTitle);
                    intent.putExtra("detialid",conversation.detialId);
                    intent.putExtra("des",conversation.mSubject);
                    intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
                    ConversationAsks.getUpdata(conversation.detialId);
                }
                intent.putExtra("showshare", true);
                mMainActivity.startActivity(intent);
            }
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            switch (mViewPager.getCurrentItem()) {
                case 0:
                    mMainActivity.waitDialog.show();
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).reset();
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).reset();
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).allpagesizemax = 1;
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).reset();
                    BigwinerApplication.mApp.conversationManager.messageback.clear();
                    for (int i = 0; i < BigWinerConversationManager.getInstance().mConversations.size(); i++) {
                        if (BigWinerConversationManager.getInstance().mConversations.get(i).mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
                            BigwinerApplication.mApp.conversationManager.messageback.add(BigWinerConversationManager.getInstance().mConversations.get(i));
                        }

                    }
                    BigWinerConversationManager.getInstance().mConversations.clear();
                    mMainActivity.allAdapter.notifyDataSetChanged();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NOTICE).clear();
                    mMainActivity.noticeAdapter.notifyDataSetChanged();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NEWS).clear();
                    mMainActivity.newsAdapter.notifyDataSetChanged();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING).clear();
                    mMainActivity.meetingAdapter.notifyDataSetChanged();
                    BigwinerApplication.mApp.conversationManager.meetingfinish = false;
                    BigwinerApplication.mApp.conversationManager.noticefinish = false;
                    BigwinerApplication.mApp.conversationManager.newfinish = false;
                    ConversationAsks.getNewsAndNotices(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, ConversationAsks.TYPE_NOTICE, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).pagesize
                            , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).currentpage);
                    ConversationAsks.getNewsAndNotices(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, ConversationAsks.TYPE_NEWS, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).pagesize
                            , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).currentpage);
                    ConversationAsks.getMettings(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).pagesize
                            , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).currentpage);
                    break;
                case 1:
                    if (BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).currentpage < BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).totlepage) {
                        mMainActivity.waitDialog.show();
                        ConversationAsks.getNewsAndNotices(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, ConversationAsks.TYPE_NOTICE,
                                BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).pagesize, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).currentpage + 1);
                    } else {
                        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.system_addall));
                    }
                    break;
                case 2:
                    if (BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).currentpage < BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).totlepage) {
                        mMainActivity.waitDialog.show();
                        ConversationAsks.getNewsAndNotices(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, ConversationAsks.TYPE_NEWS,
                                BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).pagesize, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).currentpage + 1);
                    } else {
                        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.system_addall));
                    }
                    break;
                case 3:
                    if (BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).currentpage < BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).totlepage) {
                        mMainActivity.waitDialog.show();
                        ConversationAsks.getMettings(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).pagesize
                                , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).currentpage + 1);
                    } else {
                        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.system_addall));
                    }
                    break;
                case 4:
                    break;
            }
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            ModuleDetial moduleDetial = null;

            switch (mViewPager.getCurrentItem()) {
                case 0:
                    mMainActivity.waitDialog.show();
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).reset();
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).reset();
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).allpagesizemax = 1;
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).reset();
                    BigwinerApplication.mApp.conversationManager.messageback.clear();
                    for (int i = 0; i < BigWinerConversationManager.getInstance().mConversations.size(); i++) {
                        if (BigWinerConversationManager.getInstance().mConversations.get(i).mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
                            BigwinerApplication.mApp.conversationManager.messageback.add(BigWinerConversationManager.getInstance().mConversations.get(i));
                        }

                    }
                    BigWinerConversationManager.getInstance().mConversations.clear();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NOTICE).clear();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NEWS).clear();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING).clear();
                    BigwinerApplication.mApp.conversationManager.meetingfinish = false;
                    BigwinerApplication.mApp.conversationManager.noticefinish = false;
                    BigwinerApplication.mApp.conversationManager.newfinish = false;
                    ConversationAsks.getNewsAndNotices(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, ConversationAsks.TYPE_NOTICE, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).pagesize
                            , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).currentpage);
                    ConversationAsks.getNewsAndNotices(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, ConversationAsks.TYPE_NEWS, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).pagesize
                            , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).currentpage);
                    ConversationAsks.getMettings(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).pagesize
                            , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).currentpage);
                    break;
                case 1:
                    mMainActivity.waitDialog.show();
                    for (int i = 0; i < BigWinerConversationManager.getInstance().mConversations.size(); i++) {
                        if (BigWinerConversationManager.getInstance().mConversations.get(i).mType.equals(Conversation.CONVERSATION_TYPE_NOTICE)) {
                            BigWinerConversationManager.getInstance().mConversations.remove(i);
                            i--;
                        }
                    }
                    mMainActivity.allAdapter.notifyDataSetChanged();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NOTICE).clear();
                    mMainActivity.noticeAdapter.notifyDataSetChanged();
                    moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE);
                    moduleDetial.reset();
                    ConversationAsks.getNewsAndNotices(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, ConversationAsks.TYPE_NOTICE,
                            BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).pagesize, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).currentpage);
                    break;
                case 2:
                    mMainActivity.waitDialog.show();
                    for (int i = 0; i < BigWinerConversationManager.getInstance().mConversations.size(); i++) {
                        if (BigWinerConversationManager.getInstance().mConversations.get(i).mType.equals(Conversation.CONVERSATION_TYPE_NEWS)) {
                            BigWinerConversationManager.getInstance().mConversations.remove(i);
                            i--;
                        }
                    }
                    mMainActivity.allAdapter.notifyDataSetChanged();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NEWS).clear();
                    mMainActivity.newsAdapter.notifyDataSetChanged();
                    moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS);
                    moduleDetial.reset();
                    ConversationAsks.getNewsAndNotices(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, ConversationAsks.TYPE_NEWS,
                            BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).pagesize, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).currentpage);
                    break;
                case 3:
                    mMainActivity.waitDialog.show();
                    for (int i = 0; i < BigWinerConversationManager.getInstance().mConversations.size(); i++) {
                        if (BigWinerConversationManager.getInstance().mConversations.get(i).mType.equals(Conversation.CONVERSATION_TYPE_MEETING)) {
                            BigWinerConversationManager.getInstance().mConversations.remove(i);
                            i--;
                        }
                    }
                    mMainActivity.allAdapter.notifyDataSetChanged();
                    BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING).clear();
                    mMainActivity.meetingAdapter.notifyDataSetChanged();
                    moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING);
                    moduleDetial.reset();
                    ConversationAsks.getMettings(mMainActivity, mMainActivity.mMainPresenter.mMainHandler, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).pagesize
                            , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).currentpage);
                    break;
                case 4:
                    break;
            }
            view.onHeaderRefreshComplete();
        }
    };

    public View.OnClickListener startLoginleListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            BigwinerApplication.mApp.startLogin(mMainPresenter.mMainActivity, MainActivity.ACTION_UPDATE_FRIENDS_LIST);
        }
    };

    public TabHeadView.OnTabLisener onTabLisener = new TabHeadView.OnTabLisener() {

        @Override
        public void TabClick(int tab) {
            if (tab == 4) {
                if (BigwinerApplication.mApp.mAccount.islogin == false) {
                    nologinBtn.setVisibility(View.VISIBLE);
                } else {
                    nologinBtn.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    public void messageMenu(View view,Conversation conversation) {
        if(conversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
            if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                Context wrapper = mMainActivity;
                wrapper = new ContextThemeWrapper(wrapper, R.style.NoPopupAnimation);
                mMainActivity.popup = new PopupMenu(wrapper, view, Gravity.RIGHT);
            }
            else
            {
                mMainActivity.popup = new PopupMenu(mMainActivity, view,Gravity.RIGHT);
            }

            mMainActivity.popup.getMenuInflater().inflate(R.menu.conversation_menu, mMainActivity.popup.getMenu());
            mMainActivity.popup.setOnMenuItemClickListener(new MyOnMenuItemClickListener(conversation));
            mMainActivity.popup.show();
        }
    }

    public class MyOnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener
    {

        public Conversation msg;

        public MyOnMenuItemClickListener(Conversation item) {
            this.msg = item;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            MenuItemClick(item,msg);
            return false;
        }
    }

    public void MenuItemClick(MenuItem item,Conversation conversation) {
        if (item.getTitle().equals(mMainActivity.getString(R.string.menu_set_read))) {
            doRead(conversation);
        } else if (item.getTitle().equals(mMainActivity.getString(R.string.menu_delete))) {
            doDelete(conversation);

        }
    }

    public void doRead(Conversation msg) {
       BigwinerApplication.mApp.conversationManager.setRead(mMainActivity,msg.detialId);
        mMainActivity.allAdapter.notifyDataSetChanged();
        mMainActivity.meessageAdapter.notifyDataSetChanged();
        updataReadHit();
    }

    public void doDelete(Conversation msg) {
        AppUtils.creatDialogTowButton(mMainActivity,mMainActivity.getString(R.string.delete_conversation),"",
                mMainActivity.getString(R.string.button_word_cancle),mMainActivity.getString(R.string.button_delete),
                null,new OnDeleteListener(msg));

    }

    public class OnDeleteListener implements DialogInterface.OnClickListener {

        public Conversation msg;

        public OnDeleteListener(Conversation msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            BigwinerApplication.mApp.conversationManager.deleteMessage(mMainActivity,msg.detialId);
            BigwinerApplication.mApp.chatPagerHashMap.remove(msg.detialId);
            if(ChatUtils.getChatUtils().mLeaveMessageHandler != null) {
                Message message = new Message();
                message.what = LeaveMessageHandler.DELETE_CHAT_SOURCE_DETIAL;
                message.obj = msg.detialId;
                ChatUtils.getChatUtils().mLeaveMessageHandler.sendMessage(message);
            }
            mMainActivity.allAdapter.notifyDataSetChanged();
            mMainActivity.meessageAdapter.notifyDataSetChanged();
            updataReadHit();
        }
    }
}
