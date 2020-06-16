package intersky.workreport.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.oa.OaUtils;
import intersky.workreport.WorkReportManager;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.entity.Report;
import intersky.workreport.prase.WorkReportPrase;
import intersky.workreport.view.activity.NewReportActivity;
import intersky.xpxnet.net.NetObject;
//01
public class NewReportHandler extends Handler {

    public static final int EVENT_SET_SEND = 3270100;
    public static final int EVENT_SET_COPY = 3270101;
    public static final int EVENT_ADD_PIC = 3270102;
    public static final int EVENT_WORK_SET_CONTENT5 = 3270103;
    public static final int EVENT_WORK_SET_CONTENT1 = 3270104;
    public static final int EVENT_WORK_SET_CONTENT2 = 3270105;
    public static final int EVENT_WORK_SET_CONTENT3 = 3270106;
    public static final int EVENT_WORK_SET_CONTENT4 = 3270107;

    public NewReportActivity theActivity;

    public NewReportHandler(NewReportActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case WorkReportAsks.EVENT_SAVE_SUCCESS:
                    theActivity.waitDialog.hide();
                    theActivity.issub = true;
                    if(WorkReportPrase.praseData((NetObject) msg.obj,theActivity)) {
                        NetObject netObject = (NetObject) msg.obj;
                        Report report = (Report) netObject.item;
                        if(report.mRecordid.length() == 0)
                        {
                            WorkReportManager.getInstance().upDataHit(theActivity);
                            Bus.callData(theActivity,"function/updateOahit");
                            WorkReportManager.getInstance().sendReportAdd();
                        }
                        else
                        {
                            WorkReportManager.getInstance().sendReportUpdate(report.mRecordid);
                        }
                        theActivity.finish();
                    }
                    break;
                case EVENT_SET_COPY:
                    theActivity.mNewReportPresenter.setCopyer();
                    break;
                case EVENT_SET_SEND:
                    theActivity.mNewReportPresenter.setSender();
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT:
                    theActivity.mNewReportPresenter.doSave((String) msg.obj);
                    theActivity.waitDialog.hide();
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT_FAIL:
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity, (String) msg.obj);
                    break;
                case EVENT_ADD_PIC:
                    theActivity.mNewReportPresenter.setpic((Intent) msg.obj);
                    break;
                case EVENT_WORK_SET_CONTENT5:
                    theActivity.waitDialog.hide();
                    theActivity.summerText.setText((String) msg.obj);
                    break;
                case EVENT_WORK_SET_CONTENT1:
                    theActivity.waitDialog.hide();
                    theActivity.mNowWork.setText((String) msg.obj);
                    break;
                case EVENT_WORK_SET_CONTENT2:
                    theActivity.waitDialog.hide();
                    theActivity.mNextWork.setText((String) msg.obj);
                    break;
                case EVENT_WORK_SET_CONTENT3:
                    theActivity.waitDialog.hide();
                    theActivity.mWorkHelp.setText((String) msg.obj);
                    break;
                case EVENT_WORK_SET_CONTENT4:
                    theActivity.waitDialog.hide();
                    theActivity.mRemark.setText((String) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
