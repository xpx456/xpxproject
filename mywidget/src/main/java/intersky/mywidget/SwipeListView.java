package intersky.mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;


/**
 * 作者: hewenyu
 * 日期: 2018/4/23 17:06
 * 说明:
 */

public class SwipeListView extends ListView implements AbsListView.OnScrollListener {

    public SwipeListView(Context context) {
        this(context, null);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
    }

    // 监听 listview 的滚动
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {

            if (getAdapter() instanceof SwipeAdapter) {
                SwipeAdapter adapter = (SwipeAdapter) getAdapter();
                if (adapter.mLastMenu != null && adapter.mLastMenu.isOpen()) {
                    adapter.mLastMenu.closeMenu();
                }
            }

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
