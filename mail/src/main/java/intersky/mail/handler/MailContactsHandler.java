package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.MailType;
import intersky.mail.prase.MailPrase;
import intersky.mail.presenter.MailPresenter;
import intersky.mail.view.activity.MailActivity;
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
            case MailAsks.MAIL_CUSTOMS_SUCCESS:
                theActivity.waitDialog.hide();
                NetObject netObject4 = (NetObject) msg.obj;
                MailContact mailboxobj = (MailContact) netObject4.item;
                MailPrase.praseCustoms2((NetObject) msg.obj);
                theActivity.mMailContactsPresenter.now = mailboxobj;
                theActivity.mMailContactsPresenter.contacts.clear();
                theActivity.mMailContactsPresenter.contacts.addAll(mailboxobj.mContacts);
                theActivity.mMailContactsPresenter.mContactAdapter.notifyDataSetChanged();
                theActivity.mMailContactsPresenter.doSearch(theActivity.searchView.getText());
                theActivity.mMailContactsPresenter.updataContactView();
                break;
            case MailAsks.MAIL_FENGFA_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseMove((NetObject) msg.obj))
                {
                    theActivity.finish();
                }
                else
                {
                    AppUtils.showMessage(theActivity,"fail");
                }
                break;
        }

    }
}
