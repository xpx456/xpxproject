package intersky.document.receive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.document.DocumentManager;
import intersky.document.handler.SetPositionHandler;
import intersky.appbase.BaseReceiver;


public class SetPositionReceiver extends BaseReceiver {

    public Handler mHandler;

    public SetPositionReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(DocumentManager.ACTION_CREAT_DOCUMENT_FINISH);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(DocumentManager.ACTION_CREAT_DOCUMENT_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = SetPositionHandler.EVENT_UPDATE_DOCUMENT_LIST;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }

    }
}
