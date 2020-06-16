package intersky.mail.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.mail.MailManager;
import intersky.mail.handler.MailFengFaHandler;
import intersky.appbase.BaseReceiver;

public class MailFenFaReceiver extends BaseReceiver {

    public Handler mHandler;

    public MailFenFaReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(MailManager.ACTION_MAIL_SET_FENFA_PERSON);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(MailManager.ACTION_MAIL_SET_FENFA_PERSON))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailFengFaHandler.EVENT_SET_FENFA_PERSON;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
    }

}
