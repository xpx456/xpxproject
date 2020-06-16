package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.MeetingDetialPresenter;
import com.bigwiner.android.view.BigwinerApplication;

import intersky.appbase.BaseActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class MeetingDetialActivity extends BaseActivity {

    public static final String ACTION_UPDATE_JOIN_MEETING = "ACTION_UPDATE_JOIN_MEETING";

    public Meeting meeting;
    public ImageView headImg;
    public TextView titleTxt;
    public TextView companyName;
    public TextView prise1Txt;
    public TextView prise2Txt;
    public TextView timeTxt;
    public TextView addressTxt;
    public TextView personTxt;
    public TextView desTxt;
    public TextView btnContact;
    public TextView btnCheck;
    public TextView btnJoin;
    public TextView btnSign;
    public TextView btnRequest;
    public LinearLayout companyList;
    public RelativeLayout address;
    public RelativeLayout shade;
    public ImageView back;
    public ImageView share;
    public MeetingDetialPresenter mMeetingDetialPresenter = new MeetingDetialPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMeetingDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMeetingDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener checkLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMeetingDetialPresenter.doCheck();
        }
    };

    public View.OnClickListener contactLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMeetingDetialPresenter.doContact();
        }
    };

    public View.OnClickListener joinLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMeetingDetialPresenter.startJoin();
        }
    };


    public View.OnClickListener signLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMeetingDetialPresenter.showScan();
        }
    };

    public View.OnClickListener requestLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMeetingDetialPresenter.showRequest();
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener showCompanyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMeetingDetialPresenter.showCompany((Company) v.getTag());
        }
    };

    public View.OnClickListener addressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMeetingDetialPresenter.showmap();
        }
    };
}
