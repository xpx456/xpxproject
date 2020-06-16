package com.intersky.android.prase;

import android.content.Context;

import com.intersky.R;
import com.intersky.android.entity.IntersakyData;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.AppActivityManager;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.conversation.ConversationManager;
import intersky.oa.ConversationAsks;
import intersky.function.FunctionUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class OaMessagePrase {

    public static void praseConversationList(MainActivity mainActivity, NetObject net) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = xpxJsonObject.getJSONArray("data");
            for (int i = 0; i < ja.length() - 1; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                praseConversation(jo.toString());
                if (mainActivity.readid.length() != 0)
                    mainActivity.readid += "," + jo.getString("message_id");
                else
                    mainActivity.readid += jo.getString("message_id");
                mainActivity.unreadcount++;
            }
            XpxJSONObject jo = ja.getJSONObject(ja.length() - 1);


            if (jo.getInt("total_results",0) > mainActivity.unreadcount) {
                mainActivity.messagePage++;
                ConversationAsks.getOaMessages(ConversationManager.getInstance().context, mainActivity.messagePage, mainActivity.mMainPresenter.mMainHandler
                        , InterskyApplication.mApp.mAccount.mRecordId,InterskyApplication.mApp.mAccount.mCompanyId);
            } else {
                if (mainActivity.readid.length() > 0)
                    ConversationAsks.setRead(ConversationManager.getInstance().context, mainActivity.mMainPresenter.mMainHandler,
                            mainActivity.readid,InterskyApplication.mApp.mAccount.mRecordId,InterskyApplication.mApp.mAccount.mCompanyId);
                mainActivity.messagePage = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void praseGropMessage(Context context,NetObject net) {
        JSONObject jsonObject = null;
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            Conversation start = null;
            for (int i = 0; i < ja.length() - 1; i++) {
                JSONObject jo = ja.getJSONObject(i);
                Conversation tConversationModel = new Conversation();
                tConversationModel.messageId = jo.getString("message_id");
                tConversationModel.mTime = jo.getString("create_time");
                tConversationModel.mTitle = jo.getString("title");
                tConversationModel.mSubject = jo.getString("content");
                tConversationModel.mType = Conversation.CONVERSATION_TYPE_GROP_MESSAGE;
                tConversationModel.detialId = jo.getString("module_id") + "," + jo.getString("module");
                if (jo.has("extend")) {
                    tConversationModel.sourceId = jo.getString("extend");
                    if (tConversationModel.sourceId.equals("null")) {
                        tConversationModel.sourceId = "";
                    }
                }
                if(net.item != null)
                {
                    String mid = (String) net.item;
                    if(tConversationModel.messageId.equals(mid))
                    {
                        start = tConversationModel;
                    }
                }
                InterskyApplication.mApp.conversationManager.doAdd(tConversationModel);
                if(start != null) {
                    FunctionUtils.getInstance().showWebMessage(AppActivityManager.getInstance().getCurrentActivity(),start);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void praseConversationOne(NetObject net) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
            XpxJSONObject jo = xpxJsonObject.getJSONObject("data");
            praseConversation(jo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void praseConversation(String json) {
        try {
            XpxJSONObject jo = (XpxJSONObject) new XpxJSONObject(json);
            Conversation info = new Conversation();
            if(jo.getString("source_id").equals("0") || jo.getString("source_id").length() == 0)
            {
                info.mTitle = jo.getString("title");
            }
            else
            {
                if(InterskyApplication.mApp.contactManager.mOrganization.mAllContactsMap.containsKey(jo.getString("source_id")))
                {
                    info.mTitle = InterskyApplication.mApp.contactManager.mOrganization.mAllContactsMap.get(jo.getString("source_id")).getmRName() + jo.getString("title");
                }
                else
                {
                    info.mTitle = InterskyApplication.mApp.getString(R.string.keyword_unknow);
                }
            }


            info.messageId = jo.getString("message_id");
            info.mSubject = jo.getString("content");
            if(jo.getString("source_type").toLowerCase().equals("oa"))
            info.mType = getMessageType(jo.getString("module"));
            else
                info.mType = getMessageType(jo.getString("source_type"));
            if(info.mType.length() == 0) {
                info.mType = getMessageType(jo.getString("source_type"));
            }
            info.mTime = jo.getString("create_time");
            info.detialId = jo.getString("module_id");
            InterskyApplication.mApp.conversationManager.doAdd(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseData(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
            return false;
        }
        if(AppUtils.success(json) == false)
            return false;
        return true;
    }

    public static String getMessageType(String type) {
        if(type.endsWith("Report")) {
            return IntersakyData.CONVERSATION_TYPE_REPORT;
        }
        else if(type.endsWith("Leave")) {
            return IntersakyData.CONVERSATION_TYPE_LEAVE;
        }
        else if(type.endsWith("Schedule")) {
            return IntersakyData.CONVERSATION_TYPE_SCHDULE;
        }
        else if(type.endsWith("Notice")) {
            return IntersakyData.CONVERSATION_TYPE_NOTICE;
        }
        else if(type.endsWith("Vote")) {
            return IntersakyData.CONVERSATION_TYPE_VOTE;
        }
        else if(type.endsWith("Task")) {
            return IntersakyData.CONVERSATION_TYPE_TASK;
        }
        else if(type.endsWith("iweb[workflow]") || type.endsWith("iCloud[workflow]")) {
            return IntersakyData.CONVERSATION_TYPE_IWEB_APPROVE;
        }
        else if(type.endsWith("iweb[mail]") || type.endsWith("iCloud[mail]")) {
            return IntersakyData.CONVERSATION_TYPE_IWEB_MAIL;
        }
        else if(type.endsWith("message")) {
            return IntersakyData.CONVERSATION_TYPE_MESSAGE;
        }
        return "";
    }
}
