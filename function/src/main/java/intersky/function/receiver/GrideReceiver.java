package intersky.function.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.function.handler.GridHandler;
import intersky.function.view.activity.GridDetialActivity;


public class GrideReceiver extends BaseReceiver {

    public Handler mHandler;

    public GrideReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(GridDetialActivity.ACTION_WORKFLOW_HIT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(GridDetialActivity.ACTION_WORKFLOW_HIT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = GridHandler.UPDATA_ACTION;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }

    }
}
