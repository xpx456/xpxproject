package com.exhibition.presenter;

import com.exhibition.view.activity.GustDetialActivity;

import intersky.appbase.Presenter;

public class GustDetialPresenter implements Presenter {

    public GustDetialActivity mGustDetialActivity;

    public GustDetialPresenter(GustDetialActivity GustDetialActivity) {
        mGustDetialActivity = GustDetialActivity;
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
