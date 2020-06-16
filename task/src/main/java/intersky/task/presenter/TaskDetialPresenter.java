package intersky.task.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import intersky.appbase.Actions;
import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.EditDialogListener;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.mywidget.HorizontalListView;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.SnowView;
import intersky.mywidget.WebEdit;
import intersky.oa.OaUtils;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.task.R;
import intersky.task.StageItemTouchHelperCallback;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.RagenAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.asks.TaskAttachmentAsks;
import intersky.task.asks.TaskListAsks;
import intersky.task.asks.TaskLogAsks;
import intersky.task.asks.TaskReplyAsks;
import intersky.task.entity.Contral;
import intersky.task.entity.Log;
import intersky.task.entity.Task;
import intersky.task.entity.TaskFunction;
import intersky.task.entity.TaskList;
import intersky.task.handler.TaskDetialHandler;
import intersky.task.prase.ProjectPrase;
import intersky.task.receiver.TaskDetialReceiver;
import intersky.task.view.activity.TaskCreatActivity;
import intersky.task.view.activity.TaskDetialActivity;
import intersky.task.view.activity.TaskMemberActivity;
import intersky.task.view.activity.TaskStructureActivity;
import intersky.task.view.adapter.ListViewAdapter;
import intersky.task.view.adapter.SelectMoreAdapter;
import intersky.task.view.adapter.TaskFunctionAdapter;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

import static intersky.task.asks.TaskAttachmentAsks.delAttachments;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskDetialPresenter implements Presenter {

    public TaskDetialHandler mTaskDetialHandler;
    public TaskDetialActivity mTaskDetialActivity;

    public TaskDetialPresenter(TaskDetialActivity mTaskDetialActivity) {
        this.mTaskDetialActivity = mTaskDetialActivity;
        this.mTaskDetialHandler = new TaskDetialHandler(mTaskDetialActivity);
        mTaskDetialActivity.setBaseReceiver(new TaskDetialReceiver(mTaskDetialHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mTaskDetialActivity.setContentView(R.layout.activity_task_detial);
        mTaskDetialActivity.mTask = mTaskDetialActivity.getIntent().getParcelableExtra("task");
        if (mTaskDetialActivity.getIntent().hasExtra("project"))
            mTaskDetialActivity.mProject = mTaskDetialActivity.getIntent().getParcelableExtra("project");
        if (mTaskDetialActivity.getIntent().hasExtra("parent"))
            mTaskDetialActivity.mPraentTask = mTaskDetialActivity.getIntent().getParcelableExtra("parent");
        mTaskDetialActivity.mSnowView = (SnowView) mTaskDetialActivity.findViewById(R.id.sv);
        mTaskDetialActivity.mSnowView.handler = mTaskDetialHandler;
        mTaskDetialActivity.mSnowView.event = TaskDetialHandler.EVENT_UPDATA_SONW;
        mTaskDetialActivity.taglayer = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.tagarea);
        mTaskDetialActivity.finishLayerTop = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.finish);
        mTaskDetialActivity.finishTitle = (TextView) mTaskDetialActivity.findViewById(R.id.finish_title);
        mTaskDetialActivity.projectName = (TextView) mTaskDetialActivity.findViewById(R.id.project_title);
        mTaskDetialActivity.taskName = (TextView) mTaskDetialActivity.findViewById(R.id.task_title);
        mTaskDetialActivity.headImg = (TextView) mTaskDetialActivity.findViewById(R.id.contact_img);
        mTaskDetialActivity.headTitle = (TextView) mTaskDetialActivity.findViewById(R.id.conversation_title);
        mTaskDetialActivity.beginTitle = (TextView) mTaskDetialActivity.findViewById(R.id.begin_title);
        mTaskDetialActivity.endTitle = (TextView) mTaskDetialActivity.findViewById(R.id.end_title);
        mTaskDetialActivity.sonTitle = (TextView) mTaskDetialActivity.findViewById(R.id.task_list_title);
        mTaskDetialActivity.mHorizontalListView = (LinearLayout) mTaskDetialActivity.findViewById(R.id.horizon_listview);
        mTaskDetialActivity.taksArea = (LinearLayout) mTaskDetialActivity.findViewById(R.id.task_list_content);
        mTaskDetialActivity.motherlayer = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.otherlayer);
        mTaskDetialActivity.mLefttTeb = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.day);
        mTaskDetialActivity.mMiddeleTeb = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.week);
        mTaskDetialActivity.mRightTeb = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.month);
        mTaskDetialActivity.mLine1 = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.line13);
        mTaskDetialActivity.mLine2 = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.line23);
        mTaskDetialActivity.mLine3 = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.line33);
        mTaskDetialActivity.pcount = (TextView) mTaskDetialActivity.findViewById(R.id.p_count);
        mTaskDetialActivity.mLefttImg = (TextView) mTaskDetialActivity.findViewById(R.id.daytxt);
        mTaskDetialActivity.mMiddleImg = (TextView) mTaskDetialActivity.findViewById(R.id.weektxt);
        mTaskDetialActivity.mRightImg = (TextView) mTaskDetialActivity.findViewById(R.id.monthtxt);
        mTaskDetialActivity.taskDes = (WebEdit) mTaskDetialActivity.findViewById(R.id.des_title);
        mTaskDetialActivity.taskDes.setHit("#000000", mTaskDetialActivity.getString(R.string.task_detial_des_hit));
        mTaskDetialActivity.taskDes.setAction(TaskDetialActivity.ACTION_SET_DES);

        mTaskDetialActivity.taksListArea = (RecyclerView) mTaskDetialActivity.findViewById(R.id.list_area);
        mTaskDetialActivity.memBer = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.member);
        mTaskDetialActivity.mEditTextContent = (EditText) mTaskDetialActivity.findViewById(R.id.et_sendmessage);
        mTaskDetialActivity.shade = (RelativeLayout) mTaskDetialActivity.findViewById(R.id.shade);
        mTaskDetialActivity.stare = (ImageView) mTaskDetialActivity.findViewById(R.id.stare);
        mTaskDetialActivity.tagLayer = (MyLinearLayout) mTaskDetialActivity.findViewById(R.id.tag_layer);
        mTaskDetialActivity.mContralArea = (LinearLayout) mTaskDetialActivity.findViewById(R.id.contral_list);
        mTaskDetialActivity.mEditTextContent.setOnEditorActionListener(mTaskDetialActivity.sendtext);
        mTaskDetialActivity.mLefttTeb.setOnClickListener(mTaskDetialActivity.leftClickListener);
        mTaskDetialActivity.mMiddeleTeb.setOnClickListener(mTaskDetialActivity.middleClickListener);
        mTaskDetialActivity.mRightTeb.setOnClickListener(mTaskDetialActivity.rightClickListener);
        mTaskDetialActivity.projectName.setOnClickListener(mTaskDetialActivity.mSelectProject);
        mTaskDetialActivity.projectName.setClickable(false);
        mTaskDetialActivity.headTitle.setOnClickListener(mTaskDetialActivity.mSetLeader);
        mTaskDetialActivity.headImg.setOnClickListener(mTaskDetialActivity.mSetLeader);
        mTaskDetialActivity.taskName.setOnClickListener(mTaskDetialActivity.mUpdtatName);
        mTaskDetialActivity.beginTitle.setOnClickListener(mTaskDetialActivity.mSetBeginTime);
        mTaskDetialActivity.endTitle.setOnClickListener(mTaskDetialActivity.mSetEndTime);
        mTaskDetialActivity.memBer.setOnClickListener(mTaskDetialActivity.mSetMembers);
        mTaskDetialActivity.mHorizontalListView.setOnClickListener(mTaskDetialActivity.mSetMembers);
        mTaskDetialActivity.tagLayer.setOnClickListener(mTaskDetialActivity.setTaglayer);
        mTaskDetialActivity.sonTitle.setText(mTaskDetialActivity.getString(R.string.task_detial_list_task) + ":" + "0/0");
        mTaskDetialActivity.mTask = mTaskDetialActivity.getIntent().getParcelableExtra("task");
        View mView1 = null;
        View mView2 = null;
        View mView3 = null;
        mView1 = mTaskDetialActivity.findViewById(R.id.answer);
        mTaskDetialActivity.mAnswerlayer = (LinearLayout) mView1.findViewById(R.id.answeritem);
        mView2 = mTaskDetialActivity.findViewById(R.id.attahmentlayer);
        mTaskDetialActivity.mAttchmentView = (LinearLayout) mView2.findViewById(R.id.attachment_listview);
        mTaskDetialActivity.addAttachment = (LinearLayout) mView2.findViewById(R.id.attachmentadd);
        mTaskDetialActivity.addAttachment.setOnClickListener(mTaskDetialActivity.mSelectshowPicListener2);
        mView3 = mTaskDetialActivity.findViewById(R.id.loglayer);
        mTaskDetialActivity.mLogsArea = (LinearLayout) mView3.findViewById(R.id.log_list);
        mTaskDetialActivity.mViews.add(mView1);
        mTaskDetialActivity.mViews.add(mView2);
        mTaskDetialActivity.mViews.add(mView3);

        mTaskDetialActivity.mListViewAdapter = new ListViewAdapter(mTaskDetialActivity, mTaskDetialActivity.mList, mTaskDetialHandler);

        mTaskDetialActivity.linearLayoutManager = new LinearLayoutManager(mTaskDetialActivity);
        mTaskDetialActivity.linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTaskDetialActivity.taksListArea.setLayoutManager(mTaskDetialActivity.linearLayoutManager);
        mTaskDetialActivity.taksListArea.setAdapter(mTaskDetialActivity.mListViewAdapter);

        ItemTouchHelper.Callback callback =
                new StageItemTouchHelperCallback(mTaskDetialActivity.mListViewAdapter);
        mTaskDetialActivity.touchHelper = new ItemTouchHelper(callback);
        mTaskDetialActivity.touchHelper.attachToRecyclerView(mTaskDetialActivity.taksListArea);

        mTaskDetialActivity.mListViewAdapter.setOnItemLongClickListener(new ListViewAdapter.OnItemLongClickListener() {

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder view, int position) {
                if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                        || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                    TaskList listModel = mTaskDetialActivity.mList.get(position);
                    if (listModel.type == TaskList.LIST_TYPE_ITEM) {
                        mTaskDetialActivity.touchHelper.startDrag(view);
                    }
                }

            }

        });
        mTaskDetialActivity.mListViewAdapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        if (mTaskDetialActivity.mTask.isread) {
            updataBaseView();
            getAll();
        } else {
            TaskAsks.getTaskDetial(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
        }
        initData();
        initTags();
        showLeft();
    }

    public void updataBaseView() {
        ToolBarHelper.setTitle(mTaskDetialActivity.mActionBar, mTaskDetialActivity.mTask.taskName);
        mTaskDetialActivity.mContactModels.clear();
        Bus.callData(mTaskDetialActivity, "chat/getContacts", mTaskDetialActivity.mTask.senderIdList, mTaskDetialActivity.mContactModels);
        ArrayList<Contacts> contacts = new ArrayList<Contacts>();
        contacts.add((Contacts) Bus.callData(mTaskDetialActivity, "chat/getContactItem", mTaskDetialActivity.mTask.leaderId));
        contacts.addAll(mTaskDetialActivity.mContactModels);
        updataContactViews(contacts);
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            View view = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.task_son_item_add, null);
            EditText editText = (EditText) view.findViewById(R.id.title);
            editText.setOnEditorActionListener(mTaskDetialActivity.mOnQuictClick);
            mTaskDetialActivity.taksArea.addView(view);
        }
        if (mTaskDetialActivity.mTask.isStar == 1) {
            mTaskDetialActivity.stare.setVisibility(View.VISIBLE);
        } else {
            mTaskDetialActivity.stare.setVisibility(View.INVISIBLE);
        }

        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            mTaskDetialActivity.contralEdit = true;
            ToolBarHelper.setRightBtn(mTaskDetialActivity.mActionBar, mTaskDetialActivity.finishListener, mTaskDetialActivity.mMoreListenter, R.drawable.ntask_completew, " · · ·");
        } else {
            mTaskDetialActivity.contralEdit = false;
            if (mTaskDetialActivity.mTask.isComplete == 0)
                ToolBarHelper.setRightBtnText(mTaskDetialActivity.mActionBar, mTaskDetialActivity.mMoreListenter, " · · ·");
            else
                ToolBarHelper.setRightBtnText(mTaskDetialActivity.mActionBar, mTaskDetialActivity.mMoreListenter, " · · ·");
        }
        if (mTaskDetialActivity.mTask.isComplete == 0) {
            mTaskDetialActivity.finishLayerTop.setVisibility(View.GONE);
        } else {
            mTaskDetialActivity.finishLayerTop.setVisibility(View.VISIBLE);
            if (mTaskDetialActivity.mTask.endTime.length() > 0) {
                if (TimeUtils.hoursBetween(mTaskDetialActivity.mTask.completeTime, mTaskDetialActivity.mTask.endTime) < 0) {
                    int day = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTaskDetialActivity.mTask.endTime)) / 24;
                    int hour = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTaskDetialActivity.mTask.endTime)) % 24;
                    String text;
                    if (day == 0) {
                        text = String.valueOf(hour) + "小时";
                    } else if (hour == 0) {
                        text = String.valueOf(day) + "天";
                    } else {
                        text = String.valueOf(day) + "天" + String.valueOf(hour) + "小时";
                    }
                    mTaskDetialActivity.finishTitle.setText("超时" + text + mTaskDetialActivity.getString(R.string.button_word_finish) +
                            " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                    mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(249, 183, 60));
                } else {
                    mTaskDetialActivity.finishTitle.setText(mTaskDetialActivity.getString(R.string.task_finish_nomal) +
                            " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                    mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(76, 175, 80));
                }
            } else {
                mTaskDetialActivity.finishTitle.setText(mTaskDetialActivity.getString(R.string.task_finish_nomal) +
                        " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(76, 175, 80));
            }
        }

        if (mTaskDetialActivity.mTask.projectId.length() != 0) {

        }
        mTaskDetialActivity.taskName.setText(mTaskDetialActivity.mTask.taskName);
        Contacts taskPerson = (Contacts) Bus.callData(mTaskDetialActivity, "chat/getContactItem", mTaskDetialActivity.mTask.leaderId);
        AppUtils.setContactCycleHead(mTaskDetialActivity.headImg, taskPerson.getName(), taskPerson.colorhead);
        mTaskDetialActivity.headTitle.setText(taskPerson.getName());
        if (mTaskDetialActivity.mTask.beginTime.length() > 0)
            mTaskDetialActivity.beginTitle.setText(mTaskDetialActivity.getString(R.string.task_detial_date_begin)
                    + mTaskDetialActivity.mTask.beginTime.substring(0, 16));
        if (mTaskDetialActivity.mTask.endTime.length() > 0)
            mTaskDetialActivity.endTitle.setText(mTaskDetialActivity.getString(R.string.task_detial_date_end)
                    + mTaskDetialActivity.mTask.endTime.substring(0, 16));
        mTaskDetialActivity.taskDes.setText(mTaskDetialActivity.mTask.des);
        if (mTaskDetialActivity.mTask.des.length() == 0) {
            mTaskDetialActivity.taskDes.setText(mTaskDetialActivity.getString(R.string.task_detial_des_hit));
        }
    }

    public void getAll() {
        mTaskDetialActivity.mListViewAdapter.notifyDataSetChanged();
        TaskListAsks.getList(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
        mTaskDetialActivity.taksArea.removeAllViews();
        if (mTaskDetialActivity.mTask.projectId.equals("0")) {
            mTaskDetialActivity.projectName.setText(mTaskDetialActivity.getString(R.string.task_detial_project_none));
        } else {
            if (!mTaskDetialActivity.mProject.projectId.equals(mTaskDetialActivity.mTask.projectId)) {
                mTaskDetialActivity.mProject.projectId = mTaskDetialActivity.mTask.projectId;
                ProjectAsks.getProjectItemDetial(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mProject);
            }
        }
        mTaskDetialActivity.projectName.setClickable(true);

        if (!mTaskDetialActivity.mTask.parentId.equals("0")) {

            if (!mTaskDetialActivity.mPraentTask.taskId.equals(mTaskDetialActivity.mTask.parentId)) {
                mTaskDetialActivity.mPraentTask.taskId = mTaskDetialActivity.mTask.parentId;
                TaskAsks.getTaskDetial(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mPraentTask);
            }
        }
        TaskAsks.getSon(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
        mTaskDetialActivity.mContrals.clear();
        mTaskDetialActivity.mContralArea.removeAllViews();
        TaskAsks.getContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
        mTaskDetialActivity.replyPage = 1;
        mTaskDetialActivity.isreplyall = false;
        mTaskDetialActivity.mReplys.clear();
        TaskReplyAsks.getReplays(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mTaskDetialActivity.replyPage);
        mTaskDetialActivity.logPage = 1;
        mTaskDetialActivity.islogall = false;
        mTaskDetialActivity.mLogModels.clear();
        TaskLogAsks.getLogs(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mTaskDetialActivity.logPage);
        updataTaskAttchment();
    }

    public void updataTaskAttchment() {
        mTaskDetialActivity.mAttachments.clear();
        TaskAttachmentAsks.getAttachments(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
    }


    public void initTags() {
        mTaskDetialActivity.mTags.clear();
        mTaskDetialActivity.tagLayer.removeAllViews();
        mTaskDetialActivity.mTags.add(new Select("1", mTaskDetialActivity.getString(R.string.task_tag_pup)));
        mTaskDetialActivity.mTags.add(new Select("2", mTaskDetialActivity.getString(R.string.task_tag_blu)));
        mTaskDetialActivity.mTags.add(new Select("3", mTaskDetialActivity.getString(R.string.task_tag_yel)));
        mTaskDetialActivity.mTags.add(new Select("4", mTaskDetialActivity.getString(R.string.task_tag_org)));
        mTaskDetialActivity.mTags.add(new Select("5", mTaskDetialActivity.getString(R.string.task_tag_red)));
        if (mTaskDetialActivity.mTask.tag.equals("null")) {
            mTaskDetialActivity.mTask.tag = "";
        }

        if (mTaskDetialActivity.mTask.tag.length() > 0) {
            String tag[] = mTaskDetialActivity.mTask.tag.split(",");
            for (int i = 0; i < tag.length; i++) {
                mTaskDetialActivity.mTags.get(Integer.valueOf(tag[i]) - 1).iselect = true;
                addTagView(mTaskDetialActivity.mTags.get(Integer.valueOf(tag[i]) - 1));
            }
        }
        if (mTaskDetialActivity.mTask.tag.length() == 0)
            mTaskDetialActivity.taglayer.setVisibility(View.GONE);
        else
            mTaskDetialActivity.taglayer.setVisibility(View.VISIBLE);
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

//        mTaskDetialActivity.unregisterReceiver(mTaskDetialReceiver);
        mTaskDetialActivity.taskDes.destroy();
    }


    public void doListMore(TaskList item) {
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {

            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem item1 = new MenuItem();
            item1.item = item;
            item1.btnName = mTaskDetialActivity.getString(R.string.task_detial_list_add);
            item1.mListener = mTaskDetialActivity.addListListener;
            items.add(item1);
            item1 = new MenuItem();
            item1.item = item;
            item1.btnName = mTaskDetialActivity.getString(R.string.task_detial_list_delete);
            item1.mListener = mTaskDetialActivity.deleteListListener;
            items.add(item1);
            mTaskDetialActivity.popupWindow = AppUtils.creatButtomMenu(mTaskDetialActivity, mTaskDetialActivity.shade, items, mTaskDetialActivity.findViewById(R.id.taskdetial));

        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }


    public void doListItemMore(TaskList item) {
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {

            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem item1 = new MenuItem();
            item1.item = item;
            item1.btnName = mTaskDetialActivity.getString(R.string.task_detial_list_tiem_to_task);
            item1.mListener = mTaskDetialActivity.changeToTaskListener;
            items.add(item1);
            item1 = new MenuItem();
            item1.item = item;
            item1.btnName = mTaskDetialActivity.getString(R.string.task_detial_list_delete);
            item1.mListener = mTaskDetialActivity.deleteListListener;
            items.add(item1);
            mTaskDetialActivity.popupWindow = AppUtils.creatButtomMenu(mTaskDetialActivity, mTaskDetialActivity.shade, items, mTaskDetialActivity.findViewById(R.id.taskdetial));

        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public void addList() {
        AppUtils.creatDialogTowButtonEdit(mTaskDetialActivity, "", mTaskDetialActivity.getString(R.string.task_detial_list_new_name), mTaskDetialActivity.getString(R.string.button_word_cancle)
                , mTaskDetialActivity.getString(R.string.button_word_ok), null, new AddListListener(), mTaskDetialActivity.getString(R.string.task_detial_list_new_name));

    }

    public class AddListListener extends EditDialogListener {


        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                TaskListAsks.addList(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, editText.getText().toString());
            } else {
                AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_detial_list_new_name));
            }
        }
    }


    public void changeToTask(TaskList listitem) {
        TaskListAsks.deleteListItem(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, listitem);
        Task task = new Task();
        task.taskName = listitem.name;
        task.leaderId = TaskManager.getInstance().oaUtils.mAccount.mRecordId;
        task.projectId = mTaskDetialActivity.mTask.projectId;
        task.stageId = mTaskDetialActivity.mTask.stageId;
        task.parentId = mTaskDetialActivity.mTask.taskId;
        TaskAsks.addTask(mTaskDetialActivity, mTaskDetialHandler, task);
    }


    public void doQudicCreat(String name) {

        if (name.length() > 0) {
            mTaskDetialActivity.waitDialog.show();
            Task task = new Task();
            task.taskName = name;
            task.leaderId = TaskManager.getInstance().oaUtils.mAccount.mRecordId;
            task.projectId = mTaskDetialActivity.mTask.projectId;
            task.stageId = mTaskDetialActivity.mTask.stageId;
            task.parentId = mTaskDetialActivity.mTask.taskId;
            TaskAsks.addTask(mTaskDetialActivity, mTaskDetialHandler, task);
        }

    }

    public void doListQudicCreat(String name) {

        if (name.length() > 0)
            mTaskDetialActivity.waitDialog.show();
        TaskListAsks.addList(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, name);
    }

    public void doListItemQudicCreat(TaskList list, String name, EditText editText) {

        if (name.length() > 0) {
            TaskListAsks.addListItem(mTaskDetialActivity, mTaskDetialHandler, list, name, editText);
        }

    }

    public void doCreat() {

        Intent intent = new Intent(mTaskDetialActivity, TaskCreatActivity.class);
        Task task = new Task();
        task.leaderId = TaskManager.getInstance().oaUtils.mAccount.mRecordId;
        intent.putExtra("task", task);
        intent.putExtra("parentask", mTaskDetialActivity.mTask);
        intent.putExtra("project", mTaskDetialActivity.mProject);
        mTaskDetialActivity.startActivity(intent);
    }


    public void initData() {
        mTaskDetialActivity.mFunList1.clear();
        mTaskDetialActivity.mFunList2.clear();
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            TaskFunction mFun = new TaskFunction();
            mFun.name = mTaskDetialActivity.getString(R.string.task_detial_more_addtag);
            mFun.name2 = mTaskDetialActivity.getString(R.string.task_detial_more_addtag);
            mFun.id = R.drawable.ntask_tagmenu;
            mFun.id2 = R.drawable.ntask_tagmenu;
            mTaskDetialActivity.mFunList1.add(mFun);
            mFun = new TaskFunction();
            mFun.name = mTaskDetialActivity.getString(R.string.task_detial_more_addsontask);
            mFun.name2 = mTaskDetialActivity.getString(R.string.task_detial_more_addsontask);
            mFun.id = R.drawable.ntask_successmenu;
            mFun.id2 = R.drawable.ntask_successmenu;
            mTaskDetialActivity.mFunList1.add(mFun);
            mFun = new TaskFunction();
            mFun.name = mTaskDetialActivity.getString(R.string.task_detial_more_addlist);
            mFun.name2 = mTaskDetialActivity.getString(R.string.task_detial_more_addlist);
            mFun.id = R.drawable.ntask_listmenu;
            mFun.id2 = R.drawable.ntask_listmenu;
            mTaskDetialActivity.mFunList1.add(mFun);
            mTaskDetialActivity.mFuns1 = new TaskFunctionAdapter(mTaskDetialActivity, mTaskDetialActivity.mFunList1);

            mFun = new TaskFunction();
            mFun.name = mTaskDetialActivity.getString(R.string.task_detial_more_addstar);
            mFun.name2 = mTaskDetialActivity.getString(R.string.task_detial_more_deletestar);
            mFun.id = R.drawable.ntask_star;
            mFun.id2 = R.drawable.ntask_starred;
            if (mTaskDetialActivity.mTask.isStar == 0) {
                mFun.onoff = false;
            } else {
                mFun.onoff = true;
            }
            mTaskDetialActivity.mFunList2.add(mFun);
            if (mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                    || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                mFun = new TaskFunction();
                mFun.name = mTaskDetialActivity.getString(R.string.task_detial_more_lock);
                mFun.name2 = mTaskDetialActivity.getString(R.string.task_detial_more_unlock);
                mFun.id = R.drawable.ntask_lockred;
                mFun.id2 = R.drawable.ntask_lock;
                if (mTaskDetialActivity.mTask.isLocked == 1) {
                    mFun.onoff = true;
                } else {
                    mFun.onoff = false;
                }

                mTaskDetialActivity.mFunList2.add(mFun);
            }

            mFun = new TaskFunction();
            mFun.name = mTaskDetialActivity.getString(R.string.task_detial_more_delete);
            mFun.name2 = mTaskDetialActivity.getString(R.string.task_detial_more_delete);
            mFun.id = R.drawable.ntask_deletered;
            mFun.id2 = R.drawable.ntask_deletered;
            mTaskDetialActivity.mFunList2.add(mFun);
            mTaskDetialActivity.mFuns2 = new TaskFunctionAdapter(mTaskDetialActivity, mTaskDetialActivity.mFunList2);
        } else {
            TaskFunction mFun = new TaskFunction();
            mFun.name = mTaskDetialActivity.getString(R.string.task_detial_more_addtag);
            mFun.name2 = mTaskDetialActivity.getString(R.string.task_detial_more_addtag);
            mFun.id = R.drawable.ntask_tagmenu;
            mFun.id2 = R.drawable.ntask_tagmenu;
            mTaskDetialActivity.mFunList1.add(mFun);
            mFun = new TaskFunction();
            mFun.name = mTaskDetialActivity.getString(R.string.task_detial_more_exist);
            mFun.name2 = mTaskDetialActivity.getString(R.string.task_detial_more_exist);
            mFun.id = R.drawable.exist_task;
            mFun.id2 = R.drawable.exist_task;
            mTaskDetialActivity.mFunList2.add(mFun);
            mTaskDetialActivity.mFuns2 = new TaskFunctionAdapter(mTaskDetialActivity, mTaskDetialActivity.mFunList2);
        }
    }


    public void sendtoTask(String json) {

        if (mTaskDetialActivity.uploadContral == null) {
//            if (mTaskDetialActivity.mTask.attachmentName.length() == 0) {
//                mTaskDetialActivity.mTask.attachmentName = mTaskDetialActivity.uploadnames;
//            } else {
//                mTaskDetialActivity.mTask.attachmentName += "," + mTaskDetialActivity.uploadnames;
//            }
//            if (mTaskDetialActivity.mTask.attachmentHash.length() == 0) {
//                mTaskDetialActivity.mTask.attachmentHash = json;
//            } else {
//                mTaskDetialActivity.mTask.attachmentHash += "," + json;
//            }
            TaskAttachmentAsks.sentTaskAttachment(mTaskDetialActivity, mTaskDetialHandler
                    , mTaskDetialActivity.mTask, mTaskDetialActivity.uploadnames, json, "task");
        } else {
            mTaskDetialActivity.uploadContral.testvalue = json;
//            if (mTaskDetialActivity.uploadContral.testvalue2.length() == 0) {
//                mTaskDetialActivity.uploadContral.testvalue2 = mTaskDetialActivity.uploadnames;
//            } else {
//                mTaskDetialActivity.uploadContral.testvalue2 += "," + mTaskDetialActivity.uploadnames;
//            }
            TaskAttachmentAsks.sentTaskAttachment(mTaskDetialActivity, mTaskDetialHandler
                    , mTaskDetialActivity.mTask, mTaskDetialActivity.uploadnames, mTaskDetialActivity.uploadContral.testvalue, "attence");
        }

    }

    public void getRegin(Contral contralModel) {
        String id = "0";
        if (contralModel.postItems.size() == 0) {
            id = "0";
        } else {
            id = contralModel.postItems.get(contralModel.postItems.size() - 1).mId;
        }
        RagenAsks.setRagen(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.uploadContral, id);
    }


    public void setContralsAttachment() {
        TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mTaskDetialActivity.uploadContral);
    }


    public void setTag() {
        TaskAsks.setTag(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, SelectManager.getInstance().mSelects);
        mTaskDetialActivity.waitDialog.show();
    }


    public void praseSonView() {
        mTaskDetialActivity.mSonTasks.clear();
        mTaskDetialActivity.mSonTasksFinish.clear();
        mTaskDetialActivity.hasFinishSon = false;
        mTaskDetialActivity.taksArea.removeAllViews();

        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            View view = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.task_son_item_add, null);
            EditText editText = (EditText) view.findViewById(R.id.title);
            editText.setOnEditorActionListener(mTaskDetialActivity.mOnQuictClick);
            mTaskDetialActivity.taksArea.addView(view);
        }

        int finishcount = 0;
        int count = 0;

        for (int i = 0; i < mTaskDetialActivity.mTask.tasks.size(); i++) {
            Task mTaskItemModel = mTaskDetialActivity.mTask.tasks.get(i);
            if (mTaskItemModel.isComplete == 0) {
                mTaskDetialActivity.mSonTasks.add(mTaskItemModel);
                finishcount++;
                addSonView(mTaskItemModel);
            } else {
                if (mTaskDetialActivity.hasFinishSon == false) {
                    mTaskDetialActivity.hasFinishSon = true;
                    mTaskDetialActivity.finishLayer = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.task_son_finish, null);
                    mTaskDetialActivity.finishLayer.setOnClickListener(mTaskDetialActivity.expendListener);
                    mTaskDetialActivity.taksArea.addView(mTaskDetialActivity.finishLayer);

                }
                mTaskDetialActivity.mSonTasksFinish.add(mTaskItemModel);
                if (mTaskDetialActivity.isExpend) {
                    addSonView(mTaskItemModel);
                }
            }
            count++;
        }

        if (finishcount != mTaskDetialActivity.mTask.taskfinish || count != mTaskDetialActivity.mTask.taskcount) {
            mTaskDetialActivity.mTask.taskfinish = finishcount;
            mTaskDetialActivity.mTask.taskcount = count;
        }

        mTaskDetialActivity.sonTitle.setText(mTaskDetialActivity.getString(R.string.task_detial_list_task) + ":"
                + String.valueOf(mTaskDetialActivity.mSonTasksFinish.size()) + "/" + String.valueOf(mTaskDetialActivity.mSonTasks.size() +
                mTaskDetialActivity.mSonTasksFinish.size()));
    }


    public void showLeft() {

        mTaskDetialActivity.current = 0;
        mTaskDetialActivity.mViews.get(0).setVisibility(View.VISIBLE);
        mTaskDetialActivity.mViews.get(1).setVisibility(View.GONE);
        mTaskDetialActivity.mViews.get(2).setVisibility(View.GONE);
        mTaskDetialActivity.mLine1.setVisibility(View.VISIBLE);
        mTaskDetialActivity.mLine2.setVisibility(View.INVISIBLE);
        mTaskDetialActivity.mLine3.setVisibility(View.INVISIBLE);
        mTaskDetialActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mTaskDetialActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mTaskDetialActivity.mLefttImg.setTextColor(Color.rgb(98, 153, 243));

    }

    public void showMiddle() {
        mTaskDetialActivity.current = 1;
        mTaskDetialActivity.mViews.get(0).setVisibility(View.GONE);
        mTaskDetialActivity.mViews.get(1).setVisibility(View.VISIBLE);
        mTaskDetialActivity.mViews.get(2).setVisibility(View.GONE);
        mTaskDetialActivity.mLine1.setVisibility(View.INVISIBLE);
        mTaskDetialActivity.mLine2.setVisibility(View.VISIBLE);
        mTaskDetialActivity.mLine3.setVisibility(View.INVISIBLE);
        mTaskDetialActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mTaskDetialActivity.mMiddleImg.setTextColor(Color.rgb(98, 153, 243));
        mTaskDetialActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));
        initAttachment();
    }

    public void showRight() {
        mTaskDetialActivity.current = 2;
        mTaskDetialActivity.mViews.get(0).setVisibility(View.GONE);
        mTaskDetialActivity.mViews.get(1).setVisibility(View.GONE);
        mTaskDetialActivity.mViews.get(2).setVisibility(View.VISIBLE);
        mTaskDetialActivity.mLine1.setVisibility(View.INVISIBLE);
        mTaskDetialActivity.mLine2.setVisibility(View.INVISIBLE);
        mTaskDetialActivity.mLine3.setVisibility(View.VISIBLE);
        mTaskDetialActivity.mRightImg.setTextColor(Color.rgb(98, 153, 243));
        mTaskDetialActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mTaskDetialActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));

        initLogViews();
    }

    public void doListExpend(TaskList mlist) {
        View view = mlist.mView;
        if (view != null) {
            if (mlist.expend == true) {
                mlist.expend = false;
                RelativeLayout progress = (RelativeLayout) view.findViewById(R.id.progress);
                RelativeLayout black = (RelativeLayout) view.findViewById(R.id.black);
                black.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                ImageView imageView = (ImageView) view.findViewById(R.id.contact_img);
                imageView.setImageResource(R.drawable.sniper1);
                mTaskDetialActivity.mList.removeAll(mlist.mLists);
            } else {
                mlist.expend = true;
                RelativeLayout progress = (RelativeLayout) view.findViewById(R.id.progress);
                RelativeLayout black = (RelativeLayout) view.findViewById(R.id.black);
                black.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) view.findViewById(R.id.contact_img);
                imageView.setImageResource(R.drawable.sniper1_s);
                mTaskDetialActivity.mList.addAll(mTaskDetialActivity.mList.indexOf(mlist) + 1, mlist.mLists);
            }
            mTaskDetialActivity.mListViewAdapter.notifyDataSetChanged();
        }

    }


    public void addSonView(Task mTask) {
        View view = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.task_son_item, null);
        view.setTag(mTask);
        view.setOnClickListener(mTaskDetialActivity.mTaskDetialListener);
        TextView name = (TextView) view.findViewById(R.id.title);
        name.setText(mTask.taskName);
        ImageView mCheck = (ImageView) view.findViewById(R.id.task_finish);
        mCheck.setTag(mTask);
        mCheck.setOnClickListener(mTaskDetialActivity.setCheckListener);
        TextView head = (TextView) view.findViewById(R.id.contact_img);
        String s = "";
        Contacts taskPerson = (Contacts) Bus.callData(mTaskDetialActivity, "chat/getContactItem", mTask.leaderId);
        AppUtils.setContactCycleHead(head, taskPerson.getName(), taskPerson.colorhead);
        if (mTask.isComplete == 0) {
            mCheck.setImageResource(R.drawable.ntask_select);
            mTaskDetialActivity.taksArea.addView(view, mTaskDetialActivity.mSonTasks.size() - 1);
        }
        if (mTask.isComplete == 1) {
            name.setTextColor(Color.rgb(140, 140, 140));
            mCheck.setImageResource(R.drawable.ntask_selected);
            name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mTaskDetialActivity.taksArea.addView(view);
        }
    }


    public void setCheck(Task mTask) {
        if (mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId) || mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            TaskAsks.setFinish(mTaskDetialActivity, mTaskDetialHandler, mTask, 1, 0);
        } else if (mTask.senderIdList.contains(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            if (mTask.isLocked == 0) {
                TaskAsks.setFinish(mTaskDetialActivity, mTaskDetialHandler, mTask, 1, 0);
            } else {
                AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
            }
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }

    }

    public void setUnCheck(Task mTask) {
        if (mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId) || mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            TaskAsks.setFinish(mTaskDetialActivity, mTaskDetialHandler, mTask, 0, 0);
        } else if (mTask.senderIdList.contains(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            if (mTask.isLocked == 0) {
                TaskAsks.setFinish(mTaskDetialActivity, mTaskDetialHandler, mTask, 0, 0);
            } else {
                AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
            }
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public void setListCheck(TaskList item) {

        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            TaskListAsks.setListItemFinish(mTaskDetialActivity, mTaskDetialHandler, item, 1);
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public void setUnListCheck(TaskList item) {
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            TaskListAsks.setListItemFinish(mTaskDetialActivity, mTaskDetialHandler, item, 0);
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }


    public void showFinisSon() {
        for (int i = 0; i < mTaskDetialActivity.mSonTasksFinish.size(); i++) {
            addSonView(mTaskDetialActivity.mSonTasksFinish.get(i));
        }
    }

    public void hidFinisSon() {
        for (int i = mTaskDetialActivity.mSonTasks.size() + 2; i < mTaskDetialActivity.taksArea.getChildCount(); i++) {
            mTaskDetialActivity.taksArea.removeView(mTaskDetialActivity.taksArea.getChildAt(i));
            i--;
        }

    }

    public void doExpend() {
        if (mTaskDetialActivity.isExpend) {
            mTaskDetialActivity.isExpend = false;
            ImageView imageView = (ImageView) mTaskDetialActivity.finishLayer.findViewById(R.id.select_img);
            imageView.setImageResource(R.drawable.sniper1);
            hidFinisSon();
        } else {
            mTaskDetialActivity.isExpend = true;
            ImageView imageView = (ImageView) mTaskDetialActivity.finishLayer.findViewById(R.id.select_img);
            imageView.setImageResource(R.drawable.sniper1_s);
            showFinisSon();
        }
    }


    public void showMore() {

        final View popupWindowView = LayoutInflater.from(mTaskDetialActivity).inflate(R.layout.buttom_window17, null);
        mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        TextView cancle = (TextView) popupWindowView.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        mTaskDetialActivity.popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTaskDetialActivity.popupWindow.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体可点击
        mTaskDetialActivity.popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        mTaskDetialActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x77000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        mTaskDetialActivity.popupWindow.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = popupWindowView.findViewById(R.id.content).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        mTaskDetialActivity.shade.setVisibility(View.GONE);
                        mTaskDetialActivity.popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.popupWindow.dismiss();

            }
        });
//        ColorDrawable dw = new ColorDrawable(0x00000000);
        mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
//        mTaskDetialActivity.popupWindow.setBackgroundDrawable(dw);
        mTaskDetialActivity.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTaskDetialActivity.shade.setVisibility(View.GONE);
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        HorizontalListView funlist1 = (HorizontalListView) popupWindowView.findViewById(R.id.horizon_listview1);
        HorizontalListView funlist2 = (HorizontalListView) popupWindowView.findViewById(R.id.horizon_listview2);
        mTaskDetialActivity.mFuns1 = new TaskFunctionAdapter(mTaskDetialActivity, mTaskDetialActivity.mFunList1);
        mTaskDetialActivity.mFuns2 = new TaskFunctionAdapter(mTaskDetialActivity, mTaskDetialActivity.mFunList2);
        funlist1.setAdapter(mTaskDetialActivity.mFuns1);
        funlist2.setAdapter(mTaskDetialActivity.mFuns2);
        funlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMoreFuuctionGet((TaskFunction) parent.getAdapter().getItem(position));
            }
        });
        funlist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMoreFuuctionGet((TaskFunction) parent.getAdapter().getItem(position));
            }
        });
        mTaskDetialActivity.popupWindow.showAtLocation(popupWindowView, Gravity.BOTTOM, 0, 0);
    }

    public void onMoreFuuctionGet(TaskFunction fun) {
        if (fun.name.equals(mTaskDetialActivity.getString(R.string.task_detial_more_addtag))) {
            setTags();
        } else if (fun.name.equals(mTaskDetialActivity.getString(R.string.task_detial_more_addsontask))) {
            doCreat();
        } else if (fun.name.equals(mTaskDetialActivity.getString(R.string.task_detial_more_addlist))) {
            addList();
        } else if (fun.name.contains(mTaskDetialActivity.getString(R.string.task_detial_more_addstar))) {
            if (fun.onoff == false) {
                TaskAsks.setStar(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, 1);
            } else {
                TaskAsks.setStar(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, 0);
            }
        } else if (fun.name.equals(mTaskDetialActivity.getString(R.string.task_detial_more_closeremind))) {
            if (fun.onoff == false) {

            } else {

            }
        } else if (fun.name.equals(mTaskDetialActivity.getString(R.string.task_detial_more_copy))) {
            copyTask();
        } else if (fun.name.equals(mTaskDetialActivity.getString(R.string.task_detial_more_lock))) {
            if (fun.onoff == false) {
                TaskAsks.setLock(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, 1);
            } else {
                TaskAsks.setLock(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, 0);
            }
        } else if (fun.name.equals(mTaskDetialActivity.getString(R.string.task_detial_more_delete))) {
            deleteTask();
        } else if (fun.name.equals(mTaskDetialActivity.getString(R.string.task_detial_more_exist))) {
            exitTask();
        }
        mTaskDetialActivity.popupWindow.dismiss();
    }

    public void deleteTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mTaskDetialActivity);
        View view = View.inflate(mTaskDetialActivity, R.layout.alter_dialog1, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(mTaskDetialActivity.getString(R.string.task_detial_more_delete_title));
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(mTaskDetialActivity.getString(R.string.task_detial_more_delete_content));
        TextView btn1 = (TextView) view.findViewById(R.id.btn1);
        btn1.setText(mTaskDetialActivity.getString(R.string.task_detial_more_delete_only));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskAsks.doDelete(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, 0);
                mTaskDetialActivity.dialog.dismiss();
            }
        });
        TextView btn2 = (TextView) view.findViewById(R.id.btn2);
        btn2.setText(mTaskDetialActivity.getString(R.string.task_detial_more_delete_all));
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskAsks.doDelete(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, 1);
                mTaskDetialActivity.dialog.dismiss();
            }
        });
        TextView btn3 = (TextView) view.findViewById(R.id.cancle);
        btn3.setText(mTaskDetialActivity.getString(R.string.button_word_cancle));
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.dialog.dismiss();
            }
        });
        mTaskDetialActivity.dialog = builder.create();
        mTaskDetialActivity.dialog.show();
    }

    public void exitTask() {
        AppUtils.creatDialogTowButton(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_detial_more_exist)
                , mTaskDetialActivity.getString(R.string.task_detial_more_exist), mTaskDetialActivity.getString(R.string.button_word_cancle),
                mTaskDetialActivity.getString(R.string.button_word_ok), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        TaskAsks.setExistTask(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, null);
                    }
                });
    }

    public void copyTask() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mTaskDetialActivity);
//        View view = View.inflate(mTaskDetialActivity, R.layout.alter_dialog2, null);
//        builder.setView(view);
//        builder.setCancelable(true);
//        TextView btn1 = (TextView) view.findViewById(R.id.ok);
//        btn1.setText(mTaskDetialActivity.getString(R.string.task_detial_more_delete_only));
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doCopy(mTaskDetialActivity.dialogView);
//                mTaskDetialActivity.dialog.dismiss();
//            }
//        });
//        TextView btn3 = (TextView) view.findViewById(R.id.cancle);
//        btn3.setText(mTaskDetialActivity.getString(R.string.cancel));
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTaskDetialActivity.dialog.dismiss();
//            }
//        });
//        mTaskDetialActivity.dialog = builder.create();
        mTaskDetialActivity.dialog.show();
    }

    public void selectProject() {
        Intent intent = new Intent(mTaskDetialActivity, TaskStructureActivity.class);
        intent.putExtra("task", mTaskDetialActivity.mTask);
        if (mTaskDetialActivity.mProject != null)
            intent.putExtra("project", mTaskDetialActivity.mProject);
        if (mTaskDetialActivity.mPraentTask != null)
            intent.putExtra("parent", mTaskDetialActivity.mPraentTask);
        mTaskDetialActivity.startActivity(intent);
    }

    public void updataName() {
        mTaskDetialActivity.taskName.setText(mTaskDetialActivity.mTask.taskName);
    }

    public void updtatName() {
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {

            AppUtils.creatDialogTowButtonEdit(mTaskDetialActivity, "", mTaskDetialActivity.getString(R.string.task_detial_set_task_name),
                    mTaskDetialActivity.getString(R.string.button_word_cancle), mTaskDetialActivity.getString(R.string.button_word_ok), null, new SetTaskNameListener()
                    , mTaskDetialActivity.mTask.taskName);
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public class SetTaskNameListener extends EditDialogListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                if (!editText.getText().equals(mTaskDetialActivity.mTask.taskName)) {
                    TaskAsks.setTaskName(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, editText.getText().toString());
                }
            } else {
                AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_detial_task_new_name));
            }
        }
    }

    public void updataLeader() {
        if (mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            Bus.callData(mTaskDetialActivity, "chat/setContacts", "负责人", TaskDetialActivity.ACTION_SET_LEADER);
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public void setBeginTime() {
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            if (!mTaskDetialActivity.beginTitle.equals(R.string.task_detial_set_begin)) {
                AppUtils.creatDataAndTimePicker(mTaskDetialActivity, mTaskDetialActivity.beginTitle.getText().toString().substring(3, mTaskDetialActivity.beginTitle.getText().length()) + ":00"
                        , mTaskDetialActivity.getString(R.string.task_detial_set_begin_1), mOnBeginSetListener);
            } else {
                AppUtils.creatDataAndTimePicker(mTaskDetialActivity, TimeUtils.getDateAndTime(), mTaskDetialActivity.getString(R.string.task_detial_set_begin_1), mOnBeginSetListener);
            }

        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnBeginSetListener = new DoubleDatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            if (!mTaskDetialActivity.endTitle.getText().equals(mTaskDetialActivity.getString(R.string.task_detial_set_end))) {
                if (TimeUtils.measureBefore(textString + ":00",
                        mTaskDetialActivity.endTitle.getText().toString().substring(3, mTaskDetialActivity.endTitle.getText().length()) + ":00")) {
                    TaskAsks.setBeginTime(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, textString);
                } else {
                    AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_detial_time_hit));
                }
            } else {
                TaskAsks.setBeginTime(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, textString);
            }
        }
    };

    public void setEndTime() {
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            Calendar c = Calendar.getInstance();

            if (!mTaskDetialActivity.endTitle.equals(R.string.task_detial_set_end)) {
                AppUtils.creatDataAndTimePicker(mTaskDetialActivity, mTaskDetialActivity.endTitle.getText().toString().substring(3, mTaskDetialActivity.endTitle.getText().length()) + ":00"
                        , mTaskDetialActivity.getString(R.string.task_detial_set_end_1), mOnEndSetListener);
            } else {
                AppUtils.creatDataAndTimePicker(mTaskDetialActivity, TimeUtils.getDateAndTime(), mTaskDetialActivity.getString(R.string.task_detial_set_end_1), mOnEndSetListener);
            }
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnEndSetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            if (!mTaskDetialActivity.beginTitle.getText().equals(mTaskDetialActivity.getString(R.string.task_detial_set_begin))) {
                if (TimeUtils.measureBefore(mTaskDetialActivity.beginTitle.getText().toString().substring(3, mTaskDetialActivity.beginTitle.getText().length())
                        + ":00", textString + ":00")) {
                    TaskAsks.setEndTime(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, textString);
                } else {
                    AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_detial_time_hit));
                }
            } else {
                TaskAsks.setEndTime(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, textString);
            }
        }
    };

    public void setMembers() {
        Intent intent = new Intent(mTaskDetialActivity, TaskMemberActivity.class);
        intent.putParcelableArrayListExtra("members", mTaskDetialActivity.mContactModels);
        intent.putExtra("task", mTaskDetialActivity.mTask);
        ProjectPrase.MemberDetial memberDetial = new ProjectPrase.MemberDetial();
        if (mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
            memberDetial.leavlType = 1;
        else
            memberDetial.leavlType = 3;
        intent.putExtra("memberdetial", memberDetial);
        mTaskDetialActivity.startActivity(intent);
    }

    public void updataBeginTime() {
        mTaskDetialActivity.beginTitle.setText(mTaskDetialActivity.getString(R.string.task_detial_date_begin) + mTaskDetialActivity.mTask.beginTime);
    }

    public void updataEndTime() {
        mTaskDetialActivity.endTitle.setText(mTaskDetialActivity.getString(R.string.task_detial_date_end) + mTaskDetialActivity.mTask.endTime);
    }

    public void updataLeader(NetObject net) {

        String json = net.result;
        if (AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if (AppUtils.success(json) == false) {
            AppUtils.showMessage(mTaskDetialActivity, AppUtils.getfailmessage(json));
        }
        Contacts taskPerson = (Contacts) net.item;
        mTaskDetialActivity.mTask.leaderId = taskPerson.mRecordid;
        AppUtils.setContactCycleHead(mTaskDetialActivity.headImg, taskPerson.getName(), taskPerson.colorhead);
        mTaskDetialActivity.headTitle.setText(taskPerson.getName());
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            ToolBarHelper.setRightBtn(mTaskDetialActivity.mActionBar, mTaskDetialActivity.finishListener, mTaskDetialActivity.mMoreListenter, R.drawable.ntask_completew, " · · ·");
        } else {
            if (mTaskDetialActivity.mTask.isComplete == 0)
                ToolBarHelper.setRightBtnText(mTaskDetialActivity.mActionBar, mTaskDetialActivity.mMoreListenter, " · · ·");
            else
                ToolBarHelper.setRightBtnText(mTaskDetialActivity.mActionBar, mTaskDetialActivity.mMoreListenter, " · · ·");
        }
    }

    public void removeMember(String id) {
        if (id.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                && (!mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))) {
            mTaskDetialActivity.finish();
        }
    }

    public void startDetial(Contacts mContacts) {
//        MailManager.getInstance().mShowContactDetial = mContacts;
//        Intent intent = new Intent(mTaskDetialActivity, ContactsDetialActivity.class);
//        mTaskDetialActivity.startActivity(intent);
    }

    public void showDeleteReport(final Reply mReplyModel) {

        AppUtils.creatDialogTowButton(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.xml_reply_delete), "",
                mTaskDetialActivity.getString(R.string.button_word_cancle), mTaskDetialActivity.getString(R.string.button_word_ok), null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        TaskReplyAsks.deleteReply(mTaskDetialActivity, mTaskDetialHandler, mReplyModel);
                    }
                });
    }

    public void showSetUnfinish(final int all) {

        AppUtils.creatDialogTowButton(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_detial_set_unfinish_1), mTaskDetialActivity.getString(R.string.task_detial_set_unfinish),
                mTaskDetialActivity.getString(R.string.button_word_cancle), mTaskDetialActivity.getString(R.string.button_word_ok), null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        TaskAsks.setFinish(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, 0, all);
                    }
                });

    }

    public void showsetFinish() {
        if (mTaskDetialActivity.mTask.taskcount > 0) {
            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem item1 = new MenuItem();
            item1.item = mTaskDetialActivity.mTask;
            item1.btnName = mTaskDetialActivity.getString(R.string.task_finish_btn);
            item1.mListener = mTaskDetialActivity.finishTaskListener;
            items.add(item1);
            item1 = new MenuItem();
            item1.item = mTaskDetialActivity.mTask;
            item1.btnName = mTaskDetialActivity.getString(R.string.task_beyond_all_btn);
            item1.mListener = mTaskDetialActivity.finishAllTaskListener;
            items.add(item1);
            mTaskDetialActivity.popupWindow = AppUtils.creatButtomMenu(mTaskDetialActivity, mTaskDetialActivity.shade, items, mTaskDetialActivity.findViewById(R.id.taskdetial));
        } else {
            TaskAsks.setFinish(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, 1, 0);
        }
    }

    public void showsetUnFinish() {
        if (mTaskDetialActivity.mTask.taskcount > 0) {

            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem item1 = new MenuItem();
            item1.item = mTaskDetialActivity.mTask;
            item1.btnName = mTaskDetialActivity.getString(R.string.task_unfinish_btn);
            item1.mListener = mTaskDetialActivity.unfinishTaskListener;
            items.add(item1);
            item1 = new MenuItem();
            item1.item = mTaskDetialActivity.mTask;
            item1.btnName = mTaskDetialActivity.getString(R.string.task_unbeyond_all_btn);
            item1.mListener = mTaskDetialActivity.unfinishAllTaskListener;
            items.add(item1);
            mTaskDetialActivity.popupWindow = AppUtils.creatButtomMenu(mTaskDetialActivity, mTaskDetialActivity.shade, items, mTaskDetialActivity.findViewById(R.id.taskdetial));
        } else {
            showSetUnfinish(0);
        }
    }

    public void setTags() {

        SelectManager.getInstance().startSelectView(mTaskDetialActivity, mTaskDetialActivity.mTags
                , mTaskDetialActivity.getString(R.string.task_detial_change_tags), TaskDetialActivity.ACTION_TASK_TAG, false, false);
    }

    public void updataDes() {
        mTaskDetialActivity.taskDes.setText(mTaskDetialActivity.mTask.des);
        if (mTaskDetialActivity.mTask.des.length() == 0) {
            mTaskDetialActivity.taskDes.setText(mTaskDetialActivity.getString(R.string.task_detial_des_hit));
        }
    }

    public void initAttachment() {
        mTaskDetialActivity.mAttchmentView.removeAllViews();
        for (int i = 0; i < mTaskDetialActivity.mAttachments.size(); i++) {
            addAttachmentViews(mTaskDetialActivity.mAttachments.get(i));
        }
    }

    public void addAttachmentViews(Attachment mAttachment) {
        LayoutInflater mInflater = (LayoutInflater) mTaskDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        File mfile = new File(mAttachment.mPath);
        View mView = mInflater.inflate(R.layout.fujian_long_item3, null);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
        TextView textView = (TextView) mView.findViewById(R.id.fujian_title);
        textView.setText(mAttachment.mName);
        TextView name = (TextView) mView.findViewById(R.id.user_name);
        Contacts contacts = (Contacts) Bus.callData(mTaskDetialActivity, "chat/getContactItem", mAttachment.taskuserid);
        name.setText(contacts.getName());
        TextView dete = (TextView) mView.findViewById(R.id.user_data);
        dete.setText(mAttachment.mDete.substring(0, 16));
        ImageView close = (ImageView) mView.findViewById(R.id.close_image);
        Button button = (Button) mView.findViewById(R.id.close_image_b);
        button.setTag(mAttachment);
        button.setOnClickListener(mTaskDetialActivity.mDeletePicListener2);
        close.setTag(mAttachment);
        close.setOnClickListener(mTaskDetialActivity.mDeletePicListener2);
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {

        } else if (TaskManager.getInstance().oaUtils.mAccount.mRecordId.equals(mAttachment.taskuserid)) {

        } else {
            button.setVisibility(View.INVISIBLE);
            close.setVisibility(View.INVISIBLE);
        }
        if (mTaskDetialActivity.contralEdit) {
            button.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
            close.setVisibility(View.INVISIBLE);
        }
//        OaUtils.getOaUtils().getAttachmentSize(mAttachment);
        if ((int) Bus.callData(mTaskDetialActivity, "filetools/getFileType", mAttachment.mName) == Actions.FILE_TYPE_PICTURE) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.plugin_camera_no_pictures);
            Glide.with(mTaskDetialActivity).load(TaskManager.getInstance().oaUtils.praseClodAttchmentUrl(mAttachment.mRecordid,70)).apply(options).into(mImageView);
        } else {
            Bus.callData(mTaskDetialActivity, "filetools/setfileimg", mImageView, mfile.getName());
        }

        mView.setTag(mAttachment);
        mView.setOnClickListener(mTaskDetialActivity.mShowPicListener);
        mTaskDetialActivity.mAttchmentView.addView(mView);
    }

    public void prasseReplys(NetObject net) {
        try {

            String json = net.result;
            if (AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if (AppUtils.success(json) == false) {
                AppUtils.showMessage(mTaskDetialActivity, AppUtils.getfailmessage(json));
                return;
            }

            JSONObject object = new JSONObject(json);
            JSONArray ja2 = object.getJSONArray("data");
            for (int i = 0; i < ja2.length(); i++) {
                JSONObject jo2 = ja2.getJSONObject(i);
                Reply mReplyModel = new Reply(jo2.getString("task_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), mTaskDetialActivity.mTask.taskId, jo2.getString("create_time"));
                mTaskDetialActivity.mReplys.add(mReplyModel);
            }
            mTaskDetialActivity.replyPage++;
            if (ja2.length() == 40)
                TaskReplyAsks.getReplays(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mTaskDetialActivity.replyPage);
            else {
//                mTaskDetialActivity.isreplyall = true;
//                if(mTaskDetialActivity.current == 0)
//                {
//                    initReplayViews();
//                }
                ReplyUtils.praseReplyViews(mTaskDetialActivity.mReplys, mTaskDetialActivity, null
                        , mTaskDetialActivity.mAnswerlayer, mTaskDetialActivity.mDeleteReplyListenter, mTaskDetialHandler);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void praseProjectView() {
        mTaskDetialActivity.mTask.projectName = mTaskDetialActivity.mProject.mName;
        if (mTaskDetialActivity.mTask.parentId.equals("0")) {
            mTaskDetialActivity.projectName.setText(mTaskDetialActivity.getString(R.string.task_main_tab_project)
                    + ":" + mTaskDetialActivity.mTask.projectName);
        }
    }

    public void praseProjectView(Intent intent) {
        String projectid = intent.getStringExtra("projectid");
        String name = intent.getStringExtra("name");

        if (mTaskDetialActivity.mTask.parentId.equals(projectid)) {
            mTaskDetialActivity.mTask.projectName = name;
            mTaskDetialActivity.mProject.mName = name;
            mTaskDetialActivity.projectName.setText(mTaskDetialActivity.getString(R.string.task_main_tab_project)
                    + ":" + mTaskDetialActivity.mTask.projectName);
        }
    }

    public void praseDetlete(Intent intent) {
        String projectid = intent.getStringExtra("projectid");
        if (mTaskDetialActivity.mTask.parentId.equals(projectid)) {
            mTaskDetialActivity.mTask.projectName = "";
            mTaskDetialActivity.mProject.mName = "";
            mTaskDetialActivity.mTask.projectId = "";
            mTaskDetialActivity.mProject.projectId = "";
            mTaskDetialActivity.projectName.setText(mTaskDetialActivity.getString(R.string.task_detial_project_none));
        }
    }

    public void praseTaskDetlete(Intent intent) {
        String arid = "";
        if (intent.hasExtra("guid")) {
            arid = intent.getStringExtra("guid");
        }
        String taskid = intent.getStringExtra("taskid");
        if (!arid.equals(mTaskDetialActivity.activityRid)) {
            if (mTaskDetialActivity.mTask.taskId.equals(taskid)) {
                mTaskDetialActivity.finish();

            }
        }
    }

    public void praseTaskParent() {
        mTaskDetialActivity.mTask.parentName = mTaskDetialActivity.mPraentTask.taskName;
        if (!mTaskDetialActivity.mTask.parentId.equals("0")) {
            mTaskDetialActivity.projectName.setText(mTaskDetialActivity.getString(R.string.task_structure_parent)
                    + ":" + mTaskDetialActivity.mTask.parentName);
        } else if (!mTaskDetialActivity.mTask.projectId.equals("0")) {
            mTaskDetialActivity.projectName.setText(mTaskDetialActivity.getString(R.string.task_main_tab_project)
                    + ":" + mTaskDetialActivity.mTask.projectName);
        } else {
            mTaskDetialActivity.projectName.setText(mTaskDetialActivity.getString(R.string.task_detial_project_none));
        }
    }

    public void setStare() {

        if (mTaskDetialActivity.mTask.isStar == 0) {
            mTaskDetialActivity.mTask.isStar = 1;
            mTaskDetialActivity.stare.setVisibility(View.VISIBLE);
            for (int i = 0; i < mTaskDetialActivity.mFunList2.size(); i++) {
                if (mTaskDetialActivity.mFunList2.get(i).name.contains(mTaskDetialActivity.getString(R.string.task_detial_more_addstar))) {
                    mTaskDetialActivity.mFunList2.get(i).onoff = true;
                    break;
                }
            }

            mTaskDetialActivity.stare.setVisibility(View.VISIBLE);
        } else {
            mTaskDetialActivity.mTask.isStar = 0;
            mTaskDetialActivity.stare.setVisibility(View.INVISIBLE);
            for (int i = 0; i < mTaskDetialActivity.mFunList2.size(); i++) {
                if (mTaskDetialActivity.mFunList2.get(i).name.contains(mTaskDetialActivity.getString(R.string.task_detial_more_addstar))) {
                    mTaskDetialActivity.mFunList2.get(i).onoff = false;
                    break;
                }
            }

            mTaskDetialActivity.stare.setVisibility(View.INVISIBLE);
        }
        mTaskDetialActivity.mFuns2.notifyDataSetChanged();
    }

    public void setSLock() {

        for (int i = 0; i < mTaskDetialActivity.mFunList2.size(); i++) {
            if (mTaskDetialActivity.mFunList2.get(i).name.contains("锁")) {
                if (mTaskDetialActivity.mTask.isLocked == 0) {
                    mTaskDetialActivity.mTask.isLocked = 1;
                    mTaskDetialActivity.mFunList2.get(i).onoff = true;
                } else {
                    mTaskDetialActivity.mTask.isLocked = 0;
                    mTaskDetialActivity.mFunList2.get(i).onoff = false;
                }
            }
        }
    }

    public void setTagViews() {
        mTaskDetialActivity.tagLayer.removeAllViews();
        mTaskDetialActivity.mTask.tag = "";
        for (int i = 0; i < SelectManager.getInstance().mSelects.size(); i++) {
            mTaskDetialActivity.mTags.get(i).iselect = SelectManager.getInstance().mSelects.get(i).iselect;
            if (mTaskDetialActivity.mTags.get(i).iselect == true) {
                addTagView(mTaskDetialActivity.mTags.get(i));
                if (mTaskDetialActivity.mTask.tag.length() == 0) {
                    mTaskDetialActivity.mTask.tag += mTaskDetialActivity.mTags.get(i).mId;
                } else {
                    mTaskDetialActivity.mTask.tag += "," + mTaskDetialActivity.mTags.get(i).mId;
                }
            }
        }
        if (mTaskDetialActivity.mTask.tag.length() == 0)
            mTaskDetialActivity.taglayer.setVisibility(View.GONE);
        else
            mTaskDetialActivity.taglayer.setVisibility(View.VISIBLE);
    }

    public void addTagView(Select mSelect) {
        View mView = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.tag_item, null);
        TextView sycle = (TextView) mView.findViewById(R.id.tag_img);
        if (mSelect.mId.equals("1")) {
            sycle.setBackgroundResource(R.drawable.shape_bg_tag_cycle1);
        } else if (mSelect.mId.equals("2")) {
            sycle.setBackgroundResource(R.drawable.shape_bg_tag_cycle2);
        } else if (mSelect.mId.equals("3")) {
            sycle.setBackgroundResource(R.drawable.shape_bg_tag_cycle3);
        } else if (mSelect.mId.equals("4")) {
            sycle.setBackgroundResource(R.drawable.shape_bg_tag_cycle4);
        } else if (mSelect.mId.equals("5")) {
            sycle.setBackgroundResource(R.drawable.shape_bg_tag_cycle5);
        }
        TextView title = (TextView) mView.findViewById(R.id.tag_title);
        title.setText(mSelect.mName);
        mTaskDetialActivity.tagLayer.addView(mView);
    }


    public void praseAddListItem(NetObject item) {
        try {
            JSONObject jsonObject = new JSONObject(item.result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updataListName(NetObject item) {
        try {
            JSONObject jsonObject = new JSONObject(item.result);
            JSONObject data = jsonObject.getJSONObject("data");
            TaskList mTaskList = (TaskList) item.item;
            String name = data.getString("name");
            mTaskList.name = name;
//            mTaskDetialActivity.mListViewAdapter.notifyItemChanged(mTaskDetialActivity.mList.indexOf(mTaskList));
            mTaskDetialActivity.mListViewAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updataRank(Contral contralModel) {
        LinearLayout leaves = (LinearLayout) contralModel.view.findViewById(R.id.leaves);
        if (contralModel.dayeType == 1) {
            for (int i = 0; i < leaves.getChildCount(); i++) {
                RelativeLayout leave = (RelativeLayout) leaves.getChildAt(i);
                if (i < Integer.valueOf(contralModel.value)) {
                    leave.setBackgroundResource(R.drawable.shape_bg_leave2);
                } else {
                    leave.setBackgroundResource(R.drawable.shape_bg_leave);
                }
            }
        } else {
            for (int i = 0; i < leaves.getChildCount(); i++) {
                ImageView leave = (ImageView) leaves.getChildAt(i);
                if (i < Integer.valueOf(contralModel.value)) {
                    leave.setImageResource(R.drawable.stare);
                } else {
                    leave.setImageResource(R.drawable.star_n);
                }
            }
        }
    }

    public void upDataContaralView(Contral contralModel) {

        if (contralModel.view != null) {
            if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_EDITTEXT || contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_DATAPICK) {
                contralModel.value = contralModel.testvalue;
                TextView mTextView = (TextView) contralModel.view.findViewById(R.id.value);
                mTextView.setText(contralModel.value);
            } else if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_SELECT) {
                if (contralModel.type.equals(TaskManager.CONTRAL_AREA)) {
                    TextView mTextView = (TextView) contralModel.view.findViewById(R.id.value);
                    String id = "";
                    for (int i = 0; i < contralModel.postItems.size(); i++) {
                        if (id.length() == 0) {
                            id += contralModel.postItems.get(i).mName;
                        } else {
                            id += "," + contralModel.postItems.get(i).mName;
                        }
                    }
                    contralModel.lastItems.clear();
                    contralModel.lastItems.addAll(contralModel.postItems);
                    contralModel.postItems.clear();
                    contralModel.listpos = 0;
                    mTextView.setText(id);
                } else {
                    TextView mTextView = (TextView) contralModel.view.findViewById(R.id.value);
                    contralModel.lastItems.clear();
                    contralModel.lastItems.addAll(contralModel.postItems);
                    String id = "";
                    for (int i = 0; i < contralModel.postItems.size(); i++) {
                        if (id.length() == 0) {
                            id += contralModel.postItems.get(i).mName;
                        } else {
                            id += "," + contralModel.postItems.get(i).mName;
                        }
                    }
                    contralModel.postItems.clear();
                    mTextView.setText(id);
                }

            } else if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_ATTACHMENT) {

                contralModel.value = contralModel.testvalue;
                try {
                    JSONArray jsonArray = new JSONArray(contralModel.value);
                    contralModel.mPics.clear();
                    contralModel.mImageLayer.removeAllViews();
                    for (int n = 0; n < jsonArray.length(); n++) {
                        JSONObject object = jsonArray.getJSONObject(n);
                        Attachment mAttachment = new Attachment(object.getString("hash"), object.getString("name")
                                , Bus.callData(mTaskDetialActivity, "filetools/getfilePath", "/task") + "/" + object.getString("hash")
                                + "." + object.getString("name").substring(object.getString("name").lastIndexOf("."), object.getString("name").length())
                                , TaskManager.getInstance().oaUtils.praseClodAttchmentUrl(object.getString("hash")), 0, 0, "");
                        mAttachment.mPath2 = Bus.callData(mTaskDetialActivity, "filetools/getfilePath", "/task") + "/" + object.getString("hash")
                                + "." + object.getString("name").substring(object.getString("name").lastIndexOf("."), object.getString("name").length());
                        mAttachment.taskattid = object.getString("task_attence_id");
                        mAttachment.taskuserid = object.getString("user_id");
                        contralModel.mPics.add(mAttachment);
                        addPicView(mAttachment, contralModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_RANK) {

                contralModel.value = contralModel.testvalue;
                updataRank(contralModel);
            }
        }
    }


    public void doTextEdit(final Contral contralModel) {

        final EditText et = new EditText(mTaskDetialActivity);
        et.getText().append(contralModel.defult);
        if (contralModel.type.equals(TaskManager.CONTRAL_AMOUNT) ||
                contralModel.type.equals(TaskManager.CONTRAL_NUMBER)) {
            et.setInputType(EditorInfo.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (contralModel.point > 0)
                et.setFilters(new InputFilter[]{new MyInputFilter(contralModel.point)});
            else {
                et.setKeyListener(new NumberKeyListener() {
                    @NonNull
                    @Override
                    protected char[] getAcceptedChars() {
                        char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
                        return numberChars;
                    }

                    @Override
                    public int getInputType() {
                        return InputType.TYPE_NUMBER_FLAG_DECIMAL;
                    }
                });
            }
        } else if (contralModel.type.equals(TaskManager.CONTRAL_PHONE)) {
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (contralModel.type.equals(TaskManager.CONTRAL_CARD)) {
            if (contralModel.dayeType == 0) {
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
            }
        }
        et.setText(contralModel.value);
        AlertDialog.Builder builder = new AlertDialog.Builder(mTaskDetialActivity);
        builder.setView(et);
        builder.setTitle(contralModel.name);
        builder.setNegativeButton(mTaskDetialActivity.getString(R.string.button_word_cancle), null);
        builder.setPositiveButton(mTaskDetialActivity.getString(R.string.button_word_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (et.getText().length() > 0) {
                    if (contralModel.point > 0)
                        measuredata(et, contralModel);
                    contralModel.testvalue = et.getText().toString();
                    TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, contralModel);
                }
            }
        });
        builder.show();
    }

    public void measuredata(EditText et, Contral contralModel) {
        if (et.getText().toString().contains(".")) {
            if (et.getText().toString().substring(et.getText().toString().indexOf("."), et.length()).length() - 1 < contralModel.point) {
                int count = contralModel.point - et.getText().toString().substring(et.getText().toString().indexOf("."), et.length()).length() + 1;
                String addstring = "";
                for (int i = 0; i < count; i++) {
                    addstring += "0";
                }
                et.setText(et.getText().toString() + addstring);
            }
        } else {
            int count = contralModel.point;
            String addstring = "";
            for (int i = 0; i < count; i++) {
                addstring += "0";
            }
            et.setText(et.getText() + "." + addstring);
        }
    }

    public void doDateEditTime(final Contral contralModel) {
        mTaskDetialActivity.uploadContral = contralModel;
        if (contralModel.value.length() > 0) {
            AppUtils.creatDataAndTimePicker(mTaskDetialActivity, contralModel.value + ":00", mTaskDetialActivity.getString(R.string.keyword_time), mDataTimeContralSetListener);
        } else {
            AppUtils.creatDataAndTimePicker(mTaskDetialActivity, TimeUtils.getDateAndTime(), mTaskDetialActivity.getString(R.string.keyword_time), mDataTimeContralSetListener);
        }
    }

    public DoubleDatePickerDialog.OnDateSetListener mDataTimeContralSetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            mTaskDetialActivity.uploadContral.testvalue = textString;
            TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mTaskDetialActivity.uploadContral);
        }
    };

    public void doDateEdit(final Contral contralModel) {
        mTaskDetialActivity.uploadContral = contralModel;
        if (contralModel.value.length() > 0) {
            AppUtils.creatDataPicker(mTaskDetialActivity, contralModel.value + " 00:00:00", mTaskDetialActivity.getString(R.string.keyword_time), mDataContralSetListener);
        } else {
            AppUtils.creatDataPicker(mTaskDetialActivity, TimeUtils.getDateAndTime(), mTaskDetialActivity.getString(R.string.keyword_time), mDataContralSetListener);
        }
    }


    public DoubleDatePickerDialog.OnDateSetListener mDataContralSetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);
            mTaskDetialActivity.uploadContral.testvalue = textString;
            TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mTaskDetialActivity.uploadContral);
        }
    };

    class MyInputFilter implements InputFilter {


        public int DECIMAL_DIGITS;

        public MyInputFilter(int pos) {
            DECIMAL_DIGITS = pos;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // source:当前输入的字符
            // start:输入字符的开始位置
            // end:输入字符的结束位置
            // dest：当前已显示的内容
            // dstart:当前光标开始位置
            // dent:当前光标结束位置
            if (dest.length() == 0 && source.equals(".")) {
                return "0.";
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                if (dotValue.length() == DECIMAL_DIGITS) {
                    return "";
                }
            }
            return null;
        }

    }


    public void praseContralView() {
        for (int i = 0; i < mTaskDetialActivity.mTask.mContrals.size(); i++) {
            addContralView(mTaskDetialActivity.mTask.mContrals.get(i));
        }
    }

    public void addContralView(Contral contralModel) {
        View mView = null;
        if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_EDITTEXT
                || contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_DATAPICK
                || contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_SELECT) {
            mView = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.contral_edittext, null);
            TextView name = (TextView) mView.findViewById(R.id.title);
            name.setText(contralModel.name);
            TextView value = (TextView) mView.findViewById(R.id.value);
            value.setTag(contralModel);
            value.setText(contralModel.value);
            value.setHint(contralModel.defult);
            if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_EDITTEXT)
                value.setOnClickListener(mTaskDetialActivity.doTextEdit);
            else if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_DATAPICK) {
                if (contralModel.dayeType == 1)
                    value.setOnClickListener(mTaskDetialActivity.doDateEditTime);
                else
                    value.setOnClickListener(mTaskDetialActivity.doDateEdit);
            } else if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_SELECT) {
                if (contralModel.isradio == 1)
                    value.setOnClickListener(mTaskDetialActivity.doSelectMoreEdit);
                else
                    value.setOnClickListener(mTaskDetialActivity.doSelectEdit);

                if (contralModel.type.equals(TaskManager.CONTRAL_AREA)) {
                    value.setOnClickListener(mTaskDetialActivity.doListSelectEdit);
                }
            }

            TextView unit = (TextView) mView.findViewById(R.id.unit);
            if (contralModel.type.equals(TaskManager.CONTRAL_AMOUNT) || contralModel.type.equals(TaskManager.CONTRAL_NUMBER)) {
                if (contralModel.unit.length() > 0) {
                    unit.setVisibility(View.VISIBLE);
                    unit.setText(contralModel.unit);
                } else {
                    unit.setVisibility(View.INVISIBLE);
                }
            } else {
                unit.setVisibility(View.INVISIBLE);
            }
        } else if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_ATTACHMENT) {
            mView = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.contral_attachment, null);
            contralModel.mImageLayer = (LinearLayout) mView.findViewById(R.id.image_content);
            TextView name = (TextView) mView.findViewById(R.id.title);
            name.setText(contralModel.name);
            TextView value = (TextView) mView.findViewById(R.id.value);
            value.setTag(contralModel);
            value.setOnClickListener(mTaskDetialActivity.mSelectshowPicListener);
            if (mTaskDetialActivity.contralEdit) {
                value.setVisibility(View.VISIBLE);
            } else {
                value.setVisibility(View.INVISIBLE);
            }

            for (int i = 0; i < contralModel.mPics.size(); i++) {
                Attachment mAttachment = contralModel.mPics.get(i);
                addPicView(mAttachment, contralModel);
            }

        } else if (contralModel.drawtype == TaskManager.CONTRAL_DRAW_TYPE_RANK) {
            if (contralModel.dayeType == 1) {
                mView = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.contral_leave, null);
                TextView name = (TextView) mView.findViewById(R.id.title);
                name.setText(contralModel.name);
                LinearLayout leaves = (LinearLayout) mView.findViewById(R.id.leaves);
                int count = Integer.valueOf(contralModel.value);
                for (int i = 0; i < leaves.getChildCount(); i++) {
                    RelativeLayout leave = (RelativeLayout) leaves.getChildAt(i);
                    leave.setTag(contralModel);
                    leave.setOnClickListener(mTaskDetialActivity.rankListener);
                    if (i < count) {
                        leave.setBackgroundResource(R.drawable.shape_bg_leave2);
                    } else {
                        leave.setBackgroundResource(R.drawable.shape_bg_leave);
                    }
                }
            } else {
                mView = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.contral_stare, null);
                TextView name = (TextView) mView.findViewById(R.id.title);
                name.setText(contralModel.name);
                LinearLayout leaves = (LinearLayout) mView.findViewById(R.id.leaves);
                int count = Integer.valueOf(contralModel.value);
                for (int i = 0; i < leaves.getChildCount(); i++) {
                    ImageView leave = (ImageView) leaves.getChildAt(i);
                    leave.setTag(contralModel);
                    leave.setOnClickListener(mTaskDetialActivity.rankListener);
                    if (i < count) {
                        leave.setImageResource(R.drawable.stare);
                    } else {
                        leave.setImageResource(R.drawable.star_n);
                    }
                }
            }

        }
        contralModel.view = mView;
        mTaskDetialActivity.mContralArea.addView(mView);
    }

    public void setRask(View v) {
        Contral contralModel = (Contral) v.getTag();
        LinearLayout layer = (LinearLayout) contralModel.view.findViewById(R.id.leaves);
        for (int i = 0; i < layer.getChildCount(); i++) {
            View view = layer.getChildAt(i);
            if (view.getId() == v.getId()) {
                contralModel.testvalue = String.valueOf(i + 1);
                break;
            }

        }
        TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, contralModel);
    }

    public void contralSelectMore(final Contral mContralModel) {

        final View popupWindowView = LayoutInflater.from(mTaskDetialActivity).inflate(R.layout.buttom_window19, null);
        mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        TextView cancle = (TextView) popupWindowView.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        mTaskDetialActivity.popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTaskDetialActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
        //mTaskDetialActivity.popupWindow.setOutsideTouchable(true);
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = popupWindowView.findViewById(R.id.content).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        mTaskDetialActivity.shade.setVisibility(View.GONE);
                        mTaskDetialActivity.popupWindow.dismiss();
                    }
                }
                return true;
            }
        });

        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.popupWindow.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
        mTaskDetialActivity.shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.shade.setVisibility(View.INVISIBLE);
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        mTaskDetialActivity.popupWindow.setBackgroundDrawable(dw);
        mTaskDetialActivity.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTaskDetialActivity.shade.setVisibility(View.GONE);
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        mContralModel.postItems.clear();
        for (int i = 0; i < mContralModel.mSelects.size(); i++) {
            mContralModel.mSelects.get(i).iselect = false;
            for (int j = 0; j < mContralModel.lastItems.size(); j++) {
                if (mContralModel.lastItems.get(j).mId.equals(mContralModel.mSelects.get(i).mId)) {
                    mContralModel.mSelects.get(i).iselect = true;
                    mContralModel.postItems.add(mContralModel.mSelects.get(i));
                    break;
                }
            }
        }
        mContralModel.testvalue = "";

        ListView funlist1 = (ListView) popupWindowView.findViewById(R.id.horizon_listview1);
        ArrayList<Select> mSelects = new ArrayList<Select>();
        mSelects.addAll(mContralModel.mSelects);
        final SelectMoreAdapter mSelectMoreAdapter = new SelectMoreAdapter(mTaskDetialActivity, mSelects, false);
        funlist1.setAdapter(mSelectMoreAdapter);
        funlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SelectMoreAdapter mSelectMoreAdapter = (SelectMoreAdapter) parent.getAdapter();
                Select mSelect = mSelectMoreAdapter.getItem(position);
                if (mSelect.iselect == false) {
                    mSelect.iselect = true;
                    mContralModel.postItems.add(mSelect);
                } else {
                    mSelect.iselect = false;
                    mContralModel.postItems.remove(mSelect);
                }
                mSelectMoreAdapter.notifyDataSetChanged();
            }
        });
        TextView ok = (TextView) popupWindowView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "";
                for (int i = 0; i < mContralModel.postItems.size(); i++) {
                    Select mSelect = mContralModel.postItems.get(i);
                    if (mSelect.iselect) {
                        if (id.length() == 0) {
                            id += mContralModel.postItems.get(i).mId;
                        } else {
                            id += "," + mContralModel.postItems.get(i).mId;
                        }
                    }
                }
                mContralModel.testvalue = id;
                TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mContralModel);
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        mTaskDetialActivity.popupWindow.showAtLocation(popupWindowView, Gravity.BOTTOM, 0, 0);
    }

    public void contralSelect(final Contral mContralModel) {

        final View popupWindowView = LayoutInflater.from(mTaskDetialActivity).inflate(R.layout.buttom_window20, null);
        mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        TextView cancle = (TextView) popupWindowView.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        for (int i = 0; i < mContralModel.mSelects.size(); i++) {
            mContralModel.mSelects.get(i).iselect = false;
            if (mContralModel.lastItems.size() > 0) {
                if (mContralModel.mSelects.get(i).mId.equals(mContralModel.lastItems.get(0).mId)) {
                    mContralModel.mSelects.get(i).iselect = true;
                }
            }
        }
        mContralModel.testvalue = "";
        mContralModel.postItems.clear();
        TextView title = (TextView) popupWindowView.findViewById(R.id.title);
        title.setText(mContralModel.name);
        mTaskDetialActivity.popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTaskDetialActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
        //mTaskDetialActivity.popupWindow.setOutsideTouchable(true);
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = popupWindowView.findViewById(R.id.content).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        mTaskDetialActivity.shade.setVisibility(View.GONE);
                        mTaskDetialActivity.popupWindow.dismiss();
                    }
                }
                return true;
            }
        });

        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.popupWindow.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
        mTaskDetialActivity.shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetialActivity.shade.setVisibility(View.INVISIBLE);
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        mTaskDetialActivity.popupWindow.setBackgroundDrawable(dw);
        mTaskDetialActivity.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTaskDetialActivity.shade.setVisibility(View.GONE);
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        ListView funlist1 = (ListView) popupWindowView.findViewById(R.id.horizon_listview1);
        ArrayList<Select> mSelects = new ArrayList<Select>();
        if (mContralModel.type.equals(TaskManager.CONTRAL_OPTION) || mContralModel.type.equals(TaskManager.CONTRAL_SELECT))
            mSelects.addAll(mContralModel.mSelects);
        SelectMoreAdapter mSelectMoreAdapter = new SelectMoreAdapter(mTaskDetialActivity, mSelects, false);
        funlist1.setAdapter(mSelectMoreAdapter);
        funlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Select mSelect = (Select) parent.getAdapter().getItem(position);
                mContralModel.postItems.add(mSelect);
                mContralModel.testvalue = mSelect.mId;
                TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mContralModel);
                mTaskDetialActivity.popupWindow.dismiss();
            }
        });
        mTaskDetialActivity.popupWindow.showAtLocation(popupWindowView, Gravity.BOTTOM, 0, 0);
    }

    public void contralListSelect(NetObject net) {

        try {
            String json = net.result;
            if (AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if (AppUtils.success(json) == false) {
                AppUtils.showMessage(mTaskDetialActivity, AppUtils.getfailmessage(json));
                return;
            }
            final Contral mContralModel = (Contral) net.item;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            mContralModel.mSelects.clear();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Select Select = new Select(jo.getString("region_id"), jo.getString("region_name"));
                mContralModel.mSelects.add(Select);
            }
            if (mContralModel.listpos == 0) {
                mContralModel.postItems.clear();
                mContralModel.testvalue = "";
            }
            mContralModel.listpos++;

            final View popupWindowView = LayoutInflater.from(mTaskDetialActivity).inflate(R.layout.buttom_window20, null);
            mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
            RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
            TextView cancle = (TextView) popupWindowView.findViewById(R.id.cancle);
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContralModel.postItems.clear();
                    mTaskDetialActivity.popupWindow.dismiss();
                }
            });

            for (int i = 0; i < mContralModel.mSelects.size(); i++) {
                mContralModel.mSelects.get(i).iselect = false;
                if (mContralModel.lastItems.size() == mContralModel.listcount) {
                    Select mSelect = mContralModel.lastItems.get(mContralModel.listpos - 1);
                    if (mSelect.mId.equals(mContralModel.mSelects.get(i).mId)) {
                        mContralModel.mSelects.get(i).iselect = true;
                    }
                }
            }

            TextView title = (TextView) popupWindowView.findViewById(R.id.title);
            title.setText(mContralModel.name);
            mTaskDetialActivity.popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            popupWindowView.setFocusable(true);
            popupWindowView.setFocusableInTouchMode(true);
            mTaskDetialActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
            //mTaskDetialActivity.popupWindow.setOutsideTouchable(true);
            popupWindowView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    int height = popupWindowView.findViewById(R.id.content).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            mTaskDetialActivity.shade.setVisibility(View.GONE);
                            mTaskDetialActivity.popupWindow.dismiss();
                        }
                    }
                    return true;
                }
            });

            lsyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTaskDetialActivity.popupWindow.dismiss();

                }
            });
            ColorDrawable dw = new ColorDrawable(0x00000000);
            mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
            mTaskDetialActivity.shade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTaskDetialActivity.shade.setVisibility(View.INVISIBLE);
                    mTaskDetialActivity.popupWindow.dismiss();
                }
            });
            mTaskDetialActivity.popupWindow.setBackgroundDrawable(dw);
            mTaskDetialActivity.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mTaskDetialActivity.shade.setVisibility(View.GONE);
                    mTaskDetialActivity.popupWindow.dismiss();
                }
            });
            ListView funlist1 = (ListView) popupWindowView.findViewById(R.id.horizon_listview1);
            ArrayList<Select> mSelects = new ArrayList<Select>();
            mSelects.addAll(mContralModel.mSelects);
            SelectMoreAdapter mSelectMoreAdapter = new SelectMoreAdapter(mTaskDetialActivity, mSelects, false);
            funlist1.setAdapter(mSelectMoreAdapter);
            funlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Select mSelect = (Select) parent.getAdapter().getItem(position);
                    mContralModel.postItems.add(mSelect);

                    if (mContralModel.testvalue.length() > 0) {
                        mContralModel.testvalue += "," + mSelect.mId;
                    } else {
                        mContralModel.testvalue += mSelect.mId;
                    }
                    if (mContralModel.listpos == mContralModel.listcount) {
                        TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mContralModel);
                    } else {
                        getRegin(mContralModel);
                    }
                    mTaskDetialActivity.popupWindow.dismiss();
                }
            });
            mTaskDetialActivity.popupWindow.showAtLocation(popupWindowView, Gravity.BOTTOM, 0, 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void upSendTaskDetial(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            mTaskDetialActivity.mTask.isComplete = jo.getInt("is_complete");
            mTaskDetialActivity.mTask.completeTime = TimeUtils.stampToDate(jo.getString("complete_time"));

            if (mTaskDetialActivity.mTask.isComplete == 1) {
//                mTaskDetialActivity.mSnowView.startfly();
                mTaskDetialActivity.finishLayerTop.setVisibility(View.VISIBLE);
                if (mTaskDetialActivity.mTask.endTime.length() > 0) {
                    if (TimeUtils.hoursBetween(mTaskDetialActivity.mTask.completeTime, mTaskDetialActivity.mTask.endTime) < 0) {
                        int day = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTaskDetialActivity.mTask.endTime)) / 24;
                        int hour = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTaskDetialActivity.mTask.endTime)) % 24;
                        String text;
                        if (day == 0) {
                            text = String.valueOf(hour) + "小时";
                        } else if (hour == 0) {
                            text = String.valueOf(day) + "天";
                        } else {
                            text = String.valueOf(day) + "天" + String.valueOf(hour) + "小时";
                        }
                        mTaskDetialActivity.finishTitle.setText("超时" + text + mTaskDetialActivity.getString(R.string.button_word_finish) +
                                " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                        mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(249, 183, 60));
                    } else {
                        mTaskDetialActivity.finishTitle.setText(mTaskDetialActivity.getString(R.string.task_finish_nomal) +
                                " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                        mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(76, 175, 80));
                    }
                } else {
                    mTaskDetialActivity.finishTitle.setText(mTaskDetialActivity.getString(R.string.task_finish_nomal) +
                            " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                    mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(76, 175, 80));
                }
            } else {
                mTaskDetialActivity.finishLayerTop.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setfinishview() {
        if (mTaskDetialActivity.mTask.isComplete == 1) {
            mTaskDetialActivity.mSnowView.startfly();
            mTaskDetialActivity.finishLayerTop.setVisibility(View.VISIBLE);
            if (mTaskDetialActivity.mTask.endTime.length() > 0) {
                if (TimeUtils.hoursBetween(mTaskDetialActivity.mTask.completeTime, mTaskDetialActivity.mTask.endTime) < 0) {
                    int day = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTaskDetialActivity.mTask.endTime)) / 24;
                    int hour = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTaskDetialActivity.mTask.endTime)) % 24;
                    String text;
                    if (day == 0) {
                        text = String.valueOf(hour) + "小时";
                    } else if (hour == 0) {
                        text = String.valueOf(day) + "天";
                    } else {
                        text = String.valueOf(day) + "天" + String.valueOf(hour) + "小时";
                    }
                    mTaskDetialActivity.finishTitle.setText("超期" + text + mTaskDetialActivity.getString(R.string.button_word_finish) +
                            " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                    mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(249, 183, 60));
                } else {
                    mTaskDetialActivity.finishTitle.setText(mTaskDetialActivity.getString(R.string.task_finish_nomal) +
                            " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                    mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(76, 175, 80));
                }
            } else {
                mTaskDetialActivity.finishTitle.setText(mTaskDetialActivity.getString(R.string.task_finish_nomal) +
                        " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(76, 175, 80));
            }
        } else {
            mTaskDetialActivity.finishLayerTop.setVisibility(View.GONE);
        }
    }

    public void checkSonlistUpdata(Intent intent) {
        String id = intent.getStringExtra("id");
        boolean has = false;
        for (int i = 0; i < mTaskDetialActivity.mSonTasks.size(); i++) {
            if (mTaskDetialActivity.mSonTasks.get(i).taskId.equals(id)) {
                has = true;
                break;
            }
        }
        if (has == true) {
            TaskAsks.getSon(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
        } else {
            for (int j = 0; j < mTaskDetialActivity.mSonTasksFinish.size(); j++) {
                if (mTaskDetialActivity.mSonTasksFinish.get(j).taskId.equals(id)) {
                    has = true;
                    break;
                }
            }

            if (has == true) {
                TaskAsks.getSon(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
            }
        }
    }


    public void updataContactViews(ArrayList<Contacts> contacts) {
        mTaskDetialActivity.mHorizontalListView.removeAllViews();
        for (int i = 0; i < contacts.size(); i++) {
            Contacts mContact = contacts.get(i);
            View mView = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.task_contact_item, null);
            mView.setOnClickListener(mTaskDetialActivity.mSetMembers);
            TextView mhead = (TextView) mView.findViewById(R.id.contact_img);
            AppUtils.setContactCycleHead(mhead, mContact.getName(), mContact.colorhead);
            if (i > 0) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = -30;
                mTaskDetialActivity.mHorizontalListView.addView(mView, layoutParams);
            } else {
                mTaskDetialActivity.mHorizontalListView.addView(mView);
            }
        }
        mTaskDetialActivity.pcount.setText(mTaskDetialActivity.getString(R.string.task_contacts_person) + String.valueOf(contacts.size()));
    }


    public void praseAttachment(NetObject net) {
        try {

            String json = net.result;
            if (AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if (AppUtils.success(json) == false) {
                AppUtils.showMessage(mTaskDetialActivity, AppUtils.getfailmessage(json));
                return;
            }

            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            String hashs = "";
            String names = "";
            mTaskDetialActivity.mTask.attachmentHash = "";
            mTaskDetialActivity.mTask.attachmentName = "";
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (hashs.length() == 0) {
                    hashs += jo.getString("hash");
                    names += jo.getString("name");
                } else {
                    hashs += "," + jo.getString("hash");
                    names += "," + jo.getString("name");
                }

                Attachment mAttachmentModel = new Attachment(jo.getString("hash"), jo.getString("name")
                        , Bus.callData(mTaskDetialActivity, "filetools/getfilePath", "/task") + "/" + jo.getString("hash") + jo.getString("name").substring(jo.getString("name").lastIndexOf("."), jo.getString("name").length())
                        , TaskManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")), 0, 0, "");
                mAttachmentModel.mPath2 = Bus.callData(mTaskDetialActivity, "filetools/getfilePath", "/task") + "/" + jo.getString("hash") + jo.getString("name").substring(jo.getString("name").lastIndexOf("."), jo.getString("name").length());
                mAttachmentModel.mDete = jo.getString("create_time");
                mAttachmentModel.taskuserid = jo.getString("user_id");
                mAttachmentModel.taskattid = jo.getString("task_attence_id");
                mTaskDetialActivity.mAttachments.add(mAttachmentModel);
            }
            if (hashs.length() > 0) {
                mTaskDetialActivity.mTask.attachmentHash = hashs;
            }
            if (names.length() > 0) {
                mTaskDetialActivity.mTask.attachmentName = names;
            }
            if (mTaskDetialActivity.current == 1)
                initAttachment();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initLogViews() {
        mTaskDetialActivity.mLogsArea.removeAllViews();
        for (int i = 0; i < mTaskDetialActivity.mLogModels.size(); i++) {
            Log model = mTaskDetialActivity.mLogModels.get(i);
            addlogView(model);
        }


    }

    public void praseLog(NetObject net) {
        JSONObject jsonObject = null;
        try {

            String json = net.result;
            if (AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if (AppUtils.success(json) == false) {
                AppUtils.showMessage(mTaskDetialActivity, AppUtils.getfailmessage(json));
                return;
            }

            jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for (int j = 0; j < ja.length(); j++) {
                JSONObject jo = ja.getJSONObject(j);
                Log model = new Log();
                model.content = jo.getString("content");
                model.id = jo.getString("task_log_id");
                model.creater = jo.getString("create_id");
                model.creattime = jo.getString("create_time");
                model.type = jo.getInt("icon");
                model.taskid = jo.getString("task_id");
                mTaskDetialActivity.mLogModels.add(model);

            }
            mTaskDetialActivity.logPage++;
            if (ja.length() >= 40) {
                TaskLogAsks.getLogs(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mTaskDetialActivity.logPage);
            } else {
                if (mTaskDetialActivity.current == 2)
                    initLogViews();
                mTaskDetialActivity.islogall = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onChange() {
        try {
            if (mTaskDetialActivity.changePosition != -1 && mTaskDetialActivity.startPosition != -1
                    && mTaskDetialActivity.startPosition != mTaskDetialActivity.changePosition) {

                TaskList mTaskList = mTaskDetialActivity.mList.get(mTaskDetialActivity.startPosition);
                TaskList mTaskList2 = mTaskDetialActivity.mList.get(mTaskDetialActivity.changePosition);
                TaskList mhead = null;
                TaskList mhead2 = null;
                for (int i = 0; i < mTaskDetialActivity.mList.size(); i++) {
                    if (mTaskDetialActivity.mList.get(i).mId.equals(mTaskList.mId)
                            && mTaskDetialActivity.mList.get(i).type == TaskList.LIST_TYPE_HEAD) {
                        mhead = mTaskDetialActivity.mList.get(i);
                    }
                }
                for (int i = 0; i < mTaskDetialActivity.mList.size(); i++) {
                    if (mTaskDetialActivity.mList.get(i).mId.equals(mTaskList2.mId)
                            && mTaskDetialActivity.mList.get(i).type == TaskList.LIST_TYPE_HEAD) {
                        mhead2 = mTaskDetialActivity.mList.get(i);
                    }
                }
                if (mTaskList.mId.equals(mTaskList2.mId)) {
                    if (mhead != null) {
                        int a = mhead.mLists.indexOf(mTaskList2);
                        JSONArray ja = new JSONArray();
                        JSONObject jo2 = new JSONObject();
                        jo2.put("item_name", mTaskList.name);
                        jo2.put("task_list_id", mTaskList.mId);
                        jo2.put("task_id", mTaskList.mTaskId);
                        jo2.put("is_complete", mTaskList.isComplete);
                        jo2.put("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
                        jo2.put("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
                        jo2.put("task_list_item_id", mTaskList.mListItemid);
                        ja.put(jo2);
                        for (int i = a + 1; i < mhead.mLists.size(); i++) {
                            if (mhead.mLists.get(i).type == TaskList.LIST_TYPE_ADD) {
                                break;
                            } else if (mhead.mLists.get(i).mListItemid.equals(mTaskList.mListItemid)) {

                            } else {
                                if (mhead.mLists.get(i).type == TaskList.LIST_TYPE_ITEM) {
                                    TaskList model = mhead.mLists.get(i);
                                    JSONObject jo = new JSONObject();
                                    jo.put("item_name", model.name);
                                    jo.put("task_list_id", model.mId);
                                    jo.put("task_id", model.mTaskId);
                                    jo.put("is_complete", model.isComplete);
                                    jo.put("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
                                    jo.put("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
                                    jo.put("task_list_item_id", model.mListItemid);
                                    ja.put(jo);
                                }
                            }
                        }
                        TaskListAsks.changeList(mTaskDetialActivity, mTaskDetialHandler, ja.toString());
                    }

                } else {
                    if (mhead2 != null) {
                        int a = mhead2.mLists.indexOf(mTaskList2);
                        JSONArray ja = new JSONArray();
                        JSONObject jo2 = new JSONObject();
                        jo2.put("item_name", mTaskList.name);
                        jo2.put("task_list_id", mhead2.mId);
                        jo2.put("task_id", mTaskList.mTaskId);
                        jo2.put("is_complete", mTaskList.isComplete);
                        jo2.put("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
                        jo2.put("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
                        jo2.put("task_list_item_id", mTaskList.mListItemid);
                        ja.put(jo2);
                        for (int i = a + 1; i < mhead2.mLists.size(); i++) {
                            if (mhead2.mLists.get(i).type == TaskList.LIST_TYPE_ADD) {
                                break;
                            } else {
                                if (mhead2.mLists.get(i).type == TaskList.LIST_TYPE_ITEM) {
                                    TaskList model = mhead2.mLists.get(i);
                                    JSONObject jo = new JSONObject();
                                    jo.put("item_name", model.name);
                                    jo.put("task_list_id", model.mId);
                                    jo.put("task_id", model.mTaskId);
                                    jo.put("is_complete", model.isComplete);
                                    jo.put("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
                                    jo.put("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
                                    jo.put("task_list_item_id", model.mListItemid);
                                    ja.put(jo);
                                }
                            }
                        }
                        TaskListAsks.changeList(mTaskDetialActivity, mTaskDetialHandler, ja.toString());
                    }
                }
            }
            mTaskDetialActivity.mListViewAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRecordID(String content) {
        String regEx = "([0-9a-fA-F]{8})-([0-9a-fA-F]{4})-([0-9a-fA-F]{4})-([0-9a-fA-F]{4})-([0-9a-fA-F]{12})";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(content);
        String content2 = content;
        int n = 0;
        while (matcher.find()) {
            if (n == 0) {

                content2 = content2.replace(matcher.group(0), (CharSequence) Bus.callData(mTaskDetialActivity, "chat/getContactName", matcher.group(0)));
            } else {
                content2 = content2.replace(matcher.group(0), (CharSequence) Bus.callData(mTaskDetialActivity, "chat/getContactName", matcher.group(0)));
            }
            n++;
            matcher = pattern.matcher(content2);
        }
        return content2;
    }

    public void addlogView(Log mLogModel) {
        View convertView = mTaskDetialActivity.getLayoutInflater().inflate(R.layout.log_item, null);
        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        TextView mtime = (TextView) convertView.findViewById(R.id.item_time);
        String content = getRecordID(mLogModel.content);
        switch (mLogModel.type) {

        }
        title.setText(Bus.callData(mTaskDetialActivity, "chat/getContactName", mLogModel.creater) + ": " + content);
        mtime.setText(mLogModel.creattime.substring(0, 16));
        mTaskDetialActivity.mLogsArea.addView(convertView);

    }

    public void TaskDetial(Task mTaskItemModel) {
        Intent intent = new Intent(mTaskDetialActivity, TaskDetialActivity.class);
        intent.putExtra("task", mTaskItemModel);
        mTaskDetialActivity.startActivity(intent);
    }

    public void setRagen(Contral contralModel, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);
                Select Select = new Select(jo.getString("region_id"), jo.getString("region_name"));
                contralModel.lastItems.add(Select);
                if (contralModel.value.length() == 0) {
                    contralModel.value += Select.mName;
                } else {
                    contralModel.value += "," + Select.mName;
                }

            }
            if (contralModel.view != null) {
                TextView value = (TextView) contralModel.view.findViewById(R.id.value);
                value.setText(contralModel.value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showAdd(Contral contralModel) {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = "拍照";
        item1.item = contralModel;
        item1.mListener = mTaskDetialActivity.mTakePhotoListenter;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = "相册";
        item1.item = contralModel;
        item1.mListener = mTaskDetialActivity.mAddPicListener;
        items.add(item1);
        mTaskDetialActivity.popupWindow = AppUtils.creatButtomMenu(mTaskDetialActivity, mTaskDetialActivity.shade, items, mTaskDetialActivity.findViewById(R.id.taskdetial));

//        View popupWindowView = LayoutInflater.from(mTaskDetialActivity).inflate(R.layout.picpwindowmenu, null);
//        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
//        lsyer.setFocusable(true);
//        lsyer.setFocusableInTouchMode(true);
//        mTaskDetialActivity.popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
//        popupWindowView.setFocusable(true);
//        popupWindowView.setFocusableInTouchMode(true);
//        mTaskDetialActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
//        lsyer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTaskDetialActivity.popupWindow.dismiss();
//
//            }
//        });
//        ColorDrawable dw = new ColorDrawable(0x00ffffff);
//        mTaskDetialActivity.popupWindow.setBackgroundDrawable(dw);
//        Button textview_edit = (Button) popupWindowView.findViewById(R.id.btn_pick_photo3);
//        textview_edit.setTag(contralModel);
//        textview_edit.setOnClickListener(mTaskDetialActivity.mTakePhotoListenter);
//        Button textview_delete = (Button) popupWindowView.findViewById(R.id.btn_take_photo);
//        textview_delete.setTag(contralModel);
//        textview_delete.setOnClickListener(mTaskDetialActivity.mAddPicListener);
//        Button textview_new = (Button) popupWindowView.findViewById(R.id.btn_cancel);
//        textview_new.setOnClickListener(mTaskDetialActivity.mCancleListenter2);
//        mTaskDetialActivity.shade.setVisibility(View.VISIBLE);
//        mTaskDetialActivity.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                mTaskDetialActivity.shade.setVisibility(View.INVISIBLE);
//            }
//        });
//        mTaskDetialActivity.popupWindow.showAtLocation(mTaskDetialActivity.findViewById(R.id.taskdetial),
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void showAdd() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = "拍照";
        item1.mListener = mTaskDetialActivity.mTakePhotoListenter2;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = "相册";
        item1.mListener = mTaskDetialActivity.mAddPicListener2;
        items.add(item1);
        mTaskDetialActivity.popupWindow = AppUtils.creatButtomMenu(mTaskDetialActivity, mTaskDetialActivity.shade, items, mTaskDetialActivity.findViewById(R.id.taskdetial));
    }

    public void dodismiss2() {
        mTaskDetialActivity.popupWindow.dismiss();

    }

    public void addPicView(Attachment mAttachment, Contral model) {
        model.attachment = mAttachment;
        LayoutInflater mInflater = (LayoutInflater) mTaskDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        File mfile = new File(mAttachment.mPath);
        View mView = mInflater.inflate(R.layout.fujian_long_item, null);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
        TextView textView = (TextView) mView.findViewById(R.id.fujian_title);
        textView.setText(mAttachment.mName);
        ImageView close = (ImageView) mView.findViewById(R.id.close_image);
        Button button = (Button) mView.findViewById(R.id.close_image_b);
        button.setTag(model);
        button.setOnClickListener(mTaskDetialActivity.mDeletePicListener);
        close.setTag(model);
        close.setOnClickListener(mTaskDetialActivity.mDeletePicListener);
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {

        } else if (TaskManager.getInstance().oaUtils.mAccount.mRecordId.equals(mAttachment.taskuserid)) {

        } else {
            button.setVisibility(View.INVISIBLE);
            close.setVisibility(View.INVISIBLE);
        }
        if (mTaskDetialActivity.contralEdit) {
            button.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
            close.setVisibility(View.INVISIBLE);
        }

        Bus.callData(mTaskDetialActivity, "filetools/setfileimg", mImageView, mfile.getName());
        mView.setTag(mAttachment);
        mView.setOnClickListener(mTaskDetialActivity.mShowPicListener);
        model.mImageLayer.addView(mView);
    }

    public void addPic(Contral contral) {
        mTaskDetialActivity.uploadContral = contral;
        mTaskDetialActivity.popupWindow.dismiss();
        Bus.callData(mTaskDetialActivity, "filetools/getPhotos", false, TaskManager.PIC_MAXSIZE, "intersky.task.view.activity.TaskDetialActivity", TaskDetialActivity.ACTION_ADD_TASK_PICTURE);

    }

    public void deletePic(Attachment mAttachment) {
        if (mTaskDetialActivity.uploadContral == null) {
            updataTaskAttchment();
        } else {
            try {
                JSONArray updata = new JSONArray();
                for (int i = 0; i < mTaskDetialActivity.uploadContral.mPics.size(); i++) {
                    if (!mTaskDetialActivity.uploadContral.mPics.get(i).taskattid.equals(mAttachment.taskattid)) {
                        JSONObject jo2 = new JSONObject();
                        jo2.put("user_id", mTaskDetialActivity.uploadContral.mPics.get(i).taskuserid);
                        jo2.put("hash", mTaskDetialActivity.uploadContral.mPics.get(i).mRecordid);
                        jo2.put("task_attence_id", mTaskDetialActivity.uploadContral.mPics.get(i).taskattid);
                        jo2.put("name", mTaskDetialActivity.uploadContral.mPics.get(i).mName);
                        updata.put(jo2);
                    }
                }
                mTaskDetialActivity.uploadContral.testvalue = updata.toString();
                TaskAsks.setContrals(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, mTaskDetialActivity.uploadContral);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void takePhoto(Contral contral) {
        mTaskDetialActivity.uploadContral = contral;
        if (mTaskDetialActivity.mAttachments.size() < TaskManager.PIC_MAXSIZE) {
            mTaskDetialActivity.permissionRepuest = (PermissionResult) Bus.callData(mTaskDetialActivity, "filetools/checkPermissionTakePhoto"
                    ,mTaskDetialActivity, Bus.callData(mTaskDetialActivity, "filetools/getfilePath", "notice/photo"));
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(TaskManager.PIC_MAXSIZE) + mTaskDetialActivity.getString(R.string.keyword_photoaddmax2));
        }
        mTaskDetialActivity.popupWindow.dismiss();
    }

    public void addPicResult(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        mTaskDetialActivity.mUploadAttachments.clear();
//        if (mTaskDetialActivity.uploadContral == null) {
//            mTaskDetialActivity.mUploadAttachments.addAll(mTaskDetialActivity.mAttachments);
//        } else {
//            mTaskDetialActivity.mUploadAttachments.addAll(mTaskDetialActivity.uploadContral.mPics);
//        }
        mTaskDetialActivity.mUploadAttachments.addAll(attachments);
        mTaskDetialActivity.uploadnames = "";
        for (int i = 0; i < attachments.size(); i++) {
            if (mTaskDetialActivity.uploadnames.length() == 0) {
                mTaskDetialActivity.uploadnames += attachments.get(i).mName;
            } else {
                mTaskDetialActivity.uploadnames += "," + attachments.get(i).mName;
            }
        }
        NetObject netObject = (NetObject) Bus.callData(mTaskDetialActivity, "filetools/getUploadFiles", mTaskDetialActivity.mUploadAttachments);
        if (((ArrayList<File>) netObject.item).size() > 0) {
            mTaskDetialActivity.waitDialog.show();
            TaskManager.getInstance().oaUtils.uploadAttchments((ArrayList<File>) netObject.item, mTaskDetialHandler, netObject.result);
        }
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mTaskDetialActivity, "filetools/takePhotoUri", ""));
                mTaskDetialActivity.mUploadAttachments.clear();
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("", mFile.getName(), "", "", mFile.length(), mFile.length(), "");
                    if (mTaskDetialActivity.uploadContral != null) {
                        mTaskDetialActivity.mUploadAttachments.addAll(mTaskDetialActivity.uploadContral.mPics);
                    } else {
                        mTaskDetialActivity.mUploadAttachments.addAll(mTaskDetialActivity.mAttachments);
                    }
                    mTaskDetialActivity.mUploadAttachments.add(attachment);
                    mTaskDetialActivity.uploadnames = attachment.mName;
                }
                NetObject netObject = (NetObject) Bus.callData(mTaskDetialActivity, "filetools/getUploadFiles", mTaskDetialActivity.mUploadAttachments);
                if (((ArrayList<File>) netObject.item).size() > 0) {
                    TaskManager.getInstance().oaUtils.uploadAttchments((ArrayList<File>) netObject.item, mTaskDetialHandler, netObject.result);
                }
                break;
        }
    }


    public void delete(final Attachment attachment) {

        AppUtils.creatDialogTowButton(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.cicle_delete_attachment), "",
                mTaskDetialActivity.getString(R.string.button_word_cancle), mTaskDetialActivity.getString(R.string.button_word_ok), null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //deletePic(mAttachment);
                        mTaskDetialActivity.waitDialog.show();
                        delAttachments(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, attachment);
                    }
                });
    }

    public void delete(final Contral contral) {

        AppUtils.creatDialogTowButton(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.cicle_delete_attachment), "",
                mTaskDetialActivity.getString(R.string.button_word_cancle), mTaskDetialActivity.getString(R.string.button_word_ok), null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //deletePic(mAttachment);
                        mTaskDetialActivity.uploadContral = contral;
                        delAttachments(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask, contral.attachment);
                    }
                });
    }


    public void initfinish() {
        if (mTaskDetialActivity.mTask.isComplete == 1) {
            mTaskDetialActivity.finishLayerTop.setVisibility(View.VISIBLE);
            if (mTaskDetialActivity.mTask.endTime.length() > 0) {
                if (TimeUtils.hoursBetween(mTaskDetialActivity.mTask.completeTime, mTaskDetialActivity.mTask.endTime) < 0) {
                    int day = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTaskDetialActivity.mTask.endTime)) / 24;
                    int hour = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTaskDetialActivity.mTask.endTime)) % 24;
                    String text;
                    if (day == 0) {
                        text = String.valueOf(hour) + "小时";
                    } else if (hour == 0) {
                        text = String.valueOf(day) + "天";
                    } else {
                        text = String.valueOf(day) + "天" + String.valueOf(hour) + "小时";
                    }
                    mTaskDetialActivity.finishTitle.setText("超时" + text + mTaskDetialActivity.getString(R.string.button_word_finish) +
                            " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                    mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(249, 183, 60));
                } else {
                    mTaskDetialActivity.finishTitle.setText(mTaskDetialActivity.getString(R.string.task_finish_nomal) +
                            " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                    mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(76, 175, 80));
                }
            } else {
                mTaskDetialActivity.finishTitle.setText(mTaskDetialActivity.getString(R.string.task_finish_nomal) +
                        " " + mTaskDetialActivity.mTask.completeTime.substring(0, 16));
                mTaskDetialActivity.finishLayerTop.setBackgroundColor(Color.rgb(76, 175, 80));
            }
        }
    }

    public void praseTaskDetial(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            mTaskDetialActivity.mTask.userId = jo.getString("user_id");
            mTaskDetialActivity.mTask.taskId = jo.getString("task_id");
            if (!jo.isNull("name"))
                mTaskDetialActivity.mTask.taskName = jo.getString("name");
            mTaskDetialActivity.mTask.parentId = jo.getString("parent_id");
            mTaskDetialActivity.mTask.parentIdList = jo.getString("parent_list");
            mTaskDetialActivity.mTask.leaderId = jo.getString("leader_id");
            mTaskDetialActivity.mTask.senderIdList = jo.getString("sender_id");
            if (!jo.isNull("description"))
                mTaskDetialActivity.mTask.des = jo.getString("description");
            mTaskDetialActivity.mTask.projectId = jo.getString("project_id");
            mTaskDetialActivity.mTask.isComplete = jo.getInt("is_complete");
            mTaskDetialActivity.mTask.isLocked = jo.getInt("is_locked");
            mTaskDetialActivity.mTask.isStar = jo.getInt("is_star");
            mTaskDetialActivity.mTask.beginTime = TimeUtils.stampToDate(jo.getString("begin_time"));
            mTaskDetialActivity.mTask.endTime = TimeUtils.stampToDate(jo.getString("end_time"));
            mTaskDetialActivity.mTask.stageId = jo.getString("project_stages_id");
            if (!jo.isNull("tag"))
                mTaskDetialActivity.mTask.tag = jo.getString("tag");
            mTaskDetialActivity.mTask.completeTime = TimeUtils.stampToDate(jo.getString("complete_time"));
            if (jo.has("has_one_notice")) {
                JSONObject jo3 = jo.getJSONObject("has_one_notice");
                if (jo3.getString("sender_id").equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                    if (jo3.getInt("is_read") == 1)
                        mTaskDetialActivity.mTask.isread = true;
                }
            }
            JSONArray ja;
            if (jo.has("has_task_list_item")) {
                ja = jo.getJSONArray("has_task_list_item");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete") == 1) {
                            mTaskDetialActivity.mTask.listfinish += 1;
                        }
                        mTaskDetialActivity.mTask.listcount += 1;

                    }
                }
            }

            if (jo.has("has_task")) {
                ja = jo.getJSONArray("has_task");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete") == 1) {

                            mTaskDetialActivity.mTask.taskfinish += 1;
                        }
                        mTaskDetialActivity.mTask.taskcount += 1;
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void doChangeListName(TaskList mTaskList) {
        if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
            AppUtils.creatDialogTowButtonEdit(mTaskDetialActivity, "", mTaskDetialActivity.getString(R.string.task_detial_list_new_name),
                    mTaskDetialActivity.getString(R.string.button_word_cancle), mTaskDetialActivity.getString(R.string.button_word_ok),
                    null, new ListNameChange(mTaskList), mTaskList.name);
        } else {
            AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public class ListNameChange extends EditDialogListener {

        public TaskList mTaskList;

        public ListNameChange(TaskList mTaskList) {
            this.mTaskList = mTaskList;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                TaskListAsks.changeListName(mTaskDetialActivity, mTaskDetialHandler, mTaskList, editText.getText().toString());
            } else {
                AppUtils.showMessage(mTaskDetialActivity, mTaskDetialActivity.getString(R.string.task_detial_list_new_name));
            }
        }
    }

    public void updataDetial(Intent intent) {
        String id = intent.getStringExtra("taskid");
        String arid = "";
        if (intent.hasExtra("guid")) {
            arid = intent.getStringExtra("guid");
        }
        if (!arid.equals(mTaskDetialActivity.activityRid)) {
            if (id.equals(mTaskDetialActivity.mTask.taskId))
                TaskAsks.getTaskDetial(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
            else if (mTaskDetialActivity.mPraentTask.taskId.equals(id))
                TaskAsks.getTaskDetial(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mPraentTask);
            else if (mTaskDetialActivity.sonList != null) {
                if (mTaskDetialActivity.sonList.containsKey(id))
                    TaskAsks.getSon(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mTask);
            }
        }
    }

    public void updataProjectDetial(Intent intent) {
        String id = intent.getStringExtra("projectid");
        if (id.equals(mTaskDetialActivity.mTask.projectId)) {
            ProjectAsks.getProjectItemDetial(mTaskDetialActivity, mTaskDetialHandler, mTaskDetialActivity.mProject);
        }
    }
}
