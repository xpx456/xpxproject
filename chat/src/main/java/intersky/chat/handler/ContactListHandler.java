package intersky.chat.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.R;
import intersky.chat.view.activity.ContactsListActivity;
import intersky.xpxnet.net.NetObject;

public class ContactListHandler extends Handler {


    public final static int DOWNLOAD_FIAL = 30500;
    public final static int DOWNLOAD_UPDATA = 30501;
    public final static int DOWNLOAD_FINISH = 30502;
    public final static int ADD_MESSAGE = 30503;
    public final static int SET_PIC = 30504;
    public ContactsListActivity theActivity;

    public ContactListHandler(ContactsListActivity theActivity) {
        this.theActivity = theActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {

        }

    }
}
