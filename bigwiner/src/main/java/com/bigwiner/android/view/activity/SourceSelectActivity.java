package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bigwiner.android.presenter.SourceSelectPresenter;
import com.bigwiner.android.view.adapter.SourceAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.SearchViewLayout;
import intersky.select.entity.Select;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceSelectActivity extends BaseActivity {

    public static final int MAX_SELECT = 3;
    public SourceSelectPresenter mSourceSelectPresenter = new SourceSelectPresenter(this);
    public ListView listView;
    public ImageView back;
    public SourceAdapter sourcePortAdaptert;
    public SourceAdapter sourcePositionAdapter;
    public SourceAdapter sourceTypeAdapter;
    public SourceAdapter sourceAreaAdapter;
    public SourceAdapter sourceSearchPortAdaptert;
    public SourceAdapter sourceSearchPositionAdapter;
    public SourceAdapter sourceSearchTypeAdapter;
    public SourceAdapter sourceSearchAreaAdapter;
    public ArrayList<Select> position = new ArrayList<Select>();
    public ArrayList<Select> ports = new ArrayList<Select>();
    public ArrayList<Select> types = new ArrayList<Select>();
    public ArrayList<Select> areas = new ArrayList<Select>();
//    public SourceAdapter sourceSericeAdapter;
//    public SourceAdapter sourceRoutesAdapter;
    public TextView btnPosition;
    public TextView btnPort;
    public TextView btnSerice;
    public TextView btnRoutes;
    public ConversationPageAdapter mLoderPageAdapter;
    public NoScrollViewPager mNoScrollViewPager;
    public ArrayList<View> mViews = new ArrayList<View>();
    public ArrayList<ListView> listViews = new ArrayList<ListView>();
    public ArrayList<SearchViewLayout> searchViewLayouts = new ArrayList<SearchViewLayout>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSourceSelectPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSourceSelectPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSourceSelectPresenter.onItemClick((SourceAdapter) parent.getAdapter(),(Select) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener onTebClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceSelectPresenter.clickTeb(v.getId());
        }
    };

    public SearchViewLayout.DoTextChange mOnPositionSearchActionListener = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {
            mSourceSelectPresenter.doSearchPosition(searchViewLayouts.get(0).getText().toString());
        }

    };

    public SearchViewLayout.DoTextChange mOnAreaSearchActionListener = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {
            mSourceSelectPresenter.doSearchArea(searchViewLayouts.get(3).getText().toString());
        }
    };

    public SearchViewLayout.DoTextChange mOnPortSearchActionListener = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {
            mSourceSelectPresenter.doSearchPort(searchViewLayouts.get(1).getText().toString());
        }

    };

    public SearchViewLayout.DoTextChange mOnTypeSearchActionListener = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {
            mSourceSelectPresenter.doSearchType(searchViewLayouts.get(2).getText().toString());
        }

    };

    public SearchViewLayout.DoCancle positionClean = new SearchViewLayout.DoCancle() {

        @Override
        public void doCancle() {
            listViews.get(0).setAdapter(sourcePositionAdapter);
        }
    };

    public SearchViewLayout.DoCancle portClean = new SearchViewLayout.DoCancle() {

        @Override
        public void doCancle() {
            listViews.get(1).setAdapter(sourcePortAdaptert);
        }
    };

    public SearchViewLayout.DoCancle typeClean = new SearchViewLayout.DoCancle() {

        @Override
        public void doCancle() {
            listViews.get(2).setAdapter(sourceTypeAdapter);
        }
    };

    public SearchViewLayout.DoCancle areaClean = new SearchViewLayout.DoCancle() {

        @Override
        public void doCancle() {
            listViews.get(3).setAdapter(sourceAreaAdapter);
        }
    };
}
