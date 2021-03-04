package com.bigwiner.android.asks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.entity.Conversation;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.FilesizeNetTask;
import intersky.xpxnet.net.nettask.NetTask;
import intersky.xpxnet.net.nettask.PostJsonNetRegetTask;
import intersky.xpxnet.net.nettask.PostJsonNetTask;
import intersky.xpxnet.net.nettask.PostNetTask;

public class ConversationAsks {

    public static final String TYPE_NEWS = "new";
    public static final String TYPE_NOTICE = "notice";
    public static final String TYPE_MEETING = "conference";
    public static final String TYPE_FRIEND = "friend";
    public static final String NEW_NOTICE_PATH = "/bigwinner/query/new_page_all";
    public static final String MESSAGE_PATH = "/bigwinner/query/msg/unread";
    public static final String MESSAGE_SEND = "/bigwinner/add/msg/send";
    public static final String MESSAGE_UPLOAD = "/bigwinner/add/msg/upload";
    public static final String HISTORY_PATH = "/bigwinner/query/content/browse/result/all";
    public static final String MEETING_PATH = "/bigwinner/query/conference_all";
    public static final String SEARCH_PATH = "/bigwinner/query/search_by_keyword";
    public static final String BASE_DATA_PATH = "/bigwinner/query/base/data";
    public static final String SOURCE_DATA_PATH = "/bigwinner/query/resources/all";
    public static final String UPDATA_COUNT = "/bigwinner/originallink/update/browser";
    public static final String GET_COUNT = "/bigwinner/query/outer/chain";
    public static final int NEW_NOTICE_RESULT = 100100;
    public static final int MEETING_RESULT = 100101;
    public static final int MESSAGE_RESULT = 100104;
    public static final int SEARCH_RESULT = 100102;
    public static final int BASE_DATA_RESULT = 100103;
    public static final int BASE_DATA_FAIL = 100109;
    public static final int MESSAGE_UPLOAD_RESULT = 100105;
    public static final int MESSAGE_SEND_RESULT = 100106;
    public static final int HIS_RESULT = 100107;
    public static final int SOURCE_RESULT = 100108;
    public static final int LINK_RESULT = 100110;

    public static void getNewsAndNotices(Context mContext, Handler mHandler, String type, int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH + NEW_NOTICE_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("type", type);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("currentpage", current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, NEW_NOTICE_RESULT,
                    mContext, postBody, type,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getNewsAndNoticesHis(Context mContext, Handler mHandler, int type, int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH + HISTORY_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("type", type);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("currentpage", current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, HIS_RESULT,
                    mContext, postBody, type,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMettings(Context mContext, Handler mHandler, int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH + MEETING_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("currentpage", current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MEETING_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doSearch(Context mContext, Handler mHandler, String keyword, String type) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SEARCH_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("keyword", keyword);
            jsonObject.put("type", type);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SEARCH_RESULT,
                    mContext, postBody, type,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getBaseData(Context mContext, Handler mHandler, String type) {
        String urlString = BigwinerApplication.BASE_NET_PATH + BASE_DATA_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("type", type);
            String postBody = jsonObject.toString();
            PostJsonNetRegetTask mPostNetTask = new PostJsonNetRegetTask(urlString, mHandler, BASE_DATA_RESULT,
                    BASE_DATA_FAIL, mContext, postBody, type,BigwinerApplication.mApp.checkToken);
            mPostNetTask.mRecordId = BigwinerApplication.BASE_NET_PATH + BASE_DATA_PATH;
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMessageUnread(Context mContext, Handler mHandler) {
        String urlString = BigwinerApplication.BASE_NET_PATH + MESSAGE_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, LeaveMessageHandler.GET_LEAVE_MSG_SUCCESS,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(Context mContext, Handler mHandler, Conversation msg, int id) {
        File mfile = new File(msg.sourcePath);
        if (mfile.exists()) {
            long size = mfile.length();
            msg.sourceSize = size;
        }
        String urlString = BigwinerApplication.BASE_NET_PATH + MESSAGE_UPLOAD;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("from", BigwinerApplication.mApp.mAccount.mUserName);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("to", msg.mTitle);
        nameValuePairs.add(mNameValuePair);
        if (msg.sourceType == Conversation.MESSAGE_TYPE_VOICE) {
            mNameValuePair = new NameValuePair("ext", String.valueOf(msg.mHit));
            nameValuePairs.add(mNameValuePair);
        } else {
            mNameValuePair = new NameValuePair("ext", String.valueOf(msg.mHit));
            nameValuePairs.add(mNameValuePair);
        }
        if(msg.sourceType != Conversation.MESSAGE_TYPE_MAP)
        mNameValuePair = new NameValuePair("fileName", mfile.getName());
        else
        {
            mNameValuePair = new NameValuePair("fileName", msg.detialId);
        }

        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("file", mfile.getName());
        mNameValuePair.isFile = true;
        mNameValuePair.path = mfile.getPath();
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, id, mContext, nameValuePairs, msg,BigwinerApplication.mApp.checkToken);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void sendMsg(Context mContext, Handler mHandler, Conversation msg, int id,long time,int repeat) {
        try {
            String urlString = BigwinerApplication.BASE_NET_PATH + MESSAGE_SEND;
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("token", NetUtils.getInstance().token);
            String times = String.valueOf(time).substring(0,10)+"."+String.valueOf(time).substring(10,13);
            jsonObject1.put("msg_time", times);
            jsonObject1.put("repeat", repeat);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", msg.mSubject);
            jsonObject.put("username", BigwinerApplication.mApp.mAccount.mUserName);
            if (msg.sourceId.length() != 0) {
                jsonObject.put("fileid", msg.sourceId);
                jsonObject.put("percent", 0);
                jsonObject.put("length", String.valueOf(msg.mHit));
                if(msg.sourceType != Conversation.MESSAGE_TYPE_MAP && msg.sourceType != Conversation.MESSAGE_TYPE_CARD)
                jsonObject.put("filename", msg.sourceName);
                else
                {
                    JSONObject jo = new JSONObject(msg.sourceName);
                    jsonObject.put("filename", jo.getString("filename"));
                }

            }
            if(msg.sourceType == Conversation.MESSAGE_TYPE_MAP)
            {
                JSONObject jo = new JSONObject(msg.sourceName);
                jsonObject.put("longitude",jo.getDouble("longitude"));
                jsonObject.put("latitude",jo.getDouble("latitude"));
                jsonObject.put("locationName",jo.getString("locationName"));
                jsonObject.put("locationAddress",jo.getString("locationAddress"));
            }
            if(msg.sourceType == Conversation.MESSAGE_TYPE_CARD)
            {
                JSONObject jo = new JSONObject(msg.sourceName);
                jsonObject.put("carduserid",jo.getString("carduserid"));
                jsonObject.put("cardusername",jo.getString("cardusername"));
                jsonObject.put("carduserphone",jo.getString("carduserphone"));
            }
            jsonObject.put("msgid", msg.mRecordId);
            jsonObject.put("is_im", true);
            jsonObject.put("to_userid", msg.detialId);
            jsonObject.put("userid", BigwinerApplication.mApp.mAccount.mRecordId);
            jsonObject.put("type", String.valueOf(msg.sourceType));
            if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
                BitmapFactory.Options options = new BitmapFactory.Options();

                /**
                 * 最关键在此，把options.inJustDecodeBounds = true;
                 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
                 */
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(msg.sourcePath, options); // 此时返回的bitmap为null
                /**
                 *options.outHeight为原始图片的高
                 */
                jsonObject.put("img_width", options.outWidth);
                jsonObject.put("img_height", options.outHeight);
            }
            jsonObject.put("time", TimeUtils.getDate()+" "+TimeUtils.getTimeSecond());
            jsonObject.put("kind", 0);
            jsonObject.put("to_username", msg.mTitle);
            jsonObject.put("realname ", BigwinerApplication.mApp.mAccount.mRealName);
            jsonObject1.put("msg", jsonObject.toString());
            jsonObject1.put("to", msg.mTitle);
            //jsonObject1.put("to", TimeUtils.getTimeUid());
            String postBody = jsonObject1.toString();
            //postBody = "{\"token\":\"9QxZDJygLNCzIirE+poUO4k6rGj1B0Wg\",\"msg\":\"{\\\"msg\\\":\\\"Sssss\\\",\\\"username\\\":\\\"lili\\\",\\\"msgid\\\":\\\"10303EFC-35E8-4677-84E3-26928C5099DC\\\",\\\"is_im\\\":true,\\\"to_userid\\\":\\\"2284010C-737F-4550-BC8A-DBDB3DCB6427\\\",\\\"userid\\\":\\\"942AF822-3CAD-44D5-880B-1E10C79190E2\\\",\\\"type\\\":0,\\\"time\\\":\\\"2019-08-06 08:54:33\\\",\\\"kind\\\":0,\\\"to_username\\\":\\\"zhangli\\\"}\",\"to\":\"zhangli\"}";
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, id,
                    mContext, postBody, msg,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask,"imchat");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getSourceData(Context mContext, Handler mHandler) {
        String urlString = BigwinerApplication.BASE_NET_PATH + SOURCE_DATA_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, SOURCE_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getUpdata(String id) {
        String urlString = BigwinerApplication.BASE_NET_PATH + UPDATA_COUNT + "?no=" + id + "&uid=" + BigwinerApplication.mApp.mAccount.mRecordId;
        NetTask mPostNetTask = new NetTask(urlString, null, SOURCE_RESULT,
                BigwinerApplication.mApp);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
        return urlString;
    }

    public static String getOutlink(Handler handler, String id, String type, Intent intent) {
        String urlString = BigwinerApplication.BASE_NET_PATH + GET_COUNT + "?no=" + id + "&type=" + type;
        NetTask mPostNetTask = new NetTask(urlString, handler, LINK_RESULT,
                BigwinerApplication.mApp, intent);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
        return urlString;
    }


    public static String imFileDownloadUrl(String id) {

        String urlString = BigwinerApplication.mApp.measureImg(id);
        return urlString;
    }

    public static String praseShareType(String type) {
        if(type == null)
        {
            return "";
        }

        if(type.equals(Conversation.CONVERSATION_TYPE_NEWS))
        {
            return "news";
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_NOTICE))
        {
            return "notice";
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_MEETING))
        {
            return "meeting";
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_CONTACT))
        {
            return "user";
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_RESOURCE))
        {
            return "resources";
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_COMPANY))
        {
            return "company";
        }
        else
        {
            return "";
        }
    }

    public static void getImFileInfo(Context mContext, Handler mHandler,Conversation conversation,int event) {
        String urlString = BigwinerApplication.mApp.measureImg(conversation.sourceId);
        FilesizeNetTask mPostNetTask = new FilesizeNetTask(urlString, mHandler, event, mContext,conversation);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
