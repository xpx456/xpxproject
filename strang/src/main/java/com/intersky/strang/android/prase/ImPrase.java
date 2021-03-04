package com.intersky.strang.android.prase;

import android.content.Context;
import android.os.Message;

import com.intersky.strang.android.asks.ImAsks;
import com.intersky.strang.android.view.StrangApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.chat.handler.SendMessageHandler;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class ImPrase {

    public static void praseImMsg(Context context,NetObject net) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json))
            {
                ArrayList<Conversation> file = new ArrayList<Conversation>();
                HashMap<String,ArrayList<Conversation>> addMap = new HashMap<String,ArrayList<Conversation>>();
                XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
                XpxJSONArray ja = xpxJsonObject.getJSONArray("data");
                for (int i = 0;i<ja.length();i++) {
                    XpxJSONObject jo = (XpxJSONObject) ja.getJSONObject(i);
                    XpxJSONObject content = (XpxJSONObject) new XpxJSONObject(jo.getString("Content"));
                    Conversation mConversation = new Conversation();
                    mConversation.messageId = jo.getString("MessageID");
                    mConversation.mTime = jo.getString("Time");
                    mConversation.mTitle = content.getString("username");
                    mConversation.mSubject = content.getString("msg");
                    mConversation.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
                    mConversation.mHit = content.getInt("length",0);
                    mConversation.isRead = false;
                    mConversation.isSendto = false;
                    mConversation.issend = Conversation.MESSAGE_STATAUE_SUCCESS;
                    mConversation.detialId = content.getString("userid");
                    mConversation.sourceType = content.getInt("type",0);
                    if (mConversation.sourceType != Conversation.MESSAGE_TYPE_NOMAL) {
                        mConversation.sourceName = content.getString("filename");
                        mConversation.sourceId  = content.getString("fileid");
                        if (mConversation.sourceType == Conversation.MESSAGE_TYPE_FILE) {
                            mConversation.mSubject = "[文件]";
                            mConversation.sourcePath = StrangApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/file/"
                                    +mConversation.detialId)+"/"+mConversation.sourceName;
                            file.add(mConversation);
                            Conversation source = new Conversation(mConversation);
                            Message message = new Message();
                            message.obj = new Conversation(source);
                            message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                            if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                                ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
                        } else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
                            mConversation.mSubject = "[图片]";
                            mConversation.sourcePath = StrangApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/img/"
                                    +mConversation.detialId)+"/"+mConversation.sourceName;
                            file.add(mConversation);
                            Conversation source = new Conversation(mConversation);
                            Message message = new Message();
                            message.obj = new Conversation(source);
                            message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                            if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                                ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
                        } else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_VOICE) {
                            mConversation.mSubject = "[语音]";
                            mConversation.sourcePath = StrangApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/voice/"
                                    +mConversation.detialId)+"/"+mConversation.sourceName;
                            Conversation source = new Conversation(mConversation);
                            Message message = new Message();
                            message.obj = new Conversation(source);
                            message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                            if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                                ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
                        }
                        else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_MAP) {
                            JSONObject map = new JSONObject();
                            map.put("longitude",content.getDouble("longitude",0));
                            map.put("latitude",content.getDouble("latitude",0));
                            map.put("locationName",content.getString("locationName"));
                            map.put("locationAddress",content.getString("locationAddress"));
                            mConversation.mSubject = "位置:"+content.getString("locationName");
                            mConversation.sourceName = map.toString();
                            file.add(mConversation);
                            mConversation.sourcePath = StrangApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/map/"
                                    +mConversation.detialId+"/"+mConversation.sourceName);
                        }
                    }
                    ArrayList<Conversation> conversations = addMap.get(mConversation.detialId);
                    if(conversations == null) {
                        conversations = new ArrayList<Conversation>();
                        addMap.put(mConversation.detialId,conversations);
                    }
                    conversations.add(mConversation);
                }
                for(HashMap.Entry<String, ArrayList<Conversation>> entry: addMap.entrySet())
                {
                    ArrayList<Conversation> conversations = entry.getValue();
                    StrangApplication.mApp.conversationManager.doAdd(conversations);
                }
                for(int i = 0 ; i < file.size() ; i++)
                {
                    ImAsks.getImFileInfo(context,ChatUtils.getChatUtils().messageHandler,file.get(i), SendMessageHandler.SEND_UPDATA_SIZE_SUCCESS);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseConversationList(String json) {
        try {
            XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = xpxJsonObject.getJSONArray("data");
            for (int i = 0; i < ja.length() - 1; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                praseConversation(jo.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void praseConversation(String json) {
        try {
            XpxJSONObject jo = (XpxJSONObject) new XpxJSONObject(json);
            Conversation info = new Conversation();
            info.mRecordId = jo.getString("message_id");
            info.mTitle = ContactManager.mContactManager.mOrganization.getContactName(jo.getString("source_id"),StrangApplication.mApp) + jo.getString("title");
            info.mSubject = jo.getString("content");
            info.mType = jo.getString("module");
            info.mTime = jo.getString("create_time");
            info.detialId = jo.getString("module_id");
            Bus.callData(ChatUtils.getChatUtils().context,"conversation/addConversation",info,0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseUploadFile(NetObject netObject) {
        try {
            XpxJSONObject object = new XpxJSONObject(netObject.result);
            Conversation msg = (Conversation) netObject.item;
            msg.sourceId =  object.getString("fileid");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checksuccessUploadFile(NetObject netObject) {
        try {
            XpxJSONObject object = new XpxJSONObject(netObject.result);

            String a =  object.getString("fileid");
            Conversation conversation = (Conversation) netObject.item;
            conversation.sourceId = a;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean praseData(NetObject net) {
        try {

            XpxJSONObject object = new XpxJSONObject(net.result);
            if(object.has("code"))
            {
                if(object.getInt("code",0)==0)
                {
                    return true;
                }
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
