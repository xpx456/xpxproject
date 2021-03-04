package intersky.mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FixItemListView extends ListView {

    private int mMaxItemCount;

    public FixItemListView(Context context) {
        super(context);
    }

    public FixItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        resetListViewHeight();
    }

    public void setFixItemCount(int count){
        this.mMaxItemCount = count;
        resetListViewHeight();
    }

    private void resetListViewHeight(){
        ListAdapter  listAdapter = getAdapter();
        if(listAdapter == null || mMaxItemCount == 0 || listAdapter.getCount() == 0){
            return;
        }
        View itemView = listAdapter.getView(0, null, this);
        itemView.measure(0,0);
        int itemHeight = itemView.getMeasuredHeight();
        int itemCount = listAdapter.getCount();
        RelativeLayout.LayoutParams layoutParams = null;
        if(itemCount <= mMaxItemCount) {
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        }else{
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,itemHeight*mMaxItemCount);
        }
        setLayoutParams(layoutParams);
    }
}
