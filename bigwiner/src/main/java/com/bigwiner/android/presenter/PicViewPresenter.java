package com.bigwiner.android.presenter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.view.activity.PicViewActivity;

import java.io.IOException;
import java.io.InputStream;

import intersky.appbase.Presenter;
import intersky.mywidget.BigView;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class PicViewPresenter implements Presenter {

    public PicViewActivity mPicViewActivity;
    public PicViewPresenter(PicViewActivity mPicViewActivity)
    {
        this.mPicViewActivity = mPicViewActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mPicViewActivity, Color.argb(0, 255, 255, 255));
        mPicViewActivity.setContentView(R.layout.activity_pic_view);
        mPicViewActivity.mToolBarHelper.hidToolbar(mPicViewActivity, (RelativeLayout) mPicViewActivity.findViewById(R.id.buttomaciton));
        mPicViewActivity.measureStatubar(mPicViewActivity, (RelativeLayout) mPicViewActivity.findViewById(R.id.stutebar));
        mPicViewActivity.back = mPicViewActivity.findViewById(R.id.back);
        mPicViewActivity.back.setOnClickListener(mPicViewActivity.backListener);
        TextView title = mPicViewActivity.findViewById(R.id.title);
        title.setText(mPicViewActivity.getIntent().getStringExtra("title"));
        BigView imageView = mPicViewActivity.findViewById(R.id.image);
        try {
            InputStream is = mPicViewActivity.getResources().getAssets().open(mPicViewActivity.getIntent().getStringExtra("id"));
            imageView.setImage(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
