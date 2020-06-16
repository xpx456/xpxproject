package intersky.mywidget;

import android.graphics.Rect;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GrideLayoutManager extends RecyclerView.LayoutManager {

    private int cloums;
    private SparseArray<Rect> allItemRects = new SparseArray<>();
    /**
     * 用于保存item是否处于可见状态的信息
     */
    private SparseBooleanArray itemStates = new SparseBooleanArray();
    public int totalHeight = 0;
    public int totalWidth = 0;
    private int verticalScrollOffset;
    private int horizontalScrollOffset;
    private View mLastVisiableView;
    private int mLastVisiableIndex;

    public GrideLayoutManager(int cloums) {
        this.cloums = cloums;
        if (this.cloums == 0) {
            this.cloums = 1;
        }
    }

    public void setCloums(int cloums) {
        this.cloums = cloums;
        if (this.cloums == 0) {
            this.cloums = 1;
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {

        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 先把所有的View先从RecyclerView中detach掉，然后标记为"Scrap"状态，表示这些View处于可被重用状态(非显示中)。
        // 实际就是把View放到了Recycler中的一个集合中。
        detachAndScrapAttachedViews(recycler);
        calculateChildrenSite(recycler);
        recycleAndFillView(recycler, state);


    }

    private void calculateChildrenSite(RecyclerView.Recycler recycler) {
        totalHeight = 0;
        int totalWidth = 0;
        for (int i = 0; i < getItemCount(); i++) {
            // 遍历Recycler中保存的View取出来
            View view = recycler.getViewForPosition(i);
            addView(view); // 因为刚刚进行了detach操作，所以现在可以重新添加
            int x = i % cloums;
            int y = i / cloums;
            measureChildWithMargins(view, 0, 0); // 通知测量view的margin值
            int width = getDecoratedMeasuredWidth(view); // 计算view实际大小，包括了ItemDecorator中设置的偏移量。
            int height = getDecoratedMeasuredHeight(view);
            Rect mTmpRect = allItemRects.get(i);
            if (mTmpRect == null) {
                mTmpRect = new Rect();
            }
            mTmpRect.set(totalWidth, totalHeight, totalWidth + width, totalHeight + height);
            totalWidth += width;
            if (i % cloums == cloums - 1) {
                totalHeight += height;
                this.totalWidth = totalWidth;
                totalWidth = 0;
            }
            if(this.totalWidth < totalWidth)
            {
                this.totalWidth = totalWidth;
            }
            // 保存ItemView的位置信息
            allItemRects.put(i, mTmpRect);
            // 由于之前调用过detachAndScrapAttachedViews(recycler)，所以此时item都是不可见的
            itemStates.put(i, false);
        }
    }

    private void recycleAndFillView(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }

        // 当前scroll offset状态下的显示区域
        Rect displayRect = new Rect(horizontalScrollOffset, verticalScrollOffset, horizontalScrollOffset + getHorizontalSpace(),
                verticalScrollOffset + getVerticalSpace());

        /**
         * 将滑出屏幕的Items回收到Recycle缓存中
         */
        Rect childRect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            //这个方法获取的是RecyclerView中的View，注意区别Recycler中的View
            //这获取的是实际的View
            View child = getChildAt(i);
            //下面几个方法能够获取每个View占用的空间的位置信息，包括ItemDecorator
            childRect.left = getDecoratedLeft(child);
            childRect.top = getDecoratedTop(child);
            childRect.right = getDecoratedRight(child);
            childRect.bottom = getDecoratedBottom(child);
            //如果Item没有在显示区域，就说明需要回收
            if (!Rect.intersects(displayRect, childRect)) {
                //移除并回收掉滑出屏幕的View
                removeAndRecycleView(child, recycler);
                itemStates.put(i, false); //更新该View的状态为未依附
            }
        }

        //重新显示需要出现在屏幕的子View
        for (int i = 0; i < getItemCount(); i++) {
            //判断ItemView的位置和当前显示区域是否重合
            if (Rect.intersects(displayRect, allItemRects.get(i))) {
                //获得Recycler中缓存的View
                View itemView = recycler.getViewForPosition(i);
                measureChildWithMargins(itemView, 0, 0);
                //添加View到RecyclerView上
                addView(itemView);
                //取出先前存好的ItemView的位置矩形
                Rect rect = allItemRects.get(i);
                //将这个item布局出来
                layoutDecorated(itemView,
                        rect.left - horizontalScrollOffset,
                        rect.top - verticalScrollOffset,  //因为现在是复用View，所以想要显示在
                        rect.right - horizontalScrollOffset,
                        rect.bottom - verticalScrollOffset);
                mLastVisiableView = itemView;
                mLastVisiableIndex = i;
                itemStates.put(i, true);
            }
        }
    }


    @Override
    public boolean canScrollVertically() {
        //返回true表示可以纵向滑动
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //列表向下滚动dy为正，列表向上滚动dy为负，这点与Android坐标系保持一致。
        //实际要滑动的距离
        int travel = dy;
        //如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {//如果滑动到最底部
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }

        //将竖直方向的偏移量+travel
        verticalScrollOffset += travel;

        // 调用该方法通知view在y方向上移动指定距离
        offsetChildrenVertical(-travel);
        recycleAndFillView(recycler, state);
        return travel;
    }

    private int getVerticalSpace() {
        //计算RecyclerView的可用高度，除去上下Padding值
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    @Override
    public boolean canScrollHorizontally() {
        //返回true表示可以横向滑动
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //在这个方法中处理水平滑动
        int travel = dx;
        //如果滑动到最顶部
        if (horizontalScrollOffset + dx < 0) {
            travel = -horizontalScrollOffset;
        } else if (horizontalScrollOffset + dx > totalWidth - getHorizontalSpace()) {//如果滑动到最底部
            travel = totalWidth - getHorizontalSpace() - horizontalScrollOffset;
        }

        //将竖直方向的偏移量+travel
        horizontalScrollOffset += travel;

        // 调用该方法通知view在y方向上移动指定距离
        offsetChildrenHorizontal(-travel);
        recycleAndFillView(recycler, state);
        return travel;
    }

    private int getHorizontalSpace() {
        //计算RecyclerView的可用高度，除去上下Padding值
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public View getLastVisiableView() {
        return mLastVisiableView;
    }
    public int getLastVisiableIndex() {
        return mLastVisiableIndex;
    }
}