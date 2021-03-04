package intersky.oa;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class OaAsks {

    public static final String MODUL = "api/v1/Module.html";
    public static final String MODUL_PATH = "get.module.status";
    public static final String ATTACHMENT_PATH = "api/v1/Attachment.html";
    public static final String ATTACHMENT_PATH2 = "v/edit/upload";
    public static final String ATTACHMENT_GET = "get.attachment.item";
    public static final String ATTACHMENT_ADD = "add.attachment.item";
    public static final String ATTACHMENT_LIST = "add.attachment.list";
    public static final String ATTACHMENT_GET_INFO = "get.attachment.info";
    public static final int EVENT_GET_ATTCHMENT_SUCCESS = 1220000;
    public static final int EVENT_GET_OAHIT_SUCCESS = 1220001;

    public static void getAttachments(String hashs, Handler handler, Context context) {
        if (hashs.length() > 0) {

            String urlString = OaUtils.getOaUtils().createURLStringoa(OaUtils.getOaUtils().service,ATTACHMENT_PATH);
            ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new NameValuePair("method", ATTACHMENT_GET_INFO));
            nvps.add(new NameValuePair("hash", hashs));
            PostNetTask postNetTask = new PostNetTask(urlString, handler, EVENT_GET_ATTCHMENT_SUCCESS, context, nvps);
            NetTaskManager.getInstance().addNetTask(postNetTask);

        }

    }

    public static void getAttachments(String hashs, Handler handler, Context context, Object item) {
        if (hashs.length() > 0) {

            String urlString = OaUtils.getOaUtils().createURLStringoa(OaUtils.getOaUtils().service,ATTACHMENT_PATH);
            ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new NameValuePair("method", ATTACHMENT_GET_INFO));
            nvps.add(new NameValuePair("hash", hashs));
            PostNetTask postNetTask = new PostNetTask(urlString, handler, EVENT_GET_ATTCHMENT_SUCCESS, context, nvps, item);
            NetTaskManager.getInstance().addNetTask(postNetTask);

        }

    }

    public static void getOaHit(Handler handler, Context context, String userid,String conpanyid) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(OaUtils.getOaUtils().service,MODUL);
        ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("method", MODUL_PATH));
        nvps.add(new NameValuePair("user_id", userid));
        nvps.add(new NameValuePair("company_id", conpanyid));
        PostNetTask postNetTask = new PostNetTask(urlString, handler, EVENT_GET_OAHIT_SUCCESS, context, nvps);
        NetTaskManager.getInstance().addNetTask(postNetTask);
    }


}
