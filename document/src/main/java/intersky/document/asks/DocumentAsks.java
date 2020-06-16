package intersky.document.asks;

import android.content.Context;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import intersky.document.DocumentManager;
import intersky.document.entity.DocumentNet;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.HTTP;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.URLEncodedUtils;
import intersky.xpxnet.net.nettask.PostJsonListNetTask;
import intersky.xpxnet.net.nettask.PostJsonNetTask;

//00
public class DocumentAsks {

    public static final String PATH_DOCUMENT_IMG_DOWNLOAD = "doc/file/get";
    public static final String PATH_DOCUMENT_LIST = "doc/catalogue/list";
    public static final String PATH_DOCUMENT_LIST_SEARCH = "doc/file/search";
    public static final String PATH_DOCUMENT_DELETE_FILE = "doc/file/del";
    public static final String PATH_DOCUMENT_DELETE_DOUCMENT = "doc/catalogue/del";
    public static final String PATH_DOCUMENT_RENAME_DOCUMENT = "doc/catalogue/rename";
    public static final String PATH_DOCUMENT_RENAME_FILE = "doc/file/rename";
    public static final String PATH_DOCUMENT_CREAT= "doc/catalogue/add";
    public static final int DOCUMENT_PRE_LIST_SUCCESS = 1170000;
    public static final int DOCUMENT_BACK_LIST_SUCCESS = 1170001;
    public static final int DOCUMENT_UPDATE_LIST_SUCCESS = 1170002;
    public static final int DOCUMENT_SEARCH_LIST_SUCCESS = 1170003;
    public static final int DOCUMENT_DELETE_LIST_SUCCESS = 1170004;
    public static final int DOCUMENT_RENAME_SUCCESS = 1170005;
    public static final int DOCUMENT_CREAT_SUCCESS = 1170006;
    public static final int DOCUMENT_DELETE_LIST_FAIL = 1170007;
    public static String praseUrl(DocumentNet mDocumentItem) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("token", NetUtils.getInstance().token));
        nvps.add(new BasicNameValuePair("record_id", mDocumentItem.mRecordID));
        String url = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,DocumentAsks.PATH_DOCUMENT_IMG_DOWNLOAD, URLEncodedUtils.format(nvps, HTTP.UTF_8));
        return url;
    }

    public static String praseUrl(DocumentNet mDocumentItem,int widgh,int height) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("token", NetUtils.getInstance().token));
        nvps.add(new BasicNameValuePair("record_id", mDocumentItem.mRecordID));
        nvps.add(new BasicNameValuePair("width",String.valueOf(widgh)));
		nvps.add(new BasicNameValuePair("height",String.valueOf(height)));
        String url = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,DocumentAsks.PATH_DOCUMENT_IMG_DOWNLOAD, URLEncodedUtils.format(nvps, HTTP.UTF_8));
        return url;
    }

    public static void preDocumentList(Handler mHandler, Context mContext, DocumentNet mDocumentNet)
    {
        try {
            String urlString = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("owner_type", mDocumentNet.mOwnerType);
            jsonObject.put("parent_id", mDocumentNet.mRecordID);
            jsonObject.put("owner_id", mDocumentNet.mOwnerID);
            jsonObject.put("shared", mDocumentNet.mShared);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, DOCUMENT_PRE_LIST_SUCCESS, mContext, postBody,mDocumentNet);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void backDocumentList(Handler mHandler, Context mContext, DocumentNet mDocumentNet)
    {
        try {
            String urlString = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("owner_type", mDocumentNet.mOwnerType);
            jsonObject.put("parent_id", mDocumentNet.mRecordID);
            jsonObject.put("owner_id", mDocumentNet.mOwnerID);
            jsonObject.put("shared", mDocumentNet.mShared);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, DOCUMENT_BACK_LIST_SUCCESS, mContext, postBody,mDocumentNet);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void updataDocumentList(Handler mHandler, Context mContext, DocumentNet mDocumentNet) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_LIST);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("owner_type", mDocumentNet.mOwnerType);
            jsonObject.put("parent_id", mDocumentNet.mRecordID);
            jsonObject.put("owner_id", mDocumentNet.mOwnerID);
            jsonObject.put("shared", mDocumentNet.mShared);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, DOCUMENT_UPDATE_LIST_SUCCESS, mContext, postBody,mDocumentNet);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void searchDocumentList(Handler mHandler, Context mContext, DocumentNet mDocumentNet, String keyword) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_LIST_SEARCH);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("owner_type", mDocumentNet.mOwnerType);
            jsonObject.put("record_id", mDocumentNet.mRecordID);
            jsonObject.put("owner_id", mDocumentNet.mOwnerID);
            jsonObject.put("keyword", keyword);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, DOCUMENT_SEARCH_LIST_SUCCESS, mContext, postBody,mDocumentNet);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDocumentList(ArrayList<DocumentNet> documentNets, Handler mHandler, Context mContext ) {
        try {
            ArrayList<String> urls = new ArrayList<String>();
            ArrayList<String> postBodys = new ArrayList<String>();
            ArrayList<Object> objectsbject = new ArrayList<Object>();
            objectsbject.addAll(documentNets);
            for(int i = 0 ; i < documentNets.size() ; i++)
            {
                DocumentNet mDocumentNet = documentNets.get(i);
                if(mDocumentNet.mType == DocumentManager.TYPE_DOCUMENT) {
                    urls.add(NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_DELETE_DOUCMENT));
                }
                else {
                    urls.add(NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_DELETE_FILE));
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", NetUtils.getInstance().token);
                jsonObject.put("owner_type", mDocumentNet.mOwnerType);
                jsonObject.put("record_id", mDocumentNet.mRecordID);
                postBodys.add(jsonObject.toString());
            }
            if(urls.size() > 0)
            {
                PostJsonListNetTask mPostNetTask = new PostJsonListNetTask( mHandler, DOCUMENT_DELETE_LIST_SUCCESS,
                        DOCUMENT_DELETE_LIST_FAIL, mContext, urls,postBodys,objectsbject);
                NetTaskManager.getInstance().addNetTask(mPostNetTask);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doRename(DocumentNet mDocumentNet, String Name, Handler mHandler, Context mContext) {
        try {
            String urlString;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("record_id", mDocumentNet.mRecordID);
            if(mDocumentNet.mType == DocumentManager.TYPE_DOCUMENT)
            {
                urlString = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_RENAME_DOCUMENT);
                jsonObject.put("name",Name);
            }
            else
            {
                urlString = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_RENAME_FILE);
                String end = mDocumentNet.mName;
                if (end.lastIndexOf(".") != -1)
                    end = end.substring(end.lastIndexOf("."));
                jsonObject.put("name",Name + end);
            }
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, DOCUMENT_RENAME_SUCCESS, mContext, postBody,mDocumentNet);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void doCreatDocument(DocumentNet mDocumentNet, String Name, Handler mHandler, Context mContext)
    {
        try {
            String urlString;
            JSONObject jsonObject = new JSONObject();
            urlString = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,PATH_DOCUMENT_CREAT);
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("name",Name);
            jsonObject.put("owner_type", mDocumentNet.mOwnerType);
            jsonObject.put("parent_id", mDocumentNet.parentid);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, DOCUMENT_CREAT_SUCCESS, mContext, postBody,mDocumentNet);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
