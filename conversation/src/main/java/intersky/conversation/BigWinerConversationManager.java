package intersky.conversation;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.ParcelUuid;


import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.conversation.database.DBHelper;
import intersky.conversation.handler.SendMessageHandler;
import intersky.json.EchartArray;

public class BigWinerConversationManager {

    public static final String ACTION_UPDATA_CONVERSATION = "ACTION_UPDATA_CONVERSATION";
    public static final String ACTION_CONVERSATION_SET_READ = "ACTION_CONVERSATION_SET_READ";
    public static final String ACTION_CONVERSATION_OPEN_ACTIVITY = "ACTION_CONVERSATION_OPEN_ACTIVITY";
    public static final String ACTION_ADD_CONVERSATION_MESSAGE = "ACTION_ADD_CONVERSATION_MESSAGE";
    public static final String ACTION_SEND_CONVERSATION_MESSAGE = "ACTION_SEND_CONVERSATION_MESSAGE";
    public static final String ACTION_UPDATA_CONVERSATION_MESSAGE = "ACTION_UPDATA_CONVERSATION_MESSAGE";
    public static final String ACTION_DELETE_CONVERSATION_MESSAGE = "ACTION_DELETE_CONVERSATION_MESSAGE";
    public static final String ACTION_DODELETE_CONVERSATION_MESSAGE = "ACTION_DODELETE_CONVERSATION_MESSAGE";
    public static final String ACTION_DOAUDIO_CONVERSATION_MESSAGE = "ACTION_DOAUDIO_CONVERSATION_MESSAGE";
    public static final String ACTION_CODE_CONVERSATION_MESSAGE = "ACTION_CODE_CONVERSATION_MESSAGE";
    public static final String ACTION_CODE_CONVERSATION_MESSAGE_MENU = "ACTION_CODE_CONVERSATION_MESSAGE_MENU";
    public static final String ACTION_REMOVE_CONVERSATION_MESSAGE = "ACTION_REMOVE_CONVERSATION_MESSAGE";
    public SendMessageHandler mSendMessageHandler;
    public static volatile BigWinerConversationManager mBigWinerConversationManager;
    public String mUserid = "";
    public HashMap<String,Conversation> unreads = new HashMap<String,Conversation>();
    public HashMap<String,ArrayList<Conversation>> collectionConversation = new HashMap<String,ArrayList<Conversation>>();
    public HashMap<String,ArrayList<Conversation>> messageConversation = new HashMap<String,ArrayList<Conversation>>();
    public HashMap<String,HashMap<String,Conversation>> messageConversation2 = new HashMap<String,HashMap<String,Conversation>>();
    public ArrayList<Conversation> messageback = new ArrayList<Conversation>();
    public HashMap<String,ModuleDetial> cPage = new HashMap<String,ModuleDetial>();
    public boolean newfinish = false;
    public boolean noticefinish = false;
    public boolean meetingfinish = false;
    public ArrayList<Conversation> mConversations = new ArrayList<Conversation>();
    public HashMap<String,Conversation> messageHConversation = new HashMap<String,Conversation>();
    public ArrayList<Conversation> historys = new ArrayList<Conversation>();
    public ModuleDetial hismodul = new ModuleDetial();
    public ArrayList<Conversation> activitys = new ArrayList<Conversation>();
    public ModuleDetial activitysmodul = new ModuleDetial();
    public static BigWinerConversationManager init(Context context) {


        if (mBigWinerConversationManager == null) {
            synchronized (BigWinerConversationManager.class) {
                if (mBigWinerConversationManager == null) {
                    mBigWinerConversationManager = new BigWinerConversationManager(context);
                }
                else
                {
                    mBigWinerConversationManager.mSendMessageHandler = new SendMessageHandler(context);
                    mBigWinerConversationManager.initData();
                }
            }
        }
        return mBigWinerConversationManager;
    }

    public BigWinerConversationManager(Context context) {
        mSendMessageHandler = new SendMessageHandler(context);
        initData();

    }

    public void setUserid(String userid) {
        this.mUserid = userid;
    }

    public void setUpload(SendMessageHandler.UploadFile upload) {
        mSendMessageHandler.uploadFile = upload;
    }

    public void cleanAll() {
        unreads.clear();
        collectionConversation.clear();
        messageConversation.clear();
        messageConversation2.clear();
        messageback.clear();
        cPage.clear();
        mConversations.clear();
        messageHConversation.clear();
        newfinish = false;
        noticefinish = false;
        meetingfinish = false;
        historys.clear();
        ModuleDetial hismodul = new ModuleDetial();
        activitys.clear();
        ModuleDetial activitysmodul = new ModuleDetial();
    }

    public static BigWinerConversationManager getInstance() {
        return mBigWinerConversationManager;
    }

    public void codeResult(Context context,Conversation conversation,String result) {
        Intent intent = new Intent(ACTION_CODE_CONVERSATION_MESSAGE);
        intent.putExtra("msg",conversation);
        intent.putExtra("result",result);
        context.sendBroadcast(intent);
    }

    public void showMenuAagin(Context context,Intent intent) {
    }

    public void setRead(Context context,String detial) {
        ArrayList<Conversation> conversations = messageConversation.get(detial);
        Conversation conversationh = messageHConversation.get(detial);
        for(int i = 0 ; i < conversations.size() ; i++)
        {
            if(conversations.get(i).isRead == false)
            {
                conversations.get(i).isRead = true;
                conversationh.mHit--;
                BigWinerDBHelper.getInstance(context).addConversation(conversations.get(i));
            }
        }
    }

    public void doDelete(Context context,Conversation conversation,int index) {
        Intent intent = new Intent(ACTION_DODELETE_CONVERSATION_MESSAGE);
        intent.putExtra("msg",conversation);
        intent.putExtra("index",index);
        context.sendBroadcast(intent);
    }

    public void deleteMessage(Context context,Intent intent) {
        Conversation message = intent.getParcelableExtra("msg");
        Conversation conversation = messageConversation2.get(message.detialId).get(message.mRecordId);
        messageConversation2.get(message.detialId).remove(conversation);
        messageConversation.get(message.detialId).remove(conversation);
        if(conversation.isRead == false)
        {
            Conversation conversationh = messageHConversation.get(message.detialId);
            conversationh.mHit--;
        }
        BigWinerDBHelper.getInstance(context).deleteConversation(conversation);
        Intent intent1 = new Intent(ACTION_DODELETE_CONVERSATION_MESSAGE);
        intent1.putExtra("msg", conversation);
        intent1.putExtra("index", intent.getIntExtra("index",-1));
        context.sendBroadcast(intent1);
    }

    public void deleteMessage(Context context,String detial) {
        messageConversation2.remove(detial);
        messageConversation.remove(detial);
        Conversation conversationh =  messageHConversation.get(detial);
        ModuleDetial moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MESSAGE);
        if(mConversations.contains(conversationh))
        {
            mConversations.remove(conversationh);
            moduleDetial.allpagesize--;
        }
        collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE).remove(conversationh);
        messageHConversation.remove(detial);
        BigWinerDBHelper.getInstance(context).deleteConversation(detial);
    }

    public void initData() {
        cleanAll();
        cPage.put(Conversation.CONVERSATION_TYPE_MESSAGE,new ModuleDetial());
        ModuleDetial moduleDetial =  new ModuleDetial();
        moduleDetial.allpagesizemax = 2;
        cPage.put(Conversation.CONVERSATION_TYPE_NOTICE,moduleDetial);
        cPage.put(Conversation.CONVERSATION_TYPE_MEETING,new ModuleDetial());
        moduleDetial =  new ModuleDetial();
        moduleDetial.allpagesizemax = 1;
        cPage.put(Conversation.CONVERSATION_TYPE_NEWS,moduleDetial);
        ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        collectionConversation.put(Conversation.CONVERSATION_TYPE_MESSAGE,conversations);
        conversations = new ArrayList<Conversation>();
        collectionConversation.put(Conversation.CONVERSATION_TYPE_NOTICE,conversations);
        conversations = new ArrayList<Conversation>();
        collectionConversation.put(Conversation.CONVERSATION_TYPE_NEWS,conversations);
        conversations = new ArrayList<Conversation>();
        collectionConversation.put(Conversation.CONVERSATION_TYPE_MEETING,conversations);

    }

    public void sendMessage(Context context ,Conversation conversation) {
        Intent intent = new Intent(ACTION_SEND_CONVERSATION_MESSAGE);
        intent.putExtra("msg",conversation);
        context.sendBroadcast(intent);
    }

    public void addMessage(Intent intent,Context context) {
        Conversation conversation = intent.getParcelableExtra("msg");
        ArrayList<Conversation> conversations = messageConversation.get(conversation.detialId);
        if(conversations == null)
        {
            conversations = new ArrayList<Conversation>();
            messageConversation.put(conversation.detialId,conversations);
        }
        HashMap<String,Conversation> hashMap = messageConversation2.get(conversation.detialId);
        if(hashMap == null)
        {
            hashMap = new HashMap<String,Conversation>();
            messageConversation2.put(conversation.detialId,hashMap);
        }
        if(!hashMap.containsKey(conversation.mRecordId))
        {
            conversations.add(0,conversation);
            hashMap.put(conversation.mRecordId,conversation);
        }
        else
        {
            Conversation conversation1 = hashMap.get(conversation.mRecordId);
            conversation1.copy(conversation);
        }

        ArrayList<Conversation> headlist = collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE);
        Conversation head = messageHConversation.get(conversation.detialId);
        if(head == null)
        {
            head = new Conversation(conversation);
            head.mHit = 0;
            head.isRead = true;
            headlist.add(0,head);
            messageHConversation.put(head.detialId,head);
        }
        else
        {
            headlist.remove(head);
            headlist.add(0,head);
            head.mTime = conversation.mTime;
            head.mTitle = conversation.mTitle;
            head.mSubject = conversation.mSubject;
            head.isRead = true;
            head.isSendto = conversation.isSendto;
        }

        ModuleDetial moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MESSAGE);
        if(moduleDetial.allpagesize == 0)
        {
            if(headlist.size() == 1)
            {
                mConversations.add(0,headlist.get(0));
                moduleDetial.allpagesize++;
            }
            else  if(headlist.size() > 1)
            {
                mConversations.add(0,headlist.get(0));
                mConversations.add(1,headlist.get(1));
                moduleDetial.allpagesize = 2;
            }
        }
        else if(moduleDetial.allpagesize == 1)
        {
            if(headlist.size() == 1)
            {
                mConversations.remove(0);
                mConversations.add(0,headlist.get(0));
            }
            else  if(headlist.size() > 1)
            {
                mConversations.remove(0);
                mConversations.add(0,headlist.get(0));
                mConversations.add(1,headlist.get(1));
                moduleDetial.allpagesize++;
            }
        }
        else if(moduleDetial.allpagesize == 2)
        {
            if(headlist.size() == 1)
            {
                mConversations.remove(0);
                mConversations.add(0,headlist.get(0));
            }
            else if(headlist.size() > 1)
            {
                mConversations.remove(0);
                mConversations.remove(0);
                mConversations.add(0,headlist.get(0));
                mConversations.add(1,headlist.get(1));
            }
        }
        BigWinerDBHelper.getInstance(context).addConversation(conversation);
        ArrayList<Conversation> conversations1 = new ArrayList<Conversation>();
        conversations1.add(conversation);
        Intent intent1 = new Intent(ACTION_ADD_CONVERSATION_MESSAGE);
        intent1.putExtra("addcount",1);
        intent1.putParcelableArrayListExtra("msgs",conversations1);
        intent1.putExtra("detialid",conversation.detialId);
        context.sendBroadcast(intent1);
    }

    public void cleanMessage() {
        messageConversation.clear();
        collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE).clear();
        messageHConversation.clear();
        ModuleDetial detial = cPage.get(Conversation.CONVERSATION_TYPE_MESSAGE);
        if(detial.allpagesize == 2)
        {
            mConversations.remove(0);
            mConversations.remove(0);
        }
        else if(detial.allpagesize == 1)
        {
            mConversations.remove(0);
        }
        detial.reset();
    }

    public void updataMessage(Context context,Conversation message) {
        BigWinerDBHelper.getInstance(context).addConversation(message);
        Conversation conversation = messageConversation2.get(message.detialId).get(message.mRecordId);
        conversation.copy(message);
        Intent intent = new Intent(ACTION_UPDATA_CONVERSATION_MESSAGE);
        intent.putExtra("msg",message);
        context.sendBroadcast(intent);
    }

    public void doaudo(Context context,Conversation message)
    {
        BigWinerDBHelper.getInstance(context).addConversation(message);
        Conversation conversation = messageConversation2.get(message.detialId).get(message.mRecordId);
        conversation.copy(message);
        Intent intent = new Intent(ACTION_DOAUDIO_CONVERSATION_MESSAGE);
        intent.putExtra("msg",message);
        context.sendBroadcast(intent);
    }


    public ArrayList<Conversation> searchMessage(Context context,String msg) {
        ArrayList<Conversation> conversations = BigWinerDBHelper.getInstance(context).scanMessage(mUserid,msg);
        return conversations;
    }

}
