package com.interskypad.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.interskypad.manager.OrderManager;
import com.interskypad.presenter.MainPresenter;
import com.interskypad.view.adapter.CatalogCategoryAdapter;
import com.interskypad.view.adapter.CatalogCridAdapter;
import com.interskypad.view.fragment.CatalogDetialFragment;
import com.interskypad.view.fragment.CatalogFragment;
import com.interskypad.view.fragment.CustomerFragment;
import com.interskypad.view.fragment.HomeFragment;
import com.interskypad.view.fragment.QuoteDetialFragment;
import com.interskypad.view.fragment.QuoteFragment;

import java.util.ArrayList;
import java.util.List;

import intersky.appbase.PadFragmentBaseActivity;

public class MainActivity extends PadFragmentBaseActivity {
    public static final int PAGE_HOME = 0;
    public static final int PAGE_CATALOGE = 1;
    public static final int PAGE_CUSTOMER = 2;
    public static final int PAGE_QUOTE = 3;
    public static final int PAGE_CATALOGE_DETIAL = 4;
    public static final int PAGE_QUTE_DETIAL = 5;
    public ImageView mBtnHome;
    public ImageView mBtnCatalog;
    public ImageView mBtnCustomer;
    public ImageView mBtnQute;
    public ImageView mBtnSetting;
    public TextView mQuoteCount;
    public HomeFragment mHomeFragment;
    public CatalogFragment mCatalogFragment;
    public CustomerFragment mCustomerFragment;
    public QuoteFragment mQuoteFragment;
    public CatalogDetialFragment mCatalogDetialFragment;
    public QuoteDetialFragment mQuoteDetialFragment;
    public CatalogCategoryAdapter catalogCategoryAdapter;
    public CatalogCridAdapter catalogCridAdapter;
    public List<Fragment> mFragments = new ArrayList<Fragment>();
    public int nowPage = PAGE_HOME;
    public PopupWindow popupWindow1;
    public MainPresenter mMainPresenter = new MainPresenter(this);
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mHomeListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMainPresenter.setContent(PAGE_HOME);

        }
    };

    public View.OnClickListener mCatalogListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMainPresenter.setContent(PAGE_CATALOGE);
        }
    };

    public View.OnClickListener mCustomerListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub]
            mMainPresenter.setContent(PAGE_CUSTOMER);
        }
    };

    public View.OnClickListener mQuteListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if(OrderManager.getInstance().selectOrder == null) {
                mMainPresenter.setContent(PAGE_QUOTE);
            }
            else {
                mMainPresenter.setContent(PAGE_QUTE_DETIAL);
            }
        }
    };

    public View.OnClickListener mSettingListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMainPresenter.doSetting();
        }
    };

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

}
