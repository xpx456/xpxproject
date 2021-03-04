package com.restaurant.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class GallyPageAdapter extends PagerAdapter {


    public ArrayList<View> mViews;
    public Context context;

    public GallyPageAdapter(Context context,ArrayList<View> mViews) {
        this.context = context;
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        container.addView(mViews.get(position));

        return mViews.get(position);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }
}
