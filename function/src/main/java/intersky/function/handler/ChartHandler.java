package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.function.asks.FunAsks;
import intersky.function.prase.FunPrase;
import intersky.function.view.activity.ChartActivity;
import intersky.xpxnet.net.NetObject;

//02
public class ChartHandler extends Handler {
    public ChartActivity theActivity;

    public ChartHandler(ChartActivity mChartActivity) {
        theActivity = mChartActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case FunAsks.BOARD_CHART_DATA_SUCCESS:
                FunPrase.praseChart((NetObject) msg.obj,theActivity.mFunData);
                theActivity.mChartPresenter.inidSerias();
                theActivity.waitDialog.hide();
                break;
        }

    }
}
