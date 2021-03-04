package com.interskypad.manager;

import com.interskypad.entity.Catalog;
import com.interskypad.entity.Category;
import com.interskypad.view.InterskyPadApplication;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.xpxnet.net.NetUtils;

public class ProducterManager {

    public static final String ACTION_SEARCH_SCAN = "ACTION_SEARCH_SCAN";

    public ArrayList<Category> categories = new ArrayList<Category>();
    public HashMap<String,Catalog> hashCatalogs = new HashMap<String,Catalog>();
    public ArrayList<Catalog> catalogs = new ArrayList<Catalog>();
    public Category setCategory = new Category();
    public static ProducterManager mProducterManager;
    public int nowPage = 1;
    public boolean isall = false;
    public static synchronized ProducterManager getInstance() {
        if (mProducterManager == null) {
            mProducterManager = new ProducterManager();
        }
        return mProducterManager;
    }

    public ProducterManager() {


    }

    public String getProductPhotoUrl(String photo) {
        return  NetUtils.getInstance().praseUrl(InterskyPadApplication.mApp.mService,"/image/"+ InterskyPadApplication.mApp.mAccount.mUCid +photo,"");
    }
}
