package intersky.filetools.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.filetools.FileUtils;
import intersky.filetools.handler.AttachmentHandler;


public class AttachmentReceiver extends BaseReceiver {

    public Handler mHandler;
    public AttachmentReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        intentFilter.addAction(FileUtils.ATTACHMENT_DOWNLOAD_FAIL);
        intentFilter.addAction(FileUtils.ATTACHMENT_DOWNLOAD_FINISH);
        intentFilter.addAction(FileUtils.ATTACHMENT_DOWNLOAD_UPDATA);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(FileUtils.ATTACHMENT_DOWNLOAD_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = AttachmentHandler.DOWNLOAD_FINISH;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }else if(intent.getAction().equals(FileUtils.ATTACHMENT_DOWNLOAD_FAIL))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = AttachmentHandler.DOWNLOAD_FAIL;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }else if(intent.getAction().equals(FileUtils.ATTACHMENT_DOWNLOAD_UPDATA))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = AttachmentHandler.DOWNLOAD_UPTATE;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
    }
}
