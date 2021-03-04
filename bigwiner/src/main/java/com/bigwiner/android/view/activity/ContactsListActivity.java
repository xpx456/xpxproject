package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.ContactsListPresenter;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bigwiner.android.view.adapter.ContactsSelectAdapter;
import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;

/**
 * Created by xpx on 2017/8/18.
 */

public class ContactsListActivity extends BaseActivity {

    public static final int TYPE_MY = 0;
    public static final int TYPE_WANT = 1;
    public static final int TYPE_ALL = 2;
    public ContactsListPresenter mContactsListPresenter = new ContactsListPresenter(this);
    public RecyclerView listView;
    public ImageView back;
    public TextView showList;
    public ContactsAdapter contactsAdapter;
    public ContactsSelectAdapter contactsSelectAdapter;
    public ContactsSelectAdapter contactsSelectSearchAdapter;
    public ArrayList<Contacts> mSearchItems = new ArrayList<Contacts>();
    public ArrayList<Contacts> mSearchHeadItems = new ArrayList<Contacts>();
    public boolean[] typeboolsearch= new boolean[27];
    public Meeting meeting;
    public MySlideBar msbar;
    public TextView mLetterText;
    public RelativeLayout mRelativeLetter;
    public ArrayList<Contacts> listcontacts = new ArrayList<Contacts>();
    public ModuleDetial contactdetial = new ModuleDetial();
    public SearchViewLayout searchViewLayout;
    public boolean showSearch = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mContactsListPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener showChatListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactsListPresenter.startChatList();
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            if (mContactsListPresenter.mContactsListActivity.contactdetial.currentpage < mContactsListPresenter.mContactsListActivity.contactdetial.totlepage) {
                mContactsListPresenter.mContactsListActivity.waitDialog.show();
                switch (mContactsListPresenter.mContactsListActivity.getIntent().getIntExtra("type",ContactsListActivity.TYPE_ALL))
                {
                    case ContactsListActivity.TYPE_MY:
                        ContactsAsks.getMeetingAttMyContacts(mContactsListPresenter.mContactsListActivity,mContactsListPresenter.contactsListHandler,
                                mContactsListPresenter.mContactsListActivity.meeting, mContactsListPresenter.mContactsListActivity.contactdetial.pagesize,
                                mContactsListPresenter.mContactsListActivity.contactdetial.currentpage+1);
                        break;
                    case ContactsListActivity.TYPE_WANT:
                        ContactsAsks.getMeetingAttWantContacts(mContactsListPresenter.mContactsListActivity,mContactsListPresenter.contactsListHandler,
                                mContactsListPresenter.mContactsListActivity.meeting, mContactsListPresenter.mContactsListActivity.contactdetial.pagesize,
                                mContactsListPresenter.mContactsListActivity.contactdetial.currentpage+1);
                        break;
                    case ContactsListActivity.TYPE_ALL:
                        ContactsAsks.getMeetingAttAllContacts(mContactsListPresenter.mContactsListActivity,mContactsListPresenter.contactsListHandler,
                                mContactsListPresenter.mContactsListActivity.meeting, mContactsListPresenter.mContactsListActivity.contactdetial.pagesize,
                                mContactsListPresenter.mContactsListActivity.contactdetial.currentpage+1);
                        break;
                }
            } else {
                AppUtils.showMessage(mContactsListPresenter.mContactsListActivity, mContactsListPresenter.mContactsListActivity.getString(R.string.system_addall));
            }
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            mContactsListPresenter.mContactsListActivity.contactdetial.reset();
            mContactsListPresenter.mContactsListActivity.listcontacts.clear();
            mContactsListPresenter.mContactsListActivity.contactsAdapter.notifyDataSetChanged();
            switch (mContactsListPresenter.mContactsListActivity.getIntent().getIntExtra("type",ContactsListActivity.TYPE_ALL))
            {
                case ContactsListActivity.TYPE_MY:
                    ContactsAsks.getMeetingAttMyContacts(mContactsListPresenter.mContactsListActivity,mContactsListPresenter.contactsListHandler,
                            mContactsListPresenter.mContactsListActivity.meeting, mContactsListPresenter.mContactsListActivity.contactdetial.pagesize,
                            mContactsListPresenter.mContactsListActivity.contactdetial.currentpage);
                    break;
                case ContactsListActivity.TYPE_WANT:
                    ContactsAsks.getMeetingAttWantContacts(mContactsListPresenter.mContactsListActivity,mContactsListPresenter.contactsListHandler,
                            mContactsListPresenter.mContactsListActivity.meeting, mContactsListPresenter.mContactsListActivity.contactdetial.pagesize,
                            mContactsListPresenter.mContactsListActivity.contactdetial.currentpage);
                    break;
                case ContactsListActivity.TYPE_ALL:
                    ContactsAsks.getMeetingAttAllContacts(mContactsListPresenter.mContactsListActivity,mContactsListPresenter.contactsListHandler,
                            mContactsListPresenter.mContactsListActivity.meeting, mContactsListPresenter.mContactsListActivity.contactdetial.pagesize,
                            mContactsListPresenter.mContactsListActivity.contactdetial.currentpage);
                    break;
            }
            view.onHeaderRefreshComplete();
        }
    };

    public ContactsAdapter.OnItemClickListener onContactItemClickListener = new ContactsAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Contacts contacts, int position, View view) {
            mContactsListPresenter.startContactsDetial(contacts);
        }
    };

    public ContactsSelectAdapter.OnItemClickListener onContactItemClickListener2 = new ContactsSelectAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Contacts contacts, int position, View view) {
            mContactsListPresenter.startContactsDetial(contacts);
        }
    };

    public MySlideBar.OnTouchLetterChangeListenner mOnTouchLetterChangeListenner = new MySlideBar.OnTouchLetterChangeListenner()
    {

        @Override
        public void onTouchLetterChange(MotionEvent event, int s)
        {
            // TODO Auto-generated method stub
            mContactsListPresenter.LetterChange(s);

        }
    };

}
