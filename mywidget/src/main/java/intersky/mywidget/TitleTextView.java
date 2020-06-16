package intersky.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

public class TitleTextView extends androidx.appcompat.widget.AppCompatTextView {

    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mViewHeight = 0;
    public int type = 1;
    public int colorbegin = -1;
    public int colorend = -1;
    private Rect mTextBound = new Rect();
    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleTextView);
        type = a.getInt(R.styleable.TitleTextView_titletype,1);
        colorbegin = a.getColor(R.styleable.TitleTextView_colorbegin,0xFF429321);
        colorend = a.getColor(R.styleable.TitleTextView_colorend, 0xFFB4EC51);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mPaint = getPaint();
        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        if(type == 1)
        {
            mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                    new int[] {  colorbegin, colorend },
                    null, Shader.TileMode.REPEAT);
        }
        else
        {
            mLinearGradient = new LinearGradient(0, 0, 0, mViewHeight,
                    new int[] {  colorbegin, colorend },
                    null, Shader.TileMode.REPEAT);
        }
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 +   mTextBound.height()/2, mPaint);
    }

}
