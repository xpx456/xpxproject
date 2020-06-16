package intersky.conversation.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import intersky.appbase.ConversationOrderTimeAcs;
import intersky.appbase.ConversationOrderTimeDes;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Register;
import intersky.conversation.database.DBHelper;

public class ConversationTypeMap extends Register {

    public HashMap<String, Conversation> conversationTypeMap = new HashMap<String,Conversation>();
    public HashMap<String, Conversation> conversationMsgidTypeMap = new HashMap<String,Conversation>();
    public ArrayList<Conversation> showConversations = new ArrayList<Conversation>();
    public ArrayList<Conversation> readConversations = new ArrayList<Conversation>();
    public ArrayList<Conversation> unreadConversations = new ArrayList<Conversation>();
    public Conversation conversationHead = null;

    public ConversationTypeMap(String typeNam, int registerType,int mPriority) {
        super(typeNam, registerType,mPriority);
    }

    public void orderByTime(Boolean des) {
        if(des)
        {
            Collections.sort(showConversations,new ConversationOrderTimeDes());
            Collections.sort(readConversations,new ConversationOrderTimeDes());
            Collections.sort(unreadConversations,new ConversationOrderTimeDes());
        }
        else
        {
            Collections.sort(showConversations,new ConversationOrderTimeAcs());
            Collections.sort(readConversations,new ConversationOrderTimeAcs());
            Collections.sort(unreadConversations,new ConversationOrderTimeAcs());
        }
    }

    public void updataHead() {
        if(showConversations.size() == 0)
        {
            conversationHead = null;
        }
        else
        {
            if(conversationHead == null)
            {
                conversationHead = new Conversation();
                conversationHead.mPriority = this.mPriority;
            }
            conversationHead.copy(showConversations.get(0));
            conversationHead.mHit = unreadConversations.size();
        }

    }

    public Conversation getConversation(Conversation conversation) {
        return conversationTypeMap.get(conversation.mRecordId);
    }

    public Conversation getConversationByMsgid(String msgid) {
        return conversationMsgidTypeMap.get(msgid);
    }

    public void updataOrder() {
        orderByTime(true);
    }

    public void add(Conversation info) {
        if(conversationTypeMap.containsKey(info.mRecordId))
        {
            updata(info);
        }
        else
        {
            conversationTypeMap.put(info.mRecordId,info);
            if(info.messageId.length() > 0)
            {
                conversationMsgidTypeMap.put(info.messageId,info);
            }
            showConversations.add(0,info);
            if(info.isRead)
            {
                readConversations.add(info);
            }
            else
            {
                unreadConversations.add(info);
            }
            updataHead();
        }
    }

    public void updata(Conversation info) {
        Conversation temp = conversationTypeMap.get(info.mRecordId);
        if(temp.isRead == false && info.isRead == true)
        {
            readConversations.add(temp);
            unreadConversations.remove(temp);
            updataHead();
        }
        temp.copy(info);
        updataOrder();
    }

    public boolean delete(Conversation info) {
        showConversations.remove(info);
        readConversations.remove(info);
        unreadConversations.remove(info);
        conversationMsgidTypeMap.remove(info);
        conversationTypeMap.remove(info);
        updataHead();
        if(showConversations.size() == 0)
        {
            return true;
        }
        else {
            return false;
        }
    }

    public void read(Conversation info) {
        for(int i = 0 ; i < unreadConversations.size(); i++)
        {
            if(unreadConversations.get(i).detialId.equals(info.detialId))
            {
                Conversation conversation = unreadConversations.get(i);
                conversation.isRead = true;
                unreadConversations.remove(conversation);
                readConversations.add(conversation);
                i--;

            }
        }
        updataHead();
        updataOrder();
    }

    public void readAll() {
        readConversations.addAll(unreadConversations);
        unreadConversations.clear();
        updataHead();
        updataOrder();
    }

    public int getHit() {
        return conversationHead.mHit;
    }
}
