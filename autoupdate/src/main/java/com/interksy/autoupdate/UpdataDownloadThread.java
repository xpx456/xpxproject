package com.interksy.autoupdate;

import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.Contral;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ProgressResponseListener;


public class UpdataDownloadThread extends Thread {

    private RandomAccessFile randomAccessFile = null;
    private String url;
    private String path;
    private long mSize;
    private long updateTime = 0;
    public Contral contral = new Contral();

    public UpdataDownloadThread(String path,String url,long size) {
        this.url = url;
        this.path = path;
        this.mSize = size;
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            File mfile = new File(path);
            if (mfile.exists()) {
                mfile.delete();
            }
            if(AppUtils.checkFreeSize(mSize))
            {
                randomAccessFile = new RandomAccessFile(mfile.getPath(), "rwd");
                Message message = new Message();
                updateTime = System.currentTimeMillis();
                if(NetUtils.getInstance().doDownload(url,progressResponseListener,randomAccessFile,contral) == 0)
                {
                    if(contral.stop == false)
                    {
                        message.what = UpdateHandler.EVETN_DOWNLOAD_FAIL;
                        UpDataManager.mUpDataManager.handler.sendMessage(message);
                    }
                }

            }

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private ProgressResponseListener progressResponseListener = new ProgressResponseListener() {

        @Override
        public void onProgressResponse(long allBytes, long currentBytes, boolean done) {
            // TODO Auto-generated method stub
            Log.e("TAG", "bytesRead:" + currentBytes);
            Log.e("TAG", "contentLength:" + allBytes);
            Log.e("TAG", "done:" + done);
            if (allBytes != -1) {
                // 长度未知的情况下回返回-1
                Log.e("TAG", (100 * currentBytes) / allBytes + "% done");
            }
            Log.e("TAG", "================================");
            if(done == false && currentBytes > 0 && (System.currentTimeMillis() - updateTime) > 1000)
            {
                UpMessage mDMessage = new UpMessage();
                mDMessage.finishsize = currentBytes;
                mDMessage.size = allBytes;
                Message message = new Message();
                message.obj = mDMessage;
                message.what = UpdateHandler.EVETN_DOWNLOAD_UPADTE;
                UpDataManager.mUpDataManager.handler.sendMessage(message);
                updateTime = System.currentTimeMillis();
            }
            else if(done == true)
            {
                Message message = new Message();
                message.what = UpdateHandler.EVETN_DOWNLOAD_FINISH;
                UpDataManager.mUpDataManager.handler.sendMessage(message);
                updateTime = System.currentTimeMillis();
            }
        }
    };

    public static class UpMessage{
        public long size = 0;
        public long finishsize = 0;
    }
}
