package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.function.asks.FunAsks;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.prase.FunPrase;
import intersky.function.prase.WorkFlowPrase;
import intersky.function.view.activity.GridActivity;
import intersky.function.view.activity.GridDetialActivity;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//06
public class GridHandler extends Handler {

    public final static int UPDATA_ACTION = 3180600;

    public GridActivity theActivity;

    public GridHandler(GridActivity mGridActivity) {
        theActivity = mGridActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case FunAsks.BOARD_DATA_SUCCESS:
                NetObject item = (NetObject) msg.obj;
                FunPrase.praseBoardData(item.result,theActivity.mFunData, (Integer) item.item,theActivity.mBasePresenter.mScreenDefine);
                int page = (int) item.item;
                if(page <= 1)
                    theActivity.mGridPresenter.initData();
                else
                    theActivity.mGridPresenter.updataView();
                theActivity.waitDialog.hide();
                theActivity.mTable.swipeRefreshLayout.setLoading(false);
                break;
            case FunAsks.GET_SUB_SUCCESS:
                NetObject item1 = (NetObject) msg.obj;
                FunPrase.parseGropData(item1.result,theActivity.mFunData,theActivity.mBasePresenter.mScreenDefine,theActivity.mFunction.mCaption);
                theActivity.mGridPresenter.initData();
                break;
            case FunAsks.GRIDE_UPDATE_SUCCESS:
                break;
            case FunAsks.GRIDE_UPDATE_NEW_SUCCESS:
                break;
            case WorkFlowAsks.WORKFLOW_VETO_SUCCESS:
            case WorkFlowAsks.WORKFLOW_ACCEPT_SUCCESS:
                if (WorkFlowPrase.praseAction((NetObject) msg.obj)) {
                    theActivity.finish();
                    intent = new Intent();
                    intent.putExtra("taskid",theActivity.mFunction.modulflag);
                    intent.putExtra("actionsuccess",true);
                    intent.setAction(GridDetialActivity.ACTION_WORKFLOW_HIT);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case UPDATA_ACTION:
                theActivity.mGridPresenter.updataAction((Intent) msg.obj);
                break;
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                theActivity.mTable.swipeRefreshLayout.setLoading(false);
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                theActivity.mTable.swipeRefreshLayout.setLoading(false);
                break;
        }

    }
}
