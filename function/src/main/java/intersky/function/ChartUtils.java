package intersky.function;

import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import intersky.function.receiver.entity.ChartData;
import intersky.function.receiver.entity.ChartDataItem;
import intersky.json.EchartArray;
import intersky.json.EchartObject;

public class ChartUtils {

    public static void loadColumnChart(ChartData mChartData, WebView chart) {
        EchartArray dataArrray = new EchartArray();
        EchartArray dataArrray1 = new EchartArray();
        EchartArray dataArrray2 = new EchartArray();
        for (int i = 0; i < mChartData.xLable.size(); i++) {
            dataArrray1.put(mChartData.xLable.get(i));
        }
        for (Map.Entry<String, ChartDataItem> entry : mChartData.mDataForm.entrySet())
        {
            EchartObject jo = new EchartObject();
            dataArrray2.put(entry.getKey());
            jo.put("name", entry.getKey());
            jo.put("type", "bar");
            EchartArray data = new EchartArray();
            ChartDataItem chartDataItem = entry.getValue();
            for (int j = 0; j < mChartData.xLable.size(); j++)
            {
                if(chartDataItem.mData.containsKey(mChartData.xLable.get(j)))
                {
                    data.put(chartDataItem.mData.get(mChartData.xLable.get(j)));
                }
                else
                {
                    data.put(0.00);
                }
            }
            jo.put("data",data);
            dataArrray.put(jo);
        }

        String unit = "{formatter:'{value}" + mChartData.unit + "'}";
        String url = "javascript:createChart('colums'," + dataArrray.toString() + "," + dataArrray1.toString() + "," + dataArrray2.toString() + "," + unit + "," + dataArrray2.toString() + ");";
        chart.loadUrl(url);
    }

    public static void loadBarChart(ChartData mChartData, WebView chart) {

        EchartArray dataArrray = new EchartArray();
        EchartArray dataArrray1 = new EchartArray();
        EchartArray dataArrray2 = new EchartArray();
        for (int i = 0; i < mChartData.xLable.size(); i++) {
            dataArrray1.put(mChartData.xLable.get(i));
        }
        for (Map.Entry<String, ChartDataItem> entry : mChartData.mDataForm.entrySet()) {
            ChartDataItem chartDataItem = entry.getValue();
            EchartObject jo = new EchartObject();
            dataArrray2.put(entry.getKey());
            jo.put("name", entry.getKey());
            jo.put("type","bar");
            jo.put("stack","");
            EchartArray ja = new EchartArray();
            jo.put("data",ja);
            for (int j = 0; j < mChartData.xLable.size(); j++) {
                if(chartDataItem.mData.containsKey(mChartData.xLable.get(j)))
                ja.put(chartDataItem.mData.get(mChartData.xLable.get(j)));
                else
                {
                    ja.put(0.00);
                }
            }
            dataArrray.put(jo);
        }
        String unit = "{formatter:'{value}" + mChartData.unit + "'}";
        String url = "javascript:createChart('bar'," + dataArrray.toString() + "," + dataArrray1.toString() + "," + "" + "," + unit + "," + "" + ");";
        chart.loadUrl(url);
    }

    public static void loadLineChart(ChartData mChartData, WebView chart) {
        EchartArray dataArrray = new EchartArray();
        EchartArray dataArrray1 = new EchartArray();
        EchartArray dataArrray2 = new EchartArray();
        for (int i = 0; i < mChartData.xLable.size(); i++) {
            dataArrray1.put(mChartData.xLable.get(i));
        }
        for (Map.Entry<String, ChartDataItem> entry : mChartData.mDataForm.entrySet()) {
            ChartDataItem chartDataItem = entry.getValue();
            EchartObject jo = new EchartObject();
            dataArrray2.put(entry.getKey());
            jo.put("name", entry.getKey());
            jo.put("type","line");
            jo.put("stack","");
            EchartArray ja = new EchartArray();
            jo.put("data",ja);
            for (int j = 0; j < mChartData.xLable.size(); j++) {
                if(chartDataItem.mData.containsKey(mChartData.xLable.get(j)))
                    ja.put(chartDataItem.mData.get(mChartData.xLable.get(j)));
                else
                {
                    ja.put(0.00);
                }
            }
            dataArrray.put(jo);
        }
        String unit = "{formatter:'{value}" + mChartData.unit + "'}";
        String url = "";
        if(dataArrray2.items.size() > 1)
        {
            url = "javascript:createChart('serialine'," + dataArrray.toString() + "," + dataArrray1.toString() + "," + dataArrray2.toString() + "," + unit + "," + dataArrray2.toString() + ");";
        }
        else
        {
            url = "javascript:createChart('line'," + dataArrray.toString() + "," + dataArrray1.toString() + "," + "" + "," + unit + "," + "" + ");";
        }

        chart.loadUrl(url);
    }

    public static void loadPieChart(ChartData mChartData, WebView chart) {
        EchartArray dataArrray = new EchartArray();
        EchartArray dataArrray1 = new EchartArray();
        for (Map.Entry<String, ChartDataItem> entry : mChartData.mDataForm.entrySet()) {
            ChartDataItem chartDataItem = entry.getValue();
            for (int j = 0; j < mChartData.xLable.size(); j++) {
                EchartObject jo = new EchartObject();
                dataArrray1.put(mChartData.xLable.get(j));
                jo.put("value", chartDataItem.mData.get(mChartData.xLable.get(j)));
                jo.put("name", mChartData.xLable.get(j));
                dataArrray.put(jo);
            }
        }
        String unit = "{formatter:'{value}" + mChartData.unit + "'}";
        String url = "javascript:createChart('pie'," + dataArrray.toString() + "," + dataArrray1.toString() + "," + dataArrray1.toString() + "," + unit + "," + dataArrray1.toString() + ");";
        chart.loadUrl(url);
    }

    public static void loadFunnelChart(ChartData mChartData, WebView chart) {
        EchartArray dataArrray = new EchartArray();
        EchartArray dataArrray1 = new EchartArray();
        for (Map.Entry<String, ChartDataItem> entry : mChartData.mDataForm.entrySet()) {
            ChartDataItem chartDataItem = entry.getValue();
            for (int j = 0; j < mChartData.xLable.size(); j++) {
                EchartObject jo = new EchartObject();
                dataArrray.put(mChartData.xLable.get(j));
                jo.put("value", chartDataItem.mData.get(mChartData.xLable.get(j)));
                jo.put("name", mChartData.xLable.get(j));
                dataArrray1.put(jo);
            }
        }
        String unit = "{formatter:'{value}" + mChartData.unit + "'}";
        String url = "javascript:createChart('funnel'," + dataArrray.toString() + "," + dataArrray1.toString() + "," + dataArrray.toString() + "," + unit + "," + mChartData.dataName + ");";
        chart.loadUrl(url);
    }

    public static void loadsLineChart(ChartData mChartData, WebView chart) {
        EchartArray dataArrray = new EchartArray();
        EchartArray dataArrray1 = new EchartArray();
        EchartArray dataArrray2 = new EchartArray();
        for (int i = 0; i < mChartData.xLable.size(); i++) {
            dataArrray1.put(mChartData.xLable.get(i));
        }
        for (Map.Entry<String, ChartDataItem> entry : mChartData.mDataForm.entrySet())
        {
            EchartObject jo = new EchartObject();
            dataArrray2.put(entry.getKey());
            jo.put("name", entry.getKey());
            jo.put("type", "line");
            EchartArray data = new EchartArray();
            ChartDataItem chartDataItem = entry.getValue();
            for (int j = 0; j < mChartData.xLable.size(); j++)
            {
                if(chartDataItem.mData.containsKey(mChartData.xLable.get(j)))
                {
                    data.put(chartDataItem.mData.get(mChartData.xLable.get(j)));
                }
                else
                {
                    data.put(0.00);
                }
            }
            jo.put("data",data);
            dataArrray.put(jo);
        }

        String unit = "{formatter:'{value}" + mChartData.unit + "'}";
        String url = "javascript:createChart('colums'," + dataArrray.toString() + "," + dataArrray1.toString() + "," + dataArrray2.toString() + "," + unit + "," + dataArrray2.toString() + ");";
        chart.loadUrl(url);
    }
}
