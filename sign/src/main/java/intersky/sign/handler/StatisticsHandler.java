package intersky.sign.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.sign.asks.SignAsks;
import intersky.sign.prase.SignPrase;
import intersky.sign.view.activity.StatisticsActivity;
import intersky.xpxnet.net.NetObject;

//04
public class StatisticsHandler extends Handler {

    public static final int EVENT_SET_USER = 3230300;

    StatisticsActivity theActivity;

    public StatisticsHandler(StatisticsActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case SignAsks.EVENT_GET_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                   SignPrase.praseSign((NetObject) msg.obj,theActivity,theActivity.mSignAdapter);
                    break;
                case EVENT_SET_USER:
                    theActivity.mStatisticsPresenter.setuser((Intent) msg.obj);
                    break;

            }
            super.handleMessage(msg);
        }
    }
}
