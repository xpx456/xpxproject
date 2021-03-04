package com.interskypad.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.interskypad.asks.LoginAsks;
import com.interskypad.asks.OrderAsks;
import com.interskypad.asks.ProductAsks;
import com.interskypad.entity.Catalog;
import com.interskypad.entity.Category;
import com.interskypad.entity.Order;
import com.interskypad.manager.ProducterManager;
import com.interskypad.prase.ProductPrase;
import com.interskypad.view.activity.LoginActivity;
import com.interskypad.view.activity.MainActivity;
import com.interskypad.view.adapter.CatalogCategoryAdapter;

import java.lang.ref.WeakReference;
import java.util.Collection;

import intersky.xpxnet.net.NetObject;

public class MainHandler extends Handler {

    public static final int UPDATE_ORDER_HIT = 3030000;
    public static final int UPDATE_CUSTOMER_LIST = 3030001;
    public static final int DO_SEARCH_SCAN = 3030002;
    public MainActivity theActivity;

    public MainHandler(MainActivity mMainActivity) {
        theActivity = mMainActivity;
    }


    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ProductAsks.EVENT_GET_CATEGORY_LOCAL_SUCCESS:
                theActivity.waitDialog.hide();
                ProducterManager.getInstance().categories.addAll((Collection<? extends Category>) msg.obj);
                ProducterManager.getInstance().categories.get(0).isSelected = true;
                ProducterManager.getInstance().setCategory =  ProducterManager.getInstance().categories.get(0);
                theActivity.catalogCategoryAdapter.notifyDataSetChanged();
                break;
            case ProductAsks.EVENT_GET_CATEGORY_SUCCESS:
                theActivity.waitDialog.hide();
                ProductPrase.praseCategory((NetObject) msg.obj,theActivity);
                theActivity.catalogCategoryAdapter.notifyDataSetChanged();
                break;
            case ProductAsks.EVENT_GET_CATALOG_LOCAL_SUCCESS:
                theActivity.waitDialog.hide();
                ProducterManager.getInstance().catalogs.addAll((Collection<? extends Catalog>) msg.obj);
                for(int i = 0 ; i < ProducterManager.getInstance().catalogs.size() ; i++)
                {
                    ProducterManager.getInstance().hashCatalogs.put(ProducterManager.getInstance().catalogs.get(i).mSerialID,ProducterManager.getInstance().catalogs.get(i));
                }
                ProducterManager.getInstance().isall = true;
                theActivity.catalogCridAdapter.notifyDataSetChanged();
                break;
            case ProductAsks.EVENT_GET_CATALOG_SUCCESS:
                theActivity.waitDialog.hide();
                ProductPrase.praseCatalog((NetObject) msg.obj,theActivity);
                theActivity.catalogCridAdapter.notifyDataSetChanged();
                break;
            case OrderAsks.EVENT_SUBMIT_SUCCESS:
                NetObject netObject = (NetObject) msg.obj;
                theActivity.waitDialog.hide();
                theActivity.mQuoteDetialFragment.submitSuccess((Order) netObject.item);
                theActivity.mQuoteFragment.updataView();

                break;
            case OrderAsks.EVENT_DELETE_SUCCESS:
                theActivity.waitDialog.hide();
                break;
            case UPDATE_CUSTOMER_LIST:
                theActivity.waitDialog.hide();
                theActivity.mCustomerFragment.updataView();
                break;
            case CatalogCategoryAdapter.UPDATA_LIST:
                theActivity.catalogCategoryAdapter.notifyDataSetChanged();
                break;
            case UPDATE_ORDER_HIT:
                theActivity.mMainPresenter.updateOrderHit();
                if(theActivity.catalogCridAdapter != null)
                {
                    theActivity.catalogCridAdapter.notifyDataSetChanged();
                }
                break;
            case DO_SEARCH_SCAN:
                Intent intent1 = (Intent) msg.obj;
                theActivity.mCatalogFragment.mScanSearchViewLayout.setText(intent1.getStringExtra("result"));
                theActivity.mMainPresenter.doSearch(intent1.getStringExtra("result"));
                break;
            case LoginAsks.LOGOUT_SUCCESS:
                Intent mainIntent = new Intent();
                mainIntent.setClass(theActivity, LoginActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                theActivity.startActivity(mainIntent);
                theActivity.finish();
                break;
        }
    }
}
