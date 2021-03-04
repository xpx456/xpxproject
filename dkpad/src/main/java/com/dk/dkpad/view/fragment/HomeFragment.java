package com.dk.dkpad.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dkpad.R;
import com.dk.dkpad.handler.MainHandler;
import com.dk.dkpad.view.DkPadApplication;
import com.dk.dkpad.view.activity.MainActivity;

import intersky.apputils.AppUtils;
import intersky.apputils.UtilBitmap;
import intersky.apputils.UtilScreenCapture;
import intersky.echartoption.ArrayData;
import intersky.echartoption.FunctionData;
import intersky.echartoption.ObjectData;

public class HomeFragment extends BaseFragment {

    public MainActivity mMainActivity;
    public WebView chartShow;
    public RelativeLayout bg;
    public ImageView bg2;
    public TextView textViewvalud;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        bg = mView.findViewById(R.id.bg);
        bg2 = mView.findViewById(R.id.bg2);
        chartShow = mView.findViewById(R.id.chart);
        chartShow.getSettings().setAllowFileAccess(true);
        chartShow.getSettings().setJavaScriptEnabled(true);
        chartShow.setWebViewClient(mMainActivity.mWebViewClient);
        chartShow.setVisibility(View.VISIBLE);
        chartShow.setBackgroundColor(0); // 设置背景色
        chartShow.getBackground().setAlpha(0);
        chartShow.loadUrl("file:///android_asset/echart/myechart.html");
        textViewvalud = mView.findViewById(R.id.sportvalue);
        mMainActivity.mMainPresenter.mainHandler.sendEmptyMessageDelayed(MainHandler.INIT_HOME_CHART_VIEW,800);
        setbg();
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();
    }




    public void initView() {
        if(chartShow != null)
        {
            int total = DkPadApplication.mApp.testManager.test.hashopLeave.length()*DkPadApplication.OPTATION_ITEM_PER_SECOND;
            if(DkPadApplication.mApp.testManager.test.current/60/60 == 0)
            {
                int second = DkPadApplication.mApp.testManager.test.current%60;
                int min = DkPadApplication.mApp.testManager.test.current/60;

                int second1 = total%60;
                int min1 = total/60;

                String now = String.format("%02d:%02d",min,second);
                String to = String.format("%02d:%02d",min1,second1);
                textViewvalud.setText(now+"/"+to);
                mMainActivity.timevalue1.setText(now);
                mMainActivity.timevalue2.setText(to);
            }
            else{
                int second = DkPadApplication.mApp.testManager.test.current%60;
                int min = DkPadApplication.mApp.testManager.test.current/60%60;
                int hour = DkPadApplication.mApp.testManager.test.current/60/60;

                int second1 = total%60;
                int min1 = total/60%60;
                int hour1 = total/60/60;

                String now = String.format("%02d:%02d:%02d",hour,min,second);
                String to = String.format("%02d:%02d:%02d",hour1,min1,second1);
                textViewvalud.setText(now+"/"+to);
                mMainActivity.timevalue1.setText(now);
                mMainActivity.timevalue2.setText(to);
            }


            String url = "javascript:createChart('orther'," + mMainActivity.homeoption.toString() + ");";
            chartShow.loadUrl(url);
        }
    }

    public void setbg() {
        Bitmap bitmap = UtilScreenCapture.getDrawing(bg);
        bg2.setImageBitmap(bitmap);
        UtilBitmap.blurImageView(mMainActivity, bg2, 5, 0xaaffffff);
    }
}
