package com.interskypad.view;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.content.FileProvider;
import androidx.multidex.MultiDexApplication;

import com.interksy.autoupdate.UpDataManager;
import com.interskypad.database.DBHelper;
import com.interskypad.database.DBImportHelper;
import com.interskypad.database.ProductDBHelper;
import com.interskypad.entity.Catalog;
import com.interskypad.entity.Category;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.GetProvideGetPath;
import intersky.appbase.Local.LocalData;
import intersky.appbase.entity.Account;
import intersky.filetools.FileUtils;
import intersky.scan.ScanUtils;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.Service;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-7-23
 * Copyright @ 2013 BU
 * Description: 全局application
 *
 * History:
 */
public class InterskyPadApplication extends MultiDexApplication {

	public static final String CHECK_VERSION_URL = "http://www.intersky.com.cn/app/android.version/interskypad.txt";
	public static final String UPDATA_APP_URL = "http://www.intersky.com.cn/app/android.version/interskypad.apk";
	public static final String UPDATA_NAME = "interskypad.apk";
	public static InterskyPadApplication mApp;
	public Account mAccount = new Account();
	public Service mService;
	public String szImei = "asdf";
	public String lastAddress = "";
	public String lastUserid = "";
	public FileUtils mFileUtils;
	public Boolean isLogin = false;
	public AppActivityManager appActivityManager;
	public void onCreate() {
		mApp = this;
		mFileUtils = FileUtils.init(mApp,mgetProvidePath,null,null);
		mFileUtils.pathUtils.setBase("/interskypad");
		try {
			PackageManager packageManager = mApp.getApplicationContext().getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					mApp.getPackageName(), 0);
			//UpDataManager.init(mApp,CHECK_VERSION_URL,UPDATA_APP_URL,packInfo.versionName,packInfo.versionCode,mFileUtils.pathUtils.APP_PATH+"/"+UPDATA_NAME,"interskypad");
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
//		if(UpDataManager.getInstance() != null)
//		{
//			UpDataManager.getInstance().checkVersin();
//		}
		DBImportHelper.initImports(mApp);
		if(ProductDBHelper.getInstance(mApp).DB_NAME.equals("test"))
		{
			getLoginInfo();
		}
		ScanUtils.init(mApp);
		appActivityManager = AppActivityManager.getAppActivityManager(mApp);
		NetUtils.getInstance().init(mApp);
		super.onCreate();
	}

	public void getLoginInfo() {
		SharedPreferences sharedPre = mApp.getSharedPreferences(LocalData.LOGIN_INFO, 0);
		String sServer = sharedPre.getString(LocalData.LOGIN_INFO_SERVICE_RECORDID, "");
		ArrayList<Service> services = new ArrayList<Service>();
		services.addAll(DBHelper.getInstance(mApp).scanServer());
		if(sServer.length() != 0)
		{
			InterskyPadApplication.mApp.mService =  DBHelper.getInstance(mApp).getServerInfo(sServer);
			if (InterskyPadApplication.mApp.mService != null) {
				mFileUtils.pathUtils.setBase("/interskypad");
				ProductDBHelper.init(mApp,mService.sAddress);
			}
			else
			{
				mFileUtils.pathUtils.setBase("/interskypad");
			}
		}
		else
		{
			if(services.size() != 0)
			{
				InterskyPadApplication.mApp.mService = services.get(0);
				mFileUtils.pathUtils.setBase("/interskypad");
				ProductDBHelper.init(mApp,mService.sAddress);
			}
			else
			{
				mFileUtils.pathUtils.setBase("/interskypad");
			}
		}
	}


	public void importData() {
		ArrayList<Category> categories = new ArrayList<Category>();
		Category mCategory = new Category("全部",0,"","(Root)");
        categories.add(mCategory);
		mCategory = new Category("武器",1,"","10000000");
		categories.add(mCategory);
		mCategory = new Category("守护者武器",2,"10000000","11000000");
		categories.add(mCategory);
		mCategory = new Category("巨剑",3,"11000000","11100000");
		categories.add(mCategory);
		mCategory = new Category("史诗",4,"11100000","11110000");
		categories.add(mCategory);
		mCategory = new Category("传说",4,"11100000","11120000");
		categories.add(mCategory);
		mCategory = new Category("圣物",4,"11100000","11130000");
		categories.add(mCategory);
		mCategory = new Category("钝器",3,"11000000","11200000");
		categories.add(mCategory);
		mCategory = new Category("史诗",4,"11200000","11210000");
		categories.add(mCategory);
		mCategory = new Category("传说",4,"11200000","11220000");
		categories.add(mCategory);
		mCategory = new Category("圣物",4,"11200000","11230000");
		categories.add(mCategory);
		mCategory = new Category("魔法师武器",2,"10000000","12000000");
		categories.add(mCategory);
		mCategory = new Category("法杖",3,"12000000","12100000");
		categories.add(mCategory);
		mCategory = new Category("史诗",4,"12100000","12110000");
		categories.add(mCategory);
		mCategory = new Category("传说",4,"12100000","12120000");
		categories.add(mCategory);
		mCategory = new Category("圣物",4,"12100000","12130000");
		categories.add(mCategory);
		mCategory = new Category("矛",3,"12000000","12200000");
		categories.add(mCategory);
		mCategory = new Category("史诗",4,"12200000","12210000");
		categories.add(mCategory);
		mCategory = new Category("传说",4,"12200000","12220000");
		categories.add(mCategory);
		mCategory = new Category("圣物",4,"12200000","12230000");
		categories.add(mCategory);
		mCategory = new Category("扫把",3,"12000000","12300000");
		categories.add(mCategory);
		mCategory = new Category("史诗",4,"12300000","12310000");
		categories.add(mCategory);
		mCategory = new Category("传说",4,"12300000","12320000");
		categories.add(mCategory);
		mCategory = new Category("圣物",4,"12300000","12330000");
		categories.add(mCategory);
		mCategory = new Category("防具",1,"","20000000");
		categories.add(mCategory);
		mCategory = new Category("布甲",2,"20000000","21000000");
		categories.add(mCategory);
		mCategory = new Category("史诗",3,"21000000","21100000");
		categories.add(mCategory);
		mCategory = new Category("传说",3,"21000000","21200000");
		categories.add(mCategory);
		mCategory = new Category("皮甲",2,"20000000","22000000");
		categories.add(mCategory);
		mCategory = new Category("史诗",3,"22000000","22100000");
		categories.add(mCategory);
		mCategory = new Category("传说",3,"22000000","22200000");
		categories.add(mCategory);
		mCategory = new Category("轻甲",2,"20000000","23000000");
		categories.add(mCategory);
		mCategory = new Category("史诗",3,"23000000","23100000");
		categories.add(mCategory);
		mCategory = new Category("传说",3,"23000000","23200000");
		categories.add(mCategory);
		mCategory = new Category("重甲",2,"20000000","24000000");
		categories.add(mCategory);
		mCategory = new Category("史诗",3,"24000000","24100000");
		categories.add(mCategory);
		mCategory = new Category("传说",3,"24000000","24200000");
		categories.add(mCategory);
		mCategory = new Category("板甲",2,"20000000","25000000");
		categories.add(mCategory);
		mCategory = new Category("史诗",3,"25000000","25100000");
		categories.add(mCategory);
		mCategory = new Category("传说",3,"25000000","25200000");
		categories.add(mCategory);
		ProductDBHelper.getInstance(mApp).listAddCategory(categories);
		ArrayList<Catalog> catalogs = new ArrayList<Catalog>();
		Catalog catalog = new Catalog("00000001","11110000","德雷克的战鼓");
		catalogs.add(catalog);
		catalog = new Catalog("11210001","11210000","有教无类");
		catalogs.add(catalog);
		catalog = new Catalog("11120001","11120000","誓约之缚巨剑");
		catalogs.add(catalog);
		catalog = new Catalog("11130001","11130000","兰达尔的玉衡巨剑");
		catalogs.add(catalog);
		catalog = new Catalog("12210001","12210000","胜利女神之矛");
		catalogs.add(catalog);
		catalog = new Catalog("12120001","12120000","释魂封灵法杖");
		catalogs.add(catalog);
		catalog = new Catalog("21100001","21100000","苍天之幕：卷云层");
		catalogs.add(catalog);
		catalog = new Catalog("24200001","24200000","疾风之迅铂金胸甲");
		catalogs.add(catalog);
		ProductDBHelper.getInstance(mApp).addListCatelog(catalogs);
	}

	public GetProvideGetPath mgetProvidePath = new GetProvideGetPath() {
		@Override
		public Uri getProvideGetPath(File file) {
			return  FileProvider.getUriForFile(InterskyPadApplication.mApp, "com.intersky.fileprovider", file);
		}
	};
}
