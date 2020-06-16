package intersky.appbase;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import intersky.appbase.bus.Bus;
import intersky.apputils.WaitDialog;
import xpx.com.toolbar.utils.ToolBarHelper;


public class BaseActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, View.OnTouchListener {
    public BasePresenter mBasePresenter = new BasePresenter(this);
    public FragmentTabAdapter tabAdapter;
    public WaitDialog waitDialog;
    public ToolBarHelper mToolBarHelper;
    public GestureDetector mGestureDetector;
    public Toolbar mActionBar;
    public boolean isdestory = false;
    public boolean flagFillBack = true;
    public BaseReceiver baseReceiver;
    public long clickTime = 0;
    public PermissionResult permissionRepuest;
    public String rid;
    public int decorviewHeight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBasePresenter.Create();

    }

    @Override
    public void setContentView(int layoutResID) {

        mBasePresenter.setToolBar(layoutResID);
        mBasePresenter.setGesture(layoutResID);

    }

    @Override
    protected void onDestroy() {
        mBasePresenter.Destroy();
        waitDialog.dismiss();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        mBasePresenter.Start();
        super.onStart();
        if (mBasePresenter.isAppOnForeground()) {
            AppActivityManager.getAppActivityManager(this).isActivity = true;
        } else {

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBasePresenter.isAppOnForeground()) {
            Log.d("background", "前台");
        } else {
            Log.d("background", "后台");
            AppActivityManager.getAppActivityManager(this).isActivity = false;
        }
    }

    @Override
    protected void onResume() {
        mBasePresenter.Resume();
        Bus.callData(this, "app/MoResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        mBasePresenter.Pause();
        Bus.callData(this, "app/MoPause");
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mBasePresenter.onKeyDown(keyCode, event)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
    }

    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }
    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return mBasePresenter.onFling(motionEvent, motionEvent1, v, v1);
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }


    public void setBaseReceiver(BaseReceiver baseReceiver) {
        this.baseReceiver = baseReceiver;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mBasePresenter.oPermissionsRequest(requestCode, grantResults);
    }
    public void measureStatubar(BaseActivity context,RelativeLayout statubar) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) statubar.getLayoutParams();
        params.height = context.mToolBarHelper.statusBarHeight;
        statubar.setLayoutParams(params);
    }

    public boolean isfullScreen() {
//        if ((this.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
//                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
//            return true;
//        } else {
//            return false;
//        }
        TypedValue typedValue = new TypedValue();
        this.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowFullscreen}).getValue(0, typedValue);
        if (typedValue.type == TypedValue.TYPE_INT_BOOLEAN) {
            if (typedValue.data != 0) {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }


}
