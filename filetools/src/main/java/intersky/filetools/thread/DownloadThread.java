package intersky.filetools.thread;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import intersky.filetools.FileUtils;
import intersky.appbase.entity.Attachment;
import intersky.xpxnet.net.Contral;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ProgressResponseListener;

public class DownloadThread extends Thread{

    public static final int STATE_NOMAL = 0;
    public static final int STATE_END = 1;
    public static final int STATE_START = -1;
    public Attachment mAttachment;
    public RandomAccessFile randomAccessFile;
    private long updateTime = 0;
    public String failcode = "";
    public String updatacode = "";
    public String finishcode = "";
    public int ifailcode = 0;
    public int iupdatacode = 1;
    public int ifinishcode = 2;
    public Parcelable item;
    public int state = STATE_NOMAL;
    public Contral contral = new Contral();
    public Handler handler;

    public DownloadThread(Attachment mAttachment, String failcode, String updatacode, String finishcode, Parcelable item)
    {
        this.mAttachment = mAttachment;
        this.failcode = failcode;
        this.updatacode = updatacode;
        this.finishcode = finishcode;
        this.item = item;
    };

    public DownloadThread(Attachment mAttachment, int failcode, int updatacode, int finishcode, Parcelable item,Handler handler)
    {
        this.mAttachment = mAttachment;
        this.ifailcode = failcode;
        this.iupdatacode = updatacode;
        this.ifinishcode = finishcode;
        this.item = item;
        this.handler = handler;
    };

    public DownloadThread(Attachment mAttachment,Parcelable item)
    {
        this.mAttachment = mAttachment;
        this.failcode = FileUtils.ATTACHMENT_DOWNLOAD_FAIL;
        this.updatacode = FileUtils.ATTACHMENT_DOWNLOAD_UPDATA;
        this.finishcode = FileUtils.ATTACHMENT_DOWNLOAD_FINISH;
        this.item = item;
    };

    @Override
    public void run()
    {
        state = STATE_START;
        try {
            File mfile = new File(mAttachment.mPath);
            if (mfile.exists()) {
                mfile.delete();
            }
            randomAccessFile = new RandomAccessFile(mfile.getPath(), "rwd");

            if(NetUtils.getInstance().doDownload(mAttachment.mUrl,progressResponseListener,randomAccessFile,contral) == 0)
            {
                state = STATE_END;
                Intent mfIntent = new Intent();
                mfIntent.putExtra("item", item);
                mfIntent.setAction(failcode);
                FileUtils.mFileUtils.context.sendBroadcast(mfIntent);
                if(handler != null)
                {
                    Message msg = new Message();
                    msg.what = ifailcode;
                    msg.obj = item;
                    handler.sendMessage(msg);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
                Intent mIntent = new Intent();
                mIntent.setAction(updatacode);
                mIntent.putExtra("item", item);
                mIntent.putExtra("finishsize", currentBytes);
                mIntent.putExtra("totalsize", allBytes);
                FileUtils.mFileUtils.context.sendBroadcast(mIntent);
                updateTime = System.currentTimeMillis();
            }
            else if(done == true)
            {
                Intent rIntent = new Intent();
                rIntent.setAction(finishcode);
                rIntent.putExtra("item", item);
                rIntent.putExtra("totalsize", allBytes);
                rIntent.putExtra("name", mAttachment.mName);
                FileUtils.mFileUtils.context.sendBroadcast(rIntent);
                state = STATE_END;
                if(handler != null)
                {
                    Message msg = new Message();
                    msg.what = ifinishcode;
                    msg.obj = item;
                    handler.sendMessage(msg);
                }
            }
        }
    };
}
