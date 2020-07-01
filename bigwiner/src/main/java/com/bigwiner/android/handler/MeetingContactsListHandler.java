package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MeetingContactsListActivity;

import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//08

public class MeetingContactsListHandler extends Handler {

    public MeetingContactsListActivity theActivity;
    public static final int UPDATA_MAIN_FRIENDS = 300400;
    public MeetingContactsListHandler(MeetingContactsListActivity mMeetingContactsList) {
        theActivity = mMeetingContactsList;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ContactsAsks.CONTACTS_DATA_LIST_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.paseMeetDateContacts(theActivity, (NetObject) msg.obj,theActivity.candDateContacts,theActivity.dateDetial);
                theActivity.canAdapter.notifyDataSetChanged();
                break;
            case ContactsAsks.CONTACTS_APPLY_LIST_RESULT:
                theActivity.waitDialog.hide();
                NetObject netObject = (NetObject) msg.obj;
                if((int)netObject.item == 1)
                {
                    ContactsPrase.paseMeetApplyContacts(theActivity, (NetObject) msg.obj,theActivity.applyContacts,theActivity.applyDetial1);
                    theActivity.needAdapter.notifyDataSetChanged();
                }
                else
                {
                    ContactsPrase.paseMeetApplyContacts(theActivity, (NetObject) msg.obj,theActivity.readyContacts,theActivity.applyDetial2);
                    theActivity.readyAdapter.notifyDataSetChanged();
                }
                break;
            case ContactsAsks.CONTACTS_DATA_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    Contacts contacts = (Contacts) ((NetObject) msg.obj).item;
                    theActivity.candDateContacts.remove(contacts);
                    theActivity.canAdapter.notifyDataSetChanged();
                }
                break;
            case ContactsAsks.CONTACTS_APPLY_SET_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    theActivity.mMeetingContactsListPresenter.onHead();
                    theActivity.applyDetial1.reset();
                    theActivity.applyDetial2.reset();
                    theActivity.readyContacts.clear();
                    theActivity.applyContacts.clear();
                    ContactsAsks.getMeetingMeaseContacts(theActivity,theActivity.mMeetingContactsListPresenter.mMeetingContactsListHandler
                            ,theActivity.meeting,2,theActivity.applyDetial2.pagesize,theActivity.applyDetial2.currentpage);
                    ContactsAsks.getMeetingMeaseContacts(theActivity,theActivity.mMeetingContactsListPresenter.mMeetingContactsListHandler
                            ,theActivity.meeting,1,theActivity.applyDetial1.pagesize,theActivity.applyDetial1.currentpage);
                }
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
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
