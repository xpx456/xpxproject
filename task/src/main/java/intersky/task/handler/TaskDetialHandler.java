package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.oa.OaAsks;
import intersky.oa.OaUtils;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectReplyAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.asks.TaskAttachmentAsks;
import intersky.task.asks.TaskListAsks;
import intersky.task.asks.TaskLogAsks;
import intersky.task.asks.TaskReplyAsks;
import intersky.task.entity.Contral;
import intersky.task.entity.Task;
import intersky.task.entity.TaskList;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.TaskListPrase;
import intersky.task.prase.TaskPrase;
import intersky.task.view.activity.TaskDetialActivity;
import intersky.task.view.adapter.ListViewAdapter;
import intersky.xpxnet.net.NetObject;
//06
public class TaskDetialHandler extends Handler {

    public static final int SET_TAG = 3250600;
    public static final int SET_LEADER = 3250601;
    public static final int SET_DES = 3250602;
    public static final int ADD_TASK_PIC = 3250603;
    public static final int TASK_DETIAL_UPDATA = 3250604;
    public static final int PROJECT_DETIAL_UPDATA = 3250605;
    public static final int TASK_EXIST = 3250606;
    public static final int UPDATA_NAME = 3250607;
    public static final int PROJECT_DELETE = 3250608;
    public static final int TASK_DELETE = 3250609;
    public static final int UPDATA_REPLY= 3250610;
    public static final int EVENT_UPDATA_SONW = 3250611;
    public TaskDetialActivity theActivity;

    public TaskDetialHandler(TaskDetialActivity mTaskDetialActivity) {
        theActivity = mTaskDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case TaskAsks.TASK_DETIAL_SUCCESS:
                Task task =TaskPrase.praseTaskDetial((NetObject) msg.obj,theActivity);
                if(task.taskId.equals(theActivity.mTask.taskId))
                {
                    theActivity.mTaskDetialPresenter.updataBaseView();
                    theActivity.mTaskDetialPresenter.getAll();
                }
                else
                {
                    theActivity.mTaskDetialPresenter.praseTaskParent();
                }
                break;
            case ProjectAsks.PROJECT_ITEM_DETIAL_SUCCESS:
                ProjectPrase.praseProjectItemDetial((NetObject) msg.obj,theActivity);
                theActivity.mTaskDetialPresenter.praseProjectView();
                break;
            case TaskAsks.GET_SON_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.sonList =TaskPrase.praseSon((NetObject) msg.obj,theActivity);
                theActivity.mTaskDetialPresenter.praseSonView();
                break;
            case TaskAsks.GET_PARENT_SUCCESS:
                TaskPrase.praseTaskDetial((NetObject) msg.obj,theActivity);
                theActivity.mTaskDetialPresenter.praseTaskParent();
                theActivity.waitDialog.hide();
                break;
            case TaskListAsks.TASK_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                TaskListPrase.praseList((NetObject) msg.obj,theActivity,theActivity.mList);
                theActivity.mListViewAdapter.notifyDataSetChanged();
                break;
            case TaskAsks.GET_CONTRAL_SUCCESS:
                theActivity.waitDialog.hide();
                TaskPrase.praseContral((NetObject) msg.obj,theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler);
                theActivity.mTaskDetialPresenter.praseContralView();
                break;
            case TaskLogAsks.GET_LOG_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mTaskDetialPresenter.praseLog((NetObject) msg.obj);
                break;
            case TaskAttachmentAsks.GET_ATTACHMENT_SUCCESS:
                theActivity.mTaskDetialPresenter.praseAttachment((NetObject) msg.obj);
                theActivity.waitDialog.hide();
                break;
            case TaskReplyAsks.GET_REPLAY_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mTaskDetialPresenter.prasseReplys((NetObject) msg.obj);
                break;
            case TaskReplyAsks.DELETE_REPLY_SUCCESS:
                theActivity.waitDialog.hide();
//                int pos = TaskPrase.praseReplyDelete((NetObject) msg.obj,theActivity.mReplys);
//                ReplyUtils.removeReplyView(pos,theActivity.mReplys,null,theActivity.mAnswerlayer,theActivity);
                int pos = TaskPrase.praseReplyDelete((NetObject) msg.obj,theActivity,theActivity.mReplys);
                if(pos > 0)
                {
                    intent = new Intent(TaskReplyAsks.ACTION_TASK_REPLY_UPDATA);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    intent.putExtra("guid",theActivity.activityRid);
                    ReplyUtils.removeReplyView(pos,theActivity.mReplys,null,theActivity.mAnswerlayer,theActivity);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case TaskListAsks.TASK_LIST_ADD_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mListViewAdapter.notifyItemChanged(TaskListPrase.praseAddList((NetObject) msg.obj,theActivity,theActivity.mList));
                intent = new Intent(TaskListAsks.ACTION_TASK_LIST_UPDATA);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskListAsks.LIST_DELETE_SUCCESS:
                theActivity.waitDialog.hide();
                TaskListAsks.getList(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask);
                intent = new Intent(TaskListAsks.ACTION_TASK_LIST_UPDATA);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskListAsks.LIST_ITEM_DELETE_SUCCESS:
                theActivity.waitDialog.hide();
                TaskListPrase.updataListItemName((NetObject) msg.obj,theActivity);
                theActivity.mListViewAdapter.notifyDataSetChanged();
                intent = new Intent(TaskListAsks.ACTION_TASK_LIST_UPDATA);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskAsks.TASK_ADD_SUCCESS:
                theActivity.waitDialog.hide();
                Task task1 = TaskPrase.praseAdd((NetObject) msg.obj,theActivity);
                if(task1 != null) {
                    intent = new Intent(TaskAsks.ACTION_TASK_ADD);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    intent.putExtra("guid",theActivity.activityRid);
                    theActivity.sendBroadcast(intent);
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.creat_sccess));
                }
                TaskAsks.getSon(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask);
                break;
            case TaskAsks.SET_IS_STARE:
                theActivity.waitDialog.hide();
                TaskPrase.praseStare((NetObject) msg.obj,theActivity.mTask,theActivity);
                theActivity.mTaskDetialPresenter.setStare();
                intent = new Intent(TaskAsks.ACTION_TASK_STARE);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case SET_TAG:
                theActivity.mTaskDetialPresenter.setTag();
                break;
            case TaskAsks.SET_TAG_SUCCESS:
                theActivity.waitDialog.hide();
                TaskPrase.praseTag((NetObject) msg.obj,theActivity.mTask,theActivity.mTags,theActivity);
                theActivity.mTaskDetialPresenter.setTagViews();
                intent = new Intent(TaskAsks.ACTION_TASK_TAG);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskAsks.SET_IS_LOCK:
                theActivity.waitDialog.hide();
                TaskPrase.praseLock((NetObject) msg.obj,theActivity.mTask,theActivity);
                theActivity.mTaskDetialPresenter.setSLock();
                intent = new Intent(TaskAsks.ACTION_TASK_LOCK);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskAsks.DELETE_TASK_SUCCESS:
                theActivity.waitDialog.hide();
                if(TaskPrase.praseDelete((NetObject) msg.obj,theActivity))
                {
                    theActivity.finish();
                    intent = new Intent(TaskAsks.ACTION_DELETE_TASK);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    intent.putExtra("guid",theActivity.activityRid);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case TaskAsks.EXIT_TASK_SUCCESS:
                theActivity.waitDialog.hide();
                if(TaskPrase.praseExist((NetObject) msg.obj,theActivity))
                {
                    intent = new Intent(TaskAsks.ACTION_EXIT_TASK);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    intent.putExtra("guid",theActivity.activityRid);
                    theActivity.sendBroadcast(intent);
                    theActivity.finish();
                }
                break;
            case TaskReplyAsks.ADD_REPLY_SUCCESS:
                theActivity.waitDialog.hide();
                Reply mReplyModel = TaskPrase.prasseReply((NetObject) msg.obj,theActivity,theActivity.mReplys);
                if(mReplyModel != null)
                {
                    theActivity.mEditTextContent.setText("");
                    intent = new Intent(TaskReplyAsks.ACTION_TASK_REPLY_UPDATA);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    intent.putExtra("guid",theActivity.activityRid);
                    theActivity.sendBroadcast(intent);
                    ReplyUtils.addReply(mReplyModel,true,theActivity,theActivity.mDeleteReplyListenter,theActivity.mAnswerlayer,theActivity.mTaskDetialPresenter.mTaskDetialHandler);
                }
                break;
            case TaskAsks.TASK_FINISH_SET_FINISH_SUCCESS:
                if(TaskPrase.praseFinish((NetObject) msg.obj,theActivity,1))
                {
                    intent = new Intent(TaskAsks.ACTION_TASK_FINSH);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    theActivity.sendBroadcast(intent);
                    NetObject netObject = (NetObject) msg.obj;
                    Task taskitem = (Task) netObject.item;
                    if(taskitem.taskId.equals(theActivity.mTask.taskId))
                    theActivity.mSnowView.startfly();
                }

                break;
            case TaskAsks.TASK_FINISH_SET_UNFINISH_SUCCESS:
                if(TaskPrase.praseFinish((NetObject) msg.obj,theActivity,0))
                {
                    intent = new Intent(TaskAsks.ACTION_TASK_FINSH);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case TaskAsks.TASK_FINISH_SET_FINISH_ALL_SUCCESS:
                theActivity.waitDialog.hide();
                if(TaskPrase.praseFinish((NetObject) msg.obj,theActivity,1))
                {
                    NetObject netObject = (NetObject) msg.obj;
                    Task taskitem = (Task) netObject.item;
                    if(taskitem.taskId.equals(theActivity.mTask.taskId))
                    theActivity.mSnowView.startfly();
                    TaskAsks.getSon(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask);
                    intent = new Intent(TaskAsks.ACTION_TASK_FINSH);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case TaskAsks.TASK_FINISH_SET_UNFINISH_ALL_SUCCESS:
                theActivity.waitDialog.hide();
                if(TaskPrase.praseFinish((NetObject) msg.obj,theActivity,0))
                {
                    TaskAsks.getSon(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask);
                    intent = new Intent(TaskAsks.ACTION_TASK_FINSH);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case SET_LEADER:
                intent = (Intent) msg.obj;
                Contacts contacts = intent.getParcelableExtra("contacts");
                if(!contacts.mRecordid.equals(theActivity.mTask.leaderId))
                {
                    theActivity.waitDialog.show();
                    TaskAsks.setLeader(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask,contacts);
                }
                break;
            case TaskAsks.SET_LEADER_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mTaskDetialPresenter.updataLeader((NetObject) msg.obj);
                intent = new Intent(TaskAsks.ACTION_TASK_LEADER);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                intent.putExtra("cid",theActivity.mTask.leaderId);
                theActivity.sendBroadcast(intent);
                break;
            case TaskAsks.SET_NAME_SUCCESS:
                theActivity.waitDialog.hide();
                TaskPrase.praseName((NetObject) msg.obj,theActivity.mTask,theActivity);
                theActivity.mTaskDetialPresenter.updataName();
                intent = new Intent(TaskAsks.ACTION_TASK_NAME_SUCCESS);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskAsks.SET_BEGIN_TIME_SUCCESS:
                theActivity.waitDialog.hide();
                TaskPrase.praseBegin((NetObject) msg.obj,theActivity.mTask,theActivity);
                theActivity.mTaskDetialPresenter.updataBeginTime();
                intent = new Intent(TaskAsks.ACTION_TASK_TIME);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskAsks.SET_END_TIME_SUCCESS:
                theActivity.waitDialog.hide();
                TaskPrase.praseEnd((NetObject) msg.obj,theActivity.mTask,theActivity);
                theActivity.mTaskDetialPresenter.updataEndTime();
                theActivity.mTaskDetialPresenter.initfinish();
                intent = new Intent(TaskAsks.ACTION_TASK_TIME);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case SET_DES:
                intent = (Intent) msg.obj;
                String value = intent.getStringExtra("value");
                if(value.length() > 0 && (!value.equals(theActivity.mTask.des)))
                TaskAsks.setDes(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask,value);
                break;
            case TaskAsks.SET_DES_SUCCESS:
                theActivity.waitDialog.hide();
                TaskPrase.praseDes((NetObject) msg.obj,theActivity.mTask,theActivity);
                theActivity.mTaskDetialPresenter.updataDes();
                intent = new Intent(TaskAsks.ACTION_TASK_DES_SUCCESS);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case OaUtils.EVENT_UPLOAD_FILE_RESULT:
                theActivity.mTaskDetialPresenter.sendtoTask((String) msg.obj);
                break;
            case OaUtils.EVENT_UPLOAD_FILE_RESULT_FAIL:
                theActivity.waitDialog.hide();
                AppUtils.showMessage(theActivity, (String) msg.obj);
                break;
            case TaskAttachmentAsks.ADD_ATTACHMENT_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.uploadnames = "";
                theActivity.mUploadAttachments.clear();
                if(theActivity.uploadContral == null)
                {
                    theActivity.mAttachments.clear();
                    theActivity.mTaskDetialPresenter.updataTaskAttchment();
                }
                else
                {
                    if(TaskPrase.praseContralAttachment((NetObject) msg.obj,theActivity,theActivity.uploadContral))
                    theActivity.mTaskDetialPresenter.setContralsAttachment();
                }
                intent = new Intent(TaskAsks.ACTION_TASK_ATTACHMENT);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case ADD_TASK_PIC:
                theActivity.mTaskDetialPresenter.addPicResult((Intent) msg.obj);
                break;
            case ListViewAdapter.EVENT_DO_CHANGE:
                theActivity.mTaskDetialPresenter.onChange();
                break;
            case TaskListAsks.CHANGE_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                if(TaskListPrase.praseChange((NetObject) msg.obj,theActivity))
                    TaskListAsks.getList(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask);
                intent = new Intent(TaskListAsks.ACTION_TASK_LIST_UPDATA);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskAsks.SET_CONTRAL_SUCCESS:
                theActivity.waitDialog.hide();
                NetObject item48 = (NetObject) msg.obj;
                Contral contral = TaskPrase.praseContralSet((NetObject) msg.obj,theActivity);
                if(contral != null)
                theActivity.mTaskDetialPresenter.upDataContaralView(contral);
                theActivity.uploadContral = null;
                intent = new Intent(TaskAsks.ACTION_TASK_CONTRAL);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskAttachmentAsks.DEL_ATTACHMENT_SUCCESS:
                NetObject netObject21 = (NetObject) msg.obj;
                Attachment attchmentnames = TaskPrase.praseContralAttachmentDel((NetObject) msg.obj,theActivity);
                if(attchmentnames != null)
                theActivity.mTaskDetialPresenter.deletePic(attchmentnames);
                theActivity.waitDialog.hide();
                intent = new Intent(TaskAsks.ACTION_TASK_ATTACHMENT);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskListAsks.CHANGE_LIST_ITEM_NAME_SUCCESS:
                theActivity.waitDialog.hide();
                TaskListPrase.updataListItemName((NetObject) msg.obj,theActivity);
                theActivity.mListViewAdapter.notifyDataSetChanged();
                intent = new Intent(TaskListAsks.ACTION_TASK_LIST_UPDATA);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskListAsks.CHANGE_LIST_NAME_SUCCESS:
                theActivity.waitDialog.hide();
                TaskListPrase.updataListName((NetObject) msg.obj,theActivity);
                theActivity.mListViewAdapter.notifyDataSetChanged();
                intent = new Intent(TaskListAsks.ACTION_TASK_LIST_UPDATA);
                intent.putExtra("taskid",theActivity.mTask.taskId);
                intent.putExtra("guid",theActivity.activityRid);
                theActivity.sendBroadcast(intent);
                break;
            case TaskListAsks.LIST_ITEM_ADD_SUCCESS:
                theActivity.waitDialog.hide();
                EditText mTaskListModel= TaskListPrase.praseListItemAdd((NetObject) msg.obj,theActivity);
                if(mTaskListModel != null)
                {
                    mTaskListModel.setText("");
                }
                TaskListAsks.getList(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask);
                break;
            case TaskListAsks.LIST_ITEM_FINISH_SUCCESS:
                theActivity.waitDialog.hide();
                if(TaskListPrase.praseListfinish((NetObject) msg.obj,theActivity))
                {

                    intent = new Intent(TaskListAsks.ACTION_TASK_LIST_UPDATA);
                    intent.putExtra("taskid",theActivity.mTask.taskId);
                    intent.putExtra("guid",theActivity.activityRid);
                    theActivity.sendBroadcast(intent);
                    TaskListAsks.getList(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask);
                }
                break;
            case TaskDetialActivity.EVENT_SET_LIST_ITEM_UNFINISH:
                theActivity.waitDialog.hide();
                if(TaskListPrase.praseListfinish((NetObject) msg.obj,theActivity))
                TaskListAsks.getList(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask);
                break;
            case TASK_DETIAL_UPDATA:
                theActivity.mTaskDetialPresenter.updataDetial((Intent) msg.obj);
                break;
            case PROJECT_DETIAL_UPDATA:
                theActivity.mTaskDetialPresenter.updataProjectDetial((Intent) msg.obj);
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
                    if(tid.equals(theActivity.mTask.taskId) && cid.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId) && !cid.equals(theActivity.mTask.userId))
                    {
                        theActivity.finish();
                    }
                }
                break;
            case TASK_DELETE:
                theActivity.mTaskDetialPresenter.praseDetlete((Intent) msg.obj);
                break;
            case UPDATA_NAME:
                theActivity.mTaskDetialPresenter.praseProjectView((Intent) msg.obj);
                break;
            case PROJECT_DELETE:
                theActivity.mTaskDetialPresenter.praseDetlete((Intent) msg.obj);
                break;
            case UPDATA_REPLY:
                intent = (Intent) msg.obj;
                String id1 = intent.getStringExtra("taskid");
                String guid = "";
                if(intent.hasExtra("guid"))
                {
                    guid = intent.getStringExtra("guid");
                }
                if(theActivity.mTask.taskId.equals(id1) && !guid.equals(theActivity.activityRid))
                {
                    theActivity.replyPage = 1;
                    theActivity.isreplyall = false;
                    theActivity.mReplys.clear();
                    theActivity.mAnswerlayer.removeAllViews();
                    TaskReplyAsks.getReplays(theActivity,theActivity.mTaskDetialPresenter.mTaskDetialHandler,theActivity.mTask,theActivity.replyPage);
                }
                break;
            case EVENT_UPDATA_SONW:
                theActivity.mSnowView.invalidate();
                break;
            case OaAsks.EVENT_GET_ATTCHMENT_SUCCESS:
                theActivity.waitDialog.hide();
                Attachment attachment =  OaUtils.praseAddtchment((NetObject) msg.obj);
                if(attachment != null)
                Bus.callData(theActivity,"filetools/startAttachment",attachment);
                break;
        }


    }
}
