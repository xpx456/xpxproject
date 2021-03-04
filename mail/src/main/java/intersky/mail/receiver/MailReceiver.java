package intersky.mail.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.mail.MailManager;
import intersky.mail.asks.MailAsks;
import intersky.mail.handler.MailHandler;
import intersky.appbase.BaseReceiver;
import intersky.mail.view.activity.MailActivity;


public class MailReceiver extends BaseReceiver {

    public Handler mHandler;

    public MailReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(MailManager.ACTION_GET_MAIL_HIT);
        intentFilter.addAction(MailManager.ACTION_GET_MAIL_BOX_FINISH);
        intentFilter.addAction(MailManager.ACTION_GET_MAIL_OTHERBOX_FINISH);
        intentFilter.addAction(MailManager.ACTION_MAIL_ALL_SEND_UPDATE);
        intentFilter.addAction(MailManager.ACTION_MAIL_LABLE_UPDATE);
        intentFilter.addAction(MailManager.ACTION_MAIL_FILE_UPDATE);
        intentFilter.addAction(MailActivity.ACTION_UPDATA_MAILS);
        intentFilter.addAction(MailActivity.ACTION_UPDATA_ALL_MAILS);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(MailManager.ACTION_GET_MAIL_HIT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailHandler.UPDATE_MAIL_HIT;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }else if(intent.getAction().equals(MailManager.ACTION_GET_MAIL_BOX_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailHandler.UPDATE_MAIL_BOX;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(MailManager.ACTION_GET_MAIL_OTHERBOX_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailHandler.UPDATE_MAIL_OTHER_BOX;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(MailManager.ACTION_MAIL_ALL_SEND_UPDATE))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailHandler.UPDATA_ALL_SEND;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(MailManager.ACTION_MAIL_LABLE_UPDATE))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailHandler.UPDATA_ALL_LABLE;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(MailManager.ACTION_MAIL_FILE_UPDATE))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailHandler.UPDATA_ALL_LABLE;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(MailActivity.ACTION_UPDATA_MAILS))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailHandler.UPDATA_MAILS;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(MailActivity.ACTION_UPDATA_ALL_MAILS))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailHandler.UPDATA_MAILS_ALL;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
    }
}
