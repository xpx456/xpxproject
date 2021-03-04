package intersky.mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by xpx on 2018/3/15.
 */

public class PoorPriorityScrollView extends ScrollView {


    public PoorPriorityScrollView(Context context) {
        this(context, null);
    }

    public PoorPriorityScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoorPriorityScrollView(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1) {
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

}