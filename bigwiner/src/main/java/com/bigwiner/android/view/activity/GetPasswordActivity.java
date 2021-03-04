package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigwiner.android.presenter.GetPasswordPresenter;

import intersky.appbase.BaseActivity;

public class GetPasswordActivity extends BaseActivity {


    public static final int EVENT_UPDATA_CODE_SECOND = 301004;
    public static final int CODE_SECOND = 60;
    public GetPasswordPresenter mGetPasswordPresenter = new GetPasswordPresenter(this);
    public TextView btnGetPassword;
    public int second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetPasswordPresenter.Create();
    }

    @Override
    protected void onDestroy()
    {
        mGetPasswordPresenter.Destroy();
        super.onDestroy();
    }



    public View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };
}
