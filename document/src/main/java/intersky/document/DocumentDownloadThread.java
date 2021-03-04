package intersky.document;

import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import intersky.document.entity.DMessage;
import intersky.document.entity.DocumentNet;
import intersky.document.handler.DownUpHandler;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.Contral;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ProgressResponseListener;


public class DocumentDownloadThread extends Thread {

    private RandomAccessFile randomAccessFile = null;
    public DocumentNet mDocumentNet;
    private long updateTime = 0;
    public Contral contral = new Contral();

    public DocumentDownloadThread(DocumentNet mDownload) {
        this.mDocumentNet = mDownload;
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            File mfile = new File(mDocumentNet.mPath);
            if (mfile.exists()) {
                mfile.delete();
            }
            if(AppUtils.checkFreeSize(mDocumentNet.mSize))
            {
                randomAccessFile = new RandomAccessFile(mfile.getPath(), "rwd");
                DMessage mDMessage = new DMessage();
                mDMessage.documentNet = mDocumentNet;
                mDMessage.size = mDocumentNet.mSize;
                Message message = new Message();
//                message.obj = mDocumentNet;
//                message.what = DownUpHandler.EVETN_DOWNLOAD_START;
//                DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                updateTime = System.currentTimeMillis();
                if(NetUtils.getInstance().doDownload(mDocumentNet.mUrl,progressResponseListener,randomAccessFile,contral) == 0)
                {
                    if(contral.stop == false)
                    {
                        message.what = DownUpHandler.EVETN_DOWNLOAD_FAIL;
                        DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
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
                DMessage mDMessage = new DMessage();
                mDMessage.documentNet = mDocumentNet;
                mDMessage.finishsize = currentBytes;
                mDMessage.size = allBytes;
                Message message = new Message();
                message.obj = mDMessage;
                message.what = DownUpHandler.EVETN_DOWNLOAD_UPADTE;
                DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                updateTime = System.currentTimeMillis();
            }
            else if(done == true)
            {
                Message message = new Message();
                message.obj = mDocumentNet;
                message.what = DownUpHandler.EVETN_DOWNLOAD_FINISH;
                DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                updateTime = System.currentTimeMillis();
            }
        }
    };
}
