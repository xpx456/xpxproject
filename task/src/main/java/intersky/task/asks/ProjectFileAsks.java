package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Project;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//01
public class ProjectFileAsks {


    public static final String PROJECT_FILE_PATH = "api/v1/ProjectFile";
    public static final String PROJECT_FILE_MOVE = "set.project.file.id";
    public static final String PROJECT_FILE_CREAT = "add.project.file.item";
    public static final String PROJECT_FILE_RENAME = "set.project.file.item";
    public static final String PROJECT_FILE_DELETE = "del.project.file.item";
    public static final String PROJECT_FILE_LIST = "get.project.file.list";

    public static final int PROJECT_FILE_LIST_SUCCESS = 1250100;
    public static final int PROJECT_FILE_RENAME_SUCCESS = 1250101;
    public static final int PROJECT_FILE_DELTETE_SUCCESS = 1250102;
    public static final int PROJECT_FILE_CREAT_SUCCESS = 1250103;
    public static final int PROJECT_MOVE_SUCCESS = 1250104;
    public static final int PROJECT_MOVE_OUT_SUCCESS = 1250105;

    public static void getFileNames(Context mContext, Handler mHandler,ArrayList<Project> files) {

        String ids = "";
        for(int i = 0 ; i < files.size() ; i++)
        {
            if(ids.length() == 0)
            {
                ids += files.get(i).fileid;
            }
            else
            {
                ids += ","+files.get(i).fileid;
            }
        }

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_FILE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_FILE_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_file_id", ids);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("page_size", "20");
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_FILE_LIST_SUCCESS, mContext, nameValuePairs,files);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void rename(Context mContext, Handler mHandler,Project file, String name) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_FILE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_FILE_RENAME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_file_id", file.fileid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", name);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_FILE_RENAME_SUCCESS, mContext, nameValuePairs,file);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


    public static void delete(Context mContext, Handler mHandler,Project file) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_FILE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_FILE_DELETE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_file_id", file.fileid);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_FILE_DELTETE_SUCCESS, mContext, nameValuePairs,file);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void creat(Context mContext, Handler mHandler,Project item,String name) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_FILE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_FILE_CREAT);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", item.projectId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("name", name);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_FILE_CREAT_SUCCESS, mContext, nameValuePairs,item);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void move(Context mContext, Handler mHandler,Project item) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_FILE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_FILE_MOVE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_file_id", item.nfileid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", item.projectId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_MOVE_SUCCESS, mContext, nameValuePairs,item);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void moveout(Context mContext, Handler mHandler,Project item) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,PROJECT_FILE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", PROJECT_FILE_MOVE);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_file_id", "0");
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("project_id", item.projectId);
        nameValuePairs.add(mNameValuePair);
        item.nfileid = "0";
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PROJECT_MOVE_OUT_SUCCESS, mContext, nameValuePairs,item);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
