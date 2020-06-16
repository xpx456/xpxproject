package intersky.mail.asks;

import android.content.Context;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import intersky.mail.MailManager;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailContact;
import intersky.mail.view.activity.MailListActivity;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.HTTP;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.URLEncodedUtils;
import intersky.xpxnet.net.nettask.PostJsonListNetTask;
import intersky.xpxnet.net.nettask.PostJsonNetTask;
import intersky.xpxnet.net.nettask.PostNetTask;

public class MailAsks {

    public static final String MAIL_MAILBOX = "mail/mailbox";
    public static final String MAIL_UNDERLINES = "mail/underlines";
    public static final String GET_MAIL_COUNT = "mail/badges"; // 未读已读数
    public static final String MAIL_SETUSER = "mail/setuser"; // 设置当前用户
    public static final String GET_MAIL_LIST = "mail/query";
    public static final String DELETE_MIAL = "mail/delete";
    public static final String MAIL_VIEW = "mail/view";
    public static final String MAIL_DOWNLOAD_ATTACHMENT = "mail/attachment/get";
    public static final String NEW_MAIL_PATH = "mail/action";
    public final static String GET_USER_GUEST = "mail/recipients";
    public static final String SEND_MAIL_PATH = "mail/save";
    public static final String GET_USER_LABLE = "mail/label"; // 获取用户标签
    public static final String MAIL_FENGFA = "mail/distribute";
    public static final String MAIL_APPROVEAL = "mail/approval";
    public static final String ATTACHMENT_PATH_CLOUD = "api/edit/upload";

    public static final int MAIL_MAILBOX_SUCCESS = 1200000;
    public static final int MAIL_MAILBOX_FAIL = 1424000;
    public static final int MAIL_UNDERLINES_SUCCESS = 1200001;
    public static final int MAIL_SETUSER_SUCCESS = 1200002;
    public static final int MAIL_SETUSER_FAIL = 1424002;
    public static final int MAIL_READ_COUNT_SUCCESS = 1200003;
    public static final int MAIL_LIST_SUCCESS = 1200004;
    public static final int MAIL_DELETE_SUCCESS = 1200005;
    public static final int MAIL_DELETE_FAIL = 1424005;
    public static final int MAIL_DELETE_SUCCESS_FINAL = 1200014;
    public static final int MAIL_VIEW_SUCCESS = 1200006;
    public static final int MAIL_NEW_MAIL_SUCCESS = 1200007;
    public static final int MAIL_GET_USER_GUEST_SUCCESS = 1200008;
    public static final int MAIL_GET_USER_GUEST_FAIL = 1424008;
    public static final int MAIL_SEND_SUCCESS = 1200009;
    public static final int MAIL_SAVE_SUCCESS = 1200010;
    public static final int MAIL_LABLE_SUCCESS = 1200011;
    public static final int MAIL_FENGFA_SUCCESS = 1200012;
    public static final int MAIL_APPROVE_SUCCESS = 1200013;

    public static void getUserMailBox(Context mContext, Handler mHandler) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_MAILBOX);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_MAILBOX_SUCCESS,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getUserUnderlines(Context mContext,Handler mHandler) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_UNDERLINES);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_UNDERLINES_SUCCESS,
                     mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setUser(Context mContext, Handler mHandler, MailContact mContacts) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SETUSER);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("user_id", mContacts.mailRecordID);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_SETUSER_SUCCESS,
                    mContext, postBody,mContacts);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getReadCount(Context mContext, Handler mHandler) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_MAIL_COUNT);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mailRecordID);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_READ_COUNT_SUCCESS,
                     mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMials(Context mContext, Handler mHandler, int id, int type, String mailboxid, int start) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_MAIL_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("kind", id);
            jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mailRecordID);
            jsonObject.put("start", start);
            jsonObject.put("limit", 40);
            if (id == MailListActivity.GET_MAIL_SHOUJIANXIAN) {
                if (type == 1 && mailboxid== null) {
                    jsonObject.put("is_client", true);
                } else if (type == 2 && mailboxid == null) {
                    jsonObject.put("is_client", false);
                } else if (mailboxid != null) {
                    jsonObject.put("mailboxid", mailboxid);
                }
            } else if (id == MailListActivity.GET_MAIL_FAJIANXIAN) {
                jsonObject.put("approval", type);
            } else if (id == MailListActivity.GET_MAIL_FENFAXIAN) {
                jsonObject.put("process", type);
            }
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_LIST_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getMials(Context mContext, Handler mHandler, int type1, int type2,int current, String mailboxid, int start,int pagesize) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_MAIL_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("user_id", MailManager.getInstance().account.mRecordId);
            if(type2 != -1)
            {

                jsonObject.put("state", type2);
            }
            else
            {
                jsonObject.put("approval", String.valueOf(type1));
                if(current == 0)
                    jsonObject.put("type", "0");
                else if(current == 1)
                    jsonObject.put("type", "2");
                else if(current == 2)
                    jsonObject.put("type", "1");
                else if(current == 3)
                    jsonObject.put("type", "3");
            }
            if(mailboxid.length() > 0)
                jsonObject.put("mail_box_id", mailboxid);
            jsonObject.put("page",start);
            jsonObject.put("size", pagesize);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_LIST_SUCCESS, mContext, postBody,current);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void searchMials(Context mContext, Handler mHandler,  String  subject) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_MAIL_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("kind", "-1");
            jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mailRecordID);
            jsonObject.put("start", 0);
            jsonObject.put("limit", 999);
            jsonObject.put("subject", subject);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_LIST_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void searchMialsc(Context mContext, Handler mHandler,  String  subject) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_MAIL_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("user_id", MailManager.getInstance().account.mRecordId);
            jsonObject.put("key_word", subject);
            jsonObject.put("page",1);
            jsonObject.put("size", 9999);

            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_LIST_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void deleteMails(Context mContext, Handler mHandler, ArrayList<Mail> mails) {
        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> bodys = new ArrayList<String>();
        ArrayList<Object> mailall = new ArrayList<Object>();
        mailall.addAll(mails);
        for (int i = 0; i < mails.size(); i++) {

            Mail mMailItem = mails.get(i);
            String urlString = "";
            urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,DELETE_MIAL);
            String postBody = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", NetUtils.getInstance().token);
                jsonObject.put("record_id", mMailItem.mRecordId);
                if(mMailItem.state == 3)
                {
                    jsonObject.put("state", 5);
                }
                else
                {
                    jsonObject.put("state", 3);
                }
                postBody = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            urls.add(urlString);
            bodys.add(postBody);
        }
        PostJsonListNetTask mPostNetTask = new PostJsonListNetTask(mHandler, MAIL_DELETE_SUCCESS,
                MAIL_DELETE_SUCCESS_FINAL,mContext, urls, bodys, mailall);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getMailView(Context mContext, Handler mHandler, Mail mail) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_VIEW);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("mail_id", mail.mRecordId);
            jsonObject.put("set_readed", true);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_VIEW_SUCCESS, mContext, postBody,mail);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String measureAttachmentUrl(String mRecordID, String id) {

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("token", NetUtils.getInstance().token));
        nvps.add(new BasicNameValuePair("mail_id", mRecordID));
        nvps.add(new BasicNameValuePair("index", id));
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_DOWNLOAD_ATTACHMENT,
                URLEncodedUtils.format(nvps, HTTP.UTF_8));
        return urlString;
    }

    public static void getmailData(Context mContext, Handler mHandler, String recordid, int action) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,NEW_MAIL_PATH);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("mail_id", recordid);

            jsonObject.put("action", action);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_NEW_MAIL_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendShenpi(Context mContext, Handler mHandler, int state, String memo,Mail mail) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_APPROVEAL);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("record_id", mail.mRecordId);
            jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mailRecordID);
            jsonObject.put("state", state);
            jsonObject.put("memo", memo);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_APPROVE_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void getOutGuest(Context mContext, Handler mHandler) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_USER_GUEST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_GET_USER_GUEST_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendMail(Context mContext, Handler mHandler, ArrayList<NameValuePair> nameValuePairs,boolean isend) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,SEND_MAIL_PATH);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SEND_SUCCESS, mContext, nameValuePairs,isend);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void sendMailc(Context mContext, Handler mHandler, ArrayList<NameValuePair> nameValuePairs,boolean isend) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,SEND_MAIL_PATH);
            JSONObject jsonObject = new JSONObject();
            for(int i = 0 ; i < nameValuePairs.size() ; i++)
            {
                jsonObject.put(nameValuePairs.get(i).key, nameValuePairs.get(i).value);
            }
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_SEND_SUCCESS, mContext, postBody,isend);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    public static void getUserLable(Handler mHandler, Context mContext) {


        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_USER_LABLE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_LABLE_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static  void sendFengfa(Handler mHandler, Context mContext,String mailid,String memo,String lableid,String sendid,String time)
    {
        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_FENGFA);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("record_id", mailid);
            jsonObject.put("memo", memo);
            jsonObject.put("label", lableid);
            jsonObject.put("to", sendid);
            jsonObject.put("limit", time+":00");
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_FENGFA_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
