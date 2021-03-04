package intersky.mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MapContainer extends RelativeLayout {

    private ScrollView scrollView;

    public MapContainer(Context context) {
        super(context);
    }

    public void setScrollView(ScrollView scrollView) {

        this.scrollView = scrollView;

    }

    public MapContainer(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_UP) {

            scrollView.requestDisallowInterceptTouchEvent(false);

        } else {

            scrollView.requestDisallowInterceptTouchEvent(true);

        }
        return false;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;

    }

}
