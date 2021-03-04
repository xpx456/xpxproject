package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.ImPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactsListActivity;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class ContactsListHandler extends Handler {

    public ContactsListActivity theActivity;
    public static final int UPDATA_MAIN_FRIENDS = 300400;
    public static final int UPDATA_FRIENDS_CHANGE = 300401;
    public ContactsListHandler(ContactsListActivity mContactsListActivity) {
        theActivity = mContactsListActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ContactsAsks.FRIEND_LIST_RESULT:
                break;
            case ContactsAsks.CONTACTS_MEETING_ATT_MY_RESULT:
            case ContactsAsks.CONTACTS_MEETING_ATT_WANT_RESULT:
            case ContactsAsks.CONTACTS_MEETING_ATT_ALL_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.paseMeetAttContacts(theActivity, (NetObject) msg.obj,theActivity.listcontacts,theActivity.contactdetial);
                theActivity.contactsAdapter.notifyDataSetChanged();
                break;
            case ConversationAsks.MESSAGE_SEND_RESULT:
                theActivity.waitDialog.hide();
                if(ImPrase.praseData((NetObject) msg.obj) == false)
                {
                    AppUtils.showMessage(theActivity,theActivity.getString(intersky.chat.R.string.im_send_file));
                }
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                break;
            case UPDATA_FRIENDS_CHANGE:
                if(theActivity.getIntent().hasExtra("type") == false)
                theActivity.contactsAdapter.notifyDataSetChanged();
                if(theActivity.getIntent().hasExtra("select") == true) {
//                    theActivity.contactsSelectAdapter.notifyDataSetChanged();
//                    theActivity.contactsSelectSearchAdapter.notifyDataSetChanged();
                    if(theActivity.searchViewLayout.getText().length() > 0)
                    theActivity.mContactsListPresenter.updataContactView(false);
                    else
                        theActivity.mContactsListPresenter.updataContactView(true);
                }
                theActivity.waitDialog.hide();
                break;
            case NetUtils.TOKEN_ERROR:
                if(BigwinerApplication.mApp.mAccount.islogin == true) {
                    BigwinerApplication.mApp.logout(BigwinerApplication.mApp.mAppHandler,BigwinerApplication.mApp.appActivityManager.getCurrentActivity());
                    NetUtils.getInstance().cleanTasks();
                }
                break;
        }

    }
}
