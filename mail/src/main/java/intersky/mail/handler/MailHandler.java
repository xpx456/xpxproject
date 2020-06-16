package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.mail.MailManager;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.MailContact;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailActivity;
import intersky.xpxnet.net.NetObject;

//04
public class MailHandler extends Handler {

    public static final int UPDATE_MAIL_HIT = 3200400;
    public static final int UPDATE_MAIL_BOX = 3200401;

    public MailActivity theActivity;
    public MailHandler(MailActivity mMailActivity) {
        theActivity = mMailActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case MailAsks.MAIL_SETUSER_SUCCESS:
                NetObject netObject = (NetObject) msg.obj;
                MailManager.getInstance().mSelectUser = (MailContact) netObject.item;
                MailManager.getInstance().getMailBox();
                break;
            case MailAsks.MAIL_SETUSER_FAIL:
                break;
            case MailAsks.MAIL_MAILBOX_FAIL:
                break;
            case UPDATE_MAIL_HIT:
                if(MailManager.getInstance().account.iscloud == false)
                theActivity.mMailPresenter.setnewcountView();
                else
                    theActivity.mMailPresenter.setnewcountViewCloud();
                break;
            case UPDATE_MAIL_BOX:
                if(MailManager.getInstance().account.iscloud == false)
                theActivity.mMailPresenter.checkMailBoxView();
                else
                    theActivity.mMailPresenter.checkMailBoxViewc();
                break;
            case MailAsks.MAIL_LIST_SUCCESS:
                if(MailManager.getInstance().account.iscloud == false)
                MailPrase.praseMail((NetObject) msg.obj,theActivity.mMailItems);
                else
                    MailPrase.praseMail((NetObject) msg.obj,theActivity.mMailItems,0);
                break;
        }

    }
}