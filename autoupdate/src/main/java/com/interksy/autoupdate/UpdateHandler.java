package com.interksy.autoupdate;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;

//00
public class UpdateHandler extends Handler {

    public static final int EVENT_CHECK_UPDATA_FAIL = 3070001;
    public static final int EVENT_CHECK_UPDATA = 3070002;
    public static final int EVETN_DOWNLOAD_FAIL = 3070003;
    public static final int EVETN_DOWNLOAD_UPADTE = 3070004;
    public static final int EVETN_DOWNLOAD_FINISH = 3070005;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_CHECK_UPDATA:
                UpDataManager.mUpDataManager.initJson((NetObject) msg.obj);
                break;
            case EVENT_CHECK_UPDATA_FAIL:
                UpDataManager.mUpDataManager.docheck = false;
                UpDataManager.mUpDataManager.checkVersin();
                break;
            case EVETN_DOWNLOAD_FAIL:
                UpDataManager.mUpDataManager.state = UpDataManager.UPDATA_NONE;
                break;
            case EVETN_DOWNLOAD_UPADTE:
                UpdataDownloadThread.UpMessage upMessage = (UpdataDownloadThread.UpMessage) msg.obj;
                UpDataManager.mUpDataManager.updataOperation.showProgress(UpDataManager.mUpDataManager.context.getString(R.string.keyword_updata),
                        UpDataManager.mUpDataManager.context.getString(R.string.keyword_updataloading),upMessage.size,upMessage.finishsize);
                break;
            case EVETN_DOWNLOAD_FINISH:
                UpDataManager.mUpDataManager.updataOperation.showCancle();
                UpDataManager.mUpDataManager.doUpDataAppView();
                break;
        }
    }
}
