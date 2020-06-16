package intersky.task;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Register;
import intersky.oa.OaUtils;
import intersky.select.entity.CustomSelect;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Project;
import intersky.task.entity.Task;
import intersky.task.handler.TaskHitHandler;
import intersky.task.view.activity.AddProjectActivity;
import intersky.task.view.activity.TaskCreatActivity;
import intersky.task.view.activity.TaskDetialActivity;
import intersky.task.view.activity.TaskManagerActivity;
import intersky.task.view.activity.TemplateActivity;
import intersky.task.view.adapter.ProjectSelectAdapter;

public class TaskManager {

    // TaskManager 26330 TaskDetial 26331 TaskCreat 26332 TaskMember 26333 TaskStructure 26334 Template 26335 ProjectStageDetial 26336
   // ProjectStageView26337 ProjectSys 26338 ProjectDetial 26339

    public static final String ACTION_TASK_UPDATE = "ACTION_TASK_UPDATE";
    public static final int PIC_MAXSIZE = 9;


    public static final String GET_TASK_HIT = "GET_TASK_HIT";

    public static final String CONTRAL_TEXT = "text";
    public static final String CONTRAL_PHONE = "phone";
    public static final String CONTRAL_AMOUNT = "amount";
    public static final String CONTRAL_NUMBER = "number";
    public static final String CONTRAL_EMAIL = "email";
    public static final String CONTRAL_DATE = "date";
    public static final String CONTRAL_CARD = "card";
    public static final String CONTRAL_AREA = "area";
    public static final String CONTRAL_RANK = "rank";
    public static final String CONTRAL_REMARK = "remark";
    public static final String CONTRAL_SELECT = "select";
    public static final String CONTRAL_OPTION = "option";
    public static final String CONTRAL_ATTACHMENT = "attachment";

    public static final int CONTRAL_DRAW_TYPE_EDITTEXT = 100;
    public static final int CONTRAL_DRAW_TYPE_DATAPICK = 101;
    public static final int CONTRAL_DRAW_TYPE_SELECT = 102;
    public static final int CONTRAL_DRAW_TYPE_ATTACHMENT = 103;
    public static final int CONTRAL_DRAW_TYPE_RANK = 104;
    public static final int CONTRAL_DRAW_TYPE_MIX = 106;

    public ArrayList<Task> mTasks = new  ArrayList<Task>();
    public ArrayList<Task> mSearchTasks = new  ArrayList<Task>();
    public ArrayList<Project> mProjects = new  ArrayList<Project>();
    public ArrayList<Project> mProjects2 = new  ArrayList<Project>();
    public ArrayList<CustomSelect> projectSelects = new ArrayList<CustomSelect>();
    public ArrayList<Project> mHeads = new  ArrayList<Project>();
    public ArrayList<Project> mSearchProjects = new  ArrayList<Project>();
//    public ArrayList<Contacts> members = new ArrayList<Contacts>();
    public int taskPage = 1;
    public int taskSearchPage = 1;
    public int taskOldPage = 1;
    public int taskOldSearchPage = 1;
    public int projectPage = 1;
    public int projectOldPage = 1;
    public boolean taskAll = false;
    public boolean taskSearchAll = false;
    public boolean projectkAll = false;
    public Context context;
    public ProjectSelectAdapter projectSelectAdapter;
    public int taskHit;
    public TaskHitHandler taskHitHandler;
    public Register register;
    public OaUtils oaUtils;
    public static volatile TaskManager mTaskManager;
    public static TaskManager init(OaUtils oaUtils,Context context) {

        if (mTaskManager == null) {
            synchronized (OaUtils.class) {
                if (mTaskManager == null) {
                    mTaskManager = new TaskManager(oaUtils,context);
                }
                else
                {
                    mTaskManager.context = context;
                    mTaskManager.oaUtils = oaUtils;
                    mTaskManager.projectSelectAdapter = new ProjectSelectAdapter(context,mTaskManager.projectSelects);
                    mTaskManager.taskHitHandler = new TaskHitHandler(context);
                }
            }
        }
        return mTaskManager;
    }

    public TaskManager(OaUtils oaUtils,Context context) {
        this.oaUtils = oaUtils;
        this.context = context;
        projectSelectAdapter = new ProjectSelectAdapter(context,projectSelects);
        taskHitHandler = new TaskHitHandler(context);
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public static TaskManager getInstance() {
        return mTaskManager;
    }

    public void getTaskHit() {

        TaskAsks.getTaskHint(context,taskHitHandler);
    }

    public static void showView(View contentview, String id, boolean isfirst) {
        TextView mtext = null;
        TextView base = (TextView) contentview.findViewById(R.id.basetag);
        TextView base2 = (TextView) contentview.findViewById(R.id.basetag2);
        if (id.equals("1")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_1);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        } else if (id.equals("2")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_2);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        } else if (id.equals("3")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_3);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        } else if (id.equals("4")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_4);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        } else if (id.equals("5")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_5);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        }
        if (mtext != null) {

        }

    }

    public int getDrawType(String type)
    {
        if(type.equals(CONTRAL_TEXT)||type.equals(CONTRAL_PHONE)||type.equals(CONTRAL_AMOUNT)||type.equals(CONTRAL_NUMBER)
                ||type.equals(CONTRAL_EMAIL)|| type.equals(CONTRAL_CARD)||type.equals(CONTRAL_REMARK))
        {
            return CONTRAL_DRAW_TYPE_EDITTEXT;
        }
        else if(type.equals(CONTRAL_DATE))
        {
            return CONTRAL_DRAW_TYPE_DATAPICK;
        }
        else if(type.equals(CONTRAL_AREA)||type.equals(CONTRAL_SELECT)||type.equals(CONTRAL_OPTION))
        {
            return CONTRAL_DRAW_TYPE_SELECT;
        }
        else if(type.equals(CONTRAL_ATTACHMENT)) {
            return CONTRAL_DRAW_TYPE_ATTACHMENT;
        }
        else if(type.equals(CONTRAL_RANK))
        {
            return CONTRAL_DRAW_TYPE_RANK;
        }
        else
        {
            return CONTRAL_DRAW_TYPE_MIX;
        }
    }

    public void sendTaskUpdate(String id) {
        Intent intent = new Intent(ACTION_TASK_UPDATE);
        intent.putExtra("taskid",id);
        context.sendBroadcast(intent);
    }


    public void startTaskManager(Context context) {
        Intent intent = new Intent(context, TaskManagerActivity.class);
        context.startActivity(intent);
    }

    public void startTaskNew(Context context) {
        Task task = new Task();
        Intent intent = new Intent(context, TaskCreatActivity.class);
        intent.putExtra("task",task);
        context.startActivity(intent);
    }

    public void startAddProject(Context context) {
        Intent intent = new Intent(context, TemplateActivity.class);
        context.startActivity(intent);
    }

    public void startDetial(Context context,Task task) {
        Intent intent = new Intent(context, TaskDetialActivity.class);
        intent.putExtra("task", task);
        context.startActivity(intent);
    }

    public void startTaskDetial(Context context,String recordid) {
        BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
        baseActivity.waitDialog.show();
        Task task = new Task();
        task.taskId = recordid;
        TaskAsks.getTaskDetial(context,taskHitHandler,task);
    }
}
//8265315/11963684
//8830677/12202795


//9193431/13337180
//9591942/13254762


//8784592/12676688 400
//9157841/12553404

//7442791/10700282 329
//8036793/11016693

//8017963/11546525 360
//8526264/11687652