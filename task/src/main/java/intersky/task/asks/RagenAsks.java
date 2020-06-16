package intersky.task.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.oa.OaUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Contral;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
//05
public class RagenAsks {

    public static final String REGIN_PATH = "api/v1/Region";
    public static final String REGIN_LIST = "get.region.list";
    public static final String REGIN_NAME= "get.region.name";

    public static final int GET_REGIN_LIST_SUCCESS = 1250500;
    public static final int GET_REGIN_NAME_SUCCESS = 1250501;

    public static void setLastRagenName(Context mContext, Handler mHandler, Contral contralModel, String name) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,REGIN_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", REGIN_NAME);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("region_id", name);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_REGIN_NAME_SUCCESS, mContext, nameValuePairs,contralModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


    public static void setRagen(Context mContext, Handler mHandler, Contral contralModel, String id) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,REGIN_PATH);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair = new NameValuePair("method", REGIN_LIST);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("parent_id", id);
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GET_REGIN_LIST_SUCCESS, mContext, nameValuePairs,contralModel);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
