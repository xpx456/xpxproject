package intersky.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;


public class TextButton extends AppCompatTextView {

    public int shapetype = 0;
    public TextButton(Context context) {
        super(context);
    }

    public TextButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.TextButton);
        shapetype = mTypedArray.getInt(R.styleable.TextButton_shapeColor, 0);
    }

    public void init() {

        switch (shapetype) {
            case 0:
                this.setBackgroundResource(R.drawable.button_coner_blue);
                this.setTextColor(Color.WHITE);
                break;
            case 1:
                this.setBackgroundResource(R.drawable.button_coner_gray);
                this.setTextColor(Color.WHITE);
                break;
            case 2:
                this.setBackgroundResource(R.drawable.button_coner_green);
                this.setTextColor(Color.WHITE);
                break;
        }
        this.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        super.onDraw(canvas);
    }
}
