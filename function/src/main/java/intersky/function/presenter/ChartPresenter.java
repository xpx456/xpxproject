package intersky.function.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.function.ChartUtils;
import intersky.function.R;
import intersky.function.asks.FunAsks;
import intersky.function.entity.ChartData;
import intersky.function.entity.Function;
import intersky.function.handler.ChartHandler;
import intersky.function.view.activity.ChartActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class ChartPresenter implements Presenter {

    public ChartHandler mChartHandler;
    public ChartActivity mChartActivity;

    public ChartPresenter(ChartActivity mChartActivity) {
        this.mChartActivity = mChartActivity;
        this.mChartHandler = new ChartHandler(mChartActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mChartActivity.setContentView(R.layout.activity_chart);

        TextView title = mChartActivity.findViewById(R.id.title);
        ImageView back = mChartActivity.findViewById(R.id.back1);
        back.setOnClickListener(mChartActivity.mBackListener);

        mChartActivity.chartShow = mChartActivity.findViewById(R.id.funnelChart);
        mChartActivity.mHorizontalScrollView = mChartActivity.findViewById(R.id.scrollView1);
        mChartActivity.mClassManager = mChartActivity.findViewById(R.id.classesBar);
        mChartActivity.mFunction = mChartActivity.getIntent().getParcelableExtra("function");
        title.setText(mChartActivity.mFunction.mCaption);
        mChartActivity.mFunData.type = mChartActivity.mFunction.typeName;
        mChartActivity.chartShow.setWebViewClient(mWebViewClient);
        mChartActivity.chartShow.loadUrl("file:///android_asset/echart/myechart.html");
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
            mChartActivity.waitDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            mChartActivity.waitDialog.hide();
            if (mChartActivity.isFirst == false) {
                mChartActivity.isFirst = true;
                mChartActivity.waitDialog.show();
                FunAsks.getChartBoardData(mChartActivity, mChartHandler, mChartActivity.mFunction);
            }

            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mChartActivity.waitDialog.hide();
        }
    };

    public void inidSerias() {
        if (mChartActivity.mFunData.mKeys.size() <= 1) {
            mChartActivity.mHorizontalScrollView.setVisibility(View.GONE);
            if (mChartActivity.mFunData.mKeys.size() == 1) {

                initData((ChartData) mChartActivity.mFunData.funDatas.get(mChartActivity.mFunData.mKeys.get(0)));
            }
        } else {
            mChartActivity.mHorizontalScrollView.setVisibility(View.VISIBLE);
            LayoutInflater mInflater = (LayoutInflater) mChartActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mChartActivity.mClassManager.removeAllViews();
            for (int i = 0; i < mChartActivity.mFunData.mKeys.size(); i++) {
                View mView = mInflater.inflate(R.layout.taskbuttomtab, null);
                TextView mTextViewa = (TextView) mView.findViewById(R.id.tebtext1);
                TextView mTextViewb = (TextView) mView.findViewById(R.id.tebtext2);
                mView.setTag(mChartActivity.mFunData.mKeys.get(i));
                mTextViewa.setText("  " + mChartActivity.mFunData.mKeys.get(i) + "  ");
                mTextViewb.setText("  " + mChartActivity.mFunData.mKeys.get(i) + "  ");
                mView.setOnClickListener(classTabListener);
                mChartActivity.mClassManager.addView(mView);
                if (i == 0) {
                    mChartActivity.mFunData.showTab = mView;
                    mTextViewa.setVisibility(View.VISIBLE);
                    mTextViewb.setVisibility(View.INVISIBLE);
                }
            }
            initData((ChartData) mChartActivity.mFunData.funDatas.get(mChartActivity.mFunData.showTab.getTag().toString()));
        }
    }

    public void initData(ChartData mChartData) {
        if (mChartActivity.mFunData.type.equals(Function.COLUMNS)) {
            ChartUtils.loadColumnChart(mChartData,mChartActivity.chartShow);
        } else if (mChartActivity.mFunData.type.equals(Function.BAR)) {
            ChartUtils.loadBarChart(mChartData,mChartActivity.chartShow);
        } else if (mChartActivity.mFunData.type.equals(Function.FUNNEL)) {
            ChartUtils.loadFunnelChart(mChartData,mChartActivity.chartShow);
        } else if (mChartActivity.mFunData.type.equals(Function.PIE)) {
            ChartUtils.loadPieChart(mChartData,mChartActivity.chartShow);
        } else if (mChartActivity.mFunData.type.equals(Function.LINE)) {
            ChartUtils.loadLineChart(mChartData,mChartActivity.chartShow);
        }
    }

    public View.OnClickListener classTabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView mTextViewa = (TextView) v.findViewById(R.id.tebtext1);
            TextView mTextViewb = (TextView) v.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.VISIBLE);
            mTextViewb.setVisibility(View.INVISIBLE);
            mTextViewa = (TextView) mChartActivity.mFunData.showTab.findViewById(R.id.tebtext1);
            mTextViewb = (TextView) mChartActivity.mFunData.showTab.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.INVISIBLE);
            mTextViewb.setVisibility(View.VISIBLE);
            mChartActivity.mFunData.showTab = v;
            initData((ChartData) mChartActivity.mFunData.funDatas.get(mChartActivity.mFunData.showTab.getTag().toString()));
        }
    };



}
