package com.exhibition.presenter;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.exhibition.R;
import com.exhibition.utils.DepthPageTransformer;
import com.exhibition.utils.GalleryTransformer;
import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.activity.AboutActivity;
import com.exhibition.view.adapter.GallyPageAdapter;
import com.exhibition.view.adapter.LittleGallyAdapter;
import com.exhibition.view.adapter.LittleGallyIdAdapter;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;

public class AboutPresenter implements Presenter {

    public AboutActivity mAboutActivity;

    public AboutPresenter(AboutActivity AboutActivity) {
        mAboutActivity = AboutActivity;
    }

    @Override
    public void initView() {
        mAboutActivity.flagFillBack = false;
        mAboutActivity.setContentView(R.layout.activity_about);
        mAboutActivity.mViewPager= (ViewPager) mAboutActivity.findViewById(R.id.des_pager);
        mAboutActivity.listview = mAboutActivity.findViewById(R.id.little_pager);
        mAboutActivity.back = mAboutActivity.findViewById(R.id.back1);
        mAboutActivity.back.setText("<"+mAboutActivity.getString(R.string.back));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mAboutActivity);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mAboutActivity.listview.setLayoutManager(linearLayoutManager);
        mAboutActivity.mViewPager.addOnPageChangeListener(onPageChangeListener);
        if(ExhibitionApplication.mApp.photos.size() > 0)
        {
            for(int i = 0 ; i < ExhibitionApplication.mApp.photos.size() ; i++)
            {
                View view = mAboutActivity.getLayoutInflater().inflate(R.layout.gally_image,null);
                ImageView imageView = view.findViewById(R.id.photo);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.temp);
                Glide.with(mAboutActivity).load(ExhibitionApplication.mApp.photos.get(i)).apply(options).into(imageView);
                mAboutActivity.views.add(view);

            }
        }
        else
        {
            for(int i = 0 ; i < 5 ; i++)
            {
                View view = mAboutActivity.getLayoutInflater().inflate(R.layout.gally_image,null);
                ImageView imageView = view.findViewById(R.id.photo);
                if(i%2 == 1)
                {
                    imageView.setImageResource(R.drawable.temp);
                    mAboutActivity.ids.add(R.drawable.temp);
                }
                else
                {
                    imageView.setImageResource(R.drawable.temp2);
                    mAboutActivity.ids.add(R.drawable.temp2);
                }
                mAboutActivity.views.add(view);
            }
        }

        mAboutActivity.mViewPagerAdapter = new GallyPageAdapter(mAboutActivity,mAboutActivity.views);
        mAboutActivity.mViewPager.setOffscreenPageLimit(3);
        mAboutActivity.mViewPager.setPageTransformer(true,new DepthPageTransformer());
        mAboutActivity.mViewPager.setAdapter(mAboutActivity.mViewPagerAdapter);
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
        mAboutActivity.back.setOnClickListener(backListener);

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

    public ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == 2)
            {
                if(ExhibitionApplication.mApp.photos.size() > 0)
                {
                    if(mAboutActivity.mLittleViewPagerAdapter.currentid != mAboutActivity.mViewPager.getCurrentItem())
                    {
                        mAboutActivity.mLittleViewPagerAdapter.currentid = mAboutActivity.mViewPager.getCurrentItem();
                        mAboutActivity.mViewPager.setCurrentItem(mAboutActivity.mViewPager.getCurrentItem());
                        mAboutActivity.mLittleViewPagerAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    if(mAboutActivity.mLittleViewPagerIdAdapter.currentid != mAboutActivity.mViewPager.getCurrentItem())
                    {
                        mAboutActivity.mLittleViewPagerIdAdapter.currentid = mAboutActivity.mViewPager.getCurrentItem();
                        mAboutActivity.mViewPager.setCurrentItem(mAboutActivity.mViewPager.getCurrentItem());
                        mAboutActivity.mLittleViewPagerIdAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    public LittleGallyAdapter.OnItemClickListener onItemClickListener = new LittleGallyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(File file, int position, View view) {
            if(mAboutActivity.mLittleViewPagerAdapter.currentid != position)
            {
                mAboutActivity.mLittleViewPagerAdapter.currentid = position;
                mAboutActivity.mViewPager.setCurrentItem(position);
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
                mAboutActivity.mViewPager.setCurrentItem(position);
                mAboutActivity.mLittleViewPagerIdAdapter.notifyDataSetChanged();
            }
        }

    };
}
