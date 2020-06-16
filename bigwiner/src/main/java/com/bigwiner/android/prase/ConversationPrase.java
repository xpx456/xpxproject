package com.bigwiner.android.prase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.umeng.commonsdk.debug.I;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import intersky.appbase.ConversationOrderTimeDes;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.ConversationManager;
import intersky.conversation.NotifictionManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.conversation.handler.SendMessageHandler;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.select.entity.MapSelect;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class ConversationPrase {


    public static boolean praseConversationHis(Context context, NetObject net) {
        String json = net.result;
        if (AppUtils.success(json) == false) {
            AppUtils.showMessage(context, AppUtils.getfailmessage(json));
            return false;
        }
        if (AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        int type = (int) net.item;
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray jsonArray = jsonObject.getJSONArray("list");
            if (type == 1) {
                BigwinerApplication.mApp.conversationManager.hismodul.currentpage = jsonObject.getInt("pageNo", 1);
                BigwinerApplication.mApp.conversationManager.hismodul.pagesize = jsonObject.getInt("page_size", 20);
                BigwinerApplication.mApp.conversationManager.hismodul.totlepage = jsonObject.getInt("totalPage", 1);
                BigwinerApplication.mApp.conversationManager.hismodul.totleszie = jsonObject.getInt("totalcount", 0);
            } else {
                BigwinerApplication.mApp.conversationManager.activitysmodul.currentpage = jsonObject.getInt("pageNo", 1);
                BigwinerApplication.mApp.conversationManager.activitysmodul.pagesize = jsonObject.getInt("page_size", 20);
                BigwinerApplication.mApp.conversationManager.activitysmodul.totlepage = jsonObject.getInt("totalPage", 1);
                BigwinerApplication.mApp.conversationManager.activitysmodul.totleszie = jsonObject.getInt("totalcount", 0);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                XpxJSONObject jo = jsonArray.getJSONObject(i);
                Conversation conversation = new Conversation();
                conversation.detialId = jo.getString("rid");
                conversation.mSubject = jo.getString("tital");
                conversation.mTime = jo.getString("postdate");
                conversation.sourceId = jo.getString("photo");
                if (jo.getString("type").equals(context.getString(R.string.histroy_type_meeting))) {
                    conversation.mType = Conversation.CONVERSATION_TYPE_MEETING;
                    conversation.mTitle = jo.getString("provinceorcity");
                    conversation.mTime = jo.getString("startdate");
                    conversation.mTime2 = jo.getString("enrollenddate");
                    conversation.isSendto = jo.getBoolean("isenroll",false);
                    conversation.mHit = jo.getInt("participantscount",0);
                    conversation.mHit2 = jo.getInt("limitednum",0);
                    conversation.sourceName = jo.getString("status");
                    conversation.sourcePath = jo.getString("originallink");
                }
                else if (jo.getString("type").equals(context.getString(R.string.histroy_type_new))) {
                    conversation.mType = Conversation.CONVERSATION_TYPE_NEWS;
                } else if (jo.getString("type").equals(context.getString(R.string.histroy_type_notice))) {
                    conversation.mType = Conversation.CONVERSATION_TYPE_NOTICE;
                }
//                if (type == 1) {
//                    measureTime(BigwinerApplication.mApp.conversationManager.historys,conversation,BigwinerApplication.mApp.conversationManager.hismodul);
//                } else {
//                    measureTime(BigwinerApplication.mApp.conversationManager.activitys,conversation,BigwinerApplication.mApp.conversationManager.activitysmodul);
//                }
                if (type == 1)
                    BigwinerApplication.mApp.conversationManager.historys.add(conversation);
                else
                {
                    if (jo.getString("type").equals(context.getString(R.string.histroy_type_meeting)))
                    BigwinerApplication.mApp.conversationManager.activitys.add(conversation);
                }

            }
            if (type == 1) {
                BigwinerApplication.mApp.conversationManager.hismodul.currentszie = jsonObject.getInt("totalcount", BigwinerApplication.mApp.conversationManager.historys.size());
            } else {
                BigwinerApplication.mApp.conversationManager.activitysmodul.currentszie = jsonObject.getInt("totalcount", BigwinerApplication.mApp.conversationManager.activitys.size());
            }
            return true;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
    }

    public static void measureTime(ArrayList<Conversation> conversations,Conversation conversation,ModuleDetial detial) {
        if (conversation.mTime.substring(0, 10).equals(TimeUtils.getDate())) {
            if(detial.today == null)
            {
                Conversation conversation1 = new Conversation();
                conversation1.mType = Conversation.CONVERSATION_TYPE_TITLE;
                conversation1.mTime = conversation.mTime.substring(0,10);
                conversations.add(0,conversation1);
                conversations.add(detial.todaycount+1,conversation);
                detial.todaycount++;
            }
        } else if (conversation.mTime.substring(0, 10).equals(TimeUtils.getYesterdayByDate())) {
            if(detial.yestday == null)
            {
                Conversation conversation1 = new Conversation();
                conversation1.mType = Conversation.CONVERSATION_TYPE_TITLE;
                conversation1.mTime = conversation.mTime.substring(0,10);
                conversations.add(detial.todaycount+1,conversation1);
                conversations.add(detial.todaycount+1+detial.yestdaycount+1,conversation);
                detial.yestdaycount++;
            }
        } else {
            if(detial.before == null)
            {
                Conversation conversation1 = new Conversation();
                conversation1.mType = Conversation.CONVERSATION_TYPE_TITLE;
                conversation1.mTime = conversation.mTime.substring(0,10);
                conversations.add(detial.todaycount+1+detial.yestdaycount+1,conversation1);
                conversations.add(conversation);
                detial.beforecount++;
            }
        }
    }

    public static boolean praseConversationNoticeAndNews(Context context,NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }

        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray jsonArray = jsonObject.getJSONArray("list");
            ArrayList<Conversation> conversations;
            ModuleDetial moduleDetial;
            if(net.item.equals(ConversationAsks.TYPE_NOTICE))
            {
                moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE);
                conversations = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NOTICE);
                if(conversations == null)
                {
                    conversations = new ArrayList<Conversation>();
                    BigWinerConversationManager.getInstance().collectionConversation.put(Conversation.CONVERSATION_TYPE_NOTICE,conversations);
                }
            }
            else
            {
                moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS);
                conversations = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NEWS);
                if(conversations == null)
                {
                    conversations = new ArrayList<Conversation>();
                    BigWinerConversationManager.getInstance().collectionConversation.put(Conversation.CONVERSATION_TYPE_NEWS,conversations);
                }
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo",1);
            moduleDetial.pagesize = jsonObject.getInt("page_size",20);
            moduleDetial.totlepage = jsonObject.getInt("totalPage",1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount",0);
            for(int i = 0 ; i < jsonArray.length() ; i++) {
                XpxJSONObject jo = jsonArray.getJSONObject(i);
                Conversation conversation;
                if(net.item.equals(ConversationAsks.TYPE_NOTICE))
                {
                    conversation = praseNoticeOrNew(jo,Conversation.CONVERSATION_TYPE_NOTICE);
                }
                else
                {
                    conversation = praseNoticeOrNew(jo,Conversation.CONVERSATION_TYPE_NEWS);
                }
                if(conversation.isRead == false )
                {
                    BigwinerApplication.mApp.conversationManager.unreads.put(conversation.detialId,conversation);
                }

                conversations.add(conversation);
                if(conversations.size() <= moduleDetial.allpagesizemax)
                {
                    measureAllList(conversation);
                }
            }
            //Collections.sort( conversations,new ConversationOrderTimeDes());
            moduleDetial.currentszie = jsonObject.getInt("totalcount",conversations.size());
            return  true;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
    }

    public static boolean praseConversationMeeting(Context context,NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray jsonArray = jsonObject.getJSONArray("list");
            ArrayList<Conversation> conversations = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING);
            ModuleDetial moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING);
            if(conversations == null)
            {
                conversations = new ArrayList<Conversation>();
                BigWinerConversationManager.getInstance().collectionConversation.put(Conversation.CONVERSATION_TYPE_MEETING,conversations);
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo",1);
            moduleDetial.pagesize = jsonObject.getInt("page_size",20);
            moduleDetial.totlepage = jsonObject.getInt("totalPage",1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount",0);
            for(int i = 0 ; i < jsonArray.length() ; i++) {
                XpxJSONObject jo = jsonArray.getJSONObject(i);
                Conversation conversation = praseMeeting(jo,Conversation.CONVERSATION_TYPE_MEETING);
                conversation.mType = Conversation.CONVERSATION_TYPE_MEETING;
                conversations.add(conversation);
                if(conversations.size() <= moduleDetial.allpagesizemax)
                {
                    measureAllList(conversation);
                }
            }
            //Collections.sort( conversations,new ConversationOrderTimeDes());
            moduleDetial.currentszie = jsonObject.getInt("totalcount",conversations.size());
            return  true;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
    }


    public static void praseMessage(Context mainActivity,NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(mainActivity,AppUtils.getfailmessage(json));
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONArray ja = jObject.getJSONArray("data");
            ModuleDetial moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MESSAGE);
            int addcount = 0;
            ArrayList<Conversation> files = new ArrayList<Conversation>();
            ArrayList<Conversation> conversationss = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE);
            HashMap<String,ArrayList<Conversation>> addhash = new HashMap<String,ArrayList<Conversation>>();
            for (int i = 0; i < ja.length(); i++) {
                addcount++;
                XpxJSONObject jo = (XpxJSONObject) ja.getJSONObject(i);
                XpxJSONObject content = (XpxJSONObject) new XpxJSONObject(jo.getString("Content"));
                Conversation mConversation = new Conversation();
                mConversation.issend = Conversation.MESSAGE_STATAUE_SUCCESS;
                mConversation.mTime = content.getString("time");
                mConversation.mTitle = content.getString("realname");
                if(mConversation.mTitle.length() == 0)
                    mConversation.mTitle = content.getString("username");

                mConversation.mSubject = content.getString("msg");
                mConversation.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
                mConversation.mHit = content.getInt("length",0);
                mConversation.isRead = false;
                mConversation.isSendto = false;
                mConversation.detialId = content.getString("userid");
                mConversation.sourceType = content.getInt("type",0);
                if (mConversation.sourceType != Conversation.MESSAGE_TYPE_NOMAL) {
                    mConversation.sourceName = content.getString("filename");
                    mConversation.sourceId  = content.getString("fileid");
                    if (mConversation.sourceType == Conversation.MESSAGE_TYPE_FILE) {
                        mConversation.mSubject = "[文件]";
                        files.add(mConversation);
                        mConversation.sourcePath = BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/file/"+mConversation.detialId)+"/"+mConversation.sourceName;
                        Conversation source = new Conversation(mConversation);
                        Message message = new Message();
                        message.obj = new Conversation(source);
                        message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                        if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                            ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
                    } else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
                        mConversation.mSubject = "[图片]";
                        files.add(mConversation);
                        mConversation.sourcePath = BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/img/"+mConversation.detialId)+"/"+mConversation.sourceName;
                        Conversation source = new Conversation(mConversation);
                        Message message = new Message();
                        message.obj = new Conversation(source);
                        message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                        if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                            ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
                    } else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_VOICE) {
                        mConversation.mSubject = "[语音]";
                        mConversation.sourcePath = BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/voice/"+mConversation.detialId)+"/"+mConversation.sourceName;
                        Conversation source = new Conversation(mConversation);
                        Message message = new Message();
                        message.obj = new Conversation(source);
                        message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                        if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                            ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
                    } else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_MAP) {
                        JSONObject map = new JSONObject();
                        map.put("longitude",content.getDouble("longitude",0));
                        map.put("latitude",content.getDouble("latitude",0));
                        map.put("locationName",content.getString("locationName"));
                        map.put("locationAddress",content.getString("locationAddress"));
                        mConversation.mSubject = "位置:"+content.getString("locationName");
                        mConversation.sourceName = map.toString();
                        files.add(mConversation);
                        mConversation.sourcePath = BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/map/"+mConversation.detialId)+"/"+content.getString("filename");
                        Conversation source = new Conversation(mConversation);
                        Message message = new Message();
                        message.obj = new Conversation(source);
                        message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                        if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                            ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
                    }
                    else if (mConversation.sourceType == Conversation.MESSAGE_TYPE_CARD) {
                        JSONObject map = new JSONObject();
                        map.put("carduserid",content.getString("carduserid"));
                        map.put("cardusername",content.getString("cardusername"));
                        map.put("carduserphone",content.getString("carduserphone"));
                        mConversation.mSubject = "名片";
                        mConversation.sourceName = map.toString();
//                        files.add(mConversation);
//                        mConversation.sourcePath = BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/map/"+mConversation.detialId)+"/"+content.getString("filename");
                    }

                    File file = new File(mConversation.sourcePath);
                    if(file.exists())
                    {
                        file.delete();
                    }
                }
                ArrayList<Conversation> conversations = BigwinerApplication.mApp.conversationManager.messageConversation.get(mConversation.detialId);
                HashMap<String,Conversation> hashMap = BigwinerApplication.mApp.conversationManager.messageConversation2.get(mConversation.detialId);
                if(!addhash.containsKey(mConversation.detialId))
                {
                    ArrayList<Conversation> addlist = new ArrayList<Conversation>();
                    addlist.add(0,mConversation);
                    addhash.put(mConversation.detialId,addlist);
                }
                else
                {
                    ArrayList<Conversation> addlist = addhash.get(mConversation.detialId);
                    addlist.add(0,mConversation);
                }

                Conversation conversationh = BigwinerApplication.mApp.conversationManager.messageHConversation.get(mConversation.detialId);
                if(conversationh == null)
                {
                    conversationh = new Conversation();
                    conversationss.add(conversationh);
                    BigwinerApplication.mApp.conversationManager.messageHConversation.put(mConversation.detialId,conversationh);

                }
                conversationh.mTime = content.getString("time");
                conversationh.mTitle = content.getString("realname");
                if(conversationh.mTitle.length() == 0)
                    conversationh.mTitle = content.getString("username");
                conversationh.mSubject = content.getString("msg");
                conversationh.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
                conversationh.mHit++;
                conversationh.isRead = false;
                conversationh.isSendto = false;
                conversationh.detialId = content.getString("userid");
                conversationh.sourceType = content.getInt("type",0);
                if (conversationh.sourceType != Conversation.MESSAGE_TYPE_NOMAL) {
                    conversationh.sourceName = content.getString("filename");
                    conversationh.sourceId  = content.getString("fileid");
                    if (conversationh.sourceType == Conversation.MESSAGE_TYPE_FILE) {
                        conversationh.mSubject = "[文件]";
                    } else if (conversationh.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
                        conversationh.mSubject = "[图片]";
                    } else if (conversationh.sourceType == Conversation.MESSAGE_TYPE_VOICE) {
                        conversationh.mSubject = "[语音]";
                    }
                }


                if(conversations == null)
                {
                    conversations = new ArrayList<Conversation>();
                    BigwinerApplication.mApp.conversationManager.messageConversation.put(mConversation.detialId,conversations);

                }
                conversations.add(0,mConversation);
                if(hashMap == null)
                {
                    hashMap = new HashMap<String,Conversation>();
                    BigwinerApplication.mApp.conversationManager.messageConversation2.put(mConversation.detialId,hashMap);
                }
                hashMap.put(mConversation.mRecordId,mConversation);
//                Collections.sort(conversations,new ConversationOrderTimeDes());
                Collections.sort(conversationss,new ConversationOrderTimeDes());
                if(mConversation.mTitle.length() == 0)
                {
                    if(NotifictionManager.mNotifictionManager.membernames.containsKey(mConversation.detialId))
                        mConversation.mTitle = NotifictionManager.mNotifictionManager.membernames.get(mConversation.detialId);
                }
                if(conversationh.mTitle.length() == 0)
                {
                    if(NotifictionManager.mNotifictionManager.membernames.containsKey(mConversation.detialId))
                    conversationh.mTitle = NotifictionManager.mNotifictionManager.membernames.get(mConversation.detialId);
                }

                if(mConversation.mTitle.length()==0)
                {
                    Intent intent = new Intent(MainActivity.ACTION_LEAVE_MESSAGE_NAME);
                    intent.putExtra("msg",mConversation);
                    intent.setPackage(BigwinerApplication.mApp.getPackageName());
                    mainActivity.sendBroadcast(intent);
                }

                BigWinerDBHelper.getInstance(mainActivity).addConversation(mConversation);
            }
            if(moduleDetial.allpagesize == 0) {
                if(conversationss.size() == 1)
                {
                    BigwinerApplication.mApp.conversationManager.mConversations.add(0,conversationss.get(0));
                    moduleDetial.allpagesize = 1;
                }
                else  if(conversationss.size() > 1)
                {
                    BigwinerApplication.mApp.conversationManager.mConversations.add(0,conversationss.get(0));
                    BigwinerApplication.mApp.conversationManager.mConversations.add(1,conversationss.get(1));
                    moduleDetial.allpagesize = 2;
                }
                else
                {
                    moduleDetial.allpagesize = 0;
                }
            }
            else if(moduleDetial.allpagesize == 1) {
                BigwinerApplication.mApp.conversationManager.mConversations.remove(0);
                if(conversationss.size() == 1)
                {
                    moduleDetial.allpagesize = 1;
                    BigwinerApplication.mApp.conversationManager.mConversations.add(0,conversationss.get(0));
                }
                else  if(conversationss.size() > 1)
                {
                    BigwinerApplication.mApp.conversationManager.mConversations.add(0,conversationss.get(0));
                    BigwinerApplication.mApp.conversationManager.mConversations.add(1,conversationss.get(1));
                    moduleDetial.allpagesize = 2;
                }
                else
                {
                    moduleDetial.allpagesize = 0;
                }
            }
            else if(moduleDetial.allpagesize == 2) {
                BigwinerApplication.mApp.conversationManager.mConversations.remove(0);
                BigwinerApplication.mApp.conversationManager.mConversations.remove(0);
                if(conversationss.size() == 1)
                {
                    moduleDetial.allpagesize = 1;
                    BigwinerApplication.mApp.conversationManager.mConversations.add(0,conversationss.get(0));
                }
                else if(conversationss.size() > 1)
                {
                    moduleDetial.allpagesize = 2;
                    BigwinerApplication.mApp.conversationManager.mConversations.add(0,conversationss.get(0));
                    BigwinerApplication.mApp.conversationManager.mConversations.add(1,conversationss.get(1));
                }
                else
                {
                    moduleDetial.allpagesize = 0;
                }
            }

            for(HashMap.Entry<String, ArrayList<Conversation>> entry: addhash.entrySet())
            {
                ArrayList<Conversation> conversations = entry.getValue();
                if(conversations.size() >  0)
                {
                    Intent intent = new Intent(BigWinerConversationManager.ACTION_ADD_CONVERSATION_MESSAGE);
                    intent.putExtra("addcount",conversations.size());
                    intent.putExtra("msgs",conversations);
                    intent.putExtra("detialid",entry.getKey());
                    intent.setPackage(BigwinerApplication.mApp.getPackageName());
                    mainActivity.sendBroadcast(intent);
                }

            }

        }catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static void praseSearch(ArrayList<Conversation> conversations ,Context context,NetObject net) {
        String json = net.result;
        conversations.clear();
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        String type = (String) net.item;
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray ja = jsonObject.getJSONArray(type);
            for(int j = 0 ; j < ja.length(); j++ )
            {
                XpxJSONObject jo = ja.getJSONObject(j);
                Conversation conversation = null;
                if(type.equals("notice"))
                {
                    conversation = praseNoticeOrNew(jo,Conversation.CONVERSATION_TYPE_NOTICE);
                }
                else if(type.equals("new"))
                {
                    conversation = praseNoticeOrNew(jo,Conversation.CONVERSATION_TYPE_NEWS);
                }
                else if(type.equals("conference"))
                {
                    conversation = praseMeeting(jo,Conversation.CONVERSATION_TYPE_MEETING);
                }
                if(conversation != null)
                    conversations.add(conversation);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public static void praseBaseData(Context context, NetObject net, MapSelect selects) {
//        String json = net.result;
//        if(AppUtils.success(json) == false)
//        {
//            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
//        }
//        if(AppUtils.measureToken(json) != null) {
//            NetUtils.getInstance().token = AppUtils.measureToken(json);
//        }
//        try {
//            XpxJSONObject jObject = new XpxJSONObject(json);
//            XpxJSONArray ja = jObject.getJSONArray("data");
//            for(int i = 0 ; i < ja.length() ; i++)
//            {
//                Select select = new Select(ja.getString(i),ja.getString(i));
//                selects.list.add(select);
//                selects.hashMap.put(select.mId,select);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public static void praseBaseData(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray ja = jsonObject.getJSONArray("cityorport");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                Select select = new Select(ja.getString(i),ja.getString(i));
                BigwinerApplication.mApp.ports.list.add(select);
                BigwinerApplication.mApp.ports.hashMap.put(select.mId,select);
            }
            ja = jsonObject.getJSONArray("position");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                Select select = new Select(ja.getString(i),ja.getString(i));
                BigwinerApplication.mApp.positions.list.add(select);
                BigwinerApplication.mApp.positions.hashMap.put(select.mId,select);
            }
            ja = jsonObject.getJSONArray("businesstypes");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                Select select = new Select(ja.getString(i),ja.getString(i));
                BigwinerApplication.mApp.businesstypeSelect.list.add(select);
                BigwinerApplication.mApp.businesstypeSelect.hashMap.put(select.mId,select);
            }
            ja = jsonObject.getJSONArray("areas");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                Select select = new Select(ja.getString(i),ja.getString(i));
                BigwinerApplication.mApp.businessareaSelect.list.add(select);
                BigwinerApplication.mApp.businessareaSelect.hashMap.put(select.mId,select);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public static void praseSourceData(Context context,NetObject net) {
//        String json = net.result;
//        if(AppUtils.success(json) == false)
//        {
//            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
//        }
//        if(AppUtils.measureToken(json) != null) {
//            NetUtils.getInstance().token = AppUtils.measureToken(json);
//        }
//        String[] memo = getsource(context);
//        try {
//            BigwinerApplication.mApp.ports.clear();
//            BigwinerApplication.mApp.positions.clear();
//            BigwinerApplication.mApp.routes.clear();
//            BigwinerApplication.mApp.serices.clear();
//            XpxJSONObject jObject = new XpxJSONObject(json);
//            XpxJSONObject jsonObject = jObject.getJSONObject("data");
//            XpxJSONArray ports = jsonObject.getJSONArray("ports");
//            for(int i = 0 ; i < ports.length() ; i++)
//            {
//                Select source = new Select(ports.getString(i),ports.getString(i));
//                BigwinerApplication.mApp.ports.list.add(source);
//                BigwinerApplication.mApp.ports.hashMap.put(source.mId,source);
//                if(memo[0].equals(source.mName))
//                {
//                    BigwinerApplication.mApp.my.ports = source;
//                }
//                if(memo[4].equals(source.mName))
//                {
//                    BigwinerApplication.mApp.want.ports = source;
//                }
//            }
//            XpxJSONArray positions = jsonObject.getJSONArray("positions");
//            for(int i = 0 ; i < positions.length() ; i++)
//            {
//                Select source = new Select(positions.getString(i),positions.getString(i));
//                BigwinerApplication.mApp.positions.list.add(source);
//                BigwinerApplication.mApp.positions.hashMap.put(source.mId,source);
//                if(memo[1].equals(source.mName))
//                {
//                    BigwinerApplication.mApp.my.positions = source;
//                }
//                if(memo[5].equals(source.mName))
//                {
//                    BigwinerApplication.mApp.want.positions = source;
//                }
//            }
//            XpxJSONArray routes = jsonObject.getJSONArray("routes");
//            for(int i = 0 ; i < routes.length() ; i++)
//            {
//                Select source = new Select(routes.getString(i),routes.getString(i));
//                BigwinerApplication.mApp.routes.list.add(source);
//                BigwinerApplication.mApp.routes.hashMap.put(source.mId,source);
//                if(memo[2].equals(source.mName))
//                {
//                    BigwinerApplication.mApp.my.routes = source;
//                }
//                if(memo[6].equals(source.mName))
//                {
//                    BigwinerApplication.mApp.want.routes = source;
//                }
//            }
//            XpxJSONArray serices = jsonObject.getJSONArray("serices");
//            for(int i = 0 ; i < serices.length() ; i++)
//            {
//                Select source = new Select(serices.getString(i),serices.getString(i));
//                BigwinerApplication.mApp.serices.list.add(source);
//                BigwinerApplication.mApp.serices.hashMap.put(source.mId,source);
//                if(memo[3].equals(source.mName))
//                {
//                    BigwinerApplication.mApp.my.serices = source;
//                }
//                if(memo[7].equals(source.mName))
//                {
//                    BigwinerApplication.mApp.want.serices = source;
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private static String[] getsource(Context context)
    {
        SharedPreferences sharedPre1 = context.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
        String[] strings = new String[8];
        strings[0] = sharedPre1.getString(UserDefine.MY_PORT,"");
        strings[1] = sharedPre1.getString(UserDefine.MY_POSI,"");
        strings[2] = sharedPre1.getString(UserDefine.MY_ROUTE,"");
        strings[3] = sharedPre1.getString(UserDefine.MY_SERVICE,"");
        strings[4] = sharedPre1.getString(UserDefine.WANT_PORT,"");
        strings[5] = sharedPre1.getString(UserDefine.WANT_POSI,"");
        strings[6] = sharedPre1.getString(UserDefine.WANT_ROUTE,"");
        strings[7] = sharedPre1.getString(UserDefine.WANT_SERVICE,"");
        return strings;
    }

    public static Conversation praseNoticeOrNew(XpxJSONObject jo,String type)
    {
        Conversation conversation = new Conversation();
        conversation.mType = type;
        conversation.mTitle = jo.getString("classification");
        conversation.mSubject = jo.getString("tital");
        conversation.mHit = jo.getInt("browsecount",0);
        conversation.detialId = jo.getString("no");
        conversation.mTime = jo.getString("postdate");
        conversation.sourceId = jo.getString("photo");
        conversation.sourcePath = jo.getString("originallink");
        conversation.isRead = jo.getBoolean("isread",false);
        return  conversation;
    }

    public static Conversation praseMeeting(XpxJSONObject jo,String type)
    {
        Conversation conversation = new Conversation();
        conversation.mType = type;
        conversation.mTitle = jo.getString("provinceorcity");
        conversation.mSubject = jo.getString("tital");
        conversation.detialId = jo.getString("no");
        conversation.mTime = jo.getString("startdate");
        conversation.mTime2 = jo.getString("enrollenddate");
        conversation.isSendto = jo.getBoolean("isenroll",false);
        conversation.mHit = jo.getInt("participantscount",0);
        conversation.mHit2 = jo.getInt("limitednum",0);
        conversation.sourceId = jo.getString("photo");
        conversation.sourceName = jo.getString("status");
        return  conversation;
    }

    public static void measureAllList(Conversation conversation) {
        ModuleDetial moduleDetial1 = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MESSAGE);
        ModuleDetial moduleDetial2 = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE);
        ModuleDetial moduleDetial3 = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS);
        ModuleDetial moduleDetial4 = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING);


        if(conversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE))
        {
            BigwinerApplication.mApp.conversationManager.mConversations.add(moduleDetial1.allpagesize,conversation);
            moduleDetial1.allpagesize++;
        }
        else if(conversation.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE))
        {
            BigwinerApplication.mApp.conversationManager.mConversations.add(moduleDetial1.allpagesize+moduleDetial2.allpagesize,conversation);
            moduleDetial2.allpagesize++;
        }
        else if(conversation.mType.equals(Conversation.CONVERSATION_TYPE_NEWS))
        {
            BigwinerApplication.mApp.conversationManager.mConversations.add(moduleDetial1.allpagesize+moduleDetial2.allpagesize+moduleDetial3.allpagesize,conversation);
            moduleDetial3.allpagesize++;
        }
        else if(conversation.mType.equals(Conversation.CONVERSATION_TYPE_MEETING))
        {
            BigwinerApplication.mApp.conversationManager.mConversations.add(moduleDetial1.allpagesize+moduleDetial2.allpagesize
                    +moduleDetial3.allpagesize+moduleDetial4.allpagesize,conversation);
            moduleDetial4.allpagesize++;
        }
    }

    public static Intent praselink(Context context, NetObject net) {
        String json = net.result;
        Intent intent = (Intent) net.item;
        if (AppUtils.success(json) == false) {
            AppUtils.showMessage(context, AppUtils.getfailmessage(json));
            return intent;
        }
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            if(jObject.getString("originallink").length() != 0)
            {
                intent.putExtra("url", jObject.getString("originallink"));
                if(jObject.getString("type").equals(context.getString(R.string.conversation_notice)))
                {
                    intent.removeExtra("type");
                    intent.putExtra("type",Conversation.CONVERSATION_TYPE_NOTICE);
                }
                else  if(jObject.getString("type").equals(context.getString(R.string.conversation_news)))
                {
                    intent.removeExtra("type");
                    intent.putExtra("type",Conversation.CONVERSATION_TYPE_NEWS);
                }
                return intent;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return intent;
        }
        return intent;
    }

//    public static String prseShareUrl(String type,String detialid) {
//        return "https://www.cargounions.com/biggerWinner_redirect/"+type+"/"+detialid;
//    }

    public static String prseShareUrl(String type,String detialid) {
        return "https://www.cargounions.com/biggerWinner_redirect/h5/detail/"+type+"/"+detialid;
    }
}
