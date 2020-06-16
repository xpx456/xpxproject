package intersky.mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class MyHScrollView extends HorizontalScrollView {

    private ScrollViewHListener scrollViewHListener = null;

    public MyHScrollView(Context context) {
        super(context);
    }

    public MyHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewHListener scrollViewListener) {
        this.scrollViewHListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewHListener != null) {
            scrollViewHListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

}