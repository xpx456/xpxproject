package intersky.chat.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.chat.view.activity.ContactsDetialActivity;

public class ContactsDetialHandler extends Handler {

    public final static int PERMISSION_CALL_PHONE_CONTACTS = 40400;
    public final static int PERMISSION_SEND_SMS_CONTACTS = 40401;

    public ContactsDetialActivity theActivity;

    public ContactsDetialHandler(ContactsDetialActivity mContactsDetialActivity) {
        theActivity = mContactsDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case PERMISSION_CALL_PHONE_CONTACTS:
                theActivity.mContactsDetialPresenter.tel();
                break;
            case PERMISSION_SEND_SMS_CONTACTS:
                theActivity.mContactsDetialPresenter.msn();
                break;
        }

    }
}
