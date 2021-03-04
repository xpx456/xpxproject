package intersky.filetools.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import intersky.filetools.entity.AlbumItem;

public class AlbumThread extends Thread {

    public Handler mHandler;
    public Context mContext;
    public int eventCode;

    public AlbumThread(Handler mHandler, Context mContext, int eventCode) {
        this.mHandler = mHandler;
        this.mContext = mContext;
        this.eventCode = eventCode;
    }

    @Override
    public void run() {
        AlbumItem.getInstance().buildImagesBucketList();
        Message msg = new Message();
        msg.what = eventCode;
        if (mHandler != null)
            mHandler.sendMessage(msg);
    }

}
