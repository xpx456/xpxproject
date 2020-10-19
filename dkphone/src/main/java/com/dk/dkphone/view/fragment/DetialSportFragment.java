package com.dk.dkphone.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dk.dkphone.R;
import com.dk.dkphone.handler.MainHandler;
import com.dk.dkphone.view.activity.MainActivity;

import intersky.apputils.UtilBitmap;
import intersky.apputils.UtilScreenCapture;

public class DetialSportFragment extends BaseFragment {


    public MainActivity mMainActivity;
    public WebView chartShow;

    public RelativeLayout bg;
    public ImageView bg2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_sport, container, false);
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
        mMainActivity.mMainPresenter.mainHandler.sendEmptyMessageDelayed(MainHandler.INIT_SPORT_CHART_VIEW,800);
        setbg();
        initView();
        return mView;
    }




    public void initView() {
        if(chartShow != null)
        {
            String url = "javascript:createChart('orther'," + mMainActivity.sportoption.toString() + ");";
            chartShow.loadUrl(url);
        }
    }

    public void setbg() {
        Bitmap bitmap = UtilScreenCapture.getDrawing(bg);
        bg2.setImageBitmap(bitmap);
        UtilBitmap.blurImageView(mMainActivity, bg2, 5, 0xaaffffff);
    }
}
