package intersky.function.prase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.Actions;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.receiver.entity.BusinessWranData;
import intersky.function.receiver.entity.BussinessWarnItem;
import intersky.function.receiver.entity.FunData;
import intersky.function.receiver.entity.Function;
import intersky.function.receiver.entity.TableDetial;
import intersky.function.receiver.entity.WorkFlowData;
import intersky.function.receiver.entity.WorkFlowItem;
import intersky.function.view.activity.GridDetialActivity;
import intersky.function.view.activity.WebMessageActivity;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mywidget.TableCloumArts;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class WorkFlowPrase {

    public static  void praseWorkFlowList(NetObject net, FunData mFunData) {

        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            int state = 0;
            if(net.item != null)
            {
                state = (int) net.item;
            }
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < ja.length() ; i++) {
                XpxJSONObject item = ja.getJSONObject(i);
                String series = item.getString("Module");
                WorkFlowData data = (WorkFlowData) mFunData.funDatas.get(series);
                if(data == null)
                {
                    data = new WorkFlowData();
                    mFunData.funDatas.put(series,data);
                    mFunData.mKeys.add(series);
                }
                String taskID = item.getString("TaskID");
                String subject = item.getString("Subject");
                String receiveTime = item.getString("ReceiveTime");
                String recordid = item.getString("RecordID");
                String InstanceID = item.getString("InstanceID");
                String sender = item.getString("Sender");
                WorkFlowItem mWorkFlowItem= new WorkFlowItem(taskID, recordid,  subject,
                        receiveTime, sender, InstanceID, series,state);
                data.workFlowItems.add(mWorkFlowItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static  void praseBusinessWarnList(NetObject net, FunData mFunData) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            BusinessWranData mBusinessWranData = null;
            if(mFunData.funDatas.containsKey("0"))
            {
                mBusinessWranData = (BusinessWranData) mFunData.funDatas.get("0");
            }
            if(mBusinessWranData == null)
            {
                mBusinessWranData = new BusinessWranData();
                mFunData.funDatas.put("0",mBusinessWranData);
            }
            for (int i = 0; i < ja.length(); i++) {
                XpxJSONObject item = ja.getJSONObject(i);
                String serialID = item.getString("SerialID");
                String caption = item.getString("Module");
                String module = item.getString("Module");
                String parentID = item.getString("ParentID");
                String subject = item.getString("Subject");
                String memo = item.getString("Memo");
                String startTime = item.getString("StartTime");
                mBusinessWranData.bussinessWarnItems
                        .add(new BussinessWarnItem(serialID, caption, module, parentID, subject, memo, startTime));
                mBusinessWranData.count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void praseWorkFlowTask(NetObject net, TableDetial mTableDetial, Function mFunction) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            XpxJSONObject jo = new XpxJSONObject(json);
            if(jo.has("record"))
            {
                XpxJSONObject jo1 = jo.getJSONObject("record");
                XpxJSONArray data = jo1.getJSONArray("data");
                for(int i = 0 ; i < data.length() ; i++)
                {
                    XpxJSONObject jsonObject = data.getJSONObject(i);
                    if(jsonObject.has("RecordID"))
                    {
                        if(jsonObject.getString("RecordID").equals(mFunction.mRecordId))
                        {
                            mTableDetial.recordData = jsonObject.toString();
                        }
                    }
                }
                XpxJSONArray ja = jo1.getJSONArray("fields");
                for (int i = 0; i < ja.length(); ++i) {
                    XpxJSONObject jo3 = (XpxJSONObject) ja.getJSONObject(i);
                    String caption = jo3.getString("caption");
                    String fieldName = jo3.getString("fieldname");
                    String type = "dtstring";
                    if(jo3.has("is_image"))
                    {
                        if(jo3.getBoolean("is_image",false) == true)
                            type = "dtimage";
                    }
                    boolean visiable = true;
                    TableCloumArts mTableCloumArts = new TableCloumArts();
                    mTableCloumArts.mFiledName = fieldName;
                    mTableCloumArts.mCaption = caption;
                    mTableCloumArts.dataType = type;
                    mTableCloumArts.isVisiable = true;
                    mTableDetial.tableCloums.add(mTableCloumArts);
                }
                if(jo1.has("attachment"))
                {
                    XpxJSONArray ja1 = jo1.getJSONArray("attachment");
                    if(ja1.length() > 0)
                    {
                        mTableDetial.attachmentData = ja1.toString();
                    }
                }
                XpxJSONArray ja3 = jo1.getJSONArray("groups");
                for (int i = 0; i < ja3.length(); ++i) {
                    mTableDetial.mHeads.add(ja3.getString(i));
                }
            }
            if(jo.has("task"))
            {
                XpxJSONObject task = jo.getJSONObject("task");
                mTableDetial.backEnabled = task.getBoolean("BackEnabled",false);
                mTableDetial.transmitEnabled = task.getBoolean("TransmitEnabled",false);
                mTableDetial.approvalEnabled = task.getBoolean("ApprovalEnabled",false);
                mFunction.actionjson = task.toString();
            }
            mTableDetial.tempData = mTableDetial.recordData;
        }
        catch (JSONException e) {

        }
    }

    public static boolean praseAction(NetObject json) {
        boolean has = false;
        try {
            String result = json.result;
            JSONObject jo = new JSONObject(result);
            if(jo.has("code"))
            {
                if(jo.getInt("code") == 0)
                {
                    has = true;
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return has;
    }

    public static void startWorkFlowTask(String json , Context mContext, Conversation mConversation) {

        try {
            String[] strs = mConversation.detialId.split(",");
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < ja.length() ; i++) {
                XpxJSONObject item = ja.getJSONObject(i);
                if(item.getString("TaskID").equals(strs[0]))
                {
                    String series = item.getString("Module");
                    String taskID = item.getString("TaskID");
                    String subject = item.getString("Subject");
                    String receiveTime = item.getString("ReceiveTime");
                    String recordid = item.getString("RecordID");
                    String InstanceID = item.getString("InstanceID");

                    WorkFlowItem workFlowItem= new WorkFlowItem(taskID, recordid,  subject,
                            receiveTime, "", InstanceID, series,0);

                    Intent intent = new Intent(mContext,
                            WebMessageActivity.class);
                    intent.putExtra("isurl", true);
                    String url = "";
                    if(FunctionUtils.getInstance().service.https)
                    {
                        url = "https://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/workflow/task?task_id=%@&app_languge="+ AppUtils.getLangue(mContext);
                    }
                    else
                    {
                        url = "http://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/workflow/task?task_id=%@&app_languge="+ AppUtils.getLangue(mContext);
                    }
                    intent.putExtra("url", url);
                    mContext.startActivity(intent);
                    return;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(mConversation.isRead == false)
        {
            Intent intent = new Intent();
            intent.putExtra("type",mConversation.mType);
            intent.putExtra("setread",true);
            intent.putExtra("recordid",mConversation.mRecordId);
            intent.setAction(Actions.ACTION_UPDATA_CONVERSATION);
            mContext.sendBroadcast(intent);
        }
//        AppUtils.showMessage(mContext, mContext.getString(R.string.conversation_approve_type_no));

    }

}
