package com.interskypad.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.interskypad.R;
import com.interskypad.presenter.MainPresenter;

import intersky.appbase.BaseFragment;

@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment {

    public MainPresenter mMainPresenter;
    private ImageView mBtnGuanwang;
    private ImageView mBtnWeibo;
    private ImageView mBtnWeixin;
    private RelativeLayout mRelativeLayout1;
    private RelativeLayout mRelativeLayout2;
    private RelativeLayout mHomeLayer;


    public HomeFragment(MainPresenter mMainPresenter) {
        // Required empty public constructor
        this.mMainPresenter = mMainPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        mBtnGuanwang = (ImageView) mView.findViewById(R.id.home_sub_guanwang);
        mBtnWeibo = (ImageView) mView.findViewById(R.id.home_sub_weibo);
        mBtnWeixin = (ImageView) mView.findViewById(R.id.home_sub_weixin);
        mHomeLayer = (RelativeLayout) mView.findViewById(R.id.homelayer);
        mRelativeLayout1 = (RelativeLayout) mView.findViewById(R.id.iconLayer1);
        mRelativeLayout2 = (RelativeLayout) mView.findViewById(R.id.iconLayer2);
        mRelativeLayout1.setVisibility(View.INVISIBLE);
        mRelativeLayout2.setVisibility(View.INVISIBLE);
        mBtnGuanwang.setOnClickListener(mGuanwangListener);
        mBtnWeibo.setOnClickListener(mWeiboListener);
        mBtnWeixin.setOnClickListener(mWeixinListener);
        mHomeLayer.setOnTouchListener(mHomeLayerTouch);
        return mView;
    }


    public View.OnClickListener mGuanwangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("http://www.intersky.com.cn");
            intent.setData(content_url);
            startActivity(intent);
            mRelativeLayout1.setVisibility(View.INVISIBLE);
            mRelativeLayout2.setVisibility(View.INVISIBLE);
        }
    };

    public View.OnClickListener mWeiboListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mRelativeLayout2.setVisibility(View.VISIBLE);
            mRelativeLayout1.setVisibility(View.INVISIBLE);
        }
    };

    public View.OnClickListener mWeixinListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.INVISIBLE);
        }
    };


    public View.OnTouchListener mHomeLayerTouch = new View.OnTouchListener()
    {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            // TODO Auto-generated method stub
            float x = event.getX();
            float y = event.getY();
            if(!(mBtnWeibo.getX() < x && mBtnWeibo.getX()+mBtnWeibo.getWidth() > x
                    && mBtnWeibo.getY() < y && mBtnWeibo.getY()+mBtnWeibo.getHeight() > y)
                    &&!(mBtnWeixin.getX() < x && mBtnWeixin.getX()+mBtnWeixin.getWidth() > x
                    && mBtnWeixin.getY() < y && mBtnWeixin.getY()+mBtnWeixin.getHeight() > y))
            {
                mRelativeLayout1.setVisibility(View.INVISIBLE);
                mRelativeLayout2.setVisibility(View.INVISIBLE);
            }
            return false;
        }
    };
}
