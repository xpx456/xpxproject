package com.dk.dktest.handler;

import android.os.Handler;
import android.os.Message;

import com.dk.dktest.view.activity.HistoryDetialActivity;
import com.dk.dktest.view.activity.SportDetialActivity;

import java.util.ArrayList;


//01


public class HistoryDetialHandler extends Handler {


    public HistoryDetialActivity theActivity;
    public static final int UPDATA_DATA = 100002;
    public HistoryDetialHandler(HistoryDetialActivity mHistoryDetialActivity) {
        theActivity = mHistoryDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_DATA:
                theActivity.mHistoryDetialPresenter.loadChart();
                break;
        }

    }
}
