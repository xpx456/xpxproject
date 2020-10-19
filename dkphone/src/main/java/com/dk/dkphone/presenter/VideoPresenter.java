package com.dk.dkphone.presenter;


import android.view.View;

import com.dk.dkphone.R;
import com.dk.dkphone.entity.Page;
import com.dk.dkphone.view.DkPadApplication;
import com.dk.dkphone.view.activity.VideoActivity;
import com.dk.dkphone.view.adapter.FlipAdapter;

import intersky.appbase.Presenter;
import intersky.mywidget.flipview.FlipView;
import intersky.mywidget.flipview.OverFlipMode;

public class VideoPresenter implements Presenter {

    public VideoActivity mVideoActivity;

    public VideoPresenter(VideoActivity VideoActivity) {
        mVideoActivity = VideoActivity;
    }

    @Override
    public void initView() {
        mVideoActivity.flagFillBack = false;
        mVideoActivity.setContentView(R.layout.activity_video);
        mVideoActivity.mFlipView = (FlipView) mVideoActivity.findViewById(R.id.flip_view1);
        mVideoActivity.mAdapter = new FlipAdapter(mVideoActivity);
        mVideoActivity.mFlipView.setAdapter(mVideoActivity.mAdapter);
        mVideoActivity.mFlipView.setOnFlipListener(mVideoActivity);
        mVideoActivity.mFlipView.peakNext(false);
        mVideoActivity.mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        mVideoActivity.mFlipView.setEmptyView(mVideoActivity.findViewById(R.id.empty_view1));
        mVideoActivity.mFlipView.setOnOverFlipListener(mVideoActivity);
        mVideoActivity.mFlipView.setFocusable(true);
        mVideoActivity.exit = mVideoActivity.findViewById(R.id.btnexit);
        mVideoActivity.exit.setOnClickListener(backListener);
        if(DkPadApplication.mApp.bgs.size() > 0)
        {
            for(int i = 0 ; i < DkPadApplication.mApp.bgs.size() ; i++)
            {
                Page page = new Page();
                page.filepath = DkPadApplication.mApp.bgs.get(i).getPath();
                mVideoActivity.mAdapter.items.add(page);

            }
        }
        mVideoActivity.mAdapter.notifyDataSetChanged();
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

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mVideoActivity.finish();
        }
    };

}
