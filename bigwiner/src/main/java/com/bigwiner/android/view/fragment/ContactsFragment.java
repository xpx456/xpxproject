package com.bigwiner.android.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.BigwinerScanPremissionResult;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.handler.BigwinerPermissionHandler;
import com.bigwiner.android.presenter.MainPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.ContactsListActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;
import com.bigwiner.android.view.adapter.ContactsAdapter;

import intersky.appbase.BaseFragment;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.chat.ContactManager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.mywidget.SpinnerExView;
import intersky.scan.ScanUtils;
import intersky.select.entity.Select;
import mining.app.zxing.decoding.Intents;


public class ContactsFragment extends BaseFragment {

    public RecyclerView contactList;
    public TextView myCycle;
    public TextView city;
    public TextView area;
    public TextView type;
    public RelativeLayout btncity;
    public RelativeLayout btnarea;
    public RelativeLayout btntype;
    public ImageView scan;
    public MainActivity mMainActivity;
    public TextView noLogin;
    public SearchViewLayout searchViewLayout;

    public ContactsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_contacts, container, false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        searchViewLayout = mView.findViewById(R.id.search);
        searchViewLayout.mSetOnSearchListener(onEditorActionListener);
        noLogin = mView.findViewById(R.id.nologin);
        if (BigwinerApplication.mApp.mAccount.islogin) {
            noLogin.setVisibility(View.INVISIBLE);
        } else {
            noLogin.setVisibility(View.VISIBLE);
        }
        city = mView.findViewById(R.id.citytxt);
        btncity = mView.findViewById(R.id.city_head);
        btncity.setOnClickListener(setCityListener);
        area = mView.findViewById(R.id.areatxt);
        btnarea = mView.findViewById(R.id.area_head);
        btnarea.setOnClickListener(setAreaListener);
        type = mView.findViewById(R.id.typetxt);
        btntype = mView.findViewById(R.id.type_head);
        btntype.setOnClickListener(setTypeListener);
        scan = mView.findViewById(R.id.scan);
        PullToRefreshView pullToRefreshView = mView.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.getmHeaderView().setVisibility(View.INVISIBLE);
        pullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
        pullToRefreshView.setOnFooterRefreshListener(onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(onHeadRefreshListener);

        noLogin.setOnClickListener(startLoginleListener);
        contactList = (RecyclerView) mView.findViewById(R.id.contacts_List);
        contactList.setLayoutManager(new LinearLayoutManager(mMainActivity));
        myCycle = mView.findViewById(R.id.cycle);
        contactList.setAdapter(mMainActivity.contactsAdapter);
        mMainActivity.contactsAdapter.setOnItemClickListener(onContactItemClickListener);
        mMainActivity.searchAdapter.setOnItemClickListener(onContactItemClickListener);
        myCycle.setOnClickListener(showMycycleListener);
        scan.setOnClickListener(showScanListener);
        return mView;
    }


    public View.OnClickListener showScanListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMainActivity.permissionRepuest = ScanUtils.getInstance().checkStartScan(mMainActivity,""
                    ,new BigwinerPermissionHandler(mMainActivity,"",null,mMainActivity.getString(R.string.conversation_friend_add))
                    , new BigwinerScanPremissionResult(mMainActivity,"",null,mMainActivity.getString(R.string.conversation_friend_add)));
        }
    };

    public ContactsAdapter.OnItemClickListener onContactItemClickListener = new ContactsAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Contacts contacts, int position, View view) {
            startContactsDetial(contacts);
        }
    };

    public View.OnClickListener showMycycleListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, ContactsListActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener startLoginleListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            BigwinerApplication.mApp.startLogin(mMainPresenter.mMainActivity, MainActivity.ACTION_UPDATE_FRIENDS_LIST);
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            if (BigwinerApplication.mApp.contactManager.contactPage.currentpage < BigwinerApplication.mApp.contactManager.contactPage.totlepage) {
                mMainActivity.waitDialog.show();
                ContactsAsks.getContactsList(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                        , BigwinerApplication.mApp.contactManager.contactPage.pagesize, BigwinerApplication.mApp.contactManager.contactPage.currentpage + 1, type.getText().toString(),area.getText().toString(),city.getText().toString());
            } else {
                AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.system_addall));
            }
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            mMainActivity.waitDialog.show();
            BigwinerApplication.mApp.contactManager.contactPage.reset();
            BigwinerApplication.mApp.contactManager.contactsHashMap.clear();
            BigwinerApplication.mApp.contactManager.mContactss.clear();
            mMainActivity.contactsAdapter.notifyDataSetChanged();
            if(mMainActivity.isupdatacontact == false)
            {
                mMainActivity.isupdatacontact = true;
                ContactsAsks.getContactsList(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                        , BigwinerApplication.mApp.contactManager.contactPage.pagesize, BigwinerApplication.mApp.contactManager.contactPage.currentpage,
                        type.getText().toString(),area.getText().toString(),city.getText().toString());
            }

            view.onHeaderRefreshComplete();
        }
    };

    public void startContactsDetial(Contacts contacts) {
        Intent intent = new Intent(mMainActivity, ContactDetialActivity.class);
        intent.putExtra("contacts", contacts);
        mMainActivity.startActivity(intent);

    }

    public View.OnClickListener setCityListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.startSelectView(mMainActivity,BigwinerApplication.mApp.ports.list
                    ,mMainActivity.getString(R.string.contacts_head_city), MainActivity.ACTION_SET_CITY,true,true,true);
        }

    };

    public View.OnClickListener setAreaListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.startSelectView(mMainActivity,BigwinerApplication.mApp.businessareaSelect.list
                    ,mMainActivity.getString(R.string.contacts_head_area), MainActivity.ACTION_SET_AREA,true,true,true);
        }

    };

    public View.OnClickListener setTypeListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.startSelectView(mMainActivity,BigwinerApplication.mApp.businesstypeSelect.list
                    ,mMainActivity.getString(R.string.contacts_head_type), MainActivity.ACTION_SET_TYPE,true,true,true);
        }

    };

    public void setCity(Intent intent)
    {
        Select select = intent.getParcelableExtra("item");
        if(select.iselect == false)
        {
            if(city != null)
            city.setText(mMainActivity.getString(R.string.contacts_head_city));
        }
        else
        {
            if(city != null)
            {
                city.setText(select.mName);
            }
        }
        mMainActivity.waitDialog.show();
        if(searchViewLayout.getText().toString().length() == 0)
        {

            BigwinerApplication.mApp.contactManager.contactPage.reset();
            BigwinerApplication.mApp.contactManager.contactsHashMap.clear();
            BigwinerApplication.mApp.contactManager.mContactss.clear();
            mMainActivity.contactsAdapter.notifyDataSetChanged();
            if(mMainActivity.isupdatacontact == false)
            {
                mMainActivity.isupdatacontact = true;
                ContactsAsks.getContactsList(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                        , BigwinerApplication.mApp.contactManager.contactPage.pagesize,
                        BigwinerApplication.mApp.contactManager.contactPage.currentpage,
                        type.getText().toString(),area.getText().toString(),city.getText().toString());
            }

        }
        else
        {
            BigwinerApplication.mApp.contactManager.searchPage.reset();
            BigwinerApplication.mApp.contactManager.searchHashMap.clear();
            BigwinerApplication.mApp.contactManager.mContactssearch.clear();
            mMainActivity.searchAdapter.notifyDataSetChanged();
            ContactsAsks.getContactsListSearch(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                    , BigwinerApplication.mApp.contactManager.searchPage.pagesize,
                    BigwinerApplication.mApp.contactManager.searchPage.currentpage,
                    type.getText().toString(),area.getText().toString(),city.getText().toString()
                    ,searchViewLayout.getText().toString());
        }

    }

    public void setType(Intent intent)
    {
        Select select = intent.getParcelableExtra("item");
        if(select.iselect == false)
        {
            if(type != null)
            type.setText(mMainActivity.getString(R.string.contacts_head_type));
        }
        else
        {
            if(type != null)
            {
                type.setText(select.mName);
            }
        }
        mMainActivity.waitDialog.show();
        if(searchViewLayout.getText().toString().length() == 0)
        {

            BigwinerApplication.mApp.contactManager.contactPage.reset();
            BigwinerApplication.mApp.contactManager.contactsHashMap.clear();
            BigwinerApplication.mApp.contactManager.mContactss.clear();
            mMainActivity.contactsAdapter.notifyDataSetChanged();
            if(mMainActivity.isupdatacontact == false)
            {
                mMainActivity.isupdatacontact = true;
                ContactsAsks.getContactsList(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                        , BigwinerApplication.mApp.contactManager.contactPage.pagesize,
                        BigwinerApplication.mApp.contactManager.contactPage.currentpage,
                        type.getText().toString(),area.getText().toString(),city.getText().toString());
            }

        }
        else
        {
            BigwinerApplication.mApp.contactManager.searchPage.reset();
            BigwinerApplication.mApp.contactManager.searchHashMap.clear();
            BigwinerApplication.mApp.contactManager.mContactssearch.clear();
            mMainActivity.searchAdapter.notifyDataSetChanged();
            ContactsAsks.getContactsListSearch(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                    , BigwinerApplication.mApp.contactManager.searchPage.pagesize,
                    BigwinerApplication.mApp.contactManager.searchPage.currentpage,
                    type.getText().toString(),area.getText().toString(),city.getText().toString()
                    ,searchViewLayout.getText().toString());
        }

    }

    public void setArea(Intent intent)
    {
        Select select = intent.getParcelableExtra("item");
        if(select.iselect == false)
        {
            if(area != null)
                area.setText(mMainActivity.getString(R.string.contacts_head_area));
        }
        else
        {
            if(area != null)
            {
                area.setText(select.mName);
            }
        }

        mMainActivity.waitDialog.show();
        if(searchViewLayout.getText().toString().length() == 0)
        {
            BigwinerApplication.mApp.contactManager.contactPage.reset();
            BigwinerApplication.mApp.contactManager.contactsHashMap.clear();
            BigwinerApplication.mApp.contactManager.mContactss.clear();
            if(mMainActivity.isupdatacontact == false)
            {
                mMainActivity.isupdatacontact = true;
                ContactsAsks.getContactsList(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                        , BigwinerApplication.mApp.contactManager.contactPage.pagesize,
                        BigwinerApplication.mApp.contactManager.contactPage.currentpage,
                        type.getText().toString(),area.getText().toString(),city.getText().toString());
            }

        }
        else
        {
            BigwinerApplication.mApp.contactManager.searchPage.reset();
            BigwinerApplication.mApp.contactManager.searchHashMap.clear();
            BigwinerApplication.mApp.contactManager.mContactssearch.clear();
            ContactsAsks.getContactsListSearch(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                    , BigwinerApplication.mApp.contactManager.searchPage.pagesize,
                    BigwinerApplication.mApp.contactManager.searchPage.currentpage,
                    type.getText().toString(),area.getText().toString(),city.getText().toString()
                    ,searchViewLayout.getText().toString());
        }

    }

    public TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                if(v.getText().toString().length() > 0)
                {
                    ContactManager.mContactManager.mContactssearch.clear();
                    mMainActivity.searchAdapter.notifyDataSetChanged();
                    ContactManager.mContactManager.searchHashMap.clear();
                    contactList.setAdapter(mMainActivity.searchAdapter);
                    mMainActivity.waitDialog.show();
                    if(mMainActivity.isupdatacontact == false)
                    {
                        mMainActivity.isupdatacontact = true;
                        ContactsAsks.getContactsListSearch(mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                                , BigwinerApplication.mApp.contactManager.contactPage.pagesize, BigwinerApplication.mApp.contactManager.contactPage.currentpage,
                                type.getText().toString(),area.getText().toString(),city.getText().toString(),v.getText().toString());
                    }
                }
                else
                {
                    contactList.setAdapter(mMainActivity.contactsAdapter);
                }
            }
            return true;
        }
    };
}
