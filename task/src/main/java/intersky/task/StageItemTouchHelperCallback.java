package intersky.task;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import intersky.task.view.adapter.ItemTouchHelperAdapter;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

/**
 * Created by xpx on 2017/11/10.
 */

public class StageItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public StageItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        switch (actionState) {
            case ItemTouchHelper.ACTION_STATE_SWIPE:
                //开始删除
                break;
            case ItemTouchHelper.ACTION_STATE_DRAG:
                //开始拖拽
                mAdapter.onStart(viewHolder.getAdapterPosition());
                break;
            case ItemTouchHelper.ACTION_STATE_IDLE:
                //拖拽或删除结束，这时 viewHolder 参数为 null 。
                break;
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        mAdapter.onEnd(viewHolder.getAdapterPosition());
        super.clearView(recyclerView, viewHolder);
    }


}
