package com.interskypad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.interskypad.R;
import com.interskypad.entity.Catalog;
import com.interskypad.entity.Category;
import com.interskypad.prase.ProductPrase;
import com.interskypad.view.InterskyPadApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductDBHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "test";
    public static final int DB_VERSION = 2;


    private static final String TABLE_CATEGORY = "TABLE_CATEGORY";
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String CATEGORY_PARENTID = "CATEGORY_PARENTID";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private static final String CATEGORY_LEAVE = "CATEGORY_LEAVE";


    private static final String TABLE_CATELOG = "TABLE_CATELOG";
    private static final String CATELOG_LASTMODIFIED = "CATELOG_LASTMODIFIED";
    private static final String CATELOG_SERIALID = "CATELOG_SERIALID";
    private static final String CATELOG_ITEMNO = "CATELOG_ITEMNO";
    private static final String CATELOG_BARCODE = "CATELOG_BARCODE";
    private static final String CATELOG_CATALOGUEID = "CATELOG_CATALOGUEID";
    private static final String CATELOG_ENGITEMNAME = "CATELOG_ENGITEMNAME";
    private static final String CATELOG_ENGSPECIFICATION = "CATELOG_ENGSPECIFICATION";
    private static final String CATELOG_OUTERVOLUME = "CATELOG_OUTERVOLUME";
    private static final String CATELOG_OUTHERCAPACITY = "CATELOG_OUTHERCAPACITY";
    private static final String CATELOG_PACKING = "CATELOG_PACKING";
    private static final String CATELOG_UNIT = "CATELOG_UNIT";
    private static final String CATELOG_ENGMEMO = "CATELOG_ENGMEMO";
    private static final String CATELOG_PHOTO = "CATELOG_PHOTO";
    private static final String CATELOG_SUPPLIERSHORTNAME = "CATELOG_SUPPLIERSHORTNAME";
    private static final String CATELOG_SUPPLIERITEMNO = "CATELOG_mSUPPLIERITEMNO";
    private static final String CATELOG_CANBILL = "CATELOG_CANBILL";
    private static final String CATELOG_PURCHASEPRICE = "CATELOG_PURCHASEPRICE";
    private static final String CATELOG_MINIMUNQTY = "CATELOG_MINIMUNQTY";
    private static final String CATELOG_OUTHERGROSSWEIGHT = "CATELOG_OUTHERGROSSWEIGHT";
    private static final String CATELOG_OUTERNETWEIGHT = "CATELOG_OUTERNETWEIGHT";
    private static final String CATELOG_SALESPRICE = "CATELOG_SALESPRICE";
    private static final String CATELOG_REBATE = "CATELOG_REBATE";



    private static ProductDBHelper mDBHelper;
    private SQLiteDatabase db = null;

    public static void init(Context context,String dbname) {
        DB_NAME = dbname;
        mDBHelper = new ProductDBHelper(context);
    }

    public static ProductDBHelper getInstance(Context context) {
        if (null == mDBHelper) {

            mDBHelper = new ProductDBHelper(context);
        }
        return mDBHelper;
    }

    private ProductDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        openDB();
    }


    private void openDB() {
        if (null == db || !db.isOpen()) {
            db = this.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        String sql = "CREATE TABLE " + TABLE_CATEGORY + " (" + CATEGORY_ID
                + " TEXT PRIMARY KEY," + CATEGORY_NAME + " TEXT," + CATEGORY_PARENTID + " TEXT,"  + CATEGORY_LEAVE + " TEXT)";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATELOG);
        sql = "CREATE TABLE " + TABLE_CATELOG + " (" + CATELOG_SERIALID
                + " TEXT PRIMARY KEY," + CATELOG_LASTMODIFIED + " TEXT,"  + CATELOG_ITEMNO + " TEXT," + CATELOG_BARCODE + " TEXT,"
                + CATELOG_CATALOGUEID + " TEXT," + CATELOG_ENGITEMNAME + " TEXT," + CATELOG_ENGSPECIFICATION + " TEXT,"
                + CATELOG_OUTERVOLUME + " TEXT," + CATELOG_OUTHERCAPACITY + " TEXT," + CATELOG_PACKING + " TEXT,"
                + CATELOG_UNIT + " TEXT," + CATELOG_ENGMEMO + " TEXT," + CATELOG_PHOTO + " TEXT,"
                + CATELOG_SUPPLIERSHORTNAME + " TEXT," + CATELOG_SUPPLIERITEMNO + " TEXT," + CATELOG_CANBILL + " TEXT,"
                + CATELOG_PURCHASEPRICE + " TEXT," + CATELOG_MINIMUNQTY + " TEXT,"
                + CATELOG_OUTHERGROSSWEIGHT + " TEXT," + CATELOG_OUTERNETWEIGHT + " TEXT,"
                + CATELOG_SALESPRICE + " TEXT," + CATELOG_REBATE + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        String sql = "CREATE TABLE " + TABLE_CATEGORY + " (" + CATEGORY_ID
                + " TEXT PRIMARY KEY," + CATEGORY_NAME + " TEXT," + CATEGORY_PARENTID + " TEXT,"  + CATEGORY_LEAVE + " TEXT)";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATELOG);
        sql = "CREATE TABLE " + TABLE_CATELOG + " (" + CATELOG_SERIALID
                + " TEXT PRIMARY KEY," + CATELOG_LASTMODIFIED + " TEXT,"  + CATELOG_ITEMNO + " TEXT," + CATELOG_BARCODE + " TEXT,"
                + CATELOG_CATALOGUEID + " TEXT," + CATELOG_ENGITEMNAME + " TEXT," + CATELOG_ENGSPECIFICATION + " TEXT,"
                + CATELOG_OUTERVOLUME + " TEXT," + CATELOG_OUTHERCAPACITY + " TEXT," + CATELOG_PACKING + " TEXT,"
                + CATELOG_UNIT + " TEXT," + CATELOG_ENGMEMO + " TEXT," + CATELOG_PHOTO + " TEXT,"
                + CATELOG_SUPPLIERSHORTNAME + " TEXT," + CATELOG_SUPPLIERITEMNO + " TEXT," + CATELOG_CANBILL + " TEXT,"
                + CATELOG_PURCHASEPRICE + " TEXT," + CATELOG_MINIMUNQTY + " TEXT,"
                + CATELOG_OUTHERGROSSWEIGHT + " TEXT," + CATELOG_OUTERNETWEIGHT + " TEXT,"
                + CATELOG_SALESPRICE + " TEXT," + CATELOG_REBATE + " TEXT)";
        db.execSQL(sql);
    }


    public ArrayList<Category> scanCategory() {
        openDB();
        ArrayList<Category> categories = new ArrayList<Category>();
        HashMap<String,ArrayList<Category>> hashMap = new HashMap<String,ArrayList<Category>>();
        HashMap<String,Category> hashMap2 = new HashMap<String,Category>();
        String sql = "SELECT * FROM " + TABLE_CATEGORY;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        Category mCategory = new Category(InterskyPadApplication.mApp.getString(R.string.product_cat_all),0,"","(Root)");
        hashMap.put(mCategory.id,mCategory.childs);
        categories.add(mCategory);
        while (c.moveToNext()) {
            Category info = new Category();
            info.id = c.getString(c.getColumnIndex(CATEGORY_ID));
            info.parendId = c.getString(c.getColumnIndex(CATEGORY_PARENTID));
            info.contentText = c.getString(c.getColumnIndex(CATEGORY_NAME));
            info.level = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_LEAVE)));
            ProductPrase.measureCategory2(hashMap,hashMap2,info);
        }
        c.close();
        return categories;
    }


    public int addCategory(Category sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_ID, sInfo.id);
        cv.put(CATEGORY_PARENTID, sInfo.parendId);
        cv.put(CATEGORY_NAME, sInfo.contentText);
        cv.put(CATEGORY_LEAVE, String.valueOf(sInfo.level));
        int iRet = (int) db.insert(TABLE_CATEGORY, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_CATEGORY, cv, CATEGORY_ID + "=?", new String[]
                    {sInfo.id});
        }
        return iRet;
    }

    public void listAddCategory(ArrayList<Category> sInfos) {
        openDB();
        for(int i = 0 ; i < sInfos.size() ; i++)
        {
            Category sInfo = sInfos.get(i);
            ContentValues cv = new ContentValues();
            cv.put(CATEGORY_ID, sInfo.id);
            cv.put(CATEGORY_PARENTID, sInfo.parendId);
            cv.put(CATEGORY_NAME, sInfo.contentText);
            cv.put(CATEGORY_LEAVE, String.valueOf(sInfo.level));
            int iRet = (int) db.insert(TABLE_CATEGORY, null, cv);
            if (-1 == iRet) {
                iRet = db.update(TABLE_CATEGORY, cv, CATEGORY_ID + "=?", new String[]
                        {sInfo.id});
            }
        }
    }

    public int deleteCategory(Category sInfo) {
        return db.delete(TABLE_CATEGORY, CATEGORY_ID + "=?", new String[]
                {sInfo.id});
    }

    public int deleteServiceAllCategory() {
        return db.delete(TABLE_CATEGORY, null, new String[]
                {});
    }

    public List<Catalog> scanCatalog() {
        openDB();
        List<Catalog> categories = new ArrayList<Catalog>();

        String sql = "SELECT * FROM " + TABLE_CATELOG;
        Cursor c = db.rawQuery(sql, new String[]
                {});

        while (c.moveToNext()) {
            Catalog info = new Catalog();
            info.mLastModified = c.getString(c.getColumnIndex(CATELOG_LASTMODIFIED));
            info.mSerialID = c.getString(c.getColumnIndex(CATELOG_SERIALID));
            info.mItemNo = c.getString(c.getColumnIndex(CATELOG_ITEMNO));
            info.mBarcode = c.getString(c.getColumnIndex(CATELOG_BARCODE));
            info.mCatalogueID = c.getString(c.getColumnIndex(CATELOG_CATALOGUEID));
            info.mENGItemName = c.getString(c.getColumnIndex(CATELOG_ENGITEMNAME));
            info.mENGSpecification = c.getString(c.getColumnIndex(CATELOG_ENGSPECIFICATION));
            info.mOuterVolume = c.getString(c.getColumnIndex(CATELOG_OUTERVOLUME));
            info.mOuterCapacity = c.getString(c.getColumnIndex(CATELOG_OUTHERCAPACITY));
            info.mPacking = c.getString(c.getColumnIndex(CATELOG_PACKING));
            info.mUnit = c.getString(c.getColumnIndex(CATELOG_UNIT));
            info.mENGMemo = c.getString(c.getColumnIndex(CATELOG_ENGMEMO));
            info.mPhoto = c.getString(c.getColumnIndex(CATELOG_PHOTO));
            info.mSupplierShortName = c.getString(c.getColumnIndex(CATELOG_SUPPLIERSHORTNAME));
            info.mSupplierItemNo = c.getString(c.getColumnIndex(CATELOG_SUPPLIERITEMNO));
            info.mCanBill = c.getString(c.getColumnIndex(CATELOG_CANBILL));
            info.mPurchasePrice = c.getString(c.getColumnIndex(CATELOG_PURCHASEPRICE));
            info.mMinimumQty = c.getString(c.getColumnIndex(CATELOG_MINIMUNQTY));
            info.mOuterGrossWeight = c.getString(c.getColumnIndex(CATELOG_OUTHERGROSSWEIGHT));
            info.mOuterNetWeight = c.getString(c.getColumnIndex(CATELOG_OUTERNETWEIGHT));
            info.mSalesPrice = c.getString(c.getColumnIndex(CATELOG_SALESPRICE));
            info.mRebate = c.getString(c.getColumnIndex(CATELOG_REBATE));
            categories.add(info);
        }
        c.close();
        return categories;
    }

    public List<Catalog> scanCatalog(String keyword,String catalogid) {
        openDB();
        List<Catalog> categories = new ArrayList<Catalog>();
        String sql = "";
        Cursor c;

        String[] catalodids = catalogid.split(",");
        if(catalogid.length() == 0 && keyword.length() != 0)
        {
            sql = "SELECT * FROM " + TABLE_CATELOG+" WHERE "+ CATELOG_ITEMNO + " = ?" ;
            c = db.rawQuery(sql, new String[]
                    {keyword});
            while (c.moveToNext()) {
                Catalog info = new Catalog();
                info.mLastModified = c.getString(c.getColumnIndex(CATELOG_LASTMODIFIED));
                info.mSerialID = c.getString(c.getColumnIndex(CATELOG_SERIALID));
                info.mItemNo = c.getString(c.getColumnIndex(CATELOG_ITEMNO));
                info.mBarcode = c.getString(c.getColumnIndex(CATELOG_BARCODE));
                info.mCatalogueID = c.getString(c.getColumnIndex(CATELOG_CATALOGUEID));
                info.mENGItemName = c.getString(c.getColumnIndex(CATELOG_ENGITEMNAME));
                info.mENGSpecification = c.getString(c.getColumnIndex(CATELOG_ENGSPECIFICATION));
                info.mOuterVolume = c.getString(c.getColumnIndex(CATELOG_OUTERVOLUME));
                info.mOuterCapacity = c.getString(c.getColumnIndex(CATELOG_OUTHERCAPACITY));
                info.mPacking = c.getString(c.getColumnIndex(CATELOG_PACKING));
                info.mUnit = c.getString(c.getColumnIndex(CATELOG_UNIT));
                info.mENGMemo = c.getString(c.getColumnIndex(CATELOG_ENGMEMO));
                info.mPhoto = c.getString(c.getColumnIndex(CATELOG_PHOTO));
                info.mSupplierShortName = c.getString(c.getColumnIndex(CATELOG_SUPPLIERSHORTNAME));
                info.mSupplierItemNo = c.getString(c.getColumnIndex(CATELOG_SUPPLIERITEMNO));
                info.mCanBill = c.getString(c.getColumnIndex(CATELOG_CANBILL));
                info.mPurchasePrice = c.getString(c.getColumnIndex(CATELOG_PURCHASEPRICE));
                info.mMinimumQty = c.getString(c.getColumnIndex(CATELOG_MINIMUNQTY));
                info.mOuterGrossWeight = c.getString(c.getColumnIndex(CATELOG_OUTHERGROSSWEIGHT));
                info.mOuterNetWeight = c.getString(c.getColumnIndex(CATELOG_OUTERNETWEIGHT));
                info.mSalesPrice = c.getString(c.getColumnIndex(CATELOG_SALESPRICE));
                info.mRebate = c.getString(c.getColumnIndex(CATELOG_REBATE));
                categories.add(info);
            }
            c.close();
        }
        else if(keyword.length() == 0 && catalogid.length() == 0)
        {
            sql = "SELECT * FROM " + TABLE_CATELOG;
            c = db.rawQuery(sql, new String[]
                    {});
            while (c.moveToNext()) {
                Catalog info = new Catalog();
                info.mLastModified = c.getString(c.getColumnIndex(CATELOG_LASTMODIFIED));
                info.mSerialID = c.getString(c.getColumnIndex(CATELOG_SERIALID));
                info.mItemNo = c.getString(c.getColumnIndex(CATELOG_ITEMNO));
                info.mBarcode = c.getString(c.getColumnIndex(CATELOG_BARCODE));
                info.mCatalogueID = c.getString(c.getColumnIndex(CATELOG_CATALOGUEID));
                info.mENGItemName = c.getString(c.getColumnIndex(CATELOG_ENGITEMNAME));
                info.mENGSpecification = c.getString(c.getColumnIndex(CATELOG_ENGSPECIFICATION));
                info.mOuterVolume = c.getString(c.getColumnIndex(CATELOG_OUTERVOLUME));
                info.mOuterCapacity = c.getString(c.getColumnIndex(CATELOG_OUTHERCAPACITY));
                info.mPacking = c.getString(c.getColumnIndex(CATELOG_PACKING));
                info.mUnit = c.getString(c.getColumnIndex(CATELOG_UNIT));
                info.mENGMemo = c.getString(c.getColumnIndex(CATELOG_ENGMEMO));
                info.mPhoto = c.getString(c.getColumnIndex(CATELOG_PHOTO));
                info.mSupplierShortName = c.getString(c.getColumnIndex(CATELOG_SUPPLIERSHORTNAME));
                info.mSupplierItemNo = c.getString(c.getColumnIndex(CATELOG_SUPPLIERITEMNO));
                info.mCanBill = c.getString(c.getColumnIndex(CATELOG_CANBILL));
                info.mPurchasePrice = c.getString(c.getColumnIndex(CATELOG_PURCHASEPRICE));
                info.mMinimumQty = c.getString(c.getColumnIndex(CATELOG_MINIMUNQTY));
                info.mOuterGrossWeight = c.getString(c.getColumnIndex(CATELOG_OUTHERGROSSWEIGHT));
                info.mOuterNetWeight = c.getString(c.getColumnIndex(CATELOG_OUTERNETWEIGHT));
                info.mSalesPrice = c.getString(c.getColumnIndex(CATELOG_SALESPRICE));
                info.mRebate = c.getString(c.getColumnIndex(CATELOG_REBATE));
                categories.add(info);
            }
            c.close();
        }
        else if(keyword.length() == 0 && catalogid.length() != 0)
        {
            for(int i = 0 ; i < catalodids.length ; i++)
            {
                sql = "SELECT * FROM " + TABLE_CATELOG+" WHERE "+CATELOG_CATALOGUEID + " = ?" ;
                c = db.rawQuery(sql, new String[]
                        {catalodids[i]});

                while (c.moveToNext()) {
                    Catalog info = new Catalog();
                    info.mLastModified = c.getString(c.getColumnIndex(CATELOG_LASTMODIFIED));
                    info.mSerialID = c.getString(c.getColumnIndex(CATELOG_SERIALID));
                    info.mItemNo = c.getString(c.getColumnIndex(CATELOG_ITEMNO));
                    info.mBarcode = c.getString(c.getColumnIndex(CATELOG_BARCODE));
                    info.mCatalogueID = c.getString(c.getColumnIndex(CATELOG_CATALOGUEID));
                    info.mENGItemName = c.getString(c.getColumnIndex(CATELOG_ENGITEMNAME));
                    info.mENGSpecification = c.getString(c.getColumnIndex(CATELOG_ENGSPECIFICATION));
                    info.mOuterVolume = c.getString(c.getColumnIndex(CATELOG_OUTERVOLUME));
                    info.mOuterCapacity = c.getString(c.getColumnIndex(CATELOG_OUTHERCAPACITY));
                    info.mPacking = c.getString(c.getColumnIndex(CATELOG_PACKING));
                    info.mUnit = c.getString(c.getColumnIndex(CATELOG_UNIT));
                    info.mENGMemo = c.getString(c.getColumnIndex(CATELOG_ENGMEMO));
                    info.mPhoto = c.getString(c.getColumnIndex(CATELOG_PHOTO));
                    info.mSupplierShortName = c.getString(c.getColumnIndex(CATELOG_SUPPLIERSHORTNAME));
                    info.mSupplierItemNo = c.getString(c.getColumnIndex(CATELOG_SUPPLIERITEMNO));
                    info.mCanBill = c.getString(c.getColumnIndex(CATELOG_CANBILL));
                    info.mPurchasePrice = c.getString(c.getColumnIndex(CATELOG_PURCHASEPRICE));
                    info.mMinimumQty = c.getString(c.getColumnIndex(CATELOG_MINIMUNQTY));
                    info.mOuterGrossWeight = c.getString(c.getColumnIndex(CATELOG_OUTHERGROSSWEIGHT));
                    info.mOuterNetWeight = c.getString(c.getColumnIndex(CATELOG_OUTERNETWEIGHT));
                    info.mSalesPrice = c.getString(c.getColumnIndex(CATELOG_SALESPRICE));
                    info.mRebate = c.getString(c.getColumnIndex(CATELOG_REBATE));
                    categories.add(info);
                }
                c.close();
            }


        }
        else
        {
            for(int i = 0 ; i < catalodids.length ; i++)
            {
                sql = "SELECT * FROM " + TABLE_CATELOG+" WHERE "+ CATELOG_ITEMNO + " = ?"+" AND "+CATELOG_CATALOGUEID + " = ?" ;
                c = db.rawQuery(sql, new String[]
                        {keyword,catalodids[i]});

                while (c.moveToNext()) {
                    Catalog info = new Catalog();
                    info.mLastModified = c.getString(c.getColumnIndex(CATELOG_LASTMODIFIED));
                    info.mSerialID = c.getString(c.getColumnIndex(CATELOG_SERIALID));
                    info.mItemNo = c.getString(c.getColumnIndex(CATELOG_ITEMNO));
                    info.mBarcode = c.getString(c.getColumnIndex(CATELOG_BARCODE));
                    info.mCatalogueID = c.getString(c.getColumnIndex(CATELOG_CATALOGUEID));
                    info.mENGItemName = c.getString(c.getColumnIndex(CATELOG_ENGITEMNAME));
                    info.mENGSpecification = c.getString(c.getColumnIndex(CATELOG_ENGSPECIFICATION));
                    info.mOuterVolume = c.getString(c.getColumnIndex(CATELOG_OUTERVOLUME));
                    info.mOuterCapacity = c.getString(c.getColumnIndex(CATELOG_OUTHERCAPACITY));
                    info.mPacking = c.getString(c.getColumnIndex(CATELOG_PACKING));
                    info.mUnit = c.getString(c.getColumnIndex(CATELOG_UNIT));
                    info.mENGMemo = c.getString(c.getColumnIndex(CATELOG_ENGMEMO));
                    info.mPhoto = c.getString(c.getColumnIndex(CATELOG_PHOTO));
                    info.mSupplierShortName = c.getString(c.getColumnIndex(CATELOG_SUPPLIERSHORTNAME));
                    info.mSupplierItemNo = c.getString(c.getColumnIndex(CATELOG_SUPPLIERITEMNO));
                    info.mCanBill = c.getString(c.getColumnIndex(CATELOG_CANBILL));
                    info.mPurchasePrice = c.getString(c.getColumnIndex(CATELOG_PURCHASEPRICE));
                    info.mMinimumQty = c.getString(c.getColumnIndex(CATELOG_MINIMUNQTY));
                    info.mOuterGrossWeight = c.getString(c.getColumnIndex(CATELOG_OUTHERGROSSWEIGHT));
                    info.mOuterNetWeight = c.getString(c.getColumnIndex(CATELOG_OUTERNETWEIGHT));
                    info.mSalesPrice = c.getString(c.getColumnIndex(CATELOG_SALESPRICE));
                    info.mRebate = c.getString(c.getColumnIndex(CATELOG_REBATE));
                    categories.add(info);
                }
                c.close();
            }

        }



        return categories;
    }

    public int addCatelog(Catalog sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(CATELOG_LASTMODIFIED, sInfo.mLastModified);
        cv.put(CATELOG_SERIALID, sInfo.mSerialID);
        cv.put(CATELOG_ITEMNO, String.valueOf(sInfo.mItemNo));
        cv.put(CATELOG_BARCODE, sInfo.mBarcode);
        cv.put(CATELOG_CATALOGUEID, sInfo.mCatalogueID);
        cv.put(CATELOG_ENGITEMNAME, sInfo.mENGItemName);
        cv.put(CATELOG_ENGSPECIFICATION, String.valueOf(sInfo.mENGSpecification));
        cv.put(CATELOG_OUTERVOLUME, sInfo.mOuterVolume);
        cv.put(CATELOG_OUTHERCAPACITY, sInfo.mOuterCapacity);
        cv.put(CATELOG_PACKING, sInfo.mPacking);
        cv.put(CATELOG_UNIT, String.valueOf(sInfo.mUnit));
        cv.put(CATELOG_ENGMEMO, sInfo.mENGMemo);
        cv.put(CATELOG_PHOTO, sInfo.mPhoto);
        cv.put(CATELOG_SUPPLIERSHORTNAME, sInfo.mSupplierShortName);
        cv.put(CATELOG_SUPPLIERITEMNO, String.valueOf(sInfo.mSupplierItemNo));
        cv.put(CATELOG_CANBILL, sInfo.mCanBill);
        cv.put(CATELOG_PURCHASEPRICE, sInfo.mPurchasePrice);
        cv.put(CATELOG_MINIMUNQTY, String.valueOf(sInfo.mMinimumQty));
        cv.put(CATELOG_OUTHERGROSSWEIGHT, sInfo.mOuterGrossWeight);
        cv.put(CATELOG_OUTERNETWEIGHT, sInfo.mOuterNetWeight);
        cv.put(CATELOG_SALESPRICE, sInfo.mSalesPrice);
        cv.put(CATELOG_REBATE, String.valueOf(sInfo.mRebate));
        int iRet = (int) db.insert(TABLE_CATELOG, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_CATELOG, cv, CATELOG_SERIALID + "=?", new String[]
                    {sInfo.mSerialID});
        }
        return iRet;
    }

    public void addListCatelog(ArrayList<Catalog> sInfos) {
        openDB();
        for(int i = 0 ; i < sInfos.size() ; i++)
        {
            Catalog sInfo = sInfos.get(i);
            ContentValues cv = new ContentValues();
            cv.put(CATELOG_LASTMODIFIED, sInfo.mLastModified);
            cv.put(CATELOG_SERIALID, sInfo.mSerialID);
            cv.put(CATELOG_ITEMNO, String.valueOf(sInfo.mItemNo));
            cv.put(CATELOG_BARCODE, sInfo.mBarcode);
            cv.put(CATELOG_CATALOGUEID, sInfo.mCatalogueID);
            cv.put(CATELOG_ENGITEMNAME, sInfo.mENGItemName);
            cv.put(CATELOG_ENGSPECIFICATION, String.valueOf(sInfo.mENGSpecification));
            cv.put(CATELOG_OUTERVOLUME, sInfo.mOuterVolume);
            cv.put(CATELOG_OUTHERCAPACITY, sInfo.mOuterCapacity);
            cv.put(CATELOG_PACKING, sInfo.mPacking);
            cv.put(CATELOG_UNIT, String.valueOf(sInfo.mUnit));
            cv.put(CATELOG_ENGMEMO, sInfo.mENGMemo);
            cv.put(CATELOG_PHOTO, sInfo.mPhoto);
            cv.put(CATELOG_SUPPLIERSHORTNAME, sInfo.mSupplierShortName);
            cv.put(CATELOG_SUPPLIERITEMNO, String.valueOf(sInfo.mSupplierItemNo));
            cv.put(CATELOG_CANBILL, sInfo.mCanBill);
            cv.put(CATELOG_PURCHASEPRICE, sInfo.mPurchasePrice);
            cv.put(CATELOG_MINIMUNQTY, String.valueOf(sInfo.mMinimumQty));
            cv.put(CATELOG_OUTHERGROSSWEIGHT, sInfo.mOuterGrossWeight);
            cv.put(CATELOG_OUTERNETWEIGHT, sInfo.mOuterNetWeight);
            cv.put(CATELOG_SALESPRICE, sInfo.mSalesPrice);
            cv.put(CATELOG_REBATE, String.valueOf(sInfo.mRebate));
            int iRet = (int) db.insert(TABLE_CATELOG, null, cv);
            if (-1 == iRet) {
                iRet = db.update(TABLE_CATELOG, cv, CATELOG_SERIALID + "=?", new String[]
                        {sInfo.mSerialID});
            }
        }
    }

    public int deleteCatalog(Catalog sInfo) {
        return db.delete(TABLE_CATELOG, CATELOG_SERIALID + "=?", new String[]
                {sInfo.mSerialID});
    }

    public int deleteServiceAllCatalog() {
        return db.delete(TABLE_CATELOG, null, new String[]
                {});
    }
}
