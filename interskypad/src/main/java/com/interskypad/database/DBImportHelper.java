package com.interskypad.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.interskypad.R;
import com.interskypad.entity.Catalog;
import com.interskypad.entity.Category;
import com.interskypad.prase.ProductPrase;
import com.interskypad.view.InterskyPadApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBImportHelper {

    public static final String IMPORT_PATH = "import";


    private static final String TABLE_CATEGORY = "iPaditemsCatalogue";
    private static final String CATEGORY_ID = "RecordID";
    private static final String CATEGORY_PARENTID = "ParentID";
    private static final String CATEGORY_NAME = "Name";
    private static final String CATEGORY_LEAVE = "CATEGORY_LEAVE";

    private static final String TABLE_CATELOG = "Product";
    private static final String CATELOG_LASTMODIFIED = "LastModified";
    private static final String CATELOG_SERIALID = "SerialID";
    private static final String CATELOG_ITEMNO = "ItemNo";
    private static final String CATELOG_BARCODE = "Barcode";
    private static final String CATELOG_CATALOGUEID = "CatalogueID";
    private static final String CATELOG_ENGITEMNAME = "ENGItemName";
    private static final String CATELOG_ENGSPECIFICATION = "ENGSpecification";
    private static final String CATELOG_OUTERVOLUME = "OuterVolume";
    private static final String CATELOG_OUTHERCAPACITY = "OuterCapacity";
    private static final String CATELOG_PACKING = "Packing";
    private static final String CATELOG_UNIT = "Unit";
    private static final String CATELOG_ENGMEMO = "ENGMemo";
    private static final String CATELOG_PHOTO = "Photo";
    private static final String CATELOG_SUPPLIERSHORTNAME = "SupplierShortName";
    private static final String CATELOG_SUPPLIERITEMNO = "SupplierItemNo";
    private static final String CATELOG_CANBILL = "CanBill";
    private static final String CATELOG_PURCHASEPRICE = "PurchasePrice";
    private static final String CATELOG_MINIMUNQTY = "MinimumQty";
    private static final String CATELOG_OUTHERGROSSWEIGHT = "OuterGrossWeight";
    private static final String CATELOG_OUTERNETWEIGHT = "OuterNetWeight";
    private static final String CATELOG_SALESPRICE = "SalesPrice";
    private static final String CATELOG_REBATE = "Rebate";

    public static void initImports(Context context) {


        File file = new File(InterskyPadApplication.mApp.mFileUtils.pathUtils.APP_PATH);
        if(file.isDirectory())
        {
            File[] files = file.listFiles();
            for(int i = 0 ; i < files.length ; i++) {
                if(files[i].isDirectory())
                {
                    File file1 = files[i];
                    readDB(file1,context);
                }
            }
        }
    }

    public static void readDB(File file,Context context) {
        File filedir = new File(file.getPath()+"/"+IMPORT_PATH);
        if(filedir.exists())
        {
            File[] files = filedir.listFiles();
            for(int i = 0 ; i < files.length ; i++)
            {
                File file1 = files[i];
                if(!file1.isDirectory())
                {
                    if(file1.getName().toLowerCase().startsWith("android")&&file1.getName().toLowerCase().endsWith(".db"))
                    {
                        ProductDBHelper.init(context,file.getName());
                        readIos(file1,file.getName(),context);
                    }
                    else if(file1.getName().toLowerCase().startsWith("ios")&&file1.getName().toLowerCase().endsWith(".db"))
                    {
                        ProductDBHelper.init(context,file.getName());
                        readAndroid(file1,file.getName(),context);
                    }
                }
            }
        }

    }

    public static void readIos(File file,String service,Context context) {
        String code = checkNameDateCode(file.getName().substring(3,file.getName().length()));
        int version = Integer.valueOf(code);
        if(checkeVersion(version,service,context))
        {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
            ArrayList<Category> categories = new ArrayList<Category>();
            ArrayList<Category> categories2 = new ArrayList<Category>();
            categories.addAll(scanCategoryIos(db));
            categories2.addAll(categories);
            ProductPrase.measureCategoryLeaves(categories2,new Category(context.getString(R.string.product_cat_all),0,"",""));
            ProductDBHelper.getInstance(context).listAddCategory(categories);
            ArrayList<Catalog> catalogs = new ArrayList<Catalog>();
            catalogs.addAll(scanCatalogIos(db));
            ProductDBHelper.getInstance(context).addListCatelog(catalogs);
        }

    }

    public static void readAndroid(File file,String service,Context context) {
        String code = checkNameDateCode(file.getName().substring(7,file.getName().length()));
        int version = Integer.valueOf(code);
        if(checkeVersion(version,service,context))
        {

        }
    }

    public static String checkNameDateCode(String name)
    {

        Pattern pattern = Pattern.compile("[0-9]*");
        if(name.length() >= 12)
        {
            String date = name.substring(0,12);
            Matcher isNum = pattern.matcher(date);
            if( isNum.matches() ){
                return date;
            }
            return "0";
        }
        else
        {
            return "0";
        }
    }

    public static boolean checkeVersion(int version,String service,Context context) {
        String oldcode = DBHelper.getInstance(context).getModifyInfo(service);
        if(oldcode.length() > 0)
        {
            int oleversion = Integer.valueOf(oldcode);
            if(version > oleversion)
            {
                return true;
            }
        }
        return false;
    }

    public static List<Category> scanCategoryIos(SQLiteDatabase db) {
        List<Category> categories = new ArrayList<Category>();

        String sql = "SELECT * FROM " + TABLE_CATEGORY;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            Category info = new Category();
            info.id = c.getString(c.getColumnIndex(CATEGORY_ID));
            info.parendId = c.getString(c.getColumnIndex(CATEGORY_PARENTID));
            info.contentText = c.getString(c.getColumnIndex(CATEGORY_NAME));
            categories.add(info);
        }
        c.close();
        return categories;
    }

    public static List<Catalog> scanCatalogIos(SQLiteDatabase db) {
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
}
