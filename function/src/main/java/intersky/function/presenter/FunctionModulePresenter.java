package intersky.function.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.function.ChartUtils;
import intersky.function.R;
import intersky.function.asks.FunAsks;
import intersky.function.receiver.FunctionModuleReceiver;
import intersky.function.receiver.entity.ChartData;
import intersky.function.receiver.entity.Function;
import intersky.function.receiver.entity.LableData;
import intersky.function.handler.FunctionModuleHandler;
import intersky.function.view.activity.ChartActivity;
import intersky.function.view.activity.FunctionModuleActivity;
import intersky.function.view.activity.GridActivity;
import intersky.function.view.activity.GridDetialActivity;
import intersky.function.view.activity.LabelActivity;
import intersky.function.view.adapter.LabelListAdapter;
import intersky.mywidget.Data;
import intersky.mywidget.GrideData;
import intersky.mywidget.TableCloumArts;
import intersky.mywidget.TableItem;
import intersky.mywidget.TableView;
import intersky.scan.ScanUtils;
import xpx.com.toolbar.utils.ToolBarHelper;
import xpx.map.MapUtils;

/**
 * Created by xpx on 2017/8/18.
 */

public class FunctionModulePresenter implements Presenter {

    public FunctionModuleHandler mFunctionModuleHandler;
    public FunctionModuleActivity mFunctionModuleActivity;
    public FunctionModulePresenter(FunctionModuleActivity mFunctionModuleActivity)
    {
        this.mFunctionModuleActivity = mFunctionModuleActivity;
        this.mFunctionModuleHandler = new FunctionModuleHandler(mFunctionModuleActivity);
        mFunctionModuleActivity.setBaseReceiver(new FunctionModuleReceiver(mFunctionModuleHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mFunctionModuleActivity.setContentView(R.layout.activity_functionmodule);
        mFunctionModuleActivity.flagFillBack = false;
        mFunctionModuleActivity.shade = mFunctionModuleActivity.findViewById(R.id.shade1);
        mFunctionModuleActivity.searchViewlayer = mFunctionModuleActivity.findViewById(R.id.searchViewlayer);
        mFunctionModuleActivity.mSearchScan =  mFunctionModuleActivity.findViewById(R.id.scan);
        mFunctionModuleActivity.mSearchView =  mFunctionModuleActivity.findViewById(R.id.searchView);
//        mFunctionModuleActivity.searchText =  mFunctionModuleActivity.findViewById(R.id.searchText);
        mFunctionModuleActivity.mHorizontalScrollView = mFunctionModuleActivity.findViewById(R.id.hScrollView1);
        mFunctionModuleActivity.mClassManager = mFunctionModuleActivity.findViewById(R.id.classes);
        mFunctionModuleActivity.mChart = mFunctionModuleActivity.findViewById(R.id.chart);
        mFunctionModuleActivity.mLable = mFunctionModuleActivity.findViewById(R.id.listView);
        mFunctionModuleActivity.mTable = mFunctionModuleActivity.findViewById(R.id.table);
        mFunctionModuleActivity.mshade =  mFunctionModuleActivity.findViewById(R.id.shade);
        mFunctionModuleActivity.mFunction = mFunctionModuleActivity.getIntent().getParcelableExtra("function");
        mFunctionModuleActivity.mChart.getSettings().setAllowFileAccess(true);
        // 开启脚本支持
        mFunctionModuleActivity.mChart.getSettings().setJavaScriptEnabled(true);
        //mFunctionModuleActivity.mChart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mFunctionModuleActivity.mChart.setWebViewClient(mWebViewClient);
//        File file = new File("file:///android_asset/echart/myechart.html");
//        if(file.exists())
//        {
//            mFunctionModuleActivity.mChart.loadUrl("file:///android_asset/echart/myechart.html");
//        }
        mFunctionModuleActivity.mChart.loadUrl("file:///android_asset/echart/myechart.html");
        mFunctionModuleActivity.mTable.setPullUpDownListner(mPullUpDownListner);
        mFunctionModuleActivity.mSearchView.setOnEditorActionListener(mFunctionModuleActivity.mOnEditorActionListener);
        mFunctionModuleActivity.mSearchScan.setOnClickListener(mFunctionModuleActivity.mScanListener);
        ToolBarHelper.setTitle(mFunctionModuleActivity.mActionBar, mFunctionModuleActivity.mFunction.mCaption);
//        if (mFunctionModuleActivity.searchHead.length() == 0) {
//            mFunctionModuleActivity.waitDialog.show();
//            FunAsks.getSearchHead(mFunctionModuleActivity,mFunctionModuleHandler,mFunctionModuleActivity.mFunction);
//        }
        mFunctionModuleActivity.waitDialog.show();
        FunAsks.getBoardData(mFunctionModuleActivity,mFunctionModuleActivity.mFunctionModulePresenter.mFunctionModuleHandler,mFunctionModuleActivity.mFunction,1,"");
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
            mFunctionModuleActivity.waitDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            mFunctionModuleActivity.waitDialog.hide();
            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mFunctionModuleActivity.waitDialog.hide();
        }
    };

    public void ininDraw() {
        View popupWindowView = LayoutInflater.from(mFunctionModuleActivity).inflate(R.layout.shade_view, null);
        mFunctionModuleActivity.popupWindow2 = new PopupWindow(popupWindowView, (int) (240 * mFunctionModuleActivity.mBasePresenter.mScreenDefine.density), RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mFunctionModuleActivity.mSearchlayer = (LinearLayout) popupWindowView.findViewById(R.id.right_drawer);
        mFunctionModuleActivity.popupWindow2.setAnimationStyle(R.style.PopupAnimation2);
        ColorDrawable dw = new ColorDrawable(0x0000000);
        mFunctionModuleActivity.popupWindow2.setBackgroundDrawable(dw);
        mFunctionModuleActivity.popupWindow2.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mFunctionModuleActivity.mSearchbtn = (TextView) popupWindowView.findViewById(R.id.search_btn);
        mFunctionModuleActivity.mSearchbtn.setOnClickListener(mFunctionModuleActivity.mSearchListener);
        mFunctionModuleActivity.mCleanbtn = (TextView) popupWindowView.findViewById(R.id.clean_btn);
        mFunctionModuleActivity.mCleanbtn.setOnClickListener(mFunctionModuleActivity.mCleanListener);
        mFunctionModuleActivity.mshade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFunctionModuleActivity.popupWindow3.isShowing()) {
                    mFunctionModuleActivity.popupWindow3.dismiss();
                } else if (mFunctionModuleActivity.popupWindow2.isShowing()) {
                    mFunctionModuleActivity.popupWindow2.dismiss();
                }
            }
        });
        mFunctionModuleActivity.popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mFunctionModuleActivity.mshade.setVisibility(View.INVISIBLE);
            }
        });
        popupWindowView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //do something...
//                        doclose();
                return false;
            }
        });
    }

    public void inidSerias() {
        if (mFunctionModuleActivity.mFunData.mKeys.size() <= 1) {
            mFunctionModuleActivity.mHorizontalScrollView.setVisibility(View.GONE);
            if (mFunctionModuleActivity.mFunData.mKeys.size() == 1) {
                mFunctionModuleActivity.mFunData.showKey = mFunctionModuleActivity.mFunData.mKeys.get(0);
                initData( mFunctionModuleActivity.mFunData.funDatas.get(mFunctionModuleActivity.mFunData.mKeys.get(0)));
            }
        } else {
            mFunctionModuleActivity.mHorizontalScrollView.setVisibility(View.VISIBLE);
            LayoutInflater mInflater = (LayoutInflater) mFunctionModuleActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mFunctionModuleActivity.mClassManager.removeAllViews();
            for (int i = 0; i < mFunctionModuleActivity.mFunData.mKeys.size(); i++) {
                View mView = mInflater.inflate(R.layout.taskbuttomtab, null);
                TextView mTextViewa = (TextView) mView.findViewById(R.id.tebtext1);
                TextView mTextViewb = (TextView) mView.findViewById(R.id.tebtext2);
                mView.setTag(mFunctionModuleActivity.mFunData.mKeys.get(i));
                mTextViewa.setText("  " + mFunctionModuleActivity.mFunData.mKeys.get(i) + "  ");
                mTextViewb.setText("  " + mFunctionModuleActivity.mFunData.mKeys.get(i) + "  ");
                mView.setOnClickListener(classTabListener);
                mFunctionModuleActivity.mClassManager.addView(mView);
                if (i == 0) {
                    mFunctionModuleActivity.mFunData.showTab = mView;
                    mFunctionModuleActivity.mFunData.showKey = mFunctionModuleActivity.mFunData.showTab.getTag().toString();
                    mTextViewa.setVisibility(View.VISIBLE);
                    mTextViewb.setVisibility(View.INVISIBLE);
                }
            }
            initData(mFunctionModuleActivity.mFunData.funDatas.get(mFunctionModuleActivity.mFunData.showTab.getTag().toString()));
        }
    }

    public void updataView() {
        mFunctionModuleActivity.mTable.updata();
    }

    public View.OnClickListener classTabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView mTextViewa = (TextView) v.findViewById(R.id.tebtext1);
            TextView mTextViewb = (TextView) v.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.VISIBLE);
            mTextViewb.setVisibility(View.INVISIBLE);
            mTextViewa = (TextView) mFunctionModuleActivity.mFunData.showTab.findViewById(R.id.tebtext1);
            mTextViewb = (TextView) mFunctionModuleActivity.mFunData.showTab.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.INVISIBLE);
            mTextViewb.setVisibility(View.VISIBLE);
            mFunctionModuleActivity.mFunData.showTab = v;
            mFunctionModuleActivity.mFunData.showKey = mFunctionModuleActivity.mFunData.showTab.getTag().toString();
            initData( mFunctionModuleActivity.mFunData.funDatas.get(mFunctionModuleActivity.mFunData.showTab.getTag().toString()));
        }
    };

    public void initData(Data data)
    {
        if(data.dataType.equals(Data.FUN_DATA_TYPE_GRID))
        {
            mFunctionModuleActivity.mLable.setVisibility(View.INVISIBLE);
            mFunctionModuleActivity.mTable.setVisibility(View.VISIBLE);
            mFunctionModuleActivity.mChart.setVisibility(View.INVISIBLE);
            mFunctionModuleActivity.mTable.setDoClickListener(mDoClickListener);
            mFunctionModuleActivity.mTable.init((GrideData) data);
            if(((GrideData) data).showSearch == true)
            {
                mFunctionModuleActivity.searchViewlayer.setVisibility(View.VISIBLE);
            }
            else
            {
                mFunctionModuleActivity.searchViewlayer.setVisibility(View.GONE);
            }
            if(((GrideData) data).showBoardSearch == true)
            {
                mFunctionModuleActivity.mSearchScan.setVisibility(View.VISIBLE);
            }
            else
            {
                mFunctionModuleActivity.mSearchScan.setVisibility(View.GONE);
            }
        }
        else if(data.dataType.equals(Data.FUN_DATA_TYPE_LABLE))
        {
            mFunctionModuleActivity.mLable.setVisibility(View.VISIBLE);
            mFunctionModuleActivity.mTable.setVisibility(View.INVISIBLE);
            mFunctionModuleActivity.mChart.setVisibility(View.INVISIBLE);
            LableData mLableData = (LableData) data;
            LabelListAdapter mLabelListAdapter = new LabelListAdapter(mFunctionModuleActivity,mLableData.lableDataItems);
            mFunctionModuleActivity.mLable.setAdapter(mLabelListAdapter);
            mFunctionModuleActivity.searchViewlayer.setVisibility(View.GONE);
        }
        else
        {
            mFunctionModuleActivity.mLable.setVisibility(View.INVISIBLE);
            mFunctionModuleActivity.mTable.setVisibility(View.INVISIBLE);
            mFunctionModuleActivity.mChart.setVisibility(View.VISIBLE);
            mFunctionModuleActivity.searchViewlayer.setVisibility(View.GONE);
            if(data.dataType.equals(Data.FUN_DATA_TYPE_CHART_COLUMNS))
            {
                ChartUtils.loadColumnChart((ChartData) data,mFunctionModuleActivity.mChart);
            }
            else if(data.dataType.equals(Data.FUN_DATA_TYPE_CHART_COLUMN) || data.dataType.equals(Data.FUN_DATA_TYPE_CHART_BAR))
            {
                ChartUtils.loadBarChart((ChartData) data,mFunctionModuleActivity.mChart);
            }
            else if(data.dataType.equals(Data.FUN_DATA_TYPE_CHART_PIE))
            {
                ChartUtils.loadPieChart((ChartData) data,mFunctionModuleActivity.mChart);
            }
            else if(data.dataType.equals(Data.FUN_DATA_TYPE_CHART_FUNNEL))
            {
                ChartUtils.loadFunnelChart((ChartData) data,mFunctionModuleActivity.mChart);
            }
            else if(data.dataType.equals(Data.FUN_DATA_TYPE_CHART_LINE))
            {
                ChartUtils.loadLineChart((ChartData) data,mFunctionModuleActivity.mChart);
            }
        }

    }

    public TableView.PullUpDownListner mPullUpDownListner = new TableView.PullUpDownListner() {

        @Override
        public void doPullUp() {
            GrideData grideData = (GrideData) mFunctionModuleActivity.mFunData.funDatas.get(mFunctionModuleActivity.mFunData.showKey);
            if(grideData.isall == false)
            {
                mFunctionModuleActivity.waitDialog.show();
                FunAsks.getBoardData(mFunctionModuleActivity,mFunctionModuleHandler,mFunctionModuleActivity.mFunction,mFunctionModuleActivity.mFunData.page,mFunctionModuleActivity.mFunData.keyWord);
            }
            else{
                mFunctionModuleActivity.mTable.swipeRefreshLayout.setLoading(false);
            }

        }

        @Override
        public void doPullDown() {
            mFunctionModuleActivity.mFunData.upDate();
            mFunctionModuleActivity.waitDialog.show();
            FunAsks.getBoardData(mFunctionModuleActivity,mFunctionModuleHandler,mFunctionModuleActivity.mFunction,mFunctionModuleActivity.mFunData.page,mFunctionModuleActivity.mFunData.keyWord);
        }
    };

    public TableView.DoClickListener mDoClickListener = new TableView.DoClickListener() {

        @Override
        public void doClickListener(TableCloumArts mTableCloumArts, TableItem mTableItem, int line, TextView view) {
            GrideData grideData;
            if(mFunctionModuleActivity.mFunData.mKeys.size() == 1)
            {
                grideData = (GrideData) mFunctionModuleActivity.mFunData.funDatas.get(mFunctionModuleActivity.mFunData.mKeys.get(0));
            }
            else
            {
                grideData = (GrideData) mFunctionModuleActivity.mFunData.funDatas.get(mFunctionModuleActivity.mFunData.showTab.getTag());
            }

            if(mTableCloumArts.dataType.toLowerCase().equals("email") || mTableCloumArts.dataType.toLowerCase().equals("phone") ||
                    mTableCloumArts.dataType.toLowerCase().equals("city"))
            {
                simpleoper(mTableCloumArts,mTableItem,view);
            }
            else
            {
                if(grideData != null)
                {
                    if(grideData.isLineClick == false)
                    {
                        Function function = new Function();
                        function.mName = grideData.nextAppName;
                        function.mCaption = grideData.nextAppCaption;
                        function.typeName = grideData.nextAppType;
                        function.isSecond = true;
                        function.canEdit = grideData.canEdit;
                        function.mColName = mTableCloumArts.mFiledName;
                        if(grideData.tableLeft.size() == line)
                        function.mRowName = grideData.tableLeft.get(line-1).mTitle;
                        else
                            return;
                        function.mCellValue = mTableItem.mTitle;

                        if(mFunctionModuleActivity.mFunData.mKeys.size() == 1)
                        {
                            function.series = mFunctionModuleActivity.mFunData.mKeys.get(0);
                        }
                        else if(mFunctionModuleActivity.mFunData.mKeys.size() > 1)
                        {
                            function.series = (String) mFunctionModuleActivity.mFunData.showTab.getTag();
                        }
                        Intent intent = null;
                        if (function.typeName.equals(Function.COLUMN) || function.typeName.equals(Function.COLUMNS) || function.typeName.equals(Function.FUNNEL)
                                || function.typeName.equals(Function.PIE) || function.typeName.equals(Function.BAR)) {
                            intent = new Intent(mFunctionModuleActivity, ChartActivity.class);

                        } else if (function.typeName.equals(Function.LABEL)) {
                            intent = new Intent(mFunctionModuleActivity, LabelActivity.class);

                        } else if (function.typeName.equals(Function.GRID)) {
                            intent = new Intent(mFunctionModuleActivity, GridActivity.class);
                        }
                        if(intent != null)
                        {
                            intent.putExtra("function",function);
                            intent.putExtra("edit",false);
                            mFunctionModuleActivity.startActivity(intent);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(mFunctionModuleActivity, GridDetialActivity.class);
                        Function function = new Function();
                        function.mCaption = mFunctionModuleActivity.mFunction.mCaption;
                        function.mName = mFunctionModuleActivity.mFunction.mName;
                        function.modulflag = grideData.modul;
                        function.canEdit = grideData.canEdit;
                        if(mFunctionModuleActivity.mFunData.mKeys.size() == 1)
                        {
                            function.series = mFunctionModuleActivity.mFunData.mKeys.get(0);
                        }
                        else if(mFunctionModuleActivity.mFunData.mKeys.size() > 1)
                        {
                            function.series = (String) mFunctionModuleActivity.mFunData.showTab.getTag();
                        }
                        if(grideData.dataKeys.size() >= line -1)
                        {
                            function.mRecordId = grideData.dataKeys.get(line-1);
                            if(grideData.tableContent.size() >= line-1)
                            function.mCellValue = grideData.tableContent.get(grideData.dataKeys.get(line-1));
                        }
                        else
                        {
                            return;
                        }
                        function.mColName = grideData.head;
                        intent.putExtra("function",function);
                        intent.putExtra("edit",false);
                        mFunctionModuleActivity.startActivity(intent);
                    }
                }

            }
        }
    };

    public void simpleoper(TableCloumArts mTableCloumArts, TableItem mTableItem,TextView view) {
        if(mTableCloumArts.dataType.equals("email"))
        {
            //Bus.callData(mFunctionModuleActivity,"mail/senMailAddress",mTableItem.mTitle);
        }
        else if(mTableCloumArts.dataType.equals("phone"))
        {
            AppUtils.checkTel(mFunctionModuleActivity,mTableItem.mTitle);
        }
        else if(mTableCloumArts.dataType.equals("city"))
        {
            if(mTableItem.mTitle.length() == 0)
            {
                AppUtils.showMessage(mFunctionModuleActivity,mFunctionModuleActivity.getString(R.string.address_empty));
            }
            else
            {
                MapUtils.showMap(mFunctionModuleActivity,mFunctionModuleActivity.shade
                        ,mFunctionModuleActivity.findViewById(R.id.activity_grid)
                        ,mTableItem.mTitle,mFunctionModuleActivity.getString(R.string.app_name));
            }
        }

    }

    public void doSearch() {
        mFunctionModuleActivity.mFunData.keyWord = mFunctionModuleActivity.mSearchView.getText().toString();
        mFunctionModuleActivity.mFunData.upDate();
        mFunctionModuleActivity.waitDialog.show();
        FunAsks.getBoardData(mFunctionModuleActivity,mFunctionModuleHandler,mFunctionModuleActivity.mFunction,mFunctionModuleActivity.mFunData.page,mFunctionModuleActivity.mFunData.keyWord);
    }

    public void scan() {

        ScanUtils.getInstance().checkStartScan(mFunctionModuleActivity,"");


    }

}
