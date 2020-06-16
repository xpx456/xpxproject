package intersky.attendance.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;

import intersky.attendance.AttendanceManager;
import intersky.attendance.asks.AttdenanceAsks;
import intersky.attendance.prase.AttdencePrase;
import intersky.attendance.view.activity.AttendanceActivity;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.xpxnet.net.NetObject;

//00
public class AttendanceHandler extends Handler {

    public static final int EVENT_SET_USER = 3160000;
    public static final int EVENT_SAVE_UPDATA_TIME = 3160001;
    public static final int EVENT_SET_ADDRESS = 3160002;
    public static final int EVENT_SET_ADDRESS_OUT_SIDE = 3160003;
    public static final int EVENT_UPDATA_LIST = 3160004;
    public AttendanceActivity theActivity;

    public AttendanceHandler(AttendanceActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case AttdenanceAsks.EVENT_GET_LIST_SUCCESS:
                    AttdencePrase.praseList((NetObject) msg.obj,theActivity,theActivity.mWorkAttendances);
                    theActivity.mAttendancePresenter.initListView();
                    theActivity.mAttendancePresenter.initAttendanceView();
                    theActivity.waitDialog.hide();
                    theActivity.mAttendancePresenter.setCanSign();
                    break;
                case EVENT_SET_ADDRESS:
                    if(!theActivity.mAddressOrg.equals(((AMapLocation)msg.obj).getAddress()))
                    {
                        theActivity.mAddressOrg = ((AMapLocation)msg.obj).getAddress();
                        theActivity.mCity = ((AMapLocation)msg.obj).getCityCode();
                        theActivity.mAttendancePresenter.selectAddress("畅想软件");
                        theActivity.mAddress = ((AMapLocation)msg.obj).getAddress();
//                        AppUtils.showMessage(theActivity,theActivity.mAddress);
                    }
                    break;
                case EVENT_SET_ADDRESS_OUT_SIDE:
                    if (!theActivity.mAddress.equals((String) msg.obj)) {
                        theActivity.mAddress = (String) msg.obj;

                    }
                    break;
                case AttdenanceAsks.EVENT_SAVE_SUCCESS:
                    theActivity.waitDialog.hide();
                    AttdenanceAsks.getWorkAttdenaceList(theActivity.mAttendancePresenter.mAttendanceHandler,theActivity,
                            AttendanceManager.getInstance().getSetUserid(),theActivity.mDeteString);
                    theActivity.waitDialog.show();
                    theActivity.mAttendancePresenter.showSig((NetObject) msg.obj);
                    theActivity.bingSign = false;
                    break;
                case EVENT_SET_USER:
                    theActivity.mAttendancePresenter.setuser((Intent) msg.obj);
                    break;
                case EVENT_SAVE_UPDATA_TIME:
                    theActivity.signTime.setText(TimeUtils.getTimeSecond());
                    if(theActivity.mAttendancePresenter.mAttendanceHandler != null)
                        theActivity.mAttendancePresenter.mAttendanceHandler.sendEmptyMessageDelayed(EVENT_SAVE_UPDATA_TIME, 1000);
                    break;
                case AttdenanceAsks.EVENT_UP_SUCCESS:
                    AppUtils.showMessage(theActivity,"更新打卡成功");
                    theActivity.waitDialog.hide();
                    AttdenanceAsks.getWorkAttdenaceList(theActivity.mAttendancePresenter.mAttendanceHandler,theActivity,
                            AttendanceManager.getInstance().getSetUserid(),theActivity.mDeteString);
                    theActivity.waitDialog.show();
                    break;
                case EVENT_UPDATA_LIST:
                    theActivity.mAddress = (String) msg.obj;
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
