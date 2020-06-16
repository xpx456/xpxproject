package intersky.function.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.URISyntaxException;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.function.R;
import intersky.function.asks.FunAsks;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.prase.FunPrase;
import intersky.function.prase.FunctionPrase;
import intersky.function.prase.WorkFlowPrase;
import intersky.function.receiver.GrideDetialReceiver;
import intersky.function.receiver.GrideReceiver;
import intersky.function.receiver.entity.Function;
import intersky.function.handler.GridHandler;
import intersky.function.view.activity.ChartActivity;
import intersky.function.view.activity.GridActivity;
import intersky.function.view.activity.GridDetialActivity;
import intersky.function.view.activity.LabelActivity;
import intersky.mywidget.GrideData;
import intersky.mywidget.TableCloumArts;
import intersky.mywidget.TableItem;
import intersky.mywidget.TableView;
import xpx.com.toolbar.utils.ToolBarHelper;
import xpx.map.MapUtils;

/**
 * Created by xpx on 2017/8/18.
 */

public class GridPresenter implements Presenter {

    public GridHandler mGridHandler;
    public GridActivity mGridActivity;
    public GridPresenter(GridActivity mGridActivity)
    {
        this.mGridActivity = mGridActivity;
        this.mGridHandler = new GridHandler(mGridActivity);
        mGridActivity.setBaseReceiver(new GrideReceiver(mGridHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mGridActivity.setContentView(R.layout.activity_grid);
        mGridActivity.mTable = mGridActivity.findViewById(R.id.table);
        mGridActivity.shade = mGridActivity.findViewById(R.id.shade);
        mGridActivity.mHorizontalScrollView = mGridActivity.findViewById(R.id.hScrollView1);
        mGridActivity.mClassManager = mGridActivity.findViewById(R.id.classes);
        mGridActivity.mActions = mGridActivity.findViewById(R.id.actions);
        mGridActivity.mSearchView = mGridActivity.findViewById(R.id.searchViewlayer);
        mGridActivity.mSearchView.mSetOnSearchListener(mGridActivity.mOnSearchActionListener);
        mGridActivity.mFunction = mGridActivity.getIntent().getParcelableExtra("function");
        mGridActivity.mTable.setPullUpDownListner(mPullUpDownListner);
        mGridActivity.mTable.setDoClickListener(mDoClickListener);
        mGridActivity.back = (Button) mGridActivity.findViewById(R.id.back);
        mGridActivity.summit = (Button) mGridActivity.findViewById(R.id.summit);
        mGridActivity.accept = (Button) mGridActivity.findViewById(R.id.accept);
        mGridActivity.veto = (Button) mGridActivity.findViewById(R.id.veto);
        mGridActivity.accept.setOnClickListener(mGridActivity.acceptListener);
        mGridActivity.veto.setOnClickListener(mGridActivity.vetoListener);
        mGridActivity.flagFillBack = false;
        ToolBarHelper.setTitle(mGridActivity.mActionBar,mGridActivity.mFunction.mCaption);
        initPopupWindow2();
        if(mGridActivity.mFunction.showAction == true)
        {
            mGridActivity.mHorizontalScrollView.setVisibility(View.GONE);
            mGridActivity.mActions.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mGridActivity.mTable
                    .getLayoutParams();
            mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mLayoutParams.bottomMargin = (int) (50* mGridActivity.mBasePresenter.mScreenDefine.density);
            mGridActivity.mTable.setLayoutParams(mLayoutParams);
        }
        mGridActivity.waitDialog.show();
        if(mGridActivity.mFunction.isWorkFlowDetial == true || mGridActivity.mFunction.subgirde == true)
        {
            FunAsks.getSubGride(mGridActivity, mGridHandler, mGridActivity.mFunction);
        }
        else
        {
            FunAsks.getBoardData(mGridActivity, mGridHandler, mGridActivity.mFunction, mGridActivity.mFunData.page,"");
        }

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

    private void initAciton() {

        if (!mGridActivity.mTableDetial.backEnabled) {
            mGridActivity.back.setClickable(false);
            mGridActivity.back.setTextColor(Color.rgb(231, 231, 231));
        }
        else
        {
            mGridActivity.back.setClickable(true);
            mGridActivity.back.setTextColor(Color.rgb(140, 140, 140));
        }

        if (!mGridActivity.mTableDetial.transmitEnabled) {
            mGridActivity.summit.setClickable(false);
            mGridActivity.summit.setTextColor(Color.rgb(231, 231, 231));
        }
        else
        {
            mGridActivity.summit.setClickable(true);
            mGridActivity.summit.setTextColor(Color.rgb(140, 140, 140));
        }

        if (!mGridActivity.mTableDetial.approvalEnabled) {
            mGridActivity.accept.setTextColor(Color.rgb(231, 231, 231));
            mGridActivity.accept.setClickable(false);

            mGridActivity.veto.setTextColor(Color.rgb(231, 231, 231));
            mGridActivity.veto.setClickable(false);
        }
        else
        {
            mGridActivity.accept.setClickable(true);
            mGridActivity.accept.setTextColor(Color.rgb(140, 140, 140));
            mGridActivity.veto.setClickable(true);
            mGridActivity.veto.setTextColor(Color.rgb(140, 140, 140));
        }
    }


    public void initData() {
        if(mGridActivity.mFunData.showSearch)
        {
            showSearch();
        }
        else
        {
            hidSearch();
        }
        if(mGridActivity.getIntent().hasExtra("actionjson"))
        {
            FunPrase.parseWarnDetailInfo(mGridActivity.mTableDetial,mGridActivity.getIntent().getStringExtra("actionjson"));
        }
        if(mGridActivity.mFunction.showAction)
        {
            initAciton();
        }
        if(mGridActivity.mFunData.mKeys.size() <= 1 || mGridActivity.mFunction.showAction == true)
        {
            mGridActivity.mHorizontalScrollView.setVisibility(View.GONE);
            if(mGridActivity.mFunData.mKeys.size() == 1)
            {
                mGridActivity.mFunData.showKey = mGridActivity.mFunData.mKeys.get(0);
                mGridActivity.mTable.init((GrideData) mGridActivity.mFunData.funDatas.get(mGridActivity.mFunData.mKeys.get(0)));
                mGridActivity.mTable.setVisibility(View.VISIBLE);

            }
        }
        else
        {
            mGridActivity.mHorizontalScrollView.setVisibility(View.VISIBLE);
            LayoutInflater mInflater = (LayoutInflater) mGridActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mGridActivity.mClassManager.removeAllViews();
            for(int i = 0; i < mGridActivity.mFunData.mKeys.size() ; i++)
            {
                View mView = mInflater.inflate(R.layout.taskbuttomtab, null);
                TextView mTextViewa = (TextView) mView.findViewById(R.id.tebtext1);
                TextView mTextViewb = (TextView) mView.findViewById(R.id.tebtext2);
                mView.setTag(mGridActivity.mFunData.mKeys.get(i));
                mTextViewa.setText("  "+ mGridActivity.mFunData.mKeys.get(i)+"  ");
                mTextViewb.setText("  "+ mGridActivity.mFunData.mKeys.get(i)+"  ");
                mView.setOnClickListener(classTabListener);
                mGridActivity.mClassManager.addView(mView);
                if(i == 0)
                {
                    mGridActivity.mFunData.showTab = mView;
                    mGridActivity.mFunData.showKey = mGridActivity.mFunData.showTab.getTag().toString();
                    mTextViewa.setVisibility(View.VISIBLE);
                    mTextViewb.setVisibility(View.INVISIBLE);
                }
            }
            mGridActivity.mTable.init((GrideData) mGridActivity.mFunData.funDatas.get(mGridActivity.mFunData.showTab.getTag().toString()));
        }
    }

    public View.OnClickListener classTabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView mTextViewa = (TextView) v.findViewById(R.id.tebtext1);
            TextView mTextViewb = (TextView) v.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.VISIBLE);
            mTextViewb.setVisibility(View.INVISIBLE);
            mTextViewa = (TextView) mGridActivity.mFunData.showTab.findViewById(R.id.tebtext1);
            mTextViewb = (TextView) mGridActivity.mFunData.showTab.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.INVISIBLE);
            mTextViewb.setVisibility(View.VISIBLE);
            mGridActivity.mFunData.showTab = v;
            mGridActivity.mFunData.showKey = mGridActivity.mFunData.mKeys.get(0);
            mGridActivity.mTable.init((GrideData) mGridActivity.mFunData.funDatas.get(mGridActivity.mFunData.showTab.getTag().toString()));
        }
    };

    public TableView.PullUpDownListner mPullUpDownListner = new TableView.PullUpDownListner() {

        @Override
        public void doPullUp() {
            GrideData grideData = (GrideData) mGridActivity.mFunData.funDatas.get(mGridActivity.mFunData.showKey);
            if(grideData.isall == false)
            {
                mGridActivity.waitDialog.show();
                FunAsks.getBoardData(mGridActivity, mGridHandler, mGridActivity.mFunction, mGridActivity.mFunData.page, mGridActivity.mFunData.keyWord);
            }
            else{
                mGridActivity.mTable.swipeRefreshLayout.setLoading(false);
            }

        }

        @Override
        public void doPullDown() {
            mGridActivity.mFunData.upDate();
            mGridActivity.waitDialog.show();
            FunAsks.getBoardData(mGridActivity, mGridHandler, mGridActivity.mFunction, mGridActivity.mFunData.page, mGridActivity.mFunData.keyWord);
        }
    };

    public void updataView() {
        mGridActivity.mTable.updata();
    }

    public TableView.DoClickListener mDoClickListener = new TableView.DoClickListener() {

        @Override
        public void doClickListener(TableCloumArts mTableCloumArts, TableItem mTableItem, int line, TextView view) {
            GrideData grideData;
            if(mGridActivity.mFunData.mKeys.size() == 1)
            {
                grideData = (GrideData) mGridActivity.mFunData.funDatas.get(mGridActivity.mFunData.mKeys.get(0));
            }
            else
            {
                grideData = (GrideData) mGridActivity.mFunData.funDatas.get(mGridActivity.mFunData.showTab.getTag());
            }
            if(mTableCloumArts.dataType.toLowerCase().equals("email") || mTableCloumArts.dataType.toLowerCase().equals("phone") ||
                    mTableCloumArts.dataType.toLowerCase().equals("city"))
            {
                simpleoper(mTableCloumArts,mTableItem,view);
            }
            else
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
                    function.mRowName = grideData.tableLeft.get(line).mTitle;
                    function.mCellValue = mTableItem.mTitle;
                    if(mGridActivity.mFunData.mKeys.size() == 1)
                    {
                        function.series = mGridActivity.mFunData.mKeys.get(0);
                    }
                    else if(mGridActivity.mFunData.mKeys.size() > 1)
                    {
                        function.series = (String) mGridActivity.mFunData.showTab.getTag();
                    }
                    Intent intent = null;
                    if (function.typeName.equals(Function.COLUMN) || function.typeName.equals(Function.COLUMNS) || function.typeName.equals(Function.FUNNEL)
                            || function.typeName.equals(Function.PIE) || function.typeName.equals(Function.BAR)) {
                        intent = new Intent(mGridActivity, ChartActivity.class);

                    } else if (function.typeName.equals(Function.LABEL)) {
                        intent = new Intent(mGridActivity, LabelActivity.class);

                    } else if (function.typeName.equals(Function.GRID)) {
                        intent = new Intent(mGridActivity, GridActivity.class);
                    }
                    if(intent != null)
                    {
                        intent.putExtra("function",function);
                        mGridActivity.startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(mGridActivity, GridDetialActivity.class);
                    Function function = new Function();
                    function.mCaption = mGridActivity.mFunction.mCaption;
                    function.mName = mGridActivity.mFunction.mName;
                    function.modulflag = grideData.modul;
                    function.canEdit = grideData.canEdit;
                    if(mGridActivity.mFunData.mKeys.size() == 1)
                    {
                        function.series = mGridActivity.mFunData.mKeys.get(0);
                    }
                    else if(mGridActivity.mFunData.mKeys.size() > 1)
                    {
                        function.series = (String) mGridActivity.mFunData.showTab.getTag();
                    }
                    function.mRecordId = grideData.dataKeys.get(line-1);
                    function.mCellValue = grideData.tableContent.get(grideData.dataKeys.get(line-1));
                    function.mColName = grideData.head;
                    intent.putExtra("function",function);
                    if(mGridActivity.getIntent().hasExtra("mGridActivity"))
                    intent.putExtra("actionjson",mGridActivity.getIntent().getStringExtra("mGridActivity"));
                    mGridActivity.startActivity(intent);
                }
            }
        }
    };

    public void simpleoper(TableCloumArts mTableCloumArts, TableItem mTableItem,TextView view) {
        if(mTableCloumArts.dataType.equals("email"))
        {
            Bus.callData(mGridActivity,"mail/senMailAddress",mTableItem.mTitle);
        }
        else if(mTableCloumArts.dataType.equals("phone"))
        {
            AppUtils.checkTel(mGridActivity,mTableItem.mTitle);
        }
        else if(mTableCloumArts.dataType.equals("city"))
        {
            if(mTableItem.mTitle.length() == 0)
            {
                AppUtils.showMessage(mGridActivity,mGridActivity.getString(R.string.address_empty));
            }
            else
            {
                MapUtils.showMap(mGridActivity,mGridActivity.shade
                        ,mGridActivity.findViewById(R.id.activity_grid)
                        ,mTableItem.mTitle,mGridActivity.getString(R.string.app_name));
            }
        }

    }

    public void showPopupWindow2(View mTextView) {

        if (!mGridActivity.popupWindow2.isShowing()) {
            mGridActivity.popupWindow2.showAsDropDown(mTextView, mTextView.getLayoutParams().width / 2, 0);
        } else {
            mGridActivity.popupWindow2.dismiss();
        }
    }

    public void doSearch(String keyword) {
        mGridActivity.mFunData.keyWord = keyword;
        mGridActivity.waitDialog.show();
        mGridActivity.mFunData.upDate();
        FunAsks.getBoardData(mGridActivity, mGridHandler, mGridActivity.mFunction, mGridActivity.mFunData.page, mGridActivity.mFunData.keyWord);
    }

    public void onGaode() {
        Intent intent = new Intent("android.intent.action.VIEW",
                Uri.parse("androidamap://keywordNavi?sourceApplication=intersky&keyword="
                        + Uri.encode(mGridActivity.address) + " &style=2"));
        intent.setPackage("com.autonavi.minimap");
        intent.addCategory("android.intent.category.DEFAULT");
        mGridActivity.startActivity(intent);

        mGridActivity.popupWindow2.dismiss();
    }

    public void onGaodeDown() {
        String url = "http://mobile.amap.com/download.html";
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mGridActivity.startActivity(it);
        mGridActivity.popupWindow2.dismiss();
    }

    public void onBaidu() {
        Intent intent = new Intent();
        try {

            // 移动APP调起Android百度地图方式举例
            intent = Intent.getIntent("intent://map/geocoder?city=" + mGridActivity.address
                    + "&src=intersky#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 调起百度地图客户端（Android）展示上海市"28"路公交车的检索结果
        mGridActivity.startActivity(intent); // 启动调用

        mGridActivity.popupWindow2.dismiss();
    }

    public void onBaiduDown() {
        String url = "http://shouji.baidu.com/soft/item?docid=7943539&from=web_alad_6";
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mGridActivity.startActivity(it);
        mGridActivity.popupWindow2.dismiss();
    }

    private void showSearch() {
        mGridActivity.mSearchView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mGridActivity.mTable.getLayoutParams();
        params.addRule(RelativeLayout.BELOW,R.id.searchViewlayer);
        mGridActivity.mTable.setLayoutParams(params);
    }

    private void hidSearch() {
        mGridActivity.mSearchView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mGridActivity.mTable.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mGridActivity.mTable.setLayoutParams(params);
    }

    private void initPopupWindow2() {
        initPopupWindowView2();
        mGridActivity.popupWindow2 = new PopupWindow(mGridActivity.mPopupWindowView2,
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mGridActivity.popupWindow2.setFocusable(true);
        mGridActivity.popupWindow2.setOutsideTouchable(true);
        mGridActivity.popupWindow2.setTouchable(true);
        mGridActivity.popupWindow2
                .setBackgroundDrawable(mGridActivity.getResources().getDrawable(R.color.toolBarBgColor));
        mGridActivity.popupWindow2.update();
        mGridActivity.popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mGridActivity.popupWindow2.dismiss();
            }
        });
    }

    private void initPopupWindowView2() {
        mGridActivity.mPopupWindowView2 = LayoutInflater.from(mGridActivity).inflate(R.layout.mapmenu,
                null);
        TextView mtext = (TextView) mGridActivity.mPopupWindowView2.findViewById(R.id.textview_gaoded);
        if (AppUtils.checkApkExist(mGridActivity, "com.autonavi.minimap")) {
            mtext.setVisibility(View.INVISIBLE);
            RelativeLayout mRelativeLayout = (RelativeLayout) mGridActivity.mPopupWindowView2
                    .findViewById(R.id.gaodelayer);
            mRelativeLayout.setOnClickListener(mGridActivity.mOpenGaodeListener);
        } else {
            mtext.setVisibility(View.VISIBLE);
            mtext.setOnClickListener(mGridActivity.mOpenGaodeDownListener);
        }
        mtext = (TextView) mGridActivity.mPopupWindowView2.findViewById(R.id.textview_baidud);
        if (AppUtils.checkApkExist(mGridActivity, "com.baidu.BaiduMap")) {
            mtext.setVisibility(View.INVISIBLE);
            RelativeLayout mRelativeLayout = (RelativeLayout) mGridActivity.mPopupWindowView2
                    .findViewById(R.id.baidulayer);
            mRelativeLayout.setOnClickListener(mGridActivity.mOpenBaiduListener);
        } else {
            mtext.setVisibility(View.VISIBLE);
            mtext.setOnClickListener(mGridActivity.mOpenBaiduDownListener);
        }
    }

    public void doAccept() {
        AppUtils.creatDialogTowButtonEdit(mGridActivity, mGridActivity.getString(R.string.approvemsg), ""
                , mGridActivity.getString(R.string.button_word_cancle), mGridActivity.getString(R.string.button_word_summit),null,new GridPresenter.AcceptListener()
                , mGridActivity.getString(R.string.approvemsg_hit));
    }

    public void doVote() {
        AppUtils.creatDialogTowButtonEdit(mGridActivity, mGridActivity.getString(R.string.title_approvedonot), mGridActivity.getString(R.string.title_approvedonot)
                , mGridActivity.getString(R.string.button_word_cancle), mGridActivity.getString(R.string.button_word_summit),null,new GridPresenter.VetoListener()
                , mGridActivity.getString(R.string.hit_approvedonot));
    }

    class VetoListener extends EditDialogListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mGridActivity.waitDialog.show();
            if(this.editText != null)
                WorkFlowAsks.doVeto(mGridActivity, mGridHandler, mGridActivity.mFunction.modulflag,this.editText.getText().toString());
            else
                WorkFlowAsks.doVeto(mGridActivity, mGridHandler, mGridActivity.mFunction.modulflag,"");
        }
    }

    class AcceptListener extends EditDialogListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mGridActivity.waitDialog.show();
            if(this.editText != null)
                WorkFlowAsks.doAccept(mGridActivity, mGridHandler, mGridActivity.mFunction.modulflag,editText.getText().toString());
            else
                WorkFlowAsks.doAccept(mGridActivity, mGridHandler, mGridActivity.mFunction.modulflag,"");
        }
    }

    public void updataAction(Intent intent) {

        if(mGridActivity.mFunction.showAction == true)
        {
            if(intent.hasExtra("taskid"))
            {
                if(intent.getStringExtra("taskid").equals(mGridActivity.mFunction.modulflag))
                {
                    if(intent.hasExtra("actionsuccess"))
                    {
                        closeAciton();
                    }
                }
            }
        }
    }

    private void closeAciton(){
        mGridActivity.back.setClickable(false);
        mGridActivity.back.setTextColor(Color.rgb(231, 231, 231));
        mGridActivity.accept.setTextColor(Color.rgb(231, 231, 231));
        mGridActivity.accept.setClickable(false);
        mGridActivity.summit.setClickable(false);
        mGridActivity.summit.setTextColor(Color.rgb(231, 231, 231));
        mGridActivity.veto.setTextColor(Color.rgb(231, 231, 231));
        mGridActivity.veto.setClickable(false);
    }
}
