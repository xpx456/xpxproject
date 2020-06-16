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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.appbase.FragmentTabAdapter;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.apputils.MenuItem;
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
import intersky.task.view.adapter.ClassAdapter;
import intersky.task.view.adapter.FileAdapter;
import intersky.task.view.adapter.ProjectAdapter;
import intersky.task.view.adapter.TagClassAdapter;
import intersky.task.view.adapter.TaskAdapter;
import intersky.task.view.fragment.ProjectFragment;
import intersky.task.view.fragment.TaskFragment;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskManagerPresenter implements Presenter {

    public TaskManagerHandler mTaskManagerHandler;
    public TaskManagerActivity mTaskManagerActivity;
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
        initClass();
        mTaskManagerActivity.setContentView(R.layout.activity_task_manager);
        mTaskManagerActivity.lineProject = (RelativeLayout) mTaskManagerActivity.findViewById(R.id.tab_project_line);
        mTaskManagerActivity.lineTask = (RelativeLayout) mTaskManagerActivity.findViewById(R.id.tab_task_line);
        mTaskManagerActivity.tabTask = (RelativeLayout) mTaskManagerActivity.findViewById(R.id.tab_task);
        mTaskManagerActivity.tabProject = (RelativeLayout) mTaskManagerActivity.findViewById(R.id.tab_project);
        mTaskManagerActivity.shade = (RelativeLayout) mTaskManagerActivity.findViewById(R.id.shade);
        mTaskManagerActivity.tabTask.setOnClickListener(mTaskManagerActivity.tebTaskListener);
        mTaskManagerActivity.tabProject.setOnClickListener(mTaskManagerActivity.tebProjectListener);
        ToolBarHelper.setTitle(mTaskManagerActivity.mActionBar, mTaskManagerActivity.getString(R.string.task_main_tasktitle));
        ToolBarHelper.setRightBtn(mTaskManagerActivity.mActionBar, mTaskManagerActivity.creatListener, R.drawable.headcreat);
//        ViewUtils.setTebtitle(mTaskManagerActivity,true);
        mTaskManagerActivity.fragments.add(new TaskFragment());
        mTaskManagerActivity.fragments.add(new ProjectFragment());
        mTaskManagerActivity.tabAdapter = new FragmentTabAdapter(mTaskManagerActivity, mTaskManagerActivity.fragments, R.id.tab_content);
        mTaskManagerActivity.mTaskAdapter = new TaskAdapter(mTaskManagerActivity, TaskManager.getInstance().mTasks);
        mTaskManagerActivity.mTaskSearchAdapter = new TaskAdapter(mTaskManagerActivity, TaskManager.getInstance().mSearchTasks);
        mTaskManagerActivity.mProjectAdapter = new ProjectAdapter(mTaskManagerActivity, TaskManager.getInstance().mProjects,mTaskManagerHandler);
        mTaskManagerActivity.mProjectSearchAdapter = new ProjectAdapter(mTaskManagerActivity, TaskManager.getInstance().mSearchProjects,mTaskManagerHandler);
        mTaskManagerActivity.mStatuAdapter = new ClassAdapter(mTaskManagerActivity, mTaskManagerActivity.mTaskTypeClass);
        mTaskManagerActivity.mPersonAdapter = new ClassAdapter(mTaskManagerActivity, mTaskManagerActivity.mTaskFilterClass);
        mTaskManagerActivity.mOrderAdapter = new ClassAdapter(mTaskManagerActivity, mTaskManagerActivity.mTaskOrderClass);
        mTaskManagerActivity.mTagAdapter = new TagClassAdapter(mTaskManagerActivity, mTaskManagerActivity.mTaskTagClass);
        mTaskManagerActivity.mFileAdapter = new FileAdapter(mTaskManagerActivity, TaskManager.getInstance().mHeads);
        TaskManager.getInstance().taskSearchPage = 1;
        TaskManager.getInstance().mSearchTasks.clear();
        TaskManager.getInstance().taskSearchAll = false;
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
        TaskManager.getInstance().mSearchTasks.clear();
        TaskManager.getInstance().taskSearchPage = 1;
        TaskManager.getInstance().taskSearchAll = false;
    }

    public void initClass() {
        CourseClass mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_type_all);
        mCourseClass.mClassId = "0";
        mCourseClass.isSelect = true;
        mTaskManagerActivity.mTaskType = mCourseClass;
        mTaskManagerActivity.mTaskTypeClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_type_duty);
        mCourseClass.mClassId = "1";
        mTaskManagerActivity.mTaskTypeClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_type_entrust);
        mCourseClass.mClassId = "2";
        mTaskManagerActivity.mTaskTypeClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_type_attend);
        mCourseClass.mClassId = "3";
        mTaskManagerActivity.mTaskTypeClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_filter_all);
        mCourseClass.mClassId = "all";
        mCourseClass.isSelect = true;
        mTaskManagerActivity.mTaskFilter = mCourseClass;
        mTaskManagerActivity.mTaskFilterClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_filter_processing);
        mCourseClass.mClassId = "processing";
        mTaskManagerActivity.mTaskFilterClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_filter_complete_all);
        mCourseClass.mClassId = "complete_all";
        mTaskManagerActivity.mTaskFilterClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_order_create_time);
        mCourseClass.mClassId = "create_time";
        mTaskManagerActivity.mTaskOrder = mCourseClass;
        mTaskManagerActivity.mTaskOrderClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_order_update_time);
        mCourseClass.mClassId = "update_time";
        mTaskManagerActivity.mTaskOrderClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_order_complete_time);
        mCourseClass.mClassId = "complete_time";
        mTaskManagerActivity.mTaskOrderClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_class_order_end_time);
        mCourseClass.mClassId = "end_time";
        mTaskManagerActivity.mTaskOrderClass.add(mCourseClass);

        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_structure_class_tag);
        mCourseClass.mClassId = "0";
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        mTaskManagerActivity.mTaskTag = mCourseClass;
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_pup);
        mCourseClass.mClassId = "1";
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_blu);
        mCourseClass.mClassId = "2";
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_yel);
        mCourseClass.mClassId = "3";
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_org);
        mCourseClass.mClassId = "4";
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mTaskManagerActivity.getString(R.string.task_tag_red);
        mCourseClass.mClassId = "5";
        mTaskManagerActivity.mTaskTagClass.add(mCourseClass);


    }

    public void doClass1() {

        if (mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK) != null) {
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.topLayer != null) {
                if (mFragment.showStatu == false) {
                    mFragment.txtStatuhens.setTextColor(Color.rgb(247, 114, 51));
                    mFragment.imgStatuhens.setImageResource(R.drawable.sniper1_s);
                    mFragment.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgPersonhens.setImageResource(R.drawable.sniper1);
                    mFragment.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgOrderhens.setImageResource(R.drawable.sniper1);
                    mFragment.txtTaghens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgTaghens.setImageResource(R.drawable.sniper1);
                    mFragment.mClassList.setAdapter(mTaskManagerActivity.mStatuAdapter);
                    mFragment.showPerson = false;
                    mFragment.showOrder = false;
                    mFragment.showStatu = true;
                    mFragment.showTag = false;
                    mFragment.mClassList.setOnItemClickListener(mFragment.statuListenter);
                    showTop();
                } else {
                    mFragment.showStatu = false;
                    mFragment.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.txtStatuhens.setText(mTaskManagerActivity.mTaskType.mCatName);
                    mFragment.imgStatuhens.setImageResource(R.drawable.sniper1);
                    hidTop();
                }
            }
        }
    }

    public void doClass2() {
        if (mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK) != null) {
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.topLayer != null) {
                if (mFragment.showPerson == false) {
                    mFragment.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgStatuhens.setImageResource(R.drawable.sniper1);
                    mFragment.txtPersonhens.setTextColor(Color.rgb(247, 114, 51));
                    mFragment.imgPersonhens.setImageResource(R.drawable.sniper1_s);
                    mFragment.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgOrderhens.setImageResource(R.drawable.sniper1);
                    mFragment.txtTaghens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgTaghens.setImageResource(R.drawable.sniper1);
                    mFragment.mClassList.setAdapter(mTaskManagerActivity.mPersonAdapter);
                    mFragment.showStatu = false;
                    mFragment.showOrder = false;
                    mFragment.showPerson = true;
                    mFragment.showTag = false;
                    mFragment.mClassList.setOnItemClickListener(mFragment.filterSelectListenter);
                    showTop();
                } else {
                    mFragment.showPerson = false;
                    mFragment.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.txtPersonhens.setText(mTaskManagerActivity.mTaskFilter.mCatName);
                    mFragment.imgPersonhens.setImageResource(R.drawable.sniper1);
                    hidTop();
                }
            }
        }
    }

    public void doClass3() {
        if (mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK) != null) {
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.topLayer != null) {
                if (mFragment.showOrder == false) {
                    mFragment.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgStatuhens.setImageResource(R.drawable.sniper1);
                    mFragment.txtOrderhens.setTextColor(Color.rgb(247, 114, 51));
                    mFragment.imgOrderhens.setImageResource(R.drawable.sniper1_s);
                    mFragment.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgPersonhens.setImageResource(R.drawable.sniper1);
                    mFragment.txtTaghens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgTaghens.setImageResource(R.drawable.sniper1);
                    mFragment.mClassList.setAdapter(mTaskManagerActivity.mOrderAdapter);
                    mFragment.showStatu = false;
                    mFragment.showPerson = false;
                    mFragment.showOrder = true;
                    mFragment.showTag = false;
                    mFragment.mClassList.setOnItemClickListener(mFragment.classOrderListenter);
                    showTop();
                } else {
                    mFragment.showOrder = false;
                    mFragment.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.txtOrderhens.setText(mTaskManagerActivity.mTaskOrder.mCatName);
                    mFragment.imgOrderhens.setImageResource(R.drawable.sniper1);
                    hidTop();
                }
            }
        }
    }

    public void doClass4() {
        if (mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK) != null) {
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.topLayer != null) {
                if (mFragment.showTag == false) {
                    mFragment.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgStatuhens.setImageResource(R.drawable.sniper1);
                    mFragment.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgOrderhens.setImageResource(R.drawable.sniper1_s);
                    mFragment.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.imgPersonhens.setImageResource(R.drawable.sniper1);
                    mFragment.txtTaghens.setTextColor(Color.rgb(247, 114, 51));
                    mFragment.imgTaghens.setImageResource(R.drawable.sniper1);
                    mFragment.mClassList.setAdapter(mTaskManagerActivity.mTagAdapter);
                    mFragment.showStatu = false;
                    mFragment.showPerson = false;
                    mFragment.showOrder = false;
                    mFragment.showTag = true;
                    mFragment.mClassList.setOnItemClickListener(mFragment.classTagListenter);
                    showTop();
                } else {
                    mFragment.showTag = false;
                    mFragment.txtTaghens.setTextColor(Color.rgb(98, 98, 98));
                    mFragment.txtTaghens.setText(mTaskManagerActivity.mTaskTag.mCatName);
                    mFragment.imgTaghens.setImageResource(R.drawable.sniper1);
                    hidTop();
                    String tag = "";
                    for (int i = 0; i < mTaskManagerActivity.mTaskTypeClass.size(); i++) {
                        if (mTaskManagerActivity.mTaskTypeClass.get(i).isSelect == true) {
                            if (tag.length() == 0) {
                                tag += mTaskManagerActivity.mTaskTypeClass.get(i).mClassId;
                            } else {
                                tag += "," + mTaskManagerActivity.mTaskTypeClass.get(i).mClassId;
                            }
                        }
                    }
                    if (!tag.equals(mTaskManagerActivity.tags)) {
                        TaskManager.getInstance().taskPage = 1;
                        TaskManager.getInstance().taskAll = false;
                        TaskManager.getInstance().mTasks.clear();
                        TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                                ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                                ,"",TaskManager.getInstance().taskPage);
                    }

                }
            }
        }
    }

    private void showTop() {
        if (mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK) != null) {
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.topLayer != null) {
                mFragment.topLayer.setVisibility(View.VISIBLE);
                mFragment.mShade.setVisibility(View.VISIBLE);
            }
        }

    }

    private void hidTop() {
        if (mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK) != null) {
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.topLayer != null) {
                mFragment.topLayer.setVisibility(View.INVISIBLE);
                mFragment.mShade.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void doSearchStatu(CourseClass mCourseClass) {

        if (!mTaskManagerActivity.mTaskType.mClassId.equals(mCourseClass.mClassId)) {
            falseAllClass1();
            mCourseClass.isSelect = true;
            mTaskManagerActivity.mTaskType = mCourseClass;
            mTaskManagerActivity.mStatuAdapter.notifyDataSetChanged();
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.mSearchViewLayout.ishow) {
                TaskManager.getInstance().taskSearchPage = 1;
                TaskManager.getInstance().taskSearchAll = false;
                TaskManager.getInstance().mSearchTasks.clear();
                onSearch(mFragment.mSearchViewLayout.getText());
            } else {
                TaskManager.getInstance().taskPage = 1;
                TaskManager.getInstance().taskAll = false;
                TaskManager.getInstance().mTasks.clear();
                TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                        ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                        ,"",TaskManager.getInstance().taskPage);
            }

        }
        doClass1();
    }

    public void doSearchFilter(CourseClass mCourseClass) {

        if (!mTaskManagerActivity.mTaskFilter.mClassId.equals(mCourseClass.mClassId)) {
            falseAllClass2();
            mCourseClass.isSelect = true;
            mTaskManagerActivity.mTaskFilter = mCourseClass;
            mTaskManagerActivity.mPersonAdapter.notifyDataSetChanged();
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.mSearchViewLayout.ishow) {
                TaskManager.getInstance().taskSearchPage = 1;
                TaskManager.getInstance().taskSearchAll = false;
                TaskManager.getInstance().mSearchTasks.clear();
                onSearch(mFragment.mSearchViewLayout.getText());
            } else {
                TaskManager.getInstance().taskPage = 1;
                TaskManager.getInstance().taskAll = false;
                TaskManager.getInstance().mTasks.clear();
                TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                        ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                        ,"",TaskManager.getInstance().taskPage);
            }
        }
        doClass2();
    }

    public void doOrder(CourseClass mCourseClass) {

        if (!mTaskManagerActivity.mTaskOrder.mClassId.equals(mCourseClass.mClassId)) {
            falseAllClass3();
            mCourseClass.isSelect = true;
            mTaskManagerActivity.mTaskOrder = mCourseClass;
            mTaskManagerActivity.mOrderAdapter.notifyDataSetChanged();
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mFragment.mSearchViewLayout.ishow) {
                TaskManager.getInstance().taskSearchPage = 1;
                TaskManager.getInstance().taskSearchAll = false;
                TaskManager.getInstance().mSearchTasks.clear();
                onSearch(mFragment.mSearchViewLayout.getText());
            } else {
                TaskManager.getInstance().taskPage = 1;
                TaskManager.getInstance().taskAll = false;
                TaskManager.getInstance().mTasks.clear();
                TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                        ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                        ,"",TaskManager.getInstance().taskPage);
            }
        }
        doClass3();
    }

    public void doTag(CourseClass mCourseClass) {
        if (mCourseClass.mClassId.equals("0")) {
            for (int i = 1; i < mTaskManagerActivity.mTaskTagClass.size(); i++) {
                mTaskManagerActivity.mTaskTagClass.get(i).isSelect = false;
            }
        } else {
            if (mCourseClass.isSelect == true) {
                mCourseClass.isSelect = false;
            } else {
                mCourseClass.isSelect = true;
            }
        }
        mTaskManagerActivity.mTagAdapter.notifyDataSetChanged();
    }


    public void doHidall() {
        if (mTaskManagerActivity.fragments.get(mTaskManagerActivity.PAGE_TASK) != null) {
            TaskFragment mFragment = (TaskFragment) mTaskManagerActivity.fragments.get(mTaskManagerActivity.PAGE_TASK);
            if (mFragment.topLayer != null) {
                mFragment.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
                mFragment.imgOrderhens.setImageResource(R.drawable.sniper);
                mFragment.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
                mFragment.imgPersonhens.setImageResource(R.drawable.sniper);
                mFragment.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
                mFragment.imgStatuhens.setImageResource(R.drawable.sniper);
                if (mFragment.showTag == true) {
                    String tag = "";
                    for (int i = 0; i < mTaskManagerActivity.mTaskTagClass.size(); i++) {
                        if (mTaskManagerActivity.mTaskTagClass.get(i).isSelect == true) {
                            if (tag.length() == 0) {
                                tag += mTaskManagerActivity.mTaskTagClass.get(i).mClassId;
                            } else {
                                tag += "," + mTaskManagerActivity.mTaskTagClass.get(i).mClassId;
                            }
                        }
                    }
                    if (!tag.equals(mTaskManagerActivity.tags)) {
                        TaskManager.getInstance().taskPage = 1;
                        TaskManager.getInstance().taskAll = false;
                        TaskManager.getInstance().mTasks.clear();
                        TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                                ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                                ,"",TaskManager.getInstance().taskPage);
                    }

                }
                mFragment.showStatu = false;
                mFragment.showPerson = false;
                mFragment.showOrder = false;
                mFragment.showTag = false;
                hidTop();
            }
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
            TaskManager.getInstance().mSearchTasks.clear();
            TaskManager.getInstance().taskSearchAll = false;
            TaskManager.getInstance().taskSearchPage = 1;
            TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                    ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                    ,word,TaskManager.getInstance().taskSearchPage);
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

//    public void getTask() {
//        if (TaskManager.getInstance().taskAll == false) {
//            String urlString = NetUtils.getInstance().createURLStringex(TaskManager.TASK_PATH, FunctionManager.TEST_HOST);
//            MultipartBody.Builder builder = new MultipartBody.Builder();
//            builder.setType(MultipartBody.FORM);
//            mTaskManagerActivity.tags = "";
//            for (int i = 0; i < mTaskManagerActivity.mTaskTagClass.size(); i++) {
//                if (mTaskManagerActivity.mTaskTagClass.get(i).isSelect == true) {
//                    if (mTaskManagerActivity.tags.length() == 0) {
//                        mTaskManagerActivity.tags += mTaskManagerActivity.mTaskTagClass.get(i).mClassId;
//                    } else {
//                        mTaskManagerActivity.tags += "," + mTaskManagerActivity.mTaskTagClass.get(i).mClassId;
//                    }
//                }
//            }
//            builder.addFormDataPart("method", TaskManager.TASK_LIST);
//            builder.addFormDataPart("user_id", InterskyApplication.mApp.mUser.getUsernRecordid());
//            builder.addFormDataPart("company_id", InterskyApplication.mApp.mUser.getCompanyID());
//            builder.addFormDataPart("notice_type", mTaskManagerActivity.mTaskType.mClassId);
//            builder.addFormDataPart("task_filter", mTaskManagerActivity.mTaskFilter.mClassId);
//            builder.addFormDataPart("order_type", mTaskManagerActivity.mTaskOrder.mOrderType);
//            builder.addFormDataPart("order_field", mTaskManagerActivity.mTaskOrder.mClassId);
//            builder.addFormDataPart("tag", mTaskManagerActivity.tags);
//            builder.addFormDataPart("page_no", String.valueOf(TaskManager.getInstance().taskPage));
//            builder.addFormDataPart("page_size", "20");
//            RequestBody formBody = builder.build();
//            PostNetTask mPostNetTask = new PostNetTask(urlString, mTaskManagerHandler, TaskManagerActivity.EVENT_GET_TASK_LIST_SUCCESS,
//                    TaskManagerActivity.EVENT_GET_TASK_LIST_FAIL, mTaskManagerActivity, formBody, "");
//            NetTaskManager.getInstance().addNetTask(mPostNetTask);
//            mTaskManagerActivity.waitDialog.show();
//        }
//
//    }


    public void setContent(int id) {
        mTaskManagerActivity.nowPage = id;
        mTaskManagerActivity.tabAdapter.onCheckedChanged(id);
        switch (mTaskManagerActivity.nowPage) {
            case TaskManagerActivity.PAGE_TASK:
                mTaskManagerActivity.lineProject.setVisibility(View.INVISIBLE);
                mTaskManagerActivity.lineTask.setVisibility(View.VISIBLE);
                ToolBarHelper.setTitle(mTaskManagerActivity.mActionBar, mTaskManagerActivity.getString(R.string.task_main_tasktitle));
                break;
            case TaskManagerActivity.PAGE_PROJECT:
                mTaskManagerActivity.lineProject.setVisibility(View.VISIBLE);
                mTaskManagerActivity.lineTask.setVisibility(View.INVISIBLE);
                ToolBarHelper.setTitle(mTaskManagerActivity.mActionBar, mTaskManagerActivity.getString(R.string.task_main_project));
                break;

        }

    }


    public void onFoot() {
        if (mTaskManagerActivity.nowPage == TaskManagerActivity.PAGE_TASK) {
            TaskFragment mTaskFragment = (TaskFragment) mTaskManagerActivity.fragments.get(TaskManagerActivity.PAGE_TASK);
            if (mTaskFragment.mPullToRefreshView != null) {
                mTaskFragment.mPullToRefreshView.onFooterRefreshComplete();
            }
            if(mTaskFragment.mSearchViewLayout.ishow)
            {
                TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                        ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                        ,mTaskFragment.mSearchViewLayout.getText(),TaskManager.getInstance().taskSearchPage);
            }
            else
            {
                TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                        ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                        ,"",TaskManager.getInstance().taskPage);
            }

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
            if (mTaskFragment.mSearchViewLayout.ishow == true) {
                TaskManager.getInstance().taskSearchPage = 1;
                TaskManager.getInstance().mSearchTasks.clear();
                TaskManager.getInstance().taskSearchAll = false;
                onSearch(mTaskFragment.mSearchViewLayout.getText());
            } else {
                TaskManager.getInstance().taskPage = 1;
                TaskManager.getInstance().mTasks.clear();
                TaskManager.getInstance().taskAll = false;
                TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                        ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                        ,"",TaskManager.getInstance().taskPage);
            }
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
            if (mTaskFragment.mSearchViewLayout.ishow == true) {
                TaskManager.getInstance().taskOldSearchPage = TaskManager.getInstance().taskSearchPage;
                TaskManager.getInstance().taskSearchPage = 1;
                TaskManager.getInstance().mSearchTasks.clear();
                TaskManager.getInstance().taskSearchAll = false;
                onSearch(mTaskFragment.mSearchViewLayout.getText());
            } else {
                TaskManager.getInstance().taskOldPage = TaskManager.getInstance().taskPage;
                TaskManager.getInstance().taskPage = 1;
                TaskManager.getInstance().mTasks.clear();
                TaskManager.getInstance().taskAll = false;
                TaskAsks.getTask(mTaskManagerActivity,mTaskManagerHandler,"",mTaskManagerActivity.tags,mTaskManagerActivity.mTaskType.mClassId
                        ,mTaskManagerActivity.mTaskFilter.mClassId,mTaskManagerActivity.mTaskOrder.mOrderType,mTaskManagerActivity.mTaskOrder.mClassId
                        ,"",TaskManager.getInstance().taskPage);
            }
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
}
