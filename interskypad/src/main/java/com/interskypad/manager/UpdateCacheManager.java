package com.interskypad.manager;

import android.os.Handler;
import android.os.Message;

import com.interskypad.asks.ProductAsks;
import com.interskypad.database.ProductDBHelper;
import com.interskypad.entity.Catalog;
import com.interskypad.entity.Category;
import com.interskypad.prase.ProductPrase;
import com.interskypad.view.InterskyPadApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.Contral;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;

public class UpdateCacheManager {

    public static final int DOWNLOAD_CATEGORY_FINISH = 1060000;
    public static final int DOWNLOAD_CATEGORY_FAIL = 1060006;
    public static final int DOWNLOAD_PRODUCT_IMF_FINISH = 1060001;
    public static final int DOWNLOAD_PRODUCT_IMF_FAIL = 1060007;
    public static final int DOWNLOAD_PRODUCT_PIC_UPDATE = 1060002;
    public static final int DOWNLOAD_PRODUCT_PIC_FINISH = 1060003;
    public static final int DOWNLOAD_PRODUCT_PIC_FAIL = 1060004;
    public static UpdateCacheManager mUpdateCacheManager;
    public ArrayList<Category> categories = new ArrayList<Category>();
    public ArrayList<Catalog> catalogs = new ArrayList<Catalog>();
    public Handler handler;
    public int progress = 0;
    public RandomAccessFile randomAccessFile;
    public UpdataThread thread;
    public Contral contral = new Contral();
    public static synchronized UpdateCacheManager getInstance() {
        if (mUpdateCacheManager == null) {
            mUpdateCacheManager = new UpdateCacheManager();
        }
        return mUpdateCacheManager;
    }

    public UpdateCacheManager() {

    }

    public static class UpdataThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                String urlString = NetUtils.getInstance().praseUrl(InterskyPadApplication.mApp.mService,ProductAsks.CATEGORY_PATH);
                ArrayList<NameValuePair> items1 = new ArrayList<NameValuePair>();
                BasicNameValuePair item1 = new BasicNameValuePair("token", NetUtils.getInstance().token);
                items1.add(item1);
                if(UpdateCacheManager.getInstance().contral.stop)
                {
                    UpdateCacheManager.getInstance().thread = null;
                    return;
                }
                ResposeResult request = NetUtils.getInstance().post(urlString, NetUtils.initRepuestBody(items1));
                if(request != null)
                {
                    if(request.isSuccess)
                    {
                        UpdateCacheManager.getInstance().categories.clear();
                        boolean success = ProductPrase.praseCategory2(request.result,UpdateCacheManager.getInstance().categories);
                        if(success == false)
                        {
                            if(UpdateCacheManager.getInstance().handler != null)
                            {
                                Message message = new Message();
                                message.what = DOWNLOAD_CATEGORY_FAIL;
                                message.obj = UpdateCacheManager.getInstance().progress;
                                UpdateCacheManager.getInstance().handler.sendMessage(message);
                                UpdateCacheManager.getInstance().thread = null;
                            }
                            return;
                        }
                        ProductDBHelper.getInstance(InterskyPadApplication.mApp).listAddCategory(UpdateCacheManager.getInstance().categories);
                        UpdateCacheManager.getInstance().progress = 50;
                        if(UpdateCacheManager.getInstance().handler != null)
                        {
                            Message message = new Message();
                            message.what = DOWNLOAD_CATEGORY_FINISH;
                            message.obj = UpdateCacheManager.getInstance().progress;
                            UpdateCacheManager.getInstance().handler.sendMessage(message);
                        }
                        int page = 1;
                        boolean isall = false;
                        UpdateCacheManager.getInstance().catalogs.clear();
                        if(UpdateCacheManager.getInstance().contral.stop)
                        {
                            UpdateCacheManager.getInstance().thread = null;
                            return;
                        }
                        while (isall == false)
                        {
                            urlString = NetUtils.getInstance().praseUrl(InterskyPadApplication.mApp.mService,ProductAsks.CATALOG_PATH);

                            ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
                            BasicNameValuePair item = new BasicNameValuePair("token", NetUtils.getInstance().token);
                            items.add(item);
                            item = new BasicNameValuePair("CatalogueID", "");
                            items.add(item);
                            item = new BasicNameValuePair("keyword", "");
                            items.add(item);
                            item = new BasicNameValuePair("page_no", String.valueOf(page));
                            items.add(item);
                            item = new BasicNameValuePair("page_size", "80");
                            items.add(item);
                            request = NetUtils.getInstance().post(urlString, NetUtils.initRepuestBody(items));
                            if(request != null)
                            {
                                if(request.isSuccess)
                                {
                                    isall = ProductPrase.praseCatalog2(request.result,UpdateCacheManager.getInstance().catalogs);
                                    if(isall == false)
                                    {
                                        page++;
                                    }
                                }
                            }
                            else
                            {
                                isall = true;
                            }
                        }
                        UpdateCacheManager.getInstance().progress = 150;
                        int count = UpdateCacheManager.getInstance().catalogs.size();
                        if(count > 0)
                        {
                            ProductDBHelper.getInstance(InterskyPadApplication.mApp).addListCatelog(UpdateCacheManager.getInstance().catalogs);
                            if(UpdateCacheManager.getInstance().handler != null)
                            {
                                Message message = new Message();
                                message.what = DOWNLOAD_PRODUCT_IMF_FINISH;
                                message.obj = UpdateCacheManager.getInstance().progress;
                                UpdateCacheManager.getInstance().handler.sendMessage(message);
                            }
                        }
                        else
                        {
                            if(UpdateCacheManager.getInstance().handler != null)
                            {
                                Message message = new Message();
                                message.what = DOWNLOAD_PRODUCT_IMF_FAIL;
                                message.obj = UpdateCacheManager.getInstance().progress;
                                UpdateCacheManager.getInstance().handler.sendMessage(message);
                                UpdateCacheManager.getInstance().thread = null;
                            }
                            return;
                        }
                        if(UpdateCacheManager.getInstance().contral.stop)
                        {
                            UpdateCacheManager.getInstance().thread = null;
                            return;
                        }

                        for(int i = 0 ; i <  UpdateCacheManager.getInstance().catalogs.size() ; i++)
                        {
                            Catalog catalog = UpdateCacheManager.getInstance().catalogs.get(i);
                            String path = InterskyPadApplication.mApp.mFileUtils.pathUtils.getfilePath("/photo")+"/"+catalog.mPhoto.replaceAll("/","");
                            String url = ProducterManager.getInstance().getProductPhotoUrl(catalog.mPhoto);

                            File mfile = new File(path);
                            if (mfile.exists()) {
                                mfile.delete();
                            }
                            RandomAccessFile randomAccessFile = new RandomAccessFile(mfile.getPath(), "rwd");

                            if(NetUtils.getInstance().doDownload2(url,randomAccessFile,UpdateCacheManager.getInstance().contral) == 1)
                            {
                                UpdateCacheManager.getInstance().progress += 850/count;
                                if(UpdateCacheManager.getInstance().progress >= 1000)
                                {
                                    UpdateCacheManager.getInstance(). progress = 1000;
                                }
                                if(UpdateCacheManager.getInstance().handler != null)
                                {
                                    Message message = new Message();
                                    message.what = DOWNLOAD_PRODUCT_PIC_UPDATE;
                                    message.obj = UpdateCacheManager.getInstance().progress;
                                    UpdateCacheManager.getInstance().handler.sendMessage(message);
                                }
                            }
                            else
                            {
                                UpdateCacheManager.getInstance().progress += 850/count;
                                if(UpdateCacheManager.getInstance().progress >= 1000)
                                {
                                    UpdateCacheManager.getInstance().progress = 1000;
                                }

                                if(UpdateCacheManager.getInstance().handler != null)
                                {
                                    Message message = new Message();
                                    message.what = DOWNLOAD_PRODUCT_PIC_FAIL;
                                    message.obj = catalog.mPhoto;
                                    message.arg1 = UpdateCacheManager.getInstance().progress;
                                    UpdateCacheManager.getInstance().handler.sendMessage(message);
                                }
                            }
                            if(UpdateCacheManager.getInstance().contral.stop)
                            {
                                UpdateCacheManager.getInstance().thread = null;
                                return;
                            }
                        }
                    }
                    else
                    {
                        if(UpdateCacheManager.getInstance().handler != null)
                        {
                            Message message = new Message();
                            message.what = DOWNLOAD_CATEGORY_FAIL;
                            message.obj = UpdateCacheManager.getInstance().progress;
                            UpdateCacheManager.getInstance().handler.sendMessage(message);
                            UpdateCacheManager.getInstance().thread = null;
                        }
                    }

                    if(UpdateCacheManager.getInstance().handler != null)
                    {
                        UpdateCacheManager.getInstance().progress = 1000;
                        Message message = new Message();
                        message.what = DOWNLOAD_PRODUCT_PIC_FINISH;
                        message.obj = UpdateCacheManager.getInstance().progress;
                        UpdateCacheManager.getInstance().handler.sendMessage(message);
                    }
                }
                else
                {
                    if(UpdateCacheManager.getInstance().handler != null)
                    {
                        Message message = new Message();
                        message.what = DOWNLOAD_CATEGORY_FAIL;
                        message.obj = UpdateCacheManager.getInstance().progress;
                        UpdateCacheManager.getInstance().handler.sendMessage(message);
                        UpdateCacheManager.getInstance().thread = null;
                    }
                }
                UpdateCacheManager.getInstance().thread = null;
            }  catch (FileNotFoundException e) {
                e.printStackTrace();
                UpdateCacheManager.getInstance().thread = null;
            } catch (IOException e) {
                e.printStackTrace();
                UpdateCacheManager.getInstance().thread = null;
            }

        }
    }
}
