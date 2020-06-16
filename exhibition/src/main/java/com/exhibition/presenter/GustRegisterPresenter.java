package com.exhibition.presenter;

import com.exhibition.view.activity.GustRegisterActivity;

import intersky.appbase.Presenter;

public class GustRegisterPresenter implements Presenter {

    public GustRegisterActivity mGustRegisterActivity;

    public GustRegisterPresenter(GustRegisterActivity GustRegisterActivity) {
        mGustRegisterActivity = GustRegisterActivity;
    }

    @Override
    public void initView() {

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

    }
}
