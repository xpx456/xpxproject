package intersky.task.prase;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.select.entity.CustomSelect;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.entity.Project;
import intersky.task.entity.Stage;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class ProjectPrase {

    public static ArrayList<Project> praseProjects(NetObject net, Context context) {
        ArrayList<Project> heads = new ArrayList<Project>();
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return heads;
            }
            String keyword = (String) net.item;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length() - 1; i++) {
                XpxJSONObject jo = data.getJSONObject(i);
                Project mProject = new Project();
                mProject.projectId = jo.getString("project_id");
                mProject.templateId = jo.getString("template_id");
                mProject.mName = jo.getString("name");
                mProject.isPower = jo.getInt("is_power",0);
                mProject.leaderId = jo.getString("charge_id");
                mProject.isTop = jo.getInt("is_top",0);
                mProject.isLayer = jo.getInt("is_layer",0);
                mProject.des = jo.getString("description");
                mProject.userid = jo.getString("user_id");
                mProject.fileid = jo.getString("project_file_id");
                if(jo.getString("project_file_id").equals("0"))
                {
                    TaskManager.getInstance().mProjects.add(mProject);
                }
                else
                {
                    Project mHead = checkHead(mProject);
                    if(mHead == null)
                    {
                        mHead = new Project();
                        mHead.fileid = jo.getString("project_file_id");
                        mHead.type = 1;
                        TaskManager.getInstance().mProjects.add(TaskManager.getInstance().mHeads.size(),mHead);
                        TaskManager.getInstance().mHeads.add(mHead);
                        heads.add(mHead);
                    }
                    mHead.projects.add(mProject);
                }
                TaskManager.getInstance().mProjects2.add(mProject);
                TaskManager.getInstance().projectSelects.add(new CustomSelect(mProject.projectId,mProject.mName,mProject));
                if (mProject.mName.contains(keyword)) {
                    TaskManager.getInstance().mSearchProjects.add(mProject);
                }
            }
            TaskManager.getInstance().projectPage++;
            XpxJSONObject jo = data.getJSONObject(data.length() - 1);
            if (jo.getInt("total_results",TaskManager.getInstance().mProjects.size()) == TaskManager.getInstance().mProjects.size()) {
                TaskManager.getInstance().projectkAll = true;
            }
            return heads;
        } catch (JSONException e) {
            e.printStackTrace();
            return heads;
        }
    }

    public static Project checkHead(Project item)
    {
        Project head = null;
        for(int i = 0 ; i < TaskManager.getInstance().mHeads.size() ; i++)
        {
            if(TaskManager.getInstance().mHeads.get(i).fileid.equals(item.fileid))
            {
                head = TaskManager.getInstance().mHeads.get(i);
                break;
            }
        }
        return head;
    }

    public static void praseProjectItemDetial(NetObject net, Context context) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return;
            }
            Project mProjectItemModel = (Project) net.item;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject data = jsonObject.getJSONObject("data");
            mProjectItemModel.mName = data.getString("name");
            mProjectItemModel.projectId = data.getString("project_id");
            mProjectItemModel.templateId = data.getString("template_id");
            mProjectItemModel.des = data.getString("description");
            mProjectItemModel.isPower = data.getInt("is_power",0);
            mProjectItemModel.isTop = data.getInt("is_top",0);
            mProjectItemModel.isLayer = data.getInt("is_layer",0);
//            if(!data.isNull("name"))
//                mTask.projectName = data.getString("name");
//            mTaskDetialActivity.projectName.setText(mTaskDetialActivity.mTask.parentName);
            mProjectItemModel.stageString = data.getJSONArray("has_many_stages").toString();
//            mTaskDetialActivity.mTask.projectStageJson = data.getJSONArray("has_many_stages").toString();
//            if (mTaskDetialActivity.mTask.parentId.equals("0")) {
//                mTaskDetialActivity.projectName.setText(mTaskDetialActivity.getString(R.string.task_main_tab_project)
//                        + ":" + mTaskDetialActivity.mTask.projectName);
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseStage(NetObject net,Context context) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }
            Project project = (Project) net.item;
            project.mStages.clear();
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            project.stageString = ja.toString();
            project.mStages.clear();
            project.mStageHashs.clear();
            for (int i = 0; i < ja.length(); i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                Stage Stage = new Stage();
                Stage.stageId = jo.getString("project_stages_id");
                Stage.name = jo.getString("name");
                Stage.sort = jo.getString("sort");
                project.mStages.add(Stage);
                project.mStageHashs.put(Stage.stageId,Stage);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static MemberDetial praseProjectDetial(NetObject net, Context context, ArrayList<Contacts> mContactModels) {

        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return null;
            }
            Project project = (Project) net.item;
            mContactModels.clear();
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray ja = data.getJSONArray("has_many_notice");
            MemberDetial memberDetial = new MemberDetial();
            for(int i = 0 ; i < ja.length() ; i++)
            {
                JSONObject jo = ja.getJSONObject(i);
                if(jo.getInt("is_pass") == 1)
                {
                    Contacts contactModel = (Contacts) Bus.callData(context,"chat/getContactItem",jo.getString("member_id"));
                    contactModel.isadmin = false;
                    contactModel.isapply = false;
                    if(jo.getInt("notice_type") == 2)
                    {
                        contactModel.isadmin = true;
                        if(memberDetial.admincount == 0)
                        {
                            Contacts mContactModel = new Contacts(context.getString(R.string.task_contacts_admin),1);
                            mContactModel.setName(context.getString(R.string.task_contacts_admin));
                            mContactModels.add(0,mContactModel);
                        }
                        if(memberDetial.admincount == 0)
                        {
                            mContactModels.add(1,contactModel);
                        }
                        else
                        {
                            mContactModels.add(memberDetial.admincount+1,contactModel);
                        }

                        memberDetial.admincount++;
                    }
                    else if(jo.getInt("notice_type") == 3)
                    {
                        if(memberDetial.personcount == 0)
                        {
                            Contacts mContactModel = new Contacts(context.getString(R.string.task_contacts_person),1);
                            mContactModel.setName(context.getString(R.string.task_contacts_person));
                            if(memberDetial.admincount == 0)
                                mContactModels.add(0,mContactModel);
                            else
                                mContactModels.add(memberDetial.admincount+1,mContactModel);
                        }
                        if(memberDetial.personcount == 0)
                        {
                            if(memberDetial.admincount == 0)
                                mContactModels.add(1,contactModel);
                            else
                                mContactModels.add(memberDetial.admincount+2,contactModel);
                        }
                        else
                        {
                            if(memberDetial.admincount == 0)
                                mContactModels.add(memberDetial.personcount+1,contactModel);
                            else
                                mContactModels.add(memberDetial.admincount+memberDetial.personcount+2,contactModel);
                        }
                        memberDetial.personcount++;
                    }
                    else if(jo.getInt("notice_type") == 1)
                    {
                        if(!project.leaderId.equals(jo.getString("member_id")))
                        {
                            project.leaderId = jo.getString("member_id");
                        }

                    }

                    if(jo.getString("member_id").equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
                    {
                        memberDetial.leavlType = jo.getInt("notice_type");
                    }
                }
                else
                {
                    Contacts contactModel = (Contacts) Bus.callData(context,"chat/getContactItem",jo.getString("member_id"));
                    contactModel.isadmin = false;
                    contactModel.isapply = true;
                    if(memberDetial.apply == 0)
                    {
                        Contacts mContactModel = new Contacts(context.getString(R.string.task_contacts_apply),1);
                        mContactModel.setName(context.getString(R.string.task_contacts_apply));
                        if(memberDetial.personcount == 0 && memberDetial.admincount == 0)
                        {
                            mContactModels.add(0,mContactModel);
                        }
                        else if(memberDetial.personcount == 0 && memberDetial.admincount != 0)
                        {
                            mContactModels.add(memberDetial.admincount+1,mContactModel);
                        }
                        else if(memberDetial.personcount != 0 && memberDetial.admincount == 0)
                        {
                            mContactModels.add(memberDetial.personcount+1,mContactModel);
                        }
                        else if(memberDetial.personcount != 0 && memberDetial.admincount != 0)
                        {
                            mContactModels.add(memberDetial.admincount+memberDetial.personcount+2,mContactModel);
                        }

                    }
                    mContactModels.add(contactModel);
                    memberDetial.apply++;
                }
            }
            return memberDetial;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static boolean prasseReply(NetObject net,Context context,ArrayList<Reply> replies) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }
            Project project = (Project) net.item;
            JSONObject object = new JSONObject(json);
            JSONObject jo2 = object.getJSONObject("data");
            Reply mReplyModel = new Reply(jo2.getString("project_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), project.projectId, jo2.getString("create_time"));
            replies.add(0, mReplyModel);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void praseSetName(NetObject net,Context context,Project project) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        String name  = (String) net.item;
        project.mName = name;
    }

    public static void praseSetDes(NetObject net,Context context,Project project) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        String des  = (String) net.item;
        project.des = des;
    }

    public static Project praseCreatProject(NetObject net,Context context)
    {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return null;
            }
            Project project = (Project) net.item;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");

            project.projectId = jo.getString("project_id");
            project.mName = jo.getString("name");
            project.des = jo.getString("description");
            project.userid = jo.getString("user_id");
            project.leaderId = project.userid;
            project.templateId = jo.getString("template_id");
            return project;

        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }

    }

    public static void praseTop(NetObject net,Context context,Project project) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        int id = (int) net.item;
        project.isTop = id;
    }

    public static boolean praseDelete(NetObject net,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return true;
    }

    public static class MemberDetial implements Parcelable {
        public int admincount = 0;
        public int personcount = 0;
        public int apply = 0;
        public int leavlType = 4;

        public MemberDetial()
        {

        }

        protected MemberDetial(Parcel in) {
            admincount = in.readInt();
            personcount = in.readInt();
            apply = in.readInt();
            leavlType = in.readInt();
        }

        public static final Creator<MemberDetial> CREATOR = new Creator<MemberDetial>() {
            @Override
            public MemberDetial createFromParcel(Parcel in) {
                return new MemberDetial(in);
            }

            @Override
            public MemberDetial[] newArray(int size) {
                return new MemberDetial[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(admincount);
            dest.writeInt(personcount);
            dest.writeInt(apply);
            dest.writeInt(leavlType);
        }
    }
}
