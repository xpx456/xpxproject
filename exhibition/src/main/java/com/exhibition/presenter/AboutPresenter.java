package com.exhibition.presenter;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.exhibition.R;
import com.exhibition.entity.Page;
import com.exhibition.utils.DepthPageTransformer;
import com.exhibition.utils.GalleryTransformer;
import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.activity.AboutActivity;
import com.exhibition.view.adapter.FlipAdapter;
import com.exhibition.view.adapter.GallyPageAdapter;
import com.exhibition.view.adapter.LittleGallyAdapter;
import com.exhibition.view.adapter.LittleGallyIdAdapter;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.mywidget.flipview.FlipView;
import intersky.mywidget.flipview.OverFlipMode;

public class AboutPresenter implements Presenter {

    public AboutActivity mAboutActivity;

    public AboutPresenter(AboutActivity AboutActivity) {
        mAboutActivity = AboutActivity;
    }

    @Override
    public void initView() {
        mAboutActivity.flagFillBack = false;
        mAboutActivity.setContentView(R.layout.activity_about);

        mAboutActivity.mFlipView = (FlipView) mAboutActivity.findViewById(R.id.flip_view1);


        mAboutActivity.listview = mAboutActivity.findViewById(R.id.little_pager);
        mAboutActivity.back = mAboutActivity.findViewById(R.id.back1);
        mAboutActivity.back.setText("<"+mAboutActivity.getString(R.string.back));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mAboutActivity);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mAboutActivity.listview.setLayoutManager(linearLayoutManager);

        mAboutActivity.back.setOnClickListener(backListener);
        mAboutActivity.mAdapter = new FlipAdapter(mAboutActivity,mAboutActivity.pages);
        mAboutActivity.mFlipView.setAdapter(mAboutActivity.mAdapter);
        mAboutActivity.mFlipView.setOnFlipListener(mAboutActivity);
        mAboutActivity.mFlipView.peakNext(false);
        mAboutActivity.mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        mAboutActivity.mFlipView.setEmptyView(mAboutActivity.findViewById(R.id.empty_view1));
        mAboutActivity.mFlipView.setOnOverFlipListener(mAboutActivity);
        mAboutActivity.mFlipView.setFocusable(true);

        if(ExhibitionApplication.mApp.photos.size() > 0)
        {
            for(int i = 0 ; i < ExhibitionApplication.mApp.photos.size() ; i++)
            {
                Page page = new Page();
                page.filepath = ExhibitionApplication.mApp.photos.get(i).getPath();
                mAboutActivity.mAdapter.items.add(page);

            }
        }
        else
        {
            for(int i = 0 ; i < 5 ; i++)
            {
                Page page = new Page();
                if(i%2 == 0)
                {
                    page.sourceid = R.drawable.temp;
                    mAboutActivity.ids.add(R.drawable.temp);
                }
                else
                {
                    page.sourceid = R.drawable.temp2;
                    mAboutActivity.ids.add(R.drawable.temp2);
                }
                mAboutActivity.mAdapter.items.add(page);
            }
        }

        if(ExhibitionApplication.mApp.photos.size() > 0)
        {
            mAboutActivity.mLittleViewPagerAdapter = new LittleGallyAdapter(ExhibitionApplication.mApp.photos,mAboutActivity);
            mAboutActivity.mLittleViewPagerAdapter.setOnItemClickListener(onItemClickListener);
            mAboutActivity.listview.setAdapter(mAboutActivity.mLittleViewPagerAdapter);
        }
        else
        {
            mAboutActivity.mLittleViewPagerIdAdapter = new LittleGallyIdAdapter(mAboutActivity.ids,mAboutActivity);
            mAboutActivity.mLittleViewPagerIdAdapter.setOnItemClickListener(onItemIdClickListener);
            mAboutActivity.listview.setAdapter(mAboutActivity.mLittleViewPagerIdAdapter);
        }
        mAboutActivity.mAdapter.notifyDataSetChanged();
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
            mAboutActivity.finish();
        }
    };


    public LittleGallyAdapter.OnItemClickListener onItemClickListener = new LittleGallyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(File file, int position, View view) {
            if(mAboutActivity.mLittleViewPagerAdapter.currentid != position)
            {
                mAboutActivity.mLittleViewPagerAdapter.currentid = position;
                mAboutActivity.mFlipView.smoothFlipTo(position);
                mAboutActivity.mLittleViewPagerAdapter.notifyDataSetChanged();
            }
        }
    };


    public LittleGallyIdAdapter.OnItemClickListener onItemIdClickListener = new LittleGallyIdAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Object file, int position, View view) {
            if(mAboutActivity.mLittleViewPagerIdAdapter.currentid != position)
            {
                mAboutActivity.mLittleViewPagerIdAdapter.currentid = position;
                mAboutActivity.mFlipView.smoothFlipTo(position);
                mAboutActivity.mLittleViewPagerIdAdapter.notifyDataSetChanged();
            }
        }

    };
}
