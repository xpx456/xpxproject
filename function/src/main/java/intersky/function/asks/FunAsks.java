package intersky.function.asks;

import android.content.Context;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.entity.BusinessCardModel;
import intersky.function.entity.Function;
import intersky.function.entity.TableDetial;
import intersky.function.handler.BusinesHandler;
import intersky.function.view.activity.BusinesCardActivity;
import intersky.json.XpxJSONObject;
import intersky.mywidget.TableCloumArts;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostJsonNetTask;
import intersky.xpxnet.net.nettask.PostNetTask;

//00
public class FunAsks {

    public static final String PATH_EDIT_DTAT = "module/record/get";
    public static final String PATH_SUBDETIAL = "Data/Module/SubQuery.html";
    public static final String BOARD_DATA_PATH = "board/getdata";
    public static final String SEARCE_HEAD_PATH = "App/GetBoardInfo.html";
    public static final String GET_LINK_VALUSE = "App/GetBoardListItemsData.html";
    public static final String PATH_GET_FILL = "App/GetBoardFillData.html";
    public static final String GET_HIT = "project/badges";
    public static final String UPDATA_DATA = "App/BoardExecuteData.html";
    public static final String GRID_ATTACHMENT_GET_PATH = "module/attachment/get";
    public static final String SERVER_PATH = "sql";
    public static final String SERVER_PATH2 = "sqlBatch";

    public static final int BOARD_DATA_SUCCESS = 1180000;
    public static final int BOARD_CHART_DATA_SUCCESS = 1180001;
    public static final int BOARD_LABLE_DATA_SUCCESS = 1180002;
    public static final int SEARCE_HEAD_SUCCESS = 1180003;
    public static final int GET_SUB_SUCCESS = 1180004;
    public static final int GET_LINK_SUCCESS = 1180005;
    public static final int GET_FILL_SUCCESS = 1180006;
    public static final int GET_HIT_SUCCESS = 1180007;
    public static final int GRIDE_UPDATE_NEW_SUCCESS = 1180008;
    public static final int GRIDE_UPDATE_SUCCESS = 1180009;
    public static final int GRIDE_DELETE_SUCCESS = 1180010;
    public static final int GRIDE_UPDATE_NEW_START_SUCCESS = 1180011;
    public static final int GRIDE_UPDATE_START_SUCCESS = 1180012;

    public static void getBoardData(Context mContext, Handler mHandler, Function mFuncInfo, int page, String keyWord) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,BOARD_DATA_PATH);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            if (mFuncInfo.isSecond == false)
            {
                jsonObject.put("token", NetUtils.getInstance().token);
                jsonObject.put("name", mFuncInfo.mName);
                jsonObject.put("Keyword", keyWord);
                jsonObject.put("PageIndex", String.valueOf(page));
                jsonObject.put("RowName", "");
                jsonObject.put("ColName", "");
                jsonObject.put("CellValue",  "");
            }
            else
            {
                jsonObject.put("token", NetUtils.getInstance().token);
                jsonObject.put("name", mFuncInfo.mName);
                jsonObject.put("Keyword", keyWord);
                jsonObject.put("PageIndex", String.valueOf(page));
                jsonObject.put("RowName", mFuncInfo.mRowName);
                jsonObject.put("ColName", mFuncInfo.mColName);
                jsonObject.put("CellValue",  mFuncInfo.mCellValue);
            }
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler,BOARD_DATA_SUCCESS, mContext, postBody, page);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getChartBoardData(Context mContext, Handler mHandler, Function mFuncInfo) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,BOARD_DATA_PATH);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("name", mFuncInfo.mName);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler,BOARD_CHART_DATA_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getLableBoardData(Context mContext, Handler mHandler, Function mFuncInfo) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,BOARD_DATA_PATH);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("name", mFuncInfo.mName);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler,BOARD_LABLE_DATA_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSearchHead(Context mContext, Handler mHandler, Function mFuncInfo) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,SEARCE_HEAD_PATH);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("Name", mFuncInfo.mName);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler,SEARCE_HEAD_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSubGride(Context mContext, Handler mHandler, Function mFuncInfo) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,PATH_EDIT_DTAT);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("module", mFuncInfo.modulflag);
            jsonObject.put("record_id", mFuncInfo.mRecordId);
            jsonObject.put("group", mFuncInfo.mGrop);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler,GET_SUB_SUCCESS, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getLinkValue(Context mContext, Handler mHandler, Function mFuncInfo, TableDetial detial, TableCloumArts tableCloumArts)
    {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,GET_LINK_VALUSE);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("Name", mFuncInfo.modulflag);
            jsonObject.put("Series", mFuncInfo.series);
            jsonObject.put("FieldName", tableCloumArts.mFiledName);
            JSONObject mjson = new JSONObject();
            JSONObject jo = new JSONObject(detial.tempData);
            for (int i = 0; i < detial.tableCloums.size(); i++)
            {
                TableCloumArts mTableCloumArts = detial.tableCloums.get(i);
                mjson.put(mTableCloumArts.mFiledName, jo.getString(mTableCloumArts.mFiledName));

            }
            jsonObject.put("Data", mjson);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler,GET_LINK_SUCCESS, mContext, postBody,tableCloumArts);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getFill(Context mContext, Handler mHandler, Function mFuncInfo, TableDetial detial, TableCloumArts tableCloumArts) {
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,PATH_GET_FILL);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("Name", mFuncInfo.modulflag);
            jsonObject.put("Series", mFuncInfo.modulflag);
            JSONObject jo = new JSONObject(detial.tempData);
            jsonObject.put(tableCloumArts.mFiledName, jo.getString(tableCloumArts.mFiledName));

            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler,GET_FILL_SUCCESS, mContext, postBody,tableCloumArts);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getFunhit(Context mContext, Handler mHandler) {

        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,GET_HIT);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, GET_HIT_SUCCESS,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void editGride(Context mContext, Handler mHandler, Function function, TableDetial tableDetial, boolean isnew, boolean newview) {
        String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,UPDATA_DATA);
        ArrayList<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();
        try {
            XpxJSONObject data = new XpxJSONObject(tableDetial.recordData);
            XpxJSONObject temp = new XpxJSONObject(tableDetial.tempData);
            JSONObject mjson = new JSONObject();
            for(int i = 0 ; i < tableDetial.tableCloums.size() ; i++)
            {
                TableCloumArts tableCloumArts = tableDetial.tableCloums.get(i);
                String value = temp.getString(tableCloumArts.mFiledName);
                NameValuePair mNameValuePair = null;
                if(tableCloumArts.dataType.equals("cimage"))
                {
                    File mfile = new File(value);
                    if (mfile.exists()) {
                        mNameValuePair = new NameValuePair(tableCloumArts.mFiledName,mfile.getName());
                        mNameValuePair.isFile = true;
                        mNameValuePair.path = mfile.getPath();
                        mNameValuePairs.add(mNameValuePair);
                    }
                    else if(value.length() != 0)
                    {
                        mfile =  new File(tableCloumArts.localPath);
                        if(mfile.exists())
                        {
                            mNameValuePair = new NameValuePair(tableCloumArts.mFiledName,mfile.getName());
                            mNameValuePair.isFile = true;
                            mNameValuePair.path = mfile.getPath();
                            mNameValuePairs.add(mNameValuePair);
                        }

                    }
                }
                else
                {

                    if(tableCloumArts.mAttributes.contains("Requied"))
                    {
                        if(value.length() == 0)
                        {
                            AppUtils.showMessage(mContext, tableCloumArts.mCaption+mContext.getString(R.string.title_empty));
                            return;
                        }
                    }
                    mjson.put(tableCloumArts.mFiledName,
                            value);
                }
            }
            NameValuePair mNameValuePair1;

            if (isnew == false) {
                mNameValuePair1 = new NameValuePair("Name", function.mName);
                mNameValuePairs.add(mNameValuePair1);
                mNameValuePair1 = new NameValuePair("Action", "1");
                mNameValuePairs.add(mNameValuePair1);
                mNameValuePair1 = new NameValuePair("Name", function.series);
                mNameValuePairs.add(mNameValuePair1);

            }
            else {
                mNameValuePair1 = new NameValuePair("Name", function.mName);
                mNameValuePairs.add(mNameValuePair1);
                mNameValuePair1 = new NameValuePair("Action", "0");
                mNameValuePairs.add(mNameValuePair1);
                mNameValuePair1 = new NameValuePair("Name", function.series);
                mNameValuePairs.add(mNameValuePair1);
            }
            mNameValuePair1 = new NameValuePair("Data", mjson.toString());
            mNameValuePairs.add(mNameValuePair1);
            if(isnew == false)
            {
                if(newview == false)
                {
                    PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GRIDE_UPDATE_SUCCESS, mContext, mNameValuePairs);
                    NetTaskManager.getInstance().addNetTask(mPostNetTask);
                }
                else
                {
                    PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GRIDE_UPDATE_START_SUCCESS, mContext, mNameValuePairs);
                    NetTaskManager.getInstance().addNetTask(mPostNetTask);
                }
            }
            else
            {
                if(newview == false)
                {
                    PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GRIDE_UPDATE_NEW_SUCCESS, mContext, mNameValuePairs);
                    NetTaskManager.getInstance().addNetTask(mPostNetTask);
                }
                else
                {
                    PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GRIDE_UPDATE_NEW_START_SUCCESS, mContext, mNameValuePairs);
                    NetTaskManager.getInstance().addNetTask(mPostNetTask);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void deleteGride(Context mContext, Handler mHandler, Function function, String recordid) {

        try {
            JSONObject mjson = new JSONObject();
            mjson.put("RecordID", function.mRecordId);
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,UPDATA_DATA);
            ArrayList<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();
            NameValuePair mNameValuePair1;
            mNameValuePair1 = new NameValuePair("Name", function.mName);
            mNameValuePairs.add(mNameValuePair1);
            mNameValuePair1 = new NameValuePair("Action", "2");
            mNameValuePairs.add(mNameValuePair1);
            mNameValuePair1 = new NameValuePair("Name", function.series);
            mNameValuePairs.add(mNameValuePair1);
            mNameValuePair1 = new NameValuePair("Data", mjson.toString());
            mNameValuePairs.add(mNameValuePair1);
            PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, GRIDE_DELETE_SUCCESS, mContext, mNameValuePairs);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void sendtoServer(Context mContext, Handler mHandler, ArrayList<BusinessCardModel> maddMkxCards) {
        ArrayList<String> murls = new ArrayList<String>();
        try {
            String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,SERVER_PATH2);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("token", NetUtils.getInstance().token);
            for (int i = 0; i < maddMkxCards.size(); i++) {
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			try
//			{
//
//				nvps.add(new BasicNameValuePair("Command",
//						AppUtils.replaceBlank(Encryption
//								.encrypt(addCardSql(maddMkxCards.get(i))))));
//				nvps.add(new BasicNameValuePair("token", NetUtils.getInstance().token));
//			}
//			catch (Exception e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String urlString = NetUtils.getInstance()
//					.createURLString(BusinesCardActivity.SERVER_PATH,
//							URLEncodedUtils.format(nvps, HTTP.UTF_8));
//			murls.add(urlString);


                JSONArray jsonArray = new JSONArray();
                jsonObject1.put("job", jsonArray);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sqlname", "InsertBusinessCard");
                jsonObject.put("method", "execute");
                jsonObject.put("params", addCardSql(maddMkxCards.get(i)));
                jsonArray.put(jsonObject);
            }
            String postBody = jsonObject1.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, BusinesHandler.EVENT_UPLOADFINISH, mContext, postBody);
//            NetListTask mNetListTask = new NetListTask(murls, "", mBusinesHandler, 0, BusinesCardActivity.EVENT_UPLOADFAIL, mBusinesCardActivity, BusinesCardActivity.EVENT_UPLOADFINISH, 0);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static JSONArray addCardSql(BusinessCardModel mMkxCard) {
//		String data = "insert into BusinessCard (CardUuid,Name,Duty,Mobile1,Mobile2,Email,Tel1,Tel2,Fax,Cname,Address,Website,Createtime,UserName,Updatetime)"
//				+ " values(" + "'"
//				+ mMkxCard.carduuid
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.name
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.duty
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.mobile1
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.mobile2
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.email
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.tel1
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.tel2
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.fax
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.cname
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.address
//				+ "'"
//				+ ","
//				+ "'"
//				+ mMkxCard.website
//				+ "'"
//				+ ","
//				+ "'"
//				+ AppUtils.getDateAndTime()
//				+ "'"
//				+ ","
//				+ "'"
//				+ InterskyApplication.mApp.mUser.getUsernName()
//				+ "'"
//				+ ","
//				+ "'"
//				+ AppUtils.getTime() + "'" + ")";


//		JSONObject mjson = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
//			mjson.put("type", "2");
//			mjson.put("data", data);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.carduuid);
            jsonObject.put("addon", "");
            jsonObject.put("name", "CardUuid");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.name);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Name");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.duty);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Duty");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.mobile1);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Mobile1");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.mobile2);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Mobile2");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.email);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Email");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.tel1);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Tel1");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.tel2);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Tel2");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.fax);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Fax");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.cname);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Cname");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.address);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Address");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", mMkxCard.website);
            jsonObject.put("addon", "");
            jsonObject.put("name", "Website");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", TimeUtils.getDateAndTime());
            jsonObject.put("addon", "");
            jsonObject.put("name", "Createtime");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", FunctionUtils.getInstance().account.mUserName);
            jsonObject.put("addon", "");
            jsonObject.put("name", "UserName");
            jsonObject.put("type", "");
            ja.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("value", TimeUtils.getTime());
            jsonObject.put("addon", "");
            jsonObject.put("name", "Updatetime");
            jsonObject.put("type", "");
            ja.put(jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return ja;
    }
}
