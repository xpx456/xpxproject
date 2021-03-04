package intersky.document.receive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.document.DocumentManager;
import intersky.document.handler.DocumentHandler;
import intersky.appbase.BaseReceiver;


public class DocumentReceiver extends BaseReceiver {

    public Handler mHandler;

    public DocumentReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
        intentFilter.addAction(DocumentManager.ACTION_SELECT_UPLOADFILE_FINISH);
        intentFilter.addAction(DocumentManager.ACTION_DOCUMENT_UPLOAD_FAIL);
        intentFilter.addAction(DocumentManager.ACTION_DOCUMENT_DOWNLOAD_FAIL);
        intentFilter.addAction(DocumentManager.ACTION_UPDATA_DOCUMENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(DocumentManager.ACTION_DOCUMENT_UPADA_LOAD))
        {
            if(mHandler != null)
            {
                mHandler.sendEmptyMessage(DocumentHandler.EVENT_UPDATA_LOAD_LIST);
            }

        }
        else if(intent.getAction().equals(DocumentManager.ACTION_SELECT_UPLOADFILE_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = DocumentHandler.EVENT_SET_UPLOAD_FILES;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(DocumentManager.ACTION_DOCUMENT_UPLOAD_FAIL))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = DocumentHandler.EVENT_SET_UPLOAD_FILES;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(DocumentManager.ACTION_DOCUMENT_DOWNLOAD_FAIL))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = DocumentHandler.EVENT_SET_UPLOAD_FILES;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(DocumentManager.ACTION_UPDATA_DOCUMENT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = DocumentHandler.EVENT_DOCUMENT_UPDATA;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
    }
}
