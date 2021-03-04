package intersky.mail.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.mail.MailManager;
import intersky.mail.handler.MailEditHandler;
import intersky.mail.view.activity.MailEditActivity;
import intersky.appbase.BaseReceiver;
import intersky.mail.view.activity.MailLableActivity;


public class MailEditReceiver extends BaseReceiver {

    public Handler mHandler;

    public MailEditReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(MailManager.EVENT_MAIL_DOWNLOAD_FAIL);
        intentFilter.addAction(MailManager.EVENT_MAIL_FINISH_DOWNLOAD);
        intentFilter.addAction(MailManager.EVENT_MAIL_UPADA_DOWNLOAD);
        intentFilter.addAction(MailEditActivity.ACTION_MAIL_VIDEO_SELECT);
        intentFilter.addAction(MailEditActivity.ACTION_ADD_MAIL_CONTACT);
        intentFilter.addAction(MailEditActivity.ACTION_MAIL_PHOTO_SELECT);
        intentFilter.addAction(MailLableActivity.ACTION_SET_LABLE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(MailManager.EVENT_MAIL_DOWNLOAD_FAIL)) {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailEditHandler.ATTACHMENT_DOWNLOAD_FAIL;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(MailManager.EVENT_MAIL_FINISH_DOWNLOAD)) {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailEditHandler.ATTACHMENT_DOWNLOAD_FINISH;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(MailManager.EVENT_MAIL_UPADA_DOWNLOAD)) {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailEditHandler.ATTACHMENT_DOWNLOAD_UPDATA;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(MailEditActivity.ACTION_ADD_MAIL_CONTACT)) {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailEditHandler.ADD_MAIL_CONTACT;
                msg.arg1 = intent.getIntExtra("ltype", MailEditActivity.CONTENT_TYPE_SHOUJIAN);
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(MailEditActivity.ACTION_MAIL_VIDEO_SELECT)) {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MailEditHandler.ADD_MAIL_ATTACHMENT;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(MailEditActivity.ACTION_MAIL_PHOTO_SELECT)) {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MailEditHandler.ADD_MAIL_ATTACHMENT;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(MailLableActivity.ACTION_SET_LABLE)) {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MailEditHandler.SET_LABLE;
                mHandler.sendMessage(msg);
            }
        }
    }
}
