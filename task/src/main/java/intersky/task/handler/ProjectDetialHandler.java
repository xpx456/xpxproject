package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.entity.Contacts;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectLogAsks;
import intersky.task.asks.ProjectReplyAsks;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.TaskPrase;
import intersky.task.view.activity.ProjectDetialActivity;
import intersky.xpxnet.net.NetObject;

//01
public class ProjectDetialHandler extends Handler {

    public static final int SET_LEADER = 3250101;
    public static final int SET_DES = 3250102;
    public static final int UPDATA_DETIAL = 3250100;
    public static final int PROJECT_EXIST = 3250103;
    public static final int PROJECT_DELETE= 3250104;
    public static final int UPDATA_REPLY= 3250105;
    public ProjectDetialActivity theActivity;

    public ProjectDetialHandler(ProjectDetialActivity mProjectDetialActivity) {
        theActivity = mProjectDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
//            case ProjectAsks.PROJECT_ITEM_DETIAL_SUCCESS:
//                theActivity.waitDialog.hide();
//                theActivity.memberDetial = ProjectPrase.praseProjectDetial((NetObject) msg.obj,theActivity,theActivity.mContactModels);
////                if(memberDetial != null)
////                {
////                    theActivity.memberDetial.admincount = memberDetial.admincount;
////                    theActivity.memberDetial.apply = memberDetial.apply;
////                    theActivity.memberDetial.personcount = memberDetial.personcount;
////                    theActivity.memberDetial.leavlType = memberDetial.leavlType;
////                }
//                theActivity.mProjectDetialPresenter.initdata();
//                break;
            case ProjectAsks.GET_ATTACHMENT_SUCCESS:
                theActivity.mProjectDetialPresenter.praseAttachment((NetObject) msg.obj);
                theActivity.waitDialog.hide();
                break;
            case ProjectLogAsks.GET_LOG_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mProjectDetialPresenter.praseLog((NetObject) msg.obj);
                break;
            case ProjectReplyAsks.GET_REPLAY_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mProjectDetialPresenter.prasseReplys((NetObject) msg.obj);
                break;
            case ProjectReplyAsks.ADD_REPLY_SUCCESS:
                if(ProjectPrase.prasseReply((NetObject) msg.obj,theActivity,theActivity.mReplys))
                {
                    intent = new Intent(ProjectReplyAsks.ACTION_PROJECT_REPLY);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    theActivity.sendBroadcast(intent);
                }
                theActivity.waitDialog.hide();
                break;
            case ProjectReplyAsks.DELETE_REPLY_SUCCESS:
                theActivity.waitDialog.hide();
                if(TaskPrase.praseReplyDelete((NetObject) msg.obj,theActivity,theActivity.mReplys) > 0)
                {
                    intent = new Intent(ProjectReplyAsks.ACTION_PROJECT_REPLY);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case ProjectAsks.PROJECT_SET_NEME_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectPrase.praseSetName((NetObject) msg.obj,theActivity,theActivity.project);
                theActivity.mProjectDetialPresenter.updataName();
                if(theActivity.project.projectId.length() > 0 && !theActivity.project.projectId.equals("0")) {
                    intent = new Intent(ProjectAsks.ACTION_PROJECT_SET_NEME);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    intent.putExtra("name",theActivity.project.mName);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case SET_LEADER:
                intent = (Intent) msg.obj;
                Contacts contacts = intent.getParcelableExtra("contacts");
                if(!contacts.mRecordid.equals(theActivity.project.leaderId))
                {
                    ProjectAsks.setLeader(theActivity,theActivity.mProjectDetialPresenter.mProjectDetialHandler,theActivity.project,contacts,1);
                }
                theActivity.waitDialog.show();
                break;
            case ProjectAsks.SET_LEADER_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mProjectDetialPresenter.updataLeader((NetObject) msg.obj);
                if(theActivity.project.projectId.length() > 0 && !theActivity.project.projectId.equals("0")) {
                    intent = new Intent(ProjectAsks.ACTION_PROJECT_SET_LEADER);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    intent.putExtra("cid",theActivity.project.leaderId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case SET_DES:
                intent = (Intent) msg.obj;
                String value = intent.getStringExtra("value");
                if(!theActivity.project.des.equals(value))
                ProjectAsks.setProjectDes(theActivity,theActivity.mProjectDetialPresenter.mProjectDetialHandler,theActivity.project,value);
                break;
            case ProjectAsks.SET_DES_SUCCESS:
                theActivity.waitDialog.hide();
                ProjectPrase.praseSetDes((NetObject) msg.obj,theActivity,theActivity.project);
                theActivity.mProjectDetialPresenter.updataDes();
                if(theActivity.project.projectId.length() > 0 && !theActivity.project.projectId.equals("0")) {
                    intent = new Intent(ProjectAsks.ACTION_SET_DES);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case UPDATA_DETIAL:
                theActivity.mProjectDetialPresenter.projectDetialUpdata((Intent) msg.obj);
                break;
            case PROJECT_DELETE:
                intent = (Intent) msg.obj;
                String id2 = intent.getStringExtra("projectid");
                if(theActivity.project.projectId.equals(id2))
                    theActivity.finish();
                break;
            case PROJECT_EXIST:
                intent = (Intent) msg.obj;
                String id = intent.getStringExtra("projectid");
                String cid1 = intent.getStringExtra("cid");
                if(theActivity.project.projectId.equals(id) && TaskManager.getInstance().oaUtils.mAccount.mRecordId.equals(cid1))
                    theActivity.finish();
                else
                    theActivity.mProjectDetialPresenter.projectDetialUpdata((Intent) msg.obj);
                break;
            case UPDATA_REPLY:
                intent = (Intent) msg.obj;
                String id1 = intent.getStringExtra("projectid");
                if(theActivity.project.projectId.equals(id1))
                {
                    theActivity.replyPage = 1;
                    theActivity.isreplyall = false;
                    theActivity.mReplys.clear();
                    ProjectReplyAsks.getReplays(theActivity,theActivity.mProjectDetialPresenter.mProjectDetialHandler,theActivity.project,theActivity.replyPage);
                }
                break;
            case ProjectAsks.PROJECT_DETIAL_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.memberDetial = ProjectPrase.praseProjectDetial((NetObject) msg.obj,theActivity,theActivity.mContactModels);
                theActivity.mProjectDetialPresenter.initdata();
                break;
        }

    }
}
