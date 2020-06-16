package com.interskypad.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.interskypad.R;
import com.interskypad.asks.ProductAsks;
import com.interskypad.entity.Category;
import com.interskypad.manager.ProducterManager;
import com.interskypad.presenter.MainPresenter;
import com.interskypad.view.activity.MainActivity;

import intersky.appbase.BaseFragment;
import intersky.apputils.AppUtils;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.ScanSearchViewLayout;
import intersky.scan.ScanUtils;

@SuppressLint("ValidFragment")
public class CatalogFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    public MainPresenter mMainPresenter;
    public ListView mCategoryList;
    public GridView mContentCrid;
    public ScanSearchViewLayout mScanSearchViewLayout;
    public PullToRefreshView mPullToRefreshView;
    public CatalogFragment(MainPresenter mMainPresenter) {
        // Required empty public constructor
        this.mMainPresenter = mMainPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_catalog, container, false);
        mCategoryList = (ListView) mView.findViewById(R.id.catalog_category_list);
        mContentCrid = (GridView) mView.findViewById(R.id.catalog_content_grid);
        mScanSearchViewLayout = mView.findViewById(R.id.catalog_search_layer);
        mCategoryList.setAdapter(mMainPresenter.mMainActivity.catalogCategoryAdapter);
        mContentCrid.setAdapter(mMainPresenter.mMainActivity.catalogCridAdapter);
        mCategoryList.setOnItemClickListener(categoryClicklistener);
        mContentCrid.setOnItemClickListener(catelogClicklistener);
        mPullToRefreshView = (PullToRefreshView) mView.findViewById(R.id.main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
        mScanSearchViewLayout.mSetOnSearchListener(mOnSearchActionListener);
        mScanSearchViewLayout.setOnScanlistener(doScanlistener);
        return mView;
    }

    public AdapterView.OnItemClickListener categoryClicklistener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMainPresenter.setCatagory((Category) parent.getAdapter().getItem(position));
            mMainPresenter.doSearchCatelog((Category) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemClickListener catelogClicklistener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showDetial(position);
        }
    };

    public void showDetial(int position) {
        mMainPresenter.mMainActivity.mCatalogDetialFragment.showDetial(mMainPresenter.mMainActivity.catalogCridAdapter.mCatalogGridItems,position,MainActivity.PAGE_CATALOGE);
        mMainPresenter.setContent(MainActivity.PAGE_CATALOGE_DETIAL);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.onFooterRefreshComplete();
        onFoot();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.onHeaderRefreshComplete();
        onHead();
    }

    public void onHead() {
        mMainPresenter.mMainActivity.waitDialog.show();
        ProducterManager.getInstance().isall = false;
        ProducterManager.getInstance().nowPage = 1;
        ProducterManager.getInstance().catalogs.clear();
        ProductAsks.getCatalog(mMainPresenter.mMainHandler,mMainPresenter.mMainActivity,
                mScanSearchViewLayout.getText(), ProducterManager.getInstance().setCategory.id,ProducterManager.getInstance().nowPage);
        mPullToRefreshView.onHeaderRefreshComplete();
    }

    public void onFoot() {
        if(ProducterManager.getInstance().isall == false)
        {
            mMainPresenter.mMainActivity.waitDialog.show();
            ProductAsks.getCatalog(mMainPresenter.mMainHandler,mMainPresenter.mMainActivity,
                    mScanSearchViewLayout.getText(), ProducterManager.getInstance().setCategory.id,ProducterManager.getInstance().nowPage);
            mPullToRefreshView.onFooterRefreshComplete();
        }
        else
        {
            AppUtils.showMessage(mMainPresenter.mMainActivity,mMainPresenter.mMainActivity.getString(R.string.product_all));
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
                mMainPresenter.doSearch(mScanSearchViewLayout.getText());

            }
            return true;
        }
    };

    public View.OnClickListener doScanlistener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            doScan();
        }
    };

    public void doScan()
    {
        ScanUtils.getInstance().startScanPad(mMainPresenter.mMainActivity,ProducterManager.ACTION_SEARCH_SCAN);
    }
}
