package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.function.FunctionUtils;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.prase.WorkFlowPrase;
import intersky.function.receiver.entity.Function;
import intersky.function.view.activity.WorkFlowActivity;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//10
public class WorkFlowHandler extends Handler {
    public WorkFlowActivity theActivity;
    public static final int UPDATA_LIST = 302165;
    public WorkFlowHandler(WorkFlowActivity mWorkFlowActivity) {
        theActivity = mWorkFlowActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case WorkFlowAsks.WORKFLOW_LIST_SUCCESS:
                NetObject netObject = (NetObject) msg.obj;
                if(theActivity.getIntent().getBooleanExtra("iscloud",false) == false)
                {
                    WorkFlowPrase.praseWorkFlowList((NetObject) msg.obj, FunctionUtils.getInstance().mFunData);
                    theActivity.mWorkFlowPresenter.inidSerias(0);
                }
                else
                {
                    if((int)netObject.item == 0)
                    {
                        WorkFlowPrase.praseWorkFlowList((NetObject) msg.obj,FunctionUtils.getInstance().mFunData);
                    }
                    else
                    {
                        WorkFlowPrase.praseWorkFlowList((NetObject) msg.obj,FunctionUtils.getInstance().mFunData2);
                    }
                    theActivity.mWorkFlowPresenter.inidSerias((Integer) netObject.item);
                }
                theActivity.waitDialog.hide();
                break;
            case UPDATA_LIST:
                theActivity.mWorkFlowPresenter.updataList();
                break;

        }

    }
}
