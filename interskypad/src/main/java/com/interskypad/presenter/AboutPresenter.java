package com.interskypad.presenter;

import android.view.View;
import android.widget.RelativeLayout;

import com.interskypad.R;
import com.interskypad.view.activity.AboutActivity;

import intersky.appbase.Presenter;

public class AboutPresenter implements Presenter {

    public AboutActivity mAboutActivity;

    public AboutPresenter(AboutActivity mAboutActivity) {
        this.mAboutActivity = mAboutActivity;

    }

    @Override
    public void initView() {
        mAboutActivity.setContentView(R.layout.activity_about);
        RelativeLayout back = mAboutActivity.findViewById(R.id.about_head_back_layer);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAboutActivity.finish();
            }
        });
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

