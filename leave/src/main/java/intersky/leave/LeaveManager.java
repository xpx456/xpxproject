package intersky.leave;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Register;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.entity.Leave;
import intersky.leave.handler.HitHandler;
import intersky.leave.view.activity.LeaveActivity;
import intersky.leave.view.activity.LeaveDetialActivity;
import intersky.leave.view.activity.LeaveListActivity;
import intersky.oa.OaUtils;
import intersky.select.entity.Select;

public class LeaveManager {

    public static final String ACTION_LEAVE_UPDATA_HIT = "ACTION_LEAVE_UPDATA_HIT";
    public static final String ACTION_LEAVE_UPDATA_TYPE = "ACTION_LEAVE_UPDATA_TYPE";
    public static final String ACTION_LEAVE_ADD= "ACTION_LEAVE_ADD";
    public static final String ACTION_LEAVE_UPDATE= "ACTION_LEAVE_UPDATE";
    public static final String ACTION_LEAVE_DELETE= "ACTION_LEAVE_DELETE";
    public static final String ACTION_LEAVE_UPATE_SENDER = "ACTION_LEAVE_UPATE_SENDER";
    public static final String ACTION_LEAVE_UPATE_COPYER = "ACTION_LEAVE_UPATE_COPYER";
    public static final String ACTION_LEAVE_ADDPICTORE = "ACTION_LEAVE_ADDPICTORE";
    public static final String ACTION_SET_LEAVE_TYPE = "ACTION_SET_LEAVE_TYPE";
    public static final String ACTION_SET_LEAVE_CONTENT = "ACTION_SET_LEAVE_CONTENT";
    public static final String LEAVEINFO = "leave_info";
    public static final String LEAVETSENDER = "leavesender";
    public static final String LEAVECOPYER = "leavecopyer";
    public static final int TYPE_APPROVE = 1;
    public static final int TYPE_SEND = 3;
    public static final int TYPE_COPY = 2;
    public static final int MAX_PIC_COUNT = 9;
    public Context context;
    public int mehit = 0;
    public int sendhit = 0;
    public int copyerhit = 0;
    public Contacts setContacts;
    public ArrayList<Select> mLeaveTypes = new ArrayList<Select>();
    public HitHandler hitHandler;
    public Select defaultType;
    public Register register;
    public OaUtils oaUtils;
    public static volatile LeaveManager mLeaveManager;
    public static LeaveManager init(OaUtils oaUtils,Context context) {

        if (mLeaveManager == null) {
            synchronized (OaUtils.class) {
                if (mLeaveManager == null) {
                    mLeaveManager = new LeaveManager(oaUtils,context);
                }
                else
                {
                    mLeaveManager.context = context;
                    mLeaveManager.oaUtils = oaUtils;
                    mLeaveManager.hitHandler = new HitHandler(context);
                }
            }
        }
        return mLeaveManager;
    }

    public static LeaveManager getInstance() {
        return mLeaveManager;
    }

    public LeaveManager(OaUtils oaUtils,Context context) {
        this.context = context;
        this.oaUtils = oaUtils;
        hitHandler = new HitHandler(context);
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public void upDateHit() {

        LeaveAsks.getLeaveHit(hitHandler,context,getSetUserId());
    }

    public void getLeaveType() {

        LeaveAsks.getLeaveTypes(hitHandler,context);
    }

    public static String getMemberIds(ArrayList<Contacts> contacts) {
        String ids = "";
        for (int i = 0; i < contacts.size(); i++) {
            Contacts mContacts = contacts.get(i);
            if (ids.length() > 0) {
                ids += mContacts.mRecordid;
            } else {
                ids += mContacts.mRecordid + ",";
            }

        }
        return ids;
    }

    public String getSetUserId() {
        if(setContacts == null)
        {
            return oaUtils.mAccount.mRecordId;
        }
        else
        {
            return setContacts.mRecordid;
        }
    }

    public String gettypeName(String id) {
        for (int i = 0; i < mLeaveTypes.size(); i++) {
            if (mLeaveTypes.get(i).mId.equals(id)) {
                return mLeaveTypes.get(i).mName;
            }
        }

        return "";
    }

    public Select gettype(String id) {
        for (int i = 0; i < mLeaveTypes.size(); i++) {
            if (mLeaveTypes.get(i).mId.equals(id)) {
                return mLeaveTypes.get(i);
            }
        }

        return null;
    }

    public void cleanSelect()
    {
        for (int i = 0; i < mLeaveTypes.size(); i++) {
            mLeaveTypes.get(i).iselect = false;
        }
    }

    public void sendLeaveUpdate(String leaveid) {
        Intent intent = new Intent(ACTION_LEAVE_UPDATE);
        intent.putExtra("leaveid",leaveid);
        context.sendBroadcast(intent);
    }

    public void sendLeaveAdd() {
        Intent intent = new Intent(ACTION_LEAVE_ADD);
        context.sendBroadcast(intent);
    }

    public void sendLeaveDelete(String leaveid) {
        Intent intent = new Intent(ACTION_LEAVE_DELETE);
        intent.putExtra("leaveid",leaveid);
        context.sendBroadcast(intent);
    }


    public void startDetial(Context context,String recordid) {
        BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
        baseActivity.waitDialog.show();
        Leave leave = new Leave();
        leave.lid = recordid;
        LeaveAsks.getDetial(hitHandler,context,leave);
    }

    public void startDetial(Context context,Leave leave) {
        Intent intent = new Intent(context, LeaveDetialActivity.class);
        intent.putExtra("leave",leave);
        context.startActivity(intent);
    }

    public void startLeave(Context context) {
        Intent intent = new Intent(context, LeaveListActivity.class);
        context.startActivity(intent);
    }

    public void newLeave(Context context) {
        Intent intent = new Intent(context, LeaveActivity.class);
        Leave leave = new Leave();
        if(LeaveManager.getInstance().oaUtils.mAccount.mManagerId.length() != 0)
        {
            Contacts model = (Contacts) Bus.callData(context,"chat/getContactItem",LeaveManager.getInstance().oaUtils.mAccount.mManagerId);
            leave.senders = LeaveManager.getInstance().getSenders();
            leave.copyers = LeaveManager.getInstance().getCopyers();
            if(leave.senders.length() == 0)
            {
                leave.senders = model.mRecordid;
            }
        }
        else
        {
            leave.senders = LeaveManager.getInstance().getSenders();
            leave.copyers = LeaveManager.getInstance().getCopyers();
        }
        intent.putExtra("leave", leave);
        context.startActivity(intent);
    }

    public String getSenders()
    {
        SharedPreferences sharedPre = context.getSharedPreferences(LeaveManager.LEAVEINFO, 0);
        return sharedPre.getString(LeaveManager.LEAVETSENDER+ oaUtils.mAccount.mRecordId
                + oaUtils.service.sAddress+oaUtils.service.sPort,"");
    }

    public String getCopyers()
    {
        SharedPreferences sharedPre =context.getSharedPreferences(LeaveManager.LEAVEINFO, 0);
        return sharedPre.getString(LeaveManager.LEAVECOPYER+oaUtils.mAccount.mRecordId
                +oaUtils.service.sAddress+oaUtils.service.sPort,"");
    }
}
