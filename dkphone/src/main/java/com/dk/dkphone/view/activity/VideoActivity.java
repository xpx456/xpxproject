package com.dk.dkphone.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dk.dkphone.presenter.VideoPresenter;
import com.dk.dkphone.view.adapter.FlipAdapter;

import intersky.mywidget.flipview.FlipView;
import intersky.mywidget.flipview.OverFlipMode;

public class VideoActivity extends PadBaseActivity implements  FlipView.OnFlipListener, FlipView.OnOverFlipListener  {

    public VideoPresenter mVideoPresenter = new VideoPresenter(this);
    public RecyclerView listview;
    public FlipView mFlipView;
    public FlipAdapter mAdapter;
    public View exit;
    public TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoPresenter.Create();
    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {

    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }
}