package intersky.function.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.function.handler.GridDetialHandler;
import intersky.function.view.activity.GridDetialActivity;


public class GrideDetialReceiver extends BaseReceiver {

    public Handler mHandler;

    public GrideDetialReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(GridDetialActivity.ACTION_SET_SELECT_LIST);
        intentFilter.addAction(GridDetialActivity.ACTION_GRID_PHOTO_SELECT);
        intentFilter.addAction(GridDetialActivity.ACTION_GRID_DETIAL_UPDAT);
        intentFilter.addAction(GridDetialActivity.ACTION_WORKFLOW_HIT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(GridDetialActivity.ACTION_SET_SELECT_LIST))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = GridDetialHandler.SET_LIST;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(GridDetialActivity.ACTION_GRID_PHOTO_SELECT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = GridDetialHandler.SET_PIC;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(GridDetialActivity.ACTION_GRID_DETIAL_UPDAT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = GridDetialHandler.SET_PIC;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(GridDetialActivity.ACTION_WORKFLOW_HIT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = GridDetialHandler.UPDATA_ACTION;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
    }
}
