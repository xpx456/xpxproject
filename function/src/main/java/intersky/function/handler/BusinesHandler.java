package intersky.function.handler;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import intersky.appbase.BaseActivity;
import intersky.apputils.AppUtils;
import intersky.function.R;
import intersky.function.view.activity.BusinesCardActivity;

//00
public class BusinesHandler extends Handler {

    public final static int REQUEST_CODE_ASK_PERMISSIONS = 1040002;
    public static final int EVENT_SET_DATA = 3180000;
    public static final int EVENT_UPDATA_DATA = 3180001;
    public static final int EVENT_UPLOADFINISH = 3180002;
    public static final int EVENT_UPLOADFAIL = 3180003;
    public static final int EVENT_SCHANGE = 3180004;
    BusinesCardActivity theActivity;

    public BusinesHandler(BusinesCardActivity activity) {
        theActivity =  activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case EVENT_UPDATA_DATA:
                    theActivity.waitDialog.hide();
                    if (theActivity.isShowSearch == true) {
                        theActivity.mSearchBusinesAdapter.notifyDataSetChanged();

                    } else {
                        theActivity.mBusinesAdapter.notifyDataSetChanged();
                    }
                    break;
                case EVENT_SET_DATA:
                    theActivity.mBusinesCardPresenter.initlistdata();
                    theActivity.waitDialog.hide();
                    if (theActivity.isShowSearch == true) {
                        theActivity.mListView
                                .setAdapter(theActivity.mSearchBusinesAdapter);
                    } else {
                        theActivity.mListView
                                .setAdapter(theActivity.mBusinesAdapter);
                    }
                    break;
                case EVENT_UPLOADFINISH:
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity, theActivity.getString(R.string.rode_in_finish));
                    break;
                case EVENT_UPLOADFAIL:
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity, theActivity.getString(R.string.rode_in_error));
                    break;
                case EVENT_SCHANGE:
                    theActivity.mBusinesCardPresenter.initlistdata();
                    if (theActivity.isShowSearch == true) {
                        theActivity.mSearchBusinesAdapter.notifyDataSetChanged();

                    } else {
                        theActivity.mBusinesAdapter.notifyDataSetChanged();
                    }
                    break;
                case REQUEST_CODE_ASK_PERMISSIONS:
                    theActivity.mBusinesCardPresenter.getContacts();
                    break;
            }
            super.handleMessage(msg);
        }
    }
};

