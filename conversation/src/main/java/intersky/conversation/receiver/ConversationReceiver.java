package intersky.conversation.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.conversation.ConversationManager;
import intersky.conversation.handler.ConversationListHandler;


public class ConversationReceiver extends BaseReceiver {

    public Handler mHandler;

    public ConversationReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ConversationManager.ACTION_UPDATA_CONVERSATION);
        intentFilter.addAction(ConversationManager.ACTION_READ_CONVERSATION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ConversationManager.ACTION_UPDATA_CONVERSATION)
        ||intent.getAction().equals(ConversationManager.ACTION_READ_CONVERSATION))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = ConversationListHandler.UPDATE_LIST;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
    }
}
