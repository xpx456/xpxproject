package intersky.schedule.receive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.schedule.handler.EventEditHandler;
import intersky.schedule.handler.ScheduleHandler;
import intersky.schedule.view.activity.EventEditActivity;


public class EventEditReceiver extends BaseReceiver {

    public Handler mHandler;

    public EventEditReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(EventEditActivity.ACTION_SET_SCHEDULE_REMIND);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(EventEditActivity.ACTION_SET_SCHEDULE_REMIND))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = EventEditHandler.SET_REMINDS;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }

    }
}
