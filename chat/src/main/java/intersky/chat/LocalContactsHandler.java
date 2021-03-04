package intersky.chat;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.PermissionCode;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;

public class LocalContactsHandler extends Handler {

    public final static int INIT_PHONE_CONTACTS = 30200;

    public Context baseActivity;

    public LocalContactsHandler(Context baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case INIT_PHONE_CONTACTS:
                ContactManager.mContactManager.addPhoneContacts((Contacts) msg.obj);
                AppUtils.sendSampleBroadCast(baseActivity,ContactManager.GET_LOCAL_CONTACTS_FINISH);
                break;
            case PermissionCode.PERMISSION_REQUEST_READ_CONTACTS:
                if (ContactManager.mContactManager.mOrganization.phoneContacts == null) {
                    PhoneContactsThread mPhoneContactsThread = new PhoneContactsThread(this,baseActivity, LocalContactsHandler.INIT_PHONE_CONTACTS);
                    mPhoneContactsThread.start();
                }
                break;
        }

    }
}
