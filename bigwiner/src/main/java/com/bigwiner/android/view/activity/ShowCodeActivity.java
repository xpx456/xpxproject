package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigwiner.android.presenter.ShowCodePresenter;
import com.bigwiner.android.presenter.ShowCodePresenter;

import intersky.appbase.BaseActivity;
import intersky.mywidget.CircleImageView;

/**
 * Created by xpx on 2017/8/18.
 */

public class ShowCodeActivity extends BaseActivity {

    public ShowCodePresenter mShowCodePresenter = new ShowCodePresenter(this);
    public ImageView code;
    public CircleImageView headimg;
    public TextView name;
    public TextView position;
    public ImageView back;
    public String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShowCodePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mShowCodePresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
