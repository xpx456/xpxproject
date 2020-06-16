package intersky.mywidget;

/**
 * Created by xpx on 2016/10/11.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class ShadeLayout extends RelativeLayout {
    public ShadeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ShadeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadeLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return true;
    }

}
