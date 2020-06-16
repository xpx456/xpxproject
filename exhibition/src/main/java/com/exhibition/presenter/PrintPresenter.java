package com.exhibition.presenter;

import com.exhibition.view.activity.PrintActivity;

import intersky.appbase.Presenter;

public class PrintPresenter implements Presenter {

    public PrintActivity mPrintActivity;

    public PrintPresenter(PrintActivity PrintActivity) {
        mPrintActivity = PrintActivity;
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
