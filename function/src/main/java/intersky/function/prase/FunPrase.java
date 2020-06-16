package intersky.function.prase;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import intersky.appbase.ScreenDefine;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.asks.FunAsks;
import intersky.function.receiver.entity.ChartData;
import intersky.function.receiver.entity.ChartDataItem;
import intersky.function.receiver.entity.FunData;
import intersky.function.receiver.entity.Function;
import intersky.function.receiver.entity.LableData;
import intersky.function.receiver.entity.LableDataItem;
import intersky.function.receiver.entity.TableDetial;
import intersky.function.handler.GridDetialHandler;
import intersky.function.view.activity.WebMessageActivity;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mywidget.Data;
import intersky.mywidget.GrideData;
import intersky.mywidget.SearchHead;
import intersky.mywidget.TableCloumArts;
import intersky.mywidget.TableItem;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.HTTP;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.URLEncodedUtils;


public class FunPrase {

    public static void praseBoardData(String json, FunData mFunData, int page,ScreenDefine mScreenDefine) {
        try {
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;


            XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
            XpxJSONObject jo = (XpxJSONObject) xpxJsonObject.getJSONObject("data");
            String nextAppName = jo.getString("AppName");
            boolean isLineClick = !jo.getBoolean("IsRowClick", false);
            String nextTypeName = jo.getString("NextTypeName");
            String seriesName = jo.getString("Series");
            boolean canedit = jo.getBoolean("CanEdit", false);
            boolean showSearch = jo.getBoolean("ShowSearch", false);
            String module = jo.getString("Module");
            XpxJSONArray ja = jo.getJSONArray("Items");
            mFunData.showSearch = showSearch;
            for (int i = 0; i < ja.length(); i++) {
                XpxJSONObject jo1 = (XpxJSONObject) ja.getJSONObject(i);
                XpxJSONArray jaI = jo1.getJSONArray("Columns");
                GrideData data = (GrideData) mFunData.funDatas.get(jo1.getString("Series"));
                if (data == null) {
                    data = new GrideData();
                    data.gridPage = page;
                    data.nextAppType = nextTypeName;
                    data.nextAppName = nextAppName;
                    data.nextAppCaption = seriesName;
                    data.modul = module;
                    data.isLineClick = isLineClick;
                    data.canEdit = canedit;
                    data.head = jaI.toString();
                    data.showSearch = showSearch;
                    mFunData.funDatas.put(jo1.getString("Series"), data);
                    mFunData.mKeys.add(jo1.getString("Series"));
                    for (int j = 0; j < jaI.length(); j++) {
                        XpxJSONObject tmp = (XpxJSONObject) jaI.getJSONObject(j);
                        TableCloumArts mTableCloumArts = new TableCloumArts();
                        mTableCloumArts.mCaption = tmp.getString("Caption");
                        mTableCloumArts.mFiledName = tmp.getString("Field");
                        mTableCloumArts.mWidth = tmp.getInt("Width", 80);
                        mTableCloumArts.mFieldType = tmp.getString("FieldType");
                        mTableCloumArts.dataType = mTableCloumArts.mFieldType;
                        mTableCloumArts.isReadOnly = tmp.getBoolean("ReadOnly", false);
                        if(tmp.has("Visible"))
                        mTableCloumArts.isVisiable = tmp.getBoolean("Visible", true);
                        if(tmp.has("Visble"))
                        mTableCloumArts.isVisiable = tmp.getBoolean("Visble", true);
                        mTableCloumArts.isGrideVisiable = mTableCloumArts.isVisiable;
                        if (mTableCloumArts.mFiledName.toLowerCase().equals("recordid")) {
                            mTableCloumArts.dataType = TableCloumArts.GRIDE_DATA_TYPE_RECORDID;
                            mTableCloumArts.isVisiable = false;
                        }
                        if (mTableCloumArts.isVisiable == false || mTableCloumArts.dataType.equals(TableCloumArts.GRIDE_DATA_TYPE_IMAGE))

                            mTableCloumArts.isGrideVisiable = false;

                        TableItem mTableItem = new TableItem(mTableCloumArts.mFiledName,mTableCloumArts.mCaption, true, mTableCloumArts.mWidth, (int) mScreenDefine.density);
                        if (data.tabkeBase == null) {
                            if(mTableCloumArts.isGrideVisiable)
                            data.tabkeBase = mTableItem;
                        } else {
                            data.tableHead.add(mTableItem);
                        }
                        data.tableCloums.add(mTableCloumArts);
                        data.tableCloumArtsHashMap.put(mTableCloumArts.mFiledName,mTableCloumArts);
                    }
                }
                XpxJSONArray jaK;
                if (jo1.has("Data"))
                    jaK = jo1.getJSONArray("Data");
                else
                    jaK = new XpxJSONArray("[]");
                for (int k = 0; k < jaK.length(); k++) {
                    XpxJSONObject joo = (XpxJSONObject) jaK.getJSONObject(k);
                    String recordid = "";
                    ArrayList<TableItem> lines = new ArrayList<TableItem>();
                    for (int n = 0; n < data.tableCloums.size(); n++) {
                        TableCloumArts t = data.tableCloums.get(n);
                        String value = null;
                        if (t.mFiledName.equals("OrderAmount")) {
                            DecimalFormat df = new DecimalFormat("0.00");
                            value = df.format(joo.getDouble(t.mFiledName, 0.00));
                        } else {
                            value = joo.getString(t.mFiledName);
                        }
                        if (t.dataType.equals(TableCloumArts.GRIDE_DATA_TYPE_RECORDID)) {
                            recordid = value;
                        }
                        TableItem mTableItem = new TableItem(t.mFiledName,value, false, t.mWidth,(int) mScreenDefine.density);
                        if (data.tableLeft.size() == k) {
                            if(t.isGrideVisiable)
                            data.tableLeft.add(mTableItem);
                        } else {
                            data.tableGrid.add(mTableItem);
                        }


                    }
                    if (recordid.length() == 0) {
                        recordid = String.valueOf(k);
                    }
                    data.dataKeys.add(recordid);
                    data.tableContent.put(recordid, joo.toString());

                }
            }
            mFunData.page = page + 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseChart(NetObject net, FunData mFunData) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            XpxJSONObject jsonObject = new XpxJSONObject(json);
            if (mFunData.type.equals(Function.COLUMNS)) {
                XpxJSONObject orderData = jsonObject.getJSONObject("data");
                XpxJSONArray jArray = orderData.getJSONArray("Items");
                ChartData data = (ChartData) mFunData.funDatas.get("0");
                if (data == null) {
                    data = new ChartData(mFunData.type);
                    mFunData.funDatas.put("0", data);
                }
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray.isNull(i)) continue;
                    XpxJSONObject tmpJO = jArray.getJSONObject(i);
                    String series = tmpJO.getString("Series");
                    XpxJSONArray tmpJA = tmpJO.getJSONArray("Data");
                    data.xLable.add(series);
                    for (int j = 0; j < tmpJA.length(); j++) {
                        XpxJSONObject tmpJO_1 = tmpJA.getJSONObject(j);
                        XpxJSONArray tmpJOa_1 = tmpJO_1.getJSONArray("Data");
                        for (int k = 0; k < tmpJOa_1.length(); k++) {
                            XpxJSONObject tmpJO_2 = (XpxJSONObject) tmpJOa_1.getJSONObject(k);
                            String caption = tmpJO_2.getString("Caption");
                            if(!data.xhash.containsKey(caption))
                            {
                                data.xhash.put(caption,caption);
                                data.xLable.add(caption);
                            }
                            ChartDataItem chartDataItem = data.mDataForm.get(series);
                            if (chartDataItem == null) {
                                chartDataItem = new ChartDataItem();
                                data.mDataForm.put(series, chartDataItem);
                            }
                            if (data.unit.length() == 0) {
                                data.unit = tmpJO_2.getString("Unit");
                            }
                            chartDataItem.mData.put(caption,tmpJO_2.getDouble("Value", 0));
                        }

                    }
                }
            } else if (mFunData.type.equals(Function.BAR) || mFunData.type.equals(Function.LINE)) {
                XpxJSONObject orderData = jsonObject.getJSONObject("data");
                XpxJSONArray jArray = orderData.getJSONArray("Items");

                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray.isNull(i)) continue;
                    XpxJSONObject tmpJO = jArray.getJSONObject(i);
                    String series = tmpJO.getString("Series");
                    XpxJSONArray tmpJA = tmpJO.getJSONArray("Data");
                    ChartData data = (ChartData) mFunData.funDatas.get("series");
                    if (data == null) {
                        data = new ChartData(mFunData.type);
                        mFunData.funDatas.put(series, data);
                        mFunData.mKeys.add(series);
                    }
                    ChartDataItem chartDataItem = data.mDataForm.get(data.dataName);
                    if (chartDataItem == null) {
                        chartDataItem = new ChartDataItem();
                        data.mDataForm.put(data.dataName, chartDataItem);
                    }
                    for (int j = 0; j < tmpJA.length(); j++) {
                        XpxJSONObject tmpJO_1 = tmpJA.getJSONObject(j);
                        data.xLable.add(tmpJO_1.getString("Caption"));
                        chartDataItem.mData.put(tmpJO_1.getString("Caption"),tmpJO_1.getDouble("Value", 0));
                        if (data.unit.length() == 0) {
                            data.unit = tmpJO_1.getString("Unit");
                        }
                    }
                }
            } else if (mFunData.type.equals(Function.PIE) || mFunData.type.equals(Function.FUNNEL)) {
                XpxJSONObject jo = new XpxJSONObject(json);
                XpxJSONArray ja = jo.getJSONObject("data").getJSONArray("Items");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    String series = jo.getString("Series");
                    ChartData data = (ChartData) mFunData.funDatas.get("series");
                    if (data == null) {
                        data = new ChartData(mFunData.type);
                        mFunData.funDatas.put(series, data);
                        mFunData.mKeys.add(series);
                    }
                    XpxJSONArray ja2 = jo.getJSONArray("Data");
                    ChartDataItem chartDataItem = data.mDataForm.get(data.dataName);
                    if (chartDataItem == null) {
                        chartDataItem = new ChartDataItem();
                        data.mDataForm.put(data.dataName, chartDataItem);
                    }
                    for (int j = 0; j < ja2.length(); j++) {
                        XpxJSONObject jo2 = ja2.getJSONObject(j);
                        data.xLable.add(jo2.getString("Caption"));
                        chartDataItem.mData.put(jo2.getString("Caption"),jo2.getDouble("Value", 0));
                        if (data.unit.length() == 0) {
                            data.unit = jo2.getString("Unit");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void praseLable(NetObject net, FunData mFunData) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            XpxJSONObject jo = new XpxJSONObject(json);
            jo = jo.getJSONObject("data");
            XpxJSONArray ja = jo.getJSONArray("Items");

            for (int i = 0; i < ja.length(); i++) {
                String serias = jo.getString("Series");
                LableData mLableData = (LableData) mFunData.funDatas.get(serias);
                if (mLableData == null) {
                    mLableData = new LableData();
                    mFunData.funDatas.put(serias, mLableData);
                    mFunData.mKeys.add(serias);
                }

                jo = ja.getJSONObject(i);
                XpxJSONArray jaI = jo.getJSONArray("Columns");

                for (int j = 0; j < jaI.length(); j++) {
                    XpxJSONObject joJ = jaI.getJSONObject(j);
                    String caption = joJ.getString("Caption");
                    String field = joJ.getString("Field");
                    if (j == 0) {
                        mLableData.caption1 = caption;
                        mLableData.field1 = field;
                    } else if (j == 1) {
                        mLableData.caption2 = caption;
                        mLableData.field2 = field;
                    }
                }
                jaI = jo.getJSONArray("Data");
                for (int j = 0; j < jaI.length(); j++) {
                    XpxJSONObject joJ = jaI.getJSONObject(j);
                    mLableData.lableDataItems.add(new LableDataItem(joJ.getString(mLableData.field1),
                            joJ.getString(mLableData.field2)));
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void praseSeachHead(String json, GrideData data) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONObject("Data").getJSONArray("Items");
            for (int i = 0; i < ja.length(); i++) {
                XpxJSONObject seriesdata = ja.getJSONObject(i);
                if (seriesdata.has("Params")) {
                    XpxJSONArray ja2 = seriesdata.getJSONArray("Params");
                    for (int j = 0; j < ja2.length(); j++) {
                        XpxJSONObject jo = ja2.getJSONObject(j);
                        SearchHead mSearchHeadModel = new SearchHead(jo.getString("Caption"), jo.getString("Field"), jo.getString("DefaultValue"), jo.getString("FieldType"));
                        if (jo.has("Values")) {
                            XpxJSONArray ja3 = seriesdata.getJSONArray("Values");
                            for (int k = 0; k < ja3.length(); k++) {
                                mSearchHeadModel.valueSelect.add(ja3.getString(k));
                            }
                        }
                        data.mSearchHeads.add(mSearchHeadModel);
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void praseFunctionModule(NetObject net, FunData mFunData, String heads, int page,ScreenDefine mScreenDefine) {

        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            XpxJSONObject jo = new XpxJSONObject(json);
            XpxJSONObject jo1 = jo.getJSONObject("data");
            XpxJSONArray ja = jo1.getJSONArray("Items");
            if (ja.length() == 0)
            {
                for(int i = 0 ; i < mFunData.mKeys.size() ; i++)
                {
                    Data data = mFunData.funDatas.get(mFunData.mKeys.get(i));
                    if(data.dataType.equals(Function.GRID))
                    {
                        GrideData grideData = (GrideData) data;
                        grideData.isall = true;
                    }
                }
                return;
            }

            if(heads.length() > 0)
            {
                XpxJSONObject jsonObject = new XpxJSONObject(heads);
                XpxJSONArray hja = jsonObject.getJSONObject("Data").getJSONArray("Items");
            }
            for (int s = 0; s < ja.length(); s++) {
                XpxJSONObject seriesdata = ja.getJSONObject(s);
                String seriestype = seriesdata.getString("TypeName");
                Data data = creatData(seriestype, mFunData, seriesdata.getString("Series"));
                if (seriestype.equals(Function.GRID)) {
                    GrideData grideData = (GrideData) data;
                    if (heads.length() > 0)
                        praseSeachHead(heads, grideData);
                    if (seriesdata.has("AppName")) {
                        String nextAppName = seriesdata.getString("AppName");
                        boolean isLineClick = !seriesdata.getBoolean("IsRowClick", false);
                        String nextTypeName = seriesdata.getString("NextTypeName");
                        String seriesName = seriesdata.getString("Series");
                        boolean canedit = seriesdata.getBoolean("CanEdit", false);
                        boolean showSearch = seriesdata.getBoolean("ShowSearch", false);
                        String module = seriesdata.getString("Module");
                        grideData.nextAppName = seriesdata.getString("AppName");
                        grideData.nextAppCaption = seriesName;
                        grideData.nextAppType = nextTypeName;
                        grideData.modul = module;
                        grideData.canEdit = canedit;
                        grideData.isLineClick = !seriesdata.getBoolean("IsRowClick", false);
                        grideData.canEdit = seriesdata.getBoolean("CanEdit", false);
                        grideData.gridPage = page;
                    }
                    if(seriesdata.get("ShowSearch").toString().length() > 2)
                    {
                        grideData.showSearch = seriesdata.getBoolean("ShowSearch", false);
                        grideData.showBoardSearch = seriesdata.getBoolean("ShowBarcodeSearch", false);
                    }
                    else
                    {
                        if (seriesdata.getInt("ShowSearch",0) == 1) {
                            grideData.showSearch = true;
                            if (seriesdata.getInt("ShowBarcodeSearch",0) == 1) {
                                grideData.showBoardSearch = true;
                            }

                        }
                    }
                    grideData.gridPage = page;
                    makeGrid(seriesdata, grideData,mScreenDefine);
                } else if (seriestype.equals(Function.PIE) || seriestype.equals(Function.FUNNEL) || seriestype.equals(Function.COLUMNS) || seriestype.equals(Function.LINE)
                        || seriestype.equals(Function.BAR) || seriestype.equals(Function.COLUMN)) {
                    if(page <= 1)
                    {
                        ChartData chartData = (ChartData) data;
                        makeChart(seriesdata, chartData);
                    }
                } else if (seriestype.equals(Function.LABEL)) {
                    if(page <= 1)
                    {
                        LableData lableData = (LableData) data;
                        makeLable(seriesdata, lableData);
                    }
                }

                mFunData.page = page + 1;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static Data creatData(String type, FunData mFunData, String serias) {
        Data mdata = mFunData.funDatas.get(serias);
        if (type.equals(Function.GRID)) {
            GrideData data;
            if (mdata == null) {
                data = new GrideData();
                mFunData.funDatas.put(serias, data);
                mFunData.mKeys.add(serias);
            } else {
                data = (GrideData) mdata;
            }
            return data;
        } else if (type.equals(Function.LABEL)) {
            LableData data;
            if (mdata == null) {
                data = new LableData();
                mFunData.funDatas.put(serias, data);
                mFunData.mKeys.add(serias);
            } else {
                data = (LableData) mdata;
            }
            return data;
        } else if (type.equals(Function.PIE) || type.equals(Function.FUNNEL) || type.equals(Function.COLUMNS) || type.equals(Function.LINE) || type.equals(Function.BAR) || type.equals(Function.COLUMN)) {
            ChartData data;
            if (mdata == null) {
                data = new ChartData(type);
                mFunData.funDatas.put(serias, data);
                mFunData.mKeys.add(serias);
            } else {
                data = (ChartData) mdata;
            }
            return data;
        }
        return null;
    }

    public static void initLink(ArrayList<TableCloumArts> mTableinfo) {
        for (int i = 0; i < mTableinfo.size(); i++) {
            TableCloumArts temp = mTableinfo.get(i);
            if (temp.isLinkage == true) {
                for (int j = 0; j < mTableinfo.size(); j++) {
                    TableCloumArts temp2 = mTableinfo.get(j);
                    if (temp.mLinkageFields.contains(temp2.mFiledName)) {
                        if (temp2.mToLinkageFields.length() > 0) {
                            temp2.mToLinkageFields = temp2.mToLinkageFields + "," + temp.mFiledName;
                        } else {
                            temp2.mToLinkageFields = temp.mFiledName;
                        }
                    }
                }
            }
        }
    }

    public static void praseLinkValue(String json, TableCloumArts tableCloumArts, TableDetial tableDetial) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray mval = jsonObject.getJSONArray("Data");
            JSONObject data = new JSONObject(tableDetial.tempData);
            String v = "";
            for (int j = 0; j < mval.length(); j++) {
                if (j != mval.length() - 1) {
                    v += mval.getString(j) + ",";
                } else {
                    v += mval.getString(j);
                }

            }
            tableCloumArts.mValues = v;
            if (tableCloumArts.view != null) {
                TextView textView = (TextView) tableCloumArts.view;
                textView.setText(mval.getString(0));
                data.put(tableCloumArts.mFiledName,mval.getString(0));
                tableDetial.tempData = data.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseFill(NetObject net, TableDetial mTableDetial, Handler mHandler) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            XpxJSONObject mjson = new XpxJSONObject(json);
            XpxJSONObject data = mjson.getJSONObject("Data");
            JSONObject data2 = new JSONObject(mTableDetial.tempData);
            boolean ischange = false;
            for (int i = 0; i < mTableDetial.tableCloums.size(); i++) {
                TableCloumArts mTableCloumArts = mTableDetial.tableCloums.get(i);
                if (data.has(mTableCloumArts.mFiledName)) {
                    if (!data2.getString(mTableCloumArts.mFiledName).equals(data.getString(mTableCloumArts.mFiledName)))
                        data2.put(mTableCloumArts.mFiledName, data.getString(mTableCloumArts.mFiledName));
                    ischange = true;
                }
            }
            if (ischange == true) {
                mTableDetial.tempData = data2.toString();
                if (mHandler != null)
                    mHandler.sendEmptyMessage(GridDetialHandler.UPDATE_VIEW);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void praseHit(String json, ArrayList<Function> mFunctions) {
        int count = 0;
        int count1 = 0;
        try {
            JSONObject jsonboj = new JSONObject(json);
            count1 = jsonboj.getInt("reminder");
            count = jsonboj.getInt("workflow");
            mFunctions.get(0).hintCount = count1;
            mFunctions.get(1).hintCount = count;
        } catch (JSONException e) {

        }

    }

    public static void praseGrideData(Function function, TableDetial tableDetial) {
        try {
            XpxJSONArray jaI = new XpxJSONArray(function.mColName);
            for (int i = 0; i < jaI.length(); i++) {
                XpxJSONObject tmp = (XpxJSONObject) jaI.getJSONObject(i);
                TableCloumArts mTableCloumArts = new TableCloumArts();
                mTableCloumArts.mCaption = tmp.getString("Caption");
                if(mTableCloumArts.mCaption.length() == 0)
                {
                    mTableCloumArts.mCaption = tmp.getString("caption");
                }
                mTableCloumArts.mFiledName = tmp.getString("Field");
                if(mTableCloumArts.mFiledName.length() == 0)
                {
                    mTableCloumArts.mFiledName = tmp.getString("fieldname");
                }
                mTableCloumArts.mWidth = tmp.getInt("Width", 80);
                mTableCloumArts.mFieldType = tmp.getString("FieldType");
                mTableCloumArts.dataType = mTableCloumArts.mFieldType;
                mTableCloumArts.isReadOnly = tmp.getBoolean("ReadOnly", false);
                if(tmp.has("Visible"))
                mTableCloumArts.isVisiable = tmp.getBoolean("Visible", true);
                if(tmp.has("Visble"))
                mTableCloumArts.isVisiable = tmp.getBoolean("Visble", true);
                mTableCloumArts.isGrideVisiable = mTableCloumArts.isVisiable;
                if (mTableCloumArts.mFiledName.toLowerCase().equals("recordid")) {
                    mTableCloumArts.dataType = TableCloumArts.GRIDE_DATA_TYPE_RECORDID;
                    mTableCloumArts.isVisiable = false;
                }
                if (mTableCloumArts.isVisiable == false || mTableCloumArts.dataType.equals(TableCloumArts.GRIDE_DATA_TYPE_IMAGE))
                    mTableCloumArts.isGrideVisiable = false;
                tableDetial.tableCloums.add(mTableCloumArts);

            }
            tableDetial.recordData = function.mCellValue;
            tableDetial.tempData = function.mCellValue;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void makeGrid(XpxJSONObject seriesdata, GrideData data,ScreenDefine mScreenDefine) {

        try {
            XpxJSONArray ja = seriesdata.getJSONArray("Columns");
            XpxJSONArray ja2 = null;
            if (seriesdata.has("Data"))
                ja2 = seriesdata.getJSONArray("Data");
            if (data.tableCloums.size() == 0) {
                data.head = ja.toString();
                for (int i = 0; i < ja.length(); i++) {
                    XpxJSONObject column = ja.getJSONObject(i);
                    String caption = String.valueOf(column.getString("Caption"));
                    String field = String.valueOf(column.getString("Field"));


                    TableCloumArts mTableCloumArts = new TableCloumArts();
                    mTableCloumArts.mCaption = column.getString("Caption");
                    mTableCloumArts.mFiledName = column.getString("Field");
                    mTableCloumArts.mWidth = column.getInt("Width", 80);
                    mTableCloumArts.mFieldType = column.getString("FieldType");
                    mTableCloumArts.dataType = mTableCloumArts.mFieldType;
                    mTableCloumArts.isReadOnly = column.getBoolean("ReadOnly", false);
                    if(column.has("Visible"))
                    mTableCloumArts.isVisiable = column.getBoolean("Visible", true);
                    if(column.has("Visble"))
                    mTableCloumArts.isVisiable = column.getBoolean("Visble", true);
                    mTableCloumArts.isGrideVisiable = mTableCloumArts.isVisiable;
                    mTableCloumArts.isFill = column.getBoolean("IsFill", false);
                    mTableCloumArts.mDefault = column.getString("Default");
                    mTableCloumArts.mAttributes = column.getString("Attributes");
                    if (column.has("Values")) {
                        XpxJSONArray mval = column.getJSONArray("Values");
                        String v = "";
                        for (int j = 0; j < mval.length(); j++) {
                            if (j != mval.length() - 1) {
                                v += mval.getString(j) + ",";
                            } else {
                                v += mval.getString(j);
                            }

                        }
                        mTableCloumArts.mValues = v;
                    }
                    mTableCloumArts.isLinkage = column.getBoolean("Linkage", false);
                    mTableCloumArts.mLinkageFields = column.getString("LinkageFields");
                    if (mTableCloumArts.mFiledName.toLowerCase().equals("recordid")) {
                        mTableCloumArts.dataType = TableCloumArts.GRIDE_DATA_TYPE_RECORDID;
                        mTableCloumArts.isVisiable = false;
                        mTableCloumArts.isGrideVisiable = false;
                    }
                    if ((mTableCloumArts.mFieldType.toLowerCase().equals("dtimage") || mTableCloumArts.mFieldType.toLowerCase().equals("cimage"))) {
                        mTableCloumArts.dataType = TableCloumArts.GRIDE_DATA_TYPE_IMAGE;
                        mTableCloumArts.isGrideVisiable = false;
                    }
                    TableItem mTableItem = new TableItem(mTableCloumArts.mFiledName,mTableCloumArts.mCaption, true, mTableCloumArts.mWidth, (int) mScreenDefine.density);
                    if (data.tabkeBase == null) {
                        if(mTableCloumArts.isGrideVisiable)
                        data.tabkeBase = mTableItem;
                    } else {
                        data.tableHead.add(mTableItem);
                    }
                    data.tableCloums.add(mTableCloumArts);
                    data.tableCloumArtsHashMap.put(mTableCloumArts.mFiledName,mTableCloumArts);
                }

            }

            initLink(data.tableCloums);
            if (ja2 != null) {
                int base = data.tableLeft.size();
                for (int k = 0; k < ja2.length(); k++) {
                    XpxJSONObject itemdata = ja2.getJSONObject(k);
                    String recordid = "";
                    for (int n = 0; n < data.tableCloums.size(); n++) {
                        TableCloumArts t = data.tableCloums.get(n);
                        String value = null;
                        if (t.mFiledName.equals("OrderAmount")) {
                            DecimalFormat df = new DecimalFormat("0.00");
                            value = df.format(itemdata.getDouble(t.mFiledName, 0.00));
                        } else {
                            value = itemdata.getString(t.mFiledName);
                        }
                        if (t.dataType.equals(TableCloumArts.GRIDE_DATA_TYPE_RECORDID)) {
                            recordid = value;
                        }
                        TableItem mTableItem = new TableItem(t.mFiledName,value, false, t.mWidth, (int) mScreenDefine.density);
                        if (data.tableLeft.size() == base+k) {
                            if(t.isGrideVisiable)
                            data.tableLeft.add(mTableItem);
                        } else {
                            data.tableGrid.add(mTableItem);
                        }
                    }
                    if (recordid.length() == 0) {
                        recordid = String.valueOf(k);
                    }
                    data.dataKeys.add(recordid);
                    data.tableContent.put(recordid, itemdata.toString());
                }
                if(ja2.length() == 0)
                {
                    data.isall = true;
                }
            }
            else
            {
                data.isall = true;
            }
        } catch (JSONException e) {
            String a = e.getMessage();
        }
    }

    private static void makeChart(XpxJSONObject seriesdata, ChartData data) {

        try {
            if (data.dataType.equals(Data.FUN_DATA_TYPE_CHART_PIE) || data.dataType.equals(Data.FUN_DATA_TYPE_CHART_FUNNEL)) {
                XpxJSONArray ja2 = seriesdata.getJSONArray("Data");
                ChartDataItem chartDataItem = data.mDataForm.get(data.dataName);
                if (chartDataItem == null) {
                    chartDataItem = new ChartDataItem();
                    data.mDataForm.put(data.dataName, chartDataItem);
                }

                for (int j = 0; j < ja2.length(); j++) {
                    XpxJSONObject jo = ja2.getJSONObject(j);
                    data.xLable.add(jo.getString("Caption"));
                    chartDataItem.mData.put(jo.getString("Caption"),jo.getDouble("Value", 0));
                    if (data.unit.length() == 0) {
                        data.unit = jo.getString("Unit");
                    }
                }
            } else if (data.dataType.equals(Data.FUN_DATA_TYPE_CHART_COLUMN) || data.dataType.equals(Data.FUN_DATA_TYPE_CHART_LINE) || data.dataType.equals(Data.FUN_DATA_TYPE_CHART_BAR)) {

                XpxJSONArray tmpJA = seriesdata.getJSONArray("Data");
                for (int j = 0; j < tmpJA.length(); j++) {
                    XpxJSONObject tmpJO_1 = tmpJA.getJSONObject(j);
                    String serias = tmpJO_1.getString("YValue");
                    if (serias.length() == 0) {
                        serias = data.dataName;
                    }
                    ChartDataItem chartDataItem = data.mDataForm.get(serias);
                    if (chartDataItem == null) {
                        chartDataItem = new ChartDataItem();
                        data.mDataForm.put(serias, chartDataItem);
                    }
                    data.xLable.add(tmpJO_1.getString("Caption"));
                    chartDataItem.mData.put(tmpJO_1.getString("Caption"),tmpJO_1.getDouble("Value", 0));
                    if (data.unit.length() == 0) {
                        data.unit = tmpJO_1.getString("Unit");
                    }
                }
            } else if (data.dataType.equals(Data.FUN_DATA_TYPE_CHART_COLUMNS)) {

                XpxJSONArray tmpJA = seriesdata.getJSONArray("Data");
                for (int j = 0; j < tmpJA.length(); j++) {

                    if (tmpJA.isNull(j))
                        continue;

                    XpxJSONObject tmpJO_1 = tmpJA.getJSONObject(j);
                    String series = tmpJO_1.getString("Caption");
                    //data.xLable.add(series);
                    XpxJSONArray tmpJOa_1 = tmpJO_1.getJSONArray("Data");
                    for (int k = 0; k < tmpJOa_1.length(); k++) {
                        XpxJSONObject tmpJO_2 = (XpxJSONObject) tmpJOa_1.getJSONObject(k);
                        String caption = tmpJO_2.getString("Caption");
                        if(!data.xhash.containsKey(caption))
                        {
                            data.xhash.put(caption,caption);
                            data.xLable.add(caption);
                        }
                        ChartDataItem chartDataItem = data.mDataForm.get(series);
                        if (chartDataItem == null) {
                            chartDataItem = new ChartDataItem();
                            data.mDataForm.put(series, chartDataItem);
                        }
                        if (data.unit.length() == 0) {
                            data.unit = tmpJO_2.getString("Unit");
                        }
                        chartDataItem.mData.put(caption,tmpJO_2.getDouble("Value", 0));
                    }

                }
            }

        } catch (Exception e) {
        }


    }

    private static void makeLable(XpxJSONObject seriesdata, LableData data) {

        try {
            XpxJSONArray ja = seriesdata.getJSONArray("Data");
            for (int i = 0; i < ja.length(); i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                String content = jo.getString("Content");
                String name = jo.getString("Name");
                data.lableDataItems.add(new LableDataItem(name,
                        content));
            }

        } catch (JSONException e) {
        }
    }

    public static void praseSubDate(NetObject net, TableDetial mTableDetial, Function mFunction) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            XpxJSONObject jo = new XpxJSONObject(json);
            if (jo.has("data")) {
                XpxJSONArray data = jo.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    XpxJSONObject jsonObject = data.getJSONObject(i);
                    if (jsonObject.has("RecordID")) {
                        if (jsonObject.getString("RecordID").equals(mFunction.mRecordId)) {
                            mTableDetial.recordData = jsonObject.toString();
                        }
                    }
                }
            }
            XpxJSONArray ja = jo.getJSONArray("fields");
            for (int i = 0; i < ja.length(); ++i) {
                XpxJSONObject jo3 = (XpxJSONObject) ja.getJSONObject(i);
                String caption = jo3.getString("caption");
                String fieldName = jo3.getString("fieldname");
                String type = "dtstring";
                if (jo3.has("is_image")) {
                    if (jo3.getBoolean("is_image", false) == true)
                        type = "dtimage";
                }
                boolean visiable = true;
                TableCloumArts mTableCloumArts = new TableCloumArts();
                mTableCloumArts.mFiledName = fieldName;
                mTableCloumArts.mCaption = caption;
                mTableCloumArts.dataType = type;
                mTableCloumArts.isVisiable = true;
                mTableDetial.tableCloums.add(mTableCloumArts);
            }
            if (jo.has("attachment")) {
                XpxJSONArray ja1 = jo.getJSONArray("attachment");
                if (ja1.length() > 0) {
                    mTableDetial.attachmentData = ja1.toString();
                }
            }
            XpxJSONArray ja3 = jo.getJSONArray("groups");
            for (int i = 0; i < ja3.length(); ++i) {
                mTableDetial.mHeads.add(ja3.getString(i));
            }
            if(mFunction.isWorkFlowDetial)
            {
                mFunction.actionjson = json;
                parseWarnDetailInfo(mTableDetial,json);
            }

//            XpxJSONObject task = jo.getJSONObject("task");
//            mTableDetial.backEnabled = jo.getBoolean("BackEnabled",false);
//            mTableDetial.transmitEnabled = jo.getBoolean("TransmitEnabled",false);
//            mTableDetial.approvalEnabled = jo.getBoolean("ApprovalEnabled",false);
            mTableDetial.tempData = mTableDetial.recordData;
        } catch (JSONException e) {

        }
    }

    public static void parseWarnDetailInfo(TableDetial mTableDetial,String jString) {

        try {
            JSONObject jo = new JSONObject(jString);
            //JSONObject jo = jsonObject.getJSONObject("task");

            mTableDetial.backEnabled = jo.getBoolean(WebMessageActivity.BACK_ENABLED);
            mTableDetial.transmitEnabled = jo.getBoolean(WebMessageActivity.TRANSMIT_ENABLED);
            mTableDetial.approvalEnabled = jo.getBoolean(WebMessageActivity.APPROVAL_ENABLED);

        } catch (JSONException e) {
            return;
        }
        // detailList.setAdapter(adapter);
    }

    public static void addFujianData(String json, ArrayList<Attachment> attachments, Function function) {
        JSONArray data = null;
        attachments.clear();
        try {
            data = new JSONArray(json);
            for (int i = 0; i < data.length(); i++) {
                JSONObject maildata = data.getJSONObject(i);
                Attachment mFuJianItem = new Attachment();
                mFuJianItem.mDete = maildata.getString("Date");
                mFuJianItem.mName = maildata.getString("Name");
                mFuJianItem.mRecordid = maildata.getString("SerialID");
                mFuJianItem.mSize = maildata.getLong("Size");

                mFuJianItem.mPath =     Bus.callData(FunctionUtils.getInstance().context,"filetools/getfilePath",function.mRecordId)+maildata.getString("Name");
                mFuJianItem.mUrl = measureUrl(maildata.getString("SerialID"));
                attachments.add(mFuJianItem);
            }

        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static String measureUrl(String guid) {

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("token", NetUtils.getInstance().token));
        nvps.add(new BasicNameValuePair("serial_id", String.valueOf(guid)));
        String urlString = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,FunAsks.GRID_ATTACHMENT_GET_PATH+ URLEncodedUtils.format(nvps, HTTP.UTF_8));
        return urlString;
    }

    public static void parseGropData(String json, FunData mFunData,ScreenDefine mScreenDefine,String head) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            if (ja.length() == 0) {
                return;
            }
            mFunData.funDatas.clear();
            GrideData grideData = new GrideData();
            mFunData.funDatas.put(head,grideData);
            mFunData.mKeys.add(head);
            makeGrid2( jsonObject, grideData,mScreenDefine);

        } catch (JSONException e) {
            return;
        }
    }

    public static void makeGrid2(XpxJSONObject seriesdata, GrideData data,ScreenDefine mScreenDefine) {

        try {
            XpxJSONArray ja = seriesdata.getJSONArray("fields");
            XpxJSONArray ja2 = null;
            if (seriesdata.has("data"))
                ja2 = seriesdata.getJSONArray("data");
            if (data.tableCloums.size() == 0) {
                data.head = ja.toString();
                for (int i = 0; i < ja.length(); i++) {
                    XpxJSONObject column = ja.getJSONObject(i);
                    TableCloumArts mTableCloumArts = new TableCloumArts();
                    mTableCloumArts.mCaption = column.getString("caption");
                    mTableCloumArts.mFiledName = column.getString("fieldname");
                    String type = "dtstring";
                    if(column.has("is_image"))
                    {
                        type = "dtimage";
                    }
                    mTableCloumArts.dataType = type;
                    mTableCloumArts.mWidth = column.getInt("Width", 80);
                    mTableCloumArts.mFieldType = column.getString("FieldType");
                    mTableCloumArts.dataType = mTableCloumArts.mFieldType;
                    mTableCloumArts.isReadOnly = column.getBoolean("ReadOnly", false);
                    if(column.has("Visible"))
                    mTableCloumArts.isVisiable = column.getBoolean("Visible", true);
                    if(column.has("Visble"))
                    mTableCloumArts.isVisiable = column.getBoolean("Visble", true);
                    mTableCloumArts.isGrideVisiable = mTableCloumArts.isVisiable;
                    mTableCloumArts.isFill = column.getBoolean("IsFill", false);
                    mTableCloumArts.mDefault = column.getString("Default");
                    mTableCloumArts.mAttributes = column.getString("Attributes");
                    if (column.has("Values")) {
                        XpxJSONArray mval = column.getJSONArray("Values");
                        String v = "";
                        for (int j = 0; j < mval.length(); j++) {
                            if (j != mval.length() - 1) {
                                v += mval.getString(j) + ",";
                            } else {
                                v += mval.getString(j);
                            }

                        }
                        mTableCloumArts.mValues = v;
                    }
                    mTableCloumArts.isLinkage = column.getBoolean("Linkage", false);
                    mTableCloumArts.mLinkageFields = column.getString("LinkageFields");
                    if (mTableCloumArts.mFiledName.toLowerCase().equals("recordid")) {
                        mTableCloumArts.dataType = TableCloumArts.GRIDE_DATA_TYPE_RECORDID;
                        mTableCloumArts.isVisiable = false;
                        mTableCloumArts.isGrideVisiable = false;
                    }
                    if ((mTableCloumArts.mFieldType.toLowerCase().equals("dtimage") || mTableCloumArts.mFieldType.toLowerCase().equals("cimage"))) {
                        mTableCloumArts.dataType = TableCloumArts.GRIDE_DATA_TYPE_IMAGE;
                        mTableCloumArts.isGrideVisiable = false;
                    }
                    TableItem mTableItem = new TableItem(mTableCloumArts.mFiledName,mTableCloumArts.mCaption, true, mTableCloumArts.mWidth, (int) mScreenDefine.density);
                    if (data.tabkeBase == null) {
                        if(mTableCloumArts.isGrideVisiable)
                        data.tabkeBase = mTableItem;
                    } else {
                        data.tableHead.add(mTableItem);
                    }
                    data.tableCloums.add(mTableCloumArts);
                    data.tableCloumArtsHashMap.put(mTableCloumArts.mFiledName,mTableCloumArts);
                }

            }

            initLink(data.tableCloums);
            if (ja2 != null) {
                for (int k = 0; k < ja2.length(); k++) {
                    XpxJSONObject itemdata = ja2.getJSONObject(k);
                    String recordid = "";
                    for (int n = 0; n < data.tableCloums.size(); n++) {
                        TableCloumArts t = data.tableCloums.get(n);
                        String value = null;
                        if (t.mFiledName.equals("OrderAmount")) {
                            DecimalFormat df = new DecimalFormat("0.00");
                            value = df.format(itemdata.getDouble(t.mFiledName, 0.00));
                        } else {
                            value = itemdata.getString(t.mFiledName);
                        }
                        if (t.dataType.equals(TableCloumArts.GRIDE_DATA_TYPE_RECORDID)) {
                            recordid = value;
                        }
                        TableItem mTableItem = new TableItem(t.mFiledName,value, false, t.mWidth, (int) mScreenDefine.density);
                        if (data.tableLeft.size() == k) {
                            if(t.isGrideVisiable)
                                data.tableLeft.add(mTableItem);
                        } else {
                            data.tableGrid.add(mTableItem);
                        }


                    }
                    if (recordid.length() == 0) {
                        recordid = String.valueOf(k);
                    }
                    data.dataKeys.add(recordid);
                    data.tableContent.put(recordid, itemdata.toString());
                }
            }
        } catch (JSONException e) {
        }
    }
}
