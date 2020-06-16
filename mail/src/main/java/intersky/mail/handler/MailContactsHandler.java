package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.mail.asks.MailAsks;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailContactsActivity;
import intersky.xpxnet.net.NetObject;

//01
public class MailContactsHandler extends Handler {

    public MailContactsActivity theActivity;

    public MailContactsHandler(MailContactsActivity mMailContactsActivity) {
        theActivity = mMailContactsActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case MailAsks.MAIL_GET_USER_GUEST_SUCCESS:
                theActivity.waitDialog.hide();
                MailPrase.addOutGuestData((NetObject) msg.obj);
                theActivity.mMailContactsPresenter.updataContactView();
                break;
            case MailAsks.MAIL_GET_USER_GUEST_FAIL:
                break;
        }

    }
}
