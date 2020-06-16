package com.bigwiner.android.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.handler.ShowCodeHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ShowCodeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideApp;
import intersky.apputils.GlideConfiguration;
import intersky.filetools.FileUtils;
import intersky.filetools.PathUtils;
import intersky.scan.QRCodeUtil;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ShowCodePresenter implements Presenter {

    public ShowCodeActivity mShowCodeActivity;
    public ShowCodeHandler mShowCodeHandler;
    public ShowCodePresenter(ShowCodeActivity mShowCodeActivity)
    {
        this.mShowCodeActivity = mShowCodeActivity;
        this.mShowCodeHandler = new ShowCodeHandler(mShowCodeActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mShowCodeActivity, Color.argb(0, 255, 255, 255));
        mShowCodeActivity.setContentView(R.layout.activity_code);
        mShowCodeActivity.mToolBarHelper.hidToolbar(mShowCodeActivity, (RelativeLayout) mShowCodeActivity.findViewById(R.id.buttomaciton));
        mShowCodeActivity.measureStatubar(mShowCodeActivity, (RelativeLayout) mShowCodeActivity.findViewById(R.id.stutebar));
        mShowCodeActivity.back = mShowCodeActivity.findViewById(R.id.back);
        mShowCodeActivity.back.setOnClickListener(mShowCodeActivity.backListener);
        mShowCodeActivity.code = mShowCodeActivity.findViewById(R.id.code);
        mShowCodeActivity.headimg = mShowCodeActivity.findViewById(R.id.head);
        mShowCodeActivity.name = mShowCodeActivity.findViewById(R.id.name);
        mShowCodeActivity.position = mShowCodeActivity.findViewById(R.id.position);
        mShowCodeActivity.name.setText(BigwinerApplication.mApp.mAccount.mUserName);
        mShowCodeActivity.position.setText(BigwinerApplication.mApp.mAccount.mPosition);
        mShowCodeActivity.headimg.setImageResource(R.drawable.contact_detial_head);
        RequestOptions options = new RequestOptions()
                .placeholder(com.bigwiner.R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL);
        GlideApp.with(mShowCodeActivity).load(ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mShowCodeActivity.headimg));
        File mFile = GlideConfiguration.mGlideConfiguration.getCachedFile(  ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.contactManager.updataKey),mShowCodeActivity);
        if(mFile.exists())
        {
                mShowCodeActivity.code.setImageBitmap( QRCodeUtil.createQRCodeBitmap(ContactsAsks.codeUrl(BigwinerApplication.mApp.mAccount.mRecordId),
                        (int) (mShowCodeActivity.mBasePresenter.mScreenDefine.density*270),
                        BitmapFactory.decodeFile(mFile.getPath()),0.2f));
        }
        else
        {
            if(BigwinerApplication.mApp.mAccount.sex == 0)
            {
                mShowCodeActivity.code.setImageBitmap( QRCodeUtil.createQRCodeBitmap(ContactsAsks.codeUrl(BigwinerApplication.mApp.mAccount.mRecordId),
                        (int) (mShowCodeActivity.mBasePresenter.mScreenDefine.density*270),
                        BitmapFactory.decodeResource(mShowCodeActivity.getResources(),R.drawable.default_user),0.2f));
            }
            else
            {
                mShowCodeActivity.code.setImageBitmap( QRCodeUtil.createQRCodeBitmap(ContactsAsks.codeUrl(BigwinerApplication.mApp.mAccount.mRecordId),
                        (int) (mShowCodeActivity.mBasePresenter.mScreenDefine.density*270),
                        BitmapFactory.decodeResource(mShowCodeActivity.getResources(),R.drawable.default_wuser),0.2f));
            }

        }

//        if(mFile.exists())
//        {
//            File file = new File(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/headicon")+"/"+BigwinerApplication.mApp.mAccount.mRecordId+".jpg");
//            if(file.exists())
//            {
//                if(file.length() == mFile.length())
//                {
//                    mShowCodeActivity.code.setImageBitmap( QRCodeUtil.createQRCodeBitmap(ContactsAsks.codeUrl(BigwinerApplication.mApp.mAccount.mRecordId),
//                            (int) (mShowCodeActivity.mBasePresenter.mScreenDefine.density*270),
//                            BitmapFactory.decodeFile(file.getPath()),0.2f));
//                }
//                else
//                {
//                    FileUtils.copyFile(mFile.getPath(),file.getPath());
//                    mShowCodeActivity.code.setImageBitmap( QRCodeUtil.createQRCodeBitmap(ContactsAsks.codeUrl(BigwinerApplication.mApp.mAccount.mRecordId),
//                            (int) (mShowCodeActivity.mBasePresenter.mScreenDefine.density*270),
//                            BitmapFactory.decodeFile(file.getPath()),0.2f));
//                }
//            }
//            else
//            {
//                FileUtils.copyFile(mFile.getPath(),file.getPath());
//                mShowCodeActivity.code.setImageBitmap( QRCodeUtil.createQRCodeBitmap(ContactsAsks.codeUrl(BigwinerApplication.mApp.mAccount.mRecordId),
//                        (int) (mShowCodeActivity.mBasePresenter.mScreenDefine.density*270),
//                        BitmapFactory.decodeFile(file.getPath()),0.2f));
//            }
//
//        }
//        else {
//            mShowCodeActivity.code.setImageBitmap( QRCodeUtil.createQRCodeBitmap(ContactsAsks.codeUrl(BigwinerApplication.mApp.mAccount.mRecordId), (int) (mShowCodeActivity.mBasePresenter.mScreenDefine.density*270), null,0.2f));
//        }


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

}
