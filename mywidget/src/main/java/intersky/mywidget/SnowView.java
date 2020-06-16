package intersky.mywidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/12/13.
 */

public class SnowView extends View {

    private static final int NUM_SNOWFLAKES = 30; // 雪花数量
    private static final int DELAY = 50; // 延迟
    private ArrayList<SnowFlake> mSnowFlakes = new ArrayList<SnowFlake>(); // 雪花
    public Handler handler;
    public int event = 0;
    public SnowView(Context context) {
        super(context);
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        if (w != oldw || h != oldh) {
//            initSnow(w, h);
//        }
    }

    public void startfly()
    {
        initSnow(this.getWidth(),this.getHeight());
        invalidate();
//        getHandler().postDelayed(runnable, DELAY);
    }

    private void initSnow(int width, int height) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
//        paint.setColor(Color.WHITE); // 白色雪花
        paint.setStyle(Paint.Style.FILL); // 填充;
//        mSnowFlakes = new SnowFlake[NUM_SNOWFLAKES];
        //mSnowFlakes所有的雪花都生成放到这里面
        for (int i = 0; i < NUM_SNOWFLAKES; ++i) {
            mSnowFlakes.add(SnowFlake.create(width, height, paint));
//            mSnowFlakes[i] = SnowFlake.create(width, height, paint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //for返回SnowFlake
        for(int i = 0 ; i < mSnowFlakes.size() ; i++)
        {
            SnowFlake s = mSnowFlakes.get(i);
            if(s.inside)
            {
                s.draw(canvas);
            }
            else
            {
                mSnowFlakes.remove(s);
                if(i > 0)
                {
                    i--;
                }
            }
        }
        if(mSnowFlakes.size() == 0)
        {

        }
        else
        {
            if(handler != null)
            {
                handler.sendEmptyMessageDelayed(event,DELAY);
            }
        }
        // 隔一段时间重绘一次, 动画效果
//        else
//        getHandler().postDelayed(runnable, DELAY);


    }

    // 重绘线程
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            //自动刷新
//            if(isplay == true)
//            invalidate();
//        }
//    };
}
