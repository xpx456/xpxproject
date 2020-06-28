package intersky.mywidget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class MySwipeRefreshLayout extends SwipeRefreshLayout {


    private int mScaledTouchSlop;
    private View mFooterView;
    private ListView mListView;
    private ScrollView lineView;
    private ViewGroup content;
    private OnLoadMoreListener mListener;
    /**
     * 正在加载状态
     */
    private boolean isLoading;
    private RecyclerView mRecyclerView;

    public MySwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 填充底部加载布局
        mFooterView = View.inflate(context, R.layout.refresh_footer, null);
        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 获取ListView,设置ListView的布局位置
        if (mListView == null && mRecyclerView == null) {
            // 判断容器有多少个孩子
            if (getChildCount() > 0) {
                // 判断第一个孩子是不是ListView
                if (getChildAt(0) instanceof ListView) {
                    // 创建ListView对象
                    mListView = (ListView) getChildAt(0);

                    // 设置ListView的滑动监听
                    setListViewOnScroll();
                } else if (getChildAt(0) instanceof RecyclerView) {
                    // 创建ListView对象
                    mRecyclerView = (RecyclerView) getChildAt(0);

                    // 设置RecyclerView的滑动监听
                    setRecyclerViewOnScroll();
                }
            }
        }
    }

    public void setLineView(ScrollView lineView,ViewGroup content) {
        this.lineView = lineView;
        this.content = content;
    }


    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    private float mDownY, mUpY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
                if(lineView != null)
                {
                    if(canLoadMore2())
                    {
                        loadData();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 移动的终点


                mUpY = getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean canLoadMore2() {
        // 1. 是上拉状态
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;
        // 2. 当前页面可见的item是最后一个条目,一般最后一个条目位置需要大于第一页的数据长度
        boolean condition2 = false;
        if(lineView != null)
        {
            View last = content.getChildAt(content.getChildCount()-1);

            Rect scrollRect = new Rect();
            lineView.getHitRect(scrollRect);
            //子控件在可视范围内（至少有一个像素在可视范围内）
            if(last != null)
            {
                if (last.getLocalVisibleRect(scrollRect)) {
                    condition2 = true;
                } else {
                    ////子控件完全不在可视范围内
                    condition2 = false;
                }
            }
            else
            {
                condition2 = false;
            }
        }
        // 3. 正在加载状态
        boolean condition3 = !isLoading;
        return condition1 && condition2 && condition3;
    }

    /**
     * 判断是否满足加载更多条件
     */
    private boolean canLoadMore() {
        // 1. 是上拉状态
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;
        // 2. 当前页面可见的item是最后一个条目,一般最后一个条目位置需要大于第一页的数据长度
        boolean condition2 = false;
        if(mListView != null)
        {
            if(mListView.getAdapter() != null)
            {
                condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
                if (mListView instanceof AdapterView<?>) {
                    AdapterView<?> mAdapterView = (AdapterView<?>) mListView;
                    View lastChild = mAdapterView.getChildAt(mAdapterView.getChildCount() - 1);
                    if (lastChild == null) {
                        // 如果mAdapterView中没有数据,不拦截
                        condition2 = false;
                    }
                    // 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
                    // 等于父View的高度说明mAdapterView已经滑动到最后
                    if (lastChild.getBottom() <= getHeight() && mAdapterView.getLastVisiblePosition() == mAdapterView.getCount() - 1) {
                        condition2 = true;
                    }
                }
                else
                {
                    condition2 = false;
                }
            }
        }
        else if(mRecyclerView != null)
        {
            if(mRecyclerView.getAdapter() != null)
            {
                RecyclerView.LayoutManager mLayoutManager = mRecyclerView.getLayoutManager();
                if (mLayoutManager instanceof LinearLayoutManager)
                {
                    LinearLayoutManager linearManager = (LinearLayoutManager) mLayoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    if(lastItemPosition ==  mRecyclerView.getAdapter().getItemCount()-1)
                    {
                        View lastChild = linearManager.getChildAt(mRecyclerView.getAdapter().getItemCount()-1);
                        if (lastChild.getBottom() <= getHeight()) {
                            condition2 = true;
                        }
                    }

                }
                else if(mLayoutManager instanceof GrideLayoutManager)
                {
                    GrideLayoutManager linearManager = (GrideLayoutManager) mLayoutManager;
                    int pos = linearManager.getLastVisiableIndex();
                    View lastChild = linearManager.getLastVisiableView();
                    TableAdapter mTableAdapter = (TableAdapter) mRecyclerView.getAdapter();
                    if (lastChild.getBottom() <= getHeight()&& pos/mTableAdapter.cloums == (mRecyclerView.getAdapter().getItemCount()-1)/mTableAdapter.cloums) {
                        condition2 = true;
                    }
                }

            }
        }
        boolean condition3 = !isLoading;
        return condition1 && condition2 && condition3;
    }


    /**
     * 处理加载数据的逻辑
     */
    private void loadData() {
        System.out.println("加载数据...");
        if (mListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true);
            mListener.onLoadMore();
        }

    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        // 修改当前的状态
        isLoading = loading;
        if(mListView != null)
        {
            if (isLoading) {
                // 显示布局
                mListView.addFooterView(mFooterView);
            } else {
                // 隐藏布局
                mListView.removeFooterView(mFooterView);

                // 重置滑动的坐标
                mDownY = 0;
                mUpY = 0;
            }
        }

    }


    /**
     * 设置ListView的滑动监听
     */
    private void setListViewOnScroll() {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    /**
     * 设置RecyclerView的滑动监听
     */
    private void setRecyclerViewOnScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    /**
     * 上拉加载的接口回调
     */

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }
}
