package intersky.schedule.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.schedule.entity.Event;
import intersky.schedule.presenter.EventEditPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class EventEditActivity extends BaseActivity {

    public static final String ACTION_SET_SCHEDULE_REMIND = "ACTION_SET_SCHEDULE_REMIND";
    public Event mEvent;
    public EditText eTxtContent;
    public RelativeLayout btnTime;
    public RelativeLayout btnRemind;
    public TextView img1;
    public TextView img2;
    public TextView img3;
    public TextView txtTime;
    public TextView txtRemind;
    public RelativeLayout btnsave;
    public EventEditPresenter mEventEditPresenter = new EventEditPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventEditPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mEventEditPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener saveLitener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mEventEditPresenter.doSave();
        }
    };

    public View.OnClickListener selectRemindListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mEventEditPresenter.doSelectRemind();
        }
    };

    public View.OnClickListener setTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mEventEditPresenter.doSetTime();
        }
    };

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return mEventEditPresenter.onFling(motionEvent, motionEvent1, v, v1);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果触及到返回键所要执行的操作是什么
            mEventEditPresenter.chekcBack();
            return true;
        }
        return super.onKeyDown  (keyCode ,event);

    }
}
