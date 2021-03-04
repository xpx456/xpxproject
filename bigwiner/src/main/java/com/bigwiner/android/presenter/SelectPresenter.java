package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.handler.SelectHandler;
import com.bigwiner.android.receiver.SelectReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SelectActivity;

import java.util.ArrayList;
import java.util.logging.Handler;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.SearchViewLayout;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.select.view.adapter.SelectAdapter;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SelectPresenter implements Presenter {
    ;
    public SelectActivity mSelectActivity;
    public SelectHandler selectHandler;
    public SelectPresenter(SelectActivity mSelectActivity)
    {
        this.mSelectActivity = mSelectActivity;
        selectHandler = new SelectHandler(mSelectActivity);
        mSelectActivity.setBaseReceiver(new SelectReceiver(selectHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSelectActivity, Color.argb(0, 255, 255, 255));
        mSelectActivity.setContentView(R.layout.activity_select);
        mSelectActivity.mToolBarHelper.hidToolbar(mSelectActivity, (RelativeLayout) mSelectActivity.findViewById(R.id.buttomaciton));
        mSelectActivity.measureStatubar(mSelectActivity, (RelativeLayout) mSelectActivity.findViewById(R.id.stutebar));
        mSelectActivity.back = mSelectActivity.findViewById(R.id.back);
        mSelectActivity.back.setOnClickListener(mSelectActivity.backListener);
        mSelectActivity.title = mSelectActivity.findViewById(R.id.titletext);

        mSelectActivity.selectView = mSelectActivity.findViewById(R.id.loder_List);
        mSelectActivity.searchView = mSelectActivity.findViewById(R.id.search);
        mSelectActivity.searchView.setOnCancleListener(doCancle);
        mSelectActivity.location = mSelectActivity.findViewById(R.id.location);
        mSelectActivity.mSelectAdapter = new SelectAdapter(mSelectActivity,SelectManager.getInstance().mSelects);
        mSelectActivity.mSearchAdapter = new SelectAdapter(mSelectActivity, SelectManager.getInstance().mSearchSelects);
        mSelectActivity.selectView.setOnItemClickListener(mSelectActivity.onItemClickListener);
        mSelectActivity.searchView.mSetOnSearchListener(mSelectActivity.mOnSearchActionListener);
        mSelectActivity.cancleable = mSelectActivity.getIntent().getBooleanExtra("cancleable",false);
        if(mSelectActivity.getIntent().getBooleanExtra("signal",false) == false)
        {
            mSelectActivity.btnOk = mSelectActivity.findViewById(R.id.ok);
            mSelectActivity.btnOk.setVisibility(View.VISIBLE);
            mSelectActivity.btnOk.setOnClickListener(mSelectActivity.mSaveListener);
            SelectManager.getInstance().mSelectsEd.clear();
            SelectManager.getInstance().mSelectsEd.addAll(SelectManager.getInstance().mList);

//            ToolBarHelper.setRightBtnText(mSelectActivity.mActionBar,mSelectActivity.mSaveListener,mSelectActivity.getString(R.string.button_word_save));
        }
        if(mSelectActivity.getIntent().getBooleanExtra("showSearch",false) == true)
        {
            mSelectActivity.searchView.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSelectActivity.selectView.getLayoutParams();
            params.addRule(RelativeLayout.BELOW,R.id.search);
            mSelectActivity.selectView.setLayoutParams(params);
        }
        else if(mSelectActivity.getIntent().getBooleanExtra("city",false) == true)
        {
            mSelectActivity.location.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSelectActivity.selectView.getLayoutParams();
            params.addRule(RelativeLayout.BELOW,R.id.location);
            mSelectActivity.selectView.setLayoutParams(params);
            mSelectActivity.msbar = (MySlideBar) mSelectActivity.findViewById(R.id.slideBar);
            mSelectActivity.msbar.setVisibility(View.VISIBLE);
            mSelectActivity.mLetterText = (TextView) mSelectActivity.findViewById(R.id.letter_text);
            mSelectActivity.mRelativeLetter = (RelativeLayout) mSelectActivity.findViewById(R.id.letter_layer);
            mSelectActivity.mlocationtitle = mSelectActivity.findViewById(R.id.locationtitle);
            mSelectActivity.mlocationvalue = mSelectActivity.findViewById(R.id.locationvalue);
            mSelectActivity.msbar.setVisibility(View.INVISIBLE);
            mSelectActivity.msbar.setOnTouchLetterChangeListenner(mSelectActivity.mOnTouchLetterChangeListenner);
            mSelectActivity.msbar.setmRelativeLayout(mSelectActivity.mRelativeLetter);
            mSelectActivity.msbar.setMletterView(mSelectActivity.mLetterText);
            if(BigwinerApplication.mApp.city != null)
            mSelectActivity.mlocationvalue.setText(BigwinerApplication.mApp.city.mName);
            mSelectActivity.location.setOnClickListener(mSelectActivity.mLocationListener);
            mSelectActivity.mSelectAdapter = new SelectAdapter(mSelectActivity,SelectManager.getInstance().mAllSelects,Color.rgb(0,0,0),Color.rgb(255,140,0));
            mSelectActivity.msbar.setAddletters(SelectManager.getInstance().measureHead());
            mSelectActivity.msbar.setVisibility(View.VISIBLE);
        }
        mSelectActivity.selectView.setAdapter(mSelectActivity.mSelectAdapter);
        mSelectActivity.title.setText(mSelectActivity.getIntent().getStringExtra("title"));
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

    public void LetterChange(int s) {
        Select model = SelectManager.getInstance().mHeaner.get(s);
        int a = SelectManager.getInstance().mAllSelects.indexOf(model);
        mSelectActivity.selectView.setSelectionFromTop(a, 0);
    }

    public void doItemClick(Select sslect)
    {
        if(sslect.mType == 0)
        {
            if(mSelectActivity.getIntent().getBooleanExtra("signal",false) == true)
            {
                if(sslect.iselect == true && mSelectActivity.cancleable == true)
                {
                    sslect.iselect = false;
                }
                else
                {
                    for(int i = 0 ; i < SelectManager.getInstance().mSelects.size() ; i ++)
                    {
                        SelectManager.getInstance().mSelects.get(i).iselect = false;
                    }
                    sslect.iselect = true;
                }
                SelectManager.getInstance().mSignal = sslect;
                doCallBack(sslect);
            }
            else
            {
                if(sslect.iselect == true)
                {
                    sslect.iselect = false;
                    SelectManager.getInstance().mSelectsEd.remove(sslect);
                }
                else
                {
                    if(SelectManager.getInstance().mSelectsEd.size() < mSelectActivity.getIntent().getIntExtra("max",9999))
                    {
                        sslect.iselect = true;
                        SelectManager.getInstance().mSelectsEd.add(sslect);
                    }
                    else
                    {
                        AppUtils.showMessage(mSelectActivity,mSelectActivity.getString(R.string.source_max)
                                +String.valueOf(mSelectActivity.getIntent().getIntExtra("max",9999))+mSelectActivity.getString(R.string.source_maxunit));
                    }
                }
                mSelectActivity.mSelectAdapter.notifyDataSetChanged();
            }
        }
    }

    public void doCallBack(Select select)
    {
        Intent intent = new Intent();
        intent.putExtra("item",select);
        intent.setAction(mSelectActivity.getIntent().getAction());
        intent.setPackage(BigwinerApplication.mApp.getPackageName());
        mSelectActivity.sendBroadcast(intent);
        mSelectActivity.finish();
    }

    public void doCallBack() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("items",SelectManager.getInstance().mSelectsEd);
        intent.setAction(mSelectActivity.getIntent().getAction());
        intent.setPackage(BigwinerApplication.mApp.getPackageName());
        mSelectActivity.sendBroadcast(intent);
        mSelectActivity.finish();
    }

    public void doLocationCallBack() {
        Intent intent = new Intent();
        intent.setAction(mSelectActivity.getIntent().getAction());
        intent.setPackage(BigwinerApplication.mApp.getPackageName());
        mSelectActivity.sendBroadcast(intent);
        mSelectActivity.finish();
    }

    public void doSearch(String keyword) {
        if(keyword.length() == 0)
        {
            if(!mSelectActivity.selectView.getAdapter().equals(mSelectActivity.mSelectAdapter)) {
                mSelectActivity.selectView.setAdapter(mSelectActivity.mSelectAdapter);
            }
        }
        else
        {
            SelectManager.getInstance().mSearchSelects.clear();
            for(int i = 0 ; i < SelectManager.getInstance().mSelects.size() ; i++) {
                Select mSelect = SelectManager.getInstance().mSelects.get(i);
                if(mSelect.mName.contains(keyword)) {
                    SelectManager.getInstance().mSearchSelects.add(mSelect);
                }
            }
            if(!mSelectActivity.selectView.getAdapter().equals(mSelectActivity.mSearchAdapter)){
                mSelectActivity.selectView.setAdapter(mSelectActivity.mSearchAdapter);
            }else {
                mSelectActivity.mSearchAdapter.notifyDataSetChanged();
            }
        }

    }

    public SearchViewLayout.DoCancle doCancle = new SearchViewLayout.DoCancle()
    {

        @Override
        public void doCancle() {
            if(!mSelectActivity.selectView.getAdapter().equals(mSelectActivity.mSelectAdapter)) {
                mSelectActivity.selectView.setAdapter(mSelectActivity.mSelectAdapter);
            }
        }
    };

    public void doBack() {
        for(int i = 0 ; i < SelectManager.getInstance().mSelects.size() ; i++)
        {
            SelectManager.getInstance().mSelects.get(i).iselect = false;
        }
        for(int i = 0 ; i < SelectManager.getInstance().mList.size() ; i++)
        {
            SelectManager.getInstance().mList.get(i).iselect = true;
        }
        mSelectActivity.finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            doBack();
            return false;
        }
        else {
            return false;
        }
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > mSelectActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mSelectActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {
            return false;
        }
        else if (e2.getX() - e1.getX() > mSelectActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mSelectActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {

            if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY() && mSelectActivity.flagFillBack == true)
            {
                doBack();
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }
}
