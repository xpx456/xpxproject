package intersky.conversation.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Register;
import intersky.conversation.ConversationManager;
import intersky.conversation.RegisterOrder;
import intersky.conversation.database.DBHelper;

public class ConversationCollectMap {

    public int bastId = 1;
    public HashMap<String, ConversationTypeMap> conversationTypeMap = new HashMap<String, ConversationTypeMap>();
    public HashMap<String, ConversationDetialMap> conversationDetialHashMap = new HashMap<String,ConversationDetialMap>();
    public ArrayList<Conversation> showConversations = new ArrayList<Conversation>();
    public HashMap<String,Conversation> showHash = new HashMap<String,Conversation>();
    public HashMap<String, Register> registerHashMap = new HashMap<String,Register>();
    public HashMap<String,Integer> nId = new HashMap<String,Integer>();

    public void add(Register register) {
        if(!registerHashMap.containsKey(register.typeName))
        {
            registerHashMap.put(register.typeName,register);
            if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
            {
                conversationTypeMap.put(register.typeName,new ConversationTypeMap(register.typeName,register.registerType,register.mPriority));
            }
            else
            {
                conversationDetialHashMap.put(register.typeName,new ConversationDetialMap(register.typeName,register.registerType,register.mPriority));
            }
        }
    }

    public Conversation getConversationByMsgId(String type,String id) {
        ConversationTypeMap conversationTypeMap = this.conversationTypeMap.get(type);
        if(conversationTypeMap != null)
        {
            return conversationTypeMap.getConversationByMsgid(id);
        }
        else
        {
            return null;
        }
    }

    public Conversation getConversationType(Conversation conversation) {
        ConversationTypeMap conversationTypeMap = this.conversationTypeMap.get(conversation.mType);
        return conversationTypeMap.getConversation(conversation);
    }

    public Conversation getConversationDetial(Conversation conversation) {
        ConversationDetialMap conversationDetialMap = this.conversationDetialHashMap.get(conversation.mType);
        return conversationDetialMap.getConversation(conversation);
    }

    public Object get(String name) {
        Register register = registerHashMap.get(name);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE) {
           return conversationTypeMap.get(name);
        }
        else
        {
            return conversationDetialHashMap.get(name);
        }
    }

    public Register getRegister(String name) {
        Register register = registerHashMap.get(name);
        return register;
    }

    public void addConversationDb(Conversation info) {
        Register register = ConversationManager.getInstance().conversationAll.getRegister(info.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
        {
            addTypeConversation(info);
        }
        else
        {
            addDetialConversation(info);
        }
        updataOrder();
    }

    public void addConversation(Conversation info) {
        Register register = ConversationManager.getInstance().conversationAll.getRegister(info.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
        {
            addTypeConversation(info);
        }
        else
        {
            addDetialConversation(info);
        }
        DBHelper.getInstance().addConversation(info);
        updataOrder();
    }

    public void updata(Conversation info) {
        Register register = ConversationManager.getInstance().conversationAll.getRegister(info.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
        {
            updataTypeConversation(info);
        }
        else
        {
            updataDetialConversation(info);
        }
        DBHelper.getInstance().upDataConversation(info);
        updataOrder();
    }

    public void delete(Conversation info) {
        Register register = ConversationManager.getInstance().conversationAll.getRegister(info.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
        {
            deleteTypeConversation(info);
        }
        else
        {
            deleteDetialConversation(info);
        }
        DBHelper.getInstance().deleteConversation(info);
        updataOrder();
    }

    public void remove(Conversation info) {
        Register register = ConversationManager.getInstance().conversationAll.getRegister(info.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
        {
            removeTypeConversation(info);
        }
        else
        {
            removeDetialConversation(info);
        }
        updataOrder();
    }

    public void read(Conversation info) {
        Register register = ConversationManager.getInstance().conversationAll.getRegister(info.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_DETIAL) {
            readDetialConversation(info);
        }
        else
        {
            readConversation(info);
        }
        updataOrder();
    }

    public void updataOrder() {
        Collections.sort(showConversations,new RegisterOrder());
    }

    public int getHit() {
        int count = 0;
        for(int i = 0 ; i < showConversations.size() ; i++)
        {
            count += showConversations.get(i).mHit;
        }
        return count;
    }

    public int getHit(String type) {
        if(conversationTypeMap.containsKey(type))
        {
            return conversationTypeMap.get(type).getHit();
        }
        else if(conversationDetialHashMap.containsKey(type))
        {
            return conversationDetialHashMap.get(type).getHit();
        }
        else
        {
            return 0;
        }
    }

    public int getHit(String type,String detial) {
        if(conversationDetialHashMap.containsKey(type))
        {
            return conversationDetialHashMap.get(type).getHit();
        }
        else
        {
            return 0;
        }
    }

    private void addTypeConversation(Conversation info) {
        ConversationTypeMap hash = conversationTypeMap.get(info.mType);
        hash.add(info);
        if(!showHash.containsKey(info.mType))
        {
            showConversations.add(hash.conversationHead);
            showHash.put(info.mType,hash.conversationHead);
        }
    }

    private void addDetialConversation(Conversation info) {
        ConversationDetialMap hash = conversationDetialHashMap.get(info.mType);
        Conversation head = hash.add(info);
        if(head != null)
        {
            showConversations.add(head);
            showHash.put(info.detialId,head);
        }
    }

    private void updataTypeConversation(Conversation info) {
        ConversationTypeMap hash = conversationTypeMap.get(info.mType);
        hash.updata(info);
    }

    private void updataDetialConversation(Conversation info) {
        ConversationDetialMap hash = conversationDetialHashMap.get(info.mType);
        hash.updata(info);
    }

    private void deleteTypeConversation(Conversation info) {
        ConversationTypeMap hash = conversationTypeMap.get(info.mType);
        if(hash.delete(info))
        {
            Conversation head = showHash.get(info.mType);
            showHash.remove(info.mType);
            showConversations.remove(head);
        }
    }

    private void deleteDetialConversation(Conversation info) {
        ConversationDetialMap hash = conversationDetialHashMap.get(info.mType);
        if(hash.delete(info)) {
            Conversation head = showHash.get(info.detialId);
            showHash.remove(info.detialId);
            showConversations.remove(head);
        }
    }

    private void removeTypeConversation(Conversation info) {
        conversationTypeMap.remove(info.mType);
        Conversation head = showHash.get(info.mType);
        showHash.remove(info.mType);
        showConversations.remove(head);
        DBHelper.getInstance().deleteConversationByType(info.mType);
    }

    private void removeDetialConversation(Conversation info) {
        Conversation head = showHash.get(info.detialId);
        ConversationDetialMap hash = conversationDetialHashMap.get(info.mType);
        hash.remove(info);
        showConversations.remove(head);
        DBHelper.getInstance().deleteConversationByTypeDetial(info.mType,info.detialId);
    }

    private void readDetialConversation(Conversation info) {
        ConversationDetialMap hash = conversationDetialHashMap.get(info.mType);
        hash.readall(info);
        DBHelper.getInstance().readConversationByTypeDetial(info.mType,info.detialId);
    }

    private void readConversation(Conversation info) {
        ConversationTypeMap hash = conversationTypeMap.get(info.mType);
        hash.read(info);
        DBHelper.getInstance().readConversationByTypeDetial(info.mType,info.detialId);
    }
}
