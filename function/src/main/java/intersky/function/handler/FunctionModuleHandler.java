package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.asks.FunAsks;
import intersky.function.prase.FunPrase;
import intersky.function.view.activity.FunctionModuleActivity;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//03
public class FunctionModuleHandler extends Handler {
    FunctionModuleActivity theActivity;

    public static final int SCAN_RESULT = 3180300;

    public FunctionModuleHandler(FunctionModuleActivity mFunctionModuleActivity) {
        theActivity = mFunctionModuleActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case FunAsks.SEARCE_HEAD_SUCCESS:
                NetObject object = (NetObject) msg.obj;
                String json = object.result;
                if(AppUtils.measureToken(json) != null) {
                    NetUtils.getInstance().token = AppUtils.measureToken(json);
                }
                if(AppUtils.success(json) == true)
                {
                    theActivity.searchHead = object.result;
                }
                theActivity.waitDialog.hide();
                theActivity.waitDialog.show();
                FunAsks.getBoardData(theActivity,theActivity.mFunctionModulePresenter.mFunctionModuleHandler,theActivity.mFunction,1,"");
                break;
            case FunAsks.BOARD_DATA_SUCCESS:
                NetObject netObject = (NetObject) msg.obj;
                FunPrase.praseFunctionModule((NetObject) msg.obj,theActivity.mFunData,theActivity.searchHead, (Integer) netObject.item,theActivity.mBasePresenter.mScreenDefine);
                int page = (int) netObject.item;
                if(page <= 1)
                theActivity.mFunctionModulePresenter.inidSerias();
                else
                    theActivity.mFunctionModulePresenter.updataView();
                theActivity.waitDialog.hide();
                theActivity.mTable.swipeRefreshLayout.setLoading(false);
                break;
            case SCAN_RESULT:
                intent = (Intent) msg.obj;
                theActivity.mSearchView.setText(intent.getStringExtra("result"));
                theActivity.mFunctionModulePresenter.doSearch();
                break;
            case NetUtils.NO_INTERFACE:
                if(msg.obj.equals(NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,FunAsks.SEARCE_HEAD_PATH)))
                {
                    theActivity.waitDialog.hide();
                    theActivity.waitDialog.show();
                    FunAsks.getBoardData(theActivity,theActivity.mFunctionModulePresenter.mFunctionModuleHandler,theActivity.mFunction,1,"");
                }
                theActivity.waitDialog.hide();
                theActivity.mTable.swipeRefreshLayout.setLoading(false);
                break;
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                theActivity.mTable.swipeRefreshLayout.setLoading(false);
                break;
        }

    }
}
