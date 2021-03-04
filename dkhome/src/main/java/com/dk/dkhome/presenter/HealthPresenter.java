package com.dk.dkhome.presenter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.User;
import com.dk.dkhome.entity.UserWeight;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.GoalActivity;
import com.dk.dkhome.view.activity.HealthActivity;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.RegisterActivity;

import java.util.Map;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Account;
import intersky.apputils.AppUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;


public class HealthPresenter implements Presenter {
    private static final int DECIMAL_DIGITS = 1;

    public HealthActivity mHealthActivity;
    public TextView btnFinish;
    public EditText tallValue;
    public EditText weightValue;
    public User account = new User();
    public WebView chart;
    public ObjectData weightOption;
    public ArrayData xdata;
    public ArrayData ydata;
    public HealthPresenter(HealthActivity mHealthActivity) {
        this.mHealthActivity = mHealthActivity;
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mHealthActivity.setContentView(R.layout.activity_health);
        TextView title = mHealthActivity.findViewById(R.id.title);
        ImageView back = mHealthActivity.findViewById(R.id.back);
        back.setOnClickListener(mHealthActivity.mBackListener);
        chart = mHealthActivity.findViewById(R.id.chart);
        btnFinish = mHealthActivity.findViewById(R.id.btn_finish);
        tallValue = mHealthActivity.findViewById(R.id.tall_value);
        weightValue = mHealthActivity.findViewById(R.id.weight_value);
        if(mHealthActivity.getIntent().getBooleanExtra(RegisterActivity.IS_REGISTER,false) == true)
        {
            account = mHealthActivity.getIntent().getParcelableExtra("user");
            title.setText(mHealthActivity.getString(R.string.register_title));
            chart.destroy();
        }
        else
        {
            title.setText(mHealthActivity.getString(R.string.main_left_health));
            btnFinish.setText(mHealthActivity.getString(R.string.button_word_finish));
            initOption();
            chart.getSettings().setAllowFileAccess(true);
            chart.getSettings().setJavaScriptEnabled(true);
            chart.setWebViewClient(mWebViewClient);
            chart.setVisibility(View.VISIBLE);
            chart.setBackgroundColor(Color.parseColor("#ffffff")); // 设置背景色
            chart.getBackground().setAlpha(0);
            chart.loadUrl("file:///android_asset/echart/myechart.html");
            account.lastweight = DkhomeApplication.mApp.mAccount.lastweight;
            account.tall = DkhomeApplication.mApp.mAccount.tall;
            tallValue.setText(String.valueOf(DkhomeApplication.mApp.mAccount.tall));
            weightValue.setText(String.valueOf(DkhomeApplication.mApp.mAccount.lastweight));
        }
        btnFinish.setOnClickListener(finishListener);
        tallValue.addTextChangedListener(tallWatcher);
        weightValue.addTextChangedListener(weightWatcher);
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
        if(chart != null)
        chart.destroy();
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    private void initOption() {
        weightOption = new ObjectData();
        ObjectData xAxis = new ObjectData();
        weightOption.put("xAxis",xAxis);
        xAxis.put("type","category");
        xdata = new ArrayData();
        xAxis.put("data",xdata);
        ObjectData yAxis = new ObjectData();
        weightOption.put("yAxis",yAxis);
        yAxis.put("type","value");
        ArrayData series = new ArrayData();
        weightOption.put("series",series);
        ObjectData sdata = new ObjectData();
        series.add(sdata);
        ydata = new ArrayData();
        sdata.put("name",mHealthActivity.getString(R.string.health_chart_name));
        sdata.put("data",ydata);
        sdata.put("type","line");
        sdata.put("smooth",true);
        ObjectData markPoint = new ObjectData();
        sdata.put("markPoint",markPoint);
        ArrayData mark = new ArrayData();
        markPoint.put("data",mark);
        ObjectData max = new ObjectData();
        mark.add(max);
        max.put("type","max");
        max.put("name",mHealthActivity.getString(R.string.health_chart_max));
        ObjectData min = new ObjectData();
        mark.add(min);
        min.put("type",mHealthActivity.getString(R.string.health_chart_min));
        min.put("name","");

        if(DkhomeApplication.mApp.weightManager.dayWeight.size() > 15)
        {
            int n = 1;
            for(Map.Entry<String,UserWeight> temp : DkhomeApplication.mApp.weightManager.dayWeight.entrySet())
            {
                UserWeight userWeight = temp.getValue();
                xdata.add(userWeight.date);
                ydata.add(Double.valueOf(userWeight.weight));
                n++;
                if(n > 15)
                {
                    break;
                }
            }
        }
        else
        {
            for(Map.Entry<String,UserWeight> temp : DkhomeApplication.mApp.weightManager.dayWeight.entrySet())
            {
                UserWeight userWeight = temp.getValue();
                xdata.add(userWeight.date);
                ydata.add(Double.valueOf(userWeight.weight));
            }
        }

    }

    private void updataView() {
        String url = "javascript:createChart('orther'," + weightOption.toString() + ");";
        chart.loadUrl(url);
    }

    private View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(DkhomeApplication.mApp.mAccount.tall == 0 && tallValue.getText().toString().length() == 0)
            {
                AppUtils.showMessage(mHealthActivity,mHealthActivity.getString(R.string.register_error_tall));
                return;
            }
            if(DkhomeApplication.mApp.mAccount.lastweight == 0 && weightValue.getText().toString().length() == 0)
            {
                AppUtils.showMessage(mHealthActivity,mHealthActivity.getString(R.string.register_error_weight));
                return;
            }
            if(tallValue.getText().toString().length() > 0)
            {
                account.tall = Integer.valueOf(tallValue.getText().toString());
            }
            if(weightValue.getText().toString().length() > 0)
            {
                account.lastweight = Integer.valueOf(weightValue.getText().toString());
            }


            if(mHealthActivity.getIntent().getBooleanExtra(RegisterActivity.IS_REGISTER,false) == true)
            {
                Intent intent = new Intent(mHealthActivity, GoalActivity.class);
                intent.putExtra("user",account);
                intent.putExtra(RegisterActivity.IS_REGISTER,true);
                mHealthActivity.startActivity(intent);
            }
            else
            {
                DkhomeApplication.mApp.mAccount.tall = account.tall;
                DkhomeApplication.mApp.mAccount.lastweight = account.lastweight;
                DkhomeApplication.mApp.weightManager.addWeight(account.lastweight);
                DkhomeApplication.mApp.setHealth();
                Intent intent = new Intent(MainActivity.ACTION_UPDATA_DAIRY);
                mHealthActivity.sendBroadcast(intent);
                mHealthActivity.finish();
            }


        }
    };


    private TextWatcher weightWatcher = new TextWatcher()
    {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(weightValue.getText().toString().startsWith("0"))
            {
                weightValue.setText(weightValue.getText().toString().substring(1,weightValue.getText().toString().length()));
            }
        }

    };


    private TextWatcher tallWatcher = new TextWatcher()
    {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if(tallValue.getText().toString().startsWith("0"))
            {
                tallValue.setText(tallValue.getText().toString().substring(1,tallValue.getText().toString().length()));
            }
        }

    };


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
}
