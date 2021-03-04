package com.interskypad.thread;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.interskypad.asks.ProductAsks;
import com.interskypad.database.ProductDBHelper;

public class GetCatalogThread extends Thread {

    public Handler handler;
    public Context context;
    public String keyword;
    public String categoryid;
    public GetCatalogThread(Handler handler, Context context,String keyword,String categoryid) {
        this.handler = handler;
        this.context = context;
        this.keyword = keyword;
        this.categoryid = categoryid;
    }

    @Override
    public void run() {
        super.run();
        Message message = new Message();
        message.what = ProductAsks.EVENT_GET_CATALOG_LOCAL_SUCCESS;
        message.obj =  ProductDBHelper.getInstance(context).scanCatalog(keyword,categoryid);
        handler.sendMessage(message);
    }
}
