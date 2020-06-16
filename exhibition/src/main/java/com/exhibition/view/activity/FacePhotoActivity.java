package com.exhibition.view.activity;

import android.os.Bundle;

import com.exhibition.R;
import com.exhibition.presenter.FacePhotoPresenter;

import intersky.appbase.BaseActivity;

public class FacePhotoActivity extends BaseActivity {

    public FacePhotoPresenter mFacePhotoPresenter = new FacePhotoPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facephoto);
        mFacePhotoPresenter.Create();
    }
}
