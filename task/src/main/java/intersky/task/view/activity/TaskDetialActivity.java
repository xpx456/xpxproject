package intersky.task.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.BaseActivity;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.SnowView;
import intersky.mywidget.WebEdit;
import intersky.oa.OaUtils;
import intersky.select.entity.Select;
import intersky.task.TaskManager;
import intersky.task.asks.TaskAsks;
import intersky.task.asks.TaskListAsks;
import intersky.task.asks.TaskReplyAsks;
import intersky.task.entity.Contral;
import intersky.task.entity.Log;
import intersky.task.entity.Project;
import intersky.task.entity.Task;
import intersky.task.entity.TaskFunction;
import intersky.task.entity.TaskList;
import intersky.task.presenter.TaskDetialPresenter;
import intersky.task.view.adapter.ListViewAdapter;
import intersky.task.view.adapter.TaskFunctionAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskDetialActivity extends BaseActivity {

    public static final String ACTION_SET_DES = "ACTION_SET_DES";
    public static final String ACTION_TASK_TAG = "ACTION_TASK_TAG";
    public static final String ACTION_SET_LEADER = "ACTION_SET_LEADER";
    public static final String ACTION_ADD_TASK_PICTURE= "ACTION_ADD_TASK_PICTURE";

    public static final int EVENT_LIST_ITEM_ADD_SUCCESS = 1056;
    public static final int EVENT_SET_LIST_ITEM_UNFINISH = 1066;
    public String activityRid = AppUtils.getguid();
    public int currentView = 0;
    public TaskDetialPresenter mTaskDetialPresenter = new TaskDetialPresenter(this);
    public ArrayList<Select> mTags = new ArrayList<Select>();
    public ArrayList<Attachment> mAttachments = new ArrayList<Attachment>();
    public ArrayList<Attachment> mUploadAttachments = new ArrayList<Attachment>();
    public ArrayList<Log> mLogModels = new ArrayList<Log>();
//    public TaskAttachmentAdapter mAttachmentAdapter;
    public Project mProject = new Project();
    public PopupWindow popupWindow;
    public TextView projectName;
    public TextView taskName;
    public TextView headImg;
    public TextView headTitle;
    public TextView beginTitle;
    public TextView endTitle;
    public TextView sonTitle;
    public WebEdit taskDes;
    public ImageView stare;
    public LinearLayout mHorizontalListView;
    public LinearLayout taksArea;
    public RecyclerView taksListArea;
    public RelativeLayout taglayer;
    public RelativeLayout motherlayer;
    public RelativeLayout mRightTeb;
    public RelativeLayout mMiddeleTeb;
    public RelativeLayout mLefttTeb;
    public RelativeLayout mLine1;
    public RelativeLayout mLine2;
    public RelativeLayout mLine3;
    public RelativeLayout memBer;
    public RelativeLayout shade;
    public MyLinearLayout tagLayer;
    public TextView pcount;
    public TextView mRightImg;
    public TextView mMiddleImg;
    public TextView mLefttImg;
    public RelativeLayout finishLayerTop;
    public TextView finishTitle;
    public LinearLayout addAttachment;
    public Task mTask;
    public Task mPraentTask = new Task();
    public LinearLayout mAnswerlayer;
    public LinearLayout mAttchmentView;
    public LinearLayout mLogsArea;
    public LinearLayout mContralArea;
    public EditText mEditTextContent;
    public ArrayList<View> mViews = new ArrayList<View>();
    public ArrayList<Task> mSonTasks = new  ArrayList<Task>();
    public ArrayList<Task> mSonTasksFinish = new  ArrayList<Task>();
    public ArrayList<TaskList> mList= new  ArrayList<TaskList>();
    public ArrayList<TaskFunction> mFunList1 = new ArrayList<TaskFunction>();
    public ArrayList<TaskFunction> mFunList2 = new ArrayList<TaskFunction>();
    public ArrayList<Contacts> mContactModels = new ArrayList<Contacts>();
    public ArrayList<Reply> mReplys= new ArrayList<Reply>();
    public ArrayList<Contral> mContrals = new ArrayList<Contral>();
    public TaskFunctionAdapter mFuns1;
    public TaskFunctionAdapter mFuns2;
    public boolean hasFinishSon = false;
    public boolean isExpend = false;
    public View finishLayer;
    public AlertDialog dialog;
    public int replyPage = 1;
    public boolean isreplyall = false;
    public int logPage = 1;
    public boolean islogall = false;
    public ListViewAdapter mListViewAdapter;
    public ItemTouchHelper touchHelper;
    public LinearLayoutManager linearLayoutManager;
    public int startPosition;
    public int changePosition;
    public Contral  selectContral;
    public boolean contralEdit = true;
    public SnowView mSnowView;
    public int current = 0;
    public String uploadnames = "";
    public Contral uploadContral;
    public HashMap<String,Task> sonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mTaskDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.showLeft();
        }
    };

    public View.OnClickListener middleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mTaskDetialPresenter.showMiddle();
        }
    };

    public View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.showRight();
        }
    };

    public View.OnClickListener creatListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            mTaskDetialPresenter.doCreat();
        }
    };


    public View.OnClickListener setCheckListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Task mTask = (Task) v.getTag();
            if(mTask.isComplete == 0)
            {
                mTaskDetialPresenter.setCheck(mTask);
            }
            else
            {
                mTaskDetialPresenter.setUnCheck(mTask);
            }
        }
    };

    public View.OnClickListener expendListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.doExpend();
        }
    };

    public View.OnClickListener mMoreListenter = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.showMore();
        }
    };

    public View.OnClickListener finishListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(mTask.isComplete == 0)
            {
                mTaskDetialPresenter.showsetFinish();
            }
            else
            {
                mTaskDetialPresenter.showsetUnFinish();
            }

        }
    };

    public View.OnClickListener finishAllTaskListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            TaskAsks.setFinish(mTaskDetialPresenter.mTaskDetialActivity,mTaskDetialPresenter.mTaskDetialHandler,mTask,1,1);
        }
    };

    public View.OnClickListener finishTaskListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            TaskAsks.setFinish(mTaskDetialPresenter.mTaskDetialActivity,mTaskDetialPresenter.mTaskDetialHandler,mTask,1,1);
        }
    };

    public View.OnClickListener unfinishAllTaskListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.showSetUnfinish(1);
        }
    };

    public View.OnClickListener unfinishTaskListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.showSetUnfinish(0);
        }
    };


    public View.OnClickListener mSelectProject = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.selectProject();
        }
    };

    public View.OnClickListener mUpdtatName = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.updtatName();
        }
    };

    public View.OnClickListener mSetLeader = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.updataLeader();
        }
    };

    public View.OnClickListener mSetBeginTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.setBeginTime();
        }
    };

    public View.OnClickListener mSetEndTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.setEndTime();
        }
    };

    public View.OnClickListener mSetMembers = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.setMembers();
        }
    };

    public View.OnClickListener setTaglayer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskDetialPresenter.setTags();
        }

    };

    public View.OnClickListener mDeleteReplyListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskDetialPresenter.showDeleteReport((Reply) v.getTag());
        }

    };

    public TextView.OnEditorActionListener sendtext = new TextView.OnEditorActionListener() {


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if(mEditTextContent.getText().toString().length() > 0)
                {
                    mTaskDetialPresenter.mTaskDetialActivity.waitDialog.show();
                    TaskReplyAsks.mSendReply(mTaskDetialPresenter.mTaskDetialActivity,mTaskDetialPresenter.mTaskDetialHandler,mTask,mEditTextContent.getText().toString());
                }

            }

            return true;
        }


    };

    public TextView.OnEditorActionListener mOnQuictClick = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.getAction() == KeyEvent.ACTION_UP)
            {
                EditText view = (EditText) v;
                mTaskDetialPresenter.doQudicCreat(view.getText().toString());

            }
            return true;
        }
    };

    public TextView.OnEditorActionListener mOnQuictListCreatClick = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.getAction() == KeyEvent.ACTION_UP)
            {
                EditText view = (EditText) v;
                mTaskDetialPresenter.doListItemQudicCreat((TaskList) view.getTag(),view.getText().toString(),view);
            }
            return true;
        }
    };

    public View.OnClickListener setListCheckListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            TaskList mlist = (TaskList) v.getTag();
            if(mlist.isComplete == 0)
            {
                mTaskDetialPresenter.setListCheck(mlist);
            }
            else
            {
                mTaskDetialPresenter.setUnListCheck(mlist);
            }
        }
    };

    public View.OnClickListener listMore = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            TaskList list = (TaskList) v.getTag();
            mTaskDetialPresenter.doListMore(list);
        }
    };

    public View.OnClickListener listItemMore = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            TaskList list = (TaskList) v.getTag();
            mTaskDetialPresenter.doListItemMore(list);
        }
    };

    public View.OnClickListener listExpend = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            mTaskDetialPresenter.doListExpend((TaskList) v.getTag());
        }
    };

    public TextView.OnEditorActionListener mOnListQuictClick = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.getAction() == KeyEvent.ACTION_UP)
            {
                EditText view = (EditText) v;
                TaskListAsks.changeListName(mTaskDetialPresenter.mTaskDetialActivity,mTaskDetialPresenter.mTaskDetialHandler,(TaskList) view.getTag(),view.getText().toString());

            }
            return true;
        }
    };

    public TextView.OnEditorActionListener mOnListItemQuictClick = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && event.getAction() == KeyEvent.ACTION_UP)
            {
                EditText view = (EditText) v;
                TaskListAsks.changeListItemName(mTaskDetialPresenter.mTaskDetialActivity,mTaskDetialPresenter.mTaskDetialHandler,(TaskList) view.getTag(),view.getText().toString());

            }
            return true;
        }
    };

    public View.OnClickListener doTextEdit = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(contralEdit)
            mTaskDetialPresenter.doTextEdit((Contral) v.getTag());
        }
    };

    public View.OnClickListener doDateEditTime = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(contralEdit)
            mTaskDetialPresenter.doDateEditTime((Contral) v.getTag());
        }
    };

    public View.OnClickListener doDateEdit = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(contralEdit)
            mTaskDetialPresenter.doDateEdit((Contral) v.getTag());
        }
    };

    public View.OnClickListener doSelectMoreEdit = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(contralEdit)
            mTaskDetialPresenter.contralSelectMore((Contral) v.getTag());
        }
    };

    public View.OnClickListener doSelectEdit = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(contralEdit)
            mTaskDetialPresenter.contralSelect((Contral) v.getTag());
        }
    };

    public View.OnClickListener doListSelectEdit = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(contralEdit)
            mTaskDetialPresenter.getRegin((Contral) v.getTag());
        }
    };

    public View.OnClickListener mTaskDetialListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskDetialPresenter.TaskDetial((Task) v.getTag());
        }
    };

    public View.OnClickListener mCancleListenter2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskDetialPresenter.dodismiss2();
        }

    };

    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            selectContral = (Contral) v.getTag();
            mTaskDetialPresenter.takePhoto(selectContral);
        }

    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskDetialPresenter.addPic((Contral) v.getTag());
        }
    };

    public View.OnClickListener mTakePhotoListenter2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskDetialPresenter.takePhoto(null);
        }

    };

    public View.OnClickListener mAddPicListener2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskDetialPresenter.addPic(null);
        }
    };

    public View.OnClickListener mSelectshowPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            mTaskDetialPresenter.showAdd((Contral) v.getTag());
        }
    };

    public View.OnClickListener mSelectshowPicListener2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            mTaskDetialPresenter.showAdd();
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mTaskDetialPresenter.takePhotoResult(requestCode, resultCode, data);
    }

    public View.OnClickListener mDeletePicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskDetialPresenter.delete( (Contral) v.getTag());
        }
    };

    public View.OnClickListener mDeletePicListener2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mTaskDetialPresenter.delete((Attachment) v.getTag());
        }
    };

    public View.OnClickListener mShowPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Attachment attachment = (Attachment) v.getTag();
            if(attachment.mSize > 0)
            Bus.callData(mTaskDetialPresenter.mTaskDetialActivity,"filetools/startAttachment",v.getTag());
            else
            {
                mTaskDetialPresenter.mTaskDetialActivity.waitDialog.show();
                TaskManager.getInstance().oaUtils.getAttachmentSize(attachment,mTaskDetialPresenter.mTaskDetialHandler);
            }

        }
    };

    public View.OnClickListener rankListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(contralEdit)
            mTaskDetialPresenter.setRask(v);
        }
    };

    public View.OnClickListener addListListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            popupWindow.dismiss();
            if(contralEdit)
                mTaskDetialPresenter.addList();
        }
    };

    public View.OnClickListener deleteListListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
                popupWindow.dismiss();
            if(contralEdit)
            {
                TaskListAsks.deleteList(mTaskDetialPresenter.mTaskDetialActivity,mTaskDetialPresenter.mTaskDetialHandler,mTask, (TaskList) v.getTag());
            }

        }
    };

    public View.OnClickListener changeToTaskListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
                popupWindow.dismiss();
            if(contralEdit)
                mTaskDetialPresenter.changeToTask((TaskList) v.getTag());
        }
    };
}
