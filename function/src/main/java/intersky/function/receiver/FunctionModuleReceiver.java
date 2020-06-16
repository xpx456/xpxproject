package intersky.function.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.function.handler.FunctionModuleHandler;
import intersky.function.handler.GridDetialHandler;
import intersky.function.receiver.entity.Function;
import intersky.function.view.activity.GridDetialActivity;
import intersky.scan.ScanUtils;


public class FunctionModuleReceiver extends BaseReceiver {

    public Handler mHandler;

    public FunctionModuleReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ScanUtils.ACTION_SCAN_FINISH);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ScanUtils.ACTION_SCAN_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = FunctionModuleHandler.SCAN_RESULT;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }

    }
}
