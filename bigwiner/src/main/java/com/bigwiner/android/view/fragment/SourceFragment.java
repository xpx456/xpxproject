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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.BigwinerScanPremissionResult;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.handler.BigwinerPermissionHandler;
import com.bigwiner.android.presenter.MainPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.ContactsListActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SourceAddListActivity;
import com.bigwiner.android.view.activity.SourceCollectListActivity;
import com.bigwiner.android.view.activity.SourceCreatActivity;
import com.bigwiner.android.view.activity.SourceDetialActivity;
import com.bigwiner.android.view.adapter.SourceListAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseFragment;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.scan.ScanUtils;
import intersky.select.entity.Select;


public class SourceFragment extends BaseFragment {

    public RecyclerView contactList;
    public RelativeLayout btnadd;
    public RelativeLayout btncollect;
    public RelativeLayout toplayer;
    public TextView type;
    public SearchViewLayout searchViewLayout;
    public RelativeLayout btntype;
    public MainActivity mMainActivity;
    public PopupWindow popupWindow;
    public SourceFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_source, container, false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        type = mView.findViewById(R.id.typetxt);
        btntype = mView.findViewById(R.id.type);
        btntype.setOnClickListener(setTypeListener);
        btnadd = mView.findViewById(R.id.addsource);
        toplayer = mView.findViewById(R.id.toplayer);
        btncollect = mView.findViewById(R.id.collectsource);
        searchViewLayout = mView.findViewById(R.id.search);
        searchViewLayout.mSetOnSearchListener(onEditorActionListener);
        PullToRefreshView pullToRefreshView = mView.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.getmHeaderView().setVisibility(View.INVISIBLE);
        pullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
        pullToRefreshView.setOnFooterRefreshListener(onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(onHeadRefreshListener);

        contactList =  mView.findViewById(R.id.source_List);
        contactList.setLayoutManager(new LinearLayoutManager(mMainActivity));
        contactList.setAdapter(mMainActivity.allSourceAdapter);
        mMainActivity.allSourceAdapter.setOnItemClickListener(onContactItemClickListener);
        mMainActivity.wantSourceAdapter.setOnItemClickListener(onContactItemClickListener);
        mMainActivity.gaveSourceAdapter.setOnItemClickListener(onContactItemClickListener);
        mMainActivity.searchSourceAdapter.setOnItemClickListener(onContactItemClickListener);
        btnadd.setOnClickListener(addSourceListener);
        btncollect.setOnClickListener(showCollectListener);
        return mView;
    }


    public SourceListAdapter.OnItemClickListener onContactItemClickListener = new SourceListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(SourceData sourceData, int position, View view) {
            startSourceDetial(sourceData);
        }
    };

    public View.OnClickListener showCollectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, SourceCollectListActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener addSourceListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(BigwinerApplication.mApp.checkConfirm(mMainActivity,mMainActivity.getString(R.string.confirm_source_title)))
            {
                Intent intent = new Intent(mMainActivity, SourceAddListActivity.class);
                mMainActivity.startActivity(intent);
            }
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            if(mMainActivity.issourceDataSearch == false)
            {
                if(mMainActivity.sourceType.equals("0"))
                {
                    if (mMainActivity.sorceAllPage.currentpage < mMainActivity.sorceAllPage.totlepage) {
                        mMainActivity.waitDialog.show();
                        SourceAsks.getSourceListAll(mMainActivity,mMainActivity.mMainPresenter.mMainHandler
                                ,mMainActivity.sourceType,mMainActivity.sorceAllPage.pagesize
                                ,mMainActivity.sorceAllPage.currentpage);
                    } else {
                        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.system_addall));
                    }
                }
                else if(mMainActivity.sourceType.equals("1"))
                {

                    if (mMainActivity.sorceWantPage.currentpage < mMainActivity.sorceWantPage.totlepage) {
                        mMainActivity.waitDialog.show();
                        SourceAsks.getSourceListAll(mMainActivity,mMainActivity.mMainPresenter.mMainHandler
                                ,mMainActivity.sourceType,mMainActivity.sorceWantPage.pagesize
                                ,mMainActivity.sorceWantPage.currentpage);
                    } else {
                        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.system_addall));
                    }
                }
                else
                {
                    if (mMainActivity.sorceGavePage.currentpage < mMainActivity.sorceGavePage.totlepage) {
                        mMainActivity.waitDialog.show();
                        SourceAsks.getSourceListAll(mMainActivity,mMainActivity.mMainPresenter.mMainHandler
                                ,mMainActivity.sourceType,mMainActivity.sorceGavePage.pagesize
                                ,mMainActivity.sorceGavePage.currentpage);
                    } else {
                        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.system_addall));
                    }
                }
            }
            else
            {
                if (mMainActivity.sorceSearchPage.currentpage < mMainActivity.sorceSearchPage.totlepage) {
                    mMainActivity.waitDialog.show();
                    SourceAsks.getSourceListSearch(mMainActivity,mMainActivity.mMainPresenter.mMainHandler
                            ,mMainActivity.sourceType,searchViewLayout.getText().toString(),mMainActivity.sorceSearchPage.pagesize
                            ,mMainActivity.sorceSearchPage.currentpage);
                } else {
                    AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.system_addall));
                }
            }


            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            if(mMainActivity.issourceDataSearch == false)
            {
                if(mMainActivity.sourceType.equals("0"))
                {
                    mMainActivity.sorceAllPage.reset();
                    mMainActivity.sourceAllData.clear();
                    mMainActivity.allSourceAdapter.notifyDataSetChanged();
                    mMainActivity.waitDialog.show();
                    SourceAsks.getSourceListAll(mMainActivity,mMainActivity.mMainPresenter.mMainHandler
                            ,mMainActivity.sourceType,mMainActivity.sorceAllPage.pagesize
                            ,mMainActivity.sorceAllPage.currentpage);
                }
                else if(mMainActivity.sourceType.equals("1"))
                {
                    mMainActivity.sorceWantPage.reset();
                    mMainActivity.sourceWantData.clear();
                    mMainActivity.wantSourceAdapter.notifyDataSetChanged();
                    mMainActivity.waitDialog.show();
                    SourceAsks.getSourceListAll(mMainActivity,mMainActivity.mMainPresenter.mMainHandler
                            ,mMainActivity.sourceType,mMainActivity.sorceWantPage.pagesize
                            ,mMainActivity.sorceWantPage.currentpage);
                }
                else
                {
                    mMainActivity.sorceGavePage.reset();
                    mMainActivity.sourceGaveData.clear();
                    mMainActivity.gaveSourceAdapter.notifyDataSetChanged();
                    mMainActivity.waitDialog.show();
                    SourceAsks.getSourceListAll(mMainActivity,mMainActivity.mMainPresenter.mMainHandler
                            ,mMainActivity.sourceType,mMainActivity.sorceGavePage.pagesize
                            ,mMainActivity.sorceGavePage.currentpage);
                }
            }
            else
            {

                mMainActivity.sourceSearchData.clear();
                mMainActivity.sorceSearchPage.reset();
                mMainActivity.searchSourceAdapter.notifyDataSetChanged();
                mMainActivity.waitDialog.show();
                SourceAsks.getSourceListSearch( mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                        , mMainActivity.sourceType,searchViewLayout.getText(), mMainActivity.sorceSearchPage.pagesize, mMainActivity.sorceSearchPage.currentpage);
            }

            view.onHeaderRefreshComplete();
        }
    };

    public void startSourceDetial(SourceData sourceData) {
        Intent intent = new Intent(mMainActivity, SourceDetialActivity.class);
        intent.putExtra("source", sourceData);
        mMainActivity.startActivity(intent);

    }


    public View.OnClickListener setTypeListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
            MenuItem menuItem = new MenuItem();
            menuItem.mListener = setSourceTypeListener;
            if(mMainActivity.sourceType.equals("0"))
            menuItem.select = true;
            menuItem.item = "0";
            menuItem.btnName = mMainActivity.getString(R.string.source_type_all);
            menuItems.add(menuItem);
            menuItem = new MenuItem();
            menuItem.mListener = setSourceTypeListener;
            if(mMainActivity.sourceType.equals("1"))
            menuItem.select = true;
            menuItem.item = "1";
            menuItem.btnName = mMainActivity.getString(R.string.source_type_want);
            menuItems.add(menuItem);
            menuItem = new MenuItem();
            menuItem.mListener = setSourceTypeListener;
            if(mMainActivity.sourceType.equals("2"))
            menuItem.select = true;
            menuItem.item = "2";
            menuItem.btnName = mMainActivity.getString(R.string.source_type_offer);
            menuItems.add(menuItem);
            popupWindow = AppUtils.creatPopMenu(mMainActivity,menuItems,btntype,btntype.getLeft()
                    ,toplayer.getTop()+toplayer.getHeight()-(toplayer.getHeight()-btntype.getHeight())/2,mMainActivity.mBasePresenter.mScreenDefine.ScreenWidth/2, (int) (mMainActivity.mBasePresenter.mScreenDefine.density*40));
        }

    };


    public void setType(Intent intent)
    {
        Select select = intent.getParcelableExtra("item");
        if(select.iselect == false)
        {
            if(type != null)
            type.setText(mMainActivity.getString(R.string.source_type_all));
        }
        else
        {
            if(type != null)
            {
                type.setText(select.mName);
            }
        }
    }

    public View.OnClickListener  setSourceTypeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String type = (String) v.getTag();
            if(!type.equals(mMainActivity.sourceType))
            {
                mMainActivity.sourceType = type;
                if(mMainActivity.issourceDataSearch == false)
                {
                    if(type.equals("0"))
                    {
                        mMainActivity.mSourceFragment.type.setText(mMainActivity.getString(R.string.source_type_all));
                        mMainActivity.mSourceFragment.contactList.setAdapter(mMainActivity.allSourceAdapter);

                        if(mMainActivity.sourceAllData.size() == 0 && mMainActivity.sorceAllPageF.isfinish == true)
                        {
                            mMainActivity.waitDialog.show();
                            SourceAsks.getSourceListAll( mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                                    , mMainActivity.sourceType, mMainActivity.sorceAllPage.pagesize, mMainActivity.sorceAllPage.currentpage);
                        }

                    }
                    else if(type.equals("1"))
                    {
                        mMainActivity.mSourceFragment.type.setText(mMainActivity.getString(R.string.source_type_want));
                        mMainActivity.mSourceFragment.contactList.setAdapter(mMainActivity.wantSourceAdapter);
                        if(mMainActivity.sourceWantData.size() == 0 && mMainActivity.sorceWantPageF.isfinish == true)
                        {
                            mMainActivity.waitDialog.show();
                            SourceAsks.getSourceListAll( mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                                    , mMainActivity.sourceType, mMainActivity.sorceWantPage.pagesize, mMainActivity.sorceWantPage.currentpage);
                        }
                    }
                    else
                    {
                        mMainActivity.mSourceFragment.type.setText(mMainActivity.getString(R.string.source_type_offer));
                        mMainActivity.mSourceFragment.contactList.setAdapter(mMainActivity.gaveSourceAdapter);
                        if(mMainActivity.sourceGaveData.size() == 0 && mMainActivity.sorceGavePageF.isfinish == true)
                        {
                            mMainActivity.waitDialog.show();
                            SourceAsks.getSourceListAll( mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                                    , mMainActivity.sourceType, mMainActivity.sorceGavePage.pagesize, mMainActivity.sorceGavePage.currentpage);
                        }
                    }
                }
                else
                {
                    if(type.equals("0"))
                    {
                        mMainActivity.mSourceFragment.type.setText(mMainActivity.getString(R.string.source_type_all));
                    }
                    else if(type.equals("1"))
                    {
                        mMainActivity.mSourceFragment.type.setText(mMainActivity.getString(R.string.source_type_want));
                    }
                    else
                    {
                        mMainActivity.mSourceFragment.type.setText(mMainActivity.getString(R.string.source_type_offer));
                    }
                    mMainActivity.sourceSearchData.clear();
                    mMainActivity.sorceSearchPage.reset();
                    mMainActivity.searchSourceAdapter.notifyDataSetChanged();
                    mMainActivity.waitDialog.show();
                    SourceAsks.getSourceListSearch( mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                            , mMainActivity.sourceType,searchViewLayout.getText(), mMainActivity.sorceSearchPage.pagesize, mMainActivity.sorceSearchPage.currentpage);
                }

            }
            if(popupWindow != null)
            popupWindow.dismiss();
        }
    };

    public TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                if(v.getText().toString().length() > 0)
                {
                    mMainActivity.issourceDataSearch = true;
                    mMainActivity.sourceSearchData.clear();
                    mMainActivity.sourceDataHashMap.clear();
                    mMainActivity.sorceSearchPage.reset();
                    mMainActivity.searchSourceAdapter.notifyDataSetChanged();
                    mMainActivity.mSourceFragment.contactList.setAdapter(mMainActivity.searchSourceAdapter);
                    mMainActivity.waitDialog.show();
                    SourceAsks.getSourceListSearch( mMainActivity, mMainActivity.mMainPresenter.mMainHandler
                            , mMainActivity.sourceType,v.getText().toString(), mMainActivity.sorceSearchPage.pagesize, mMainActivity.sorceSearchPage.currentpage);
                }
                else
                {
                    mMainActivity.issourceDataSearch = false;
                    if(type.equals("0"))
                    {
                        mMainActivity.mSourceFragment.contactList.setAdapter(mMainActivity.allSourceAdapter);
                    }
                    else if(type.equals("1"))
                    {
                        mMainActivity.mSourceFragment.contactList.setAdapter(mMainActivity.wantSourceAdapter);
                    }
                    else
                    {
                        mMainActivity.mSourceFragment.contactList.setAdapter(mMainActivity.gaveSourceAdapter);
                    }
                }
            }
            return true;
        }
    };


}
