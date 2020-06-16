package com.interskypad.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;

import com.interskypad.R;
import com.interskypad.presenter.ServiceSettingPresenter;

import intersky.appbase.PadBaseActivity;
import intersky.mywidget.TextButton;

/**
 * Created by xpx on 2017/8/18.
 */

public class ServiceSettingActivity extends PadBaseActivity {

    public static final String ACTION_SERVICE_UPDATA = "ACTION_SERVICE_UPDATA";
    public ServiceSettingPresenter mServiceSettingPresenter = new ServiceSettingPresenter(this);
    public EditText eTxtName;
    public EditText eTxtAddress;
    public EditText eTxtPort;
    public CheckBox checkIp;
    public CheckBox chectAgent;
    public TextButton btnConfirm;
    public ScrollView root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceSettingPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mServiceSettingPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mConfirmListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mServiceSettingPresenter.doConfirm();
        }
    };

    public TextWatcher nameAndAddressChange = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(eTxtName.getText().toString().length() > 0 && eTxtAddress.getText().toString().length() > 0)
            {
                if(btnConfirm.isEnabled() == false)
                {
                    btnConfirm.setEnabled(true);
                }

            }
            else
            {
                if(btnConfirm.isEnabled() == true)
                {
                    btnConfirm.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public CompoundButton.OnCheckedChangeListener mIpAndAgentCheckListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(buttonView.getId() == R.id.ipcheck)
            {
                if(isChecked)
                {
                    if(chectAgent.isChecked() == true)
                    chectAgent.setChecked(false);
                }
                else
                {
                    if(chectAgent.isChecked() == false)
                        chectAgent.setChecked(true);
                }
            }
            else
            {
                if(isChecked)
                {
                    if(chectAgent.isChecked() == true)
                        checkIp.setChecked(false);
                }
                else
                {
                    if(chectAgent.isChecked() == false)
                        checkIp.setChecked(true);
                }
            }

        }
    };

    public boolean onTouchEvent(MotionEvent event) {

        //获得触摸的坐标
        float x = event.getX();
        float y = event.getY(); switch (event.getAction())
        {
            //触摸屏幕时刻
            case MotionEvent.ACTION_DOWN:

                break;
            //触摸并移动时刻
            case MotionEvent.ACTION_MOVE:

                break;
            //终止触摸时刻
            case MotionEvent.ACTION_UP:
                if(!(root.getX() < x && root.getX()+root.getWidth() > x
                        && root.getY() < y && root.getY()+root.getHeight() > y))
                {
                    finish();
                }
                break;
        }
        return true;
    }
}
