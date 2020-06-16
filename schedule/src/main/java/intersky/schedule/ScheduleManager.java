package intersky.schedule;

import android.content.Context;
import android.content.Intent;

import intersky.appbase.entity.Account;
import intersky.apputils.TimeUtils;
import intersky.oa.OaUtils;
import intersky.schedule.entity.Event;
import intersky.schedule.view.activity.EventEditActivity;
import intersky.schedule.view.activity.ScheduleActivity;
import intersky.xpxnet.net.Service;

public class ScheduleManager {

    public static volatile ScheduleManager mScheduleManager;
    public Context context;
    public OaUtils oaUtils;
    public static ScheduleManager init(OaUtils oaUtils,Context context) {
        if (mScheduleManager == null) {
            synchronized (ScheduleManager.class) {
                if (mScheduleManager == null) {
                    mScheduleManager = new ScheduleManager(oaUtils,context);
                }
                else
                {
                    mScheduleManager.context = context;
                    mScheduleManager.oaUtils = oaUtils;
                }
            }
        }
        return mScheduleManager;
    }

    public static ScheduleManager getInstance() {
        return mScheduleManager;
    }

    public ScheduleManager(OaUtils oaUtils,Context context) {
        this.context = context;
        this.oaUtils = oaUtils;
    }

    public void startScheduleMain(Context context) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        context.startActivity(intent);

    }

    public void startEvent(Context context) {
        Intent intent = new Intent(context, EventEditActivity.class);
        Event event = new Event();
        event.mTime = TimeUtils.getDateAndTime();
        intent.putExtra("event",event);
        context.startActivity(intent);
    }
}
