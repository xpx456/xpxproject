package intersky.notice.receicer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.notice.NoticeManager;
import intersky.notice.handler.NewNoticeHandler;


@SuppressLint("NewApi")
public class NewNoticeReceiver extends BaseReceiver {

    public Handler mHandler;

    public NewNoticeReceiver(Handler mHandler) {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(NoticeManager.ACTION_NOTICE_UPATE_SENDER);
        intentFilter.addAction(NoticeManager.ACTION_NOTICE_ADDPICTORE);
        intentFilter.addAction(NoticeManager.ACTION_SET_NOTICE_CONTENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(NoticeManager.ACTION_NOTICE_ADDPICTORE)) {
            Message smsg = new Message();
            smsg.what = NewNoticeHandler.EVENT_ADD_PIC;
            smsg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(smsg);
        } else if (intent.getAction().equals(NoticeManager.ACTION_NOTICE_UPATE_SENDER)) {
            Message smsg = new Message();
            smsg.what = NewNoticeHandler.EVENT_SET_SEND;
            smsg.obj = intent;
            if (mHandler != null)
                mHandler.sendMessage(smsg);
        } else if (intent.getAction().equals(NoticeManager.ACTION_SET_NOTICE_CONTENT)) {
            Message msg = new Message();
            msg.what = NewNoticeHandler.EVENT_NOTICE_SET_CONTENT;
            msg.obj = intent.getStringExtra("value");
            if (mHandler != null)
                mHandler.sendMessage(msg);
        }
    }

}
