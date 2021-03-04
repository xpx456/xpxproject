package com.interskypad.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.interskypad.presenter.LoginPresenter;
import com.interskypad.view.InterskyPadApplication;

import intersky.appbase.PadBaseActivity;
import intersky.mywidget.TextButton;
import intersky.xpxnet.net.Service;


/**
 * Created by xpx on 2017/8/18.
 */

public class LoginActivity extends PadBaseActivity {

    public static String ACTION_UPDATA_BUDGE = "ACTION_UPDATA_BUDGE";
    public LoginPresenter mLoginPresenter = new LoginPresenter(this);
    public ArrayAdapter<Service> sAdapter;
    public Spinner mSpinner;
    public EditText eTxtAccount;
    public EditText eTxtPassword;
    public TextView btnServiceList;
    public TextButton btnLogin;
    public LinearLayout root;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.Destroy();
        super.onDestroy();
    }

    public TextWatcher accountChange = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(eTxtAccount.getText().toString().length() > 0 && InterskyPadApplication.mApp.mService != null)
            {
                if(btnLogin.isEnabled() == false)
                {
                    btnLogin.setEnabled(true);
                }

            }
            else
            {
                if(btnLogin.isEnabled() == true)
                {
                    btnLogin.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public View.OnClickListener loginListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mLoginPresenter.doLogin();
        }
    };

    public AdapterView.OnItemSelectedListener sniperItemClick = new AdapterView.OnItemSelectedListener()
    {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mLoginPresenter.onItemClick(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public View.OnClickListener serviceListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mLoginPresenter.doService();
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
