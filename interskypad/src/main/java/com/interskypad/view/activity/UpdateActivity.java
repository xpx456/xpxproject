package com.interskypad.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.interskypad.manager.UpdateCacheManager;
import com.interskypad.presenter.UpdatePresenter;

import intersky.appbase.PadBaseActivity;

public class UpdateActivity extends PadBaseActivity {

    public TextView mImf;
    public ProgressBar progressBar;
    public TextView progress_text;
    public TextView cancle;
    public TextView background;
    public UpdatePresenter mUpdatePresenter = new UpdatePresenter(this);
    public UpdateActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpdatePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mUpdatePresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener cancleDownloadListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mUpdatePresenter.doCancle();
        }
    };

    public View.OnClickListener backgroundListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(UpdateCacheManager.getInstance().thread != null)
            mUpdatePresenter.doBackground();
            else
            {
                mUpdatePresenter.tryAgain();
            }
        }
    };
}
