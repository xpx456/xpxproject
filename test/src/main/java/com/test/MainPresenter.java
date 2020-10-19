package com.test;

import intersky.appbase.Presenter;
import intersky.mywidget.flipview.FlipView;
import intersky.mywidget.flipview.FlipView;
import intersky.mywidget.flipview.OverFlipMode;

public class MainPresenter implements Presenter, FlipAdapter.Callback, FlipView.OnFlipListener, FlipView.OnOverFlipListener {

    public MainActivity mainActivity;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void initView() {
        mainActivity.flagFillBack = false;
        mainActivity.setContentView(R.layout.activity_main);

        mainActivity.mFlipView = (FlipView) mainActivity.findViewById(R.id.flip_view);
        mainActivity.mAdapter = new FlipAdapter(mainActivity);
        mainActivity.mAdapter.setCallback(this);
        mainActivity.mFlipView.setAdapter(mainActivity.mAdapter);
        mainActivity.mFlipView.setOnFlipListener(this);
        mainActivity.mFlipView.peakNext(false);
        mainActivity.mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        mainActivity.mFlipView.setEmptyView(mainActivity.findViewById(R.id.empty_view));
        mainActivity.mFlipView.setOnOverFlipListener(this);
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

    @Override
    public void onPageRequested(int page) {

    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {

    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }
}
