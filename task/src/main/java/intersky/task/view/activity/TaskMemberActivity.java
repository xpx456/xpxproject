package intersky.task.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.task.entity.Project;
import intersky.task.entity.Task;
import intersky.task.prase.ProjectPrase;
import intersky.task.presenter.TaskMemberPresenter;
import intersky.task.view.adapter.TaskContactAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskMemberActivity extends BaseActivity {

    public static final String ACTION_ADD_MEMBER = "ACTION_ADD_MEMBER";

    public static final int EVENT_START_MORE =  1000;
    public String activityRid = AppUtils.getguid();
    public TaskMemberPresenter mTaskMemberPresenter = new TaskMemberPresenter(this);
    public TaskContactAdapter mContactAdapter;
    public PopupWindow menuWindow;
    public ArrayList<Contacts> mMembers = new ArrayList<Contacts>();
    public ListView mList;
    public Project project;
    public Task task;
    public ProjectPrase.MemberDetial memberDetial;
    public RelativeLayout shade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskMemberPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mTaskMemberPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener addMember = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskMemberPresenter.doAdd();
        }
    };

    public AdapterView.OnItemClickListener menberClick = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTaskMemberPresenter.startDetial((Contacts) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener setLeaderlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskMemberPresenter.setLeader((Contacts) v.getTag());
        }
    };

    public View.OnClickListener setAdminlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskMemberPresenter.setAdmin((Contacts) v.getTag());
        }
    };

    public View.OnClickListener setCancleAdminlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskMemberPresenter.cancleAdmin((Contacts) v.getTag());
        }
    };

    public View.OnClickListener setRemovelistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskMemberPresenter.removeMember((Contacts) v.getTag());
        }
    };

    public View.OnClickListener setAccesslistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskMemberPresenter.accessMember((Contacts) v.getTag());
        }
    };

    public View.OnClickListener setAccessFaillistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTaskMemberPresenter.unAccessMember((Contacts) v.getTag());
        }
    };
}
