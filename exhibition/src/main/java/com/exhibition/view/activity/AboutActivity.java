package com.exhibition.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bookpage.view.BookPageView;
import com.bookpage.view.CoverPageView;
import com.exhibition.R;
import com.exhibition.presenter.AboutPresenter;
import com.exhibition.view.adapter.GallyPageAdapter;
import com.exhibition.view.adapter.LittleGallyAdapter;
import com.exhibition.view.adapter.LittleGallyIdAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;

public class AboutActivity extends PadBaseActivity {

    public AboutPresenter mAboutPresenter = new AboutPresenter(this);
    public ViewPager mViewPager;
    public GallyPageAdapter mViewPagerAdapter;
    public LittleGallyAdapter mLittleViewPagerAdapter;
    public LittleGallyIdAdapter mLittleViewPagerIdAdapter;
    public RecyclerView listview;
    public ArrayList<View> views = new ArrayList<View>();
    public ArrayList<Integer> ids =new ArrayList<Integer>();
    public TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAboutPresenter.Create();
    }
}
