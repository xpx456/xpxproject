package com.dk.dkhome.view;

import android.content.Context;
import android.icu.text.CaseMap;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.dk.dkhome.R;
import com.dk.dkhome.presenter.SportDetialPresenter;
import com.dk.dkhome.utils.BigwinerScanPremissionResult;
import com.dk.dkhome.utils.TestManager;

import java.text.DecimalFormat;

import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.scan.ScanUtils;

public class SportView {

    public SportDetialPresenter mSportDetialPresenter;
    public View view;
    public TextView btnScan;
    public ScrollView operView;
    public WebView chartShow;
    public TextView sportTime;
    public TextView sportDis;
    public TextView sportCarl;
    public TextView sportHert;
    public AppCompatSeekBar drageProgress;
    public ObjectData homeoption;
    public ArrayData homeLegend;
    public ObjectData homespeed;
    public ObjectData homeRpm;
    public ObjectData homeselect;
    public TextView persent;
    public RelativeLayout btnadd1;
    public RelativeLayout btnadd5;
    public RelativeLayout btnadd10;
    public RelativeLayout btnadd20;
    public RelativeLayout btndes1;
    public RelativeLayout btndes5;
    public RelativeLayout btndes10;
    public RelativeLayout btndes20;
    public SportView(SportDetialPresenter mSportDetialPresenter) {
        this.mSportDetialPresenter = mSportDetialPresenter;
        init();
    }

    public void init() {
        LayoutInflater mInflater = (LayoutInflater) mSportDetialPresenter.mSportDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.fragment_home,null);
        operView = view.findViewById(R.id.operview);
        btnScan = view.findViewById(R.id.scan);
        chartShow = view.findViewById(R.id.chart);
        chartShow.getSettings().setAllowFileAccess(true);
        chartShow.getSettings().setJavaScriptEnabled(true);
        chartShow.setWebViewClient(mSportDetialPresenter.mWebViewClient);
        chartShow.setVisibility(View.VISIBLE);
        chartShow.setBackgroundColor(0); // 设置背景色
        chartShow.getBackground().setAlpha(0);
        chartShow.loadUrl("file:///android_asset/echart/myechart.html");
        sportTime = view.findViewById(R.id.sptorttimedragvalue);
        sportDis = view.findViewById(R.id.sptortdisvalue);
        sportCarl = view.findViewById(R.id.sptortcarlvalue);
        sportHert = view.findViewById(R.id.sptorthertvalue);
        persent = view.findViewById(R.id.dragvalue);
        btnadd1 = view.findViewById(R.id.btnadd1);
        btnadd5 = view.findViewById(R.id.btnadd5);
        btnadd10 = view.findViewById(R.id.btnadd10);
        btnadd20 = view.findViewById(R.id.btnadd20);
        btndes1 = view.findViewById(R.id.btndes1);
        btndes5 = view.findViewById(R.id.btndes5);
        btndes10 = view.findViewById(R.id.btndes10);
        btndes20 = view.findViewById(R.id.btndes20);
        btnadd1.setOnClickListener(add1Listner);
        btnadd5.setOnClickListener(add5Listner);
        btnadd10.setOnClickListener(add10Listner);
        btnadd20.setOnClickListener(add20Listner);
        btndes1.setOnClickListener(des1Listner);
        btndes5.setOnClickListener(des5Listner);
        btndes10.setOnClickListener(des10Listner);
        btndes20.setOnClickListener(des20Listner);
        drageProgress = view.findViewById(R.id.dragseekbar);
        drageProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);
        btnScan.setOnClickListener(doScanListner);
        initData();
        initOptionHome();
        updataView();
    }

    public void updataView() {
        if(TestManager.TEST_MODE)
        {
            operView.setVisibility(View.VISIBLE);
            btnScan.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(TestManager.testManager.state == false)
            {
                operView.setVisibility(View.INVISIBLE);
                btnScan.setVisibility(View.VISIBLE);
            }
            else
            {
                operView.setVisibility(View.VISIBLE);
                btnScan.setVisibility(View.INVISIBLE);
            }
        }


    }

    public void doUpdata() {
        int h = mSportDetialPresenter.course.current/60/60;
        int m = (mSportDetialPresenter.course.current/60)%60;
        int s = mSportDetialPresenter.course.current%60;
        sportTime.setText(String.format("%02d:%02d:%02d",h,m,s));
        sportDis.setText(String .format("%.2f",mSportDetialPresenter.course.dis)+"km");
        sportCarl.setText(String .format("%.2f",mSportDetialPresenter.course.totalCarl)+"kcal");
        sportHert.setText(String.valueOf(mSportDetialPresenter.course.nowhert)+"bpm");
        persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
        DecimalFormat df   =new   java.text.DecimalFormat("#.00");
        homespeed.put("value", df.format(mSportDetialPresenter.course.nowspeed));
        homeRpm.put("value",df.format(mSportDetialPresenter.deviceView.device.nowrpm));
        homeselect.put("value", mSportDetialPresenter.deviceView.device.nowleavel);
        drageProgress.setProgress(mSportDetialPresenter.deviceView.device.nowleavel);
        String url = "javascript:createChart('orther'," + homeoption.toString() + ");";
        chartShow.loadUrl(url);
    }


    public void onDestory()
    {
        chartShow.destroy();
    }

    private View.OnClickListener doScanListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSportDetialPresenter.mSportDetialActivity.permissionRepuest = ScanUtils.getInstance().
                    checkStartScan(mSportDetialPresenter.mSportDetialActivity,""
                    ,mSportDetialPresenter.mSportDetialHandler
                    , new BigwinerScanPremissionResult(mSportDetialPresenter.mSportDetialActivity,"",mSportDetialPresenter.mSportDetialActivity.getString(R.string.home_scan)));
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mSportDetialPresenter.deviceView.device.nowleavel = seekBar.getProgress();
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };


    private View.OnClickListener add1Listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSportDetialPresenter.deviceView.device.nowleavel < 100)
            {
                mSportDetialPresenter.deviceView.device.nowleavel++;
            }
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };

    private View.OnClickListener add5Listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSportDetialPresenter.deviceView.device.nowleavel+5 < 100)
            {
                mSportDetialPresenter.deviceView.device.nowleavel+=5;
            }
            else if(mSportDetialPresenter.deviceView.device.nowleavel < 100)
            {
                mSportDetialPresenter.deviceView.device.nowleavel = 100;
            }
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };

    private View.OnClickListener add10Listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSportDetialPresenter.deviceView.device.nowleavel+10 < 100)
            {
                mSportDetialPresenter.deviceView.device.nowleavel+=10;
            }
            else if(mSportDetialPresenter.deviceView.device.nowleavel < 100)
            {
                mSportDetialPresenter.deviceView.device.nowleavel = 100;
            }
            drageProgress.setProgress(mSportDetialPresenter.deviceView.device.nowleavel);
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };

    private View.OnClickListener add20Listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSportDetialPresenter.deviceView.device.nowleavel+20 < 100)
            {
                mSportDetialPresenter.deviceView.device.nowleavel+=20;
            }
            else if(mSportDetialPresenter.deviceView.device.nowleavel < 100)
            {
                mSportDetialPresenter.deviceView.device.nowleavel = 100;
            }
            drageProgress.setProgress(mSportDetialPresenter.deviceView.device.nowleavel);
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };


    private View.OnClickListener des1Listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSportDetialPresenter.deviceView.device.nowleavel > 0)
            {
                mSportDetialPresenter.deviceView.device.nowleavel--;
            }
            drageProgress.setProgress(mSportDetialPresenter.deviceView.device.nowleavel);
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };

    private View.OnClickListener des5Listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSportDetialPresenter.deviceView.device.nowleavel-5 > 0)
            {
                mSportDetialPresenter.deviceView.device.nowleavel-=5;
            }
            else if(mSportDetialPresenter.deviceView.device.nowleavel > 0)
            {
                mSportDetialPresenter.deviceView.device.nowleavel = 0;
            }
            drageProgress.setProgress(mSportDetialPresenter.deviceView.device.nowleavel);
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };

    private View.OnClickListener des10Listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSportDetialPresenter.deviceView.device.nowleavel-10 > 0)
            {
                mSportDetialPresenter.deviceView.device.nowleavel-=10;
            }
            else if(mSportDetialPresenter.deviceView.device.nowleavel > 0)
            {
                mSportDetialPresenter.deviceView.device.nowleavel = 0;
            }
            drageProgress.setProgress(mSportDetialPresenter.deviceView.device.nowleavel);
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };

    private View.OnClickListener des20Listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSportDetialPresenter.deviceView.device.nowleavel-20 > 0)
            {
                mSportDetialPresenter.deviceView.device.nowleavel-=20;
            }
            else if(mSportDetialPresenter.deviceView.device.nowleavel > 0)
            {
                mSportDetialPresenter.deviceView.device.nowleavel = 0;
            }
            drageProgress.setProgress(mSportDetialPresenter.deviceView.device.nowleavel);
            persent.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            mSportDetialPresenter.fselect.setText(String.valueOf(mSportDetialPresenter.deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(mSportDetialPresenter.deviceView.device.nowleavel,mSportDetialPresenter.deviceView.device);
        }
    };


    private void initData() {
        sportTime.setText("00:00:00");
        sportDis.setText("0 km");
        sportCarl.setText("0 C");
        sportHert.setText("0 bpm");
        drageProgress.setMax(100);
        drageProgress.setProgress(0);
    }

    private void initOptionHome() {
        homeoption = new ObjectData();

        ObjectData tooltip = new ObjectData();
        homeoption.put("tooltip", tooltip);
        tooltip.put("formatter", "{a} <br/>{c} {b}");
        homeoption.put("backgroundColor", "rgba(0, 0, 0, 0)");
//        ObjectData toolbox = new ObjectData();
//        homeoption.put("toolbox", toolbox);
//        toolbox.put("show", true);
//        ObjectData feature = new ObjectData();
//        toolbox.put("feature", feature);
//        ObjectData restore = new ObjectData();
//        feature.put("restore", restore);
//        restore.put("show", true);
//        ObjectData saveAsImage = new ObjectData();
//        feature.put("saveAsImage", saveAsImage);
//        saveAsImage.put("show", true);

        ArrayData series = new ArrayData();
        homeoption.put("series", series);

        ObjectData speed = new ObjectData();
        speed.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.chart_name_speed));
        speed.put("type", "gauge");
        speed.put("z", 3);
        speed.put("min", 0);
        speed.put("max", 40);
        speed.put("splitNumber", 10);
        speed.put("radius", "76%");

        ObjectData axisLines = new ObjectData();
        speed.put("axisLine", axisLines);
        ObjectData lineStyleset = new ObjectData();
        axisLines.put("lineStyle", lineStyleset);
        lineStyleset.put("width", 10);
        ArrayData color = new ArrayData();
        lineStyleset.put("color", color);
        ArrayData color1 = new ArrayData();
        color1.add(0.2);
        color1.add("#108135");
        ArrayData color2 = new ArrayData();
        color2.add(0.8);
        color2.add("#f08519");
        ArrayData color3 = new ArrayData();
        color3.add(1);
        color3.add("#fc210a");
        color.add(color1);
        color.add(color2);
        color.add(color3);

        ObjectData axisTick = new ObjectData();
        speed.put("axisTick", axisTick);
        axisTick.put("length", 12);
        ObjectData lineStyle2 = new ObjectData();
        axisTick.put("lineStyle", lineStyle2);
        lineStyle2.put("color", "auto");

        ObjectData splitLine = new ObjectData();
        speed.put("splitLine", splitLine);
        axisTick.put("length", 16);
        ObjectData lineStyle3 = new ObjectData();
        splitLine.put("lineStyle", lineStyle3);
        lineStyle3.put("color", "auto");

        ObjectData axisLabel = new ObjectData();
        speed.put("axisLabel", axisLabel);
        axisLabel.put("fontSize", 9);
        axisLabel.put("backgroundColor", "auto");
        axisLabel.put("borderRadius", 2);
        axisLabel.put("color", "#eee");
        axisLabel.put("padding", 2);
        axisLabel.put("textShadowBlur", 2);
        axisLabel.put("textShadowOffsetX", 1);
        axisLabel.put("textShadowOffsetY", 1);
        axisLabel.put("textShadowColor", "#222");

        ObjectData title = new ObjectData();
        speed.put("title", title);
        title.put("fontWeight", "bolder");
        title.put("fontSize", 11);
        title.put("fontStyle", "italic");

        ObjectData detail = new ObjectData();
        speed.put("detail", detail);
        detail.put("fontWeight", "bolder");
        detail.put("fontSize", 12);
        detail.put("borderRadius", 2);
        detail.put("backgroundColor", "#444");
        detail.put("borderColor", "#aaa");
        detail.put("shadowBlur", 4);
        detail.put("shadowColor", "#333");
        detail.put("shadowOffsetX", 0);
        detail.put("shadowOffsetY", 2);
        detail.put("borderWidth", 2);
        detail.put("textBorderColor", "#000");
        detail.put("textBorderWidth", 2);
        detail.put("textShadowBlur", 2);
        detail.put("textShadowColor", "#fff");
        detail.put("textShadowOffsetX", 0);
        detail.put("textShadowOffsetY", 0);
        detail.put("fontFamily", "Arial");
        detail.put("width", 24);
        detail.put("color", "#eee");
        detail.put("rich", new ObjectData());

        ArrayData data = new ArrayData();
        speed.put("data", data);
        homespeed = new ObjectData();
        data.add(homespeed);
        homespeed.put("value", 0);
        homespeed.put("name", "km/h");
//        ObjectData title1 = new ObjectData();
//        homespeed.put("title",title1);
//        ArrayData offsetCenter4 = new ArrayData();
//        title1.put("offsetCenter",offsetCenter4);
//        offsetCenter4.add(0);
//        offsetCenter4.add("20%");

        ObjectData hert = new ObjectData();
        if(DkhomeApplication.mApp.testManager.hertconnect)
        {
            hert.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.chart_name_rpm));
            hert.put("min", 0);
            hert.put("max", 120);
            hert.put("splitNumber", 12);
        }
        else
        {
            hert.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.chart_name_hert));
            hert.put("min", 0);
            hert.put("max", 240);
            hert.put("splitNumber", 12);
        }
        hert.put("type", "gauge");
        ArrayData center = new ArrayData();
        hert.put("center", center);
        center.add("18%");
        center.add("55%");
        hert.put("radius", "55%");
        hert.put("endAngle", 45);


        ObjectData axisLine1 = new ObjectData();
        hert.put("axisLine", axisLine1);
        lineStyleset = new ObjectData();
        axisLine1.put("lineStyle", lineStyleset);
        lineStyleset.put("width", 8);
        lineStyleset.put("color", color);

        ObjectData axisTick1 = new ObjectData();
        hert.put("axisTick", axisTick1);
        axisTick1.put("length", 12);
        ObjectData lineStyle4 = new ObjectData();
        axisTick1.put("lineStyle", lineStyle4);
        lineStyle4.put("color", "auto");

        ObjectData splitLine1 = new ObjectData();
        hert.put("splitLine", splitLine1);
        splitLine1.put("length", 18);
        ObjectData lineStyle5 = new ObjectData();
        splitLine1.put("lineStyle", lineStyle5);
        lineStyle5.put("color", "auto");

        ObjectData pointer = new ObjectData();
        hert.put("pointer", pointer);
        pointer.put("width", 5);

        ObjectData axisLabel1 = new ObjectData();
        hert.put("axisLabel", axisLabel1);
        axisLabel1.put("fontSize", 5);

        ObjectData title2 = new ObjectData();
        hert.put("title", title2);
        ArrayData offsetCenter = new ArrayData();
        title2.put("fontSize", 13);
        title2.put("offsetCenter", offsetCenter);
        offsetCenter.add(0);
        offsetCenter.add("-30%");

        ObjectData detail1 = new ObjectData();
        hert.put("detail", detail1);
        detail1.put("fontWeight", "bolder");

        ArrayData data1 = new ArrayData();
        hert.put("data", data1);
        homeRpm = new ObjectData();
        data1.add(homeRpm);
        homeRpm.put("value", 0);
        homeRpm.put("name", "rpm");


        ObjectData starng = new ObjectData();
        starng.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.chart_name_select));
        starng.put("type", "gauge");
        ArrayData center1 = new ArrayData();
        starng.put("center", center1);
        center1.add("83%");
        center1.add("55%");
        starng.put("radius", "55%");
        starng.put("min", 0);
        starng.put("max", 100);
        starng.put("endAngle", -45);
        starng.put("startAngle", 135);
        starng.put("splitNumber", 10);

        ObjectData axisLine2 = new ObjectData();
        starng.put("axisLine", axisLine2);
        lineStyleset = new ObjectData();
        axisLine2.put("lineStyle", lineStyleset);
        lineStyleset.put("width", 8);
        lineStyleset.put("color", color);

        ObjectData axisTick2 = new ObjectData();
        starng.put("axisTick", axisTick2);
        axisTick2.put("length", 12);
        ObjectData lineStyle7 = new ObjectData();
        axisTick2.put("lineStyle", lineStyle7);
        lineStyle7.put("color", "auto");

        ObjectData splitLine2 = new ObjectData();
        starng.put("splitLine", splitLine2);
        splitLine2.put("length", 20);
        ObjectData lineStyle8 = new ObjectData();
        splitLine2.put("lineStyle", lineStyle8);
        lineStyle8.put("color", "auto");

        ObjectData pointer1 = new ObjectData();
        starng.put("pointer", pointer1);
        pointer1.put("width", 5);

        ObjectData axisLabel2 = new ObjectData();
        starng.put("axisLabel", axisLabel2);
        axisLabel2.put("fontSize", 5);


        ObjectData title3 = new ObjectData();
        starng.put("title", title3);
        title3.put("fontSize", 13);
        ArrayData offsetCenter1 = new ArrayData();
        title3.put("offsetCenter", offsetCenter1);
        offsetCenter1.add(0);
        offsetCenter1.add("-30%");

        ObjectData detail2 = new ObjectData();
        starng.put("detail", detail2);
        detail2.put("fontWeight", "bolder");

        ArrayData data2 = new ArrayData();
        starng.put("data", data2);
        homeselect = new ObjectData();
        data2.add(homeselect);
        homeselect.put("value", 0);
        homeselect.put("name", "%");

        series.add(speed);
        series.add(hert);
        series.add(starng);

        String url = "javascript:createChart('orther'," + homeoption.toString() + ");";
        chartShow.loadUrl(url);
    }
}
