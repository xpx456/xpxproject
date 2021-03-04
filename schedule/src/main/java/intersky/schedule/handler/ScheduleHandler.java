package intersky.schedule.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.bus.Bus;
import intersky.apputils.TimeUtils;
import intersky.schedule.asks.ScheduleAsks;
import intersky.schedule.entity.Event;
import intersky.schedule.prase.SchedulePrase;
import intersky.schedule.view.activity.ScheduleActivity;
import intersky.xpxnet.net.NetObject;

//01
public class ScheduleHandler extends Handler {

    public static final int UPDATA_CONTACT_SCHEDULE = 3230100;
    public static final int UPDATA_EVENT = 3230101;
    public static final int SET_TODAY = 3230102;

    public ScheduleActivity theActivity;

    public ScheduleHandler(ScheduleActivity mScheduleActivity) {
        theActivity = mScheduleActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ScheduleAsks.EVENT_LIST_SUCCESS:
                SchedulePrase.praseEvent((NetObject) msg.obj,theActivity.calendarEventHelper);
                theActivity.mSchedulePresenter.updataSelectEvent();
                break;
            case UPDATA_CONTACT_SCHEDULE:
                theActivity.mSchedulePresenter.setUser((Intent) msg.obj);
                break;
            case UPDATA_EVENT:
                intent = (Intent) msg.obj;
                Event event = intent.getParcelableExtra("event");
                if(event != null)
                theActivity.calendarEventHelper.updateEvent(event);
                break;
            case ScheduleAsks.EVENT_DELETE_SUCCESS:
                NetObject netObject = (NetObject) msg.obj;
                theActivity.waitDialog.hide();
                Bus.callData(theActivity,"function/updateOahit");
                theActivity.calendarEventHelper.deleteEvent((Event) netObject.item);
                break;
            case SET_TODAY:
                theActivity.calendarView.toSpecifyDate(Integer.valueOf(TimeUtils.getDate().substring(0,4)),
                Integer.valueOf(TimeUtils.getDate().substring(5,7)),
                        Integer.valueOf(TimeUtils.getDate().substring(8,10)));
                break;
        }

    }

}
