package intersky.mywidget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


/**
 * Created by xpx on 2017/12/4.
 */

public class MoveRelativeLayout extends RelativeLayout {

    private int _xDelta;
    private int _yDelta;
    public long tiem;
    public boolean drageable = false;
    public Handler mHander;
    public int X;
    public int Y;
    public MoveRelativeLayout(Context context) {
        super(context);
    }

    public MoveRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void ontTouch(MotionEvent event)
    {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.leftMargin = X - _xDelta;
        layoutParams.topMargin = Y - _yDelta;
        MoveRelativeLayout.this.setLayoutParams(layoutParams);      //自己继承VIEW的this

    }

    public void setDrage(int x  ,int y)
    {
        final int X = x;
        final int Y = y;
        LayoutParams lParams = (LayoutParams) getLayoutParams();
        _xDelta = X - lParams.leftMargin;
        _yDelta = Y - lParams.topMargin;
    }

}
