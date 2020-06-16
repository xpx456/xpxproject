package com.interskypad.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.interskypad.R;
import com.interskypad.entity.Catalog;
import com.interskypad.manager.OrderManager;
import com.interskypad.manager.ProducterManager;
import com.interskypad.presenter.MainPresenter;
import com.interskypad.view.activity.BigImageActivity;
import com.interskypad.view.activity.MainActivity;
import com.interskypad.view.adapter.ProductPageAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseFragment;

@SuppressLint("ValidFragment")
public class CatalogDetialFragment extends BaseFragment {

    public MainPresenter mMainPresenter;
    public ProductPageAdapter productPageAdapter;
    public ViewPager viewPager;
    public ImageView backPage;
    public ImageView fowardPage;
    public TextView buy;
    public TextView showPageDetial;
    public RelativeLayout doBack;
    public int id = 0;
    public int backid = MainActivity.PAGE_CATALOGE;
    public ArrayList<Catalog> catalogs = new ArrayList<Catalog>();

    public CatalogDetialFragment(MainPresenter mMainPresenter) {
        // Required empty public constructor
        this.mMainPresenter = mMainPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_catalog_detial, container, false);
        viewPager = mView.findViewById(R.id.catalog_pager);
        doBack = mView.findViewById(R.id.detial_back_layer);
        backPage = mView.findViewById(R.id.detial_img_bac_btn);
        fowardPage = mView.findViewById(R.id.detial_img_for_btn);
        buy = mView.findViewById(R.id.detail_buy);
        showPageDetial = mView.findViewById(R.id.detial_show);
        doBack.setOnClickListener(backListener);
        backPage.setOnClickListener(dobackPageListener);
        fowardPage.setOnClickListener(doforwardkPageListener);
        buy.setOnClickListener(addListener);
        showPageDetial.setOnClickListener(showDetialListener);
        if(catalogs.size() > 0)
        {
            if(OrderManager.getInstance().isInCart(catalogs.get(id)))
            {
                buy.setText(mMainPresenter.mMainActivity.getString(R.string.xml_quote_removeto_order));
            } else {
                buy.setText(mMainPresenter.mMainActivity.getString(R.string.xml_quote_addto_order));
            }
        }
        viewPager.setAdapter(productPageAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setCurrentItem(id);
        return mView;
    }

    public void showDetial(ArrayList<Catalog> catalogs, int id, int backid) {
        this.backid = backid;
        this.catalogs.clear();
        this.catalogs.addAll(catalogs);
        ArrayList<View> views = new ArrayList<View>();
        if (catalogs.size() > 5) {
            for (int i = 0; i < 5; i++) {
                View view = mMainPresenter.mMainActivity.getLayoutInflater().inflate(R.layout.detial_page, null);
                views.add(view);
            }
        } else {
            for (int i = 0; i < catalogs.size(); i++) {
                View view = mMainPresenter.mMainActivity.getLayoutInflater().inflate(R.layout.detial_page, null);
                views.add(view);
            }

        }
        productPageAdapter = new ProductPageAdapter(mMainPresenter.mMainActivity, views, ProducterManager.getInstance().catalogs, showBigImageListener);
        this.id = id;


        if (viewPager != null) {

            if( OrderManager.getInstance().isInCart(catalogs.get(id)))
            {
                buy.setText(mMainPresenter.mMainActivity.getString(R.string.xml_quote_removeto_order));
            } else {
                buy.setText(mMainPresenter.mMainActivity.getString(R.string.xml_quote_addto_order));
            }

            viewPager.setAdapter(productPageAdapter);
            viewPager.addOnPageChangeListener(onPageChangeListener);
            viewPager.setCurrentItem(id);

            if(backid == MainActivity.PAGE_QUTE_DETIAL)
            {
                buy.setVisibility(View.INVISIBLE);
            }
            else
            {
                buy.setVisibility(View.VISIBLE);
            }
        }


    }

    public View.OnClickListener dobackPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (viewPager.getCurrentItem() > 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                updateBuy();
            }

        }
    };

    public View.OnClickListener doforwardkPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (viewPager.getCurrentItem() < productPageAdapter.getCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                updateBuy();
            }

        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainPresenter.setContent(backid);
        }
    };

    public View.OnClickListener showBigImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showBigImage();
        }
    };

    public ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            updateBuy();
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void updateBuy() {
        Catalog catalog = productPageAdapter.catalogs.get(viewPager.getCurrentItem());
        if (OrderManager.getInstance().isInCart(catalog)) {
            buy.setText(mMainPresenter.mMainActivity.getString(R.string.xml_quote_removeto_order));
        } else {
            buy.setText(mMainPresenter.mMainActivity.getString(R.string.xml_quote_addto_order));
        }
    }

    public View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrderManager.getInstance().addCatalog(productPageAdapter.catalogs.get(viewPager.getCurrentItem()));
            updateBuy();
        }
    };

    public View.OnClickListener showDetialListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(productPageAdapter.showdetial)
            {
                productPageAdapter.showdetial = false;
                productPageAdapter.setShowdetial(false);
                showPageDetial.setText("▲ 参数");
            }
            else
            {
                productPageAdapter.showdetial = true;
                productPageAdapter.setShowdetial(true);
                showPageDetial.setText("▼ 参数");
            }
        }
    };

    public void showBigImage() {
        Catalog catalog = productPageAdapter.catalogs.get(viewPager.getCurrentItem());
        Intent intent = new Intent(mMainPresenter.mMainActivity, BigImageActivity.class);
        intent.putExtra("catalog", catalog);
        mMainPresenter.mMainActivity.startActivity(intent);
    }
}
