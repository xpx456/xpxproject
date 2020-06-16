package intersky.task.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.ScreenDefine;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.apputils.MenuItem;
import intersky.mywidget.MoveRelativeLayout;
import intersky.task.R;
import intersky.task.StageItemTouchHelperCallback;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectStageAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.entity.Stage;
import intersky.task.entity.Task;
import intersky.task.handler.ProjectStageViewHandler;
import intersky.task.receiver.ProjectStageViewReceiver;
import intersky.task.view.activity.ProjectStageViewActivity;
import intersky.task.view.activity.TaskDetialActivity;
import intersky.task.view.adapter.StageAdapter;
import intersky.task.view.adapter.StageViewAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectStageViewPresenter implements Presenter {

    public ProjectStageViewHandler mProjectStageViewHandler;
    public ProjectStageViewActivity mProjectStageViewActivity;
    public ProjectStageViewReceiver mProjectStageViewReceiver;

    public ProjectStageViewPresenter(ProjectStageViewActivity mProjectStageViewActivity) {
        this.mProjectStageViewActivity = mProjectStageViewActivity;
        this.mProjectStageViewHandler = new ProjectStageViewHandler(mProjectStageViewActivity);
        mProjectStageViewReceiver = new ProjectStageViewReceiver(mProjectStageViewHandler);

    }

    @Override
    public void Create() {
        initView();
        mProjectStageViewActivity.registerReceiver(mProjectStageViewReceiver,mProjectStageViewReceiver.intentFilter);
    }

    @Override
    public void initView() {
        mProjectStageViewActivity.setContentView(R.layout.activity_project_stage_view);
        mProjectStageViewActivity.mScreenDefine = new ScreenDefine(mProjectStageViewActivity);
        mProjectStageViewActivity.templateList = (RecyclerView) mProjectStageViewActivity.findViewById(R.id.templateList);
        mProjectStageViewActivity.shade = (RelativeLayout) mProjectStageViewActivity.findViewById(R.id.shade);
        mProjectStageViewActivity.mProject = mProjectStageViewActivity.getIntent().getParcelableExtra("project");
        praseExpend();
        mProjectStageViewActivity.tags = mProjectStageViewActivity.getIntent().getStringExtra("tags");
        mProjectStageViewActivity.item1 = mProjectStageViewActivity.getIntent().getStringExtra("item1");
        mProjectStageViewActivity.item2 = mProjectStageViewActivity.getIntent().getStringExtra("item2");
        mProjectStageViewActivity.item3 = mProjectStageViewActivity.getIntent().getStringExtra("item3");
        mProjectStageViewActivity.linearLayoutManager = new LinearLayoutManager(mProjectStageViewActivity);
        mProjectStageViewActivity.linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mProjectStageViewActivity.waitDialog = createLoadingDialog(mProjectStageViewActivity, "");
        mProjectStageViewActivity.waitDialog.setCancelable(true);
        mProjectStageViewActivity.templateList.setLayoutManager(mProjectStageViewActivity.linearLayoutManager);
        mProjectStageViewActivity.templateList.addOnScrollListener(mProjectStageViewActivity.mOnScrollListener);
        mProjectStageViewActivity.back = (RelativeLayout) mProjectStageViewActivity.findViewById(R.id.back);
        mProjectStageViewActivity.mRoot = (RelativeLayout) mProjectStageViewActivity.findViewById(R.id.example);
//        mProjectStageViewActivity.mRoot.setOnTouchListener(mProjectStageViewActivity.onTouchListener);
//        ViewUtils.setTitle(mProjectStageViewActivity.mActionBar,mProjectStageViewActivity.getString(R.string.stage_view));
        Stage Stage1 = new Stage();
        Stage1.stageId = "add";
        Stage1.type = 1;
        Stage1.name = mProjectStageViewActivity.getString(R.string.project_task_view_add);
        ArrayList<Stage> stages = new ArrayList<Stage>();
        stages.add(Stage1);
        stages.addAll(0, mProjectStageViewActivity.mProject.mStages);
        mProjectStageViewActivity.mStageAdapter = new StageAdapter(mProjectStageViewActivity, mProjectStageViewActivity.mProject.mStages);
        mProjectStageViewActivity.mStageViewAdapter = new StageViewAdapter(mProjectStageViewActivity, stages, mProjectStageViewHandler);
        mProjectStageViewActivity.mStageViewAdapter.setOnItemClickListener(new StageViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Stage Stage = mProjectStageViewActivity.mStageViewAdapter.mStages.get(position);
                if (Stage.type == 1) {
                    doCreatSelect();
                }
            }
        });
        mProjectStageViewActivity.mStageViewAdapter.setOnItemLongClickListener(new StageViewAdapter.OnItemLongClickListener() {

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder view, int position) {
                if (position != mProjectStageViewActivity.mStageViewAdapter.getItemCount() - 1) {
                    mProjectStageViewActivity.touchHelper.startDrag(view);
                }
            }

        });
        mProjectStageViewActivity.templateList.setAdapter(mProjectStageViewActivity.mStageViewAdapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mProjectStageViewActivity.templateList);

        ItemTouchHelper.Callback callback =
                new StageItemTouchHelperCallback(mProjectStageViewActivity.mStageViewAdapter);
        mProjectStageViewActivity.touchHelper = new ItemTouchHelper(callback);
        mProjectStageViewActivity.touchHelper.attachToRecyclerView(mProjectStageViewActivity.templateList);
        mProjectStageViewActivity.back.setOnClickListener(mProjectStageViewActivity.doback);
    }

    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog2, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.text);// 提示文字
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        // 加载动画
//		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//				context, R.anim.load_animation);
        // 使用ImageView显示动画
//		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        //tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog1);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

    public void praseExpend() {
        for(int i = 0 ; i < mProjectStageViewActivity.mProject.mStages.size() ; i++)
        {
            if(mProjectStageViewActivity.mProject.mStages.get(i).mTasks.size() > 0)
            {
                initExpend(mProjectStageViewActivity.mProject.mStages.get(i).mTasks);
            }

        }
    }

    public void initExpend(ArrayList<Task> tasks) {
        for(int i = 0 ; i < tasks.size(); i++)
        {
            if(tasks.get(i).expend)
            {
                mProjectStageViewActivity.expentTask.put(tasks.get(i).taskId,tasks.get(i));
            }
            if(tasks.get(i).tasks.size() > 0)
            {
                initExpend(tasks);
            }
        }
    }

    public void doback() {
        mProjectStageViewActivity.finish();
    }

    public void doselect() {
        getStage();
    }

    //net
    public void getTask() {

        for(int i = 0 ; i < mProjectStageViewActivity.mProject.mStages.size() ; i++)
        {
            mProjectStageViewActivity.mProject.mStages.get(i).mTasks.clear();
        }
        TaskAsks.getTask(mProjectStageViewActivity, mProjectStageViewHandler,mProjectStageViewActivity.mProject.projectId,
                mProjectStageViewActivity.tags,"0",mProjectStageViewActivity.item1,mProjectStageViewActivity.item2,mProjectStageViewActivity.item3,""
                ,0);
    }


    public void getStage() {
        mProjectStageViewActivity.mProject.mStages.clear();
        ProjectAsks.getStage(mProjectStageViewActivity, mProjectStageViewHandler,mProjectStageViewActivity.mProject);
    }

    public void doChange() {
        Stage model = mProjectStageViewActivity.mStageViewAdapter.mStages.get(mProjectStageViewActivity.startPosition);
        Stage model2 = null;
        if(mProjectStageViewActivity.changePosition != -1 && mProjectStageViewActivity.changePosition != mProjectStageViewActivity.startPosition
                && mProjectStageViewActivity.changePosition != mProjectStageViewActivity.mStageViewAdapter.getItemCount()-1)
        {
            model2 = mProjectStageViewActivity.mStageViewAdapter.mStages.get(mProjectStageViewActivity.changePosition);
        }
        if(model2 != null)
        {
            ProjectStageAsks.changeStage(mProjectStageViewActivity, mProjectStageViewHandler,mProjectStageViewActivity.mProject,model,model2);
        }


    }



    public void doCreatTask(String name, Stage stage) {
        Task task = new Task();
        task.leaderId = TaskManager.getInstance().oaUtils.mAccount.mRecordId;
        task.projectId = mProjectStageViewActivity.mProject.projectId;
        task.stageId = stage.stageId;
        task.taskName = name;
        TaskAsks.addTask(mProjectStageViewActivity, mProjectStageViewHandler,task);
    }



    public MoveRelativeLayout copyView(Task mTaskItemModel) {
        MoveRelativeLayout view = (MoveRelativeLayout) LayoutInflater.from(mProjectStageViewActivity).inflate(R.layout.drage_stage_task_tiem_view, null);
        TextView title = (TextView) view.findViewById(R.id.taskname);
        TextView head = (TextView) view.findViewById(R.id.contact_img);
        ImageView imagetask = (ImageView) view.findViewById(R.id.task_son);
        ImageView imagelist = (ImageView) view.findViewById(R.id.task_list);
        TextView taskcount = (TextView) view.findViewById(R.id.task_son_count);
        TextView listcount = (TextView) view.findViewById(R.id.task_list_count);
        LinearLayout detiallayer = (LinearLayout) view.findViewById(R.id.detiallayer);
        title.setText(mTaskItemModel.taskName);
        title.setText(mTaskItemModel.taskName);
        if (mTaskItemModel.isComplete == 1) {
            title.setTextColor(Color.rgb(140, 140, 140));
            title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            title.setTextColor(Color.rgb(0, 0, 0));
        }
        String s = "";
        Contacts taskPerson = (Contacts) Bus.callData(mProjectStageViewActivity,"chat/getContactItem",mTaskItemModel.leaderId);
        AppUtils.setContactCycleHead(head,taskPerson.getName(),taskPerson.colorhead);
        if (mTaskItemModel.taskcount > 0) {
            taskcount.setVisibility(View.VISIBLE);
            imagetask.setVisibility(View.VISIBLE);
            taskcount.setText(String.valueOf(mTaskItemModel.taskfinish) + "/" + String.valueOf(mTaskItemModel.taskcount));
        } else {
            taskcount.setVisibility(View.GONE);
            imagetask.setVisibility(View.GONE);
        }
        if (mTaskItemModel.listcount > 0) {
            listcount.setVisibility(View.VISIBLE);
            imagelist.setVisibility(View.VISIBLE);
            listcount.setText(String.valueOf(mTaskItemModel.listfinish) + "/" + String.valueOf(mTaskItemModel.listcount));
        } else {

            listcount.setVisibility(View.GONE);
            imagelist.setVisibility(View.GONE);
        }
        view.setTag(mTaskItemModel);
        return view;
    }


    public void doCreatSelect() {
        final View popupWindowView = LayoutInflater.from(mProjectStageViewActivity).inflate(R.layout.buttom_window18, null);
        mProjectStageViewActivity.shade.setVisibility(View.VISIBLE);
//        mProjectStageViewActivity.shade.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mProjectStageViewActivity.popupWindow.dismiss();
//            }
//        });
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        mProjectStageViewActivity.popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mProjectStageViewActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
        mProjectStageViewActivity.popupWindow.setOutsideTouchable(true);
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = popupWindowView.findViewById(R.id.content).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        mProjectStageViewActivity.shade.setVisibility(View.GONE);
                        mProjectStageViewActivity.popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProjectStageViewActivity.popupWindow.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mProjectStageViewActivity.shade.setVisibility(View.VISIBLE);
        mProjectStageViewActivity.popupWindow.setBackgroundDrawable(dw);
        mProjectStageViewActivity.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mProjectStageViewActivity.shade.setVisibility(View.GONE);
                mProjectStageViewActivity.popupWindow.dismiss();
            }
        });
        ListView funlist1 = (ListView) popupWindowView.findViewById(R.id.horizon_listview1);
        funlist1.setAdapter(mProjectStageViewActivity.mStageAdapter);
        funlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inputName((Stage) parent.getAdapter().getItem(position));
                mProjectStageViewActivity.popupWindow.dismiss();
            }
        });
        mProjectStageViewActivity.popupWindow.showAtLocation(popupWindowView, Gravity.CENTER, 0, 0);
    }

    public void inputName(Stage mStage) {

        AppUtils.creatDialogTowButtonEdit(mProjectStageViewActivity,"",mProjectStageViewActivity.getString(R.string.task_detial_set_task_name)
                ,mProjectStageViewActivity.getString(R.string.button_word_cancle),mProjectStageViewActivity.getString(R.string.button_word_ok)
                ,null,new CreatStagetListener(mStage),"");
    }

    public class CreatStagetListener extends EditDialogListener{

        public Stage mStage;

        public CreatStagetListener(Stage mStage) {
            this.mStage = mStage;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                ProjectStageAsks.doCreatStage(mProjectStageViewActivity, mProjectStageViewHandler,mProjectStageViewActivity.mProject,editText.getText().toString(), mStage);
            } else {
                AppUtils.showMessage(mProjectStageViewActivity, mProjectStageViewActivity.getString(R.string.project_task_view_name_empty));
            }
        }
    }


    public void updtatName(Stage Stage) {

        AppUtils.creatDialogTowButtonEdit(mProjectStageViewActivity,"",mProjectStageViewActivity.getString(R.string.task_detial_set_task_name)
                ,mProjectStageViewActivity.getString(R.string.button_word_cancle),mProjectStageViewActivity.getString(R.string.button_word_cancle),
                null,new RenameStagetListener(Stage),Stage.name);
    }

    public class RenameStagetListener extends EditDialogListener{

        public Stage mStage;

        public RenameStagetListener(Stage mStage) {
            this.mStage = mStage;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                ProjectStageAsks.doRenameStage(mProjectStageViewActivity, mProjectStageViewHandler,mProjectStageViewActivity.mProject,editText.getText().toString(), mStage);
            } else {
                AppUtils.showMessage(mProjectStageViewActivity, mProjectStageViewActivity.getString(R.string.project_task_view_name_empty));
            }
        }
    }

    public void showMore(Stage Stage) {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();

        MenuItem menuItem = new MenuItem();
        menuItem.btnName = mProjectStageViewActivity.getString(R.string.stage_setting_rename);
        menuItem.mListener = mProjectStageViewActivity.renameListener;
        menuItem.item = Stage;
        items.add(menuItem);
        menuItem = new MenuItem();
        menuItem.btnName = mProjectStageViewActivity.getString(R.string.stage_setting_add);
        menuItem.mListener = mProjectStageViewActivity.addListener;
        menuItem.item = Stage;
        items.add(menuItem);
        menuItem = new MenuItem();
        menuItem.btnName = mProjectStageViewActivity.getString(R.string.button_delete);
        menuItem.mListener = mProjectStageViewActivity.deleteListener;
        menuItem.item = Stage;
        items.add(menuItem);
        AppUtils.creatButtomMenu(mProjectStageViewActivity,mProjectStageViewActivity.shade,items,mProjectStageViewActivity.findViewById(R.id.example));
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
        mProjectStageViewActivity.unregisterReceiver(mProjectStageViewReceiver);
    }

    public void updataAll() {
        getStage();
    }

    public void initTask(Task task) {
        Stage stage = mProjectStageViewActivity.mProject.mStageHashs.get(task.stageId);
        stage.mTasks.remove(task);
        stage.mTaskHashs.remove(task.taskId);
        mProjectStageViewActivity.stage1.mTasks.add(task);
        mProjectStageViewActivity.stage1.mTaskHashs.put(task.taskId,task);
        mProjectStageViewActivity.mStageViewAdapter.notifyDataSetChanged();
    }

    public void updataPositon(int x, int y) {
        if (mProjectStageViewActivity.mStageViewAdapter.mStages.get(mProjectStageViewActivity.currentPosition).mViewview != null) {
            RelativeLayout layout = (RelativeLayout) mProjectStageViewActivity.mStageViewAdapter.mStages.get(mProjectStageViewActivity.currentPosition).mViewview;
            if (x <= 60) {
                if (mProjectStageViewActivity.currentPosition > 0)
                    mProjectStageViewActivity.templateList.smoothScrollToPosition(mProjectStageViewActivity.currentPosition - 1);
            } else if (x > mProjectStageViewActivity.mScreenDefine.ScreenHeight - 60) {
                if (mProjectStageViewActivity.currentPosition < mProjectStageViewActivity.mStageViewAdapter.mStages.size() - 1)
                    mProjectStageViewActivity.templateList.smoothScrollToPosition(mProjectStageViewActivity.currentPosition + 1);
            }
        }


    }

    public void praseStage(Stage model, String json) {
        model.name = model.newname;
        mProjectStageViewActivity.mStageViewAdapter.notifyDataSetChanged();
    }

    public void deleteStage(Stage model) {
        for (int i = 0; i < model.mTasks.size(); i++) {
            Task taskItemModel = model.mTasks.get(i);
            RelativeLayout group = (RelativeLayout) mProjectStageViewActivity.findViewById(R.id.example);
            if (taskItemModel.dview != null) {
                group.removeView(taskItemModel.dview);
            }
        }
        mProjectStageViewActivity.mStageViewAdapter.mStages.remove(model);
        mProjectStageViewActivity.mStageViewAdapter.notifyDataSetChanged();
    }


    public void onChange() {
        doChange();
    }

    public void changeSuccess() {
        updataAll();
    }

    public void startTask(Task task)
    {
        Intent intent = new Intent(mProjectStageViewActivity, TaskDetialActivity.class);
        intent.putExtra("task",task);
        intent.putExtra("project",mProjectStageViewActivity.mProject);
        mProjectStageViewActivity.startActivity(intent);
    }

    public void creatDrageview(View view)
    {
        Task mTaskItemModel = (Task) view.getTag();
        mProjectStageViewActivity.drageView = copyView(mTaskItemModel);
        view.setVisibility(View.INVISIBLE);
        RelativeLayout group = (RelativeLayout) mProjectStageViewActivity.findViewById(R.id.example);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams((int) (500* mProjectStageViewActivity.mScreenDefine.density), RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout parent = (LinearLayout) view.getParent();
        LinearLayout parent11 = (LinearLayout) parent.getParent();
        ViewGroup parent12 = (ViewGroup) parent11.getParent();
        LinearLayout parent2 = (LinearLayout) parent12.getParent();
        RelativeLayout relativeLayout = (RelativeLayout) parent2.getParent();
        param1.leftMargin = view.getLeft()+parent.getLeft()+parent2.getLeft()+parent11.getLeft()+parent12.getLeft()+mProjectStageViewActivity.templateList.getLeft()+relativeLayout.getLeft();;
        param1.topMargin =  view.getTop()+parent.getTop()+parent2.getTop()+parent11.getTop()+parent12.getTop()+mProjectStageViewActivity.templateList.getTop()+relativeLayout.getTop();
        param1.rightMargin = view.getRight()+parent.getRight()+parent2.getRight()+parent11.getRight()+parent12.getRight()+mProjectStageViewActivity.templateList.getRight()+relativeLayout.getRight();
        mProjectStageViewActivity.mRoot.addView(mProjectStageViewActivity.drageView,param1);
        mProjectStageViewActivity. _xDelta = mProjectStageViewActivity.X - param1.leftMargin;
        mProjectStageViewActivity. _yDelta = mProjectStageViewActivity.Y - param1.topMargin;
        mProjectStageViewActivity.drageView.setDrage(mProjectStageViewActivity.X,mProjectStageViewActivity.Y);
    }

    public void doChange(int x , int y )
    {
        Task taskItemModel = (Task) mProjectStageViewActivity.drageView.getTag();
        taskItemModel.view.setVisibility(View.VISIBLE);
        Stage Stage  = mProjectStageViewActivity.mStageViewAdapter.mStages.get(mProjectStageViewActivity.currentPosition);
        if(!taskItemModel.stageId.equals(Stage.stageId))
        {
            if (Stage.mViewview != null) {
                RelativeLayout layer = (RelativeLayout) Stage.mViewview;
                int a = layer.getTop();
                int b = layer.getHeight();
                View layout = (View) Stage.mViewview.getParent();
                int c = layout.getTop();
                int d = mProjectStageViewActivity.mRoot.getTop();
                //int heightt = (int) (mProjectStageViewActivity.getResources().getDimension(R.dimen.abc_action_bar_default_height_material)*InterskyApplication.mAppScreenDenineModel.density);
                if (layer.getTop() +d + c <= y
                        && layer.getTop() +d+c+ layer.getHeight() >= y) {

                    mProjectStageViewActivity.stage1 = Stage;
                    TaskAsks.changeTaskStage(mProjectStageViewActivity, mProjectStageViewHandler,taskItemModel, Stage);
                }
            }
        }
    }

    public boolean onTouch(MotionEvent event)
    {

        mProjectStageViewActivity.X = (int) event.getRawX();
        mProjectStageViewActivity.Y = (int) event.getRawY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("---action down-----");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("---action move-----");
                if(mProjectStageViewActivity.drageView != null)
                {
                    mProjectStageViewActivity.drageView.ontTouch(event);
                    if(System.currentTimeMillis() - mProjectStageViewActivity.tiem > 1000)
                    {
                        mProjectStageViewActivity.tiem = System.currentTimeMillis();
                        Message msg = new Message();
                        msg.obj = mProjectStageViewActivity.drageView.getTag();
                        msg.arg1 = mProjectStageViewActivity.X;
                        msg.arg2 = mProjectStageViewActivity.X;
                        msg.what = ProjectStageViewHandler.UPDATA_POSITION;
                        mProjectStageViewHandler.sendMessage(msg);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("---action up-----");
                if(mProjectStageViewActivity.drageView != null)
                {
                    mProjectStageViewActivity.mRoot.removeView(mProjectStageViewActivity.drageView);
                    doChange(mProjectStageViewActivity.X,mProjectStageViewActivity.Y);
                    mProjectStageViewActivity.drageView = null;
                }
                break;
        }
        if(mProjectStageViewActivity.drageView != null)
        return true;
        else
            return false;
    }

    public void startDetial(Task task) {
        Intent intent = new Intent(mProjectStageViewActivity, TaskDetialActivity.class);
        intent.putExtra("task", task);
        intent.putExtra("project", mProjectStageViewActivity.mProject);
        mProjectStageViewActivity.startActivity(intent);
    }

    public void updataTask(Intent intent) {
        String id = intent.getStringExtra("taskid");
        String projectid = intent.getStringExtra("projectid");
        String stageId = intent.getStringExtra("stageid");
        if(mProjectStageViewActivity.mProject.projectId.equals(projectid)) {
            getTask();
        }
    }

    public void updataProject(Intent intent) {
        String id = intent.getStringExtra("projectid");
        if(mProjectStageViewActivity.mProject != null)
        {
            if(mProjectStageViewActivity.mProject.projectId.equals(id))
            {
                ProjectAsks.getProjectItemDetial(mProjectStageViewActivity, mProjectStageViewHandler,mProjectStageViewActivity.mProject);
            }
        }
    }

}
