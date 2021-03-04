package com.intersky.android.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intersky.R;
import com.intersky.android.presenter.MainPresenter;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.MainActivity;

import intersky.appbase.BaseFragment;

public class MyFragment extends BaseFragment {

    public MainActivity mMainActivity;
    public TextView mHead;
    public TextView mName;
    public TextView mPhone;
    public TextView mNowUser;
    public TextView mLogout;
    public RelativeLayout mService;
    public RelativeLayout mClean;
    public RelativeLayout mAbout;


    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_my, container, false);
        String name = InterskyApplication.mApp.contactManager.mAccount.mRealName;
        if(name.length() == 0)
        {
            name = InterskyApplication.mApp.contactManager.mAccount.mUserName;
        }
        this.mName = (TextView) mView.findViewById(R.id.name);
        if(!InterskyApplication.mApp.contactManager.mAccount.mOrgName.equals("(Root)"))
        {
            if(InterskyApplication.mApp.contactManager.mAccount.mOrgName.length() > 0)
                mName.setText(name+"("+InterskyApplication.mApp.contactManager.mAccount.mOrgName+")");
            else
                mName.setText(name);
        }
        else
        {
            mName.setText(name);
        }
        this.mHead = (TextView) mView.findViewById(R.id.head);
        this.mPhone = (TextView) mView.findViewById(R.id.phone);
        if(InterskyApplication.mApp.contactManager.mAccount.mMobile.length() != 0)
        {
            this.mPhone.setVisibility(View.VISIBLE);
            this.mPhone.setText(InterskyApplication.mApp.contactManager.mAccount.mMobile);
        }
        else if(InterskyApplication.mApp.contactManager.mAccount.mPhone.length() != 0)
        {
            this.mPhone.setVisibility(View.VISIBLE);
            this.mPhone.setText(InterskyApplication.mApp.contactManager.mAccount.mPhone);
        }
        else
        {
            this.mPhone.setVisibility(View.GONE);
        }
//        this.mPhone = (TextView) mView.findViewById(R.id.phone);
//        this.mPhone.setText("");
        this.mNowUser = (TextView) mView.findViewById(R.id.nowusername);
        this.mNowUser.setText(name);
        this.mService = (RelativeLayout) mView.findViewById(R.id.servicelayer);
        this.mClean = (RelativeLayout) mView.findViewById(R.id.cleanlayer);
        this.mAbout = (RelativeLayout) mView.findViewById(R.id.aboutlayer);
        this.mLogout = (TextView) mView.findViewById(R.id.exit);
        this.mClean.setOnClickListener(mCleanListener);
        this.mAbout.setOnClickListener(mAboutListener);
        this.mService.setOnClickListener(mServiceListener);
        this.mLogout.setOnClickListener(mLogoutListener);
        return mView;
    }

    public View.OnClickListener mCleanListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.askClean();
        }
    };

    public View.OnClickListener mServiceListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.selectService();
        }
    };

    public View.OnClickListener mAboutListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.showAbout();

        }
    };

    public View.OnClickListener mLogoutListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.doLogout();
        }
    };
}
