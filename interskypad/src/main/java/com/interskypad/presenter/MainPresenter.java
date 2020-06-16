package com.interskypad.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.interskypad.R;
import com.interskypad.asks.ProductAsks;
import com.interskypad.entity.Category;
import com.interskypad.handler.MainHandler;
import com.interskypad.manager.OrderManager;
import com.interskypad.manager.ProducterManager;
import com.interskypad.receiver.MainReceiver;
import com.interskypad.view.InterskyPadApplication;
import com.interskypad.view.activity.MainActivity;
import com.interskypad.view.activity.SettingActivity;
import com.interskypad.view.adapter.CatalogCategoryAdapter;
import com.interskypad.view.adapter.CatalogCridAdapter;
import com.interskypad.view.fragment.CatalogDetialFragment;
import com.interskypad.view.fragment.CatalogFragment;
import com.interskypad.view.fragment.CustomerFragment;
import com.interskypad.view.fragment.HomeFragment;
import com.interskypad.view.fragment.QuoteDetialFragment;
import com.interskypad.view.fragment.QuoteFragment;

import intersky.appbase.PadFragmentTabAdapter;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mMainHandler;

    public MainPresenter(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
        this.mMainHandler = new MainHandler(mMainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mMainHandler));
    }

    @Override
    public void initView() {
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.mBtnHome = (ImageView) mMainActivity.findViewById(R.id.mian_buttom_home);
        mMainActivity.mBtnCatalog = (ImageView) mMainActivity.findViewById(R.id.mian_buttom_catalog);
        mMainActivity.mBtnCustomer = (ImageView) mMainActivity.findViewById(R.id.mian_buttom_customer);
        mMainActivity.mBtnQute = (ImageView) mMainActivity.findViewById(R.id.mian_buttom_quote);
        mMainActivity.mBtnSetting = (ImageView) mMainActivity.findViewById(R.id.mian_buttom_setting);
        mMainActivity.mQuoteCount = (TextView) mMainActivity.findViewById(R.id.mian_quote_number);
        ProducterManager.getInstance().categories.clear();
        ProducterManager.getInstance().catalogs.clear();
        mMainActivity.catalogCategoryAdapter = new CatalogCategoryAdapter(mMainActivity,ProducterManager.getInstance().categories,mMainHandler);
        mMainActivity.catalogCridAdapter =  new CatalogCridAdapter(mMainActivity,ProducterManager.getInstance().catalogs,mMainHandler);
        ProductAsks.getCateGory(mMainHandler,mMainActivity);
        ProductAsks.getCatalog(mMainHandler,mMainActivity,"","",ProducterManager.getInstance().nowPage);
        mMainActivity.mHomeFragment = new HomeFragment(this);
        mMainActivity.mCatalogFragment = new CatalogFragment(this);
        mMainActivity.mCustomerFragment = new CustomerFragment(this);
        mMainActivity.mQuoteFragment = new QuoteFragment(this);
        mMainActivity.mCatalogDetialFragment = new CatalogDetialFragment(this);
        mMainActivity.mQuoteDetialFragment = new QuoteDetialFragment(this);
        mMainActivity.mFragments.add(mMainActivity.mHomeFragment);
        mMainActivity.mFragments.add(mMainActivity.mCatalogFragment);
        mMainActivity.mFragments.add(mMainActivity.mCustomerFragment);
        mMainActivity.mFragments.add(mMainActivity.mQuoteFragment);
        mMainActivity.mFragments.add(mMainActivity.mCatalogDetialFragment);
        mMainActivity.mFragments.add(mMainActivity.mQuoteDetialFragment);
        mMainActivity.tabAdapter = new PadFragmentTabAdapter(mMainActivity, mMainActivity.mFragments, R.id.tab_content);
        setContent(MainActivity.PAGE_HOME);
        mMainActivity.mBtnHome.setOnClickListener(mMainActivity.mHomeListener);
        mMainActivity.mBtnCatalog.setOnClickListener(mMainActivity.mCatalogListener);
        mMainActivity.mBtnCustomer.setOnClickListener(mMainActivity.mCustomerListener);
        mMainActivity.mBtnQute.setOnClickListener(mMainActivity.mQuteListener);
        mMainActivity.mBtnSetting.setOnClickListener(mMainActivity.mSettingListener);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }


    public void setContent(int id) {
        mMainActivity.nowPage = id;
        mMainActivity.tabAdapter.onCheckedChanged(id);
        switch (mMainActivity.nowPage) {
            case MainActivity.PAGE_HOME:
                mMainActivity.mBtnHome.setImageResource(R.drawable.btnhomes);
                mMainActivity.mBtnCatalog.setImageResource(R.drawable.btncatalog);
                mMainActivity.mBtnCustomer.setImageResource(R.drawable.btncustomer);
                mMainActivity.mBtnQute.setImageResource(R.drawable.btnquote);
                break;
            case MainActivity.PAGE_CATALOGE:
                mMainActivity.mBtnHome.setImageResource(R.drawable.btnhome);
                mMainActivity.mBtnCatalog.setImageResource(R.drawable.btncatalogs);
                mMainActivity.mBtnCustomer.setImageResource(R.drawable.btncustomer);
                mMainActivity. mBtnQute.setImageResource(R.drawable.btnquote);
                break;
            case MainActivity.PAGE_CUSTOMER:
                mMainActivity.mBtnHome.setImageResource(R.drawable.btnhome);
                mMainActivity.mBtnCatalog.setImageResource(R.drawable.btncatalog);
                mMainActivity.mBtnCustomer.setImageResource(R.drawable.btncustomers);
                mMainActivity.mBtnQute.setImageResource(R.drawable.btnquote);
                break;
            case MainActivity.PAGE_QUOTE:
                mMainActivity.mBtnHome.setImageResource(R.drawable.btnhome);
                mMainActivity.mBtnCatalog.setImageResource(R.drawable.btncatalog);
                mMainActivity.mBtnCustomer.setImageResource(R.drawable.btncustomer);
                mMainActivity.mBtnQute.setImageResource(R.drawable.btnquotes);
                break;
            case MainActivity.PAGE_CATALOGE_DETIAL:
                break;
            case MainActivity.PAGE_QUTE_DETIAL:
                mMainActivity.mBtnHome.setImageResource(R.drawable.btnhome);
                mMainActivity.mBtnCatalog.setImageResource(R.drawable.btncatalog);
                mMainActivity.mBtnCustomer.setImageResource(R.drawable.btncustomer);
                mMainActivity.mBtnQute.setImageResource(R.drawable.btnquotes);
                mMainActivity.mQuoteDetialFragment.updataView();
                break;
        }

    }

    public void setCatagory(Category category) {
        ProducterManager.getInstance().setCategory.isSelected = false;
        ProducterManager.getInstance().setCategory = category;
        ProducterManager.getInstance().setCategory.isSelected = true;
        mMainHandler.sendEmptyMessage(CatalogCategoryAdapter.UPDATA_LIST);
    }

    public void doSearchCatelog(Category category) {
        mMainActivity.waitDialog.show();
        ProducterManager.getInstance().catalogs.clear();
        ProducterManager.getInstance().nowPage = 1;
        ProducterManager.getInstance().isall = false;
        if(category.id.length() == 0)
        {
            ProductAsks.getCatalog(mMainHandler,mMainActivity,mMainActivity.mCatalogFragment.mScanSearchViewLayout.getText(),"",mMainActivity.nowPage);
        }
        else
        {
            String id = category.id;
            if(InterskyPadApplication.mApp.isLogin)
            {
                if(id.toLowerCase().contains("root"))
                {
                    id = "";
                }
            }
            else
            {
                for(int i = 0 ; i < category.childs.size() ; i++)
                {
                    id += getchildid(category.childs.get(i));
                }
            }
            ProductAsks.getCatalog(mMainHandler,mMainActivity,mMainActivity.mCatalogFragment.mScanSearchViewLayout.getText(),id,mMainActivity.nowPage);
        }
    }

    public void doSearch(String keyword) {

        ProducterManager.getInstance().catalogs.clear();
        ProducterManager.getInstance().nowPage = 1;
        ProducterManager.getInstance().isall = false;

        String id = ProducterManager.getInstance().setCategory.id;
        if(InterskyPadApplication.mApp.isLogin)
        {

        }
        else
        {
            for(int i = 0 ; i < ProducterManager.getInstance().setCategory.childs.size() ; i++)
            {
                id += getchildid(ProducterManager.getInstance().setCategory.childs.get(i));
            }
        }
        ProductAsks.getCatalog(mMainHandler,mMainActivity,keyword,id,mMainActivity.nowPage);
    }

    public String getchildid(Category category) {
        String id = "";
        id = ","+category.id;
        for(int i = 0 ; i < category.childs.size() ; i++)
        {
            id += getchildid(category.childs.get(i));
        }
        return id;
    }

    public void updateOrderHit() {
        if(OrderManager.getInstance().selectOrder == null)
        {
            mMainActivity.mQuoteCount.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(OrderManager.getInstance().selectOrder.isedit)
            {
                mMainActivity.mQuoteCount.setVisibility(View.VISIBLE);
                AppUtils.setHit(OrderManager.getInstance().selectOrder.count,mMainActivity.mQuoteCount);

            }
            else
            {
                mMainActivity.mQuoteCount.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void doSetting() {
        Intent intent = new Intent(mMainActivity, SettingActivity.class);
        mMainActivity.startActivity(intent);
    }
}

