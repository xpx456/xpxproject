package com.bigwiner.android.asks;

import android.content.Context;
import android.os.Handler;
import android.os.ParcelUuid;
import android.widget.BaseAdapter;

import com.bigwiner.R;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.NetTask;
import intersky.xpxnet.net.nettask.PostJsonNetTask;

public class ContactsAsks {

    public static final String CONTACTS_LIST_PATH = "/bigwinner/query/connections/all";
    public static final String FRIEND_LIST_PATH = "/bigwinner/query/friends_list";
    public static final String CONTACTS_DETIAL_PATH = "/bigwinner/query/userinfo/user/id";
    public static final String CONTACTS_COMPANY_PATH = "/bigwinner/query/company/info/id";
    public static final String COMPANY_CONTACTS_PATH = "/bigwinner/query/company/member/id";
    public static final String CONTACTS_ADD_PATH = "/bigwinner/generate/Add/Friend";
    public static final String CONTACTS_DEL_PATH = "/bigwinner/generate/concel/Friend";
    public static final String CONTACT_ICON_PATH  = "/bigwinner/query/msg/logo/id";
    public static final String CONTACT_BG_PATH  = "/bigwinner/query/cover/id";
    public static final String COMPANNY_ICON_PATH  = "/bigwinner/query/company/logo/ddr/id";
    public static final String COMPANNY_BG_PATH  = "/bigwinner/query/company/background/id";
    public static final String MEETING_CONTACT_DATA_PATH = "/bigwinner/query/interview/all";
    public static final String MEETING_CONTACT_MEASURE_PATH = "/bigwinner/interview/handled/all/list";
    public static final String MEETING_DATE = "/bigwinner/add/interview";
    public static final String MEETING_APPLAY_SET = "/bigwinner/interview/handled/set/Status";
    public static final String MEETING_ATT_MY = "/bigwinner/query/participants/my/resources/all";
    public static final String MEETING_ATT_WANT = "/bigwinner/query/participants/demand/resources/all";
    public static final String MEETING_ATT_ALL = "/bigwinner/query/participants/all";
    public static final String CONTACT_CODE_SCAN = "/bigwinner/add/generate/qrcode";
    public static final String CONTACT_CODE_ADD = "/bigwinner/generate/qrcode/Add/Friend";
    public static final String CONTACTS_TYPE_BUSINESSAREA = "businessarea";
    public static final String CONTACTS_TYPE_CITY = "city";
    public static final String CONTACTS_TYPE_BUSINESSTYPE = "businesstype";
    public static final int CONTACTS_LIST_RESULT = 100200;
    public static final int FRIEND_LIST_RESULT = 100201;
    public static final int CONTACTS_DETIAL_RESULT = 100202;
    public static final int COMPANY_DETIAL_RESULT = 100203;
    public static final int COMPANY_CONTACTS_RESULT = 100204;
    public static final int CONTACTS_ADD_RESULT = 100205;
    public static final int CONTACTS_DATA_LIST_RESULT = 100206;
    public static final int CONTACTS_DATA_RESULT = 100208;
    public static final int CONTACTS_APPLY_LIST_RESULT = 100207;
    public static final int CONTACTS_APPLY_SET_RESULT = 1002013;
    public static final int CONTACTS_SCAN_RESULT = 100209;
    public static final int CONTACTS_MEETING_ATT_MY_RESULT = 100210;
    public static final int CONTACTS_MEETING_ATT_WANT_RESULT = 100211;
    public static final int CONTACTS_MEETING_ATT_ALL_RESULT = 100212;
    public static final int CONTACTS_DEL_RESULT = 100213;
    public static final int UPDATA_CONVERSATION_NAME = 100214;
    public static final int CONTACTS_LIST_SEARCH_RESULT = 100215;
    public static final int CONTACTS_DETIAL_RESULT2 = 100216;
    public static final int CONTACTS_DETIAL_RESULT3 = 100217;

    public static final String ACTION_FRIEND_CHANGE = "ACTION_FRIEND_CHANGE";
    public static final String ACTION_UPDATA_CONTACTS = "ACTION_UPDATA_CONTACTS";
    public static void getContactsList(Context mContext , Handler mHandler,int pagesize, int current,String type,String area,String city) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_LIST_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            if(!type.equals(mContext.getString(R.string.contacts_head_type)) )
            {
                jsonObject.put(CONTACTS_TYPE_BUSINESSTYPE,type);
            }
            if(!area.equals(mContext.getString(R.string.contacts_head_area)))
            {
                jsonObject.put(CONTACTS_TYPE_BUSINESSAREA,area);
            }
            if(!city.equals(mContext.getString(R.string.contacts_head_city)))
            {
                jsonObject.put(CONTACTS_TYPE_CITY,city);
            }
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_LIST_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getContactsListSearch(Context mContext , Handler mHandler,int pagesize, int current,String type,String area,String city,String keyword) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_LIST_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            if(!type.equals(mContext.getString(R.string.contacts_head_type)) )
            {
                jsonObject.put(CONTACTS_TYPE_BUSINESSTYPE,type);
            }
            if(!area.equals(mContext.getString(R.string.contacts_head_area)))
            {
                jsonObject.put(CONTACTS_TYPE_BUSINESSAREA,area);
            }
            if(!city.equals(mContext.getString(R.string.contacts_head_city)))
            {
                jsonObject.put(CONTACTS_TYPE_CITY,city);
            }
            jsonObject.put("keyword",keyword);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_LIST_SEARCH_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getFriendList(Context mContext , Handler mHandler, int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH+FRIEND_LIST_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, FRIEND_LIST_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getContactDetial(Context mContext , Handler mHandler, Contacts contacts) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_DETIAL_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid",contacts.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_DETIAL_RESULT,
                    mContext, postBody,contacts,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void checkHead(Context context, Handler handler, Attachment attachment, int event) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_DETIAL_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid",attachment.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, handler, event,
                    context, postBody,attachment,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getContactDetial2(Context mContext , Handler mHandler, Contacts contacts) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_DETIAL_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid",contacts.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_DETIAL_RESULT2,
                    mContext, postBody,contacts,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getUpdataConversationName(Context mContext , Handler mHandler, Contacts conversation) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_DETIAL_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid",conversation.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, UPDATA_CONVERSATION_NAME,
                    mContext, postBody,conversation,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getContactDetial(Context mContext , Handler mHandler, Contacts contacts, BaseAdapter adapter) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_DETIAL_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid",contacts.mRecordid);
            String postBody = jsonObject.toString();
            contacts.object = adapter;
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_DETIAL_RESULT,
                    mContext, postBody,contacts,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getContactAdd(Context mContext , Handler mHandler, Contacts contacts) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_ADD_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid",contacts.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_ADD_RESULT,
                    mContext, postBody,contacts,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getContactDel(Context mContext , Handler mHandler, Contacts contacts) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_DEL_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("userid",contacts.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_DEL_RESULT,
                    mContext, postBody,contacts,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getCompanyDetial(Context mContext , Handler mHandler, Company company) {
        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACTS_COMPANY_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("companyid",company.id);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, COMPANY_DETIAL_RESULT,
                    mContext, postBody,company,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getCompanyContacts(Context mContext , Handler mHandler, Company company,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH+COMPANY_CONTACTS_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("companyid",company.id);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, COMPANY_CONTACTS_RESULT,
                    mContext, postBody,company,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMeetingDateContacts(Context mContext , Handler mHandler, Meeting meeting,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_CONTACT_DATA_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no",meeting.recordid);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_DATA_LIST_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void getMeetingAttMyContacts(Context mContext , Handler mHandler, Meeting meeting,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_ATT_MY;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no",meeting.recordid);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_MEETING_ATT_MY_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMeetingAttWantContacts(Context mContext , Handler mHandler, Meeting meeting,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_ATT_WANT;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no",meeting.recordid);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_MEETING_ATT_WANT_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getMeetingAttAllContacts(Context mContext , Handler mHandler, Meeting meeting,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_ATT_ALL;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no",meeting.recordid);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_MEETING_ATT_ALL_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //status 状态 根据状态查询相关列表 2 已处理 （已处理列表） 1 需处理 （需处理列表）
    public static void getMeetingMeaseContacts(Context mContext , Handler mHandler, Meeting meeting,int stute,int pagesize, int current) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_CONTACT_MEASURE_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no",meeting.recordid);
            jsonObject.put("status",stute);
            jsonObject.put("pagesize",pagesize);
            jsonObject.put("currentpage",current);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_APPLY_LIST_RESULT,
                    mContext, postBody,stute,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doDate(Context mContext , Handler mHandler, Meeting meeting, Contacts contacts) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_DATE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no",meeting.recordid);
            jsonObject.put("uid",contacts.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_DATA_RESULT,
                    mContext, postBody,contacts,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //status 为状态 两个值  agreed refused
    public static void doSetApply(Context mContext , Handler mHandler, Meeting meeting, Contacts contacts,String status) {
        String urlString = BigwinerApplication.BASE_NET_PATH+MEETING_APPLAY_SET;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("no",meeting.recordid);
            jsonObject.put("uid",contacts.mRecordid);
            jsonObject.put("status",contacts.mRecordid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_APPLY_SET_RESULT,
                    mContext, postBody,contacts,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void scanCode(Context mContext , Handler mHandler, String uid) {

        String urlString = BigwinerApplication.BASE_NET_PATH+CONTACT_CODE_ADD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sweep_token", NetUtils.getInstance().token);
            jsonObject.put("qrcontent", uid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CONTACTS_SCAN_RESULT,
                    mContext, postBody,uid,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String  codeUrl(String uid) {
        String urlString = CONTACT_CODE_SCAN+"/"+uid;
        return urlString;
    }

//    public static String getContactIconUrl(String id) {
//        return BigwinerApplication.BASE_NET_PATH+CONTACT_ICON_PATH+"?uid="+id+"&rand="+ TimeUtils.getDateAndTimeCode();
//    } 1590392174804 1590391771900
    public static String getContactIconUrl(String id,String key) {
        return BigwinerApplication.BASE_NET_PATH+CONTACT_ICON_PATH+"?uid="+id+"&key="+key;
    }

    public static String getContactIconUrlPath(String path) {
        return BigwinerApplication.BASE_NET_PATH+path;
    }

//    public static String getContactBgUrl(String id) {
//        return BigwinerApplication.BASE_NET_PATH+CONTACT_BG_PATH+"?uid="+id+"&rand="+ TimeUtils.getDateAndTimeCode();
//    }

    public static String getContactBgUrl(String id,String key) {
        return BigwinerApplication.BASE_NET_PATH+CONTACT_BG_PATH+"?uid="+id+"&key="+key;
    }

    public static String getCompanyIconUrl(String id,String key) {
        return BigwinerApplication.BASE_NET_PATH+COMPANNY_ICON_PATH+"?companyid="+id;
    }

    public static String getCompanyBgUrl(String id,String key) {
        return BigwinerApplication.BASE_NET_PATH+COMPANNY_BG_PATH+"?companyid="+id;
    }
}
