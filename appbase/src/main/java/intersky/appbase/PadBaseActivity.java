package intersky.appbase;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import intersky.apputils.WaitDialog;


public class PadBaseActivity extends Activity implements GestureDetector.OnGestureListener, View.OnTouchListener {
    public PadBasePresenter mBasePresenter = new PadBasePresenter(this);
    public FragmentTabAdapter tabAdapter;
    public WaitDialog waitDialog;
    public GestureDetector mGestureDetector;
    public Toolbar mActionBar;
    public boolean isdestory = false;
    public boolean flagFillBack = true;
    public BaseReceiver baseReceiver;
    public PermissionResult permissionRepuest;
    public String bpromisssion = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBasePresenter.Create();

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
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
		if(mBasePresenter.isAppOnForeground())
		{
            AppActivityManager.getAppActivityManager(this).isActivity = true;
//		    if(AppActivityManager.getAppActivityManager(this).msetBudge != null)
//                AppActivityManager.getAppActivityManager(this).msetBudge.setBudge();
		}
		else
		{

		}

    }

    @Override
    protected void onStop() {
        super.onStop();
		if(mBasePresenter.isAppOnForeground())
		{
		}
		else
		{
            AppActivityManager.getAppActivityManager(this).isActivity = false;
		}
    }

    @Override
    protected void onResume() {
        mBasePresenter.Resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mBasePresenter.Pause();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(mBasePresenter.onKeyDown(keyCode, event))
        {
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }

    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }
    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gettouch();
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
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return mBasePresenter.onFling(motionEvent, motionEvent1, v, v1);
    }

    public void setBaseReceiver(BaseReceiver baseReceiver) {
        this.baseReceiver = baseReceiver;
    }

    public void setBaseReceiver(BaseReceiver baseReceiver,String bpromisssion) {
        this.baseReceiver = baseReceiver;
        this.bpromisssion = bpromisssion;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mBasePresenter.oPermissionsRequest(requestCode,grantResults);
    }

    public void gettouch() {

    }


}
