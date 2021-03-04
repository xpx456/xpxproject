package com.dk.dkhome.presenter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.ChartData;
import com.dk.dkhome.entity.Eat;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.entity.Goal;
import com.dk.dkhome.entity.SportData;
import com.dk.dkhome.utils.FoodManager;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.ChartActivity;
import com.dk.dkhome.view.adapter.SportPageAdapter;
import com.github.abel533.echarts.Grid;

import java.util.Map;

import intersky.appbase.Presenter;
import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.SemicircleProgressView;


public class ChartPresenter implements Presenter {
    private static final int DECIMAL_DIGITS = 1;

    public ChartActivity mChartActivity;

    public WebView chart;
    public WebView chart2;
    public WebView chart3;
    public ObjectData sportData1;
    public ArrayData xdata;
    public ArrayData eatdata = new ArrayData();
    public ArrayData burndata = new ArrayData();
    public ArrayData timedata = new ArrayData();

    public ObjectData goalData1;
    public ArrayData xdaydata = new ArrayData();
    public ArrayData dayburndata = new ArrayData();

    public ObjectData persentData;
    public ObjectData elliptical;
    public ObjectData rowing;
    public ObjectData exercise;
    public ObjectData spinning;
    public ObjectData base;

    public RelativeLayout btnHistory;
    public RelativeLayout btnGoal;
    public TextView historytitle;
    public TextView goalTitle;
    public SportPageAdapter sportPageAdapter;
    public NoScrollViewPager noScrollViewPager;
    public View history;
    public TextView totalhvalue;
    public TextView hburnedValue;
    public TextView hbburnedValue;
    public TextView sportTime;
    public View goal;
    public TextView totalgvalue;
    public TextView eatValue;
    public TextView burnedValue;
    public TextView persentValue;
    public SemicircleProgressView progressView;
    public ChartPresenter(ChartActivity mChartActivity) {
        this.mChartActivity = mChartActivity;
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub\
        mChartActivity.setContentView(R.layout.activity_chart);
        ImageView back = mChartActivity.findViewById(R.id.back);
        back.setOnClickListener(mChartActivity.mBackListener);
        TextView title = mChartActivity.findViewById(R.id.title);
        title.setText(mChartActivity.getString(R.string.chart_title));
        sportPageAdapter = new SportPageAdapter();
        initHisory();
        initGoal();
        sportPageAdapter.mViews.add(history);
        sportPageAdapter.mViews.add(goal);
        noScrollViewPager = mChartActivity.findViewById(R.id.page);
        btnHistory = mChartActivity.findViewById(R.id.btnhistory);
        historytitle = mChartActivity.findViewById(R.id.historytext);
        btnGoal = mChartActivity.findViewById(R.id.btngoal);
        goalTitle = mChartActivity.findViewById(R.id.goaltext);
        noScrollViewPager.setAdapter(sportPageAdapter);
        noScrollViewPager.setNoScroll(true);
        btnGoal.setOnClickListener(goalListener);
        btnHistory.setOnClickListener(historyListener);

        historytitle.setTextColor(Color.parseColor("#ff5e3a"));
        goalTitle.setTextColor(Color.parseColor("#333333"));
        noScrollViewPager.setCurrentItem(0);

        initOption();
        initOption2();
        initOption3();
        initData();

    }

    public void initHisory() {
        LayoutInflater mInflater = (LayoutInflater) mChartActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        history = mInflater.inflate(R.layout.fragment_chart_history,null);
        chart2 = history.findViewById(R.id.chart1);
        chart = history.findViewById(R.id.chart);
        chart2.getSettings().setAllowFileAccess(true);
        chart2.getSettings().setJavaScriptEnabled(true);
        chart2.setWebViewClient(mWebViewClient);
        chart2.setVisibility(View.VISIBLE);
        chart2.loadUrl("file:///android_asset/echart/myechart.html");
        chart.getSettings().setAllowFileAccess(true);
        chart.getSettings().setJavaScriptEnabled(true);
        chart.setWebViewClient(mWebViewClient);
        chart.setVisibility(View.VISIBLE);
        chart.loadUrl("file:///android_asset/echart/myechart.html");
        totalhvalue = history.findViewById(R.id.totalvalue);
        sportTime = history.findViewById(R.id.sporttimevalue);
        hburnedValue = history.findViewById(R.id.burnedvalue);
        hbburnedValue = history.findViewById(R.id.basevalue);
    }

    private void initGoal()
    {
        LayoutInflater mInflater = (LayoutInflater) mChartActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        goal = mInflater.inflate(R.layout.fragment_chart_goal,null);
        chart3 = goal.findViewById(R.id.chart);
        chart3.getSettings().setAllowFileAccess(true);
        chart3.getSettings().setJavaScriptEnabled(true);
        chart3.setWebViewClient(mWebViewClient);
        chart3.setVisibility(View.VISIBLE);
        chart3.loadUrl("file:///android_asset/echart/myechart.html");
        totalgvalue = goal.findViewById(R.id.totalvalue);
        eatValue = goal.findViewById(R.id.eatvalue);
        burnedValue = goal.findViewById(R.id.burnedvalue);
        persentValue = goal.findViewById(R.id.persentvalue);
        progressView = goal.findViewById(R.id.carprogress);
    }

    public void initData() {
        double etotal = DkhomeApplication.mApp.foodManager.getTotalCarl();
        double stotal = DkhomeApplication.mApp.sportDataManager.getToalCarl();
        double btotal = DkhomeApplication.mApp.sportDataManager.getBToalCarl();
        int goal = EquipData.weightCarl(DkhomeApplication.mApp.goal.goalweight);
        totalhvalue.setText(String.valueOf(mChartActivity.getString(R.string.chart_sport_total_title1)+TimeUtils.measureDay(DkhomeApplication.mApp.mAccount.creat,TimeUtils.getDate()))+mChartActivity.getString(R.string.chart_unit_day));
        hburnedValue.setText(String.format("%d",(int)(stotal+btotal))+"kcal");
        hbburnedValue.setText(String.format("%d",(int)DkhomeApplication.mApp.sportDataManager.getTotalDis())+"km");
        int min = (int) (DkhomeApplication.mApp.sportDataManager.getTotalDuring()/60);
        if(min > 60)
        sportTime.setText(String.valueOf(min/60)+" hour");
        else
            sportTime.setText(String.valueOf(min)+" min");

        if(DkhomeApplication.mApp.goal.type == Goal.TYPE_LOSE_WEIGHT) {
            totalgvalue.setText(mChartActivity.getString(R.string.goal_name2)+String.valueOf(DkhomeApplication.mApp.goal.goalweight)+"kg");
            int last = (int) (btotal+stotal-etotal);
            if(last > 0)
            {
                progressView.setSubTile(mChartActivity.getString(R.string.main_calor_lose_title));
                if(last > goal)
                {
                    progressView.setSesameValues(last,
                            goal);
                    progressView.setTitle(mChartActivity.getString(R.string.mian_day_goal_finish));
                    progressView.setSubTile("");
                    persentValue.setText(String.valueOf(100)+"%");
                }
                else
                {
                    progressView.setSesameValues(goal -last,
                            goal);
                    persentValue.setText(String.format("%.1f",(double)100*last/goal)+"%");
                }

            }
            else
            {
                progressView.setSesameValues(0,goal-last);
                persentValue.setText(String.valueOf(0)+"%");
            }
        }
        else if(DkhomeApplication.mApp.goal.type == Goal.TYPE_HEALTH) {
            int totalb = (int) (btotal+stotal);
            int total = (int) etotal;
            totalhvalue.setText(mChartActivity.getString(R.string.goal_name1));
            if(total > totalb)
            {
                progressView.setSubTile(mChartActivity.getString(R.string.main_calor_lose_title));
                progressView.setSesameValues(total-totalb,
                        total);
                persentValue.setText(String.valueOf(100*totalb/total)+"%");
            }
            else if(total < totalb)
            {
                progressView.setSubTile(mChartActivity.getString(R.string.main_calor_add_title));
                progressView.setSesameValues(totalb-total,totalb);
                persentValue.setText(String.format("%.1f",(double)100*total/totalb)+"%");
            }
            else
            {
                progressView.setSesameValues(0,total);
                progressView.setTitle(mChartActivity.getString(R.string.mian_day_goal_finish));
                progressView.setSubTile("");
                persentValue.setText(String.valueOf(100)+"%");
            }
        }
        else{
            totalhvalue.setText(mChartActivity.getString(R.string.goal_name3)+String.valueOf(DkhomeApplication.mApp.goal.goalweight)+"kg");
            int last = (int) (etotal-etotal-stotal);
            if(last > 0)
            {
                progressView.setSubTile(mChartActivity.getString(R.string.main_calor_add1_title));
                if(last > goal)
                {
                    progressView.setSesameValues(last,
                            goal);
                    progressView.setTitle(mChartActivity.getString(R.string.mian_day_goal_finish));
                    progressView.setSubTile("");
                    persentValue.setText(String.valueOf(100)+"%");
                }
                else
                {
                    progressView.setSesameValues(last,
                            goal);
                    persentValue.setText(String.format("%.1f",(double)100*last/goal)+"%");
                }

            }
            else
            {
                progressView.setSesameValues(0,goal-last);
                persentValue.setText(String.valueOf(100)+"%");
            }
        }
        eatValue.setText(String.format("%d",(int)etotal)+"kcal");
        int a = (int) (stotal+btotal);
        burnedValue.setText(String.format("%d",a)+"kcal");


    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
//        chart.destroy();
//        chart2.destroy();
//        chart3.destroy();
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    private void initOption() {
        sportData1 = new ObjectData();
        ObjectData tooltip = new ObjectData();
        ObjectData grid = new ObjectData();
        sportData1.put("grid",grid);
        grid.put("left","10%");
        grid.put("right","15%");
        sportData1.put("tooltip",tooltip);
        tooltip.put("trigger","axis");
        ObjectData axisPointer = new ObjectData();
        tooltip.put("axisPointer",axisPointer);
        axisPointer.put("type","cross");
        ObjectData crossStyle = new ObjectData();
        axisPointer.put("crossStyle",crossStyle);
        crossStyle.put("color","#999");
        ObjectData data = new ObjectData();
        sportData1.put("legend",data);
        ArrayData legend = new ArrayData();
        data.put("data",legend);
        legend.add(mChartActivity.getString(R.string.chart1_carl_eat));
        legend.add(mChartActivity.getString(R.string.chart1_carl_burn));
        legend.add(mChartActivity.getString(R.string.chart1_sport_time));

        ObjectData xAxis = new ObjectData();
        sportData1.put("xAxis",xAxis);
        xAxis.put("type","category");
        xdata = new ArrayData();
        xAxis.put("data",xdata);

        ArrayData yAxis = new ArrayData();
        sportData1.put("yAxis",yAxis);
        ObjectData carl = new ObjectData();
        ObjectData carleat = new ObjectData();
        ObjectData time = new ObjectData();
        yAxis.add(carl);
        yAxis.add(time);
        yAxis.add(carleat);
        carl.put("type","value");
        carl.put("name",mChartActivity.getString(R.string.chart1_carl_unit_c));
        carl.put("min",0);
        carl.put("max",5000);
        carl.put("interval",1000);
        carl.put("position","right");
        ObjectData axisLabel = new ObjectData();
        carl.put("axisLabel",axisLabel);
        ObjectData axisLine = new ObjectData();
        carl.put("axisLine",axisLine);
        axisLine.put("show",true);
        ObjectData lineStyle = new ObjectData();
        axisLine.put("lineStyle",lineStyle);
        lineStyle.put("color", ChartData.COLOR_YELLOW);
//        axisLabel.put("formatter","{value} kCal");

//        carleat.put("type","value");
//        carleat.put("name",mChartActivity.getString(R.string.chart1_carl_unit_e));
//        carleat.put("min",0);
//        carleat.put("max",5000);
//        carleat.put("position","right");
//        carleat.put("offset",80);
//        carleat.put("interval",1000);
//        ObjectData axisLabel2 = new ObjectData();
//        carleat.put("axisLabel",axisLabel2);
//        ObjectData axisLine2 = new ObjectData();
//        carleat.put("axisLine",axisLine2);
//        axisLine2.put("show",true);
//        ObjectData lineStyle2 = new ObjectData();
//        axisLine2.put("lineStyle",lineStyle2);
//        lineStyle2.put("color", ChartData.COLOR_YELLOW);
//        axisLabel2.put("formatter","{value} kCal");


        time.put("type","value");
        time.put("name",mChartActivity.getString(R.string.chart1_time_unit));
        time.put("min",0);
        time.put("max",200);
        time.put("position","left");
        time.put("interval",40);
        ObjectData axisLabel1 = new ObjectData();
        time.put("axisLabel",axisLabel1);
        ObjectData axisLine1 = new ObjectData();
        time.put("axisLine",axisLine1);
        axisLine1.put("show",true);
        ObjectData lineStyle1 = new ObjectData();
        axisLine1.put("lineStyle",lineStyle1);
        lineStyle1.put("color", ChartData.COLOR_GREEN);
//        axisLabel1.put("formatter","{value} min");

        ArrayData series = new ArrayData();
        sportData1.put("series",series);
        ObjectData sdata = new ObjectData();
        series.add(sdata);
        eatdata = new ArrayData();
        sdata.put("name",mChartActivity.getString(R.string.chart1_carl_eat));
        sdata.put("data", eatdata);
        sdata.put("type","bar");
        ObjectData itemStyle = new ObjectData();
        sdata.put("itemStyle",itemStyle);
        ObjectData normal = new ObjectData();
        itemStyle.put("normal",normal);
        normal.put("color","#5ac8fb");
        sdata.put("yAxisIndex",0);
        ObjectData markPoint = new ObjectData();
        sdata.put("markPoint",markPoint);
        ArrayData mark = new ArrayData();
        markPoint.put("data",mark);
        ObjectData max = new ObjectData();
        mark.add(max);
        max.put("type","max");
        max.put("name",mChartActivity.getString(R.string.health_chart_max));
        ObjectData min = new ObjectData();
        mark.add(min);
        min.put("type","min");
        min.put("name",mChartActivity.getString(R.string.health_chart_min));


        ObjectData sdata1 = new ObjectData();
        series.add(sdata1);
        sdata1.put("name",mChartActivity.getString(R.string.chart1_carl_burn));
        sdata1.put("data", burndata);
        sdata1.put("type","bar");
        sdata1.put("yAxisIndex",0);
        ObjectData itemStyle1 = new ObjectData();
        sdata1.put("itemStyle",itemStyle1);
        ObjectData normal1 = new ObjectData();
        itemStyle1.put("normal",normal1);
        normal1.put("color","#ff5e3a");
        ObjectData markPoint1 = new ObjectData();
        sdata1.put("markPoint",markPoint1);
        ArrayData mark1 = new ArrayData();
        markPoint1.put("data",mark1);
        ObjectData max1 = new ObjectData();
        mark1.add(max1);
        max1.put("type","max");
        max1.put("name",mChartActivity.getString(R.string.health_chart_max));
        ObjectData min1 = new ObjectData();
        mark1.add(min1);
        min1.put("type","min");
        min1.put("name",mChartActivity.getString(R.string.health_chart_min));


        ObjectData sdata2 = new ObjectData();
        series.add(sdata2);
        sdata2.put("name",mChartActivity.getString(R.string.chart1_sport_time));
        sdata2.put("data", timedata);
        sdata2.put("yAxisIndex",1);
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
        sdata2.put("markPoint",markPoint2);
        ArrayData mark2 = new ArrayData();
        markPoint2.put("data",mark2);
        ObjectData max2 = new ObjectData();
        mark2.add(max2);
        max2.put("type","max");
        max2.put("name",mChartActivity.getString(R.string.health_chart_max));
        ObjectData min2 = new ObjectData();
        mark2.add(min2);
        min2.put("type","min");
        min2.put("name",mChartActivity.getString(R.string.health_chart_min));
        for(Map.Entry<String, SportData> temp:DkhomeApplication.mApp.sportDataManager.dayData.entrySet())
        {
            SportData tsportData = temp.getValue();
            Eat eat = DkhomeApplication.mApp.foodManager.dayEat.get(tsportData.time);
            if(eat == null)
            {
                eatdata.add(DkhomeApplication.mApp.foodManager.totalCarl);
            }
            else
            {
                eatdata.add(eat.carl);
            }
            timedata.add(tsportData.gettotalTime()/60);
            burndata.add((int)(tsportData.gettotalCarl()+tsportData.baseCarl));
            xdata.add(tsportData.time);

        }
        
    }

    private void initOption2() {
        goalData1 = new ObjectData();
        ObjectData grid = new ObjectData();
        goalData1.put("grid",grid);
        grid.put("left","15%");
        grid.put("right","20%");
        ObjectData tooltip = new ObjectData();
        goalData1.put("tooltip",tooltip);
        tooltip.put("trigger","axis");
        ObjectData axisPointer = new ObjectData();
        tooltip.put("axisPointer",axisPointer);
        axisPointer.put("type","cross");
        ObjectData crossStyle = new ObjectData();
        axisPointer.put("crossStyle",crossStyle);
        crossStyle.put("color","#999");

        ObjectData xAxis = new ObjectData();
        goalData1.put("xAxis",xAxis);
        xAxis.put("type","category");
        xdaydata = new ArrayData();
        xAxis.put("data",xdaydata);

        ArrayData yAxis = new ArrayData();
        goalData1.put("yAxis",yAxis);
        ObjectData carl = new ObjectData();
        yAxis.add(carl);
        carl.put("type","value");
        carl.put("name",mChartActivity.getString(R.string.chart1_carl_unit_b));
        carl.put("min",-5000);
        carl.put("max",5000);
        carl.put("interval",1000);
        ObjectData axisLabel = new ObjectData();
        carl.put("axisLabel",axisLabel);

        ArrayData series = new ArrayData();
        goalData1.put("series",series);
        ObjectData sdata1 = new ObjectData();
        series.add(sdata1);
        sdata1.put("name",mChartActivity.getString(R.string.chart1_carl_burn));
        sdata1.put("data", dayburndata);
        sdata1.put("type","bar");
        sdata1.put("yAxisIndex",0);
        ObjectData itemStyle1 = new ObjectData();
        sdata1.put("itemStyle",itemStyle1);
        ObjectData normal1 = new ObjectData();
        itemStyle1.put("normal",normal1);
        normal1.put("color","#ff5e3a");
        ObjectData markPoint1 = new ObjectData();
        sdata1.put("markPoint",markPoint1);
        ArrayData mark1 = new ArrayData();
        markPoint1.put("data",mark1);
        ObjectData max1 = new ObjectData();
        mark1.add(max1);
        max1.put("type","max");
        max1.put("name",mChartActivity.getString(R.string.health_chart_max));
        ObjectData min1 = new ObjectData();
        mark1.add(min1);
        min1.put("type","min");
        min1.put("name",mChartActivity.getString(R.string.health_chart_min));

        ObjectData markLine = new ObjectData();
        sdata1.put("markLine",markLine);
        ArrayData mldata = new ArrayData();
        markLine.put("data",mldata);
        ObjectData mklinedata = new ObjectData();
        mldata.add(mklinedata);
        ObjectData lable = new ObjectData();
        mklinedata.put("label",lable);
        lable.put("position","end");
        lable.put("formatter",mChartActivity.getString(R.string.chart_head_goal)+"("+String.valueOf(DkhomeApplication.mApp.goal.daycarl)+")");
        mklinedata.put("yAxis",DkhomeApplication.mApp.goal.daycarl);
        for(Map.Entry<String, SportData> temp:DkhomeApplication.mApp.sportDataManager.dayData.entrySet())
        {
            SportData tsportData = temp.getValue();
            Eat eat = DkhomeApplication.mApp.foodManager.dayEat.get(tsportData.time);
            dayburndata.add((int)(tsportData.gettotalCarl()+tsportData.baseCarl-eat.carl));
            xdaydata.add(tsportData.time);
        }
    }

    private void initOption3(){
        persentData = new ObjectData();
        ObjectData tooltip = new ObjectData();
        persentData.put("tooltip",tooltip);
        tooltip.put("trigger","item");
        ObjectData legend = new ObjectData();
        persentData.put("legend",legend);
        legend.put("top","%5");
        legend.put("left","center");
        ArrayData series = new ArrayData();
        persentData.put("series",series);
        ObjectData sdata = new ObjectData();
        series.add(sdata);
        sdata.put("name",mChartActivity.getString(R.string.chart_burn_type));
        sdata.put("type","pie");
        ArrayData radius = new ArrayData();
        sdata.put("radius",radius);
        radius.add("30%");
        radius.add("60%");
        ArrayData center = new ArrayData();
        sdata.put("center",center);
        center.add("50%");
        center.add("65%");
        sdata.put("avoidLabelOverlap",false);
        ObjectData itemStyle = new ObjectData();
        sdata.put("itemStyle",itemStyle);
        itemStyle.put("borderRadius",10);
        itemStyle.put("borderColor","#fff");
        itemStyle.put("borderWidth",2);
        ObjectData label = new ObjectData();
        sdata.put("label",label);
        label.put("show",false);
        label.put("position","center");
        ObjectData emphasis = new ObjectData();
        sdata.put("emphasis",emphasis);
        ObjectData label1 = new ObjectData();
        emphasis.put("label",label1);
        label1.put("show",false);
        label1.put("fontSize","40");
        label1.put("fontWeight","bold");
        ObjectData labelLine = new ObjectData();
        sdata.put("labelLine",labelLine);
        labelLine.put("show",false);
        ArrayData persent = new ArrayData();
        sdata.put("data",persent);
        elliptical = new ObjectData();
        elliptical.put("name",mChartActivity.getString(R.string.device_type_elliptical_machine));
        persent.add(elliptical);
        rowing = new ObjectData();
        rowing.put("name",mChartActivity.getString(R.string.device_type_rowing_machine));
        persent.add(rowing);
        exercise = new ObjectData();
        exercise.put("name",mChartActivity.getString(R.string.device_type_exercise_bike));
        persent.add(exercise);
        spinning = new ObjectData();
        spinning.put("name",mChartActivity.getString(R.string.device_type_spinning_bike));
        persent.add(spinning);
        base = new ObjectData();
        base.put("name",mChartActivity.getString(R.string.chart1_carl_unit_b1));
        persent.add(base);
        elliptical.put("value",DkhomeApplication.mApp.sportDataManager.getEllipticalToalCarl());
        rowing.put("value",DkhomeApplication.mApp.sportDataManager.getRowingToalCarl());
        exercise.put("value",DkhomeApplication.mApp.sportDataManager.getExerciseToalCarl());
        spinning.put("value",DkhomeApplication.mApp.sportDataManager.getSpinningToalCarl());
        base.put("value",DkhomeApplication.mApp.sportDataManager.getBToalCarl());
    }

    private void updataView() {
        String url = "javascript:createChart('orther'," + sportData1.toString() + ");";
        chart.loadUrl(url);
        String url2 = "javascript:createChart('orther'," + persentData.toString() + ");";
        chart2.loadUrl(url2);
        String url3 = "javascript:createChart('orther'," + goalData1.toString() + ");";
        chart3.loadUrl(url3);
    }



    private WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            // 当有新连接时使用当前的webview进行显示
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        // 开始加载网页时要做的工作
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setVisibility(View.INVISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
            if(url.equals("file:///android_asset/echart/myechart.html"))
            updataView();
        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }
    };

    public View.OnClickListener historyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            historytitle.setTextColor(Color.parseColor("#ff5e3a"));
            goalTitle.setTextColor(Color.parseColor("#333333"));
            noScrollViewPager.setCurrentItem(0);
        }
    };

    public View.OnClickListener goalListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            historytitle.setTextColor(Color.parseColor("#333333"));
            goalTitle.setTextColor(Color.parseColor("#ff5e3a"));
            noScrollViewPager.setCurrentItem(1);
        }
    };
}
