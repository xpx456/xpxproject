package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.presenter.SelectPresenter;

import intersky.appbase.BaseActivity;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.SearchViewLayout;
import intersky.select.entity.Select;
import intersky.select.view.adapter.SelectAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class SelectActivity extends BaseActivity {

    public ImageView back;
    public ListView selectView;
    public TextView title;
    public SearchViewLayout searchView;
    public RelativeLayout location;
    public SelectAdapter mSelectAdapter;
    public SelectAdapter mSearchAdapter;
    public MySlideBar msbar;
    public TextView mlocationtitle;
    public TextView mlocationvalue;
    public TextView mLetterText;
    public TextView btnOk;
    public RelativeLayout mRelativeLetter;
    public boolean cancleable = false;
    public SelectPresenter mSelectPresenter = new SelectPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSelectPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSelectPresenter.doBack();
        }
    };

    public View.OnClickListener mSaveListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mSelectPresenter.doCallBack();
        }
    };

    public View.OnClickListener mLocationListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mSelectPresenter.doLocationCallBack();
        }
    };

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSelectPresenter.doItemClick((Select) parent.getAdapter().getItem(position));
        }
    };

    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                mSelectPresenter.doSearch(searchView.getText());

            }
            return true;
        }
    };

    public MySlideBar.OnTouchLetterChangeListenner mOnTouchLetterChangeListenner = new MySlideBar.OnTouchLetterChangeListenner()
    {

        @Override
        public void onTouchLetterChange(MotionEvent event, int s)
        {
            // TODO Auto-generated method stub
            mSelectPresenter.LetterChange(s);

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mSelectPresenter.onKeyDown(keyCode, event)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return mSelectPresenter.onFling(motionEvent, motionEvent1, v, v1);
    }
}
