package intersky.notice.prase;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.notice.NoticeManager;
import intersky.notice.R;
import intersky.notice.entity.Notice;
import intersky.notice.view.adapter.NoticeAdapter;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class NoticePrase {

    public static void praseList(NetObject net, Context context, NoticeAdapter noticeAdapter) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return;
            }

            JSONObject object = new JSONObject(json);
            JSONArray ja = object.getJSONArray("data");
            for(int i = 0 ; i < ja.length()-1 ; i++)
            {
                JSONObject jo = ja.getJSONObject(i);
                Notice mNoticeModel = new Notice(jo.getString("notice_id"),jo.getString("user_id"),jo.getString("company_id"),jo.getString("title")
                        ,jo.getString("content"),jo.getString("attence"),jo.getString("sender_id"),jo.getString("dept_name"),jo.getString("create_time"),jo.getString("update_time"));
                mNoticeModel.username = (String) Bus.callData(NoticeManager.getInstance().context,"chat/getContactName",mNoticeModel.uid);
                mNoticeModel.isread = (int) net.item;
                noticeAdapter.getList().add(mNoticeModel);
            }

            if(noticeAdapter.totalCount == -1)
            {
                JSONObject jo = ja.getJSONObject(ja.length()-1);
                noticeAdapter.totalCount = jo.getInt("total_results");
            }
            if(ja.length() > 0)
            {
                noticeAdapter.nowpage++;
            }

            if(noticeAdapter.totalCount == 0 && noticeAdapter.nowpage==1)
            {
                AppUtils.showMessage(context, context.getString(R.string.noInfoFind));
            }
            noticeAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseList2(NetObject net, Context context, NoticeAdapter noticeAdapter) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }

            JSONObject object = new JSONObject(json);
            JSONArray ja = object.getJSONArray("data");
            for(int i = 0 ; i < ja.length()-1 ; i++)
            {
                JSONObject jo = ja.getJSONObject(i);
                Notice mNoticeModel = new Notice(jo.getString("notice_id"),jo.getString("user_id"),jo.getString("company_id"),jo.getString("title")
                        ,jo.getString("content"),jo.getString("attence"),jo.getString("sender_id"),jo.getString("dept_name"),jo.getString("create_time"),jo.getString("update_time"));
                mNoticeModel.username = (String) Bus.callData(NoticeManager.getInstance().context,"chat/getContactName",mNoticeModel.uid);
                mNoticeModel.isread = (int) net.item;
                noticeAdapter.getList().add(mNoticeModel);
            }

            if(noticeAdapter.totalCount == -1)
            {
                JSONObject jo = ja.getJSONObject(ja.length()-1);
                noticeAdapter.totalCount = jo.getInt("total_results");
            }
            if(ja.length() > 0)
            {
                noticeAdapter.nowpage++;
            }

            if(noticeAdapter.totalCount == 0 && noticeAdapter.nowpage==1)
            {
                AppUtils.showMessage(context, context.getString(R.string.noInfoFind));
            }
            noticeAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean praseDetial(NetObject net,Context context) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }
            Notice notice = (Notice) net.item;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            notice.uid = jo.getString("user_id");
            notice.create_time = jo.getString("create_time");
            notice.title = jo.getString("title");
            notice.content = jo.getString("content");
            notice.attachment = jo.getString("attence");

            if (jo.has("has_many_read")) {
                JSONArray ja = jo.getJSONArray("has_many_read");
                notice.unread_id = "";
                notice.read_id = "";
                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo2 = ja.getJSONObject(i);
                    if (jo2.getInt("is_read") == 0) {
                        if(notice.unread_id.length() == 0 )
                        {
                            notice.unread_id += jo2.getString("user_id");
                        }
                        else
                        {
                            notice.unread_id += (","+jo2.getString("user_id"));
                        }
                    } else {
                        if(notice.read_id.length() == 0 )
                        {
                            notice.read_id += jo2.getString("user_id");
                        }
                        else
                        {
                            notice.read_id += (","+jo2.getString("user_id"));
                        }
                    }
                }
            }
            if(jo.has("has_many_reply"))
            notice.replyJson = jo.getJSONArray("has_many_reply").toString();
            if(notice.isread == 0)
            {
                notice.isread = 1;
                NoticeManager.getInstance().sendNoticeUpdate(notice.nid);
                Bus.callData(context,"function/updateOahit");
                Bus.callData(context,"conversation/setdetialread", Conversation.CONVERSATION_TYPE_NOTICE,notice.nid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void praseAddtchment(NetObject net, ArrayList<Attachment> attachments, Notice report) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }
            report.attachJson = net.result;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Attachment mAttachmentModel = new Attachment(jo.getString("hash"),jo.getString("name"),
                        Bus.callData(NoticeManager.getInstance().context,"filetools/getfilePath","/" + "intersky" + "/" + "notice")+"/"+jo.getString("hash")+"."+jo.getString("ext"),
                        NoticeManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")),jo.getLong("size"),0,"");
                mAttachmentModel.mPath2 = Bus.callData(NoticeManager.getInstance().context,"filetools/getfilePath","/" + "intersky" + "/" + "notice")+"/"+jo.getString("hash")+"2"+"."+jo.getString("ext");
                attachments.add(mAttachmentModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseAddtchment(String json, ArrayList<Attachment> attachments,Notice report) {
        try {


            report.attachJson = json;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
            String hash = jsonObject3.getString("attence");
            String[] hashs = hash.split(",");
            for (int j = 0; j < hashs.length; j++) {
                String temp = hashs[j];
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getString("hash").equals(temp)) {
                        Attachment mAttachmentModel = new Attachment(jo.getString("hash"),jo.getString("name"),
                                Bus.callData(NoticeManager.getInstance().context,"filetools/getfilePath","/" + "Intersky" + "/" + "notice")+"/"+jo.getString("hash")+jo.getString("ext"),
                                NoticeManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")),jo.getLong("size"),0,"");
                        mAttachmentModel.mPath2 = Bus.callData(NoticeManager.getInstance().context,"filetools/getfilePath","/" + "Intersky" + "/" + "notice")+"/"+jo.getString("hash")+"2"+jo.getString("ext");
                        attachments.add(mAttachmentModel);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Reply prasseReply(NetObject net,ArrayList<Reply> replies,String noticeid) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return null;
            }
            JSONObject object = new JSONObject(json);
            JSONObject jo2 = object.getJSONObject("data");
            Reply mReplyModel = new Reply(jo2.getString("notice_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), noticeid, jo2.getString("create_time"));
            replies.add(0, mReplyModel);
            return mReplyModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int praseReplyDelete(NetObject net,ArrayList<Reply> replies) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            return -1;
        }

        Reply mreply = (Reply) net.item;
        int pos = replies.indexOf(mreply);
        replies.remove(mreply);
        return  pos;
    }

    public static boolean praseData(NetObject net, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return  true;
    }
}
