package com.dk.dkhome.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.ChartData;
import com.dk.dkhome.presenter.SportDetialPresenter;

import org.json.JSONException;

import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.mywidget.RoundProgressBar;

public class ChartView {

    public SportDetialPresenter mSportDetialPresenter;
    public View view;
    public WebView chart;
    public ObjectData sportData1;
    public ArrayData xdata;
    public ArrayData leaveldata = new ArrayData();
    public ArrayData speeddata = new ArrayData();
    public ArrayData heartdata = new ArrayData();
    public RoundProgressBar progressBar;
    public TextView pvalue;
    public TextView speedvalue;
    public TextView disvalue;
    public TextView burnedvalue;
    public TextView timevalue;

    public ChartView(SportDetialPresenter mSportDetialPresenter) {
        this.mSportDetialPresenter = mSportDetialPresenter;
        init();
        initOption();
        initData();
    }

    public void doUpdata() {
        progressBar.setProgress(100 * mSportDetialPresenter.course.current / mSportDetialPresenter.course.during);
        pvalue.setText(String.format("%.1f", (double)100 * mSportDetialPresenter.course.current / mSportDetialPresenter.course.during) + "%");
        speedvalue.setText(String.format("%.1f",(double)mSportDetialPresenter.course.dis*3600/mSportDetialPresenter.course.current) + "km/h");
        disvalue.setText(String.format("%.1f",mSportDetialPresenter.course.dis) + "km");
        timevalue.setText(praseTime(mSportDetialPresenter.course.current));
        burnedvalue.setText(String.format("%d",(int)mSportDetialPresenter.course.totalCarl)+"kcal");
        leaveldata.add(mSportDetialPresenter.deviceView.device.nowleavel);
        heartdata.add(mSportDetialPresenter.course.nowhert);
        speeddata.add(mSportDetialPresenter.course.nowspeed);
        xdata.add(praseTime(mSportDetialPresenter.course.current));
        String url = "javascript:createChart('orther'," + sportData1.toString() + ");";
        chart.loadUrl(url);
    }


    private void init() {
        LayoutInflater mInflater = (LayoutInflater) mSportDetialPresenter.mSportDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.fragment_chart, null);
        chart = view.findViewById(R.id.chart);
        chart.getSettings().setAllowFileAccess(true);
        chart.getSettings().setJavaScriptEnabled(true);
        chart.setWebViewClient(mSportDetialPresenter.mWebViewClient);
        chart.setVisibility(View.VISIBLE);
        chart.setBackgroundColor(0); // 设置背景色
        chart.getBackground().setAlpha(0);
        chart.loadUrl("file:///android_asset/echart/myechart.html");
        progressBar = view.findViewById(R.id.carprogress);
        pvalue = view.findViewById(R.id.pvalue);
        speedvalue = view.findViewById(R.id.speedvalue);
        disvalue = view.findViewById(R.id.disvalue);
        timevalue = view.findViewById(R.id.timevalue);
        burnedvalue = view.findViewById(R.id.burnedvalue);
        progressBar.setMax(100);
    }

    private String praseTime(int itime) {
        int max = itime;
        String time = "";
        if (max / 60 / 60 > 0) {
            time = String.format("%d:%02d:%02d", max / 60 / 60, (max / 60) % 60, max % 60);
        } else if (max / 60 > 0) {
            time = String.format("%02d:%02d", (max / 60) % 60, max % 60);
        } else {
            time = String.format("%02d", max % 60);
        }
        return time;
    }

    private void initData() {
        progressBar.setProgress(100 * mSportDetialPresenter.course.current / mSportDetialPresenter.course.during);
        pvalue.setText(String.format("%d", 100 * mSportDetialPresenter.course.current / mSportDetialPresenter.course.during) + "%");
        speedvalue.setText(String.valueOf(mSportDetialPresenter.course.nowspeed) + "km/h");
        disvalue.setText(String.valueOf(mSportDetialPresenter.course.dis) + "km");
        timevalue.setText(praseTime(mSportDetialPresenter.course.current));
        burnedvalue.setText(String.valueOf(mSportDetialPresenter.course.totalCarl));

        try {
            for (int i = 0; i < mSportDetialPresenter.course.leavels.length(); i++) {
                leaveldata.add(mSportDetialPresenter.course.leavels.getDouble(i));
                heartdata.add(mSportDetialPresenter.course.herts.getDouble(i));
                speeddata.add(mSportDetialPresenter.course.speeds.getDouble(i));
                xdata.add(praseTime(i+1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initOption() {
        sportData1 = new ObjectData();
        ObjectData grid = new ObjectData();
        sportData1.put("grid",grid);
        grid.put("left","10%");
        ObjectData tooltip = new ObjectData();
        sportData1.put("tooltip", tooltip);
        tooltip.put("trigger", "axis");
        ObjectData axisPointer = new ObjectData();
        tooltip.put("axisPointer", axisPointer);
        axisPointer.put("type", "cross");
        ObjectData crossStyle = new ObjectData();
        axisPointer.put("crossStyle", crossStyle);
        crossStyle.put("color", "#999");

        ArrayData datazoom = new ArrayData();
        sportData1.put("dataZoom", datazoom);
        ObjectData zoom1 = new ObjectData();
        datazoom.add(zoom1);
        zoom1.put("type", "slider");
        zoom1.put("start", 95);
        zoom1.put("end", 100);
        ObjectData zoom2 = new ObjectData();
        datazoom.add(zoom2);
        zoom2.put("type", "inside");
        zoom2.put("start", 95);
        zoom2.put("end", 100);

        ArrayData legend = new ArrayData();
        legend.add(mSportDetialPresenter.mSportDetialActivity.getString(R.string.home_sport_chart_speed));
        legend.add(mSportDetialPresenter.mSportDetialActivity.getString(R.string.home_sport_chart_leavel));


        ObjectData xAxis = new ObjectData();
        sportData1.put("xAxis", xAxis);
        xAxis.put("type", "category");
        xdata = new ArrayData();
        xAxis.put("data", xdata);

        ArrayData yAxis = new ArrayData();
        sportData1.put("yAxis", yAxis);
        ObjectData select = new ObjectData();
        ObjectData speed = new ObjectData();
        ObjectData hert = new ObjectData();
        yAxis.add(speed);
        yAxis.add(select);
        if(DkhomeApplication.mApp.testManager.hertconnect)
        {
            legend.add(mSportDetialPresenter.mSportDetialActivity.getString(R.string.chart1_carl_hert));
            yAxis.add(hert);
            grid.put("right","25%");
        }
        else{
            grid.put("right","15%");
        }
        select.put("type", "value");
        select.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.home_sport_chart_leavel_unit));
        select.put("min", 0);
        select.put("max", 100);
        select.put("position", "right");
        select.put("interval", 20);
        ObjectData axisLabel = new ObjectData();
        select.put("axisLabel", axisLabel);
        ObjectData axisLine = new ObjectData();
        select.put("axisLine", axisLine);
        axisLine.put("show", true);
        ObjectData lineStyle = new ObjectData();
        axisLine.put("lineStyle", lineStyle);
        lineStyle.put("color", ChartData.COLOR_YELLOW);
        axisLabel.put("formatter", "{value} %");

        hert.put("type", "value");
        hert.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.home_sport_chart_hert_unit));
        hert.put("min", 0);
        hert.put("max", 250);
        hert.put("position", "left");
        hert.put("offset", 80);
        hert.put("interval", 50);
        ObjectData axisLabel3 = new ObjectData();
        hert.put("axisLabel", axisLabel3);
        ObjectData axisLine3 = new ObjectData();
        hert.put("axisLine", axisLine3);
        axisLine3.put("show", true);
        ObjectData lineStyle3 = new ObjectData();
        axisLine3.put("lineStyle", lineStyle3);
        lineStyle3.put("color", ChartData.COLOR_GREEN);
        axisLabel3.put("formatter", "{value} min");

        speed.put("type", "value");
        speed.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.home_sport_chart_speed_unit));
        speed.put("min", 0);
        speed.put("max", 50);
        speed.put("position", "left");
        speed.put("interval", 10);
        ObjectData axisLabel1 = new ObjectData();
        speed.put("axisLabel", axisLabel1);
        ObjectData axisLine1 = new ObjectData();
        speed.put("axisLine", axisLine1);
        axisLine1.put("show", true);
        ObjectData lineStyle1 = new ObjectData();
        axisLine1.put("lineStyle", lineStyle1);
        lineStyle1.put("color", ChartData.COLOR_GREEN);
        axisLabel1.put("formatter", "{value} min");


        ArrayData series = new ArrayData();
        sportData1.put("series", series);
        ObjectData sdata = new ObjectData();
        series.add(sdata);
        leaveldata = new ArrayData();
        sdata.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.home_sport_chart_leavel));
        sdata.put("data", leaveldata);
        sdata.put("type", "line");
        sdata.put("yAxisIndex", 1);
        ObjectData itemStyle1 = new ObjectData();
        sdata.put("itemStyle",itemStyle1);
        ObjectData normal1 = new ObjectData();
        itemStyle1.put("normal",normal1);
        normal1.put("color","#ff5e3a");
        ObjectData lineStyle4 = new ObjectData();
        normal1.put("lineStyle",lineStyle4);
        lineStyle4.put("color","#ff5e3a");
        ObjectData markPoint = new ObjectData();
        sdata.put("markPoint", markPoint);
        ArrayData mark = new ArrayData();
        markPoint.put("data", mark);
        ObjectData max = new ObjectData();
        mark.add(max);
        max.put("type", "max");
        max.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.health_chart_max));
        ObjectData min = new ObjectData();
        mark.add(min);
        min.put("type", "min");
        min.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.health_chart_min));


        ObjectData sdata2 = new ObjectData();
        series.add(sdata2);
        sdata2.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.home_sport_chart_speed));
        sdata2.put("data", speeddata);
        sdata2.put("type", "line");
        sdata2.put("yAxisIndex", 0);
        ObjectData itemStyle2 = new ObjectData();
        sdata2.put("itemStyle",itemStyle2);
        ObjectData normal2 = new ObjectData();
        itemStyle2.put("normal",normal2);
        normal2.put("color","#00FF00");
        ObjectData lineStyle2 = new ObjectData();
        normal2.put("lineStyle",lineStyle2);
        lineStyle2.put("color","#00FF00");
        sdata2.put("type","line");
        ObjectData markPoint2 = new ObjectData();
        sdata2.put("markPoint", markPoint2);
        ArrayData mark2 = new ArrayData();
        markPoint2.put("data", mark2);
        ObjectData max2 = new ObjectData();
        mark2.add(max2);
        max2.put("type", "max");
        max2.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.health_chart_max));
        ObjectData min2 = new ObjectData();
        mark2.add(min2);
        min2.put("type", "min");
        min2.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.health_chart_min));


        ObjectData sdata3 = new ObjectData();
        if(DkhomeApplication.mApp.testManager.hertconnect)
        series.add(sdata3);
        sdata3.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.chart1_carl_hert));
        sdata3.put("data", heartdata);
        sdata3.put("type", "line");
        sdata3.put("yAxisIndex", 2);
        ObjectData markPoint3 = new ObjectData();
        sdata3.put("markPoint", markPoint3);
        ArrayData mark3 = new ArrayData();
        markPoint3.put("data", mark3);
        ObjectData max3 = new ObjectData();
        mark3.add(max3);
        max3.put("type", "max");
        max3.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.health_chart_max));
        ObjectData min3 = new ObjectData();
        mark3.add(min3);
        min3.put("type", "min");
        min3.put("name", mSportDetialPresenter.mSportDetialActivity.getString(R.string.health_chart_min));

    }

}
