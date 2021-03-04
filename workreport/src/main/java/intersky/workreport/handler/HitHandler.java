package intersky.workreport.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.apputils.AppUtils;
import intersky.workreport.WorkReportManager;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.entity.Report;
import intersky.workreport.prase.WorkReportPrase;
import intersky.xpxnet.net.NetObject;
//00
public class HitHandler extends Handler {

    public Context context;

    public HitHandler(Context context){
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {

            switch (msg.what) {
                case WorkReportAsks.EVENT_GET_HIT_SUCCESS:
                    WorkReportPrase.praseHit((NetObject) msg.obj);
                    AppUtils.sendSampleBroadCast(context, WorkReportManager.ACTION_WORK_REPORT_ACTION_UPDATA_HIT);
                    break;
                case WorkReportAsks.EVENT_GET_DETIAL_SUCCESS:
                    BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
                    baseActivity.waitDialog.hide();
                    boolean exist = WorkReportPrase.prasseDetial((NetObject) msg.obj,context);
                    Report report = (Report) ((NetObject) msg.obj).item;
                    if(exist == true)
                    WorkReportManager.getInstance().startDetial( WorkReportManager.getInstance().workContext,report);
                    break;
            }
            super.handleMessage(msg);

    }
}
