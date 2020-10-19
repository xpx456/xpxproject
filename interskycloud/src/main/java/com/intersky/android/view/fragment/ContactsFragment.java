package com.intersky.android.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intersky.R;
import com.intersky.android.presenter.MainPresenter;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.BaseFragment;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.appbase.entity.Contacts;
import intersky.chat.SortContactComparator;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.SearchViewLayout;

public class ContactsFragment extends BaseFragment {

    public ListView contactList;
    public SearchViewLayout searchView;
    public MySlideBar msbar;
    public TextView mLetterText;
    public RelativeLayout mRelativeLetter;
    public MainActivity mMainActivity;
    public boolean isShowSearch = false;
    public ContactsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactList = (ListView)mView.findViewById(R.id.contacts_List);
        contactList.setOnItemClickListener(onContactItemClickListener);
        searchView = mView.findViewById(R.id.search);
        msbar = (MySlideBar) mView.findViewById(R.id.slideBar);
        mLetterText = (TextView) mView.findViewById(R.id.letter_text);
        mRelativeLetter = (RelativeLayout) mView.findViewById(R.id.letter_layer);
        msbar.setVisibility(View.INVISIBLE);
        msbar.setOnTouchLetterChangeListenner(mOnTouchLetterChangeListenner);
        contactList.setAdapter(mMainActivity.mMainPresenter.mContactAdapter);
        searchView.mSetOnSearchListener(mOnSearchActionListener);
        contactList.setOnScrollListener(mOnScoll);
        msbar.setmRelativeLayout(mRelativeLetter);
        msbar.setMletterView(mLetterText);
        return mView;
    }

    public void doSearch(String keyword) {
        if(keyword.length() == 0)
        {
            if(isShowSearch == true)
            {
                isShowSearch = false;
                contactList.setAdapter(mMainActivity.mMainPresenter.mContactAdapter);
                mMainActivity.mMainPresenter.updataContactView();
            }
            return;
        }
        boolean typebooleans[] = new boolean[27];
        ArrayList<Contacts> temps = new ArrayList<Contacts>();
        ArrayList<Contacts> tempheads = new ArrayList<Contacts>();
        for (int i = 0; i < InterskyApplication.mApp.contactManager.mOrganization.allContacts.size(); i++) {
            Contacts mContactModel = InterskyApplication.mApp.contactManager.mOrganization.allContacts.get(i);
            if (mContactModel.mType == Contacts.TYPE_PERSON) {
                if (mContactModel.getmPingyin().contains(keyword.toLowerCase()) || mContactModel.getName().contains(keyword)) {
                    temps.add(mContactModel);

                    String s = mContactModel.getmPingyin().substring(0, 1).toUpperCase();
                    int pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (typebooleans[pos] == false) {
                            tempheads.add(new Contacts(s));
                            typebooleans[pos] = true;
                        }
                    }
                    else
                    {
                        s = "#";
                        pos = CharacterParser.typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (typebooleans[pos] == false) {
                                tempheads.add(new Contacts(s));
                                typebooleans[pos] = true;
                            }
                        }
                    }
                }
            }
        }
        if (temps.size() == 0) {
            AppUtils.showMessage(mMainActivity.mMainPresenter.mMainActivity, mMainActivity.mMainPresenter.mMainActivity.getString(R.string.searchview_search_none));
        } else {
            mMainActivity.mMainPresenter.mSearchItems.clear();
            mMainActivity.mMainPresenter.mSearchHeadItems.clear();
            mMainActivity.mMainPresenter.mSearchItems.addAll(temps);
            mMainActivity.mMainPresenter.mSearchHeadItems.addAll(tempheads);
            mMainActivity.mMainPresenter.mSearchItems.addAll(0, mMainActivity.mMainPresenter.mSearchHeadItems);
            Collections.sort(mMainActivity.mMainPresenter.mSearchItems, new SortContactComparator());
            Collections.sort(mMainActivity.mMainPresenter.mSearchHeadItems, new SortContactComparator());
            if (InterskyApplication.mApp.contactManager.mOrganization.phoneContacts != null) {
                mMainActivity.mMainPresenter.mSearchItems.add(0, InterskyApplication.mApp.contactManager.mOrganization.phoneContacts);
            }
            mMainActivity.mMainPresenter.mSearchItems.add(0, InterskyApplication.mApp.contactManager.mOrganization.organizationContacts);
            contactList.setAdapter(mMainActivity.mMainPresenter.mSearchContactAdapter);
            isShowSearch = true;
            mMainActivity.mMainPresenter.updataContactView();
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

    public MySlideBar.OnTouchLetterChangeListenner mOnTouchLetterChangeListenner = new MySlideBar.OnTouchLetterChangeListenner()
    {

        @Override
        public void onTouchLetterChange(MotionEvent event, int s)
        {
            // TODO Auto-generated method stub
            mMainActivity.mMainPresenter.LetterChange(s);

        }
    };

    public AdapterView.OnItemClickListener onContactItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMainActivity.mMainPresenter.contactsClick((Contacts) parent.getAdapter().getItem(position));

        }
    };

}
