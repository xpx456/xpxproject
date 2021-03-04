package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.TaskPrase;
import intersky.task.view.activity.TaskMemberActivity;
import intersky.task.view.adapter.TaskContactAdapter;
import intersky.xpxnet.net.NetObject;

//08
public class TaskMemberHandler extends Handler {

    public static final int ADD_MEMBER = 3250800;
    public static final int UPDATA_PROJECT = 3250801;
    public static final int UPDATA_TASK = 3250802;
    public static final int TASK_EXIST = 3250803;
    public static final int PROJECT_EXIST = 3250804;
    public TaskMemberActivity theActivity;

    public TaskMemberHandler(TaskMemberActivity mTaskMemberActivity) {
        theActivity = mTaskMemberActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {

            case TaskContactAdapter.EVENT_START_MORE:
                theActivity.mTaskMemberPresenter.showMore((Contacts) msg.obj);
                break;
            case ADD_MEMBER:
                theActivity.mTaskMemberPresenter.addMember((Intent) msg.obj);
                break;
            case TaskAsks.SET_LEADER_SUCCESS:
                Contacts contacts = theActivity.mTaskMemberPresenter.praseData((NetObject) msg.obj);
                if(contacts != null)
                {
                    intent = new Intent(TaskAsks.ACTION_TASK_LEADER);
                    intent.putExtra("taskid",theActivity.task.taskId);
                    intent.putExtra("cid",contacts.mRecordid);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case TaskAsks.ADD_TASK_MEMBER_SUCCESS:
                if(theActivity.mTaskMemberPresenter.praseData2((NetObject) msg.obj))
                {
                    intent = new Intent(TaskAsks.ACTION_TASK_ADD_MEMBER);
                    intent.putExtra("taskid",theActivity.task.taskId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case ProjectAsks.SET_LEADER_SUCCESS:
                Contacts contacts1 = theActivity.mTaskMemberPresenter.praseData((NetObject) msg.obj);
                if(contacts1 != null)
                {
                    intent = new Intent(ProjectAsks.ACTION_PROJECT_SET_LEADER);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    intent.putExtra("cid",contacts1.mRecordid);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case ProjectAsks.ADD_MEMBER_SUCCESS:
                theActivity.waitDialog.hide();
                if(theActivity.mTaskMemberPresenter.praseData2((NetObject) msg.obj))
                {

                    intent = new Intent(ProjectAsks.ACTION_PROJECT_ADD_MEMBER);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case TaskAsks.EXIT_TASK_SUCCESS:
                Contacts contacts3 = theActivity.mTaskMemberPresenter.praseData((NetObject) msg.obj);
                if(contacts3 != null)
                {
                    intent = new Intent(TaskAsks.ACTION_EXIT_TASK);
                    intent.putExtra("taskid",theActivity.task.taskId);
                    intent.putExtra("guid",theActivity.activityRid);
                    intent.putExtra("cid",contacts3.mRecordid);
                    theActivity.sendBroadcast(intent);
                }
                if(contacts3.mRecordid.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId) && !theActivity.task.userId.equals(contacts3.mRecordid))
                {

                    theActivity.finish();
                }
                else
                theActivity.mTaskMemberPresenter.updataMember();
                break;
            case ProjectAsks.REMOVE_MEMBER_SUCCESS:
                theActivity.waitDialog.hide();
                Contacts contacts4 = theActivity.mTaskMemberPresenter.praseData((NetObject) msg.obj);
                if(contacts4 != null)
                {
                    intent = new Intent(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    intent.putExtra("guid",theActivity.activityRid);
                    intent.putExtra("cid",contacts4.mRecordid);
                    theActivity.sendBroadcast(intent);
                }
                if(contacts4.mRecordid.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
                {

                    theActivity.finish();
                }
                else
                theActivity.mTaskMemberPresenter.updataMember();
                break;
            case ProjectAsks.MEMBER_ACCESS_SUCCESS:
                break;
            case ProjectAsks.PROJECT_DETIAL_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectPrase.MemberDetial memberDetial = ProjectPrase.praseProjectDetial((NetObject) msg.obj,theActivity,theActivity.mMembers);
                if(memberDetial != null)
                {
                    theActivity.memberDetial = memberDetial;
                }
                theActivity.mContactAdapter.notifyDataSetChanged();
                break;
            case TaskAsks.TASK_DETIAL_SUCCESS:
                TaskPrase.praseTask((NetObject) msg.obj,theActivity);
                Bus.callData(theActivity,"chat/getContacts",theActivity.task.senderIdList, theActivity.mMembers);
                theActivity.mContactAdapter.notifyDataSetChanged();
                break;
            case UPDATA_PROJECT:
                theActivity.mTaskMemberPresenter.updataProjectMember((Intent) msg.obj);
                break;
            case UPDATA_TASK:
                theActivity.mTaskMemberPresenter.updataTaskMember((Intent) msg.obj);
                break;
            case TASK_EXIST:
                intent = (Intent) msg.obj;
                String cid = intent.getStringExtra("cid");
                String tid = intent.getStringExtra("taskid");
                String arid = "";
                if(intent.hasExtra("guid"))
                {
                    arid = intent.getStringExtra("guid");
                }
                if(!arid.equals(theActivity.activityRid))
                {
                    if(tid.equals(theActivity.task.taskId) && cid.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId) && !cid.equals(theActivity.task.userId))
                    {
                        theActivity.finish();
                    }
                }
                break;
            case PROJECT_EXIST:
                intent = (Intent) msg.obj;
                String id = intent.getStringExtra("projectid");
                String cid1 = intent.getStringExtra("cid");
                String arid1 = "";
                if(intent.hasExtra("guid"))
                {
                    arid1 = intent.getStringExtra("guid");
                }
                if(!arid1.equals(theActivity.activityRid))
                {
                    if(theActivity.project.projectId.equals(id) && TaskManager.getInstance().oaUtils.mAccount.mRecordId.equals(cid1))
                        theActivity.finish();
                }

                break;
        }

    }
}
