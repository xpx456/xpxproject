package intersky.mail.handler;

import android.os.Handler;
import android.os.Message;

import intersky.mail.MailManager;
import intersky.mail.asks.MailAsks;
import intersky.mail.prase.MailPrase;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;

//00
public class InitHandler extends Handler {



    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case MailAsks.MAIL_MAILBOX_SUCCESS:
                if(MailManager.getInstance().account.iscloud == false)
                {
                    MailPrase.addMailBoxData((NetObject) msg.obj);
                }
                else
                {
                    MailPrase.addMailBoxDataCloud((NetObject) msg.obj);
                    MailManager.getInstance().getReadCount();
                }
                AppUtils.sendSampleBroadCast(MailManager.getInstance().context,MailManager.ACTION_GET_MAIL_BOX_FINISH);
                break;
            case MailAsks.MAIL_READ_COUNT_SUCCESS:
                if(MailManager.getInstance().account.iscloud == false)
                MailPrase.getMailHint((NetObject) msg.obj);
                else
                    MailPrase.getMailHintCloud((NetObject) msg.obj);
                AppUtils.sendSampleBroadCast(MailManager.getInstance().context,MailManager.ACTION_GET_MAIL_HIT);
                break;
            case MailAsks.MAIL_UNDERLINES_SUCCESS:
                MailPrase.addUser((NetObject) msg.obj);
                AppUtils.sendSampleBroadCast(MailManager.getInstance().context,MailManager.ACTION_GET_MAIL_UNDERLINE_FINISH);
                break;
        }
    }

}
