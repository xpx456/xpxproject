package com.dk.dkpad.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.dk.dkpad.presenter.PadFragmentBasePresenter;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseReceiver;
import intersky.appbase.PadFragmentTabAdapter;
import intersky.appbase.PermissionResult;
import intersky.apputils.WaitDialog;


public class PadFragmentBaseActivity extends FragmentActivity implements GestureDetector.OnGestureListener, View.OnTouchListener {
    public PadFragmentBasePresenter mBasePresenter = new PadFragmentBasePresenter(this);
    public PadFragmentTabAdapter tabAdapter;
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
			Log.d("background", "前台");
		}
		else
		{
			Log.d("background", "后台");
            AppActivityManager.getAppActivityManager(this).isActivity = false;
//            if(AppActivityManager.getAppActivityManager(this).msetBudge != null)
//                AppActivityManager.getAppActivityManager(this).msetBudge.setBudge();
		}
    }

    @Override
    protected void onResume() {
        mBasePresenter.Resume();
        Intent in =new Intent();
        in.setAction("elc.view.hide");
        sendBroadcast(in);
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
}
