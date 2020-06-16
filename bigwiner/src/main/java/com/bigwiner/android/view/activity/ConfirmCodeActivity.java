package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigwiner.android.presenter.ConfirmCodePresenter;

import intersky.appbase.BaseActivity;

public class ConfirmCodeActivity extends BaseActivity {

    public static final int EVENT_UPDATA_CODE_SECOND = 301004;
    public static final int CODE_SECOND = 60;
    public ConfirmCodePresenter mConfirmCodePresenter = new ConfirmCodePresenter(this);
    public TextView phoneNumber;
    public TextView imf1;
    public TextView imf2;
    public TextView btnConfirmCode;
    public TextView title;
    public ImageView back;
    public boolean showPassowrd = false;
    public boolean showPassowrdConfirm = false;
    public int second = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfirmCodePresenter.Create();
    }

    @Override
    protected void onDestroy()
    {
        mConfirmCodePresenter.Destroy();
        super.onDestroy();
    }



    public View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
