package com.interskypad.prase;

import android.content.Context;

import com.interskypad.entity.Catalog;
import com.interskypad.entity.Category;
import com.interskypad.manager.ProducterManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class ProductPrase {



    public static void measureCategoryLeaves(ArrayList<Category> categories,Category category) {

        while (categories.size() > 0)
        {
            Category temp = categories.get(0);
            if(temp.level == -1)
            {
                if(category.parendId.length() == 0)
                {
                    temp.level = 1;
                    categories.remove(temp);
                    measureCategoryLeaves(categories,temp);
                }
                else
                {
                    if(category.id.equals(temp.parendId)) {
                        temp.level = category.level+1;
                        categories.remove(temp);
                        measureCategoryLeaves(categories,temp);
                    }
                }
            }
            else
            {
                categories.remove(temp);
            }
        }
    }


    public static  void measureNetCategory(ArrayList<Category> categories) {
        while (categories.size() > 0)
        {
            Category temp = categories.get(0);
            if(temp.parendId.length() == 0)
            {
                temp.level = 0;
                categories.remove(temp);
                recNetCategory(categories,temp);
            }
            else
            {

            }

        }

    }

    public static void recNetCategory(ArrayList<Category> categories,Category category) {

    }

    public static void measureCategory2(HashMap<String ,ArrayList<Category>> chash,HashMap<String ,Category> hash,Category category) {

        if(category.parendId.length() == 0)
        {
            hash.put("(Root)",category);
            category.level = 0;
            ArrayList<Category> categories = chash.get(category.id);
            if(categories != null)
            {
                category.childs.addAll(categories);
            }
            chash.put(category.id,category.childs);
        }
        else
        {

            ArrayList<Category> categories1 = chash.get(category.parendId);
            if(categories1 == null) {
                categories1 = new ArrayList<Category>();
                hash.put(category.parendId,category);
            }
            categories1.add(category);

            ArrayList<Category> categories = chash.get(category.id);
            if(categories != null)
            {
                category.childs.addAll(categories);

            }
            chash.put(category.id,category.childs);

        }

    }

    public static void measureCategory(HashMap<String ,ArrayList<Category>> chash,HashMap<String ,Category> hash,Category category) {

        if(category.parendId.length() == 0)
        {
            hash.put("(Root)",category);
            category.level = 0;
            ArrayList<Category> categories = chash.get(category.id);
            if(categories != null)
            {
                category.childs.addAll(categories);
                measureChileLeave(category);
            }
            chash.put(category.id,category.childs);
        }
        else
        {
            ArrayList<Category> categories1 = chash.get(category.parendId);
            if(categories1 == null) {
                categories1 = new ArrayList<Category>();
                hash.put(category.parendId,category);
            }
            categories1.add(category);

            ArrayList<Category> categories = chash.get(category.id);
            if(categories != null)
            {
                category.childs.addAll(categories);
                measureChileLeave(category);
            }
            chash.put(category.id,category.childs);

        }

    }

    public static void measureChileLeave(Category category)
    {
        for(int i = 0 ; i < category.childs.size() ;i++)
        {
            category.childs.get(i).level = category.level+1;
            measureChileLeave(category);
        }
    }

    public static void praseCategory(NetObject net, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        try {
            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("Data");
            Category bcategory = new Category();
            bcategory.id = "(Root)";
            bcategory.level = 0;
            bcategory.contentText = "全部";
            HashMap<String ,ArrayList<Category>> chash = new  HashMap<String ,ArrayList<Category>>();
            chash.put("(Root)",bcategory.childs);
            HashMap<String ,Category> hash = new  HashMap<String ,Category>();
            hash.put("(Root)",bcategory);
            for(int i = 0 ; i < ja.length() ; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                Category category = new Category();
                category.id = jo.getString("RecordID");
                category.parendId = jo.getString("ParentID");
                category.contentText = jo.getString("Name");
                measureCategory(chash,hash,category);
            }
            ProducterManager.getInstance().categories.clear();
            Category category = hash.get("(Root)");
            ProducterManager.getInstance().categories.add(hash.get("(Root)"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseCatalog(NetObject net, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        try {
            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("Data");
            for(int i = 0 ; i < ja.length() ; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                Catalog catalog = new Catalog();
                catalog.mSerialID = jo.getString("SerialID");
                catalog.mENGItemName = jo.getString("ItemNo");
                catalog.mBarcode = jo.getString("Barcode");
                catalog.mCatalogueID = jo.getString("CatalogueID");
                catalog.mENGItemName = jo.getString("ENGItemName");
                catalog.mENGSpecification = jo.getString("ENGSpecification");
                catalog.mSalesPrice = jo.getString("SalesPrice");
                catalog.mOuterVolume = jo.getString("OuterVolume");
                catalog.mOuterCapacity = jo.getString("OuterCapacity");
                catalog.mPacking = jo.getString("Packing");
                catalog.mUnit = jo.getString("Unit");
                catalog.mENGMemo = jo.getString("ENGMemo");
                catalog.mPhoto = jo.getString("Photo");
                catalog.mSupplierShortName = jo.getString("SupplierShortName");
                catalog.mSupplierItemNo = jo.getString("SupplierItemNo");
                catalog.mCanBill = jo.getString("CanBill");
                catalog.mRebate = jo.getString("Rebate");
                catalog.mPurchasePrice = jo.getString("PurchasePrice");
                catalog.mMinimumQty = jo.getString("MinimumQty");
                catalog.mOuterGrossWeight = jo.getString("OuterGrossWeight");
                catalog.mOuterNetWeight = jo.getString("OuterNetWeight");
                ProducterManager.getInstance().catalogs.add(catalog);
                ProducterManager.getInstance().hashCatalogs.put(catalog.mSerialID,catalog);
            }
            if(ja.length() == 20)
            {
                ProducterManager.getInstance().nowPage++;
            }
            else
            {
                ProducterManager.getInstance().isall = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseCategory2(String  result,ArrayList<Category> categories) {
        String json = result;
        try {

            if(AppUtils.success(json) == false)
            {
                return false;
            }

            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("Data");
            HashMap<String ,ArrayList<Category>> chash = new  HashMap<String ,ArrayList<Category>>();
            HashMap<String ,Category> hash = new  HashMap<String ,Category>();
            for(int i = 0 ; i < ja.length() ; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                Category category = new Category();
                category.id = jo.getString("RecordID");
                category.parendId = jo.getString("ParentID");
                category.contentText = jo.getString("Name");
                measureCategory(chash,hash,category);
                categories.add(category);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean praseCatalog2(String  result,ArrayList<Catalog> catalogs) {
        String json = result;
        try {

            if(AppUtils.success(json) == false)
            {
                return true;
            }

            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("Data");
            for(int i = 0 ; i < ja.length() ; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                Catalog catalog = new Catalog();
                catalog.mSerialID = jo.getString("SerialID");
                catalog.mENGItemName = jo.getString("ItemNo");
                catalog.mBarcode = jo.getString("Barcode");
                catalog.mCatalogueID = jo.getString("CatalogueID");
                catalog.mENGItemName = jo.getString("ENGItemName");
                catalog.mENGSpecification = jo.getString("ENGSpecification");
                catalog.mSalesPrice = jo.getString("SalesPrice");
                catalog.mOuterVolume = jo.getString("OuterVolume");
                catalog.mOuterCapacity = jo.getString("OuterCapacity");
                catalog.mPacking = jo.getString("Packing");
                catalog.mUnit = jo.getString("Unit");
                catalog.mENGMemo = jo.getString("ENGMemo");
                catalog.mPhoto = jo.getString("Photo");
                catalog.mSupplierShortName = jo.getString("SupplierShortName");
                catalog.mSupplierItemNo = jo.getString("SupplierItemNo");
                catalog.mCanBill = jo.getString("CanBill");
                catalog.mRebate = jo.getString("Rebate");
                catalog.mPurchasePrice = jo.getString("PurchasePrice");
                catalog.mMinimumQty = jo.getString("MinimumQty");
                catalog.mOuterGrossWeight = jo.getString("OuterGrossWeight");
                catalog.mOuterNetWeight = jo.getString("OuterNetWeight");
                catalogs.add(catalog);
            }
            if(ja.length() == 80)
            {
                return false;
            }
            else
            {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }
}
