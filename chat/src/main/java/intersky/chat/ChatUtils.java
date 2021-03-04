package intersky.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.ParcelUuid;

import com.amap.api.location.AMapLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import intersky.appbase.Downloadobject;
import intersky.appbase.PermissionResult;
import intersky.appbase.ScreenDefine;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Register;
import intersky.chat.database.DBHelper;
import intersky.chat.entity.ChatPager;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.chat.handler.SendMessageHandler;
import intersky.chat.view.activity.ChatActivity;
import intersky.filetools.FileUtils;
import intersky.filetools.thread.DownloadThread;
import intersky.talk.TalkUtils;
import intersky.xpxnet.net.NetObject;
import xpx.map.MapManager;

public class ChatUtils {

    public final static String GET_LAEVE_MESSAGE_FINISHN = "GET_LAEVE_MESSAGE_FINISHN";
    public static final String ACTION_CHAT_PHOTO_SELECT = "ACTION_CHAT_PHOTO_SELECT";
    public static final String ACTION_CHAT_LOCATION_SELECT = "ACTION_CHAT_LOCATION_SELECT";
    public static final String ACTION_CHAT_CARD_SELECT = "ACTION_CHAT_CARD_SELECT";
    public static final String ACTION_IM_FILE_DOWNLOAD_FAIL = "ACTION_IM_FILE_DOWNLOAD_FAIL";
    public static final String ACTION_IM_FILE_DOWNLOAD_UPDATA = "ACTION_IM_FILE_DOWNLOAD_UPDATA";
    public static final String ACTION_IM_FILE_DOWNLOAD_FINISH = "ACTION_IM_FILE_DOWNLOAD_FINISH";
    public static final String ACTION_IM_AUDIO_DOWNLOAD_FINISH = "ACTION_IM_AUDIO_DOWNLOAD_FINISH";
    public static final String ACTION_IM_IMG_DOWNLOAD_FINISH = "ACTION_IM_IMG_DOWNLOAD_FINISH";
    public static final String ACTION_ADD_CHAT_MESSAGE = "ACTION_ADD_CHAT_MESSAGE";
    public static final String ACTION_UPDATA_CHAT_MESSAGE = "ACTION_UPDATA_CHAT_MESSAGE";
    public static final String ACTION_OPEN_ITEM = "ACTION_OPEN_ITEM";
    public static final String ACTION_UPDATA_HEAD = "ACTION_UPDATA_HEAD";
    public static final int EVENT_GET_FAIL = 1050100;
    public static final int EVENT_GET_UPDATA = 1050102;
    public static final int EVENT_GET_SUCCESS = 1050101;

    public static final int EVENT_HEAD_GET_FAIL = 1050103;
    public static final int EVENT_HEAD_GET_UPDATA = 1050104;
    public static final int EVENT_HEAD_GET_SUCCESS = 1050105;

    public static final int EVENT_UPDATA_HEAD_LIST = 1050106;
    public Context context;
    public LeaveMessageHandler mLeaveMessageHandler = new LeaveMessageHandler();
    public ChatFunctions mChatFunctions;
    public SampleChatFunctions mSampleChatFunctions;
    public Account mAccount;
    public Contacts my;
    public SendMessageHandler messageHandler;
    public Register register;
    public Contacts showContacts;
    public static volatile ChatUtils mChatUtils;
    public static TalkUtils mTalkUtils;
    public MapManager mapManager;
    public int maxthread = 3;
    public HashMap<String, Contacts> hashContacts = new HashMap<String, Contacts>();
    public HashMap<String, ChatPager> hashChatPager = new HashMap<String, ChatPager>();
    public HashMap<String, Downloadobject> mDownloadThreads = new HashMap<String, Downloadobject>();
    public HashMap<String,DownloadThread> downloadhash = new HashMap<String,DownloadThread>();
    public HashMap<String,DownloadThread> headhash = new HashMap<String,DownloadThread>();
    public ArrayList<Conversation> sources = new ArrayList<Conversation>();
    public HashMap<String,ArrayList<Conversation>> headSourceList = new HashMap<String,ArrayList<Conversation>>();
    public HashMap<String,Conversation> sourceHash = new HashMap<String,Conversation>();
    public HashMap<String,Attachment> headImages = new HashMap<String,Attachment>();
    public String headPath = "";
    public boolean enable = true;
    public ScreenDefine mScreenDefine;
    public static ChatUtils init(Context context, HashMap<String, Contacts> hashContacts
            , ChatFunctions mChatFunctions, MapManager mapManager,String headPath) {

        if (mChatUtils == null) {
            synchronized (ChatUtils.class) {
                if (mChatUtils == null) {
                    mChatUtils = new ChatUtils(context, hashContacts, mChatFunctions,mapManager,headPath);
                    mTalkUtils = TalkUtils.init(context);
                }
                else
                {
                    mChatUtils.context = context;
                    mChatUtils.hashContacts = hashContacts;
                    mChatUtils.mChatFunctions = mChatFunctions;
                    mChatUtils.mScreenDefine = new ScreenDefine(context);
                    mChatUtils.headPath = headPath;
                    mChatUtils.mapManager = mapManager;
                    mChatUtils.messageHandler = new SendMessageHandler(context);
                    mTalkUtils = TalkUtils.init(context);
                }
            }
        }
        return mChatUtils;
    }

    public static ChatUtils sampleinit(Context context,SampleChatFunctions sampleChatFunctions,MapManager mapManager,String headPath) {

        if (mChatUtils == null) {
            synchronized (ChatUtils.class) {
                if (mChatUtils == null) {
                    mChatUtils = new ChatUtils(context,sampleChatFunctions,mapManager,headPath);
                    mTalkUtils = TalkUtils.init(context);
                }
                else
                {
                    mChatUtils.context = context;
                    mChatUtils.mScreenDefine = new ScreenDefine(context);
                    mChatUtils.headPath = headPath;
                    mChatUtils.mapManager = mapManager;
                    mChatUtils.messageHandler = new SendMessageHandler(context);
                    mTalkUtils = TalkUtils.init(context);
                }
            }
        }
        return mChatUtils;
    }

    public ChatUtils(Context context,SampleChatFunctions sampleChatFunctions,MapManager mapManager,String headPath) {
        this.mapManager = mapManager;
        mSampleChatFunctions = sampleChatFunctions;
        this.mScreenDefine = new ScreenDefine(context);
        this.headPath = headPath;
        File file = new File(this.headPath);
        if(file.exists())
        {
            FileUtils.pathUtils.recursionDeleteFile(file);
            file.delete();
        }
        this.context = context;

    }

    public void cleanAll() {
        hashContacts.clear();
        hashChatPager.clear();
        mDownloadThreads.clear();
        mDownloadThreads.clear();
        downloadhash.clear();
        headhash.clear();
        sources.clear();
        headSourceList.clear();
        sourceHash.clear();
        headImages.clear();
    }

    public static ChatUtils getChatUtils() {
        return mChatUtils;
    }

    public ChatUtils(Context context, HashMap<String, Contacts> hashContacts, ChatFunctions mChatFunctions,MapManager mapManager,String headPath) {
        this.context = context;
        this.hashContacts = hashContacts;
        this.mChatFunctions = mChatFunctions;
        this.mScreenDefine = new ScreenDefine(context);
        this.headPath = headPath;
        this.mapManager = mapManager;
        this.messageHandler = new SendMessageHandler(context);

    }

    public void setmAccount(Account account) {
        this.mAccount = account;
        this.my = new Contacts(account);
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public void startChart()
    {
        cleanAll();
        this.sources.addAll(DBHelper.getInstance(context).scanSource(sourceHash,mAccount.mRecordId+mAccount.serviceid,headSourceList));
        DBHelper.getInstance(context).scanHead(headImages,mAccount.mRecordId+mAccount.serviceid);
        getChatSourceDownload();
    }

    public void startSampleChart()
    {
        cleanAll();
        this.sources.addAll(DBHelper.getInstance(context).scanSource(sourceHash,mAccount.mRecordId+mAccount.serviceid,headSourceList));
        DBHelper.getInstance(context).scanHead(headImages,mAccount.mRecordId+mAccount.serviceid);

    }

    public void cleanSource() {

    }

    public void cleanHead() {

    }

    public File getHead(String id) {
        File file = null;
        if(headImages.containsKey(id))
        {
            Attachment attachment = headImages.get(id);
            file = new File(attachment.mPath2);
            if(file.exists())
            {
                return file;
            }
        }
        return null;
    }

    public void upDataHeadList() {
        if(enable)
        {
            for (Map.Entry<String, Attachment> entry : headImages.entrySet()) {
                if(mChatFunctions != null)
                    mChatFunctions.checkHead(entry.getValue());
                if(mSampleChatFunctions != null)
                    mSampleChatFunctions.checkHead(entry.getValue());
            }
            if(mLeaveMessageHandler != null)
            {
                mLeaveMessageHandler.removeMessages(EVENT_UPDATA_HEAD_LIST);
                mLeaveMessageHandler.sendEmptyMessageDelayed(EVENT_UPDATA_HEAD_LIST,1000*15*headImages.size()+10000);
            }
        }
    }

    public void stopHeadList()
    {
        if(mLeaveMessageHandler != null)
            mLeaveMessageHandler.removeMessages(EVENT_UPDATA_HEAD_LIST);
        enable = false;
    }

    public void stopSourceList()
    {
        sources.clear();
        sourceHash.clear();
    }

    public void addHead(Attachment attachment) {
        DBHelper.getInstance(context).addHead(attachment,mAccount.mRecordId+mAccount.serviceid);
        if(headImages.containsKey(attachment.mRecordid))
        {
            if(attachment.mPath.length() == 0)
            {
                attachment.mPath = ChatUtils.getChatUtils().headPath+attachment.mRecordid+"temp"+attachment.mUrltemp.substring(attachment.mUrltemp.lastIndexOf("."),attachment.mUrltemp.length());
                attachment.mPath2 = ChatUtils.getChatUtils().headPath+attachment.mRecordid+attachment.mUrltemp.substring(attachment.mUrltemp.lastIndexOf("."),attachment.mUrltemp.length());
            }
            File file = new File(attachment.mPath2);
            if(!attachment.mUrltemp.equals(attachment.mUrl) || file.exists() == false || file.length() == 0)
            {
                doHeadUpdata(attachment);
            }
        }
        else
        {
            attachment.mPath = headPath+attachment.mRecordid+"temp"+attachment.mUrltemp.substring(attachment.mUrltemp.lastIndexOf("."),attachment.mUrltemp.length());
            attachment.mPath2 = headPath+attachment.mRecordid+attachment.mUrltemp.substring(attachment.mUrltemp.lastIndexOf("."),attachment.mUrltemp.length());
            doHeadUpdata(attachment);
        }
    }

    public void addHead(Contacts contacts)
    {
//        if(!headImages.containsKey(contacts.mRecordid) && contacts.icon.length() > 0 && contacts.mRecordid.length() > 0)
//        {
//            Attachment attachment = new Attachment();
//            attachment.mRecordid = contacts.mRecordid;
//            if(mChatFunctions != null)
//            {
//                attachment.mUrl = mChatFunctions.getHeadIcom(contacts.icon);
//                attachment.mUrltemp = mChatFunctions.getHeadIcom(contacts.icon);
//            }
//            if(mSampleChatFunctions != null)
//            {
//                attachment.mUrl = mSampleChatFunctions.getHeadIcom(contacts.icon);
//                attachment.mUrltemp = mSampleChatFunctions.getHeadIcom(contacts.icon);
//            }
//            addHead(attachment);
//        }

    }

    public void doHeadUpdata(Attachment attachment)
    {
        if(!headhash.containsKey(attachment))
        {
            if(attachment.mPath.length() == 0)
            {
                attachment.mPath = ChatUtils.getChatUtils().headPath+attachment.mRecordid+"temp"+attachment.mUrltemp.substring(attachment.mUrltemp.lastIndexOf("."),attachment.mUrltemp.length());
                attachment.mPath2 = ChatUtils.getChatUtils().headPath+attachment.mRecordid+attachment.mUrltemp.substring(attachment.mUrltemp.lastIndexOf("."),attachment.mUrltemp.length());
            }
            DownloadThread downloadThread = new DownloadThread(attachment,EVENT_HEAD_GET_FAIL,EVENT_HEAD_GET_UPDATA,EVENT_HEAD_GET_SUCCESS,attachment,mLeaveMessageHandler);
            headhash.put(attachment.mRecordid,downloadThread);
            downloadThread.start();
        }

    }

    public void updataHeadFinish(Attachment attachment) {
        File file = new File(attachment.mPath2);
        if(file.exists())
        {
            file.delete();
        }
        FileUtils.copyFile(attachment.mPath,attachment.mPath2);
        attachment.mUrltemp = attachment.mUrl;
        DBHelper.getInstance(context).addHead(attachment,mAccount.mRecordId+mAccount.serviceid);
        if(headhash.containsKey(attachment.mRecordid));
        headhash.remove(attachment.mRecordid);
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATA_HEAD);
        intent.putExtra("head",attachment);
        context.sendBroadcast(intent);
    }

    public void updataHeadFail(Attachment attachment)
    {
        headhash.remove(attachment.mRecordid);
    }

    public void getChatSourceDownload() {
        while (downloadhash.size() < maxthread && sources.size() > 0)
        {
            Conversation source = sources.get(0);
            if(source.finish == false)
            {
                Attachment attachment = new Attachment();
                if(mChatFunctions != null)
                    attachment.mUrl = mChatFunctions.getFileUrl(source.sourceId);
                else
                    attachment.mUrl = mSampleChatFunctions.getFileUrl(source.sourceId);
                attachment.mName = source.sourceName;
                attachment.mPath = source.sourcePath;
                sources.remove(0);
                if(!downloadhash.containsKey(attachment.mRecordid)) {
                    DownloadThread downloadThread = new DownloadThread(attachment,EVENT_GET_FAIL,EVENT_GET_UPDATA,EVENT_GET_SUCCESS,source,mLeaveMessageHandler);
                    downloadThread.start();
                    downloadhash.put(source.mRecordId,downloadThread);
                }
            }
            else
            {
                sources.remove(0);
            }
        }
    }

    public void addChatSource(Conversation conversation) {
        if(!sourceHash.containsKey(conversation.mRecordId))
        {
            sourceHash.put(conversation.mRecordId,conversation);
            sources.add(conversation);
            ArrayList<Conversation> list = headSourceList.get(conversation.detialId);
            if(list == null)
            {
                list = new ArrayList<Conversation>();
                headSourceList.put(conversation.detialId,list);
            }
            list.add(conversation);
            DBHelper.getInstance(context).addSource(conversation,mAccount.mRecordId+mAccount.serviceid);
            getChatSourceDownload();
        }

    }

    public void deleteSource(Conversation conversation) {
        if(sourceHash.containsKey(conversation.mRecordId))
        {
            Conversation conversation1 = sourceHash.get(conversation.mRecordId);
            sourceHash.remove(conversation.mRecordId);
            sources.remove(conversation1);
            ArrayList<Conversation> list = headSourceList.get(conversation1.detialId);
            if(list == null)
            {
                list = new ArrayList<Conversation>();
                headSourceList.put(conversation.detialId,list);
            }
            list.remove(conversation1);
            if(downloadhash.containsKey(conversation.mRecordId))
            {
                DownloadThread downloadThread = downloadhash.get(conversation.mRecordId);
                downloadThread.contral.stop = true;
                downloadhash.remove(conversation.mRecordId);
            }
            File file = new File(conversation1.sourcePath);
            if(file.exists())
            file.delete();
            DBHelper.getInstance(context).deleteSource(conversation1);
        }
    }

    public void deleteSourceDetial(String detialid) {

        ArrayList<Conversation> list = headSourceList.get(detialid);
        if(list == null)
        {
            list = new ArrayList<Conversation>();
            headSourceList.put(detialid,list);
        }
        for(int i = 0 ; i < list.size() ; i++)
        {
            deleteSource(list.get(i));
            i--;
        }
    }

    public void deleteSourceALl() {
        for (HashMap.Entry<String,Conversation> entry : sourceHash.entrySet()) {
            deleteSource(entry.getValue());
        }
    }

    public void finishSource(Conversation conversation) {
        conversation.finish = true;
        DBHelper.getInstance(context).addSource(conversation,mAccount.mRecordId+mAccount.serviceid);
        downloadhash.remove(conversation.mRecordId);
        if(conversation.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
        {
            if(mChatFunctions != null)
                mChatFunctions.updataChatView(conversation);
            else if(mSampleChatFunctions != null)
                mSampleChatFunctions.updataChatView(conversation);
        }
        getChatSourceDownload();
    }

    public void failSource(Conversation conversation) {
        //sources.add(conversation);
        downloadhash.remove(conversation.mRecordId);
        getChatSourceDownload();
    }

    public boolean checkFinish(Conversation conversation) {
         if(sourceHash.containsKey(conversation.mRecordId)) {
            Conversation conversation1 = sourceHash.get(conversation.mRecordId);
            if(conversation1.finish == false)
            {
                getChatSourceDownload();
            }

            return conversation1.finish;
        }
        else {
            Conversation source = new Conversation(conversation);
            addChatSource(source);
            return false;
        }
    }


    public void getLeaveMessage() {
        mChatFunctions.getLeaveMessage(context, mLeaveMessageHandler);
    }

    public void addMessage(ArrayList<Conversation> msgs) {
        Intent intent1 = new Intent(ACTION_ADD_CHAT_MESSAGE);
        intent1.putExtra("addcount",msgs.size());
        intent1.putParcelableArrayListExtra("msgs",msgs);
        context.sendBroadcast(intent1);
    }

    public void updataMessage(Conversation msg) {
        Intent intent1 = new Intent(ACTION_UPDATA_CHAT_MESSAGE);
        intent1.putExtra("msg",msg);
        context.sendBroadcast(intent1);
    }


    public void startChat(Context context,Conversation conversation) {
        Contacts contacts = hashContacts.get(conversation.detialId);
        if(contacts == null)
        {
            contacts = new Contacts();
            contacts.mRecordid = conversation.detialId;
            contacts.mName = conversation.mTitle;
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("contacts",contacts);
            context.startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("contacts",contacts);
            context.startActivity(intent);
        }
    }

    public void startChat(Context context,Contacts contacts) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("contacts",contacts);
        context.startActivity(intent);
    }

    public void startChat(Context context,String id) {
        Contacts contacts = hashContacts.get(id);
        if(contacts == null)
        {
            contacts = new Contacts();
            contacts.mRecordid = id;
            contacts.mName = context.getString(R.string.sex_unknowfemale);
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("contacts",contacts);
            context.startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("contacts",contacts);
            context.startActivity(intent);
        }
    }

    public interface ChatFunctions
    {
        ArrayList<Conversation> getMessages(String detialid);
        Conversation getMessages(String detialid,String recordid);
        String getFileUrl(String id);
        void sendFile(Context context, File file);
        void sendText(Context context, String content);
        void scanCode(Context context,String url);
        String measureCode(Bitmap bitmap);
        void sendMessage(Context context, Conversation msg);
        void sendMessage(Context context, Conversation msg,File file);
        void sendMessageSuccess(Context context, Conversation msg);
        void sendMessageFail(Context context, Conversation msg);
        void delete(Context context,Conversation msg,int index);
        PermissionResult takePhoto(Activity context, Contacts contacts);
        void slectPhoto(Context context);
        void showContactHead(Context context,Contacts contacts);
        void readMessages(String id);
        String photoResult(int requestCode, int resultCode, Intent data);
        Intent openFile(File file);
        boolean praseFile(NetObject netObject);
        void sendMessage(Context context,NetObject netObject);
        void getLeaveMessage(Context context, Handler handler);
        void praseLeaveMessage(NetObject netObject);
        void setNotificationShow();
        void startLeaveMessage(Context context,String id);
        void openFile(Context context,Conversation msg);
        void openLocation(Context context,Conversation msg);
        void praseFilesize(Context context,NetObject netObject);
        void getFilesize(Context context,Conversation conversation);
        void startChart(Contacts contacts);
        String getGlideUrl(String id);
        void updataChatView(Conversation conversation);
        void checkHead(Attachment attachment);
        String checkHeadResult(NetObject netObject);
        String getHeadIcom(String id);
    }

    public interface SampleChatFunctions{
        void praseLeaveMessage(NetObject netObject);
        String getFileUrl(String id);
        void updataChatView(Conversation conversation);
        void checkHead(Attachment attachment);
        void checkHeadResult(NetObject netObject);
        String getHeadIcom(String id);
    }


}
