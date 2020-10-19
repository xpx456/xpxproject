package com.dk.dkphone.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.dk.dkphone.presenter.MoviPresenter;
import com.dk.dkphone.view.DkPadApplication;

import intersky.appbase.PadBaseActivity;

public class MoviActivity extends PadBaseActivity {

    public MoviPresenter mMoviPresenter = new MoviPresenter(this);
    public WebView video;
    public RelativeLayout root;
    public MoviActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMoviPresenter.Destroy();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DkPadApplication.mApp.appSetProtectTime(DkPadApplication.mApp.maxProtectSecond);
        return super.dispatchTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {

        //获得触摸的坐标
        float x = event.getX();
        float y = event.getY(); switch (event.getAction())
        {
            //触摸屏幕时刻
            case MotionEvent.ACTION_DOWN:

                break;
            //触摸并移动时刻
            case MotionEvent.ACTION_MOVE:

                break;
            //终止触摸时刻
            case MotionEvent.ACTION_UP:
                if(!(root.getX() < x && root.getX()+root.getWidth() > x
                        && root.getY() < y && root.getY()+root.getHeight() > y))
                {
                    finish();
                }
                break;
        }
        return true;
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

            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {


            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }
    };
}
