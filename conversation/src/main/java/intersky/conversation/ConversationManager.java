package intersky.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.BaseReceiver;
import intersky.appbase.entity.Contacts;
import intersky.budge.shortcutbadger.ShortcutBadger;
import intersky.conversation.database.DBHelper;
import intersky.conversation.entity.Channel;
import intersky.conversation.entity.ConversationCollectMap;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Register;
import intersky.conversation.entity.ConversationTypeMap;
import intersky.conversation.handler.OaMessageHandler;
import intersky.conversation.view.activity.ConversationListActivity;
import intersky.xpxnet.net.Service;


public class ConversationManager {

    public static final String ACTION_UPDATA_CONVERSATION = "ACTION_UPDATA_CONVERSATION";
    public static final String ACTION_ADD_CONVERSATION = "ACTION_ADD_CONVERSATION";
    public static final String ACTION_ADD_CONVERSATION_LIST = "ACTION_ADD_CONVERSATION_LIST";
    public static final String ACTION_DELETE_CONVERSATION= "ACTION_DELETE_CONVERSATION";
    public static final String ACTION_REMOVE_CONVERSATION = "ACTION_REMOVE_CONVERSATION";
    public static final String ACTION_READ_CONVERSATION = "ACTION_READ_CONVERSATION";
    public Context context;
    public String dataBaseId;
    public String dataBaseName;
    public int dbversion;
    public ConversationCollectMap conversationAll = new ConversationCollectMap();
    public static volatile ConversationManager mConversationManager;
    public OaMessageHandler mOaMessageHandler = new OaMessageHandler();
    public ArrayList<Channel> channels = new ArrayList<Channel>();
    public HashMap<String,Channel> hashMapChannel = new HashMap<String,Channel>();
    public BaseReceiver baseReceiver;
    public Service service;
    public static ConversationManager init(Context context,String dataBaseName,int version) {

        if (mConversationManager == null) {
            synchronized (ConversationManager.class) {
                if (mConversationManager == null) {
                    mConversationManager = new ConversationManager(context,dataBaseName,version);
                }
                else
                {
                    mConversationManager.context = context;
                    mConversationManager.dbversion = version;
                    mConversationManager.dataBaseName = dataBaseName;
                    DBHelper.getInstance().init(context,dataBaseName,mConversationManager.dbversion);
                }
            }
        }
        return mConversationManager;
    }

    public ConversationManager(Context context,String dataBaseName,int version) {
        this.context = context;
        this.dbversion = version;
        this.dataBaseName = dataBaseName;
        DBHelper.getInstance().init(context,dataBaseName,dbversion);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setDataBaseId(String dbid) {
        conversationAll = new ConversationCollectMap();
        channels .clear();
        hashMapChannel.clear();
        this.dataBaseId = dbid;
    }

    public static ConversationManager getInstance() {
        return mConversationManager;
    }

    public void registerConversations(ArrayList<Register> registers,BaseReceiver baseReceiver,Context context) {
        this.baseReceiver = baseReceiver;
        if(conversationAll.registerHashMap.size() == 0)
        {
            for(int i = 0 ; i < registers.size() ; i++)
            {
                conversationAll.add(registers.get(i));
                Channel channel = new  Channel();
                channel.leave = registers.get(i).mNotivicationleavel;
                channel.moduleid = registers.get(i).moduleId;
                channel.id = dataBaseName+registers.get(i).typeName;
                channel.type = registers.get(i).registerType;
                channel.name = registers.get(i).typeRealName;
                channel.registername = registers.get(i).typeName;
                if(registers.get(i).registerType == Register.CONVERSATION_COLLECT_TYPE_BY_DETIAL)
                channel.messageid = getNotificationGropId(registers.get(i).typeName);
                else
                    channel.messageid = getNotificationId(registers.get(i).typeName);
                hashMapChannel.put(channel.id,channel);
                channels.add(channel);
                this.baseReceiver.intentFilter.addAction(channel.id);
            }
            this.baseReceiver.onReceive = notificationreceive;
            context.registerReceiver(baseReceiver,this.baseReceiver.intentFilter);
        }

    }

    public Conversation getConversationByMessageid(String type,String msgid) {
        return conversationAll.getConversationByMsgId(type,msgid);
    };

    public Conversation getConversation(Conversation conversation) {
        Register register = conversationAll.getRegister(conversation.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
        {
            return conversationAll.getConversationType(conversation);
        }
        else
        {
           return conversationAll.getConversationDetial(conversation);
        }
    }


    public Channel getChannel(String type) {
        return hashMapChannel.get(dataBaseName+type);
    }

    public void getDataBaseData() {
        DBHelper.getInstance().scanConversation(conversationAll);

    }

    public void doAdd(ArrayList<Conversation> conversations) {
        Message msg = new Message();
        msg.obj = conversations;
        msg.what = OaMessageHandler.ADD_CONVERSATION_LIST;
        mOaMessageHandler.sendMessage(msg);
    }

    public void doAdd(Conversation conversation) {
        Message msg = new Message();
        msg.obj = conversation;
        msg.what = OaMessageHandler.ADD_CONVERSATION;
        mOaMessageHandler.sendMessage(msg);

    }

    public void doUpdata(Conversation conversation) {
        Message msg = new Message();
        msg.obj = conversation;
        msg.what = OaMessageHandler.UPDTATA_CONVERSATION;
        mOaMessageHandler.sendMessage(msg);
    }

    public void doDelete(Conversation conversation) {
        Message msg = new Message();
        msg.obj = conversation;
        msg.what = OaMessageHandler.DELETE_CONVERSATION;
        mOaMessageHandler.sendMessage(msg);
    }

    public void doRemove(Conversation conversation) {
        Message msg = new Message();
        msg.obj = conversation;
        msg.what = OaMessageHandler.REMOVE_CONVERSATION;
        mOaMessageHandler.sendMessage(msg);
    }

    public void doRead(Conversation conversation) {
        Message msg = new Message();
        msg.obj = conversation;
        msg.what = OaMessageHandler.READ_CONVERSATION;
        mOaMessageHandler.sendMessage(msg);
    }

    public void doClick(Context context,Conversation conversation) {
        Intent intent = new Intent(context, ConversationListActivity.class);
        intent.putExtra("conversation",conversation);
        context.startActivity(intent);
    }

    public ArrayList<Conversation> getDetialList(Conversation conversation, boolean isread) {
        Register register = conversationAll.getRegister(conversation.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
        {
            if(isread)
            return conversationAll.conversationTypeMap.get(conversation.mType).readConversations;
            else
                return conversationAll.conversationTypeMap.get(conversation.mType).unreadConversations;
        }
        else
        {
            if(isread)
            return conversationAll.conversationDetialHashMap.get(conversation.mType).conversationTypeMap.get(conversation.detialId).readConversations;
            else
                return conversationAll.conversationDetialHashMap.get(conversation.mType).conversationTypeMap.get(conversation.detialId).unreadConversations;
        }
    }

    public ArrayList<Conversation> getDetialList(Conversation conversation) {
        Register register = conversationAll.getRegister(conversation.mType);
        if(register.registerType == Register.CONVERSATION_COLLECT_TYPE_BY_TYPE)
        {
            return conversationAll.conversationTypeMap.get(conversation.mType).showConversations;
        }
        else
        {
            return conversationAll.conversationDetialHashMap.get(conversation.mType).conversationTypeMap.get(conversation.detialId).showConversations;
        }
    }

    public ArrayList<Conversation> getDetialList(String type, String detialid) {
        if(!conversationAll.conversationDetialHashMap.get(type).conversationTypeMap.containsKey(detialid))
        {
            Register register = conversationAll.getRegister(type);
            ConversationTypeMap hash = new ConversationTypeMap(detialid,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,register.mPriority);
            conversationAll.conversationDetialHashMap.get(type).conversationTypeMap.put(detialid,hash);
        }
        return conversationAll.conversationDetialHashMap.get(type).conversationTypeMap.get(detialid).showConversations;

    }

    public Conversation getConversation(String type,String detialid,String recordid) {
        if(!conversationAll.conversationDetialHashMap.get(type).conversationTypeMap.containsKey(detialid))
        {
            Register register = conversationAll.getRegister(type);
            ConversationTypeMap hash = new ConversationTypeMap(detialid,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,register.mPriority);
            conversationAll.conversationDetialHashMap.get(type).conversationTypeMap.put(detialid,hash);
        }
        return conversationAll.conversationDetialHashMap.get(type).conversationTypeMap.get(detialid).conversationTypeMap.get(recordid);
    }


    public static void setBudge(Context context,int count) {
        ShortcutBadger.applyCount(context, count);
    }

    public  void setBudge() {
        ShortcutBadger.applyCount(context, conversationAll.getHit());
    }
    public BaseReceiver.Receive notificationreceive = new BaseReceiver.Receive(){

        @Override
        public void onReceive(Handler handler,Context context, Intent intent) {
            if(hashMapChannel.containsKey(intent.getAction())) {
                Channel channel = hashMapChannel.get(intent.getAction());
                if(conversationAll.getRegister(channel.registername).conversationFunctions != null)
                    conversationAll.getRegister(channel.registername).conversationFunctions.Asks(handler,intent);
            }
            else
            {
                if (hashMapChannel.containsKey(dataBaseName+intent.getStringExtra("type")))
                {
                    Channel channel = hashMapChannel.get(dataBaseName+intent.getStringExtra("type"));
                    if(conversationAll.getRegister(channel.registername).conversationFunctions != null)
                        conversationAll.getRegister(channel.registername).conversationFunctions.Open(intent);
                }

            }

        }
    };

    public void add(Conversation conversation) {
        conversationAll.addConversation(conversation);
        Intent intent = new Intent(ACTION_ADD_CONVERSATION);
        intent.putExtra("msg",conversation);
        context.sendBroadcast(intent);
        setBudge();
    }

    public void add(ArrayList<Conversation> conversations) {
        HashMap<String,ArrayList<Conversation>> addhash = new HashMap<String,ArrayList<Conversation>>();
        for(int i = 0 ; i < conversations.size(); i++)
        {
            conversationAll.addConversation(conversations.get(i));
            if(!addhash.containsKey(conversations.get(i).detialId))
            {
                ArrayList<Conversation> addlist = new ArrayList<Conversation>();
                addhash.put(conversations.get(i).detialId,addlist);
            }
            addhash.get(conversations.get(i).detialId).add(conversations.get(i));
        }

        for(HashMap.Entry<String, ArrayList<Conversation>> entry: addhash.entrySet())
        {
            ArrayList<Conversation> conversationadd = entry.getValue();
            Intent intent = new Intent(ACTION_ADD_CONVERSATION_LIST);
            intent.putParcelableArrayListExtra("msgs",conversationadd);
            context.sendBroadcast(intent);
        }
        setBudge();
    }

    public void updata(Conversation conversation) {
        conversationAll.updata(conversation);
        Intent intent = new Intent(ACTION_UPDATA_CONVERSATION);
        intent.putExtra("msg",conversation);
        context.sendBroadcast(intent);
        setBudge();
    }

    public void delete(Conversation conversation) {
        conversationAll.delete(conversation);
        Intent intent = new Intent(ACTION_DELETE_CONVERSATION);
        intent.putExtra("msg",conversation);
        context.sendBroadcast(intent);
        setBudge();
    }

    public void remove(Conversation conversation) {
        conversationAll.remove(conversation);
        Intent intent = new Intent(ACTION_REMOVE_CONVERSATION);
        intent.putExtra("msg",conversation);
        context.sendBroadcast(intent);
        setBudge();
    }

    public void read(Conversation conversation) {
        conversationAll.read(conversation);
        Intent intent = new Intent(ACTION_READ_CONVERSATION);
        intent.putExtra("msg",conversation);
        context.sendBroadcast(intent);
        setBudge();
    }

    public void doRead(String type,String detial) {
        Conversation conversation = new Conversation();
        conversation.detialId = detial;
        conversation.mType = type;
        Message msg = new Message();
        msg.obj = conversation;
        msg.what = OaMessageHandler.READ_CONVERSATION;
        mOaMessageHandler.sendMessage(msg);
    }


    private int getNotificationId(String type) {
        if(!conversationAll.nId.containsKey(type))
        {
            int id = conversationAll.bastId;
            conversationAll.nId.put(type,id);
            conversationAll.bastId++;
        }
        return conversationAll.nId.get(type);
    }

    private int getNotificationGropId(String type) {
        if(!conversationAll.nId.containsKey(type))
        {
            int id = conversationAll.bastId;
            conversationAll.nId.put(type,id);
            conversationAll.bastId= conversationAll.bastId+10000;
        }
        return conversationAll.nId.get(type);
    }

    public static void setContactCycleHead(TextView mhead, String name) {
        String s;
        if(name.length() > 0)
        {
            if(name.length() > 4)
            {
                s = name.substring(0,4);
                mhead.setText(name.substring(0,4));
            }
            else
            {
                s = name.toString();
                if(s != null)
                    mhead.setText(name.substring(0,s.length()));
            }
        }
        mhead.setBackgroundResource(R.drawable.contact_head);
    }
}
