package com.dk.dkphone.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dkphone.R;
import com.dk.dkphone.handler.MainHandler;
import com.dk.dkphone.view.DkPhoneApplication;
import com.dk.dkphone.view.activity.MainActivity;

import java.math.BigDecimal;

import intersky.apputils.UtilBitmap;
import intersky.apputils.UtilScreenCapture;

public class DetialSportFragment extends BaseFragment {


    public MainActivity mMainActivity;
    public WebView chartShow;
    public TextView textViewvalud;
    public TextView textViewdisvalud;
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
        textViewvalud = mView.findViewById(R.id.sportvalue);
        textViewdisvalud = mView.findViewById(R.id.sportdisvalue);
        mMainActivity.mMainPresenter.mainHandler.sendEmptyMessageDelayed(MainHandler.INIT_SPORT_CHART_VIEW,800);
        setbg();
        initView();
        return mView;
    }




    public void initView() {
        if(chartShow != null)
        {
            //int total = DkPadApplication.mApp.testManager.test.hashopLeave.length()*DkPadApplication.OPTATION_ITEM_PER_SECOND;
            if(DkPhoneApplication.mApp.testManager.test.current/60/60 == 0)
            {
                int second = DkPhoneApplication.mApp.testManager.test.current%60;
                int min = DkPhoneApplication.mApp.testManager.test.current/60;

//                int second1 = total%60;
//                int min1 = total/60;

                String now = String.format("%02d:%02d",min,second);
//                String to = String.format("%02d:%02d",min1,second1);
//                textViewvalud.setText(now+"/"+to);
                textViewvalud.setText(now);
                mMainActivity.timevalue1.setText(now);
//                mMainActivity.timevalue2.setText(to);
            }
            else{
                int second = DkPhoneApplication.mApp.testManager.test.current%60;
                int min = DkPhoneApplication.mApp.testManager.test.current/60%60;
                int hour = DkPhoneApplication.mApp.testManager.test.current/60/60;

//                int second1 = total%60;
//                int min1 = total/60%60;
//                int hour1 = total/60/60;

                String now = String.format("%02d:%02d:%02d",hour,min,second);
//                String to = String.format("%02d:%02d:%02d",hour1,min1,second1);
//                textViewvalud.setText(now+"/"+to);
                textViewvalud.setText(now);
                mMainActivity.timevalue1.setText(now);
//                mMainActivity.timevalue2.setText(to);
            }
            BigDecimal bd = new BigDecimal(DkPhoneApplication.mApp.testManager.test.distence/1000);

            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            textViewdisvalud.setText(String.valueOf(bd)+"km");
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
