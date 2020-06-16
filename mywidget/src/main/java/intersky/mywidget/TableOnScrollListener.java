package intersky.mywidget;


import androidx.recyclerview.widget.RecyclerView;

public class TableOnScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == recyclerView.SCROLL_STATE_IDLE) {
//            recyclerView.removeOnScrollListener(this);
        }
    }

}
