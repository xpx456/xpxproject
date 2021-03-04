package com.dk.dktest.presenter;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.dk.dktest.EchartOptionUtil;
import com.dk.dktest.R;
import com.dk.dktest.database.DBHelper;
import com.dk.dktest.entity.TestRecord;
import com.dk.dktest.handler.SportDetialHandler;
import com.dk.dktest.view.DkTestApplication;
import com.dk.dktest.view.activity.SportDetialActivity;

import java.util.ArrayList;
import java.util.Random;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.function.asks.FunAsks;
import intersky.mywidget.conturypick.DbHelper;
import intersky.mywidget.conturypick.PinyinUtil;
import xpx.com.toolbar.utils.ToolBarHelper;

public class SportDetialPresenter implements Presenter {

    public SportDetialActivity mSportDetialActivity;
    public SportDetialHandler mSportDetialHandler;

    public SportDetialPresenter(SportDetialActivity SportDetialActivity) {
        mSportDetialActivity = SportDetialActivity;
        mSportDetialHandler = new SportDetialHandler(SportDetialActivity);
    }

    @Override
    public void initView() {
        mSportDetialActivity.flagFillBack = false;
        ToolBarHelper.setSutColor(mSportDetialActivity, Color.argb(0, 255, 255, 255));
        if(mSportDetialActivity.getIntent().getIntExtra("type", 1) == 1)
        {
            mSportDetialActivity.setContentView(R.layout.activity_sport_detial);
            mSportDetialActivity.cyclevalue = mSportDetialActivity.findViewById(R.id.cyclevalue);
            mSportDetialActivity.stop = mSportDetialActivity.findViewById(R.id.stop);
            mSportDetialActivity.stop.setOnClickListener(stopListener);
        }
        else
        {
            mSportDetialActivity.setContentView(R.layout.activity_sport_detial2);
            mSportDetialActivity.add = mSportDetialActivity.findViewById(R.id.add);
            mSportDetialActivity.del = mSportDetialActivity.findViewById(R.id.del);
            mSportDetialActivity.add.setOnClickListener(addListener);
            mSportDetialActivity.del.setOnClickListener(delListener);
        }
        mSportDetialActivity.save = mSportDetialActivity.findViewById(R.id.save);
        mSportDetialActivity.save.setOnClickListener(saveListener);
        mSportDetialActivity.chartShow = mSportDetialActivity.findViewById(R.id.chart);
        mSportDetialActivity.chartShow.getSettings().setAllowFileAccess(true);
        mSportDetialActivity.chartShow.getSettings().setJavaScriptEnabled(true);
        mSportDetialActivity.chartShow.setWebViewClient(mWebViewClient);
        iniop();
        mSportDetialActivity.btnBack = mSportDetialActivity.findViewById(R.id.back);
        mSportDetialActivity.start = mSportDetialActivity.findViewById(R.id.start);

        mSportDetialActivity.electvalue = mSportDetialActivity.findViewById(R.id.electvalue);

        mSportDetialActivity.leavelvalue = mSportDetialActivity.findViewById(R.id.leavelvalue);
        mSportDetialActivity.wran = mSportDetialActivity.findViewById(R.id.warn);
        mSportDetialActivity.wran.setOnClickListener(btn4Listener);
        mSportDetialActivity.start.setOnClickListener(startListener);

        mSportDetialActivity.btnBack.setOnClickListener(backListener);
        dostop();
        mSportDetialActivity.chartShow.setVisibility(View.VISIBLE);
        mSportDetialActivity.chartShow.loadUrl("file:///android_asset/echart/myechart.html");
        DkTestApplication.mApp.getDatas.add(getData);
        mSportDetialActivity.mToolBarHelper.hidToolbar(mSportDetialActivity, (RelativeLayout) mSportDetialActivity.findViewById(R.id.buttomaciton));
        mSportDetialActivity.measureStatubar(mSportDetialActivity, (RelativeLayout) mSportDetialActivity.findViewById(R.id.stutebar));


    }

    @Override
    public void Create() {
        initView();
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
        mSportDetialActivity.chartShow.destroy();
        DkTestApplication.mApp.getDatas.remove(getData);
    }

    public void iniop(){
        mSportDetialActivity.option = new ObjectData();
        ObjectData grid = new ObjectData();
        mSportDetialActivity.option.put("grid",grid);
        grid.put("left","1%");
        grid.put("right","1%");
        grid.put("containLabel",true);
        ArrayData datazoom = new ArrayData();
        mSportDetialActivity.option.put("dataZoom",datazoom);
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

        ObjectData title = new ObjectData();
        mSportDetialActivity.option.put("title",title);
        title.put("text","电流变化图");



        ObjectData xAxis = new ObjectData();
        mSportDetialActivity.option.put("xAxis",xAxis);
        xAxis.put("type","category");
        mSportDetialActivity.x = new ArrayData();
        xAxis.put("data",mSportDetialActivity.x);
        ObjectData yAxis = new ObjectData();
        mSportDetialActivity.option.put("yAxis",yAxis);
        yAxis.put("type","value");
        ObjectData al = new ObjectData();
        al.put("formatter","{value} mA");
        yAxis.put("axisLabel",al);
        ArrayData series = new ArrayData();
        mSportDetialActivity.option.put("series",series);
        ObjectData objectData1 = new ObjectData();
        series.add(objectData1);
        mSportDetialActivity.y = new ArrayData();
        objectData1.put("data",mSportDetialActivity.y);
        objectData1.put("type","line");
        objectData1.put("smooth",true);


    }

    public void updata(ArrayList<String[]> items) {
        for(int i = 0 ; i < items.size() ; i++)
        {
            String[] item = items.get(i);
            mSportDetialActivity.time += 0.2;
            int a = (int)mSportDetialActivity.time;
            int b = (int)(mSportDetialActivity.time*1000)%1000;
            int second = a%60;
            int m = a/60;
            int h = m/60;
            String v = String.format("%d:%02d:%02d.%03d",h,m,second,b);
            mSportDetialActivity.x.add(v);
            if(item.length >= 3)
            mSportDetialActivity.y.add(Double.valueOf(item[2]));
            if(items.size()-1 == i)
            {
                if(item.length >= 1)
                {
                    if(item[0].length() >= 4)
                    mSportDetialActivity.leavelvalue.setText(item[0].substring(2,4));
                }
                if(item.length >= 5)
                {
                    if(mSportDetialActivity.getIntent().getIntExtra("type",1)==1)
                    mSportDetialActivity.cyclevalue.setText(item[4]);
                }

                if(item.length >= 3)
                mSportDetialActivity.electvalue.setText(item[2]);
                if(item.length >= 5)
                {
                    if(item[4].equals("1"))
                    {
                        mSportDetialActivity.wran.setBackgroundResource(R.drawable.shape_bg_round_red);
                    }
                    else{
                        mSportDetialActivity.wran.setBackgroundResource(R.drawable.shape_bg_round_gray);
                    }
                }
            }
        }
        if(mSportDetialActivity.isstart)
        {
            String url = "javascript:createChart('orther'," + mSportDetialActivity.option.toString()+");";
            mSportDetialActivity.chartShow.loadUrl(url);
        }
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
            mSportDetialActivity.waitDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            mSportDetialActivity.waitDialog.hide();
            if (mSportDetialActivity.isFirst == false) {
                mSportDetialActivity.isFirst = true;
                //mChartActivity.waitDialog.show();
            }
            //ChartUtils.loadBarChart(mChartActivity.chartData,mChartActivity.chartShow);
            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mSportDetialActivity.waitDialog.hide();
        }
    };

    public View.OnClickListener btn4Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DkTestApplication.mApp.wridtCmd("BT4\r\n");
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mSportDetialActivity.finish();
        }
    };

    public View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mSportDetialActivity.getIntent().getIntExtra("type",1) == 1)
            dostart();
            else
            {
                if(mSportDetialActivity.isstart == true)
                dostop();
                else
                    dostart();
            }

        }
    };

    public View.OnClickListener stopListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dostop();
        }
    };

    public View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            doAdd();
        }
    };

    public View.OnClickListener delListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            doDel();
        }
    };

    public View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            save();
        }
    };

    public AppUtils.GetEditText startSaveListener = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            if(s.length() > 0)
            {
                TestRecord testRecord = new TestRecord();
                testRecord.time = TimeUtils.getDateAndTime2();
                testRecord.day = TimeUtils.getDate();
                testRecord.name = s;
                testRecord.rid = AppUtils.getguid();
                testRecord.during = (int)mSportDetialActivity.time;
                testRecord.data = mSportDetialActivity.option.toString();
                DBHelper.getInstance(mSportDetialActivity).addRecord(testRecord);
                AppUtils.showMessage(mSportDetialActivity,"保存成功");
            }
            else{
                AppUtils.showMessage(mSportDetialActivity,"名称不能为空");
            }
        }
    };

    private void dostart()
    {
        mSportDetialActivity.isstart = true;
        if(mSportDetialActivity.getIntent().getIntExtra("type",1) == 1)
        {
            mSportDetialActivity.stop.setBackgroundResource(R.drawable.btn_shape_gray);
            mSportDetialActivity.start.setBackgroundResource(R.drawable.btn_shape_yellow);
            DkTestApplication.mApp.wridtCmd("BT0\r\n");
        }
        else
        {
            mSportDetialActivity.start.setText(mSportDetialActivity.getString(R.string.button_word_stop));
            DkTestApplication.mApp.wridtCmd("BT0\r\n");
        }
    }

    private void dostop()
    {
        mSportDetialActivity.isstart = false;
        if(mSportDetialActivity.getIntent().getIntExtra("type",1) == 1)
        {
            mSportDetialActivity.stop.setBackgroundResource(R.drawable.btn_shape_yellow);
            mSportDetialActivity.start.setBackgroundResource(R.drawable.btn_shape_gray);
            DkTestApplication.mApp.wridtCmd("BT1\r\n");
        }
        else
        {
            mSportDetialActivity.start.setText(mSportDetialActivity.getString(R.string.button_word_start));
            DkTestApplication.mApp.wridtCmd("BT1\r\n");
        }
    }

    public void save()
    {
        if(mSportDetialActivity.isstart == false)
        {
            AppUtils.creatXpxDialogEdit(mSportDetialActivity,null
                    ,mSportDetialActivity.getString(R.string.save_name)
                    ,mSportDetialActivity.getString(R.string.save_unknow_name)
                    ,startSaveListener,mSportDetialActivity.findViewById(R.id.activity_splash), InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            AppUtils.showMessage(mSportDetialActivity,"请先停止设备");
        }
    }

    public void doAdd() {
        DkTestApplication.mApp.wridtCmd("BT2\r\n");
    }

    public void doDel() {
        DkTestApplication.mApp.wridtCmd("BT3\r\n");
    }


    public DkTestApplication.GetData getData = new DkTestApplication.GetData() {
        @Override
        public void getData(ArrayList<String[]> data) {
            //if(mSportDetialActivity.isstart)
            {
                Message message = new Message();
                message.what = SportDetialHandler.UPDATA_DATA;
                message.obj = data;
                if(mSportDetialHandler != null)
                mSportDetialHandler.sendMessage(message);
            }

        }

    };
}
