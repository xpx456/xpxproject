package intersky.mail.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.mail.MailManager;
import intersky.mail.handler.MailEditHandler;
import intersky.mail.handler.MailShowHandler;
import intersky.mail.view.activity.MailEditActivity;


public class MaiShowReceiver extends BaseReceiver {

    public Handler mHandler;

    public MaiShowReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction("ATTACHMENT_SAVE_NET_FILE_SUCCESS");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("ATTACHMENT_SAVE_NET_FILE_SUCCESS")) {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MailShowHandler.SAVE_SUCCESS;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
    }
}
