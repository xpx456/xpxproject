package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.SailPresenter;

import intersky.appbase.BaseActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class SailActivity extends BaseActivity {

    public static final String ACTION_SAIL_APPLY_SUCCESS = "ACTION_SAIL_APPLY_SUCCESS";
    public SailPresenter mSailPresenter = new SailPresenter(this);
    public ListView listView;
    public ImageView back;
    public RelativeLayout desBtn;
    public RelativeLayout memberBtn;
    public RelativeLayout complaintBtn;
    public TextView btnApply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSailPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSailPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener desListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSailPresenter.showDes();
        }
    };

    public View.OnClickListener memberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSailPresenter.showMember();
        }
    };

    public View.OnClickListener complaintListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSailPresenter.showComplaint();
        }
    };

    public View.OnClickListener applySailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSailPresenter.doApplay();
        }
    };
}
