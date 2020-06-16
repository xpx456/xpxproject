package com.exhibition.presenter;

import com.exhibition.view.activity.VeinCollectionActivity;

import intersky.appbase.Presenter;

public class VeinCollectionPresenter implements Presenter {

    public VeinCollectionActivity mVeinCollectionActivity;

    public VeinCollectionPresenter(VeinCollectionActivity VeinCollectionActivity) {
        mVeinCollectionActivity = VeinCollectionActivity;
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
