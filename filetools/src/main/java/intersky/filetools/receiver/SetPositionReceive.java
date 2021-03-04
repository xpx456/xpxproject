package intersky.filetools.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.filetools.FileUtils;
import intersky.filetools.handler.PhotoSelectHandler;

public class SetPositionReceive extends BaseReceiver {

    public Handler mHandler;
    public SetPositionReceive(Handler mHandler)
    {
        this.mHandler = mHandler;
        intentFilter.addAction(FileUtils.ATTACHMENT_SET_POSITION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(FileUtils.ATTACHMENT_SET_POSITION))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = PhotoSelectHandler.SET_POSITION;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
    }

}
