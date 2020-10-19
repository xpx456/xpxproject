package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.function.ChartUtils;
import intersky.function.asks.FunAsks;
import intersky.function.prase.FunPrase;
import intersky.function.view.activity.ChartActivity;
import intersky.function.view.activity.MyChartActivity;
import intersky.xpxnet.net.NetObject;

//02
public class MyChartHandler extends Handler {
    public MyChartActivity theActivity;

    public MyChartHandler(MyChartActivity mChartActivity) {
        theActivity = mChartActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case FunAsks.BOARD_CHART_DATA_SUCCESS:
                //theActivity.chartShow.loadUrl(theActivity.mChartPresenter.url);
//                ChartUtils.loadsLineChart(theActivity.chartData,theActivity.chartShow);
                theActivity.mChartPresenter.updata();
                break;
        }

    }
}
