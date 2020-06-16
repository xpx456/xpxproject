package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Project;
import intersky.task.entity.Stage;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//04
public class ProjectStageAsks {

    public static final String PROJECT_STAGE_PATH = "api/v1/ProjectStages";
    public static final String PROJECT_STAGE_SET= "set.project.stage.item";
    public static final String PROJECT_STAGE_CREAT= "add.project.stage.item";
    public static final String PROJECT_STAGE_DELETE= "del.project.stage.item";
    public static final String PROJECT_STAGE_RENAME= "set.project.stage.name";

    public static final int CREAT_STAGE_SUCCESS = 1250400;
    public static final int DELETE_STAGE_SUCCESS = 1250401;
    public static final int RENAME_STAGE_SUCCESS = 1250402;
    public static final int CHANGE_STAGE_SUCCESS = 1250403;

    public static final String ACTION_PROJECT_STAGE_UPDATE = "ACTION_PROJECT_STAGE_UPDATE";

    public static void doCreatStage(Context mContext, Handler mHandler, Project project, String name, Stage stage) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_STAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_STAGE_CREAT);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", name);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("sort", String.valueOf(Integer.valueOf(stage.sort)+1));
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, CREAT_STAGE_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void deleteStage(Context mContext, Handler mHandler, Project project,Stage stage) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_STAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_STAGE_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_stages_id", stage.stageId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, DELETE_STAGE_SUCCESS, mContext, nameValuePairs,stage);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void doRenameStage(Context mContext, Handler mHandler, Project project,String name, Stage stage) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_STAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_STAGE_RENAME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_stages_id", stage.stageId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", name);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, RENAME_STAGE_SUCCESS, mContext, nameValuePairs,stage);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void changeStage(Context mContext, Handler mHandler, Project project, Stage stage,Stage stage2)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_STAGE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_STAGE_SET);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_stages_id", stage.stageId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("sort", stage2.sort);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, CHANGE_STAGE_SUCCESS, mContext, nameValuePairs,stage);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }
}
