package com.intersky.strang.android.asks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.intersky.strang.android.view.StrangApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.FilesizeNetTask;
import intersky.xpxnet.net.nettask.PostJsonNetTask;
import intersky.xpxnet.net.nettask.PostNetTask;

public class ImAsks {

    public static final String NOTATION_MESSAGE_IM = "im/unread";
    public static final String MESSAGE_UPLOAD= "im/upload";
    public static final String MESSAGE_SEND= "im/send";
    public static final String MESSAGE_DOWNLOAD= "im/download";
    public static final int IM_HIT_SUCCESS = 1212000;
    public final static int UPLOAD_MESSAGE_FILE_SUCCESS = 1212001;
    public final static int SEND_MESSAGE_SUCCESS = 1212002;


    public static void getImMessage(Context mContext, Handler mHandler,int event) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,NOTATION_MESSAGE_IM);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, event, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getImMessage(Context mContext, Handler mHandler, int event, Contacts contacts) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,NOTATION_MESSAGE_IM);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, event, mContext, postBody,contacts);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getImFileInfo(Context mContext, Handler mHandler,Conversation conversation,int event) {
        String urlString = ImAsks.imFileDownloadUrl(conversation.sourceId);
        FilesizeNetTask mPostNetTask = new FilesizeNetTask(urlString, mHandler, event, mContext,conversation);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void uploadFile(Context mContext, Handler mHandler,int success, Conversation msg) {
        File mfile = new File(msg.sourcePath);
        if (mfile.exists()) {
            long size = mfile.length();
            msg.sourceSize = size;
        }
        String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,MESSAGE_UPLOAD);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("from", ChatUtils.getChatUtils().mAccount.mUserName);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("to", msg.mTitle);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("ext", String.valueOf(msg.mHit));
        nameValuePairs.add(mNameValuePair);
        if(msg.sourceType != Conversation.MESSAGE_TYPE_MAP)
        mNameValuePair = new NameValuePair("filename", msg.sourceName);
        else
            mNameValuePair = new NameValuePair("filename", mfile.getName());
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("file", mfile.getName());
        mNameValuePair.isFile = true;
        mNameValuePair.path = mfile.getPath();
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, success, mContext, nameValuePairs, msg);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void sendMsg(Context mContext, Handler mHandler,int success, Conversation msg,long time,int repeat) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,MESSAGE_SEND);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("token", NetUtils.getInstance().token);
            String times = String.valueOf(time).substring(0,10)+"."+String.valueOf(time).substring(10,13);
            jsonObject1.put("msg_time", times);
            jsonObject1.put("repeat", repeat);
            jsonObject1.put("to", msg.mTitle);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", ChatUtils.getChatUtils().mAccount.mUserName);
            if(msg.sourceId.length() != 0)
            {
                jsonObject.put("fileid", msg.sourceId);
                jsonObject.put("percent",0);
                jsonObject.put("length", String.valueOf(msg.mHit));
                if(msg.sourceType != Conversation.MESSAGE_TYPE_MAP)
                {
                    jsonObject.put("filename",msg.sourceName);
                }
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

            jsonObject.put("msg",msg.mSubject);
            jsonObject.put("msgid", msg.mRecordId);
            jsonObject.put("is_im",true);
            jsonObject.put("to_userid",msg.detialId);
            jsonObject.put("userid",ChatUtils.getChatUtils().mAccount.mRecordId);
            jsonObject.put("type", msg.sourceType);
            if(msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
            {
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
                jsonObject.put("img_width",options.outWidth);
                jsonObject.put("img_height",options.outHeight);
            }
            jsonObject.put("time", TimeUtils.getDate()+" "+TimeUtils.getTime());
            jsonObject.put("kind",0);
            jsonObject.put("to_username",msg.mTitle);
            jsonObject1.put("msg", jsonObject.toString());
            String postBody = jsonObject1.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, success,
                     mContext, postBody,  msg);
            NetTaskManager.getInstance().addNetTask(mPostNetTask,"imchat");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String imFileDownloadUrl(String id) {

        String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,MESSAGE_DOWNLOAD);
        urlString += "?file_id=" + id + "&token=" + NetUtils.getInstance().token;
        return urlString;
    }

    public static String imFileDownloadUrlg(String id) {

        String urlString = NetUtils.getInstance().praseUrl(StrangApplication.mApp.mService,MESSAGE_DOWNLOAD);
        urlString += "?file_id=" + id;
        return urlString;
    }
}
