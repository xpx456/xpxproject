package com.interskypad.thread;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.interskypad.asks.ProductAsks;
import com.interskypad.database.ProductDBHelper;

public class GetCategoryThread extends Thread {

    public Handler handler;
    public Context context;
    public GetCategoryThread(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        Message message = new Message();
        message.what = ProductAsks.EVENT_GET_CATEGORY_LOCAL_SUCCESS;
        message.obj =  ProductDBHelper.getInstance(context).scanCategory();
        handler.sendMessage(message);
    }
}
