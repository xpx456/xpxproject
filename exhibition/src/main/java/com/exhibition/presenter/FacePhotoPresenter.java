package com.exhibition.presenter;

import com.exhibition.view.activity.FacePhotoActivity;

import intersky.appbase.Presenter;

public class FacePhotoPresenter implements Presenter {

    public FacePhotoActivity mFacePhotoActivity;

    public FacePhotoPresenter(FacePhotoActivity FacePhotoActivity) {
        mFacePhotoActivity = FacePhotoActivity;
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
