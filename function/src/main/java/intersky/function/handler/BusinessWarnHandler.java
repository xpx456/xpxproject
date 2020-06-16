package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.receiver.entity.BussinessWarnItem;
import intersky.function.prase.WorkFlowPrase;
import intersky.function.view.activity.BusinessWarnActivity;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//01
public class BusinessWarnHandler extends Handler {

    public BusinessWarnActivity theActivity;

    public BusinessWarnHandler(BusinessWarnActivity mBusinessWarnActivity) {
        theActivity = mBusinessWarnActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case WorkFlowAsks.REMIND_LIST_SUCCESS:
                WorkFlowPrase.praseBusinessWarnList((NetObject) msg.obj,theActivity.mFunData);
                theActivity.mBusinessWarnPresenter.initData();
                theActivity.waitDialog.hide();
                break;
            case WorkFlowAsks.REMIND_DISMISS_SUCCESS:
                NetObject object = (NetObject) msg.obj;
                if(AppUtils.measureToken(object.result) != null) {
                    NetUtils.getInstance().token = AppUtils.measureToken(object.result);
                }
                if(AppUtils.success(object.result) == true)
                {
                    theActivity.mBusinessWarnPresenter.removeItem((BussinessWarnItem) object.item);
                    FunctionUtils.getInstance().getBaseHit();
                }
                theActivity.waitDialog.hide();
                break;
        }

    }
}
