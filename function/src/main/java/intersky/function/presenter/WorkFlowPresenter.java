package intersky.function.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.receiver.GrideDetialReceiver;
import intersky.function.receiver.WorkFlowReceiver;
import intersky.function.receiver.entity.Function;
import intersky.function.receiver.entity.WorkFlowData;
import intersky.function.receiver.entity.WorkFlowItem;
import intersky.function.handler.WorkFlowHandler;
import intersky.function.view.activity.GridDetialActivity;
import intersky.function.view.activity.WebMessageActivity;
import intersky.function.view.activity.WorkFlowActivity;
import intersky.function.view.adapter.WorkFlowAdapter;
import intersky.function.view.adapter.WorkFlowerPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.TabHeadView;
import intersky.xpxnet.net.NetUtils;

/**
 * Created by xpx on 2017/8/18.
 */

public class WorkFlowPresenter implements Presenter {

    public WorkFlowHandler mWorkFlowHandler;
    public WorkFlowActivity mWorkFlowActivity;
    public WorkFlowPresenter(WorkFlowActivity mWorkFlowActivity)
    {
        this.mWorkFlowActivity = mWorkFlowActivity;
        this.mWorkFlowHandler = new WorkFlowHandler(mWorkFlowActivity);
        mWorkFlowActivity.setBaseReceiver(new WorkFlowReceiver(mWorkFlowHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mWorkFlowActivity.setContentView(R.layout.activity_workflow);
        if(mWorkFlowActivity.getIntent().getBooleanExtra("iscloud",false) == false)
        {
            if(FunctionUtils.getInstance() != null)
            FunctionUtils.getInstance().cleanWorkflow();
            mWorkFlowActivity.mTabHeadView = mWorkFlowActivity.findViewById(R.id.head);
            mWorkFlowActivity.mTabHeadView.setVisibility(View.GONE);
            mWorkFlowActivity.mViewPager = (NoScrollViewPager) mWorkFlowActivity.findViewById(R.id.load_pager);
            mWorkFlowActivity.mViewPager.setNoScroll(true);
            View mView1 = null;
            mView1 = mWorkFlowActivity.getLayoutInflater().inflate(R.layout.workflower_pager, null);
            mWorkFlowActivity.classesBar = mView1.findViewById(R.id.classesBar);
            mWorkFlowActivity.mHorizontalScrollView = mView1.findViewById(R.id.hScrollView1);
            mWorkFlowActivity.mViews.add(mView1);
            mWorkFlowActivity.mListView = mView1.findViewById(R.id.busines_List);
            String[] names = new String[1];
            names[0] = "";
            mWorkFlowActivity.mLoderPageAdapter = new WorkFlowerPageAdapter(mWorkFlowActivity.mViews,names);
            mWorkFlowActivity.mViewPager.setAdapter(mWorkFlowActivity.mLoderPageAdapter);
            mWorkFlowActivity.mViewPager.setCurrentItem(0);
            WorkFlowAsks.getWorkFlowList(mWorkFlowActivity,mWorkFlowHandler);
            mWorkFlowActivity.mListView.setOnItemClickListener(mWorkFlowActivity.onItemClickListener);
//            WorkFlowAsks.getWorkFlowList(mWorkFlowActivity,mWorkFlowHandler);
        }
        else
        {
            if(FunctionUtils.getInstance() != null)
            FunctionUtils.getInstance().cleanWorkflow();
            View mView1 = null;
            View mView2 = null;
            String[] names = new String[2];
            mView1 = mWorkFlowActivity.getLayoutInflater().inflate(R.layout.workflower_pager, null);
            mView2 = mWorkFlowActivity.getLayoutInflater().inflate(R.layout.workflower_pager, null);
            mWorkFlowActivity.classesBar = mView1.findViewById(R.id.classesBar);
            mWorkFlowActivity.mHorizontalScrollView = mView1.findViewById(R.id.hScrollView1);
            mWorkFlowActivity.classesBar2 = mView2.findViewById(R.id.classesBar);
            mWorkFlowActivity.mHorizontalScrollView2 = mView2.findViewById(R.id.hScrollView1);
            names[0] = "待审批";
            names[1] = "审批中";
            mWorkFlowActivity.mTabHeadView = mWorkFlowActivity.findViewById(R.id.head);
            mWorkFlowActivity.mViewPager = (NoScrollViewPager) mWorkFlowActivity.findViewById(R.id.load_pager);
            mWorkFlowActivity.mViewPager.setNoScroll(true);
            mWorkFlowActivity.mViews.add(mView1);
            mWorkFlowActivity.mViews.add(mView2);
            mWorkFlowActivity.mListView = mView1.findViewById(R.id.busines_List);
            mWorkFlowActivity.mListView2 = mView2.findViewById(R.id.busines_List);
            mWorkFlowActivity.mLoderPageAdapter = new WorkFlowerPageAdapter(mWorkFlowActivity.mViews,names);
            mWorkFlowActivity.mViewPager.setAdapter(mWorkFlowActivity.mLoderPageAdapter);
            mWorkFlowActivity.mTabHeadView.setmOnTabLisener(mOnTabLisener);
            mWorkFlowActivity.mTabHeadView.setViewPager(mWorkFlowActivity.mViewPager);
            mWorkFlowActivity.mViewPager.setCurrentItem(0);
            mWorkFlowActivity.mFunction = mWorkFlowActivity.getIntent().getParcelableExtra("function");
            mWorkFlowActivity.waitDialog.show();
            mWorkFlowActivity.mListView.setOnItemClickListener(mWorkFlowActivity.onItemClickListener);
            mWorkFlowActivity.mListView2.setOnItemClickListener(mWorkFlowActivity.onItemClickListener);
            if(FunctionUtils.getInstance().mFunData != null)
            {
                if(FunctionUtils.getInstance().mFunData.funDatas.size() == 0)
                    WorkFlowAsks.getWorkFlowList(mWorkFlowActivity,mWorkFlowHandler,0);
            }
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

    public void inidSerias(int state) {
        if(state == 0) {
            if(FunctionUtils.getInstance().mFunData.mKeys.size() <= 1)
            {
                mWorkFlowActivity.mHorizontalScrollView.setVisibility(View.GONE);
                if(FunctionUtils.getInstance().mFunData.mKeys.size() == 1)
                {

                    initData((WorkFlowData) FunctionUtils.getInstance().mFunData.funDatas.get(FunctionUtils.getInstance().mFunData.mKeys.get(0)),state);
                }
            }
            else
            {
                mWorkFlowActivity.mHorizontalScrollView.setVisibility(View.VISIBLE);
                LayoutInflater mInflater = (LayoutInflater) mWorkFlowActivity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mWorkFlowActivity.classesBar.removeAllViews();
                for(int i = 0 ; i < FunctionUtils.getInstance().mFunData.mKeys.size() ; i++)
                {
                    View mView = mInflater.inflate(R.layout.taskbuttomtab, null);
                    TextView mTextViewa = (TextView) mView.findViewById(R.id.tebtext1);
                    TextView mTextViewb = (TextView) mView.findViewById(R.id.tebtext2);
                    mView.setTag(FunctionUtils.getInstance().mFunData.mKeys.get(i));
                    mTextViewa.setText("  "+FunctionUtils.getInstance().mFunData.mKeys.get(i)+"  ");
                    mTextViewb.setText("  "+FunctionUtils.getInstance().mFunData.mKeys.get(i)+"  ");
                    mView.setOnClickListener(classTabListener);
                    mWorkFlowActivity.classesBar.addView(mView);
                    if(i == 0)
                    {
                        FunctionUtils.getInstance().mFunData.showTab = mView;
                        mTextViewa.setVisibility(View.VISIBLE);
                        mTextViewb.setVisibility(View.INVISIBLE);
                    }
                }
                initData((WorkFlowData) FunctionUtils.getInstance().mFunData.funDatas.get(FunctionUtils.getInstance().mFunData.showTab.getTag().toString()),state);
            }
        }
        else {
            if(FunctionUtils.getInstance().mFunData2.mKeys.size() <= 1)
            {
                mWorkFlowActivity.mHorizontalScrollView2.setVisibility(View.GONE);
                if(FunctionUtils.getInstance().mFunData2.mKeys.size() == 1)
                {

                    initData((WorkFlowData) FunctionUtils.getInstance().mFunData2.funDatas.get(FunctionUtils.getInstance().mFunData2.mKeys.get(0)),state);
                }
            }
            else
            {
                mWorkFlowActivity.mHorizontalScrollView2.setVisibility(View.VISIBLE);
                LayoutInflater mInflater = (LayoutInflater) mWorkFlowActivity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mWorkFlowActivity.classesBar2.removeAllViews();
                for(int i = 0 ; i < FunctionUtils.getInstance().mFunData2.mKeys.size() ; i++)
                {
                    View mView = mInflater.inflate(R.layout.taskbuttomtab, null);
                    TextView mTextViewa = (TextView) mView.findViewById(R.id.tebtext1);
                    TextView mTextViewb = (TextView) mView.findViewById(R.id.tebtext2);
                    mView.setTag(FunctionUtils.getInstance().mFunData2.mKeys.get(i));
                    mTextViewa.setText("  "+FunctionUtils.getInstance().mFunData2.mKeys.get(i)+"  ");
                    mTextViewb.setText("  "+FunctionUtils.getInstance().mFunData2.mKeys.get(i)+"  ");
                    mView.setOnClickListener(classTabListener2);
                    mWorkFlowActivity.classesBar2.addView(mView);
                    if(i == 0)
                    {
                        FunctionUtils.getInstance().mFunData2.showTab = mView;
                        mTextViewa.setVisibility(View.VISIBLE);
                        mTextViewb.setVisibility(View.INVISIBLE);
                    }
                }
                initData((WorkFlowData) FunctionUtils.getInstance().mFunData2.funDatas.get(FunctionUtils.getInstance().mFunData2.showTab.getTag().toString()),state);
            }
        }

    }

    public void initData(WorkFlowData mWorkFlowData,int state) {
        if(state == 0)
        {
            WorkFlowAdapter mWorkFlowAdapter = new WorkFlowAdapter(mWorkFlowActivity,mWorkFlowData.workFlowItems);
            mWorkFlowActivity.mListView.setAdapter(mWorkFlowAdapter);
        }
        else
        {
            WorkFlowAdapter mWorkFlowAdapter = new WorkFlowAdapter(mWorkFlowActivity,mWorkFlowData.workFlowItems);
            mWorkFlowActivity.mListView2.setAdapter(mWorkFlowAdapter);
        }
    }

    public View.OnClickListener classTabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView mTextViewa = (TextView) v.findViewById(R.id.tebtext1);
            TextView mTextViewb = (TextView) v.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.VISIBLE);
            mTextViewb.setVisibility(View.INVISIBLE);
            mTextViewa = (TextView) FunctionUtils.getInstance().mFunData.showTab.findViewById(R.id.tebtext1);
            mTextViewb = (TextView) FunctionUtils.getInstance().mFunData.showTab.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.INVISIBLE);
            mTextViewb.setVisibility(View.VISIBLE);
            FunctionUtils.getInstance().mFunData.showTab = v;
            initData((WorkFlowData) FunctionUtils.getInstance().mFunData.funDatas.get(FunctionUtils.getInstance().mFunData.showTab.getTag().toString()),0);
        }
    };

    public View.OnClickListener classTabListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView mTextViewa = (TextView) v.findViewById(R.id.tebtext1);
            TextView mTextViewb = (TextView) v.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.VISIBLE);
            mTextViewb.setVisibility(View.INVISIBLE);
            mTextViewa = (TextView) FunctionUtils.getInstance().mFunData2.showTab.findViewById(R.id.tebtext1);
            mTextViewb = (TextView) FunctionUtils.getInstance().mFunData2.showTab.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.INVISIBLE);
            mTextViewb.setVisibility(View.VISIBLE);
            FunctionUtils.getInstance().mFunData2.showTab = v;
            initData((WorkFlowData) FunctionUtils.getInstance().mFunData2.funDatas.get(FunctionUtils.getInstance().mFunData2.showTab.getTag().toString()),1);
        }
    };

    public void doClickListener(WorkFlowItem workFlowItem) {
        if(mWorkFlowActivity.getIntent().getBooleanExtra("iscloud",false) == false) {
            Function info = new Function();
            info.mCaption = workFlowItem.subject;
            info.mName = workFlowItem.module;
            info.modulflag = workFlowItem.taskID;
            info.mRecordId = workFlowItem.recordID;
            info.isWorkFlowDetial = true;
            info.showAction = true;
            Intent intent = new Intent(mWorkFlowActivity, GridDetialActivity.class);
            intent.putExtra("function",info);
            mWorkFlowActivity.startActivity(intent);
        }
        else {
            Intent intent = new Intent(mWorkFlowActivity,
                    WebMessageActivity.class);
            intent.putExtra("isurl", true);
            String url = "";
            if(FunctionUtils.getInstance().service.https)
            {
                url = "https://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/workflow/task?task_id="+workFlowItem.taskID+"&app_languge="
                        + AppUtils.getLangue(mWorkFlowActivity)+"&token="+NetUtils.getInstance().token;
            }
            else
            {
                url = "http://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/workflow/task?task_id="+workFlowItem.taskID+"&app_languge="
                        + AppUtils.getLangue(mWorkFlowActivity)+"&token="+NetUtils.getInstance().token;
            }
            intent.putExtra("showaction", true);
            intent.putExtra("taskid", workFlowItem.taskID);
            intent.putExtra("url", url);
            mWorkFlowActivity.startActivity(intent);
        }


    }

    public TabHeadView.OnTabLisener mOnTabLisener = new TabHeadView.OnTabLisener(){

        @Override
        public void TabClick(int tab) {
            if(tab == 0)
            {
                if(FunctionUtils.getInstance().mFunData.funDatas.size() == 0)
                    WorkFlowAsks.getWorkFlowList(mWorkFlowActivity,mWorkFlowHandler,0);
            }
            else
            {
                if(FunctionUtils.getInstance().mFunData2.funDatas.size() == 0)
                    WorkFlowAsks.getWorkFlowList(mWorkFlowActivity,mWorkFlowHandler,1);
            }
        }
    };

    public void updataList() {
        FunctionUtils.getInstance().getBaseHit();
        if(mWorkFlowActivity.getIntent().getBooleanExtra("iscloud",false) == false)
        {
            mWorkFlowActivity.waitDialog.show();
            FunctionUtils.getInstance().mFunData.clean();
            WorkFlowAdapter mWorkFlowAdapter = (WorkFlowAdapter) mWorkFlowActivity.mListView.getAdapter();
            mWorkFlowAdapter.notifyDataSetChanged();
            WorkFlowAsks.getWorkFlowList(mWorkFlowActivity,mWorkFlowHandler);
        }
        else
        {
            if(mWorkFlowActivity.mViewPager.getCurrentItem() == 0)
            {
                mWorkFlowActivity.waitDialog.show();
                FunctionUtils.getInstance().mFunData.clean();
                WorkFlowAdapter mWorkFlowAdapter = (WorkFlowAdapter) mWorkFlowActivity.mListView.getAdapter();
                mWorkFlowAdapter.notifyDataSetChanged();
                WorkFlowAsks.getWorkFlowList(mWorkFlowActivity,mWorkFlowHandler,0);
            }
            else
            {
                mWorkFlowActivity.waitDialog.show();
                FunctionUtils.getInstance().mFunData2.clean();
                WorkFlowAdapter mWorkFlowAdapter = (WorkFlowAdapter) mWorkFlowActivity.mListView2.getAdapter();
                mWorkFlowAdapter.notifyDataSetChanged();
                WorkFlowAsks.getWorkFlowList(mWorkFlowActivity,mWorkFlowHandler,1);
            }
        }
    }
}
