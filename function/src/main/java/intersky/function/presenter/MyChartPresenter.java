package intersky.function.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Random;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.function.ChartUtils;
import intersky.function.R;
import intersky.function.asks.FunAsks;
import intersky.function.handler.ChartHandler;
import intersky.function.handler.MyChartHandler;
import intersky.function.receiver.entity.ChartData;
import intersky.function.receiver.entity.ChartDataItem;
import intersky.function.receiver.entity.Function;
import intersky.function.view.activity.ChartActivity;
import intersky.function.view.activity.MyChartActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class MyChartPresenter implements Presenter {

    public MyChartHandler mChartHandler;
    public MyChartActivity mChartActivity;
    public String url = "javascript:createChart('colums',[{'name':'2019','type':'bar','data':[65000.0,36300.0,90600.0,107366.0,5200.0,0.0,0.0,30000.0,10500.0]},{'name':'2018','type':'bar','data':[32590.0,6300.0,17000.0,22100.0,5200.0,8000.0,90080.0,0.0,0.0]},{'name':'2020','type':'bar','data':[16900.0,8700.0,62800.0,211764.0,0.0,0.0,0.0,0.0,81000.0]}],['上海','义乌','南京','宁波','广州','深圳','苏州','北京','杭州'],['2019','2018','2020'],{formatter:'{value}元'},['2019','2018','2020']);";
    public MyChartPresenter(MyChartActivity mChartActivity) {
        this.mChartActivity = mChartActivity;
        this.mChartHandler = new MyChartHandler(mChartActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mChartActivity.flagFillBack = false;
        mChartActivity.setContentView(R.layout.activity_mychart);
        mChartActivity.chartShow = mChartActivity.findViewById(R.id.funnelChart);
        mChartActivity.chartShow.getSettings().setAllowFileAccess(true);
        mChartActivity.chartShow.getSettings().setJavaScriptEnabled(true);
        mChartActivity.chartShow.setWebViewClient(mWebViewClient);
        iniop();

        //mFunctionModuleActivity.mChart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mChartActivity.chartShow.setVisibility(View.VISIBLE);
        mChartActivity.chartShow.loadUrl("file:///android_asset/echart/myechart.html");
        mChartHandler.sendEmptyMessageDelayed(FunAsks.BOARD_CHART_DATA_SUCCESS,1000);
        praseFunction();
    }



    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            // 当有新连接时使用当前的webview进行显示
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        // 开始加载网页时要做的工作
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setVisibility(View.INVISIBLE);
            mChartActivity.waitDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            mChartActivity.waitDialog.hide();
            if (mChartActivity.isFirst == false) {
                mChartActivity.isFirst = true;
                //mChartActivity.waitDialog.show();
            }
            //ChartUtils.loadBarChart(mChartActivity.chartData,mChartActivity.chartShow);
            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mChartActivity.waitDialog.hide();
        }
    };

    public void praseFunction() {
        mChartActivity.chartData = new ChartData(Function.BAR);
        mChartActivity.chartData.dataName = "xpx";
        mChartActivity.chartData.unit = "m";
        ChartDataItem chartDataItem = new ChartDataItem();
        mChartActivity.chartData.mDataForm.put("force", chartDataItem);
        for(int i = 0 ; i < 12 ; i++)
        {
            mChartActivity.chartData.xLable.add(String.valueOf(i));
        }
        chartDataItem.mData.put("0",0.00);
        chartDataItem.mData.put("1",1.00);
        chartDataItem.mData.put("2",3.00);
        chartDataItem.mData.put("3",7.00);
        chartDataItem.mData.put("4",5.00);
        chartDataItem.mData.put("5",4.00);
        chartDataItem.mData.put("6",2.00);
        chartDataItem.mData.put("7",0.00);
        chartDataItem.mData.put("8",1.00);
        chartDataItem.mData.put("9",2.00);
        chartDataItem.mData.put("10",4.00);
        chartDataItem.mData.put("11",8.00);
        chartDataItem.mData.put("12",6.00);

    }

    public void initData(ChartData mChartData) {
//        if (mChartActivity.mFunData.type.equals(Function.COLUMNS)) {
//            ChartUtils.loadColumnChart(mChartData,mChartActivity.chartShow);
//        } else if (mChartActivity.mFunData.type.equals(Function.BAR)) {
//            ChartUtils.loadBarChart(mChartData,mChartActivity.chartShow);
//        } else if (mChartActivity.mFunData.type.equals(Function.FUNNEL)) {
//            ChartUtils.loadFunnelChart(mChartData,mChartActivity.chartShow);
//        } else if (mChartActivity.mFunData.type.equals(Function.PIE)) {
//            ChartUtils.loadPieChart(mChartData,mChartActivity.chartShow);
//        } else if (mChartActivity.mFunData.type.equals(Function.LINE)) {
//            ChartUtils.loadLineChart(mChartData,mChartActivity.chartShow);
//        }
        ChartUtils.loadLineChart(mChartData,mChartActivity.chartShow);
    }

    //        String url = "javascript:createChart('orther'," + dataArrray.toString());";
    public void iniop(){
        mChartActivity.option = new ObjectData();
        ArrayData datazoom = new ArrayData();
        mChartActivity.option.put("dataZoom",datazoom);
        ObjectData zoom1 = new ObjectData();
        datazoom.add(zoom1);
        zoom1.put("type","slider");
        zoom1.put("start",10);
        zoom1.put("end",60);
        ObjectData zoom2 = new ObjectData();
        datazoom.add(zoom2);
        zoom2.put("type","inside");
        zoom2.put("start",10);
        zoom2.put("end",60);

        ObjectData xAxis = new ObjectData();
        mChartActivity.option.put("xAxis",xAxis);
        xAxis.put("type","category");
        mChartActivity.x = new ArrayData();
        xAxis.put("data",mChartActivity.x);
        ObjectData yAxis = new ObjectData();
        mChartActivity.option.put("yAxis",yAxis);
        yAxis.put("type","value");
        ArrayData series = new ArrayData();
        mChartActivity.option.put("series",series);
        ObjectData objectData1 = new ObjectData();
        series.add(objectData1);
        mChartActivity.y = new ArrayData();
        objectData1.put("data",mChartActivity.y);
        objectData1.put("type","line");
        objectData1.put("smooth",true);


    }

    public void updata()
    {
        mChartActivity.x.add(TimeUtils.getTimeMinuteSecond());
        Random ra = new Random();
        double data = ra.nextInt(10);
        mChartActivity.y.add(data);
        String url = "javascript:createChart('orther'," + mChartActivity.option.toString()+");";
        mChartActivity.chartShow.loadUrl(url);
        if(mChartActivity.now < mChartActivity.max)
        {
            mChartActivity.now += 33;
            mChartHandler.sendEmptyMessageDelayed(FunAsks.BOARD_CHART_DATA_SUCCESS,33);
        }

    }

}
