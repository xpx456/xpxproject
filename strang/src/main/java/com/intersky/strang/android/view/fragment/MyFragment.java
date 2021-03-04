package com.intersky.strang.android.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intersky.strang.R;
import com.intersky.strang.android.view.StrangApplication;
import com.intersky.strang.android.view.activity.MainActivity;

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
        String name = StrangApplication.mApp.contactManager.mAccount.mRealName;
        if(name.length() == 0)
        {
            name = StrangApplication.mApp.contactManager.mAccount.mUserName;
        }
        this.mName = (TextView) mView.findViewById(R.id.name);
        if(!StrangApplication.mApp.contactManager.mAccount.mOrgName.equals("(Root)"))
        {
            if(StrangApplication.mApp.contactManager.mAccount.mOrgName.length() > 0)
                mName.setText(name+"("+StrangApplication.mApp.contactManager.mAccount.mOrgName+")");
            else
                mName.setText(name);
        }
        else
        {
            mName.setText(name);
        }
        this.mHead = (TextView) mView.findViewById(R.id.head);
        this.mPhone = (TextView) mView.findViewById(R.id.phone);
        if(StrangApplication.mApp.contactManager.mAccount.mMobile.length() != 0)
        {
            this.mPhone.setVisibility(View.VISIBLE);
            this.mPhone.setText(StrangApplication.mApp.contactManager.mAccount.mMobile);
        }
        else if(StrangApplication.mApp.contactManager.mAccount.mPhone.length() != 0)
        {
            this.mPhone.setVisibility(View.VISIBLE);
            this.mPhone.setText(StrangApplication.mApp.contactManager.mAccount.mPhone);
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
