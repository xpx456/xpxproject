package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.oa.OaUtils;
import intersky.select.entity.Select;
import intersky.task.TaskManager;
import intersky.task.entity.Project;
import intersky.task.entity.Template;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//00
public class ProjectAsks {

    public static final String ACTION_UPDATA_PROJECT_LIST_DATA = "ACTION_UPDATA_PROJECT_LIST_DATA";

    public static final String PROJECT_PATH = "api/v1/Project";
    public static final String PROJECT_LIST= "get.project.list";
    public static final String PROJECT_ADD= "add.project.item";
    public static final String PROJECT_ADD_MEMER= "add.project.member";
    public static final String PROJECT_SET_MEMER= "set.project.member";
    public static final String PROJECT_EXIT= "del.project.notice.item";
    public static final String PROJECT_GET_STAGES = "get.project.stages";
    public static final String PROJECT_GET_ITEM_DETIAL = "get.project.item";
    public static final String PROJECT_SET_NEME= "set.project.name";
    public static final String PROJECT_SET_DES= "set.project.des";
    public static final String PROJECT_DETIAL= "get.project.detail";
    public static final String PROJECT_TOP= "set.project.top";
    public static final String PROJECT_SAVE_TEMPAL= "copy.template.item";
    public static final String PROJECT_SET_BASE= "set.project.base";
    public static final String PROJECT_DELETE= "del.project.item";
    public static final String PROJECT_FAIL= "get.project.file";

    public static final String PROJECT_NOTICE_PATH = "api/v1/ProjectNotice";
    public static final String PROJECT_NOTICE_APPLY= "apply.project.member";
    public static final String PROJECT_NOTICE_PASS= "pass.project.member";

    public static final String ACTION_PROJECT_SET_NEME = "ACTION_PROJECT_SET_NEME";
    public static final String ACTION_SET_DES = "ACTION_SET_DES";
    public static final String ACTION_PROJECT_SET_LEADER = "ACTION_PROJECT_SET_LEADER";
    public static final String ACTION_PROJECT_REMOVE_MEMBER = "ACTION_PROJECT_REMOVE_MEMBER";
    public static final String ACTION_MEMBER_ACCESS = "ACTION_MEMBER_ACCESS";
    public static final String ACTION_PROJECT_ADD_MEMBER = "ACTION_PROJECT_ADD_MEMBER";
    public static final String ACTION_CREAT_PROJECT = "ACTION_CREAT_PROJECT";
    public static final String ACTION_SET_PROJECT_OTHRE = "ACTION_SET_PROJECT_OTHRE";
    public static final String ACTION_DELETE_PROJECT = "ACTION_DELETE_PROJECT";

    public static final int PROJECT_LIST_SUCCESS = 1250000;
    public static final int PROJECT_ITEM_DETIAL_SUCCESS = 1250001;
    public static final int PROJECT_STAGE_SUCCESS = 1250002;
    public static final int PROJECT_DETIAL_SUCCESS = 1250003;
    public static final int GET_ATTACHMENT_SUCCESS = 1250004;
    public static final int PROJECT_SET_NEME_SUCCESS = 1250005;
    public static final int SET_LEADER_SUCCESS = 1250006;
    public static final int SET_DES_SUCCESS = 1250007;
    public static final int REMOVE_MEMBER_SUCCESS = 1250007;
    public static final int MEMBER_ACCESS_SUCCESS = 1250008;
    public static final int MEMBER_APPLAY_SUCCESS = 1250009;
    public static final int ADD_MEMBER_SUCCESS = 1250010;
    public static final int CREAT_PROJECT_SUCCESS = 1250011;
    public static final int SET_PROJECT_TOP = 1250012;
    public static final int DELETE_PROJECT = 1250013;
    public static final int SET_SAVE_SUCCESS = 1250015;
    public static final int SET_POWER_SUCCESS = 1250016;
    public static final int SET_CONTRAL_SUCCESS = 1250017;

    public static void getProject(Context mContext, Handler mHandler,String keyword,int page) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_no", String.valueOf(page));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "20");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_LIST_SUCCESS, mContext, nameValuePairs,keyword);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void getProjectItemDetial(Context mContext, Handler mHandler, Project project) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_GET_ITEM_DETIAL);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_ITEM_DETIAL_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getStage(Context mContext, Handler mHandler, Project project) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_GET_STAGES);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_STAGE_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getProjectDetial(Context mContext, Handler mHandler, Project project) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_DETIAL);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_DETIAL_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getAttachments(Context mContext, Handler mHandler, Project project,int attachmentPage)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_FAIL);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_no", String.valueOf(attachmentPage));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "40");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_ATTACHMENT_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setProjectName(Context mContext, Handler mHandler, Project project,String name) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_SET_NEME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", name);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_SET_NEME_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setLeader(Context mContext, Handler mHandler, Project project, Contacts contacts, int leavetype) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_SET_MEMER);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("member_id", contacts.mRecordid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("notice_type", String.valueOf(leavetype));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_LEADER_SUCCESS, mContext, nameValuePairs,contacts);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setProjectDes(Context mContext, Handler mHandler, Project project,String des) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_SET_DES);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("description", des);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_DES_SUCCESS, mContext, nameValuePairs,des);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void removeMember(Context mContext, Handler mHandler, Project project,Contacts contactModel)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_EXIT);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        if(contactModel != null) {
            mNameValuePair = new NameValuePair("member_id", contactModel.mRecordid);
            nameValuePairs.add(mNameValuePair);
        }
        else {
            mNameValuePair = new NameValuePair("member_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
            nameValuePairs.add(mNameValuePair);
        }
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, REMOVE_MEMBER_SUCCESS, mContext, nameValuePairs,contactModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setAccess(Context mContext, Handler mHandler, Project project,Contacts contactModel)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_NOTICE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_NOTICE_PASS);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("member_id", contactModel.mRecordid);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MEMBER_ACCESS_SUCCESS, mContext, nameValuePairs,contactModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setApply(Context mContext, Handler mHandler, Project project,Contacts contactModel)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_NOTICE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_NOTICE_APPLY);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("member_id", contactModel.mRecordid);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MEMBER_APPLAY_SUCCESS, mContext, nameValuePairs,contactModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void doApply(Context mContext, Handler mHandler, Project project)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_NOTICE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_NOTICE_APPLY);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("member_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, MEMBER_APPLAY_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void addMembers(Context mContext, Handler mHandler, Project project,String ids)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_ADD_MEMER);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("member_id", ids);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("notice_type", "3");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, ADD_MEMBER_SUCCESS, mContext, nameValuePairs,ids);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);


    }

    public static void creatProject(Context mContext, Handler mHandler, Project project,Template mTemplateModel) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_ADD);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", project.mName);
        nameValuePairs.add(mNameValuePair);
        if(!mTemplateModel.mId.equals("0"))
        {
            mNameValuePair = new NameValuePair("template_id", project.templateId);
            nameValuePairs.add(mNameValuePair);
            mNameValuePair = new NameValuePair("type", mTemplateModel.mType);
            nameValuePairs.add(mNameValuePair);
        }
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, CREAT_PROJECT_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setTop(Context mContext, Handler mHandler, Project project,int top) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_TOP);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_top", String.valueOf(top));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_PROJECT_TOP, mContext, nameValuePairs,top);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }


    public static void deleteProject(Context mContext, Handler mHandler, Project project)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, DELETE_PROJECT, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


    public static void saveTempal(Context mContext, Handler mHandler, Project project)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_SAVE_TEMPAL);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_SAVE_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setPower(Context mContext, Handler mHandler, Project project,Select mSelectMoreModel)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_SET_BASE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_power", mSelectMoreModel.mId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_POWER_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void setContral(Context mContext, Handler mHandler, Project project,Select mSelectMoreModel)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_SET_BASE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("is_layer", mSelectMoreModel.mId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, SET_CONTRAL_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

}
