package com.dk.dkphone.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.dk.dkphone.presenter.MainPresenter;
import com.dk.dkphone.view.DkPadApplication;
import com.dk.dkphone.view.X5WebView;
import com.dk.dkphone.view.adapter.PagerMainAdapter;
import com.dk.dkphone.view.fragment.DetialSportFragment;
import com.dk.dkphone.view.fragment.HomeFragment;
import com.dk.dkphone.view.fragment.PersonFragment;

import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.mywidget.CircleImageView;
import intersky.mywidget.VerticalSeekBar;


public class MainActivity extends PadFragmentBaseActivity {
    public static final String BLUE_STATE = "BLUE_STATE";
    public static final String ACTION_SET_MAX = "ACTION_SET_MAX";
    public static final String SPEET_COLOR = "#28d9cf";
    public static final String HERT_COLOR = "#f08519";
    public static final String SELECT_COLOR = "#fc210a";
    public static final int TAKE_PHOTO_HEAD = 0x4;
    public static final int TAKE_PHOTO_BG = 0x5;
    public static final int CHOSE_PICTURE_HEAD = 0x6;
    public static final int CHOSE_PICTURE_BG = 0x7;
    public static final int CROP_HEAD = 0x8;
    public static final int CROP_BG = 0x9;
    public MainPresenter mMainPresenter = new MainPresenter(this);
    public ImageView setting;
    public PersonFragment personFragment;
    public HomeFragment homeFragment;
    public DetialSportFragment detialSportFragment;
    public ViewPager viewPager;
    public RelativeLayout head;
    public RelativeLayout optation;

    public RelativeLayout headlayer;
    public RelativeLayout oplayer;
    public PagerMainAdapter pagerMainAdapter;
    public TextView time;
    public ImageView btnStart;
    public ImageView btnAdd;
    public ImageView btnDes;
    public ImageView btnWatch;
    public ImageView btnMap;
    public ImageView btntv;
    public ImageView btnlanya;

    public RelativeLayout showlayer;
    public TextView timevalue1;
    public TextView timevalue2;
    public TextView speedvalue;
    public TextView hertvalue;
    public TextView selectvalue;
    public ImageView btnStart2;
    public ImageView btnAdd2;
    public ImageView btnDes2;
    public ImageView btntv2;

    public ImageView btnleft;
    public ImageView btnright;

    public View btnShowHead;
    public ImageView headarray;
    public View btnShowOptation;
    public ImageView optationArray;
    public ListView userList;
    public ListView optationList;
    public CircleImageView sHead;
    public TextView sHeadName;
    public ImageView sOptation;
    public TextView sOptationName;
    public Animation showActionUp;
    public Animation hideActionUp;
    public Animation showActionDown;
    public Animation hideActionDown;
    public RelativeLayout mlayoutHead;
    public X5WebView tv;
    public RelativeLayout buttomBtn;
    public ImageView youku;
    public ImageView tenxun;
    public ImageView aiqiyi;

    public ObjectData sportoption;
    public ArrayData sportx;
    public ArrayData sportLegend;
    public ArrayData sportspeed;
    public ArrayData sporthert;
    public ArrayData sportselect;
    public VerticalSeekBar power;

    public ObjectData homeoption;
    public ArrayData homeLegend;
    public ObjectData homespeed;
    public ObjectData homehert;
    public ObjectData homeselect;

    public ObjectData personoption;
    public ArrayData personopLegend;
    public ArrayData series;
    public ObjectData seriesItem;
    public ArrayData seriesData;
    public ArrayData indicator;
    public ObjectData myvalue;
    public ObjectData othervalue;
    public ObjectData personTitle;
    public boolean first = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.Destroy();
        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        mMainPresenter.Start();
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        mMainPresenter.Resume();
        super.onResume();
    }


    public WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            // 当有新连接时使用当前的webview进行显示
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        // 开始加载网页时要做的工作
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setVisibility(View.INVISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }
    };


    public X5WebView.WebclientOptation webclientOptation = new X5WebView.WebclientOptation()
    {

        @Override
        public void onPageStarted() {
            if(mMainPresenter.mMainActivity.first)
            {
                first = false;
            }
            else
            mMainPresenter.mMainActivity.waitDialog.show();
        }

        @Override
        public void onPageFinished() {
            mMainPresenter.mMainActivity.waitDialog.hide();
        }

        @Override
        public void onReceivedError() {
            mMainPresenter.mMainActivity.waitDialog.hide();
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DkPadApplication.mApp.appSetProtectTime(DkPadApplication.mApp.maxProtectSecond);
        return super.dispatchTouchEvent(ev);
    }

}
