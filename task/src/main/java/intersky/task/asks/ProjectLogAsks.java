package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Project;
import intersky.task.view.activity.ProjectDetialActivity;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
//02
public class ProjectLogAsks {

    public static final String PROJECT_LOG_PATH = "api/v1/ProjectLog";
    public static final String PROJECT_LOG_LIST= "get.project.log.list";

    public static final int GET_LOG_SUCCESS = 1250200;

    public static void getLogs(Context mContext, Handler mHandler, Project project,int page)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_LOG_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_LOG_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", project.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_no", String.valueOf(page));
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "20");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_LOG_SUCCESS, mContext, nameValuePairs,project);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

}
