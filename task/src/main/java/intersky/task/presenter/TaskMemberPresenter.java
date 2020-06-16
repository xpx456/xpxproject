package intersky.task.presenter;

import android.content.Intent;
import android.widget.ListView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TaskAsks;
import intersky.task.handler.TaskMemberHandler;
import intersky.task.receiver.TaskMemberReceiver;
import intersky.task.view.activity.TaskMemberActivity;
import intersky.task.view.adapter.TaskContactAdapter;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class TaskMemberPresenter implements Presenter {

    public TaskMemberHandler mTaskMemberHandler;
    public TaskMemberActivity mTaskMemberActivity;
    public TaskMemberPresenter(TaskMemberActivity mTaskMemberActivity)
    {
        this.mTaskMemberActivity = mTaskMemberActivity;
        this.mTaskMemberHandler = new TaskMemberHandler(mTaskMemberActivity);
        mTaskMemberActivity.setBaseReceiver(new TaskMemberReceiver(mTaskMemberHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mTaskMemberActivity.setContentView(R.layout.activity_task_member);
        mTaskMemberActivity.mList = (ListView) mTaskMemberActivity.findViewById(R.id.busines_List);
        mTaskMemberActivity.memberDetial = mTaskMemberActivity.getIntent().getParcelableExtra("memberdetial");
        ArrayList<Contacts> contacts = mTaskMemberActivity.getIntent().getParcelableArrayListExtra("members");
        mTaskMemberActivity.mMembers.addAll(contacts);
        mTaskMemberActivity.shade = mTaskMemberActivity.findViewById(R.id.shade);
        if(mTaskMemberActivity.getIntent().hasExtra("task"))
        {
            mTaskMemberActivity.task = mTaskMemberActivity.getIntent().getParcelableExtra("task");
            ToolBarHelper.setTitle(mTaskMemberActivity.mActionBar,mTaskMemberActivity.getString(R.string.task_member_title));
            if(mTaskMemberActivity.task.isLocked == 0)
            {
                ToolBarHelper.setRightBtn(mTaskMemberActivity.mActionBar,mTaskMemberActivity.addMember,R.drawable.headcreat);
            }
            else
            {
                if( mTaskMemberActivity.memberDetial.leavlType < 3)
                {
                    ToolBarHelper.setRightBtn(mTaskMemberActivity.mActionBar,mTaskMemberActivity.addMember,R.drawable.headcreat);
                }
            }
        }
        if(mTaskMemberActivity.getIntent().hasExtra("project"))
        {
            mTaskMemberActivity.project = mTaskMemberActivity.getIntent().getParcelableExtra("project");
            ToolBarHelper.setTitle(mTaskMemberActivity.mActionBar,mTaskMemberActivity.getString(R.string.project_member_title));
            if(mTaskMemberActivity.memberDetial.leavlType < 3)
                ToolBarHelper.setRightBtn(mTaskMemberActivity.mActionBar,mTaskMemberActivity.addMember,R.drawable.headcreat);
        }
        mTaskMemberActivity.mContactAdapter = new TaskContactAdapter(mTaskMemberActivity.mMembers,mTaskMemberActivity,
                mTaskMemberHandler,mTaskMemberActivity.memberDetial.leavlType);
        mTaskMemberActivity.mList.setAdapter(mTaskMemberActivity.mContactAdapter);
        mTaskMemberActivity.mList.setOnItemClickListener(mTaskMemberActivity.menberClick);

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


    public void doAdd()
    {
        Bus.callData(mTaskMemberActivity,"chat/selectListAddContact",new ArrayList<Contacts>(),TaskMemberActivity.ACTION_ADD_MEMBER,"添加成员",false,mTaskMemberActivity.mContactAdapter.mContacts);
    }


    public void startDetial(Contacts mContactModel)
    {
        Bus.callData(mTaskMemberActivity,"chat/startContactDetial",mContactModel);
    }

    public void showMore(Contacts contact)
    {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        if(mTaskMemberActivity.task != null)
        {
            MenuItem mMenuItem = new MenuItem();
            mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_setduty);
            mMenuItem.mListener = mTaskMemberActivity.setLeaderlistener;
            mMenuItem.item = contact;
            menuItems.add(mMenuItem);
            mMenuItem = new MenuItem();
            mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_remove);
            mMenuItem.mListener = mTaskMemberActivity.setRemovelistener;
            mMenuItem.item = contact;
            menuItems.add(mMenuItem);
        }
        else
        {
            if(contact.isapply)
            {
                if(mTaskMemberActivity.memberDetial.leavlType == 1 || mTaskMemberActivity.memberDetial.leavlType == 2)
                {
                    MenuItem mMenuItem = new MenuItem();
                    mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_apply_success);
                    mMenuItem.mListener = mTaskMemberActivity.setAccesslistener;
                    mMenuItem.item = contact;
                    menuItems.add(mMenuItem);
                    mMenuItem = new MenuItem();
                    mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_apply_fail);
                    mMenuItem.mListener = mTaskMemberActivity.setAccessFaillistener;
                    mMenuItem.item = contact;
                    menuItems.add(mMenuItem);
                }
            }
            else
            {
                if(mTaskMemberActivity.memberDetial.leavlType == 1)
                {
                    MenuItem mMenuItem = new MenuItem();
                    mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_setduty);
                    mMenuItem.mListener = mTaskMemberActivity.setLeaderlistener;
                    mMenuItem.item = contact;
                    menuItems.add(mMenuItem);
                    if(contact.isadmin)
                    {
                        mMenuItem = new MenuItem();
                        mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_unsetadmin);
                        mMenuItem.mListener = mTaskMemberActivity.setCancleAdminlistener;
                        mMenuItem.item = contact;
                        menuItems.add(mMenuItem);
                    }
                    else
                    {
                        mMenuItem = new MenuItem();
                        mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_setadmin);
                        mMenuItem.mListener = mTaskMemberActivity.setAdminlistener;
                        mMenuItem.item = contact;
                        menuItems.add(mMenuItem);
                    }
                    mMenuItem = new MenuItem();
                    mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_remove);
                    mMenuItem.mListener = mTaskMemberActivity.setRemovelistener;
                    mMenuItem.item = contact;
                    menuItems.add(mMenuItem);
                }
                else if(mTaskMemberActivity.memberDetial.leavlType == 2)
                {

//                    if(contact.isadmin && contact.mRecordid.equals(TaskManager.getInstance().account.mRecordId))
//                    {
//                        MenuItem mMenuItem = new MenuItem();
//                        mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_remove);
//                        mMenuItem.mListener = mTaskMemberActivity.setRemovelistener;
//                        mMenuItem.item = contact;
//                        menuItems.add(mMenuItem);
//                    }
                    if(contact.isadmin == false)
                    {
                        MenuItem mMenuItem = new MenuItem();
                        mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_setadmin);
                        mMenuItem.mListener = mTaskMemberActivity.setAdminlistener;
                        mMenuItem.item = contact;
                        menuItems.add(mMenuItem);
                        mMenuItem = new MenuItem();
                        mMenuItem.btnName = mTaskMemberActivity.getString(R.string.task_contacts_remove);
                        mMenuItem.mListener = mTaskMemberActivity.setRemovelistener;
                        mMenuItem.item = contact;
                        menuItems.add(mMenuItem);
                    }

                }
            }
        }
        mTaskMemberActivity.menuWindow = AppUtils.creatButtomMenu(mTaskMemberActivity,mTaskMemberActivity.shade,menuItems,mTaskMemberActivity.findViewById(R.id.activity_busines2));
    }

    public void setLeader(Contacts contacts) {
        mTaskMemberActivity.waitDialog.show();
        if(mTaskMemberActivity.task != null)
        {
            TaskAsks.setLeader(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.task,contacts);
        }
        else
        {
            ProjectAsks.setLeader(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project,contacts,1);
        }
        if(mTaskMemberActivity.menuWindow != null)
        {
            mTaskMemberActivity.menuWindow.dismiss();
        }
    }

    public void setAdmin(Contacts contacts) {
        mTaskMemberActivity.waitDialog.show();
        ProjectAsks.setLeader(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project,contacts,2);
        if(mTaskMemberActivity.menuWindow != null)
        {
            mTaskMemberActivity.menuWindow.dismiss();
        }
    }

    public void cancleAdmin(Contacts contacts) {
        mTaskMemberActivity.waitDialog.show();
        ProjectAsks.setLeader(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project,contacts,3);
        if(mTaskMemberActivity.menuWindow != null)
        {
            mTaskMemberActivity.menuWindow.dismiss();
        }
    }

    public void removeMember(Contacts contacts) {
        mTaskMemberActivity.waitDialog.show();
        if(mTaskMemberActivity.task != null)
        {
            TaskAsks.setExistTask(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.task,contacts);
        }
        else
        {
            ProjectAsks.removeMember(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project,contacts);
        }
        if(mTaskMemberActivity.menuWindow != null)
        {
            mTaskMemberActivity.menuWindow.dismiss();
        }
    }

    public void accessMember(Contacts contacts) {
        mTaskMemberActivity.waitDialog.show();
        ProjectAsks.setAccess(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project,contacts);
        if(mTaskMemberActivity.menuWindow != null)
        {
            mTaskMemberActivity.menuWindow.dismiss();
        }
    }

    public void unAccessMember(Contacts contacts) {
        mTaskMemberActivity.waitDialog.show();
        ProjectAsks.removeMember(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project,contacts);
        if(mTaskMemberActivity.menuWindow != null)
        {
            mTaskMemberActivity.menuWindow.dismiss();
        }
    }


    public void addMember(Intent intent) {
        ArrayList<Contacts> contacts = new ArrayList<Contacts>();
        contacts.addAll((ArrayList<Contacts>)Bus.callData(mTaskMemberActivity,"chat/mselectitems",""));
        if(contacts.size() == 0)
            return;
        String recordid = "";
        for(int i = 0 ; i < contacts.size() ; i++)
        {
            if(i == 0)
            {
                recordid += contacts.get(i).mRecordid;
            }
            else
            {
                recordid += ","+contacts.get(i).mRecordid;
            }
        }
        mTaskMemberActivity.waitDialog.show();
        if(mTaskMemberActivity.task == null)
        {
            ProjectAsks.addMembers(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project,recordid);
        }
        else
        {
            TaskAsks.addTaskMember(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.task,recordid);
        }
    }


    public Contacts praseData(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(mTaskMemberActivity,AppUtils.getfailmessage(json));
            return null;
        }
        Contacts contacts = (Contacts) net.item;
        return contacts;
    }

    public boolean praseData2(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(mTaskMemberActivity,AppUtils.getfailmessage(json));
            return false;
        }
        return true;
    }

    public void updataMember() {
        if(mTaskMemberActivity.task != null)
        {
            mTaskMemberActivity.waitDialog.show();
            TaskAsks.getTaskDetial(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.task);
        }
        else
        {
            mTaskMemberActivity.waitDialog.show();
            ProjectAsks.getProjectDetial(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project);
        }
    }

    @Override
    public void Destroy() {

    }

    public void updataTaskMember(Intent intent) {
        String id = intent.getStringExtra("taskid");
        if(mTaskMemberActivity.task != null)
        {
            if(mTaskMemberActivity.task.taskId.equals(id))
            {
                mTaskMemberActivity.waitDialog.show();
                TaskAsks.getTaskDetial(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.task);
            }
        }
    }

    public void updataProjectMember(Intent intent) {
        String id = intent.getStringExtra("projectid");
        if(mTaskMemberActivity.project != null)
        {
            if(mTaskMemberActivity.project.projectId.equals(id))
            {
                mTaskMemberActivity.waitDialog.show();
                ProjectAsks.getProjectDetial(mTaskMemberActivity,mTaskMemberHandler,mTaskMemberActivity.project);
            }
        }
    }
}
