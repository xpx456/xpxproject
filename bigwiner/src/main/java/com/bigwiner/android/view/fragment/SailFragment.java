package com.bigwiner.android.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.BaseFragment;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.mywidget.MyGridView;

public class SailFragment extends BaseFragment {

//    public MainPresenter mMainPresenter;
    public ListView listView;
    public ImageView back;
    public RelativeLayout desBtn;
    public RelativeLayout memberBtn;
    public RelativeLayout complaintBtn;
    public TextView btnApply;
    public MainActivity mMainActivity;


    public SailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_sail, container, false);
        mMainActivity = (MainActivity) getActivity();
        measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        desBtn = mView.findViewById(R.id.des);
        memberBtn = mView.findViewById(R.id.person);
        complaintBtn = mView.findViewById(R.id.redblack);
        btnApply = mView.findViewById(R.id.join_in);

        if(BigwinerApplication.mApp.mAccount.issail)
        {
            btnApply.setVisibility(View.INVISIBLE);
        }
        btnApply.setOnClickListener(applySailListener);
        desBtn.setOnClickListener(desListener);
        memberBtn.setOnClickListener(memberListener);
        complaintBtn.setOnClickListener(complaintListener);

        return mView;
    }


    public View.OnClickListener desListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.showDes();
        }
    };

    public View.OnClickListener memberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.showMember();
        }
    };

    public View.OnClickListener complaintListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.showComplaint();
        }
    };

    public View.OnClickListener applySailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.mMainPresenter.doApplay();
        }
    };
}
