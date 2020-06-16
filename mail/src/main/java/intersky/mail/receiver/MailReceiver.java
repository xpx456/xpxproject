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


public class MailReceiver extends BaseReceiver {

    public Handler mHandler;

    public MailReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(MailManager.ACTION_GET_MAIL_HIT);
        intentFilter.addAction(MailManager.ACTION_GET_MAIL_BOX_FINISH);
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
    }
}
