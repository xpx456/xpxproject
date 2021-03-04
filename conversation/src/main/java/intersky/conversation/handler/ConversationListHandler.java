package intersky.conversation.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.conversation.view.activity.ConversationListActivity;

//01
public class ConversationListHandler extends Handler {

    public static final int UPDATE_LIST = 3060100;

    public ConversationListActivity theActivity;

    public ConversationListHandler(ConversationListActivity mConversationListActivity) {
        theActivity = mConversationListActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case UPDATE_LIST:
                theActivity.mConversationListPresenter.doUpdate();
                break;
        }

    }
}
