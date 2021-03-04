package intersky.workreport.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.workreport.view.activity.WorkReportActivity;
//04
public class WorkReportHandler extends Handler {

    public static final  int EVENT_UPDATA_HIT = 3270400;

    public WorkReportActivity theActivity;

    public WorkReportHandler(WorkReportActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case EVENT_UPDATA_HIT:
                    theActivity.waitDialog.hide();
                    theActivity.mWorkReportPresenter.updateHitView();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
