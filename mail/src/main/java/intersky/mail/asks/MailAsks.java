package intersky.mail.asks;

import android.content.Context;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import intersky.appbase.entity.Contacts;
import intersky.mail.MailManager;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.MailFile;
import intersky.mail.entity.MailSend;
import intersky.mail.entity.MailType;
import intersky.mail.presenter.MailPresenter;
import intersky.mail.view.activity.MailListActivity;
import intersky.select.entity.Select;
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
    public static final String MAIL_GET_UNDERLINES = "mail/get/underlings";
    public static final String GET_MAIL_COUNT = "mail/badges"; // 未读已读数
    public static final String MAIL_SETUSER = "mail/setuser"; // 设置当前用户
    public static final String GET_MAIL_LIST = "mail/query";
    public static final String GET_MAIL_LIST_C = "mail/list";
    public static final String DELETE_MIAL = "mail/delete";
    public static final String MAIL_VIEW = "mail/view";
    public static final String MAIL_DOWNLOAD_ATTACHMENT = "mail/attachment/get";
    public static final String NEW_MAIL_PATH = "mail/action";
    public final static String GET_USER_GUEST = "mail/recipients";
    public static final String SEND_MAIL_PATH = "mail/save";
    public static final String SEND_MAIL_PUSH_PATH = "mail/write/setPush";
    public static final String GET_USER_LABLE = "mail/label"; // 获取用户标签
    public static final String MAIL_FENGFA = "mail/distribute";
    public static final String MAIL_APPROVEAL = "mail/approval";
    public static final String ATTACHMENT_PATH_CLOUD = "v/edit/upload";
    public static final String RECEIVE_MAIL = "ail/pop";
    public static final String MAIL_TYPE = "mail/relationship";
    public static final String MAIL_GET_CUSTOMS = "mail/customer/list";
    public static final String MAIL_GET_FILES = "mail/directory/mobile";
    public static final String MAIL_FILE_SET = "mail/directory/set";
    public static final String MAIL_FILE_DEL = "mail/directory/del";
    public static final String MAIL_PUSH_CHECK = "mail/check/push";
    public static final String MIAL_PUSH_MAIL_LIST = "mail/push/GetDirectList";
    public static final String LABLE_DELETE = "mail/label/del";
    public static final String LABLE_SET = "mail/label/set";
    public static final String LABLE_MAIL = "mail/set/label";
    public static final String MAIL_MANAGER = "mail/set/merger";
    public static final String MAIL_SET = "mail/set";
    public static final String MAIL_SET_APPROVEAL = "mail/set/approval";
    public static final String MAIL_GROP_LABLE = "mail/push/QueryTagByParam";
    public static final String MAIL_DELETE_PUSH_MAIL = "mail/push/DelDirectItem";
    public static final String MAIL_PUSH_VIEW = "mail/push/GetDirectItem";
    public static final String MAIL_FENG_FA = "mail/set/issue";
    public static final String MAIL_CONTACTS= "api/get/organization";

    public static final int MAIL_MAILBOX_SUCCESS = 1200000;
    public static final int MAIL_MAILBOX_FAIL = 1424000;
    public static final int MAIL_UNDERLINES_SUCCESS = 1200001;
    public static final int MAIL_SETUSER_SUCCESS = 1200002;
    public static final int MAIL_SETUSER_FAIL = 1424002;
    public static final int MAIL_READ_COUNT_SUCCESS = 1200003;
    public static final int MAIL_LIST_SUCCESS = 1200004;
    public static final int MAIL_DELETE_SUCCESS = 1200005;
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
    public static final int MAIL_MAIL_SHIP = 1200013;
    public static final int MAIL_MAIL_FILES = 1200014;

    public static final int MAIL_PUSH_CHECK_SUCCESS = 1200015;
    public static final int MAIL_LABLE_DELETE_SUCCESS = 1200016;
    public static final int MAIL_LABLE_SET_SUCCESS = 1200017;
    public static final int MAIL_FILE_DELETE_SUCCESS = 1200018;
    public static final int MAIL_FILE_SET_SUCCESS = 1200019;
    public static final int MAIL_LABLE_MAIL_SUCCESS = 1200020;
    public static final int MAIL_MANAGE_MAIL_SUCCESS = 1200021;
    public static final int MAIL_CUSTOMS_SUCCESS = 1200022;
    public static final int MAIL_SET_READ_SUCCESS = 1200023;
    public static final int MAIL_SET_UNREAD_SUCCESS = 1200024;
    public static final int MAIL_SET_NOREPLY_SUCCESS = 1200025;
    public static final int MAIL_SET_UNNOREPLY_SUCCESS = 1200026;
    public static final int MAIL_SET_READALL_SUCCESS = 1200027;
    public static final int MAIL_SET_MOVE_SUCCESS = 1200028;
    public static final int MAIL_SET_APPROVE_SUCCESS = 1200029;
    public static final int MAIL_SET_VOTE_SUCCESS = 1200030;
    public static final int MAIL_GROP_LABLE_SUCCESS = 1200031;
    public static final int MAIL_CONTACTS_SUCCESS = 1200032;

    public static void getUserMailBox(Context mContext, Handler mHandler,String userid) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_MAILBOX);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid", userid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_MAILBOX_SUCCESS,
                    mContext, postBody, userid);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getUserUnderlines(Context mContext,Handler mHandler,String userid) {

        try {
            String urlString = "";
            if(MailManager.getInstance().account.iscloud)
            {
                 urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_GET_UNDERLINES);
            }
            else
            {
                 urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_UNDERLINES);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid", userid);
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
            jsonObject.put("user_id", mContacts.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_SETUSER_SUCCESS,
                    mContext, postBody,mContacts);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getReadCount(Context mContext, Handler mHandler,String userid) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_MAIL_COUNT);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid", userid);
            if(MailManager.getInstance().mSelectUser != null)
            jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("user_id", MailManager.getInstance().account.mRecordId);
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
            jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
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

    public static void getMials(Context mContext, Handler mHandler, int id, int type, String mailboxid, int start, MailPresenter.Mailboxobj mMailBoxModel) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_MAIL_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
            jsonObject.put("kind", id);
            jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
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
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("user_id", MailManager.getInstance().account.mRecordId);
            if(type2 == -1)
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
            else if(type2 == -2)
            {
                urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MIAL_PUSH_MAIL_LIST);
                jsonObject.put("state", type1);
            }
            else
            {
                jsonObject.put("state", type2);
            }
            if(mailboxid.length() > 0 && type2 != -2)
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

    public static void getMials(Context mContext, Handler mHandler, int type1, int type2, int current, String mailboxid, int start, int pagesize, MailPresenter.Mailboxobj mMailBoxModel ) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_MAIL_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("user_id", MailManager.getInstance().account.mRecordId);
            if(mMailBoxModel.state == -1)
            {
                jsonObject.put("approval", String.valueOf(mMailBoxModel.id));
                if(current == 0)
                    jsonObject.put("type", "0");
                else if(current == 1)
                    jsonObject.put("type", "2");
                else if(current == 2)
                    jsonObject.put("type", "1");
                else if(current == 3)
                    jsonObject.put("type", "3");

            }
            else if(mMailBoxModel.state == -2)
            {

                urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MIAL_PUSH_MAIL_LIST);
                ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
                BasicNameValuePair item = new BasicNameValuePair("state", String.valueOf(mMailBoxModel.id));
                items.add(item);
                if(MailManager.getInstance().mSelectUser != null)
                {
                    item = new BasicNameValuePair("user_id", MailManager.getInstance().mSelectUser.mRecordid);
                    items.add(item);
                }
                else
                {
                    item = new BasicNameValuePair("user_id", MailManager.getInstance().account.mRecordId);
                    items.add(item);
                }
                item = new BasicNameValuePair("page", String.valueOf(start));
                items.add(item);
                item = new BasicNameValuePair("size", String.valueOf(pagesize));
                items.add(item);
                PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_LIST_SUCCESS, mContext, items,current);
                NetTaskManager.getInstance().addNetTask(mPostNetTask);
                return;
            }
            else if(mMailBoxModel.state == -3)
            {
                jsonObject.put("state", 1);
                jsonObject.put("label", mMailBoxModel.mailboxid);
            }
            else if(mMailBoxModel.state == -4)
            {
                jsonObject.put("state", 1);
                jsonObject.put("directory_id", mMailBoxModel.mailboxid);

            }
            else if(mMailBoxModel.state == -5)
            {
                jsonObject.put("state", 1);
                jsonObject.put("customer_mail", mMailBoxModel.mailboxid);
            }
            else
            {
                jsonObject.put("state", mMailBoxModel.state);
                if(mMailBoxModel.type == 100)
                {
                    jsonObject.put("is_read", true);
                }
            }
            if(mailboxid.length() > 0 && mMailBoxModel.state != -2 && mMailBoxModel.state != -3 && mMailBoxModel.state != -4 && mMailBoxModel.state != -5)
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
            jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
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
            if(MailManager.getInstance().mSelectUser != null)
            jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
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
                if(MailManager.getInstance().mSelectUser != null)
                    jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
                else
                    jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
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
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
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
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
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
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("user_id", MailManager.getInstance().account.mRecordId);
            jsonObject.put("state", state);
            jsonObject.put("memo", memo);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_APPROVE_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void getOutGuest(Context mContext, Handler mHandler ) {
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

    public static void sendMailc(Context mContext, Handler mHandler, MailSend mailSend, boolean isend,boolean push ) {

        try {
            String urlString = "";

            if(push)
            {
                urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,SEND_MAIL_PUSH_PATH);
                ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
                BasicNameValuePair item = new BasicNameValuePair("token", NetUtils.getInstance().token);
                items.add(item);
//                item = new BasicNameValuePair("mail_box_id", mailSend.mail_box_id);
//                items.add(item);
                item = new BasicNameValuePair("from_address", mailSend.from_address);
                items.add(item);
                item = new BasicNameValuePair("subject", mailSend.subject);
                items.add(item);
                item = new BasicNameValuePair("attachments", mailSend.attachments);
                items.add(item);
                item = new BasicNameValuePair("mail_direct_address_id", mailSend.mail_box_id);
                items.add(item);
                item = new BasicNameValuePair("content", mailSend.content);
                items.add(item);
                item = new BasicNameValuePair("to_address", mailSend.to_address);
                items.add(item);
                item = new BasicNameValuePair("state", "1");
                items.add(item);
                item = new BasicNameValuePair("send_state", "0");
                items.add(item);
                for(int i = 0 ; i < mailSend.lable.length() ; i++)
                {
                    item = new BasicNameValuePair("tag_name", mailSend.lable.getString(i));
                    items.add(item);
                }
//                item = new BasicNameValuePair("track", MailManager.getInstance().account.mRecordId);
//                items.add(item);
                PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SEND_SUCCESS, mContext, items,isend);
                NetTaskManager.getInstance().addNetTask(mPostNetTask);
            }
            else
            {
                urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,SEND_MAIL_PATH);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", NetUtils.getInstance().token);
                if(MailManager.getInstance().mSelectUser != null)
                    jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
                else
                    jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
                jsonObject.put("send_state",mailSend.send_state);
                jsonObject.put("mail_box_id",mailSend.mail_box_id);
                jsonObject.put("subject",mailSend.subject);
                jsonObject.put("content",mailSend.content);
                jsonObject.put("from_address",mailSend.from_address);
                jsonObject.put("cc_address",mailSend.cc_address);
                jsonObject.put("to_address",mailSend.to_address);
                jsonObject.put("bcc_address",mailSend.bcc_address);
                if(mailSend.resent_address.length() > 0)
                    jsonObject.put("resent_address",mailSend.resent_address);
                jsonObject.put("state",mailSend.state);
                jsonObject.put("mail_type",mailSend.mail_type);
                jsonObject.put("submit_type",mailSend.submit_type);
                jsonObject.put("isText",mailSend.isText);
                jsonObject.put("isCritical",mailSend.isCritical);
                jsonObject.put("isReceipt",mailSend.isReceipt);
                jsonObject.put("isTrack",mailSend.isTrack);
                jsonObject.put("approval",mailSend.approval);
                jsonObject.put("priority",mailSend.priority);
                jsonObject.put("label",mailSend.lable);
                jsonObject.put("attachments",mailSend.attachments);
                if(mailSend.raw_mail_id.length() > 0)
                    jsonObject.put("raw_mail_id",mailSend.raw_mail_id);
                if(mailSend.raw_mail_type.length() > 0)
                    jsonObject.put("raw_mail_type",mailSend.raw_mail_type);
                if(mailSend.mail_user_id.length() > 0)
                    jsonObject.put("mail_user_id",mailSend.mail_user_id);
                String postBody = jsonObject.toString();
                PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_SEND_SUCCESS, mContext, postBody,isend);
                NetTaskManager.getInstance().addNetTask(mPostNetTask);
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    public static void getUserLable(Handler mHandler, Context mContext,String userid) {


        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,GET_USER_LABLE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid", userid);
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
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
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

    public static  void getReceive(Handler mHandler, Context mContext)
    {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,RECEIVE_MAIL);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_MAIL_SHIP, mContext,postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static  void getFiles(Handler mHandler, Context mContext)
    {
        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_GET_FILES);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("user_id", MailManager.getInstance().account.mRecordId);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_MAIL_FILES, mContext,postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static  void getTypes(Handler mHandler, Context mContext,String userid)
    {


        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_TYPE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid", userid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_MAIL_SHIP, mContext,postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static  void getMailCustoms(Handler mHandler, Context mContext, MailPresenter.Mailboxobj mailboxobj )
    {

        try {
            MailType mailType = (MailType) mailboxobj.object;
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_GET_CUSTOMS);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("group", mailType.group);
            jsonObject.put("where", mailType.where);
            jsonObject.put("module", MailManager.getInstance().module);
            jsonObject.put("order", MailManager.getInstance().order);
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_CUSTOMS_SUCCESS, mContext,postBody,mailboxobj);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static  void getMailCustoms2(Handler mHandler, Context mContext, MailContact contacts )
    {

        try {
            MailType mailType = (MailType) contacts.mailType;
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_GET_CUSTOMS);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("group", mailType.group);
            jsonObject.put("where", mailType.where);
            jsonObject.put("module", MailManager.getInstance().module);
            jsonObject.put("order", MailManager.getInstance().order);
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_CUSTOMS_SUCCESS, mContext,postBody,contacts);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void pushCheck(Context mContext,Handler mHandler,String userid) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_PUSH_CHECK);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid", userid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_PUSH_CHECK_SUCCESS, mContext,postBody);
            if(MailManager.getInstance().account.iscloud == true)
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void lableDel(Context mContext, Handler mHandler, Select select ) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,LABLE_DELETE);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        item = new BasicNameValuePair("_id", select.mId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_LABLE_DELETE_SUCCESS, mContext, items,select);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void lableSet(Context mContext, Handler mHandler, Select select) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,LABLE_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(select.mId.length() > 0)
        {
            item = new BasicNameValuePair("_id", select.mId);
            items.add(item);
        }
        item = new BasicNameValuePair("title", select.mName);
        items.add(item);
        item = new BasicNameValuePair("color", select.mColor);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_LABLE_SET_SUCCESS, mContext, items,select);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);


    }

    public static void lableMail(Context mContext, Handler mHandler, ArrayList<Mail> mails,ArrayList<Select> lables ) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,LABLE_MAIL);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
            String ilables = "";
            for(int i = 0 ; i < lables.size() ; i++)
            {
                if(ilables.length() == 0)
                {
                    ilables += lables.get(i).mId;
                }
                else
                {
                    ilables += (","+lables.get(i).mId);
                }
            }
            jsonObject.put("mail_label_id", ilables);
            JSONArray ja = new JSONArray();
            for(int i = 0 ; i < mails.size() ; i++)
            {
                ja.put(mails.get(i).mRecordId);
            }
            jsonObject.put("mail_user_id", ja);
            jsonObject.put("is_mobile", true);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_LABLE_MAIL_SUCCESS, mContext,postBody,ilables);
            if(MailManager.getInstance().account.iscloud == true)
                NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void setFile(Context mContext, Handler mHandler, MailFile select ) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_FILE_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(select.rid.length() > 0)
        {
            item = new BasicNameValuePair("_id", select.rid);
            items.add(item);
        }
        item = new BasicNameValuePair("name", select.name);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_FILE_SET_SUCCESS, mContext, items,select);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void fileDel(Context mContext, Handler mHandler, MailFile select ) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_FILE_DEL);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        item = new BasicNameValuePair("_id", select.rid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_FILE_DELETE_SUCCESS, mContext, items,select);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }


    public static void managerMails(Context mContext, Handler mHandler, int current,  int start, int pagesize, MailPresenter.Mailboxobj mMailBoxModel,int managetype,ArrayList<Mail> mails ) {


        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_MANAGER);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mMailBoxModel.state == -1)
        {
            item = new BasicNameValuePair("approval", String.valueOf(mMailBoxModel.id));
            items.add(item);
            if(current == 0)
            {
                item = new BasicNameValuePair("type", "0");
                items.add(item);
            }
            else if(current == 1)
            {
                item = new BasicNameValuePair("type", "2");
                items.add(item);
            }
            else if(current == 2)
            {
                item = new BasicNameValuePair("type", "1");
                items.add(item);
            }
            else if(current == 3)
            {
                item = new BasicNameValuePair("type", "3");
                items.add(item);
            }
        }
        else if(mMailBoxModel.state == -2)
        {
            //urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MIAL_PUSH_MAIL_LIST);
            item = new BasicNameValuePair("state", String.valueOf(mMailBoxModel.id));
            items.add(item);
        }
        else if(mMailBoxModel.state == -3)
        {
            item = new BasicNameValuePair("lable", mMailBoxModel.mailboxid);
            items.add(item);
            item = new BasicNameValuePair("key_word", "");
            items.add(item);
            item = new BasicNameValuePair("sreach_type", "subject");
            items.add(item);
            item = new BasicNameValuePair("is_process", "1");
            items.add(item);
        }
        else if(mMailBoxModel.state == -4)
        {
            item = new BasicNameValuePair("directory_id", mMailBoxModel.mailboxid);
            items.add(item);
            item = new BasicNameValuePair("key_word", "");
            items.add(item);
            item = new BasicNameValuePair("sreach_type", "subject");
            items.add(item);
            item = new BasicNameValuePair("is_process", "1");
            items.add(item);
        }
        else if(mMailBoxModel.state == -5)
        {
            item = new BasicNameValuePair("customer_mail", mMailBoxModel.mailboxid);
            items.add(item);
            item = new BasicNameValuePair("key_word", "");
            items.add(item);
            item = new BasicNameValuePair("sreach_type", "subject");
            items.add(item);
            item = new BasicNameValuePair("is_process", "1");
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("state", String.valueOf(mMailBoxModel.state));
            items.add(item);
            if(mMailBoxModel.type == 100)
            {
                item = new BasicNameValuePair("is_read", "false");
            }
        }
        if(mMailBoxModel.mailboxid.length() > 0 && mMailBoxModel.state != -2 && mMailBoxModel.state != -3 && mMailBoxModel.state != -4 && mMailBoxModel.state != -5)
        {
            item = new BasicNameValuePair("mail_box_id", mMailBoxModel.mailboxid);
            items.add(item);
        }
        item = new BasicNameValuePair("page", String.valueOf(start));
        items.add(item);
        item = new BasicNameValuePair("merger_type", String.valueOf(managetype));
        items.add(item);

        if(mails != null)
        {
            String mailids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;
                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }
            }
            item = new BasicNameValuePair("mail_user_id", mailids);
            items.add(item);
        }
        item = new BasicNameValuePair("size", String.valueOf(pagesize));
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_MANAGE_MAIL_SUCCESS, mContext, items,current);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

//        try {
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("token", NetUtils.getInstance().token);
//
//            if(MailManager.getInstance().mSelectUser != null)
//                jsonObject.put("user_id", MailManager.getInstance().mSelectUser.mailRecordID);
//            else
//                jsonObject.put("user_id", MailManager.getInstance().account.mRecordId);
//            if(mMailBoxModel.state == -1)
//            {
//                jsonObject.put("approval", String.valueOf(mMailBoxModel.id));
//                if(current == 0)
//                    jsonObject.put("type", "0");
//                else if(current == 1)
//                    jsonObject.put("type", "2");
//                else if(current == 2)
//                    jsonObject.put("type", "1");
//                else if(current == 3)
//                    jsonObject.put("type", "3");
//            }
//            else if(mMailBoxModel.state == -2)
//            {
//                //urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MIAL_PUSH_MAIL_LIST);
//                jsonObject.put("state", mMailBoxModel.id);
//            }
//            else if(mMailBoxModel.state == -3)
//            {
//                jsonObject.put("lable", mMailBoxModel.mailboxid);
//                jsonObject.put("key_word", "");
//                jsonObject.put("sreach_type", "subject");
//                jsonObject.put("is_process", "1");
//
//            }
//            else if(mMailBoxModel.state == -4)
//            {
//                jsonObject.put("directory_id", mMailBoxModel.mailboxid);
//                jsonObject.put("key_word", "");
//                jsonObject.put("sreach_type", "subject");
//                jsonObject.put("is_process", "1");
//            }
//            else if(mMailBoxModel.state == -5)
//            {
//
//                jsonObject.put("customer_mail", mMailBoxModel.mailboxid);
//                jsonObject.put("key_word", "");
//                jsonObject.put("sreach_type", "subject");
//                jsonObject.put("is_process", "1");
//            }
//            else
//            {
//                jsonObject.put("state", mMailBoxModel.state);
//                if(mMailBoxModel.type == 100)
//                {
//                    jsonObject.put("is_read", false);
//                }
//            }
//            if(mMailBoxModel.mailboxid.length() > 0 && mMailBoxModel.state != -2 && mMailBoxModel.state != -3 && mMailBoxModel.state != -4 && mMailBoxModel.state != -5)
//                jsonObject.put("mail_box_id", mMailBoxModel.mailboxid);
//            jsonObject.put("page",start);
//            jsonObject.put("merger_type",managetype);
//            if(mails != null)
//            {
//                String mailids = "";
//                for(int i = 0 ; i < mails.size() ; i++)
//                {
//                    if(mailids.length() == 0)
//                    {
//                        mailids += mails.get(i).mRecordId;
//                    }
//                    else
//                    {
//                        mailids += (","+mails.get(i).mRecordId);
//                    }
//                }
//                jsonObject.put("mail_user_id",mailids);
//            }
//            jsonObject.put("size", pagesize);
//            String postBody = jsonObject.toString();
//            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_MANAGE_MAIL_SUCCESS, mContext, postBody,current);
//            NetTaskManager.getInstance().addNetTask(mPostNetTask);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    public static void setReadAll(Context mContext, Handler mHandler, MailPresenter.Mailboxobj mailboxobj) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        item = new BasicNameValuePair("_id", "all");
        items.add(item);
        item = new BasicNameValuePair("is_read", "true");
        items.add(item);
        if(mailboxobj.type < 5 && mailboxobj.type != 1 )
        {
            if(mailboxobj.mailboxid.length() > 0)
            {
                item = new BasicNameValuePair("mail_box_id", mailboxobj.mailboxid);
                items.add(item);
            }
            else
            {
                item = new BasicNameValuePair("mail_box_id", "");
                items.add(item);
            }
        }
        else
        {
            item = new BasicNameValuePair("mail_box_id", "");
            items.add(item);
        }
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SET_READALL_SUCCESS, mContext, items,mailboxobj);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setRead(Context mContext, Handler mHandler,ArrayList<Mail> mails) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;
                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }
            }
            item = new BasicNameValuePair("_id",mailids);
            items.add(item);
        }
        item = new BasicNameValuePair("is_read", "true");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SET_READ_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setunRead(Context mContext, Handler mHandler,ArrayList<Mail> mails) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;
                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }
            }
            item = new BasicNameValuePair("_id",mailids);
            items.add(item);
        }
        item = new BasicNameValuePair("is_read", "false");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SET_UNREAD_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setnoRepeat(Context mContext, Handler mHandler,ArrayList<Mail> mails) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;
                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }
            }
            item = new BasicNameValuePair("_id",mailids);
            items.add(item);
        }
        item = new BasicNameValuePair("is_reply", "true");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SET_NOREPLY_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setunnoRepeat(Context mContext, Handler mHandler,ArrayList<Mail> mails) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;
                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }
            }
            item = new BasicNameValuePair("_id",mailids);
            items.add(item);
        }
        item = new BasicNameValuePair("is_reply", "false");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SET_UNNOREPLY_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setdeleteMails(Context mContext, Handler mHandler, ArrayList<Mail> mails, MailPresenter.Mailboxobj mailboxobj) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;
                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }
            }
            item = new BasicNameValuePair("_id",mailids);
            items.add(item);
        }
        if(mailboxobj.type == 4 || mailboxobj.type == 5)
        item = new BasicNameValuePair("state", "5");
        else
            item = new BasicNameValuePair("state", "3");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_DELETE_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setdeletePushMails(Context mContext, Handler mHandler,ArrayList<Mail> mails) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_DELETE_PUSH_MAIL);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;
                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }
            }
            item = new BasicNameValuePair("_id",mailids);
            items.add(item);
        }
        item = new BasicNameValuePair("state", "3");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_DELETE_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void moveMail(Context mContext, Handler mHandler,ArrayList<Mail> mails,MailFile mailFile) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;
                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }
            }
            item = new BasicNameValuePair("_id",mailids);
            items.add(item);
        }
        if(mailFile.rid.equals("0")) {
            item = new BasicNameValuePair("state","1");
            items.add(item);
        }
        else if(mailFile.rid.equals("2")){
            item = new BasicNameValuePair("state","2");
            items.add(item);
            item = new BasicNameValuePair("send_state","4");
            items.add(item);
        }
        else if(mailFile.rid.equals("3")){
            item = new BasicNameValuePair("state","0");
            items.add(item);
        }
        else if(mailFile.rid.equals("4")){
            item = new BasicNameValuePair("state","3");
            items.add(item);
        }
        else if(mailFile.rid.equals("5")){
            item = new BasicNameValuePair("state","4");
            items.add(item);
        }
        else{
            item = new BasicNameValuePair("directory_id",mailFile.rid);
            items.add(item);
        }
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SET_MOVE_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setApprove(Context mContext, Handler mHandler, ArrayList<Mail> mails) {

        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET_APPROVEAL);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            String uids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;

                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }

                if(uids.length() == 0)
                {
                    uids += mails.get(i).mUserId;
                }
                else
                {
                    uids += (","+mails.get(i).mUserId);
                }
            }
            item = new BasicNameValuePair("mail_user_id",mailids);
            items.add(item);
            item = new BasicNameValuePair("user_id",uids);
            items.add(item);
        }
        item = new BasicNameValuePair("approval", "true");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SET_APPROVE_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setVote(Context mContext, Handler mHandler,ArrayList<Mail> mails) {


        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_SET_APPROVEAL);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        if(MailManager.getInstance().mSelectUser != null)
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
            items.add(item);
        }
        if(mails != null)
        {
            String mailids = "";
            String uids = "";
            for(int i = 0 ; i < mails.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += mails.get(i).mRecordId;

                }
                else
                {
                    mailids += (","+mails.get(i).mRecordId);
                }

                if(uids.length() == 0)
                {
                    uids += mails.get(i).mUserId;
                }
                else
                {
                    uids += (","+mails.get(i).mUserId);
                }
            }
            item = new BasicNameValuePair("mail_user_id",mailids);
            items.add(item);
            item = new BasicNameValuePair("user_id",uids);
            items.add(item);
        }
        item = new BasicNameValuePair("approval", "false");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_SET_VOTE_SUCCESS, mContext, items,mails);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getGropLable(Context mContext, Handler mHandler) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_GROP_LABLE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_GROP_LABLE_SUCCESS, mContext,postBody);
            if(MailManager.getInstance().account.iscloud == true)
                NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void getPushMailbox(Context mContext, Handler mHandler) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_GROP_LABLE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_GROP_LABLE_SUCCESS, mContext,postBody);
            if(MailManager.getInstance().account.iscloud == true)
                NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getMailPushView(Context mContext, Handler mHandler, Mail mail) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_PUSH_VIEW);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if(MailManager.getInstance().mSelectUser != null)
                jsonObject.put("userid", MailManager.getInstance().mSelectUser.mRecordid);
            else
                jsonObject.put("userid", MailManager.getInstance().account.mRecordId);
            jsonObject.put("mail_id", mail.mRecordId);
            jsonObject.put("set_readed", true);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, MAIL_VIEW_SUCCESS, mContext, postBody,mail);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void fengfa(Context mContext, Handler mHandler,ArrayList<MailContact> contacts,Mail mail) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_FENG_FA);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
//        if(MailManager.getInstance().mSelectUser != null)
//        {
//            item = new BasicNameValuePair("userid", MailManager.getInstance().mSelectUser.mRecordid);
//            items.add(item);
//        }
//        else
//        {
//            item = new BasicNameValuePair("userid", MailManager.getInstance().account.mRecordId);
//            items.add(item);
//        }
        if(contacts != null)
        {
            String mailids = "";
            for(int i = 0 ; i < contacts.size() ; i++)
            {
                if(mailids.length() == 0)
                {
                    mailids += contacts.get(i).mRecordid;
                }
                else
                {
                    mailids += (";"+contacts.get(i).mRecordid);
                }
            }
            item = new BasicNameValuePair("issue_address",mailids);
            items.add(item);
        }
        item = new BasicNameValuePair("mail_id",mail.mRecordId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_FENGFA_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getMailContacts(Context mContext, Handler mHandler) {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MAIL_CONTACTS);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token",NetUtils.getInstance().token);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MAIL_CONTACTS_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
