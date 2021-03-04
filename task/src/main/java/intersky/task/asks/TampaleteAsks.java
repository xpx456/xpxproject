package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.TemplateType;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

//06
public class TampaleteAsks {


    public static final String TASK_TAMPALTE_TYPE_PATH = "api/v1/TemplateType";
    public static final String TASK_TAMPALTE_TYPE_LIST = "get.template.type.list";
    public static final String TASK_TAMPALTE_PATH = "api/v1/Template";
    public static final String TASK_TAMPALTE_LIST = "get.template.list";
    public static final int GET_TYPE_SUCCESS = 1250600;
    public static final int GET_LIST_SUCCESS = 1250601;
    public static void getTampaleteType(Context mContext, Handler mHandler)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_TAMPALTE_TYPE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_TAMPALTE_TYPE_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_TYPE_SUCCESS, mContext, nameValuePairs);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getTampalets(Context mContext, Handler mHandler,TemplateType mTemplateTypeModel) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,TASK_TAMPALTE_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", TASK_TAMPALTE_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("user_id", TaskManager.getInstance().oaUtils.mAccount.mRecordId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("company_id", TaskManager.getInstance().oaUtils.mAccount.mCompanyId);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("template_type_id", mTemplateTypeModel.typeid);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("type", mTemplateTypeModel.type);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_LIST_SUCCESS, mContext, nameValuePairs,mTemplateTypeModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
