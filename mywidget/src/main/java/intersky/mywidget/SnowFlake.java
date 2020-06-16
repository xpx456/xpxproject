package intersky.mywidget;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

/**
 * Created by xpx on 2017/12/13.
 */

public class SnowFlake {

    private static final float ANGE_RANGE = 0.9f; // 角度范围
    private static final float HALF_ANGLE_RANGE = ANGE_RANGE / 2f; // 一般的角度
    private static final float HALF_PI = (float) Math.PI / 2f; // 半PI
    private static final float ANGLE_SEED = 25f; // 角度随机种子
    private static final float ANGLE_DIVISOR = 10000f;
    // 雪花的移动速度
    private static final float INCREMENT_LOWER = 2f;
    private static final float INCREMENT_UPPER = 4f;

    // 雪花的大小
    private static final float FLAKE_SIZE_LOWER = 15f;
    private static final float FLAKE_SIZE_UPPER = 25f;

    private final RandomGenerator mRandom; // 随机控制器
    private final Point mPosition; // 雪花位置
    private float mAngle; // 角度
    private final float mIncrement; // 雪花的速度
    private final float mFlakeSize; // 雪花的大小
    private final Paint mPaint; // 画笔
    private final int mDrawType; //形状
    private final int shapecolor; //颜色
    private final int ratSize;
    private int startRat = 0;
    public boolean inside = false;
    private SnowFlake(RandomGenerator random, Point position, float angle, float increment
            , float flakeSize, Paint paint, int mType, int color, int rat) {
        mRandom = random;
        mPosition = position;
        mIncrement = increment;
        mFlakeSize = flakeSize;
        mPaint = paint;
        mAngle = angle;
        mDrawType = mType;
        shapecolor = color;
        inside = true;
        ratSize = rat;
    }

    public static SnowFlake create(int width, int height, Paint paint) {
        RandomGenerator random = new RandomGenerator();
        int x = random.getRandom(width);
        int y = 0;
        Point position = new Point(x, y);
        float angle = random.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
        float increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER)+8;
        float flakeSize = random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        int rat = random.getRandom(111)+50;
        int type = random.getRandom(2);
        int color = 0xff000000 | random.getRandom(0x00ffffff);
        return new SnowFlake(random, position, angle, increment, flakeSize, paint,type,color,rat);
    }

    // 绘制雪花
    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        move(width, height);
        mPaint.setColor(shapecolor);
        if(mDrawType == 0)
        canvas.drawCircle(mPosition.x, mPosition.y, mFlakeSize, mPaint);
        else if(mDrawType == 1)
        {
            RectF rect = new RectF(mPosition.x - mFlakeSize, mPosition.y - mFlakeSize, mPosition.x
                    + mFlakeSize, mPosition.y + mFlakeSize);
            canvas.drawArc(rect, startRat, ratSize, true, mPaint);
        }
    }

    // 移动雪花
    private void move(int width, int height) {
        //x水平方向，那么需要晃动，主要设置这个值就可以，现在取消晃动了
        //如果 mPosition.x不加上后面那个值，就不会晃动了
        double x = mPosition.x + (mIncrement * Math.cos(mAngle));
        //y是竖直方向，就是下落
        double y = mPosition.y + (mIncrement * Math.sin(mAngle));

        mAngle += mRandom.getRandom(-ANGLE_SEED, ANGLE_SEED) / ANGLE_DIVISOR;
        startRat += 10;
        if(startRat >= 360)
        {
            startRat = 0;
        }
        //这个是设置雪花位置，如果在很短时间内刷新一次，就是连起来的动画效果
        mPosition.set((int) x, (int) y);
        inside = isInside(width, height);
        // 移除屏幕, 重新开始
//        if (!isInside(width, height)) {
//            // 重置雪花
//            reset(width);
//        }
    }

    // 判断是否在其中
    private boolean isInside(int width, int height) {
        int x = mPosition.x;
        int y = mPosition.y;
        if(mDrawType == 0)
        return y >= -mFlakeSize - 1 && y - mFlakeSize < height;
        else if(mDrawType == 1)
            return y >= -60 && y - 60 < height;
        else
            return y >= -20 && y - 20 < height;
    }

    // 重置雪花
    private void reset(int width) {
        mPosition.x = mRandom.getRandom(width);
        mPosition.y = (int) (-mFlakeSize - 1); // 最上面
        mAngle = mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
    }
}
