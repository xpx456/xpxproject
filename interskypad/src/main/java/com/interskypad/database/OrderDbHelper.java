package com.interskypad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.interskypad.entity.Catalog;
import com.interskypad.entity.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "interskypadOrder.db";
    public static final int DB_VERSION = 2;




    private static final String TABLE_ORDER = "TABLE_ORDER";
    private static final String ORDER_NUMBER= "ORDER_NUMBER";
    private static final String ORDER_TIME = "ORDER_TIME";
    private static final String ORDER_MEMO = "ORDER_MEMO";
    private static final String ORDER_PRODUCT_ID = "ORDER_PRODUCT_ID";
    private static final String ORDER_CUSTOMER_NAME = "ORDER_CUSTOMER_NAME";
    private static final String ORDER_CUSTOMER_PHONE = "ORDER_CUSTOMER_PHONE";
    private static final String ORDER_CUSTOMER_MOBIL = "ORDER_CUSTOMER_MOBIL";
    private static final String ORDER_CUSTOMER_ADDRESS = "ORDER_CUSTOMER_ADDRESS";
    private static final String ORDER_CUSTOMER_MAIL = "ORDER_CUSTOMER_MAIL";
    private static final String ORDER_CUSTOMER_MEMO = "ORDER_CUSTOMER_MEMO";
    private static final String ORDER_UPDATA = "ORDER_UPDATA";

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

    private static OrderDbHelper mDBHelper;
    private SQLiteDatabase db = null;

    public static OrderDbHelper getInstance(Context context) {
        if (null == mDBHelper) {

            mDBHelper = new OrderDbHelper(context);
        }
        return mDBHelper;
    }

    private OrderDbHelper(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        String sql = "CREATE TABLE " + TABLE_ORDER + " (" + ORDER_NUMBER
                + " TEXT PRIMARY KEY," + ORDER_TIME + " TEXT," + ORDER_MEMO + " TEXT,"
                + ORDER_CUSTOMER_NAME + " TEXT," + ORDER_CUSTOMER_PHONE + " TEXT,"
                + ORDER_CUSTOMER_MOBIL + " TEXT," + ORDER_CUSTOMER_ADDRESS + " TEXT,"
                + ORDER_CUSTOMER_MAIL + " TEXT," + ORDER_CUSTOMER_MEMO + " TEXT,"+ ORDER_UPDATA + " TEXT," + ORDER_PRODUCT_ID + " TEXT)";
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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        String sql = "CREATE TABLE " + TABLE_ORDER + " (" + ORDER_NUMBER
                + " TEXT PRIMARY KEY," + ORDER_TIME + " TEXT," + ORDER_MEMO + " TEXT,"
                + ORDER_CUSTOMER_NAME + " TEXT," + ORDER_CUSTOMER_PHONE + " TEXT,"
                + ORDER_CUSTOMER_MOBIL + " TEXT," + ORDER_CUSTOMER_ADDRESS + " TEXT,"
                + ORDER_CUSTOMER_MAIL + " TEXT," + ORDER_CUSTOMER_MEMO + " TEXT,"+ ORDER_UPDATA + " TEXT," + ORDER_PRODUCT_ID + " TEXT)";
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

    public List<Order> sacnOrder() {
        openDB();
        List<Order> orders = new ArrayList<Order>();
        String sql = "SELECT * FROM " + TABLE_ORDER;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            Order info = new Order();
            info.id = c.getString(c.getColumnIndex(ORDER_NUMBER));
            info.time = c.getString(c.getColumnIndex(ORDER_TIME));
            info.memo = c.getString(c.getColumnIndex(ORDER_MEMO));
            info.productids = c.getString(c.getColumnIndex(ORDER_PRODUCT_ID));
            info.c_name = c.getString(c.getColumnIndex(ORDER_CUSTOMER_NAME));
            info.c_phone = c.getString(c.getColumnIndex(ORDER_CUSTOMER_PHONE));
            info.c_mobil = c.getString(c.getColumnIndex(ORDER_CUSTOMER_MOBIL));
            info.c_address = c.getString(c.getColumnIndex(ORDER_CUSTOMER_ADDRESS));
            info.c_mail = c.getString(c.getColumnIndex(ORDER_CUSTOMER_MAIL));
            info.c_memo = c.getString(c.getColumnIndex(ORDER_CUSTOMER_MEMO));
            info.issubmit = Boolean.valueOf(c.getString(c.getColumnIndex(ORDER_CUSTOMER_MEMO)));
            orders.add(info);
        }
        c.close();
        for(int i = 0 ; i < orders.size() ; i++) {
            String[] ids = orders.get(i).productids.split(",");
            orders.get(i).addAll(scanCatalog(ids));
        }
        return orders;
    }

    public int addOreder(Order sInfo) {
        sInfo.setProductidsId();
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(ORDER_NUMBER, sInfo.id);
        cv.put(ORDER_TIME, sInfo.time);
        cv.put(ORDER_MEMO, sInfo.memo);
        cv.put(ORDER_UPDATA, String.valueOf(sInfo.issubmit));
        cv.put(ORDER_PRODUCT_ID, sInfo.productids);
        cv.put(ORDER_CUSTOMER_NAME, sInfo.c_name);
        cv.put(ORDER_CUSTOMER_PHONE, sInfo.c_phone);
        cv.put(ORDER_CUSTOMER_MOBIL, sInfo.c_mobil);
        cv.put(ORDER_CUSTOMER_ADDRESS, sInfo.c_address);
        cv.put(ORDER_CUSTOMER_MAIL, sInfo.c_mail);
        cv.put(ORDER_CUSTOMER_MEMO, sInfo.c_memo);

        int iRet = (int) db.insert(TABLE_ORDER, null, cv);
        if (-1 == iRet) {
            db.update(TABLE_ORDER, cv, ORDER_NUMBER + "=?", new String[]
                    {sInfo.id});
        }
        addListCatelog(sInfo.hashcatalogs);
        return iRet;
    }

    public int deleteOrder(Order sInfo) {
        return db.delete(TABLE_ORDER, ORDER_NUMBER + "=?", new String[]
                {sInfo.id});
    }

    public List<Catalog> scanCatalog(String[] ids) {
        openDB();
        List<Catalog> categories = new ArrayList<Catalog>();

        for(int i = 0 ; i < ids.length ; i++)
        {
            String sql = "SELECT * FROM " + TABLE_CATELOG+" WHERE "+ CATELOG_SERIALID + " = ?";
            Cursor c = db.rawQuery(sql, new String[]
                    {ids[i]});

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

    public void addListCatelog(HashMap<String,Catalog> sInfos) {
        openDB();

        for(Map.Entry<String, Catalog> entry: sInfos.entrySet())
        {
            Catalog sInfo = entry.getValue();
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
