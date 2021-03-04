package intersky.mywidget.scollpick.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import intersky.mywidget.R;
import intersky.mywidget.conturypick.PinyinUtil;
import intersky.mywidget.scollpick.IPickerViewOperation;
import intersky.mywidget.scollpick.util.ScreenUtil;


public class MyScrollPickerView extends RecyclerView {

    public MyScrollPickerView(@NonNull Context context) {
        super(context);
    }

    public MyScrollPickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollPickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}

