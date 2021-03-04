package intersky.chat.bus;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Contacts;
import intersky.chat.ContactManager;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class ChatBusObject extends BusObject {
    public ChatBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if (TextUtils.equals(bizName, "chat/getContactName")) {
            return ContactManager.mContactManager.mOrganization.getContactName((String) var3[0],context);
        }
        else if (TextUtils.equals(bizName, "chat/getContactId")) {
            return ContactManager.mContactManager.mOrganization.getContactId((String) var3[0]);
        }
        else if (TextUtils.equals(bizName, "chat/setContactCycleHead")) {
            ContactManager.setContactCycleHead((TextView) var3[0],(Contacts) var3[1]);
            return null;
        }
        else if (TextUtils.equals(bizName, "chat/getContacts")) {
            ContactManager.mContactManager.getContacts((String ) var3[0],(ArrayList<Contacts>) var3[1]);
            return null;
        }
        else if (TextUtils.equals(bizName, "chat/setUnderlineContacts")) {
            ContactManager.mContactManager.setUnderlineContacts(context,(String ) var3[0],(String) var3[1]);
            return null;
        }
        else if (TextUtils.equals(bizName, "chat/selectListContact")) {
            ContactManager.mContactManager.selectListContact(context,(ArrayList<Contacts> ) var3[0],(String) var3[1],(String) var3[2],(boolean) var3[3]);
            return null;
        }
        else if (TextUtils.equals(bizName, "chat/selectListAddContact")) {
            ContactManager.mContactManager.selectListAddContact(context,(ArrayList<Contacts> ) var3[0],(String) var3[1],(String) var3[2],(boolean) var3[3],(ArrayList<Contacts> ) var3[4]);
            return null;
        }
        else if (TextUtils.equals(bizName, "chat/setContacts")) {
            ContactManager.mContactManager.setContacts(context,ContactManager.mContactManager.mOrganization.allContacts,ContactManager.mContactManager.mOrganization.allContactsHead
                    ,(String) var3[0],(String) var3[1]);
            return null;
        }
        else if (TextUtils.equals(bizName, "chat/setContactsUnderline")) {
            ContactManager.mContactManager.setContacts(context,ContactManager.mContactManager.mOrganization.underLineContacts,ContactManager.mContactManager.mOrganization.underLineContactsHead
                    ,(String) var3[0],(String) var3[1]);
            return null;
        }
        else if (TextUtils.equals(bizName, "chat/mselectitems")) {
            return ContactManager.mContactManager.mOrganization.mselectitems;
        }
        else if (TextUtils.equals(bizName, "chat/getMemberIds")) {
            return ContactManager.mContactManager.getMemberIds((ArrayList<Contacts> ) var3[0]);
        }
        else if (TextUtils.equals(bizName, "chat/getAccountId")) {
            return ContactManager.mContactManager.mAccount.mRecordId;
        }
        else if (TextUtils.equals(bizName, "chat/getContactItem")) {
            return ContactManager.mContactManager.mOrganization.getContact((String) var3[0],context);
        }
        else if (TextUtils.equals(bizName, "chat/startContactDetial")) {
            ContactManager.mContactManager.startContactDetial(context, (Contacts) var3[0]);
            return null;
        }
        return null;
    }
}
