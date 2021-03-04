package intersky.task.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import intersky.appbase.ScreenDefine;
import intersky.mywidget.MoveRelativeLayout;
import intersky.task.asks.ProjectStageAsks;
import intersky.task.entity.Project;
import intersky.task.entity.Stage;
import intersky.task.entity.Task;
import intersky.task.presenter.ProjectStageViewPresenter;
import intersky.task.view.adapter.StageAdapter;
import intersky.task.view.adapter.StageViewAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectStageViewActivity extends Activity {

    public ProjectStageViewPresenter mProjectStageViewPresenter = new ProjectStageViewPresenter(this);
    public RecyclerView templateList;
    public Project mProject;
    public StageViewAdapter mStageViewAdapter;
    public StageAdapter mStageAdapter;
    public PopupWindow popupWindow;
    public int lastPostiton = 0;
    public RelativeLayout shade;
    public Stage lastShowItem;
    public ItemTouchHelper touchHelper;
    public LinearLayoutManager linearLayoutManager;
    public long time;
    public Dialog waitDialog;
    public HashMap<String, Task> expentTask;
    public int changePosition = -1;
    public int startPosition = -1;
    public int currentPosition = 0;
    public RelativeLayout mRoot;
    public MoveRelativeLayout drageView;
    public int _xDelta = 0;
    public int _yDelta = 0;
    public int X = 0;
    public int Y = 0;
    public MotionEvent ev;
    public long tiem;
    public Stage stage1;
    public Stage stage2;
    public String tags = "";
    public String item1 = "";
    public String item2 = "";
    public String item3 = "";
    public RelativeLayout back;
    public ScreenDefine mScreenDefine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectStageViewPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mProjectStageViewPresenter.Destroy();
        super.onDestroy();
    }


    public RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener()
    {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState)
        {
            if(newState == RecyclerView.SCROLL_STATE_IDLE)
            {
                currentPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            }
        }
    };

    public View.OnClickListener doback = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageViewPresenter.doback();
        }
    };



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean next = mProjectStageViewPresenter.onTouch(ev);
        if(next == false)
        return super.dispatchTouchEvent(ev);
        else
            return true;
    }

    public View.OnClickListener renameListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageViewPresenter.updtatName((Stage) v.getTag());
        }
    };

    public View.OnClickListener addListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mProjectStageViewPresenter.inputName((Stage) v.getTag());
        }
    };


    public View.OnClickListener deleteListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            ProjectStageAsks.deleteStage(mProjectStageViewPresenter.mProjectStageViewActivity,mProjectStageViewPresenter.mProjectStageViewHandler,mProject,(Stage) v.getTag());
        }
    };
}
