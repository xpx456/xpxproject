package com.exhibition.presenter;

import com.exhibition.view.activity.SettingActivity;

import intersky.appbase.Presenter;

public class SettingPresenter implements Presenter {

    public SettingActivity mSettingActivity;

    public SettingPresenter(SettingActivity SettingActivity) {
        mSettingActivity = SettingActivity;
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
