package intersky.schedule.receive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.schedule.handler.ScheduleHandler;
import intersky.schedule.view.activity.ScheduleActivity;


public class ScheduleReceiver extends BaseReceiver {

    public Handler mHandler;

    public ScheduleReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ScheduleActivity.ACTION_SET_SCHEDULE_CONTACT);
        intentFilter.addAction(ScheduleActivity.ACTION_UPDATA_EVENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ScheduleActivity.ACTION_SET_SCHEDULE_CONTACT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = ScheduleHandler.UPDATA_CONTACT_SCHEDULE;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(ScheduleActivity.ACTION_UPDATA_EVENT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = ScheduleHandler.UPDATA_EVENT;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }

    }
}
