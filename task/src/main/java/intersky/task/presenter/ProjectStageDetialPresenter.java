package intersky.task.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.Presenter;
import intersky.apputils.TimeUtils;
import intersky.mywidget.PullToRefreshView;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.CourseClass;
import intersky.task.entity.Project;
import intersky.task.entity.Stage;
import intersky.task.entity.Task;
import intersky.task.handler.ProjectStageDetialHandler;
import intersky.task.receiver.ProjectStageDetialReceiver;
import intersky.task.view.activity.ProjectDetialActivity;
import intersky.task.view.activity.ProjectStageDetialActivity;
import intersky.task.view.activity.ProjectStageViewActivity;
import intersky.task.view.activity.ProjectSysActivity;
import intersky.task.view.activity.TaskCreatActivity;
import intersky.task.view.activity.TaskDetialActivity;
import intersky.task.view.adapter.ClassAdapter;
import intersky.task.view.adapter.TagClassAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectStageDetialPresenter implements Presenter {

    public ProjectStageDetialHandler mProjectStageDetialHandler;
    public ProjectStageDetialActivity mProjectStageDetialActivity;

    public ProjectStageDetialPresenter(ProjectStageDetialActivity mProjectStageDetialActivity) {
        this.mProjectStageDetialActivity = mProjectStageDetialActivity;
        this.mProjectStageDetialHandler = new ProjectStageDetialHandler(mProjectStageDetialActivity);
        mProjectStageDetialActivity.setBaseReceiver(new ProjectStageDetialReceiver(mProjectStageDetialHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mProjectStageDetialActivity.setContentView(R.layout.activity_project_task_detial);
        ImageView back = mProjectStageDetialActivity.findViewById(R.id.back);
        back.setOnClickListener(mProjectStageDetialActivity.mBackListener);


        mProjectStageDetialActivity.mPullToRefreshView = (PullToRefreshView) mProjectStageDetialActivity.findViewById(R.id.mail_pull_refresh_view);
        mProjectStageDetialActivity.mPullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mProjectStageDetialActivity.mPullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mProjectStageDetialActivity.mPullToRefreshView.setOnHeaderRefreshListener(mProjectStageDetialActivity);
        mProjectStageDetialActivity.mPullToRefreshView.setOnFooterRefreshListener(mProjectStageDetialActivity);
        initClass();
        mProjectStageDetialActivity.templateView = (RelativeLayout) mProjectStageDetialActivity.findViewById(R.id.taskview);
        mProjectStageDetialActivity.stageArea = (LinearLayout) mProjectStageDetialActivity.findViewById(R.id.stage_detial_layer);
        mProjectStageDetialActivity.btnStatuhensive = (RelativeLayout) mProjectStageDetialActivity.findViewById(R.id.statu_layer_head);
        mProjectStageDetialActivity.txtStatuhens = (TextView) mProjectStageDetialActivity.findViewById(R.id.statu_head_title);
        mProjectStageDetialActivity.sysView = (RelativeLayout) mProjectStageDetialActivity.findViewById(R.id.sysview);
        mProjectStageDetialActivity.txtStatuhens.setText(mProjectStageDetialActivity.getString(R.string.task_structure_class_tag));
        mProjectStageDetialActivity.imgStatuhens = (ImageView) mProjectStageDetialActivity.findViewById(R.id.statu_img);
        mProjectStageDetialActivity.btnPersonhensive = (RelativeLayout) mProjectStageDetialActivity.findViewById(R.id.person_layer_head);
        mProjectStageDetialActivity.txtPersonhens = (TextView) mProjectStageDetialActivity.findViewById(R.id.person_head_title);
        mProjectStageDetialActivity.txtPersonhens.setText(mProjectStageDetialActivity.mTaskFilter.mCatName);
        mProjectStageDetialActivity.imgPersonhens = (ImageView) mProjectStageDetialActivity.findViewById(R.id.person_img);
        mProjectStageDetialActivity.btnOrderhensive = (RelativeLayout) mProjectStageDetialActivity.findViewById(R.id.select_layer_head);
        mProjectStageDetialActivity.txtOrderhens = (TextView) mProjectStageDetialActivity.findViewById(R.id.select_head_title);
        mProjectStageDetialActivity.txtOrderhens.setText(mProjectStageDetialActivity.mTaskOrder.mCatName);
        mProjectStageDetialActivity.imgOrderhens = (ImageView) mProjectStageDetialActivity.findViewById(R.id.select_img);
        mProjectStageDetialActivity.mShade = (RelativeLayout) mProjectStageDetialActivity.findViewById(R.id.shade);
        mProjectStageDetialActivity.mClassList = (ListView) mProjectStageDetialActivity.findViewById(R.id.class_list);
        mProjectStageDetialActivity.topLayer = (LinearLayout) mProjectStageDetialActivity.findViewById(R.id.classlistlayer);
        mProjectStageDetialActivity.mProject = mProjectStageDetialActivity.getIntent().getParcelableExtra("project");
        mProjectStageDetialActivity.mStatuAdapter = new TagClassAdapter(mProjectStageDetialActivity, mProjectStageDetialActivity.mTaskTypeClass);
        mProjectStageDetialActivity.mPersonAdapter = new ClassAdapter(mProjectStageDetialActivity, mProjectStageDetialActivity.mTaskFilterClass);
        mProjectStageDetialActivity.mOrderAdapter = new ClassAdapter(mProjectStageDetialActivity, mProjectStageDetialActivity.mTaskOrderClass);
        mProjectStageDetialActivity.btnStatuhensive.setOnClickListener(mProjectStageDetialActivity.mShowClassLinstener1);
        mProjectStageDetialActivity.btnPersonhensive.setOnClickListener(mProjectStageDetialActivity.mShowClassLinstener2);
        mProjectStageDetialActivity.btnOrderhensive.setOnClickListener(mProjectStageDetialActivity.mShowClassLinstener3);
        mProjectStageDetialActivity.mShade.setOnClickListener(mProjectStageDetialActivity.shadeClick);
        ProjectAsks.getStage(mProjectStageDetialActivity, mProjectStageDetialHandler,mProjectStageDetialActivity.mProject);
        mProjectStageDetialActivity.templateView.setOnClickListener(mProjectStageDetialActivity.startTemplateView);
        mProjectStageDetialActivity.sysView.setOnClickListener(mProjectStageDetialActivity.startSysView);
        TextView title = mProjectStageDetialActivity.findViewById(R.id.title);
        ImageView add = mProjectStageDetialActivity.findViewById(R.id.add);
        ImageView detial = mProjectStageDetialActivity.findViewById(R.id.detial);
        title.setText(mProjectStageDetialActivity.mProject.mName);
        add.setOnClickListener(mProjectStageDetialActivity.newTask);
        detial.setOnClickListener(mProjectStageDetialActivity.showDetial);
    }

    public void initClass() {

        CourseClass mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_structure_class_tag);
        mCourseClass.mClassId = "0";
        mProjectStageDetialActivity.mTaskTypeClass.add(mCourseClass);
        mProjectStageDetialActivity.mTaskType = mCourseClass;
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_tag_pup);
        mCourseClass.mClassId = "1";
        mProjectStageDetialActivity.mTaskTypeClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_tag_blu);
        mCourseClass.mClassId = "2";
        mProjectStageDetialActivity.mTaskTypeClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_tag_yel);
        mCourseClass.mClassId = "3";
        mProjectStageDetialActivity.mTaskTypeClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_tag_org);
        mCourseClass.mClassId = "4";
        mProjectStageDetialActivity.mTaskTypeClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_tag_red);
        mCourseClass.mClassId = "5";
        mProjectStageDetialActivity.mTaskTypeClass.add(mCourseClass);

        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_filter_all);
        mCourseClass.mClassId = "all";
        mCourseClass.isSelect = true;
        mProjectStageDetialActivity.mTaskFilter = mCourseClass;
        mProjectStageDetialActivity.mTaskFilterClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_filter_processing);
        mCourseClass.mClassId = "processing";
        mProjectStageDetialActivity.mTaskFilterClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_filter_complete_all);
        mCourseClass.mClassId = "complete_all";
        mProjectStageDetialActivity.mTaskFilterClass.add(mCourseClass);

        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_order_task_id);
        mCourseClass.mClassId = "task_id";
        mCourseClass.mOrderType = "asc";
        mCourseClass.isSelect = true;
        mProjectStageDetialActivity.mTaskOrder = mCourseClass;
        mProjectStageDetialActivity.mTaskOrderClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_order_end_time);
        mCourseClass.mClassId = "end_time";
        mProjectStageDetialActivity.mTaskOrderClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_order_update_time);
        mCourseClass.mClassId = "update_time";
        mProjectStageDetialActivity.mTaskOrderClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_order_name);
        mCourseClass.mOrderType = "asc";
        mCourseClass.mClassId = "name";
        mProjectStageDetialActivity.mTaskOrderClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_order_create_time);
        mCourseClass.mClassId = "create_time";
        mProjectStageDetialActivity.mTaskOrderClass.add(mCourseClass);
        mCourseClass = new CourseClass();
        mCourseClass.mCatName = mProjectStageDetialActivity.getString(R.string.task_class_order_complete_time);
        mCourseClass.mClassId = "complete_time";
        mProjectStageDetialActivity.mTaskOrderClass.add(mCourseClass);
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

    public void doClass1() {

        if (mProjectStageDetialActivity.showStatu == false) {
            mProjectStageDetialActivity.txtStatuhens.setTextColor(Color.rgb(247, 114, 51));
            mProjectStageDetialActivity.imgStatuhens.setImageResource(R.drawable.sniper1_s);
            mProjectStageDetialActivity.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.imgPersonhens.setImageResource(R.drawable.sniper1);
            mProjectStageDetialActivity.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.imgOrderhens.setImageResource(R.drawable.sniper1);
            mProjectStageDetialActivity.mClassList.setAdapter(mProjectStageDetialActivity.mStatuAdapter);
            mProjectStageDetialActivity.showPerson = false;
            mProjectStageDetialActivity.showOrder = false;
            mProjectStageDetialActivity.showStatu = true;
            mProjectStageDetialActivity.mClassList.setOnItemClickListener(mProjectStageDetialActivity.statuListenter);
            showTop();
        } else {
            mProjectStageDetialActivity.showStatu = false;
            mProjectStageDetialActivity.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.imgStatuhens.setImageResource(R.drawable.sniper1);
            hidTop();
            String tag = "";
            for (int i = 0; i < mProjectStageDetialActivity.mTaskTypeClass.size(); i++) {
                if (mProjectStageDetialActivity.mTaskTypeClass.get(i).isSelect == true) {
                    if (tag.length() == 0) {
                        tag += mProjectStageDetialActivity.mTaskTypeClass.get(i).mClassId;
                    } else {
                        tag += "," + mProjectStageDetialActivity.mTaskTypeClass.get(i).mClassId;
                    }
                }
            }
            if (!tag.equals(mProjectStageDetialActivity.tags))
            {
                getTask();
            }
        }

    }

    public void doClass2() {
        if (mProjectStageDetialActivity.showPerson == false) {
            mProjectStageDetialActivity.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.imgStatuhens.setImageResource(R.drawable.sniper1);
            mProjectStageDetialActivity.txtPersonhens.setTextColor(Color.rgb(247, 114, 51));
            mProjectStageDetialActivity.imgPersonhens.setImageResource(R.drawable.sniper1_s);
            mProjectStageDetialActivity.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.imgOrderhens.setImageResource(R.drawable.sniper1);
            mProjectStageDetialActivity.mClassList.setAdapter(mProjectStageDetialActivity.mPersonAdapter);
            mProjectStageDetialActivity.showStatu = false;
            mProjectStageDetialActivity.showOrder = false;
            mProjectStageDetialActivity.showPerson = true;
            mProjectStageDetialActivity.mClassList.setOnItemClickListener(mProjectStageDetialActivity.filterSelectListenter);
            showTop();
        } else {
            mProjectStageDetialActivity.showPerson = false;
            mProjectStageDetialActivity.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.txtPersonhens.setText(mProjectStageDetialActivity.mTaskFilter.mCatName);
            mProjectStageDetialActivity.imgPersonhens.setImageResource(R.drawable.sniper1);
            hidTop();
        }

    }

    public void doClass3() {

        if (mProjectStageDetialActivity.showOrder == false) {
            mProjectStageDetialActivity.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.imgStatuhens.setImageResource(R.drawable.sniper1);
            mProjectStageDetialActivity.txtOrderhens.setTextColor(Color.rgb(247, 114, 51));
            mProjectStageDetialActivity.imgOrderhens.setImageResource(R.drawable.sniper1_s);
            mProjectStageDetialActivity.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.imgPersonhens.setImageResource(R.drawable.sniper1);
            mProjectStageDetialActivity.mClassList.setAdapter(mProjectStageDetialActivity.mOrderAdapter);
            mProjectStageDetialActivity.showStatu = false;
            mProjectStageDetialActivity.showPerson = false;
            mProjectStageDetialActivity.showOrder = true;
            mProjectStageDetialActivity.mClassList.setOnItemClickListener(mProjectStageDetialActivity.classOrderListenter);
            showTop();
        } else {
            mProjectStageDetialActivity.showOrder = false;
            mProjectStageDetialActivity.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
            mProjectStageDetialActivity.txtOrderhens.setText(mProjectStageDetialActivity.mTaskOrder.mCatName);
            mProjectStageDetialActivity.imgOrderhens.setImageResource(R.drawable.sniper1);
            hidTop();
        }

    }

    private void showTop() {

        mProjectStageDetialActivity.topLayer.setVisibility(View.VISIBLE);
        mProjectStageDetialActivity.mShade.setVisibility(View.VISIBLE);

    }

    private void hidTop() {

        mProjectStageDetialActivity.topLayer.setVisibility(View.INVISIBLE);
        mProjectStageDetialActivity.mShade.setVisibility(View.INVISIBLE);

    }

    public void doSearchStatu(CourseClass mCourseClass) {

        if (mCourseClass.mClassId.equals("0")) {
            for (int i = 1; i < mProjectStageDetialActivity.mTaskTypeClass.size(); i++) {
                mProjectStageDetialActivity.mTaskTypeClass.get(i).isSelect = false;
            }
        } else {
            if (mCourseClass.isSelect == true) {
                mCourseClass.isSelect = false;
            } else {
                mCourseClass.isSelect = true;
            }
        }
        mProjectStageDetialActivity.mStatuAdapter.notifyDataSetChanged();
    }

    public void doSearchFilter(CourseClass mCourseClass) {

        if (!mProjectStageDetialActivity.mTaskFilter.mClassId.equals(mCourseClass.mClassId)) {
            falseAllClass2();
            mCourseClass.isSelect = true;
            mProjectStageDetialActivity.mTaskFilter = mCourseClass;
            mProjectStageDetialActivity.mPersonAdapter.notifyDataSetChanged();
            getTask();
        }
        doClass2();
    }

    public void doOrder(CourseClass mCourseClass) {

        if (!mProjectStageDetialActivity.mTaskOrder.mClassId.equals(mCourseClass.mClassId)) {
            falseAllClass3();
            mCourseClass.isSelect = true;
            mProjectStageDetialActivity.mTaskOrder = mCourseClass;
            mProjectStageDetialActivity.mOrderAdapter.notifyDataSetChanged();
            getTask();
        }
        doClass3();
    }


    public void getTask() {
        for(int i = 0 ; i < mProjectStageDetialActivity.mProject.mStages.size() ; i++)
        {
            for(int j = 0 ; j < mProjectStageDetialActivity.mProject.mStages.get(i).mTasks.size() ; j++)
            {
                mProjectStageDetialActivity.mProject.mStages.get(i).mTasks.clear();
            }
        }
        TaskAsks.getTask(mProjectStageDetialActivity, mProjectStageDetialHandler,mProjectStageDetialActivity.mProject.projectId,
                mProjectStageDetialActivity.tags,"0",mProjectStageDetialActivity.mTaskFilter.mClassId,mProjectStageDetialActivity.mTaskOrder.mOrderType,mProjectStageDetialActivity.mTaskOrder.mClassId,"",0);
    }

    public void falseAllClass1() {
        for (int i = 0; i < mProjectStageDetialActivity.mTaskTypeClass.size(); i++) {
            mProjectStageDetialActivity.mTaskTypeClass.get(i).isSelect = false;
        }
    }

    public void falseAllClass2() {
        for (int i = 0; i < mProjectStageDetialActivity.mTaskFilterClass.size(); i++) {
            mProjectStageDetialActivity.mTaskFilterClass.get(i).isSelect = false;
        }
    }

    public void falseAllClass3() {
        for (int i = 0; i < mProjectStageDetialActivity.mTaskOrderClass.size(); i++) {
            mProjectStageDetialActivity.mTaskOrderClass.get(i).isSelect = false;
        }
    }

    public void startTask(Task task) {
        Intent intent = new Intent(mProjectStageDetialActivity, TaskDetialActivity.class);
        intent.putExtra("project", mProjectStageDetialActivity.mProject);
        intent.putExtra("task", task);
        mProjectStageDetialActivity.startActivity(intent);
    }

    public void startTemplateView() {
        Intent intent = new Intent(mProjectStageDetialActivity, ProjectStageViewActivity.class);
        intent.putExtra("project", mProjectStageDetialActivity.mProject);
        intent.putExtra("tags", mProjectStageDetialActivity.tags);
        intent.putExtra("item1", mProjectStageDetialActivity.mTaskFilter.mClassId);
        intent.putExtra("item2", mProjectStageDetialActivity.mTaskOrder.mOrderType);
        intent.putExtra("item3", mProjectStageDetialActivity.mTaskOrder.mClassId);
        mProjectStageDetialActivity.startActivity(intent);
    }

    public void startSysView() {
        Intent intent = new Intent(mProjectStageDetialActivity, ProjectSysActivity.class);
        intent.putExtra("projectid", mProjectStageDetialActivity.mProject.projectId);
        intent.putExtra("projectname", mProjectStageDetialActivity.mProject.mName);
        mProjectStageDetialActivity.startActivity(intent);
    }

    public void doHidall() {
        mProjectStageDetialActivity.txtOrderhens.setTextColor(Color.rgb(98, 98, 98));
        mProjectStageDetialActivity.imgOrderhens.setImageResource(R.drawable.sniper);
        mProjectStageDetialActivity.txtPersonhens.setTextColor(Color.rgb(98, 98, 98));
        mProjectStageDetialActivity.imgPersonhens.setImageResource(R.drawable.sniper);
        mProjectStageDetialActivity.txtStatuhens.setTextColor(Color.rgb(98, 98, 98));
        mProjectStageDetialActivity.imgStatuhens.setImageResource(R.drawable.sniper);
        if (mProjectStageDetialActivity.showStatu == true) {
            String tag = "";
            for (int i = 0; i < mProjectStageDetialActivity.mTaskTypeClass.size(); i++) {
                if (mProjectStageDetialActivity.mTaskTypeClass.get(i).isSelect == true) {
                    if (tag.length() == 0) {
                        tag += mProjectStageDetialActivity.mTaskTypeClass.get(i).mClassId;
                    } else {
                        tag += "," + mProjectStageDetialActivity.mTaskTypeClass.get(i).mClassId;
                    }
                }
            }
            if (!tag.equals(mProjectStageDetialActivity.tags))
            {
                getTask();
            }
        }
        mProjectStageDetialActivity.showStatu = false;
        mProjectStageDetialActivity.showPerson = false;
        mProjectStageDetialActivity.showOrder = false;
        hidTop();

    }

    public void initStageView() {
        mProjectStageDetialActivity.stageArea.removeAllViews();
        for(int i = 0 ; i < mProjectStageDetialActivity.mProject.mStages.size() ; i++) {
            Stage Stage = mProjectStageDetialActivity.mProject.mStages.get(i);
            addStageView(Stage);
        }
    }

    public void initTaskView() {
        for(int i = 0 ; i < mProjectStageDetialActivity.mProject.mStages.size() ; i++)
        {
            for(int j = 0 ; j < mProjectStageDetialActivity.mProject.mStages.get(i).mTasks.size() ; j++ )
            {
                addStageTaskView(mProjectStageDetialActivity.mProject.mStages.get(i)
                        , mProjectStageDetialActivity.mProject.mStages.get(i).mTasks.get(j));
            }
        }
    }


    public void addStageView(Stage mStage) {
        View view = mProjectStageDetialActivity.getLayoutInflater().inflate(R.layout.stage_view, null);
        TextView tiel = (TextView) view.findViewById(R.id.stage_title);
        tiel.setText(mStage.name);
        mStage.mDetialview = view;
        mProjectStageDetialActivity.stageArea.addView(view);
    }

    public void addStageTaskView(Stage mStage, Task task) {
        View view = mStage.mDetialview;
        if (view != null) {
            LinearLayout taskarea = (LinearLayout) view.findViewById(R.id.tasklayer);
            View taskview = mProjectStageDetialActivity.getLayoutInflater().inflate(R.layout.stage_task_view, null);
            TextView title = (TextView) taskview.findViewById(R.id.stage_title);
            title.setText(task.taskName);
            if (task.isComplete == 1) {
//                title.setTextColor(Color.rgb(140, 140, 140));
                title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
//                title.setTextColor(Color.rgb(0, 0, 0));
            }
            task.view = taskview;
            taskview.setTag(task);
            taskview.setOnClickListener(mProjectStageDetialActivity.startTaskDetial);


            TextView mtext;
            mtext = (TextView) taskview.findViewById(R.id.basetag);
            mtext.setVisibility(View.GONE);
            mtext = (TextView) taskview.findViewById(R.id.tag_1);
            mtext.setVisibility(View.GONE);
            mtext = (TextView) taskview.findViewById(R.id.tag_2);
            mtext.setVisibility(View.GONE);
            mtext = (TextView) taskview.findViewById(R.id.tag_3);
            mtext.setVisibility(View.GONE);
            mtext = (TextView) taskview.findViewById(R.id.tag_4);
            mtext.setVisibility(View.GONE);
            mtext = (TextView) taskview.findViewById(R.id.tag_5);
            mtext.setVisibility(View.GONE);
            mtext = (TextView) taskview.findViewById(R.id.basetag2);
            mtext.setVisibility(View.GONE);


            if (task.tag.length() > 0) {
                String[] str = task.tag.split(",");
                for (int i = 0; i < str.length; i++) {
                    if (i == 0)
                        TaskManager.showView(taskview, str[i], true);
                    else
                        TaskManager.showView(taskview, str[i], false);
                }
            }

            TextView dete = (TextView) taskview.findViewById(R.id.taskdete);
            if (task.endTime.length() > 0) {
                dete.setVisibility(View.VISIBLE);
                if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), task.endTime) >= 0) {
                    if (task.isComplete == 1) {
                        dete.setTextColor(Color.rgb(76, 175, 80));
                        dete.setText(mProjectStageDetialActivity.getString(R.string.task_finish_nomal));
                    } else {
                        if(task.beginTime.length() > 0)
                        {
                            if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), task.beginTime) > 0) {
                                dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.beginTime) + "~" + TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.endTime));
                                dete.setTextColor(Color.rgb(118, 118, 118));
                            }
                            else
                            {
                                dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.beginTime) + "~" + TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.endTime));
                                dete.setTextColor(Color.rgb(118, 118, 118));
                            }
                        }
                        else
                        {
                            dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.endTime)+mProjectStageDetialActivity.getString(R.string.task_date_end));
                            dete.setTextColor(Color.rgb(118, 118, 118));
                        }

                    }
                } else {
                    int day = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), task.endTime)) / 24;
                    int hour = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), task.endTime)) % 24;
                    String text;
                    if (day == 0) {
                        if(hour == 0)
                        {
                            hour = 1;
                        }
                        text = String.valueOf(hour) + mProjectStageDetialActivity.getString(R.string.hour);
                    } else if (hour == 0) {
                        text = String.valueOf(day) + mProjectStageDetialActivity.getString(R.string.day);
                    } else {
                        text = String.valueOf(day) + mProjectStageDetialActivity.getString(R.string.hour) + String.valueOf(hour) + mProjectStageDetialActivity.getString(R.string.hour);
                    }
                    dete.setTextColor(Color.rgb(233, 79, 79));
                    if (task.isComplete == 1) {
                        dete.setText(mProjectStageDetialActivity.getString(R.string.task_beyond)  + mProjectStageDetialActivity.getString(R.string.button_word_finish));
                    } else {
                        dete.setText(mProjectStageDetialActivity.getString(R.string.task_beyond_1) + text);
                    }
                }
            } else {
                if (task.beginTime.length() > 0 && task.isComplete == 0) {
                    dete.setVisibility(View.VISIBLE);
                    if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), task.beginTime) > 0) {
                        dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.beginTime)  +mProjectStageDetialActivity.getString(R.string.task_date_start));
                        dete.setTextColor(Color.rgb(118, 118, 118));
                    }
                    else
                    {
                        dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.beginTime)+mProjectStageDetialActivity.getString(R.string.task_date_start));
                        dete.setTextColor(Color.rgb(118, 118, 118));
                    }
                } else if (task.isComplete == 1) {
                    dete.setVisibility(View.VISIBLE);
                    dete.setTextColor(Color.rgb(76, 175, 80));
                    dete.setText(mProjectStageDetialActivity.getString(R.string.task_finish_nomal));
                } else {
                    dete.setVisibility(View.GONE);
                }
            }
            TextView taskcount = (TextView) taskview.findViewById(R.id.task_son_count);
            ImageView imagetask = (ImageView) taskview.findViewById(R.id.task_son);
            TextView listcount = (TextView) taskview.findViewById(R.id.task_list_count);
            ImageView imagelist = (ImageView) taskview.findViewById(R.id.task_list);
            if(task.taskcount > 0)
            {
                taskcount.setVisibility(View.VISIBLE);
                imagetask.setVisibility(View.VISIBLE);
                taskcount.setText(String.valueOf(task.taskfinish)+"/"+String.valueOf(task.taskcount));
            }else
            {
                taskcount.setVisibility(View.GONE);
                imagetask.setVisibility(View.GONE);
            }
            if(task.listcount > 0)
            {
                listcount.setVisibility(View.VISIBLE);
                imagelist.setVisibility(View.VISIBLE);
                listcount.setText(String.valueOf(task.listfinish)+"/"+String.valueOf(task.listcount));
            }else
            {

                listcount.setVisibility(View.GONE);
                imagelist.setVisibility(View.GONE);
            }

            ImageView son = (ImageView) taskview.findViewById(R.id.more);
            RelativeLayout sonarea = (RelativeLayout) taskview.findViewById(R.id.morearea);
            sonarea.setOnClickListener(mProjectStageDetialActivity.doExpendListener);
            sonarea.setTag(task);
            if (task.tasks.size() > 0) {
                son.setVisibility(View.VISIBLE);
                if (task.expend == true) {

                    son.setImageResource(R.drawable.ntask_nodea);
                    for(int i = 0 ; i < task.tasks.size() ; i++)
                    {
                        addSonTaskView(task,task.tasks.get(i));
                    }

                } else {
                    son.setImageResource(R.drawable.ntask_nodeadd);
                }
            } else {
                LinearLayout detal = (LinearLayout) taskview.findViewById(R.id.iform);
                detal.setVisibility(View.GONE);
                son.setVisibility(View.INVISIBLE);
            }

            taskarea.addView(taskview);
        }
    }

    public void addSonTaskView(Task taskroot, Task task) {
        View view = taskroot.view;
        if (view != null) {
            LinearLayout taskarea = (LinearLayout) view.findViewById(R.id.sontaskarea);
            View taskview = mProjectStageDetialActivity.getLayoutInflater().inflate(R.layout.stage_task_view, null);
            TextView title = (TextView) taskview.findViewById(R.id.stage_title);
            title.setText(task.taskName);
            if (task.isComplete == 1) {
//                title.setTextColor(Color.rgb(140, 140, 140));
                title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
//                title.setTextColor(Color.rgb(0, 0, 0));
            }
            task.view = taskview;
            taskview.setTag(task);
            taskview.setOnClickListener(mProjectStageDetialActivity.startTaskDetial);

            TextView dete = (TextView) taskview.findViewById(R.id.taskdete);
            if (task.endTime.length() > 0) {
                dete.setVisibility(View.VISIBLE);
                if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), task.endTime) >= 0) {
                    if (task.isComplete == 1) {
                        dete.setTextColor(Color.rgb(76, 175, 80));
                        dete.setText(mProjectStageDetialActivity.getString(R.string.task_finish_nomal));
                    } else {
                        if(task.beginTime.length() > 0)
                        {
                            if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), task.beginTime) > 0) {
                                dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.beginTime) + "~" + TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.endTime));
                                dete.setTextColor(Color.rgb(187, 187, 187));
                            }
                            else
                            {
                                dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.beginTime) + "~" + TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.endTime));
                                dete.setTextColor(Color.rgb(187, 187, 187));
                            }
                        }
                        else
                        {
                            dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.endTime)+mProjectStageDetialActivity.getString(R.string.task_date_end));
                            dete.setTextColor(Color.rgb(187, 187, 187));
                        }

                    }
                } else {
                    int day = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), task.endTime)) / 24;
                    int hour = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), task.endTime)) % 24;
                    String text;
                    if (day == 0) {
                        if(hour == 0)
                        {
                            hour = 1;
                        }
                        text = String.valueOf(hour) + mProjectStageDetialActivity.getString(R.string.hour);
                    } else if (hour == 0) {
                        text = String.valueOf(day) + mProjectStageDetialActivity.getString(R.string.hour);
                    } else {
                        text = String.valueOf(day) + mProjectStageDetialActivity.getString(R.string.hour) + String.valueOf(hour) + mProjectStageDetialActivity.getString(R.string.hour);
                    }
                    dete.setTextColor(Color.rgb(233, 79, 79));
                    if (task.isComplete == 1) {
                        dete.setText(mProjectStageDetialActivity.getString(R.string.task_beyond)  + mProjectStageDetialActivity.getString(R.string.button_word_finish));
                    } else {
                        dete.setText(mProjectStageDetialActivity.getString(R.string.task_beyond_1) + text);
                    }
                }
            } else {
                if (task.beginTime.length() > 0 && task.isComplete == 0) {
                    dete.setVisibility(View.VISIBLE);
                    if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), task.beginTime) > 0) {
                        dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.beginTime)  +mProjectStageDetialActivity.getString(R.string.task_date_start));
                        dete.setTextColor(Color.rgb(187, 187, 187));
                    }
                    else
                    {
                        dete.setText(TimeUtils.praseTaskItemdata(mProjectStageDetialActivity,task.beginTime)+mProjectStageDetialActivity.getString(R.string.task_date_start));
                        dete.setTextColor(Color.rgb(187, 187, 187));
                    }
                } else if (task.isComplete == 1) {
                    dete.setVisibility(View.VISIBLE);
                    dete.setTextColor(Color.rgb(76, 175, 80));
                    dete.setText(mProjectStageDetialActivity.getString(R.string.task_finish_nomal));
                } else {
                    dete.setVisibility(View.GONE);
                }
            }
            TextView taskcount = (TextView) taskview.findViewById(R.id.task_son_count);
            ImageView imagetask = (ImageView) taskview.findViewById(R.id.task_son);
            TextView listcount = (TextView) taskview.findViewById(R.id.task_list_count);
            ImageView imagelist = (ImageView) taskview.findViewById(R.id.task_list);
            if(task.taskcount > 0)
            {
                taskcount.setVisibility(View.VISIBLE);
                imagetask.setVisibility(View.VISIBLE);
                taskcount.setText(String.valueOf(task.taskfinish)+"/"+String.valueOf(task.taskcount));
            }else
            {
                taskcount.setVisibility(View.GONE);
                imagetask.setVisibility(View.GONE);
            }
            if(task.listcount > 0)
            {
                listcount.setVisibility(View.VISIBLE);
                imagelist.setVisibility(View.VISIBLE);
                listcount.setText(String.valueOf(task.listfinish)+"/"+String.valueOf(task.listcount));
            }else
            {

                listcount.setVisibility(View.GONE);
                imagelist.setVisibility(View.GONE);
            }

            RelativeLayout line = (RelativeLayout) taskview.findViewById(R.id.line);
            line.setVisibility(View.INVISIBLE);
            ImageView son = (ImageView) taskview.findViewById(R.id.more);
            son.setOnClickListener(mProjectStageDetialActivity.doExpendListener);
            son.setTag(task);
            if (task.tasks.size() > 0) {
                son.setVisibility(View.VISIBLE);
                if (task.expend == true) {

                    son.setImageResource(R.drawable.ntask_nodea);
                    for(int i = 0 ; i < task.tasks.size() ; i++)
                    {
                        addSonTaskView(task,task.tasks.get(i));
                    }

                } else {
                    son.setImageResource(R.drawable.ntask_nodeadd);
                }
            } else {
                LinearLayout detal = (LinearLayout) taskview.findViewById(R.id.iform);
                detal.setVisibility(View.GONE);
                son.setVisibility(View.INVISIBLE);
            }
            taskarea.addView(taskview);
        }
    }

    public void doexPend(Task task) {
        if (task.expend == false) {
            if (task.view != null) {
                task.expend = true;


                for (int i = 0; i < task.tasks.size(); i++) {
                    addSonTaskView(task, task.tasks.get(i));
                }
                ImageView more = (ImageView) task.view.findViewById(R.id.more);
                more.setImageResource(R.drawable.ntask_nodea);
            }
        } else {

            if(mProjectStageDetialActivity.expentTask.containsKey(task.taskId)) {
                mProjectStageDetialActivity.expentTask.remove(task.taskId);
            }
            if (task.view != null) {
                LinearLayout taskarea = (LinearLayout) task.view.findViewById(R.id.sontaskarea);
                taskarea.removeAllViews();
                task.expend = false;
                ImageView more = (ImageView) task.view.findViewById(R.id.more);
                more.setImageResource(R.drawable.ntask_nodeadd);
            }
        }
    }

    public void newTask()
    {
        Intent intent = new Intent(mProjectStageDetialActivity, TaskCreatActivity.class);
        intent.putExtra("project", mProjectStageDetialActivity.mProject);
        Task task = new Task();
        task.leaderId = TaskManager.getInstance().oaUtils.mAccount.mRecordId;
        task.projectId = mProjectStageDetialActivity.mProject.projectId;
        intent.putExtra("task",task);
        mProjectStageDetialActivity.startActivity(intent);
    }

    public void showDetial()
    {
        Intent intent = new Intent(mProjectStageDetialActivity, ProjectDetialActivity.class);
        intent.putExtra("project", mProjectStageDetialActivity.mProject);
        mProjectStageDetialActivity.startActivity(intent);
    }

    public void praseDetial(String json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            mProjectStageDetialActivity.mProject = new Project();
            if(!data.isNull("name"))
            mProjectStageDetialActivity.mProject.mName = data.getString("name");
            mProjectStageDetialActivity.mProject.projectId = data.getString("project_id");
            mProjectStageDetialActivity.mProject.templateId = data.getString("template_id");
            if(!data.isNull("description"))
            mProjectStageDetialActivity.mProject.des = data.getString("description");
            mProjectStageDetialActivity.mProject.isPower = data.getInt("is_power");
            mProjectStageDetialActivity.mProject.isTop = data.getInt("is_top");
            mProjectStageDetialActivity.mProject.isLayer = data.getInt("is_layer");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updataStageViews()
    {
        mProjectStageDetialActivity.stageArea.removeAllViews();
        for (int i = 0; i < mProjectStageDetialActivity.mProject.mStages.size(); i++) {
            Stage Stage = mProjectStageDetialActivity.mProject.mStages.get(i);
            addStageView(Stage);
            for (int j = 0; j < mProjectStageDetialActivity.mProject.mStages.get(i).mTasks.size(); j++) {
                addStageTaskView(mProjectStageDetialActivity.mProject.mStages.get(i), mProjectStageDetialActivity.mProject.mStages.get(i).mTasks.get(j));
            }
        }
    }

    public void updataName(Intent intent) {
        String projectid = intent.getStringExtra("projectid");
        String name = intent.getStringExtra("name");
        if(mProjectStageDetialActivity.mProject.projectId.equals(projectid)) {
            mProjectStageDetialActivity.mProject.mName = name;
            ToolBarHelper.setTitle(mProjectStageDetialActivity.mActionBar,mProjectStageDetialActivity.mProject.mName);
        }
    }

    public void updataProject(Intent intent) {
        String id = intent.getStringExtra("projectid");
        if(mProjectStageDetialActivity.mProject != null)
        {
            if(mProjectStageDetialActivity.mProject.projectId.equals(id))
            {
                ProjectAsks.getProjectItemDetial(mProjectStageDetialActivity, mProjectStageDetialHandler,mProjectStageDetialActivity.mProject);
            }
        }
    }
}
