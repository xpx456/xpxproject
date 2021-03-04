package intersky.schedule.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.appbase.bus.Bus;
import intersky.schedule.asks.ScheduleAsks;
import intersky.schedule.prase.SchedulePrase;
import intersky.schedule.view.activity.EventEditActivity;
import intersky.schedule.view.activity.ScheduleActivity;
import intersky.xpxnet.net.NetObject;
//00
public class EventEditHandler extends Handler {

    public static final int SET_REMINDS = 3230000;

    public EventEditActivity theActivity;

    public EventEditHandler(EventEditActivity mEventEditActivity) {
        theActivity = mEventEditActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ScheduleAsks.EVENT_REMIND_SUCCESS:
                SchedulePrase.praseRemind((NetObject) msg.obj,theActivity.mEventEditPresenter.mReminds,theActivity.mEvent);
                theActivity.mEventEditPresenter.updataRemind();
                break;
            case SET_REMINDS:
                theActivity.mEventEditPresenter.updataRemind();
                break;
            case ScheduleAsks.EVENT_SAVE_SUCCESS:
                intent = new Intent(ScheduleActivity.ACTION_UPDATA_EVENT);
                intent.putExtra("event",theActivity.mEvent);
                theActivity.sendBroadcast(intent);
                Bus.callData(theActivity,"function/updateOahit");
                theActivity.finish();
                break;

        }

    }

}
