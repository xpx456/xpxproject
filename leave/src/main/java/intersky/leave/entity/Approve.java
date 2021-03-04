package intersky.leave.entity;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.leave.LeaveManager;
import intersky.leave.R;

/**
 * Created by xpx on 2017/5/24.
 */

public class Approve {

    public String leave_prog_id = "";
    public String content = "";
    public String create_time = "";
    public String userid = "";
    public int isapprove = 0;
    public Contacts mContact;

    public Approve(String leave_prog_id, String create_time, String content)
    {
        this.leave_prog_id = leave_prog_id;
        this.create_time = create_time;
        String reg = "[^\u4e00-\u9fa5]";
        String approve = content.replaceAll(reg, "");
        userid = content.substring(0,content.length()-approve.length());
        if(userid.length() != 0)
        mContact = (Contacts) Bus.callData(LeaveManager.getInstance().context,"chat/getContactItem",userid);

        if(mContact == null)
        {
            mContact = new Contacts("","");
        }
        if(userid.length() != 0)
        this.content = approve;
        else
            this.content = approve;
        if(approve.equals(LeaveManager.getInstance().context.getString(R.string.approveing)))
        {
            isapprove = 0;
        }
        else if(approve.equals(LeaveManager.getInstance().context.getString(R.string.approve_access)) || approve.equals(LeaveManager.getInstance().context.getString(R.string.approve_finish)))
        {
            isapprove = 2;
        }
        else
        {
            isapprove = 3;
        }

    }

}
