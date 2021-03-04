package intersky.mail.handler;

import android.content.Intent;
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
                    int b = MailPrase.addMailBoxDataCloud((NetObject) msg.obj);
                    if(b == -1 || b == 0){
                        AppUtils.sendSampleBroadCast(MailManager.getInstance().context,MailManager.ACTION_GET_MAIL_BOX_FINISH);
                    }
                    else
                    {
                        AppUtils.sendSampleBroadCast(MailManager.getInstance().context,MailManager.ACTION_GET_MAIL_OTHERBOX_FINISH);
                    }
                    MailManager.getInstance().getReadCount();
                }

                break;
            case MailAsks.MAIL_READ_COUNT_SUCCESS:
                if(MailManager.getInstance().account.iscloud == false)
                MailPrase.getMailHint((NetObject) msg.obj);
                else
                    MailPrase.getMailHintCloud((NetObject) msg.obj);
                AppUtils.sendSampleBroadCast(MailManager.getInstance().context,MailManager.ACTION_GET_MAIL_HIT);
                break;
            case MailAsks.MAIL_UNDERLINES_SUCCESS:
                if(MailManager.getInstance().account.iscloud)
                {
                    MailPrase.addUserc((NetObject) msg.obj);
                }
                else
                {
                    MailPrase.addUser((NetObject) msg.obj);
                }
                AppUtils.sendSampleBroadCast(MailManager.getInstance().context,MailManager.ACTION_GET_MAIL_UNDERLINE_FINISH);
                break;
            case MailAsks.MAIL_LABLE_SUCCESS:
                if(MailManager.getInstance().account.iscloud == false)
                MailPrase.addLData((NetObject) msg.obj);
                else
                {
                    MailPrase.addLDatac((NetObject) msg.obj);
                    Intent intent = new Intent(MailManager.ACTION_MAIL_LABLE_UPDATE);
                    MailManager.getInstance().context.sendBroadcast(intent);
                }

                break;
            case MailAsks.MAIL_GROP_LABLE_SUCCESS:
                MailPrase.addGLData((NetObject) msg.obj);
                Intent intent = new Intent(MailManager.ACTION_MAIL_LABLE_UPDATE);
                MailManager.getInstance().context.sendBroadcast(intent);
                break;
            case MailAsks.MAIL_MAIL_SHIP:
                MailPrase.praseRship((NetObject) msg.obj);
                break;
            case MailAsks.MAIL_MAIL_FILES:
                if(MailManager.getInstance().account.iscloud == true)
                {
                    MailPrase.praseMailFiles((NetObject) msg.obj);
                    Intent intent10 = new Intent(MailManager.ACTION_MAIL_FILE_UPDATE);
                    MailManager.getInstance().context.sendBroadcast(intent10);
                }
                break;
            case MailAsks.MAIL_PUSH_CHECK_SUCCESS:
                if(MailManager.getInstance().account.iscloud == true)
                {
                    MailPrase.praseAllSend((NetObject) msg.obj);
                    MailManager.getInstance().getReadCount();
                    Intent intent9 = new Intent(MailManager.ACTION_MAIL_ALL_SEND_UPDATE);
                    MailManager.getInstance().context.sendBroadcast(intent9);
                }
                break;
        }
    }

}
