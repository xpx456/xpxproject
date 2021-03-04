package com.bigwiner.android.prase;


import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class ImPrase {

    public static void praseImMsg(NetObject net) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json))
            {
                ArrayList<Conversation> conversations = new ArrayList<Conversation>();
                XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
                XpxJSONArray ja = xpxJsonObject.getJSONArray("data");
                for (int i = ja.length() - 1; i >= 0; i--) {
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
                        } else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
                            mConversation.mSubject = "[图片]";
                        } else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_VOICE) {
                            mConversation.mSubject = "[语音]";
                        }
                    }
                    conversations.add(mConversation);
                }
                if(conversations.size() > 0)
                {
//                    InterskyApplication.mApp.conversationManager.doAdd(conversations);
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
            info.mTitle = ContactManager.mContactManager.mOrganization.getContactName(jo.getString("source_id"), BigwinerApplication.mApp) + jo.getString("title");
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
