package com.interskypad.manager;

import android.content.Intent;

import com.interskypad.database.OrderDbHelper;
import com.interskypad.entity.Catalog;
import com.interskypad.entity.Order;
import com.interskypad.view.InterskyPadApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderManager {

    public static final String ACTION_UPDATE_CATALOG_COUNT = "ACTION_UPDATE_CATALOG_COUNT";
    public ArrayList<Order> mAllOrders = new ArrayList<Order>();
    public ArrayList<Order> mSubmitOrders = new ArrayList<Order>();
    public ArrayList<Order> mUnSubmitOrders = new ArrayList<Order>();
    public ArrayList<Order> mSAllOrders = new ArrayList<Order>();
    public ArrayList<Order> mSSubmitOrders = new ArrayList<Order>();
    public ArrayList<Order> mSUnSubmitOrders = new ArrayList<Order>();
    public HashMap<String,Order> orderHashMap = new HashMap<String,Order>();
    public Order selectOrder;
    public static OrderManager mOrderManmager;

    public static synchronized OrderManager getInstance() {
        if (mOrderManmager == null) {
            mOrderManmager = new OrderManager();
        }
        return mOrderManmager;
    }

    public OrderManager() {
        mAllOrders.addAll(OrderDbHelper.getInstance(InterskyPadApplication.mApp).sacnOrder());
        for(int i = 0 ; i < mAllOrders.size() ; i++)
        {
            orderHashMap.put(mAllOrders.get(i).id,mAllOrders.get(i));
            if(mAllOrders.get(i).issubmit)
            {
                mSubmitOrders.add(mAllOrders.get(i));
            }
            else
            {
                mUnSubmitOrders.add(mAllOrders.get(i));
            }
        }
    }

    public void addCatalog(Catalog catalog) {
        if(selectOrder == null)
        {
            selectOrder = new Order();
            selectOrder.put(catalog);
            selectOrder.isedit = true;
        }
        else
        {
            selectOrder.put(catalog);
        }
        InterskyPadApplication.mApp.sendBroadcast(new Intent(ACTION_UPDATE_CATALOG_COUNT));
    }

    public void deleteCatalog(Catalog catalog) {

        selectOrder.remove(catalog.mSerialID);
        if(selectOrder.count == 0)
        {
            selectOrder = null;
        }
        InterskyPadApplication.mApp.sendBroadcast(new Intent(ACTION_UPDATE_CATALOG_COUNT));
    }

    public boolean isInCart(Catalog catalog) {
        if(selectOrder == null)
        {
            return false;
        }
        else
        {
            if(selectOrder.hashcatalogs.containsKey(catalog.mSerialID))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
