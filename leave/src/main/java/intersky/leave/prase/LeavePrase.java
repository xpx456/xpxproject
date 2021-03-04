package intersky.leave.prase;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.leave.LeaveManager;
import intersky.leave.R;
import intersky.leave.entity.Leave;
import intersky.leave.view.adapter.LeaveAdapter;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class LeavePrase {

    public static void praseHit(NetObject net)
    {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }

            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject jo= jsonObject.getJSONObject("data");
            LeaveManager.getInstance().mehit = jo.getInt("me_total",0);
            LeaveManager.getInstance().sendhit = jo.getInt("sender_total",0);
            LeaveManager.getInstance().copyerhit = jo.getInt("copyer_total",0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseType(NetObject net) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }
            LeaveManager.getInstance().mLeaveTypes.clear();
            LeaveManager.getInstance().hashMap.clear();
            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                Select mSelectTypeModel = new Select(String.valueOf(jo.get("leave_type_id")), String.valueOf(jo.get("name")));

                if (String.valueOf(jo.get("name")).equals(LeaveManager.getInstance().context.getString(R.string.leave_type_0))) {
                    LeaveManager.getInstance().mLeaveTypes.add(0,mSelectTypeModel);
                }
                else
                {
                    LeaveManager.getInstance().mLeaveTypes.add(mSelectTypeModel);
                }
                LeaveManager.getInstance().hashMap.put(mSelectTypeModel.mId,mSelectTypeModel);
            }
            if(LeaveManager.getInstance().mLeaveTypes.size() > 0)
            {
                LeaveManager.getInstance().defaultType = LeaveManager.getInstance().mLeaveTypes.get(0);
                LeaveManager.getInstance().defaultType.iselect = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseList(NetObject net, LeaveAdapter mLeaveAdapter,LeaveAdapter mSLeaveAdapter,String keyword, Context context) {
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

            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("data");
            for (int i = 0; i < ja.length() - 1; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                String nowapprove = "";
                if (jo.has("get_approver") && jo.getInt("is_approval",0) == 0) {

                    XpxJSONObject obj = jo.getJSONObject("get_approver");
                    nowapprove = obj.getString("sender_id");
                }
                Leave mLeave = new Leave(jo.getString("leave_id"), jo.getString("user_id"), jo.getString("company_id"), jo.getString("begin_time")
                        , jo.getString("end_time"), jo.getString("days"), jo.getString("sender_id"), jo.getString("copyer_id"), nowapprove
                        , jo.getString("leave_type_id"), jo.getInt("is_approval",0), jo.getString("create_time"), jo.getString("content"));
//                if((int)net.item == LeaveManager.TYPE_COPY)
                {
                    if(jo.getInt("is_read",0) == 0)
                    {
                        mLeave.isread = false;
                    }
                    else
                    {
                        mLeave.isread = true;
                    }
                }
                mLeave.type = (int) net.item;

                if (jo.getString("is_approval").equals("0")) {
                    if (nowapprove.equals(LeaveManager.getInstance().oaUtils.mAccount.mRecordId)) {
                        mLeave.is_approval = 0;
                    } else {
                        mLeave.is_approval = 1;
                    }
                }
                mLeaveAdapter.getmLeaves().add(mLeave);
                if(keyword.length() > 0)
                {
                    if(mLeave.name.contains(keyword)) {
                        mSLeaveAdapter.getmLeaves().add(mLeave);
                    }
                }
            }
            if (mLeaveAdapter.totalCount == -1) {
                XpxJSONObject jo = ja.getJSONObject(ja.length() - 1);
                mLeaveAdapter.totalCount = jo.getInt("total_results",0);

            }
            if (ja.length() > 0) {
                mLeaveAdapter.nowpage++;
            }
            if (mLeaveAdapter.totalCount == 0 && mLeaveAdapter.nowpage == 1) {
                AppUtils.showMessage(context, context.getString(R.string.noInfoFind));
            }
            mLeaveAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseList2(NetObject net, LeaveAdapter mLeaveAdapter,LeaveAdapter mSLeaveAdapter,String keyword, Context context) {
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

            XpxJSONObject object = new XpxJSONObject(json);
            XpxJSONArray ja = object.getJSONArray("data");
            for (int i = 0; i < ja.length() - 1; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                String nowapprove = "";
                if (jo.has("get_approver") && jo.getInt("is_approval",0) == 0) {

                    XpxJSONObject obj = jo.getJSONObject("get_approver");
                    nowapprove = obj.getString("sender_id");
                }
                Leave mLeave = new Leave(jo.getString("leave_id"), jo.getString("user_id"), jo.getString("company_id"), jo.getString("begin_time")
                        , jo.getString("end_time"), jo.getString("days"), jo.getString("sender_id"), jo.getString("copyer_id"), nowapprove
                        , jo.getString("leave_type_id"), jo.getInt("is_approval",0), jo.getString("create_time"), jo.getString("content"));
                if((int)net.item == LeaveManager.TYPE_COPY)
                {
                    if(jo.getInt("is_read",0) == 0)
                    {
                        mLeave.isread = false;
                    }
                    else
                    {
                        mLeave.isread = true;
                    }
                }
                mLeave.type = (int) net.item;

                if (jo.getString("is_approval").equals("0")) {
                    if (nowapprove.equals(LeaveManager.getInstance().oaUtils.mAccount.mRecordId)) {
                        mLeave.is_approval = 0;
                    } else {
                        mLeave.is_approval = 1;
                    }
                }
                mLeaveAdapter.getmLeaves().add(mLeave);
                if(keyword.length() > 0)
                {
                    if(mLeave.name.contains(keyword)) {
                        mSLeaveAdapter.getmLeaves().add(mLeave);
                    }
                }
            }
            if (mLeaveAdapter.totalCount == -1) {
                XpxJSONObject jo = ja.getJSONObject(ja.length() - 1);
                mLeaveAdapter.totalCount = jo.getInt("total_results",0);

            }
            if (ja.length() > 0) {
                mLeaveAdapter.nowpage++;
            }
            if (mLeaveAdapter.totalCount == 0 && mLeaveAdapter.nowpage == 1) {
                AppUtils.showMessage(context, context.getString(R.string.noInfoFind));
            }
            mLeaveAdapter.notifyDataSetChanged();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
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
            Leave leave = (Leave) net.item;
            if(leave.isread == false)
            {
                leave.isread = true;
                LeaveManager.getInstance().sendLeaveUpdate(leave.lid);
                LeaveManager.getInstance().upDateHit();
                Bus.callData(context,"function/updateOahit");
            }
            Bus.callData(context,"conversation/setdetialread", Conversation.CONVERSATION_TYPE_LEAVE,leave.lid);
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            leave.uid = jo.getString("user_id");
            leave.start = jo.getString("begin_time");
            leave.end = jo.getString("end_time");
            leave.leave_type_id = jo.getString("leave_type_id");
            leave.count = jo.getString("days");
            leave.content = jo.getString("content");
            leave.is_approval = jo.getInt("is_approval",0);
            leave.attachs = jo.getString("image");
            if(jo.has("has_many_prog"))
            {
                XpxJSONArray ja = jo.getJSONArray("has_many_prog");
                leave.approverjson = ja.toString();
            }


            if(jo.has("get_approver")&& jo.getInt("is_approval",0) == 0)
            {
                XpxJSONObject jo2 = jo.getJSONObject("get_approver");
                leave.nowapprover = jo2.getString("sender_id");
            }
            leave.copyers = jo.getString("copyer_id");


        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void praseAddtchment(NetObject net, ArrayList<Attachment> attachments, Leave report) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }
            report.approverjson = net.result;
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
                                Bus.callData(LeaveManager.getInstance().context,"filetools/getfilePath", "/" + "leave")+"/"+jo.getString("hash")+jo.getString("ext"),
                                LeaveManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")),jo.getLong("size"),0,"");
                        mAttachmentModel.mPath2 = Bus.callData(LeaveManager.getInstance().context,"filetools/getfilePath", "/" + "leave")+"/"+jo.getString("hash")+"2"+jo.getString("ext");
                        attachments.add(mAttachmentModel);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseAddtchment(String json, ArrayList<Attachment> attachments,Leave report) {
        try {
            report.approverjson = json;
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
                                Bus.callData(LeaveManager.getInstance().context,"filetools/getfilePath", "/" + "leave")+"/"+jo.getString("hash")+jo.getString("ext"),
                                LeaveManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")),jo.getLong("size"),0,"");
                        mAttachmentModel.mPath2 = Bus.callData(LeaveManager.getInstance().context,"filetools/getfilePath","/" + "Intersky" + "/" + "leave")+"/"+jo.getString("hash")+"2"+jo.getString("ext");
                        attachments.add(mAttachmentModel);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
