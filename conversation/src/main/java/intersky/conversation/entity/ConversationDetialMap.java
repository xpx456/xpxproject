package intersky.conversation.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import intersky.appbase.ConversationOrderTimeAcs;
import intersky.appbase.ConversationOrderTimeDes;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Register;

public class ConversationDetialMap extends Register {

    public HashMap<String,ConversationTypeMap> conversationTypeMap = new HashMap<String,ConversationTypeMap>();
    public HashMap<String, Conversation> hashMapHeads = new  HashMap<String,Conversation>();
    public ArrayList<Conversation> showConversations = new ArrayList<Conversation>();
    public ConversationDetialMap(String typeNam, int registerType,int mPriority) {
        super(typeNam, registerType,mPriority);
    }

    public void orderByTime(Boolean des) {
        if(des)
        {
            Collections.sort(showConversations,new ConversationOrderTimeDes());
        }
        else
        {
            Collections.sort(showConversations,new ConversationOrderTimeAcs());
        }
    }

    public void updataOrder() {
        Collections.sort(showConversations,new ConversationOrderTimeDes());
    }

    public Conversation getConversation(Conversation conversation) {
        ConversationTypeMap conversationTypeMap = this.conversationTypeMap.get(conversation.detialId);
        if(conversationTypeMap != null)
        {
            return conversationTypeMap.getConversation(conversation);
        }
        else
        {
            return null;
        }
    }

    public Conversation add(Conversation info) {
        ConversationTypeMap mtype = conversationTypeMap.get(info.detialId);
        if(mtype != null)
        {
            mtype.add(info);
            if(!hashMapHeads.containsKey(mtype.conversationHead.detialId))
            {
                hashMapHeads.put(mtype.conversationHead.detialId,mtype.conversationHead);
                showConversations.add(mtype.conversationHead);
                updataOrder();
                return  mtype.conversationHead;
            }
            else
            {
                updataOrder();
                return null;
            }
        }
        else
        {
            ConversationTypeMap hash = new ConversationTypeMap(info.detialId,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,this.mPriority);
            conversationTypeMap.put(info.detialId,hash);
            hash.add(info);
            showConversations.add(hash.conversationHead);
            hashMapHeads.put(hash.conversationHead.detialId,hash.conversationHead);
            updataOrder();
            return hash.conversationHead;
        }

    }

    public void updata(Conversation info)
    {
        ConversationTypeMap hash = conversationTypeMap.get(info.detialId);
        hash.add(info);
        updataOrder();
    }

    public boolean delete(Conversation info) {
        if(conversationTypeMap.containsKey(info.detialId))
        {
            if(conversationTypeMap.get(info.detialId).delete(info))
            {
                remove(info);
                return true;
            }
            return false;
        }
        return false;
    }

    public void remove(Conversation info) {
        Conversation conversation = hashMapHeads.get(info.detialId);
        if(conversation != null)
        {
            showConversations.remove(conversation);
            hashMapHeads.remove(conversation);
            conversationTypeMap.remove(info.detialId);
        }

    }

    public void read(Conversation info) {
        Conversation conversation = hashMapHeads.get(info.detialId);
        if(conversation != null)
        {
            ConversationTypeMap mTypeMap = conversationTypeMap.get(info.detialId);
            if(mTypeMap != null)
            {
                for(int i = 0 ; i < mTypeMap.showConversations.size() ; i++)
                {
                    mTypeMap.showConversations.get(i).isRead = true;
                }

            }
            conversation.mHit = 0;
        }

    }

    public void readall(Conversation info) {
        Conversation conversation = hashMapHeads.get(info.detialId);
        if(conversation != null)
        {
            ConversationTypeMap mTypeMap = conversationTypeMap.get(info.detialId);
            if(mTypeMap != null)
            {
                mTypeMap.readAll();
            }
            conversation.mHit = 0;
        }

    }

    public int getHit() {
        int count = 0;
        for(int i = 0 ; i < showConversations.size() ; i++) {
            count += showConversations.get(i).mHit;
        }
        return count;
    }
}
