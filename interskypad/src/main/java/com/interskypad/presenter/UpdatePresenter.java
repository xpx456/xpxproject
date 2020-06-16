package com.interskypad.presenter;

import com.interskypad.R;
import com.interskypad.handler.UpdateHandler;
import com.interskypad.manager.UpdateCacheManager;
import com.interskypad.view.activity.UpdateActivity;

import intersky.appbase.Presenter;

public class UpdatePresenter implements Presenter {

    public UpdateActivity mUpdateActivity;
    public UpdateHandler mUpdateHandler;
    public UpdatePresenter(UpdateActivity mUpdateActivity) {
        this.mUpdateActivity = mUpdateActivity;
        this.mUpdateHandler = new UpdateHandler(mUpdateActivity);
    }

    @Override
    public void initView() {
        mUpdateActivity.setContentView(R.layout.activity_update);
        mUpdateActivity.progress_text = mUpdateActivity.findViewById(R.id.progress_text);
        mUpdateActivity.background = mUpdateActivity.findViewById(R.id.btn_backgound);
        mUpdateActivity.cancle = mUpdateActivity.findViewById(R.id.btn_cancle);
        mUpdateActivity.mImf = mUpdateActivity.findViewById(R.id.updata_text);
        mUpdateActivity.progressBar = mUpdateActivity.findViewById(R.id.updata_progress);
        mUpdateActivity.progressBar.setMax(1000);
        mUpdateActivity.progressBar.setProgress(0);
        initProgerss();
        mUpdateActivity.cancle.setOnClickListener(mUpdateActivity.cancleDownloadListener);
        mUpdateActivity.background.setOnClickListener(mUpdateActivity.backgroundListener);
        UpdateCacheManager.getInstance().handler = mUpdateHandler;
        if(UpdateCacheManager.getInstance().thread == null)
        {
            UpdateCacheManager.getInstance().thread = new UpdateCacheManager.UpdataThread();
            UpdateCacheManager.getInstance().thread.start();
        }
        else
        {

        }
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
        mUpdateHandler = null;
    }

    public void initProgerss()
    {
        float c =  mUpdateActivity.progressBar.getProgress();
        float a = c / 10;
        mUpdateActivity.progress_text.setText(String.format("%.2f", a)+"%");
    }

    public void doBackground() {
        mUpdateActivity.finish();
    }

    public void tryAgain() {
        UpdateCacheManager.getInstance().thread = new UpdateCacheManager.UpdataThread();
        UpdateCacheManager.getInstance().thread.start();
        mUpdateActivity.background.setText("后台进行");
    }

    public void doCancle() {
        UpdateCacheManager.getInstance().contral.stop = true;
        mUpdateActivity.finish();
    }
}

