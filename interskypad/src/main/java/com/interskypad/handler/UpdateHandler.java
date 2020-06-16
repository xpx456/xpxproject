package com.interskypad.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.interskypad.R;
import com.interskypad.manager.UpdateCacheManager;
import com.interskypad.view.activity.UpdateActivity;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;

public class UpdateHandler extends Handler {

    public static final int LOGIN_OUT = 1040000;


    public UpdateActivity mUpdateActivity;

    public UpdateHandler(UpdateActivity mUpdateActivity) {
        this.mUpdateActivity = mUpdateActivity;
    }


    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case UpdateCacheManager.DOWNLOAD_CATEGORY_FINISH:
                mUpdateActivity.progressBar.setProgress((Integer) msg.obj);
                mUpdateActivity.mUpdatePresenter.initProgerss();
                mUpdateActivity.mImf.setText(mUpdateActivity.getString(R.string.update_catalog));
                break;
            case UpdateCacheManager.DOWNLOAD_CATEGORY_FAIL:
                AppUtils.showMessage(mUpdateActivity,mUpdateActivity.getString(R.string.update_category_fail));
                mUpdateActivity.background.setText(mUpdateActivity.getString(R.string.update_tyr_again));
                break;
            case UpdateCacheManager.DOWNLOAD_PRODUCT_IMF_FINISH:
                mUpdateActivity.mImf.setText(mUpdateActivity.getString(R.string.update_catalog_pic));
                mUpdateActivity.progressBar.setProgress((Integer) msg.obj);
                mUpdateActivity.mUpdatePresenter.initProgerss();
                break;
            case UpdateCacheManager.DOWNLOAD_PRODUCT_IMF_FAIL:
                AppUtils.showMessage(mUpdateActivity,mUpdateActivity.getString(R.string.update_catalog_fail));
                mUpdateActivity.background.setText(mUpdateActivity.getString(R.string.update_tyr_again));
                break;
            case UpdateCacheManager.DOWNLOAD_PRODUCT_PIC_UPDATE:
                mUpdateActivity.progressBar.setProgress((Integer) msg.obj);
                mUpdateActivity.mUpdatePresenter.initProgerss();
                break;
            case UpdateCacheManager.DOWNLOAD_PRODUCT_PIC_FAIL:
                AppUtils.showMessage(mUpdateActivity,mUpdateActivity.getString(R.string.update_catalog_pic_fail)+msg.obj
                        +mUpdateActivity.getString(R.string.update_catalog_pic_fail_ex));
                mUpdateActivity.progressBar.setProgress(msg.arg1);
                mUpdateActivity.mUpdatePresenter.initProgerss();
                break;
            case UpdateCacheManager.DOWNLOAD_PRODUCT_PIC_FINISH:
                mUpdateActivity.progressBar.setProgress((Integer) msg.obj);
                mUpdateActivity.mUpdatePresenter.initProgerss();
                AppUtils.showMessage(mUpdateActivity,mUpdateActivity.getString(R.string.update_success));
                mUpdateActivity.finish();
                break;

        }
    }
}
