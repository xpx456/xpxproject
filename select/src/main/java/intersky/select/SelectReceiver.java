package intersky.select;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import intersky.appbase.BaseReceiver;

/**
 * Created by xpx on 2017/8/4.
 */



public class SelectReceiver extends BaseReceiver {

//    public static final String ACTION_UPDATA_PROJECT_VIEW = "updata_project_view";
//    public static final String ACTION_UPDATA_PROJECT_LEADER= "updata_base_project_leader";

    public static final String  ACTION_UPDATA_SELECT_LIST = "ACTION_UPDATA_SELECT_LIST";
    public Handler mHandler;

    public SelectReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(ACTION_UPDATA_SELECT_LIST);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_UPDATA_SELECT_LIST))
        {
            if(mHandler != null)
            {
                mHandler.sendEmptyMessage(SelectHandler.UPDATA_LIST_VIEW);
            }
        }
    }
}
