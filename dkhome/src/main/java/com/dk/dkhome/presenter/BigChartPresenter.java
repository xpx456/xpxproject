package com.dk.dkhome.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.dk.dkhome.R;
import com.dk.dkhome.handler.BigChartHandler;
import com.dk.dkhome.receiver.BigChartReceiver;
import com.dk.dkhome.view.activity.BigChartActivity;

import intersky.appbase.BaseReceiver;
import intersky.appbase.Presenter;

public class BigChartPresenter implements Presenter {

	public BigChartActivity mBigChartActivity;
	public WebView chart;
	public String optation = "";
	public String action = null;
	public BigChartHandler bigChartHandler;
	public BigChartPresenter(BigChartActivity mBigChartActivity) {
		this.mBigChartActivity = mBigChartActivity;
		bigChartHandler = new BigChartHandler(mBigChartActivity);
		action = mBigChartActivity.getIntent().getStringExtra("action");
		if(action != null)
		mBigChartActivity.setBaseReceiver(new BigChartReceiver(bigChartHandler,action));
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mBigChartActivity.setContentView(R.layout.activity_big_chart);
		ImageView back = mBigChartActivity.findViewById(R.id.back);
		back.setOnClickListener(mBigChartActivity.mBackListener);
		optation = mBigChartActivity.getIntent().getStringExtra("optation");
		chart = mBigChartActivity.findViewById(R.id.chart);
		chart.getSettings().setAllowFileAccess(true);
		chart.getSettings().setJavaScriptEnabled(true);
		chart.setWebViewClient(mWebViewClient);
		chart.setVisibility(View.VISIBLE);
		chart.setBackgroundColor(0); // 设置背景色
		chart.getBackground().setAlpha(0);
		chart.loadUrl("file:///android_asset/echart/myechart.html");
	}

	@Override
	public void Create() {
		// TODO Auto-generated method stub
		initView();
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
		
	}

	public void updataView() {
		String url1 = "javascript:createChart('orther'," + optation + ");";
		chart.loadUrl(url1);
	}

	public void updataView(Intent intent) {
		optation = intent.getStringExtra("optation");
		String url1 = "javascript:createChart('orther'," + optation + ");";
		chart.loadUrl(url1);
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
