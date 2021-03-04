package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.function.asks.FunAsks;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.prase.FunPrase;
import intersky.function.prase.WorkFlowPrase;
import intersky.function.view.activity.GridDetialActivity;
import intersky.mywidget.TableCloumArts;
import intersky.xpxnet.net.NetObject;

//05
public class GridDetialHandler extends Handler {

    public final static int SET_PIC = 3180500;
    public final static int SET_LIST = 3180501;
    public final static int UPDATE_VIEW = 3180502;
    public final static int SET_IMAGE_LOCAL_PATH = 3180503;
    public final static int UPDATA_ACTION = 3180504;
    public GridDetialActivity theActivity;

    public GridDetialHandler(GridDetialActivity mGridDetialActivity) {
        theActivity = mGridDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case WorkFlowAsks.WORKFLOW_TASK_SUCCESS:
                WorkFlowPrase.praseWorkFlowTask((NetObject) msg.obj,theActivity.mTableDetial,theActivity.mFunction);
                theActivity.mGridDetialPresenter.initData();
                theActivity.waitDialog.hide();
                break;
            case FunAsks.GET_SUB_SUCCESS:
                FunPrase.praseSubDate((NetObject) msg.obj,theActivity.mTableDetial,theActivity.mFunction);
                theActivity.mGridDetialPresenter.initData();
                theActivity.waitDialog.hide();
                break;
            case FunAsks.GET_LINK_SUCCESS:
                NetObject item = (NetObject) msg.obj;
                FunPrase.praseLinkValue(item.result, (TableCloumArts) item.item,theActivity.mTableDetial);
                theActivity.waitDialog.hide();
                break;
            case SET_PIC:
                theActivity.mGridDetialPresenter.setPicResult((Intent) msg.obj);
                break;
            case SET_LIST:
                theActivity.mGridDetialPresenter.setSelectValue();
                break;
            case FunAsks.GET_FILL_SUCCESS:
                FunPrase.praseFill((NetObject) msg.obj,theActivity.mTableDetial,this);
                theActivity.waitDialog.hide();
                break;
            case UPDATE_VIEW:
                theActivity.mGridDetialPresenter.updateView();
                break;
            case WorkFlowAsks.WORKFLOW_ACCEPT_SUCCESS:
            case WorkFlowAsks.WORKFLOW_VETO_SUCCESS:
                if (WorkFlowPrase.praseAction((NetObject) msg.obj)) {
                    theActivity.finish();
                    intent = new Intent();
                    intent.putExtra("taskid",theActivity.mFunction.modulflag);
                    intent.putExtra("actionsuccess",true);
                    intent.setAction(GridDetialActivity.ACTION_WORKFLOW_HIT);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case SET_IMAGE_LOCAL_PATH:
                NetObject netObject = (NetObject) msg.obj;
                TableCloumArts tableCloumArts = (TableCloumArts) netObject.item;
                tableCloumArts.localPath = netObject.result;
                break;
            case FunAsks.GRIDE_UPDATE_NEW_SUCCESS:
                theActivity.mGridDetialPresenter.doEditEnd();
                theActivity.waitDialog.hide();
                break;
            case FunAsks.GRIDE_UPDATE_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.finish();
                break;
            case FunAsks.GRIDE_UPDATE_NEW_START_SUCCESS:
            case FunAsks.GRIDE_UPDATE_START_SUCCESS:
                theActivity.mGridDetialPresenter.doReCreat();
                theActivity.waitDialog.hide();
                break;
            case FunAsks.GRIDE_DELETE_SUCCESS:
                theActivity.finish();
                break;
            case UPDATA_ACTION:
                theActivity.mGridDetialPresenter.updataAction((Intent) msg.obj);
                break;
        }

    }
}
