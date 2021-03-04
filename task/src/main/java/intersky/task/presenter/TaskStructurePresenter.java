package intersky.task.presenter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Task;
import intersky.task.handler.TaskStructureHandler;
import intersky.task.prase.TaskPrase;
import intersky.task.receiver.TaskStructureReceiver;
import intersky.task.view.activity.AddProjectActivity;
import intersky.task.view.activity.ProjectStageDetialActivity;
import intersky.task.view.activity.TaskDetialActivity;
import intersky.task.view.activity.TaskStructureActivity;
import intersky.task.view.adapter.TaskSelectAdapter;
import intersky.xpxnet.net.NetObject;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskStructurePresenter implements Presenter {

    public TaskStructureHandler mTaskStructureHandler;
    public TaskStructureActivity mTaskStructureActivity;
    public TextView change;
    public TaskStructurePresenter(TaskStructureActivity mTaskStructureActivity) {
        this.mTaskStructureActivity = mTaskStructureActivity;
        this.mTaskStructureHandler = new TaskStructureHandler(mTaskStructureActivity);
        mTaskStructureActivity.setBaseReceiver(new TaskStructureReceiver(mTaskStructureHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mTaskStructureActivity.setContentView(R.layout.activity_task_structure);

        ImageView back = mTaskStructureActivity.findViewById(R.id.back);
        back.setOnClickListener(mTaskStructureActivity.mBackListener);

        mTaskStructureActivity.mTask = mTaskStructureActivity.getIntent().getParcelableExtra("task");
        if(mTaskStructureActivity.getIntent().hasExtra("project"))
        {
            mTaskStructureActivity.mProject = mTaskStructureActivity.getIntent().getParcelableExtra("project");
        }
        if(mTaskStructureActivity.getIntent().hasExtra("parent"))
        {
            mTaskStructureActivity.mPraentTask = mTaskStructureActivity.getIntent().getParcelableExtra("parent");
        }
        mTaskStructureActivity.projectName = (TextView) mTaskStructureActivity.findViewById(R.id.project_name);
        mTaskStructureActivity.projectNameChange = (TextView) mTaskStructureActivity.findViewById(R.id.project_name_change);
        mTaskStructureActivity.stageName = (TextView) mTaskStructureActivity.findViewById(R.id.stage_name);
        mTaskStructureActivity.stageNameChange = (TextView) mTaskStructureActivity.findViewById(R.id.stage_name_change);
        mTaskStructureActivity.parentName = (TextView) mTaskStructureActivity.findViewById(R.id.parent_task_name);
        mTaskStructureActivity.parentNameChange = (TextView) mTaskStructureActivity.findViewById(R.id.parent_task_name_change);
        mTaskStructureActivity.parentlayer = (LinearLayout) mTaskStructureActivity.findViewById(R.id.parent_area);
        mTaskStructureActivity.lineparent = (RelativeLayout) mTaskStructureActivity.findViewById(R.id.layerparent);
        mTaskStructureActivity.taskSelectAdapter = new TaskSelectAdapter(mTaskStructureActivity,mTaskStructureActivity.praentSetTasks);
//        mTaskStructureActivity.parentArea = (LinearLayout) mTaskStructureActivity.findViewById(R.id.parent_task);
//        mTaskStructureActivity.addArea = (RelativeLayout) mTaskStructureActivity.findViewById(R.id.new_task);
        mTaskStructureActivity.addEdit = (EditText) mTaskStructureActivity.findViewById(R.id.edittitle);
        mTaskStructureActivity.mSonArea = (LinearLayout) mTaskStructureActivity.findViewById(R.id.son_area);
        mTaskStructureActivity.stage = (RelativeLayout) mTaskStructureActivity.findViewById(R.id.stage);
        mTaskStructureActivity.nowTask = (TextView) mTaskStructureActivity.findViewById(R.id.task_name);
        mTaskStructureActivity.image1 = (ImageView) mTaskStructureActivity.findViewById(R.id.image1);
        mTaskStructureActivity.image2 = (ImageView) mTaskStructureActivity.findViewById(R.id.image2);
        mTaskStructureActivity.image3 = (ImageView) mTaskStructureActivity.findViewById(R.id.image3);
        mTaskStructureActivity.mSonTitle = (TextView) mTaskStructureActivity.findViewById(R.id.son_task_title);
        mTaskStructureActivity.parentLine = (RelativeLayout) mTaskStructureActivity.findViewById(R.id.parent_line);
        mTaskStructureActivity.taskline = (RelativeLayout) mTaskStructureActivity.findViewById(R.id.task_line);
        mTaskStructureActivity.head = (TextView) mTaskStructureActivity.findViewById(R.id.contact_img);
        String s = "";
        Contacts taskPerson = (Contacts) Bus.callData(mTaskStructureActivity,"chat/getContactItem",mTaskStructureActivity.mTask.leaderId);
        AppUtils.setContactCycleHead(mTaskStructureActivity.head,taskPerson.getName());
        mTaskStructureActivity.newTaskArea = (RelativeLayout) mTaskStructureActivity.findViewById(R.id.new_task);

        mTaskStructureActivity.projectName.setOnClickListener(mTaskStructureActivity.openProject);
        mTaskStructureActivity.parentName.setOnClickListener(mTaskStructureActivity.openParentTask);
        mTaskStructureActivity.projectNameChange.setOnClickListener(mTaskStructureActivity.setProject);
        mTaskStructureActivity.stageNameChange.setOnClickListener(mTaskStructureActivity.setStage);
        mTaskStructureActivity.parentNameChange.setOnClickListener(mTaskStructureActivity.setParent);
        mTaskStructureActivity.nowTask.setText(mTaskStructureActivity.mTask.taskName);
        mTaskStructureActivity.addEdit.setOnEditorActionListener(mTaskStructureActivity.mOnEditorActionListener);
        change = mTaskStructureActivity.findViewById(R.id.btnchange);
        change.setOnClickListener(mTaskStructureActivity.doEditListtener);
        onSave();

        if (mTaskStructureActivity.mTask.isLocked == 0 || mTaskStructureActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskStructureActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
        {

            change.setVisibility(View.VISIBLE);
        }
        else
        {
            change.setVisibility(View.INVISIBLE);
        }

        if (mTaskStructureActivity.mTask.isLocked == 0 || mTaskStructureActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskStructureActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
        {

        }
        else
        {
            mTaskStructureActivity.newTaskArea.setVisibility(View.GONE);
        }

        initStructureView();
        initSonTaskView();
        initParentView();
    }

    public void initStructureView() {

        if (mTaskStructureActivity.mTask.projectId.equals("0")) {
            mTaskStructureActivity.stage.setVisibility(View.GONE);
            mTaskStructureActivity.projectName.setText(mTaskStructureActivity.getString(R.string.task_structure_aline_project));
        } else {
            if(mTaskStructureActivity.mProject.mName.length() > 0 )
            {
                mTaskStructureActivity.projectName.setText(mTaskStructureActivity.mTask.projectName);
                initStages();
            }
            else
            {
                mTaskStructureActivity.mProject.projectId = mTaskStructureActivity.mTask.projectId;
                ProjectAsks.getProjectItemDetial(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mProject);
            }
        }

    }

    public void initSonTaskView() {
        if (mTaskStructureActivity.mTask.sonJson.length() > 0 && mTaskStructureActivity.mTask.taskcount> 0) {
            mTaskStructureActivity.mSonTitle.setVisibility(View.VISIBLE);
            NetObject netObject = new NetObject();
            netObject.result = mTaskStructureActivity.mTask.sonJson;
            netObject.item = mTaskStructureActivity.mTask;
            TaskPrase.praseSon(netObject,mTaskStructureActivity);
            praseSonView();
        } else {
            if(mTaskStructureActivity.mTask.taskcount > 0)
            {
                mTaskStructureActivity.mSonTitle.setVisibility(View.VISIBLE);
                TaskAsks.getSon(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mTask);
            }
            else
            {
                mTaskStructureActivity.mSonTitle.setVisibility(View.INVISIBLE);
            }

        }
    }

    public void initParentView() {
        if (mTaskStructureActivity.mTask.parentId.equals("0")) {
            mTaskStructureActivity.parentName.setText(mTaskStructureActivity.getString(R.string.task_structure_of_parent));
        } else {
            if(mTaskStructureActivity.mPraentTask.taskName.length() > 0 )
            {
                mTaskStructureActivity.parentName.setText(mTaskStructureActivity.mPraentTask.taskName);
            }
            else
            {
                mTaskStructureActivity.mPraentTask.taskId = mTaskStructureActivity.mTask.parentId;
                TaskAsks.getTaskDetial(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mPraentTask);
            }
        }

    }

    public void initStages() {
        mTaskStructureActivity.mStages.clear();
        try {
            JSONArray ja = new JSONArray(mTaskStructureActivity.mProject.stageString);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Select mSelectMoreModel = new Select(jo.getString("project_stages_id"), jo.getString("name"));
                mTaskStructureActivity.mStages.add(mSelectMoreModel);
                if (mSelectMoreModel.mId.equals(mTaskStructureActivity.mTask.stageId)) {
                    mTaskStructureActivity.mselectmStage = mSelectMoreModel;
                    mTaskStructureActivity.mTask.stageName = mSelectMoreModel.mName;
                    mSelectMoreModel.iselect = true;
                    mTaskStructureActivity.stageName.setText(mTaskStructureActivity.getString(R.string.task_structure_stage) + ":" + mTaskStructureActivity.mTask.stageName);
                    if (mTaskStructureActivity.stage.getVisibility() == View.GONE) {
                        mTaskStructureActivity.stage.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    }

    //net


    public void doEdit() {
        if (mTaskStructureActivity.isEdit == false) {
            mTaskStructureActivity.isEdit = true;
            onEdit();
        } else {
            mTaskStructureActivity.isEdit = false;
            onSave();
        }
    }

    public void doDelete(final Task taskItemModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mTaskStructureActivity);
        View view = View.inflate(mTaskStructureActivity, R.layout.alter_dialog1, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(mTaskStructureActivity.getString(R.string.task_detial_more_delete_title));
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(mTaskStructureActivity.getString(R.string.task_detial_more_delete_content));
        TextView btn1 = (TextView) view.findViewById(R.id.btn1);
        btn1.setText(mTaskStructureActivity.getString(R.string.task_detial_more_delete_only));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskAsks.doDelete(mTaskStructureActivity,mTaskStructureHandler,taskItemModel, 0);
                mTaskStructureActivity.dialog.dismiss();
            }
        });
        TextView btn2 = (TextView) view.findViewById(R.id.btn2);
        btn2.setText(mTaskStructureActivity.getString(R.string.task_detial_more_delete_all));
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskAsks.doDelete(mTaskStructureActivity,mTaskStructureHandler,taskItemModel, 1);
                mTaskStructureActivity.dialog.dismiss();
            }
        });
        TextView btn3 = (TextView) view.findViewById(R.id.cancle);
        btn3.setText(mTaskStructureActivity.getString(R.string.button_word_cancle));
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskStructureActivity.dialog.dismiss();
            }
        });
        mTaskStructureActivity.dialog = builder.create();
        mTaskStructureActivity.dialog.show();
    }

    public void onSave() {
        mTaskStructureActivity.image1.setVisibility(View.VISIBLE);
        mTaskStructureActivity.image2.setVisibility(View.VISIBLE);
        mTaskStructureActivity.image3.setVisibility(View.VISIBLE);
        if (mTaskStructureActivity.mTask.isLocked == 0 || mTaskStructureActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskStructureActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
        {
            mTaskStructureActivity.newTaskArea.setVisibility(View.VISIBLE);
        }
        mTaskStructureActivity.taskline.setVisibility(View.VISIBLE);
        mTaskStructureActivity.projectNameChange.setVisibility(View.INVISIBLE);
        mTaskStructureActivity.parentNameChange.setVisibility(View.INVISIBLE);
        if (mTaskStructureActivity.stageNameChange != null) {
            mTaskStructureActivity.stageNameChange.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < mTaskStructureActivity.mSonTasks.size(); i++) {
            View view = mTaskStructureActivity.mSonTasks.get(i).view;
            if (view != null) {
                TextView delete = (TextView) view.findViewById(R.id.delete);
                delete.setVisibility(View.INVISIBLE);
                TextView head = (TextView) view.findViewById(R.id.contact_img);
                head.setVisibility(View.VISIBLE);
            }
        }
        for (int i = 0; i < mTaskStructureActivity.mSonTasksFinish.size(); i++) {
            View view = mTaskStructureActivity.mSonTasks.get(i).view;
            if (view != null) {
                TextView delete = (TextView) view.findViewById(R.id.delete);
                delete.setVisibility(View.INVISIBLE);
                TextView head = (TextView) view.findViewById(R.id.contact_img);
                head.setVisibility(View.VISIBLE);
            }
        }

        if(mTaskStructureActivity.mTask.parentId.equals("0"))
        {
            mTaskStructureActivity.parentlayer.setVisibility(View.GONE);
            mTaskStructureActivity.lineparent.setVisibility(View.GONE);
        }

        if (mTaskStructureActivity.mTask.isLocked == 0 || mTaskStructureActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskStructureActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
        {
            change.setText(mTaskStructureActivity.getString(R.string.button_word_edit));
        }
    }

    public void onEdit() {

        mTaskStructureActivity.parentlayer.setVisibility(View.VISIBLE);
        mTaskStructureActivity.lineparent.setVisibility(View.VISIBLE);
        mTaskStructureActivity.image1.setVisibility(View.GONE);
        mTaskStructureActivity.image2.setVisibility(View.GONE);
        mTaskStructureActivity.image3.setVisibility(View.GONE);
        mTaskStructureActivity.newTaskArea.setVisibility(View.GONE);
        mTaskStructureActivity.taskline.setVisibility(View.GONE);
        mTaskStructureActivity.projectNameChange.setVisibility(View.VISIBLE);
        mTaskStructureActivity.parentNameChange.setVisibility(View.VISIBLE);

        if (mTaskStructureActivity.stageNameChange != null) {
            mTaskStructureActivity.stageNameChange.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < mTaskStructureActivity.mSonTasks.size(); i++) {
            View view = mTaskStructureActivity.mSonTasks.get(i).view;
            if (view != null) {
                TextView delete = (TextView) view.findViewById(R.id.delete);
                delete.setVisibility(View.VISIBLE);
                TextView head = (TextView) view.findViewById(R.id.contact_img);
                head.setVisibility(View.INVISIBLE);
            }
        }
        for (int i = 0; i < mTaskStructureActivity.mSonTasksFinish.size(); i++) {
            View view = mTaskStructureActivity.mSonTasksFinish.get(i).view;
            if (view != null) {
                TextView delete = (TextView) view.findViewById(R.id.delete);
                delete.setVisibility(View.VISIBLE);
                TextView head = (TextView) view.findViewById(R.id.contact_img);
                head.setVisibility(View.INVISIBLE);
            }
        }
        if (mTaskStructureActivity.mTask.isLocked == 0 || mTaskStructureActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                || mTaskStructureActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
        {
            change.setText( mTaskStructureActivity.getString(R.string.button_word_finish));

        }

    }

    public void openProject() {
        if((!mTaskStructureActivity.mTask.projectId.equals("0")) && mTaskStructureActivity.projectNameChange.getVisibility() == View.INVISIBLE)
        {
            ProjectAsks.getProjectDetial(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mProject);
        }
    }

    public void openParent() {


        if(!mTaskStructureActivity.mTask.parentId.equals("0")&& mTaskStructureActivity.projectNameChange.getVisibility() == View.INVISIBLE)
        {
            if(mTaskStructureActivity.mPraentTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId) ||
                    mTaskStructureActivity.mPraentTask.senderIdList.contains(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
            {
                mTaskStructureActivity.mPraentTask.show = true;
                TaskAsks.getTaskDetial(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mPraentTask);
            }
            else
            {
                AppUtils.showMessage(mTaskStructureActivity,mTaskStructureActivity.getString(R.string.task_no_auth));
            }
        }

    }



    public void selectProject() {

        TaskManager.getInstance().projectSelectAdapter.setSelect(mTaskStructureActivity.mTask.projectId);
        SelectManager.getInstance().startCustomSelectView(mTaskStructureActivity,TaskManager.getInstance().projectSelectAdapter,mTaskStructureActivity.selectProjectDetial,mTaskStructureActivity.getString(R.string.task_structure_aline_project2),
                TaskStructureActivity.ACTION_SET_PROJECT,true,false);
    }

    public void selectStage() {
        SelectManager.getInstance().startSelectView(mTaskStructureActivity,mTaskStructureActivity.mStages,mTaskStructureActivity.getString(R.string.task_structure_change_stage),
                TaskStructureActivity.ACTION_SET_STAGE,true,false);
    }

    public void selectParent() {
        SelectManager.getInstance().startCustomSelectView(mTaskStructureActivity,mTaskStructureActivity.taskSelectAdapter,null,mTaskStructureActivity.getString(R.string.task_structure_of_parent2),
                TaskStructureActivity.ACTION_SET_PARENT,true,false);
    }


    public void addSonView(Task mTask) {
        View view = mTaskStructureActivity.getLayoutInflater().inflate(R.layout.task_son_item_sample, null);
        view.setTag(mTask);
        view.setOnClickListener(mTaskStructureActivity.doDetialListener);
        TextView name = (TextView) view.findViewById(R.id.title);
        name.setText(mTask.taskName);
        TextView head = (TextView) view.findViewById(R.id.contact_img);
        TextView delete = (TextView) view.findViewById(R.id.delete);
        delete.setTag(mTask);
        delete.setOnClickListener(mTaskStructureActivity.doDeleteListener);
        String s = "";
        Contacts taskPerson = (Contacts) Bus.callData(mTaskStructureActivity,"chat/getContactItem",mTask.leaderId);
        AppUtils.setContactCycleHead(head,taskPerson.getName());
        if (mTaskStructureActivity.isEdit) {
            head.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            head.setVisibility(View.VISIBLE);
            delete.setVisibility(View.INVISIBLE);
        }
        if (mTask.isComplete == 1) {
            name.setTextColor(Color.rgb(140, 140, 140));
            name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mTaskStructureActivity.mSonArea.addView(view);
        } else {
            mTaskStructureActivity.mSonArea.addView(view, mTaskStructureActivity.mSonTasks.size() - 1);
        }
        mTask.view = view;
    }

    public void praseSonView() {

        mTaskStructureActivity.mSonArea.removeAllViews();
        mTaskStructureActivity.mSonTasks.clear();
        mTaskStructureActivity.mSonTasksFinish.clear();
        for (int i = 0; i < mTaskStructureActivity.mTask.tasks.size(); i++) {
            Task mTaskItemModel = mTaskStructureActivity.mTask.tasks.get(i);
            if (mTaskItemModel.isComplete == 0) {
                mTaskStructureActivity.mSonTasks.add(mTaskItemModel);
            } else {
                mTaskStructureActivity.mSonTasksFinish.add(mTaskItemModel);
            }
            if (mTaskStructureActivity.mSonTasks.size() > 0 || mTaskStructureActivity.mSonTasksFinish.size() > 0) {
                mTaskStructureActivity.mSonTitle.setVisibility(View.VISIBLE);
                mTaskStructureActivity.mSonTitle.setText(mTaskStructureActivity.getString(R.string.task_structure_son) + ":" + String.valueOf(mTaskStructureActivity.mSonTasksFinish.size()) + "/" +
                        String.valueOf(mTaskStructureActivity.mSonTasks.size() + mTaskStructureActivity.mSonTasksFinish.size()));
            } else {
                mTaskStructureActivity.mSonTitle.setVisibility(View.INVISIBLE);
            }
            addSonView(mTaskItemModel);
        }
    }

    public void doCreat(String name) {
        if(name.length() > 0)
        {
            Task task = new Task();
            task.taskName = name;
            task.leaderId = TaskManager.getInstance().oaUtils.mAccount.mRecordId;
            task.projectId = mTaskStructureActivity.mTask.projectId;
            task.stageId = mTaskStructureActivity.mTask.stageId;
            task.parentId = mTaskStructureActivity.mTask.taskId;
            TaskAsks.addTask(mTaskStructureActivity,mTaskStructureHandler,task);
            mTaskStructureActivity.waitDialog.show();
        }

    }

    public void startProject()
    {
        Intent intent = new Intent(mTaskStructureActivity, ProjectStageDetialActivity.class);
        intent.putExtra("project", mTaskStructureActivity.mProject);
        mTaskStructureActivity.startActivity(intent);
    }

    public void startAddProject()
    {
        Intent intent = new Intent(mTaskStructureActivity, AddProjectActivity.class);
        intent.putExtra("project", mTaskStructureActivity.mProject);
        mTaskStructureActivity.startActivity(intent);
    }

    public void startTaskDetial(Task task) {
        Intent intent  = new Intent(mTaskStructureActivity, TaskDetialActivity.class);
        intent.putExtra("task",task);
        intent.putExtra("parent",mTaskStructureActivity.mPraentTask);
        intent.putExtra("project",mTaskStructureActivity.mProject);
        mTaskStructureActivity.startActivity(intent);
    }

    public void updataTaskDetial(Intent intent) {
        String id = intent.getStringExtra("taskid");
        if(id.equals(mTaskStructureActivity.mTask.taskId))
        {
            TaskAsks.getTaskDetial(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mTask);
        }
        else if(id.equals(mTaskStructureActivity.mPraentTask.taskId))
        {
            TaskAsks.getTaskDetial(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mPraentTask);
        }
        else  {
//            TaskAsks.getTaskDetial(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mTask);
            TaskAsks.getSon(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mTask);
        }
    }

    public void updataTaskProject(Intent intent) {
        String id = intent.getStringExtra("projectid");
        if(id.equals(mTaskStructureActivity.mTask.projectId))
        {
            ProjectAsks.getProjectItemDetial(mTaskStructureActivity,mTaskStructureHandler,mTaskStructureActivity.mProject);
        }
    }
}
