package intersky.conversation.bus;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.conversation.ConversationManager;
import intersky.conversation.NotifictionManager;
import intersky.conversation.database.DBHelper;
import intersky.conversation.database.BigWinerDBHelper;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class ConversationBusObject extends BusObject {
    public ConversationBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {

        if (TextUtils.equals(bizName, "conversation/scanConversationMessage")) {
            Contacts contacts = (Contacts) var3[0];
//            return DBHelper.getInstance(context).scanConversationMessage(contacts.mRecordid);
        }
        else if (TextUtils.equals(bizName, "conversation/scanOurwinConversationMessage")) {
            Contacts contacts = (Contacts) var3[0];
            return BigWinerDBHelper.getInstance(context).scanConversationMessage(contacts.mRecordid);
        }
        else if (TextUtils.equals(bizName, "conversation/addConversation")) {
//            ConversationManager.getInstance().addConversation((Conversation) var3[0],(Integer) var3[1]);
            return null;
        }
        else if (TextUtils.equals(bizName, "conversation/updataConversation")) {
//            DBHelper.getInstance(context).addConversation((Conversation) var3[0]);
            return null;
        }
        else if (TextUtils.equals(bizName, "conversation/creatDownloadNotifiction")) {
            NotifictionManager.mNotifictionManager.creatDownloadNotifiction(context,(int)var3[0],(String)var3[1],(String)var3[2],(String)var3[3],(int)var3[4],(int)var3[5]);
            return null;
        }
        else if (TextUtils.equals(bizName, "conversation/creatTipNotifiction")) {
            NotifictionManager.mNotifictionManager.creatTipNotifiction(context,(int)var3[0],(String)var3[1],(String)var3[2],(String)var3[3]);
            return null;
        }
        else if (TextUtils.equals(bizName, "conversation/removeNotifiction")) {
            NotifictionManager.mNotifictionManager.removeNotifiction((int)var3[0]);
            return null;
        }
        else if (TextUtils.equals(bizName, "conversation/setdetialread")) {
            ConversationManager.mConversationManager.doRead((String)var3[0],(String)var3[1]);
            return null;
        }
        else if (TextUtils.equals(bizName, "conversation/setdetialdelete")) {
//            ConversationManager.mConversationManager.doDeleteCoversation((Intent)var3[0]);
            return null;
        }
        return null;
    }
}
