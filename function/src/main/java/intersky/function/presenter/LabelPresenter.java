package intersky.function.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.function.R;
import intersky.function.asks.FunAsks;
import intersky.function.receiver.entity.LableData;
import intersky.function.handler.LabelHandler;
import intersky.function.view.activity.LabelActivity;
import intersky.function.view.adapter.LabelListAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class LabelPresenter implements Presenter {

    public LabelHandler mLabelHandler;
    public LabelActivity mLabelActivity;
    public LabelPresenter(LabelActivity mLabelActivity)
    {
        this.mLabelActivity = mLabelActivity;
        this.mLabelHandler = new LabelHandler(mLabelActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mLabelActivity.setContentView(R.layout.activity_lable);
        mLabelActivity.mListView = mLabelActivity.findViewById(R.id.warnList);
        mLabelActivity.classesBar = mLabelActivity.findViewById(R.id.classesBar);
        mLabelActivity.mHorizontalScrollView = mLabelActivity.findViewById(R.id.hScrollView1);
        mLabelActivity.mFunction = mLabelActivity.getIntent().getParcelableExtra("function");
        mLabelActivity.waitDialog.show();
        FunAsks.getLableBoardData(mLabelActivity,mLabelHandler,mLabelActivity.mFunction);
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

    public void inidSerias() {
        if(mLabelActivity.mFunData.mKeys.size() <= 1)
        {
            mLabelActivity.mHorizontalScrollView.setVisibility(View.GONE);
            if(mLabelActivity.mFunData.mKeys.size() == 1)
            {

                initData((LableData) mLabelActivity.mFunData.funDatas.get(mLabelActivity.mFunData.mKeys.get(0)));
            }
        }
        else
        {
            mLabelActivity.mHorizontalScrollView.setVisibility(View.VISIBLE);
            LayoutInflater mInflater = (LayoutInflater) mLabelActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLabelActivity.classesBar.removeAllViews();
            for(int i = 0 ; i < mLabelActivity.mFunData.mKeys.size() ; i++)
            {
                View mView = mInflater.inflate(R.layout.taskbuttomtab, null);
                TextView mTextViewa = (TextView) mView.findViewById(R.id.tebtext1);
                TextView mTextViewb = (TextView) mView.findViewById(R.id.tebtext2);
                mView.setTag(mLabelActivity.mFunData.mKeys.get(i));
                mTextViewa.setText("  "+mLabelActivity.mFunData.mKeys.get(i)+"  ");
                mTextViewb.setText("  "+mLabelActivity.mFunData.mKeys.get(i)+"  ");
                mView.setOnClickListener(classTabListener);
                mLabelActivity.classesBar.addView(mView);
                if(i == 0)
                {
                    mLabelActivity.mFunData.showTab = mView;
                    mTextViewa.setVisibility(View.VISIBLE);
                    mTextViewb.setVisibility(View.INVISIBLE);
                }
            }
            initData((LableData) mLabelActivity.mFunData.funDatas.get(mLabelActivity.mFunData.showTab.getTag().toString()));
        }
    }

    public void initData(LableData mLableData) {
        LabelListAdapter mLabelListAdapter = new LabelListAdapter(mLabelActivity,mLableData.lableDataItems);
        mLabelActivity.mListView.setAdapter(mLabelListAdapter);
    }

    public View.OnClickListener classTabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView mTextViewa = (TextView) v.findViewById(R.id.tebtext1);
            TextView mTextViewb = (TextView) v.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.VISIBLE);
            mTextViewb.setVisibility(View.INVISIBLE);
            mTextViewa = (TextView) mLabelActivity.mFunData.showTab.findViewById(R.id.tebtext1);
            mTextViewb = (TextView) mLabelActivity.mFunData.showTab.findViewById(R.id.tebtext2);
            mTextViewa.setVisibility(View.INVISIBLE);
            mTextViewb.setVisibility(View.VISIBLE);
            mLabelActivity.mFunData.showTab = v;
            initData((LableData) mLabelActivity.mFunData.funDatas.get(mLabelActivity.mFunData.showTab.getTag().toString()));
        }
    };
}
