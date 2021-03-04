package intersky.function.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.function.entity.Function;
import intersky.function.entity.WorkFlowItem;
import intersky.function.presenter.WorkFlowPresenter;
import intersky.function.view.adapter.WorkFlowerPageAdapter;
import intersky.mywidget.NoScrollViewPager;


/**
 * Created by xpx on 2017/8/18.
 */

public class WorkFlowActivity extends BaseActivity {

    public ListView mListView;
    public LinearLayout classesBar;
    public ListView mListView2;
    public LinearLayout classesBar2;
    public HorizontalScrollView mHorizontalScrollView;
    public HorizontalScrollView mHorizontalScrollView2;
    public Function mFunction;
    public RelativeLayout mRightTeb;
    public RelativeLayout mLefttTeb;
    public TextView mRightImg;
    public TextView mLefttImg;
    public WorkFlowerPageAdapter mLoderPageAdapter;
    public NoScrollViewPager mViewPager;
    public ArrayList<View> mViews = new ArrayList<View>();


    public WorkFlowPresenter mWorkFlowPresenter = new WorkFlowPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWorkFlowPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mWorkFlowPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mWorkFlowPresenter.doClickListener((WorkFlowItem) parent.getAdapter().getItem(position));
        }
    };


}
