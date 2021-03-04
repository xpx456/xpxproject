package intersky.document;

import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import intersky.document.entity.DMessage;
import intersky.document.entity.DocumentNet;
import intersky.document.handler.DownUpHandler;
import intersky.xpxnet.net.Contral;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ProgressRequestListener;


public class DocumentUpLoadThread extends Thread {

    public static final String HTTP_UPLOAD_PATH = "doc/file/upload";
    public DocumentNet mDocumentNet;
    private long updateTime = 0;
    public Contral contral = new Contral();

    public DocumentUpLoadThread(DocumentNet mDownload) {
        this.mDocumentNet = mDownload;
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            File mfile = new File(mDocumentNet.mPath);
            if (mfile.exists()) {
//                mfile.delete();
                String urlString = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,HTTP_UPLOAD_PATH);
                ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new NameValuePair("owner_type", mDocumentNet.mOwnerType));
                nvps.add(new NameValuePair("parent_id", mDocumentNet.parentid));
                nvps.add(new NameValuePair("owner_id", mDocumentNet.mOwnerID));
                nvps.add(new NameValuePair("catalogue_id", mDocumentNet.mCatalogueID));
                nvps.add(new NameValuePair("creation", mDocumentNet.mDate));
                nvps.add(new NameValuePair("record_id", mDocumentNet.mRecordID));
                nvps.add(new NameValuePair("token", NetUtils.getInstance().token));
                nvps.add(new NameValuePair("filename", URLEncoder.encode(mfile.getName(), "UTF-8")));
                NameValuePair mNameValuePair = new NameValuePair("file",URLEncoder.encode(mfile.getName(), "UTF-8"));
                mNameValuePair.isFile = true;
                mNameValuePair.path = mfile.getPath();
                nvps.add(mNameValuePair);
                DMessage mDMessage = new DMessage();
                mDMessage.documentNet = mDocumentNet;
                mDMessage.size = mfile.length();
                Message message = new Message();
//                message.what = DownUpHandler.EVETN_UPLOAD_START_NO;
//                message.obj = mDMessage;
//                DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                updateTime = System.currentTimeMillis();
                boolean success = NetUtils.getInstance().initRepuestBodyUpload(nvps,mDocumentNet.mUrl,progressRequestListener);
                if (success) {
                    message.what = DownUpHandler.EVETN_UPLOAD_FINISH;
                    message.obj = mDocumentNet;
                    DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                }
                else {
                    message.what = DownUpHandler.EVETN_UPLOAD_FAIL;
                    message.obj = mDocumentNet;
                    DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                }
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private ProgressRequestListener progressRequestListener = new ProgressRequestListener() {
        @Override
        public void onRequestProgress(long bytesRead, long contentLength, boolean done) {
            Log.e("TAG", "bytesRead:" + bytesRead);
            Log.e("TAG", "contentLength:" + contentLength);
            Log.e("TAG", "done:" + done);
            if (contentLength != -1) {
                //长度未知的情况下回返回-1
                Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
            }
            if(done == false && bytesRead > 0 && (System.currentTimeMillis() - updateTime) > 1000)
            {
                DMessage mDMessage = new DMessage();
                mDMessage.documentNet = mDocumentNet;
                mDMessage.finishsize = bytesRead;
                Message message = new Message();
                message.what = DownUpHandler.EVETN_UPLOAD_UPADTE;
                message.obj = mDMessage;
                message.arg1 = (int) bytesRead;
                DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                updateTime = System.currentTimeMillis();
            }

            Log.e("TAG", "================================");
        }
    };

    public void doCancle() {
        NetUtils.getInstance().stopDocumentUpCall();
    }
}
