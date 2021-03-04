package intersky.task.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.appbase.FragmentTabAdapter;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.apputils.MenuItem;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.MyRelativeLayout;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectFileAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.CourseClass;
import intersky.task.entity.Project;
import intersky.task.entity.Task;
import intersky.task.handler.TaskManagerHandler;
import intersky.task.receiver.TaskManagerReceiver;
import intersky.task.view.activity.ProjectStageDetialActivity;
import intersky.task.view.activity.ProjectSysActivity;
import intersky.task.view.activity.TaskCreatActivity;
import intersky.task.view.activity.TaskDetialActivity;
import intersky.task.view.activity.TaskManagerActivity;
import intersky.task.view.activity.TemplateActivity;
import intersky.task.view.adapter.FileAdapter;
import intersky.task.view.adapter.ProjectAdapter;
import intersky.task.view.adapter.TaskAdapter;
import intersky.task.view.fragment.ProjectFragment;
import intersky.task.view.fragment.TaskFragment;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskManagerPresenter implements Presenter {

    public TaskManagerHandler mTaskManagerHandler;
    public TaskManagerActivity mTaskManagerActivity;
    public TextView title;
    public ImageView select;
    private Project item;
    public TaskManagerPresenter(TaskManagerActivity mTaskManagerActivity) {
        this.mTaskManagerActivity = mTaskManagerActivity;
        this.mTaskManagerHandler = new TaskManagerHandler(mTaskManagerActivity);
        mTaskManagerActivity.setBaseReceiver(new TaskManagerReceiver(mTaskManagerHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mTaskManagerActivity.setContentView(R.layout.activity_task_manager);
        ImageView back = mTaskManagerActivity.findViewById(R.id.back);
        back.setOnClickListener(mTaskManagerActivity.mBackListener);
        ImageView creat = mTaskManagerActivity.findViewById(R.id.creat);
        creat.setOnClickListener(mTaskManagerActivity.creatListener);
        select = mTaskManagerActivity.findViewById(R.id.select);
        select.setOnClickListener(showSelectListener);
        LayoutInflater inflater = LayoutInflater.from(mTaskManagerActivity);
        mTaskManagerActivity.buttomView = inflater.inflate(R.layout.buttom_select, null);
        mTaskManagerActivity.type = mTaskManagerActivity.buttomView.findViewById(R.id.type);
        mTaskManagerActivity.order = mTaskManagerActivity.buttomView.findViewById(R.id.order);
        mTaskManagerActivity.filter = mTaskManagerActivity.buttomView.findViewById(R.id.filter);
        mTaskManagerActivity.color = mTaskManagerActivity.buttomView.findViewById(R.id.color);
        mTaskManagerActivity.btnOk = mTaskManagerActivity.buttomView.findViewById(R.id.ok);
        mTaskManagerActivity.btnOk.setOnClickListener(okListener);
        mTaskManagerActivity.btnReset = mTaskManagerActivity.buttomView.findViewById(R.id.reset);
        mTaskManagerActivity.btnReset.setOnClickListener(resetListener);
        initClass();
        mTaskManagerActivity.tabTask = (RelativeLayout) mTaskManagerActivity.findViewById(R.id.tab_task);
        mTaskManagerActivity.task = mTaskManagerActivity.findViewById(R.id.tab_task_title);
        mTaskManagerActivity.tabProject = (RelativeLayout) mTaskManagerActivity.findViewById(R.id.tab_project);
        mTaskManagerActivity.project = mTaskManagerActivity.findViewById(R.id.tab_project_title);
        mTaskManagerActivity.shade = (RelativeLayout) mTaskManagerActivity.findViewById(R.id.shade);
        mTaskManagerActivity.tabTask.setOnClickListener(mTaskManagerActivity.tebTaskListener);
        mTaskManagerActivity.tabProject.setOnClickListener(mTaskManagerActivity.tebProjectListener);

        mTaskManagerActivity.taskFragment = new TaskFragment();
        mTaskManagerActivity.projectFragment = new ProjectFragment();
        mTaskManagerActivity.fragments.add(mTaskManagerActivity.taskFragment);
        mTaskManagerActivity.fragments.add(mTaskManagerActivity.projectFragment);
        mTaskManagerActivity.tabAdapter = new FragmentTabAdapter(mTaskManagerActivity, mTaskManagerActivity.fragments, R.id.tab_content);
        mTaskManagerActivity.mTaskAdapter = new TaskAdapter(mTaskManagerActivity, TaskManager.getInstance().mTasks);
        mTaskManagerActivity.mProjectAdapter = new ProjectAdapter(mTaskManagerActivity, TaskManager.getInstance().mProjects,mTaskManagerHandler);
        mTaskManagerActivity.mProjectSearchAdapter = new ProjectAdapter(mTaskManagerActivity, TaskManager.getInstance().mSearchProjects,mTaskManagerHandler);
        mTaskManagerActivity.mFileAdapter = new FileAdapter(mTaskManagerActivity, TaskManager.getInstance().mHeads);
        TaskManager.getInstance().taskPage = 1;
        TaskManager.getInstance().mTasks.clear();
        TaskManager.getInstance().taskAll = false;
        TaskManager.getInstance().projectPage = 1;
        TaskManager.getInstance().mProjects.clear();
        TaskManager.getInstance().mProjects2.clear();
        TaskManager.getInstance().mHeads.clear();
        TaskManager.getInstance().projectkAll = false;
        TaskManager.getInstance().mSearchProjects.clear();
        if (TaskManager.getInstance().mTasks.size() == 0)
            TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                    ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId,"",TaskManager.getInstance().taskPage);
        if (TaskManager.getInstance().mProjects.size() == 0)
            ProjectAsks.getProject(mTaskManagerActivity,mTaskManagerHandler,"",TaskManager.getInstance().projectPage);
        setContent(TaskManagerActivity.PAGE_TASK);

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
        if (mTaskManagerActivity.mTaskOrder.mClassId.equals("task_id ") && mTaskManagerActivity.mTaskFilter.mClassId.equals("all")
                && mTaskManagerActivity.mTaskType.mClassId.equals("0")) {

        } else {
            TaskManager.getInstance().mTasks.clear();
            TaskManager.getInstance().taskPage = 1;
            TaskManager.getInstance().taskAll = false;
        }
    }

    public void initClass() {
        CourseClass mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_type_all);
        mCourseClass.mClassId = "0";
        mCourseClass.isSelect = true;
        mTaskManagerActivity.mTaskType = mCourseClass;
        mTaskManagerActivity.mTaskTypeClass.add(mCourseClass);
        addView(mTaskManagerActivity.type,mCourseClass,true,typeClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_type_duty);
        mCourseClass.mClassId = "1";
        mTaskManagerActivity.mTaskTypeClass.add(mCourseClass);
        addView(mTaskManagerActivity.type,mCourseClass,false,typeClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_type_entrust);
        mCourseClass.mClassId = "2";
        mTaskManagerActivity.mTaskTypeClass.add(mCourseClass);
        addView(mTaskManagerActivity.type,mCourseClass,false,typeClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_type_attend);
        mCourseClass.mClassId = "3";
        mTaskManagerActivity.mTaskTypeClass.add(mCourseClass);
        addView(mTaskManagerActivity.type,mCourseClass,false,typeClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_filter_all);
        mCourseClass.mClassId = "all";
        mCourseClass.isSelect = true;
        mTaskManagerActivity.mTaskFilter = mCourseClass;
        mTaskManagerActivity.mTaskFilterClass.add(mCourseClass);
        addView(mTaskManagerActivity.filter,mCourseClass,true,filterClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_filter_processing);
        mCourseClass.mClassId = "processing";
        mTaskManagerActivity.mTaskFilterClass.add(mCourseClass);
        addView(mTaskManagerActivity.filter,mCourseClass,false,filterClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_filter_complete_all);
        mCourseClass.mClassId = "complete_all";
        mTaskManagerActivity.mTaskFilterClass.add(mCourseClass);
        addView(mTaskManagerActivity.filter,mCourseClass,false,filterClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_order_create_time);
        mCourseClass.mClassId = "create_time";
        mTaskManagerActivity.mTaskOrder = mCourseClass;
        mTaskManagerActivity.mTaskOrderClass.add(mCourseClass);
        addView(mTaskManagerActivity.order,mCourseClass,true,orderClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_order_update_time);
        mCourseClass.mClassId = "update_time";
        mTaskManagerActivity.mTaskOrderClass.add(mCourseClass);
        addView(mTaskManagerActivity.order,mCourseClass,true,orderClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_order_complete_time);
        mCourseClass.mClassId = "complete_time";
        mTaskManagerActivity.mTaskOrderClass.add(mCourseClass);
        addView(mTaskManagerActivity.order,mCourseClass,true,orderClickListener);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_order_end_time);
        mCourseClass.mClassId = "end_time";
        mTaskManagerActivity.mTaskOrderClass.add(mCourseClass);
        addView(mTaskManagerActivity.order,mCourseClass,true,orderClickListener);
//        mCourseClass = new CourseClass();
//        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_structure_class_tag);
//        mCourseClass.mClassId = "0";
//        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
//        mTaskManagerActivity.mTaskTag = mCourseClass;
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_pup);
        mCourseClass.mClassId = "1";
        mCourseClass.colorid = R.drawable.shape_bg_tag_cycle1;
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        addColorView(mTaskManagerActivity.color,mCourseClass,false);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_blu);
        mCourseClass.mClassId = "2";
        mCourseClass.colorid = R.drawable.shape_bg_tag_cycle2;
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        addColorView(mTaskManagerActivity.color,mCourseClass,false);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_yel);
        mCourseClass.mClassId = "3";
        mCourseClass.colorid = R.drawable.shape_bg_tag_cycle3;
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        addColorView(mTaskManagerActivity.color,mCourseClass,false);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_org);
        mCourseClass.mClassId = "4";
        mCourseClass.colorid = R.drawable.shape_bg_tag_cycle4;
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        addColorView(mTaskManagerActivity.color,mCourseClass,false);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_red);
        mCourseClass.mClassId = "5";
        mCourseClass.colorid = R.drawable.shape_bg_tag_cycle5;
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        addColorView(mTaskManagerActivity.color,mCourseClass,false);
    }

    public void addView(MyLinearLayout layout,CourseClass mCourseClass,boolean select,View.OnClickListener clickListener) {
        LayoutInflater inflater = LayoutInflater.from(mTaskManagerActivity);
        View view = inflater.inflate(R.layout.select_text_item, null);
        view.setOnClickListener(clickListener);
        mCourseClass.view = view;
        TextView name = view.findViewById(R.id.name);
        name.setText(mCourseClass.mCatName);
        view.setTag(mCourseClass);
        layout.addView(view);
        setSelect(view,select);
    }

    public void addColorView(MyLinearLayout layout, CourseClass mCourseClass, boolean select) {
        LayoutInflater inflater = LayoutInflater.from(mTaskManagerActivity);
        View view = inflater.inflate(R.layout.select_color_item, null);
        view.setOnClickListener(colorClickListener);
        mCourseClass.view = view;
        RelativeLayout bg = view.findViewById(R.id.button);
        bg.setBackgroundResource(mCourseClass.colorid);
        view.setTag(mCourseClass);
        layout.addView(view);
        setColorSelect(view,select);
    }

    public void setSelect(View view,boolean select)
    {
        TextView name = view.findViewById(R.id.name);
        RelativeLayout layer = view.findViewById(R.id.button);
        if(select == true)
        {
            layer.setBackgroundResource(R.drawable.buttom_select_bg_blue);
            name.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            layer.setBackgroundResource(R.drawable.buttom_select_bg_gary);
            name.setTextColor(Color.parseColor("#23272E"));
        }
    }

    public void setColorSelect(View view,boolean select)
    {
        ImageView name = view.findViewById(R.id.name);
        if(select == true)
        {
            name.setVisibility(View.VISIBLE);
        }
        else
        {
            name.setVisibility(View.INVISIBLE);
        }
    }



    public void falseAllClass1() {
        for (int i = 0; i < mTaskManagerActivity.mTaskTypeClass.size(); i++) {
            mTaskManagerActivity.mTaskTypeClass.get(i).isSelect = false;
        }
    }

    public void falseAllClass2() {
        for (int i = 0; i < mTaskManagerActivity.mTaskFilterClass.size(); i++) {
            mTaskManagerActivity.mTaskFilterClass.get(i).isSelect = false;
        }
    }

    public void falseAllClass3() {
        for (int i = 0; i < mTaskManagerActivity.mTaskOrderClass.size(); i++) {
            mTaskManagerActivity.mTaskOrderClass.get(i).isSelect = false;
        }
    }

    public void falseAllClass4() {
        for (int i = 0; i < mTaskManagerActivity.mTaskTagClass.size(); i++) {
            mTaskManagerActivity.mTaskTagClass.get(i).isSelect = false;
        }
    }

    public void startDetial(Task task) {
        Intent intent = new Intent(mTaskManagerActivity, TaskDetialActivity.class);
        intent.putExtra("task", task);
        mTaskManagerActivity.startActivity(intent);
    }

    public void startDetial(Project project) {
        if(mTaskManagerActivity.shade.getVisibility() == View.INVISIBLE)
        {
            if(project.type == 0)
            {
                Intent intent = new Intent(mTaskManagerActivity, ProjectStageDetialActivity.class);
                intent.putExtra("project", project);
                mTaskManagerActivity.startActivity(intent);
            }
            else
            {
                String ids = "";
                for(int i = 0 ; i < project.projects.size() ; i++)
                {
                    if(ids.length() == 0)
                    {
                        ids += project.projects.get(i).projectId;
                    }
                    else
                    {
                        ids += ","+project.projects.get(i).projectId;
                    }
                }
                Intent intent = new Intent(mTaskManagerActivity, ProjectSysActivity.class);
                intent.putExtra("projectid", ids);
                intent.putExtra("projectname", project.mName);
                mTaskManagerActivity.startActivity(intent);
            }
        }
    }

    //net
    public void getFileNames(ArrayList<Project> files) {

        if(files.size() > 0)
        {
            ProjectFileAsks.getFileNames(mTaskManagerActivity,mTaskManagerHandler,files);
            mTaskManagerActivity.waitDialog.show();
        }

    }


    public void onSearch(String word) {
        if (mTaskManagerActivity.nowPage == TaskManagerActivity.PAGE_TASK) {
            TaskManager.getInstance().taskAll = false;
            TaskManager.getInstance().mTasks.clear();
            TaskManager.getInstance().taskPage = 1;
            TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                    ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                    ,word,TaskManager.getInstance().taskPage);
            mTaskManagerActivity.waitDialog.show();
        } else {
            ProjectFragment mTaskFragment = (ProjectFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_PROJECT);
            TaskManager.getInstance().mSearchProjects.clear();
            for (int i = 0; i < TaskManager.getInstance().mProjects.size(); i++) {

                if(TaskManager.getInstance().mProjects.get(i).type == 0)
                {
                    if (TaskManager.getInstance().mProjects.get(i).mName.contains(word)) {
                        TaskManager.getInstance().mSearchProjects.add(TaskManager.getInstance().mProjects.get(i));
                    }
                }
                else
                {
                    for(int j = 0 ; j < TaskManager.getInstance().mProjects.get(i).projects.size(); j++)
                    {
                        if (TaskManager.getInstance().mProjects.get(i).projects.get(j).mName.contains(word)) {
                            TaskManager.getInstance().mSearchProjects.add(TaskManager.getInstance().mProjects.get(i).projects.get(j));
                        }
                    }
                }
            }
            mTaskFragment.projectList.setAdapter(mTaskManagerActivity.mProjectSearchAdapter);
        }
    }


    public void setContent(int id) {
        mTaskManagerActivity.nowPage = id;
        mTaskManagerActivity.tabAdapter.onCheckedChanged(id);
        switch (mTaskManagerActivity.nowPage) {
            case TaskManagerActivity.PAGE_TASK:
                mTaskManagerActivity.tabTask.setBackgroundResource(R.drawable.shape_task_mamger_head_ls);
                mTaskManagerActivity.task.setTextColor(Color.parseColor("#ffffff"));
                mTaskManagerActivity.tabProject.setBackgroundResource(R.drawable.shape_task_mamger_head_r);
                mTaskManagerActivity.project.setTextColor(Color.parseColor("#1EA1F3"));
                select.setVisibility(View.VISIBLE);
                break;
            case TaskManagerActivity.PAGE_PROJECT:
                mTaskManagerActivity.tabTask.setBackgroundResource(R.drawable.shape_task_mamger_head_l);
                mTaskManagerActivity.task.setTextColor(Color.parseColor("#1EA1F3"));
                mTaskManagerActivity.tabProject.setBackgroundResource(R.drawable.shape_task_mamger_head_rs);
                mTaskManagerActivity.project.setTextColor(Color.parseColor("#ffffff"));
                select.setVisibility(View.INVISIBLE);
                break;

        }

    }


    public void onFoot() {
        if (mTaskManagerActivity.nowPage == TaskManagerActivity.PAGE_TASK) {
            TaskFragment mTaskFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mTaskFragment.mPullToRefreshView != null) {
                mTaskFragment.mPullToRefreshView.onFooterRefreshComplete();
            }
            TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                    ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                    ,"",TaskManager.getInstance().taskPage);

        } else {
            ProjectFragment mProjectFragment = (ProjectFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_PROJECT);
            if (mProjectFragment.mPullToRefreshView != null) {
                mProjectFragment.mPullToRefreshView.onFooterRefreshComplete();
            }
            ProjectAsks.getProject(mTaskManagerActivity,mTaskManagerHandler,mProjectFragment.mSearchViewLayout.getText(),TaskManager.getInstance().projectPage);
        }
    }

    public void onHead() {
        if (mTaskManagerActivity.nowPage == TaskManagerActivity.PAGE_TASK) {
            TaskFragment mTaskFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mTaskFragment.mPullToRefreshView != null) {
                mTaskFragment.mPullToRefreshView.onHeaderRefreshComplete();
            }
            TaskManager.getInstance().taskPage = 1;
            TaskManager.getInstance().mTasks.clear();
            TaskManager.getInstance().taskAll = false;
            TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                    ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                    ,"",TaskManager.getInstance().taskPage);
        } else {
            ProjectFragment mProjectFragment = (ProjectFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_PROJECT);
            if (mProjectFragment.mPullToRefreshView != null) {
                mProjectFragment.mPullToRefreshView.onHeaderRefreshComplete();
            }

            TaskManager.getInstance().projectPage = 1;
            TaskManager.getInstance().mProjects.clear();
            TaskManager.getInstance().mProjects2.clear();
            TaskManager.getInstance().mHeads.clear();
            TaskManager.getInstance().projectkAll = false;
            TaskManager.getInstance().mSearchProjects.clear();
            ProjectAsks.getProject(mTaskManagerActivity,mTaskManagerHandler,mProjectFragment.mSearchViewLayout.getText(),TaskManager.getInstance().projectPage);

        }
    }


    public void upDataProjectItem(Intent intent) {
        Project taskItemModel = intent.getParcelableExtra("project");
        for (int i = 0; i < TaskManager.getInstance().mTasks.size(); i++) {
            Project temp = TaskManager.getInstance().mProjects.get(i);
            if (taskItemModel.projectId.equals(temp.projectId)) {

                temp.leaderId = taskItemModel.leaderId;
                temp.des = taskItemModel.des;
                temp.mName = taskItemModel.mName;
//                temp.isTop = taskItemModel.isTop;
//                temp.isPower = taskItemModel.isPower;
                break;
            }
        }
        mTaskManagerActivity.mProjectAdapter.notifyDataSetChanged();
        mTaskManagerActivity.mProjectSearchAdapter.notifyDataSetChanged();
    }


    public void doCreat() {
        if (mTaskManagerActivity.nowPage == TaskManagerActivity.PAGE_TASK) {
            Intent intent = new Intent(mTaskManagerActivity, TaskCreatActivity.class);
            Task task = new Task();
            task.leaderId = TaskManager.getInstance().oaUtils.mAccount.mRecordId;
            intent.putExtra("task",task);
            mTaskManagerActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(mTaskManagerActivity, TemplateActivity.class);
            mTaskManagerActivity.startActivity(intent);
        }

    }

    public void updataProjectLeaderr(Intent intent)
    {
        for (int i = 0; i < TaskManager.getInstance().mProjects.size(); i++) {
            Project mProject = TaskManager.getInstance().mProjects.get(i);
            if (mProject.projectId.equals(intent.getStringExtra("id"))) {
                mProject.leaderId = intent.getStringExtra("leader");
            }
        }
        for (int i = 0; i < TaskManager.getInstance().mSearchProjects.size(); i++) {
            Project mProject = TaskManager.getInstance().mProjects.get(i);
            if (mProject.projectId.equals(intent.getStringExtra("id"))) {
                mProject.leaderId = intent.getStringExtra("leader");
            }
        }
        mTaskManagerActivity.mProjectAdapter.notifyDataSetChanged();
        mTaskManagerActivity.mProjectSearchAdapter.notifyDataSetChanged();
    }

    public void odExpend(Project item)
    {
        item.expend = true;
        TaskManager.getInstance().mProjects.addAll(TaskManager.getInstance().mProjects.indexOf(item)+1, item.projects);
        mTaskManagerActivity.mProjectAdapter.notifyDataSetChanged();
    }

    public void unExpend(Project item)
    {
        item.expend = false;
        TaskManager.getInstance().mProjects.removeAll(item.projects);
        mTaskManagerActivity.mProjectAdapter.notifyDataSetChanged();

    }

    public void doMore(Project item)
    {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        if(item.type == 1)
        {
            MenuItem menuItem = new MenuItem();
            menuItem.btnName = mTaskManagerActivity.getString(R.string.project_file_rename);
            menuItem.mListener = mTaskManagerActivity.renameListener;
            menuItem.item = item;
            menuItems.add(menuItem);
            menuItem = new MenuItem();
            menuItem.btnName = mTaskManagerActivity.getString(R.string.project_file_delete);
            menuItem.mListener = mTaskManagerActivity.deleteListener;
            menuItem.item = item;
            menuItems.add(menuItem);
        }
        else
        {
            if(item.fileid.equals("0"))
            {
                MenuItem menuItem = new MenuItem();
                menuItem.btnName = mTaskManagerActivity.getString(R.string.project_file_new);
                menuItem.mListener = mTaskManagerActivity.creatFileListener;
                menuItem.item = item;
                menuItems.add(menuItem);
                menuItem = new MenuItem();
                menuItem.btnName = mTaskManagerActivity.getString(R.string.project_file_move);
                menuItem.mListener = mTaskManagerActivity.moveListener;
                menuItem.item = item;
                menuItems.add(menuItem);
            }
            else
            {
                MenuItem menuItem = new MenuItem();
                menuItem.btnName = mTaskManagerActivity.getString(R.string.project_file_out);
                menuItem.mListener = mTaskManagerActivity.moveOutListener;
                menuItem.item = item;
                menuItems.add(menuItem);
            }
        }
        mTaskManagerActivity.popupWindow = AppUtils.creatButtomMenu(mTaskManagerActivity,mTaskManagerActivity.shade,menuItems,mTaskManagerActivity.findViewById(R.id.taskmember));

    }

    public void doReName(final Project item) {

        AppUtils.creatDialogTowButtonEdit(mTaskManagerActivity,"",mTaskManagerActivity.getString(R.string.project_file_new_name)
                ,mTaskManagerActivity.getString(R.string.button_word_cancle),mTaskManagerActivity.getString(R.string.button_word_ok)
                ,null,new RenameListener(item),item.mName);
    }

    public class RenameListener extends EditDialogListener {

        public Project item;

        public RenameListener(Project item)
        {
            this.item = item;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                mTaskManagerActivity.waitDialog.show();
                ProjectFileAsks.rename(mTaskManagerActivity,mTaskManagerHandler,item,editText.getText().toString());
            } else {
                AppUtils.showMessage(mTaskManagerActivity, mTaskManagerActivity.getString(R.string.project_file_new_name));
            }
        }
    }

    public void doCreat(final Project item) {

        AppUtils.creatDialogTowButtonEdit(mTaskManagerActivity,"",mTaskManagerActivity.getString(R.string.project_file_new_name)
                ,mTaskManagerActivity.getString(R.string.button_word_cancle),mTaskManagerActivity.getString(R.string.button_word_ok)
                ,null,new CreatListener(item),"");
    }

    public class CreatListener extends EditDialogListener {

        public Project item;

        public CreatListener(Project item)
        {
            this.item = item;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                mTaskManagerActivity.waitDialog.show();
                ProjectFileAsks.creat(mTaskManagerActivity,mTaskManagerHandler,item,editText.getText().toString());
            } else {
                AppUtils.showMessage(mTaskManagerActivity, mTaskManagerActivity.getString(R.string.project_file_new_name));
            }
        }
    }

    public void doMoveSelect(final Project item) {
        mTaskManagerActivity.domove = false;
        final View popupWindowView = LayoutInflater.from(mTaskManagerActivity).inflate(R.layout.buttom_window181, null);
        mTaskManagerActivity.shade.setVisibility(View.VISIBLE);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        mTaskManagerActivity.popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTaskManagerActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
        mTaskManagerActivity.popupWindow.setOutsideTouchable(true);
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = popupWindowView.findViewById(R.id.content).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        mTaskManagerActivity.shade.setVisibility(View.INVISIBLE);
                        mTaskManagerActivity.popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskManagerActivity.popupWindow.dismiss();


            }
        });
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mTaskManagerActivity.shade.setVisibility(View.VISIBLE);
        mTaskManagerActivity.popupWindow.setBackgroundDrawable(dw);
        mTaskManagerActivity.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTaskManagerActivity.shade.setVisibility(View.INVISIBLE);
                if(mTaskManagerActivity.domove == false)
                {
                    doMore(item);
                }
            }
        });
        ListView funlist1 = (ListView) popupWindowView.findViewById(R.id.horizon_listview1);
        funlist1.setAdapter(mTaskManagerActivity.mFileAdapter);
        funlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item.nfileid = ((Project) parent.getAdapter().getItem(position)).fileid;
                ProjectFileAsks.move(mTaskManagerActivity,mTaskManagerHandler,item);
                mTaskManagerActivity.domove = true;
                mTaskManagerActivity.popupWindow.dismiss();
                mTaskManagerActivity.waitDialog.show();
            }
        });
        mTaskManagerActivity.popupWindow.showAtLocation(popupWindowView, Gravity.CENTER, 0, 0);
    }

    public void praseRename(Project item,String json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            item.mName = jo.getString("name");
            mTaskManagerActivity.mProjectAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void initDelete(Project item) {
        ArrayList<Project> temps = new ArrayList<Project>();
        temps.addAll(TaskManager.getInstance().mProjects2);
        TaskManager.getInstance().mHeads.remove(item);
        TaskManager.getInstance().mProjects.clear();
        for(int i = 0 ; i < TaskManager.getInstance().mHeads.size() ; i++)
        {
            temps.removeAll(TaskManager.getInstance().mHeads.get(i).projects);
            TaskManager.getInstance().mProjects.add(TaskManager.getInstance().mHeads.get(i));
            if(TaskManager.getInstance().mHeads.get(i).expend)
            {
                TaskManager.getInstance().mProjects.addAll(TaskManager.getInstance().mHeads.get(i).projects);
            }
        }
        TaskManager.getInstance().mProjects.addAll(temps);
        for(int i = 0 ; i < item.projects.size() ; i++)
        {
            item.projects.get(i).fileid = "0";
        }

        mTaskManagerActivity.mProjectAdapter.notifyDataSetChanged();
    }

    public void initMove(Project item) {

        if(item.fileid.equals("0"))
        {
            TaskManager.getInstance().mProjects.remove(item);
            item.fileid = item.nfileid;
            item.nfileid = "";
        }
        else
        {
            for(int i = 0; i < TaskManager.getInstance().mHeads.size(); i++)
            {
                if(TaskManager.getInstance().mHeads.get(i).fileid.equals(item.fileid))
                {
                    TaskManager.getInstance().mHeads.get(i).projects.remove(item);
                    if(TaskManager.getInstance().mHeads.get(i).expend)
                        TaskManager.getInstance().mProjects.remove(item);

                    if(TaskManager.getInstance().mHeads.get(i).projects.size() == 0)
                    {
                        TaskManager.getInstance().mProjects.remove(TaskManager.getInstance().mHeads.get(i));
                        TaskManager.getInstance().mHeads.remove(TaskManager.getInstance().mHeads.get(i));
                    }
                    break;
                }
            }

            item.fileid = item.nfileid;
            item.nfileid = "";
        }
        if(item.fileid.equals("0"))
        {
            ArrayList<Project> temps = new ArrayList<Project>();
            temps.addAll(TaskManager.getInstance().mProjects2);
            TaskManager.getInstance().mProjects.clear();
            for(int i = 0 ; i < TaskManager.getInstance().mHeads.size() ; i++)
            {
                temps.removeAll(TaskManager.getInstance().mHeads.get(i).projects);
                TaskManager.getInstance().mProjects.add(TaskManager.getInstance().mHeads.get(i));
                if(TaskManager.getInstance().mHeads.get(i).expend)
                {
                    TaskManager.getInstance().mProjects.addAll(TaskManager.getInstance().mHeads.get(i).projects);
                }
            }
            TaskManager.getInstance().mProjects.addAll(temps);
        }
        else
        {
            for(int i = 0 ; i < TaskManager.getInstance().mHeads.size() ; i++)
            {
                if(TaskManager.getInstance().mHeads.get(i).fileid.equals(item.fileid))
                {
                    int a = TaskManager.getInstance().mProjects2.indexOf(item);
                    boolean has = false;
                    for(int j = 0 ; j < TaskManager.getInstance().mHeads.get(i).projects.size() ; j++)
                    {
                        int pos = TaskManager.getInstance().mProjects2.indexOf(TaskManager.getInstance().mHeads.get(i).projects.get(j));
                        if(pos > a)
                        {
                            has = true;
                            TaskManager.getInstance().mHeads.get(i).projects.add(j,item);
                            if(TaskManager.getInstance().mHeads.get(i).expend)
                            {
                                int b = TaskManager.getInstance().mProjects.indexOf(TaskManager.getInstance().mHeads.get(i).projects.get(j));
                                TaskManager.getInstance().mProjects.add(b,item);
                            }
                            break;
                        }
                    }
                    if(has == false)
                    {
                        TaskManager.getInstance().mHeads.get(i).projects.add(item);
                        if(TaskManager.getInstance().mHeads.get(i).expend)
                        {
                            int b = TaskManager.getInstance().mProjects.indexOf(TaskManager.getInstance().mHeads.get(i));
                            TaskManager.getInstance().mProjects.add(b+1,item);
                        }
                    }
                    break;
                }
            }
        }

        mTaskManagerActivity.mProjectAdapter.notifyDataSetChanged();
    }

    public void updataProjectAll() {
        TaskManager.getInstance().projectOldPage = TaskManager.getInstance().projectPage;
        TaskManager.getInstance().projectPage = 1;
        TaskManager.getInstance().mProjects.clear();
        TaskManager.getInstance().mProjects2.clear();
        TaskManager.getInstance().projectSelects.clear();
        TaskManager.getInstance().mHeads.clear();
        TaskManager.getInstance().projectkAll = false;
        TaskManager.getInstance().mSearchProjects.clear();
        ProjectFragment mProjectFragment = (ProjectFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_PROJECT);
        if (mProjectFragment.mPullToRefreshView != null) {
            ProjectAsks.getProject(mTaskManagerActivity,mTaskManagerHandler,mProjectFragment.mSearchViewLayout.getText(),TaskManager.getInstance().projectPage);
        }
        else
        {
            ProjectAsks.getProject(mTaskManagerActivity,mTaskManagerHandler,"",TaskManager.getInstance().projectPage);
        }

    }

    public void updataTaskAll() {
        TaskFragment mTaskFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
        if (mTaskFragment.mPullToRefreshView != null) {
            TaskManager.getInstance().taskOldPage = TaskManager.getInstance().taskPage;
            TaskManager.getInstance().taskPage = 1;
            TaskManager.getInstance().mTasks.clear();
            TaskManager.getInstance().taskAll = false;
            TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                    ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                    ,"",TaskManager.getInstance().taskPage);
        }
        else
        {
            TaskManager.getInstance().taskOldPage = TaskManager.getInstance().taskPage;
            TaskManager.getInstance().taskPage = 1;
            TaskManager.getInstance().mTasks.clear();
            TaskManager.getInstance().taskAll = false;
            TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                    ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                    ,"",TaskManager.getInstance().taskPage);
        }

    }

    public View.OnClickListener showSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskManagerActivity.popupWindow = AppUtils.creatButtomView(mTaskManagerActivity,mTaskManagerActivity.shade,mTaskManagerActivity.findViewById(R.id.taskmember),mTaskManagerActivity.buttomView);
        }
    };

    public View.OnClickListener typeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CourseClass courseClass = (CourseClass) v.getTag();
            if(!courseClass.mClassId.equals(mTaskManagerActivity.mTaskType.mClassId))
            {
                mTaskManagerActivity.mTaskType.isSelect = false;
                setSelect(mTaskManagerActivity.mTaskType.view,false);
                mTaskManagerActivity.mTaskType = courseClass;
                mTaskManagerActivity.mTaskType.isSelect = true;
                setSelect(mTaskManagerActivity.mTaskType.view,true);
            }


        }
    };

    public View.OnClickListener filterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CourseClass courseClass = (CourseClass) v.getTag();
            if(!courseClass.mClassId.equals(mTaskManagerActivity.mTaskType.mClassId))
            {
                mTaskManagerActivity.mTaskFilter.isSelect = false;
                setSelect(mTaskManagerActivity.mTaskFilter.view,false);
                mTaskManagerActivity.mTaskFilter = courseClass;
                mTaskManagerActivity.mTaskFilter.isSelect = true;
                setSelect(mTaskManagerActivity.mTaskFilter.view,true);
            }

        }
    };


    public View.OnClickListener orderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CourseClass courseClass = (CourseClass) v.getTag();
            if(!courseClass.mClassId.equals(mTaskManagerActivity.mTaskOrder.mClassId))
            {
                mTaskManagerActivity.mTaskOrder.isSelect = false;
                setSelect(mTaskManagerActivity.mTaskOrder.view,false);
                mTaskManagerActivity.mTaskOrder = courseClass;
                mTaskManagerActivity.mTaskOrder.isSelect = true;
                setSelect(mTaskManagerActivity.mTaskOrder.view,true);
            }

        }
    };


    public View.OnClickListener colorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CourseClass courseClass = (CourseClass) v.getTag();
            if(courseClass.isSelect == true)
            {
                courseClass.isSelect = false;
                setColorSelect(courseClass.view,false);
            }
            else
            {
                courseClass.isSelect = true;
                setColorSelect(courseClass.view,true);
            }
        }
    };

    public View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doSelect();

        }
    };


    public View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for(int i = 0 ; i < mTaskManagerActivity.mTaskTagClass.size() ; i--)
            {
                CourseClass courseClass = mTaskManagerActivity.mTaskTagClass.get(i);
                courseClass.isSelect = false;
                setColorSelect(courseClass.view,false);
            }
            mTaskManagerActivity.mTaskType.isSelect = false;
            setSelect(mTaskManagerActivity.mTaskType.view,false);
            mTaskManagerActivity.mTaskType.isSelect = false;
            setSelect(mTaskManagerActivity.mTaskType.view,false);
            mTaskManagerActivity.mTaskFilter.isSelect = false;
            setSelect(mTaskManagerActivity.mTaskFilter.view,false);
            mTaskManagerActivity.mTaskType = mTaskManagerActivity.mTaskTypeClass.get(0);
            mTaskManagerActivity.mTaskFilter = mTaskManagerActivity.mTaskFilterClass.get(0);
            mTaskManagerActivity.mTaskOrder = mTaskManagerActivity.mTaskOrderClass.get(0);
            mTaskManagerActivity.mTaskType.isSelect = true;
            setSelect(mTaskManagerActivity.mTaskType.view,true);
            mTaskManagerActivity.mTaskFilter.isSelect = true;
            setSelect(mTaskManagerActivity.mTaskFilter.view,true);
            mTaskManagerActivity.mTaskFilter.isSelect = true;
            setSelect(mTaskManagerActivity.mTaskFilter.view,true);
            doSelect();
            if(mTaskManagerActivity.popupWindow != null)
            {
                mTaskManagerActivity.popupWindow.dismiss();
            }


        }
    };

    public void doSelect()
    {
        mTaskManagerActivity.tags = "";
        for (int i = 0; i < mTaskManagerActivity.mTaskTagClass.size(); i++) {
            if (mTaskManagerActivity.mTaskTagClass.get(i).isSelect == true) {
                if (mTaskManagerActivity.tags.length() == 0) {
                    mTaskManagerActivity.tags += mTaskManagerActivity.mTaskTagClass.get(i).mClassId;
                } else {
                    mTaskManagerActivity.tags += "," + mTaskManagerActivity.mTaskTagClass.get(i).mClassId;
                }
            }
        }
        TaskManager.getInstance().taskPage = 1;
        TaskManager.getInstance().taskAll = false;
        TaskManager.getInstance().mTasks.clear();
        TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                ,"",TaskManager.getInstance().taskPage);
    }


}
