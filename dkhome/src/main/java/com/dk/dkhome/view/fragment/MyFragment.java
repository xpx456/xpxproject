package com.dk.dkhome.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dk.dkhome.R;
import com.dk.dkhome.view.activity.MainActivity;

import intersky.appbase.BaseFragment;

public class MyFragment extends BaseFragment {

//    public MainPresenter mMainPresenter;
    public MainActivity mMainActivity;


    public MyFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_my, container, false);
        return mView;
    }

}
