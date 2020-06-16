package intersky.mail.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.mail.MailManager;
import intersky.mail.handler.MailListHandler;
import intersky.appbase.BaseReceiver;


public class MailListReceiver extends BaseReceiver {

    public Handler mHandler;

    public MailListReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(MailManager.ACTION_MAIL_LIST_UPDATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(MailManager.ACTION_MAIL_LIST_UPDATE))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailListHandler.EVENT_LIST_UPDATE;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
    }
}
