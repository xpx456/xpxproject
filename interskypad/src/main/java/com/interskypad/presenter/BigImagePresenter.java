package com.interskypad.presenter;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.interskypad.R;
import com.interskypad.entity.Catalog;
import com.interskypad.manager.ProducterManager;
import com.interskypad.view.InterskyPadApplication;
import com.interskypad.view.activity.BigImageActivity;

import java.io.File;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;

public class BigImagePresenter implements Presenter {

    public BigImageActivity mPadBaseActivity;

    public BigImagePresenter(BigImageActivity mPadBaseActivity) {
        this.mPadBaseActivity = mPadBaseActivity;
    }

    @Override
    public void initView() {
        mPadBaseActivity.setContentView(R.layout.activity_pic_view);
        mPadBaseActivity.mRelativeLayout = (RelativeLayout) mPadBaseActivity.findViewById(R.id.detial_pic_view_layer);
        ImageView mImageView = (ImageView) mPadBaseActivity.findViewById(R.id.detial_pic_view);
        Catalog mCatalogGridItem = mPadBaseActivity.getIntent().getParcelableExtra("catalog");
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.temp);
        if(InterskyPadApplication.mApp.isLogin)
            Glide.with(mPadBaseActivity).load(ProducterManager.getInstance().getProductPhotoUrl(mCatalogGridItem.mPhoto)).apply(options).into(new MySimpleTarget(mImageView));
        else
        {
            File file1 = new File(InterskyPadApplication.mApp.mFileUtils.pathUtils.getfilePath("/photo")+"/"+mCatalogGridItem.mPhoto);
            if(file1.exists() && !file1.isDirectory())
            {
                Glide.with(mPadBaseActivity).load(file1).apply(options).into(new MySimpleTarget(mImageView));
            }
            else
            {
                mImageView.setImageResource(R.drawable.temp);
            }

        }
    }

    @Override
    public void Create() {
        initView();
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
