package intersky.mywidget;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;


import java.util.List;

/**
 * 作者: hewenyu
 * 日期: 2018/4/20 14:15
 * 说明: 需要侧滑的 adapter 继承此类
 */

public abstract class SwipeAdapter<T> extends BaseAdapter {

    public List<T> mDatas;
    public Context mContext;

    /**
     * 记录打开 menu 的item
     */
    public SwipeLayout mLastMenu;

    public SwipeAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 在 getView 方法中调用此方法
     *
     * @param convertView
     * @param position
     */
    public void init(View convertView, final int position) {
        if (convertView instanceof SwipeLayout) {
            final SwipeLayout sl = (SwipeLayout) convertView;

            // menu 的点击事件的处理
            sl.setOnMenuClickListener(new SwipeLayout.OnMenuClickListener() {
                @Override
                public void onMenuClick(View view) {

                    // item menu 的点击事件回调监听
                    if (onItemMenuClickListener != null) {
                        onItemMenuClickListener.onItemMenuClick(position, view,mDatas.get(position));
                    }

                }
            });

            // item 的点击事件的处理
            sl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (sl.isOpen()) {
                        sl.closeMenu();
                        return;
                    }

                    if (mLastMenu != null && mLastMenu.isOpen()) {
                        mLastMenu.closeMenu();
                        return;
                    }

                    // 设置点击事件的回调监听
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, v,mDatas.get(position));
                    }

                }
            });

            // 监听 menu 是否被打开，如果打开，记录该 item
            sl.setOnSwitchScrollListener(new SwipeLayout.OnSwitchScrollListener() {
                @Override
                public void onSwitchScroll(View view, boolean open) {
                    if (open) {
                        mLastMenu = (SwipeLayout) view;
                    } else {
                        mLastMenu = null;
                    }
                }
            });

            // 监听 item 开始滑动，关闭之前打开的 item
            sl.setOnStartScrollListener(new SwipeLayout.OnStartScrollListener() {
                @Override
                public void onStartScroll(View view) {
                    if (mLastMenu != null && view != mLastMenu) {
                        mLastMenu.closeMenu();
                    }
                }
            });

        }
    }

    /**
     * 添加按钮
     *
     * @param convertView
     * @param menu
     */
    public void addMenuView(View convertView, View menu) {
        if (convertView instanceof SwipeLayout) {
            SwipeLayout sl = (SwipeLayout) convertView;
            sl.addMenu(menu);
        }
    }

    /**
     * 设置滚动的延迟
     *
     * @param convertView
     * @param delay
     */
    public void setScrollDelay(View convertView, int delay) {
        if (convertView instanceof SwipeLayout) {
            SwipeLayout sl = (SwipeLayout) convertView;
            sl.setScrollDelay(delay);
        }
    }

    /**
     * 移除所有的menu
     *
     * @param convertView
     */
    public void removeAllMenu(View convertView) {
        if (convertView instanceof SwipeLayout) {
            SwipeLayout sl = (SwipeLayout) convertView;
            sl.removeAllMenu();
        }
    }

    /**
     * 移除对应下标的 menu
     *
     * @param convertView
     * @param index
     */
    public void removeMenu(View convertView, int index) {
        if (convertView instanceof SwipeLayout) {
            SwipeLayout sl = (SwipeLayout) convertView;
            sl.removeMenu(index);
        }
    }

    private OnItemMenuClickListener onItemMenuClickListener;

    public void setOnItemMenuClickListener(OnItemMenuClickListener onItemMenuClickListener) {
        this.onItemMenuClickListener = onItemMenuClickListener;
    }

    /**
     * item menu 点击事件的回调
     */
    public interface OnItemMenuClickListener {

        void onItemMenuClick(int position, View menu,Object item);

    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item 点击事件的回调
     */
    public interface OnItemClickListener {

        void onItemClick(int position, View view,Object item);

    }

}
