package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.prase.WorkFlowPrase;
import intersky.function.view.activity.GridDetialActivity;
import intersky.function.view.activity.WebMessageActivity;
import intersky.function.view.activity.WorkFlowActivity;
import intersky.xpxnet.net.NetObject;

//09
public class WebMessageHandler extends Handler {
    public WebMessageActivity theActivity;
    public static final int EVENT_INPUT = 3180900;
    public WebMessageHandler(WebMessageActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case WebMessageActivity.SCANNIN_EVENT:
                    theActivity.mWebMessagePresenter.startScan();
                    break;
                case WebMessageActivity.SCANNIN_EVENT_WITH_JSON:
                    theActivity.json = (String) msg.obj;
                    theActivity.mWebMessagePresenter.startScan();
                    break;
                case WebMessageActivity.GET_PIC_PATH:
                    theActivity.mWebMessagePresenter.doupload((String) msg.obj);
                    break;
                case WebMessageActivity.EVENT_GET_INFO:
                    theActivity.mWebMessagePresenter.getloginfo();
                    break;
                case WorkFlowAsks.WORKFLOW_ACCEPT_SUCCESS:

                    if (WorkFlowPrase.praseAction((NetObject) msg.obj)) {
                        Intent intent = new Intent(GridDetialActivity.ACTION_WORKFLOW_HIT);
                        theActivity.sendBroadcast(intent);
                        FunctionUtils.getInstance().getBaseHit();
                        theActivity.finish();
                    }
                    else
                    {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.keyword_operfail));
                    }
                    break;
                case WorkFlowAsks.WORKFLOW_TASK_SUCCESS:
                    NetObject netObject = (NetObject) msg.obj;
                    theActivity.mWebMessagePresenter.parseWarnDetailInfo(netObject.result);
                    theActivity.waitDialog.hide();
                    break;
                case EVENT_INPUT:
                    if(theActivity.ongoback)
                    {
                        theActivity.mWebView.loadUrl("javascript:valueToInput()");
                        theActivity.ongoback = false;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
