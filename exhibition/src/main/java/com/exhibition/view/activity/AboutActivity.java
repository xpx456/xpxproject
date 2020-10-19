package com.exhibition.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.exhibition.R;
import com.exhibition.entity.Page;
import com.exhibition.presenter.AboutPresenter;
import com.exhibition.view.adapter.FlipAdapter;
import com.exhibition.view.adapter.GallyPageAdapter;
import com.exhibition.view.adapter.LittleGallyAdapter;
import com.exhibition.view.adapter.LittleGallyIdAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;
import intersky.mywidget.flipview.FlipView;
import intersky.mywidget.flipview.OverFlipMode;

public class AboutActivity extends PadBaseActivity implements FlipAdapter.Callback, FlipView.OnFlipListener, FlipView.OnOverFlipListener  {

    public AboutPresenter mAboutPresenter = new AboutPresenter(this);
    public LittleGallyAdapter mLittleViewPagerAdapter;
    public LittleGallyIdAdapter mLittleViewPagerIdAdapter;
    public RecyclerView listview;
    public FlipView mFlipView;
    public FlipAdapter mAdapter;
    public ArrayList<Integer> ids =new ArrayList<Integer>();
    public ArrayList<Page> pages = new ArrayList<Page>();
    public TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAboutPresenter.Create();
    }

    @Override
    public void onPageRequested(int page) {

    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {
        if(mLittleViewPagerAdapter != null)
        {
            mLittleViewPagerAdapter.currentid = position;
            mLittleViewPagerAdapter.notifyDataSetChanged();
        }
        if(mLittleViewPagerIdAdapter != null)
        {
            mLittleViewPagerIdAdapter.currentid = position;
            mLittleViewPagerIdAdapter.notifyDataSetChanged();
        }
        listview.scrollToPosition(position);
    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }
}
