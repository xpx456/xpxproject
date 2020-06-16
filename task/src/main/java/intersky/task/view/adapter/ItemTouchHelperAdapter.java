package intersky.task.view.adapter;

/**
 * Created by xpx on 2017/11/10.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onStart(int position);

    void onEnd(int position);
}
