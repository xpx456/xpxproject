package intersky.mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 作者: hewenyu
 * 日期: 2018/4/20 11:42
 * 说明:
 */

public class SwipeLayout extends FrameLayout implements View.OnClickListener {

    /**
     * 默认滚动的延迟
     */
    private static final int SCROLL_DELAY = 100;
    private static final float DEFAULT_PERCENT = 0.5f;

    /**
     * 帮助滚动的工具类
     */
    private Scroller mScroller;

    private Context mContext;

    /**
     * 回滚动画的延迟时间
     */
    private int delay = SCROLL_DELAY;

    /**
     * 插值器
     */
    private Interpolator mInterpolator;

    /**
     * 侧滑菜单的布局容器
     */
    private LinearLayout mMenuLayout;

    /**
     * 内容视图
     */
    private View mContentView;

    /**
     * 屏幕的宽高
     */
    private int mScreenWidth, mScreenHeight;

    /**
     * 滑动的最大距离
     */
    private int mMaxDistance;

    /**
     * 系统默认是滑动操作的最小间距
     */
    private int mTouchSlop;

    /**
     * 记录是否打开了menu
     */
    private boolean isOpen;

    /**
     * 记录开始的坐标
     */
    private int startX, startY;

    /**
     * 用来判断此次事件序列是否是侧滑的
     */
    private boolean mArrowScroll;

    /**
     * 速度检测
     */
    private VelocityTracker mVelocityTracker;

    /**
     * x 轴方向的滑动速度
     */
    private float xVelocity;

    /**
     * 打开menu的阈值（矢量）
     */
    private float mSpeedThreshold = 100;

    /**
     * 打开menu的长度百分比
     */
    private float mSwitchPercent = DEFAULT_PERCENT;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        // 滚动回弹的插值器
        mInterpolator = new AccelerateDecelerateInterpolator();
        mScroller = new Scroller(mContext, mInterpolator);

        // 获取屏幕的宽高
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        // 获取系统默认滑动的最小距离
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

        // 只有是 clickable 的， onTouchEvent 才会消费事件
        setClickable(true);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 获取到内容视图
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }

        // 为 menu 布局增加容器
        mMenuLayout = new LinearLayout(mContext);
        mMenuLayout.setOrientation(LinearLayout.HORIZONTAL);
        mMenuLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mMenuLayout);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = 0;
        int height = 0;

        // 计算出内容控件的高度，并将其赋值给父容器
        if (mContentView != null) {
            measureChild(mContentView, MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
            height = mContentView.getMeasuredHeight();
            width = mContentView.getMeasuredWidth();
        }

        int childCount = getChildCount();

        // 计算 menu 的宽高
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == mContentView) {
                continue;
            }
            measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
            width += child.getMeasuredWidth();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            if (child == mMenuLayout) {
                mMaxDistance = child.getMeasuredWidth();
                child.layout(mScreenWidth, 0, mScreenWidth + child.getMeasuredWidth(), child.getMeasuredHeight());
            } else {
                child.layout(0, 0, mScreenWidth, child.getMeasuredHeight());
            }

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();

                mArrowScroll = false;

                break;
            case MotionEvent.ACTION_MOVE:

                // 手指移动在 x/y 轴上的距离
                int distanceX = (int) (startX - ev.getX());
                int distanceY = (int) (startY - ev.getY());

                // 如果事件已经反拦截了，则不需要再次进行下面的判断
                if (mArrowScroll) {
                    break;
                }

                // 判断是否是侧滑menu事件，如果是，反拦截事件
                if (mMenuLayout.getChildCount() > 0 && distanceX > mTouchSlop || (getScrollX() > 0 && Math.abs(distanceX) > mTouchSlop)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    startX = (int) ev.getX();
                    mArrowScroll = true;
                    initVelocityTracker(ev);
                    if (onStartScrollListener != null) {
                        onStartScrollListener.onStartScroll(this);
                    }
                }

                // 如果是上下滑动，则不在接收此次事件序列
                if (Math.abs(distanceY) > mTouchSlop) {
                    return false;
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 初始化滑动速度相关
     *
     * @param ev
     */
    private void initVelocityTracker(MotionEvent ev) {
        mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(ev);
        xVelocity = 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                int distanceX = (int) (startX - ev.getX());
                // 判断是否是侧滑menu事件，如果是，拦截此次事件（此处的判断条件同反拦截的一致）
                if (mMenuLayout.getChildCount() > 0 && distanceX > mTouchSlop || (getScrollX() > 0 && Math.abs(distanceX) > mTouchSlop)) {
                    return true;
                }

                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                if (!mArrowScroll) {
                    break;
                }

                // 计算滑动速度
                mVelocityTracker.computeCurrentVelocity(100);
                // 获取 x 轴每 100ms 划过的像素数量（矢量）
                xVelocity = mVelocityTracker.getXVelocity();

                int distanceX = (int) (startX - event.getX());

                // 只能滑出右侧菜单
                if (getScrollX() + distanceX <= 0) {
                    distanceX = -getScrollX();
                }

                // 滑动的距离不能超过菜单按钮的的宽度
                if (distanceX + getScrollX() > mMaxDistance) {
//                    distanceX = distanceX - (distanceX + getScrollX() - mMaxDistance);
                    distanceX = mMaxDistance - getScrollX();
                }

                scrollBy(distanceX, getScrollY());
                startX = (int) event.getX();

                break;
            case MotionEvent.ACTION_UP:

                if ((getScrollX() > 0 && getScrollX() >= mMaxDistance * mSwitchPercent)
                        || (getScrollX() > 0 && xVelocity < -mSpeedThreshold)) {    // 滑动的速度超过
                    // 打开菜单
                    openMenu();
                } else if ((getScrollX() > 0 && xVelocity > mSpeedThreshold)    // 滑动的速度超过阈值
                        || (getScrollX() > 0 && getScrollX() < mMaxDistance * mSwitchPercent)) {
                    // 关闭菜单
                    closeMenu();
                }


                // 如果是菜单滚动事件，则需要屏蔽 onClick 事件
                if (mArrowScroll) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                    xVelocity = 0;
                    return true;
                }

                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        mScroller.startScroll(getScrollX(), getScrollY(), mMaxDistance - getScrollX(), 0, delay);
        invalidate();
        isOpen = true;
        if (onSwitchScrollListener != null) {
            onSwitchScrollListener.onSwitchScroll(this, true);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), 0, delay);
        invalidate();
        isOpen = false;
        if (onSwitchScrollListener != null) {
            onSwitchScrollListener.onSwitchScroll(this, false);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 判断是否是打开了菜单
     *
     * @return
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 打开menu的百分比
     *
     * @param percent
     */
    public void setSwitchPercent(float percent) {
        mSwitchPercent = (percent <= 0 || percent >= 1) ? DEFAULT_PERCENT : percent;
    }

    /**
     * 设置速度的阈值
     *
     * @param speed
     */
    public void setSpeedThreshold(int speed) {
        this.mSpeedThreshold = speed;
    }

    /**
     * 添加菜单按钮
     *
     * @param menu
     */
    public void addMenu(View menu) {
        mMenuLayout.addView(menu);
        menu.setOnClickListener(this);
    }

    /**
     * 清除所有的按钮
     */
    public void removeAllMenu() {
        mMenuLayout.removeAllViews();
    }

    /**
     * 移除目标menu
     *
     * @param index 对应Menu的下标
     */
    public void removeMenu(int index) {
        if (mMenuLayout.getChildCount() > index) {
            mMenuLayout.removeViewAt(index);
        }
    }

    /**
     * 设置滚动延迟
     *
     * @param delay
     */
    public void setScrollDelay(int delay) {
        this.delay = delay;
    }


    @Override
    public void onClick(View v) {
        closeMenu();
        if (onMenuClickListener != null) {
            onMenuClickListener.onMenuClick(v);
        }
    }


    private OnMenuClickListener onMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    /**
     * 设置菜单点击事件的监听
     */
    public interface OnMenuClickListener {

        void onMenuClick(View view);

    }

    private OnSwitchScrollListener onSwitchScrollListener;

    public void setOnSwitchScrollListener(OnSwitchScrollListener onSwitchScrollListener) {
        this.onSwitchScrollListener = onSwitchScrollListener;
    }

    /**
     * item打开关闭的监听，用来记录打开的item
     */
    public interface OnSwitchScrollListener {
        void onSwitchScroll(View view, boolean open);
    }


    private OnStartScrollListener onStartScrollListener;

    public void setOnStartScrollListener(OnStartScrollListener onStartScrollListener) {
        this.onStartScrollListener = onStartScrollListener;
    }

    /**
     * item开始滚动的监听，用来关闭上一个打开的item
     */
    public interface OnStartScrollListener {

        void onStartScroll(View view);

    }

}
