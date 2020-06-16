package intersky.conversation.handler;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import intersky.conversation.ConversationManager;
import intersky.appbase.entity.Conversation;

//02
public class OaMessageHandler extends Handler {

    public static final int ADD_CONVERSATION = 3060200;
    public static final int DELETE_CONVERSATION = 3060201;
    public static final int UPDTATA_CONVERSATION = 3060202;
    public static final int REMOVE_CONVERSATION = 3060203;
    public static final int READ_CONVERSATION = 3060204;
    public static final int ADD_CONVERSATION_LIST = 3060205;
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ADD_CONVERSATION:
                ConversationManager.getInstance().add((Conversation) msg.obj);
                break;
            case ADD_CONVERSATION_LIST:
                ConversationManager.getInstance().add((ArrayList<Conversation>) msg.obj);
                break;
            case DELETE_CONVERSATION:
                ConversationManager.getInstance().delete((Conversation) msg.obj);
                break;
            case UPDTATA_CONVERSATION:
                ConversationManager.getInstance().updata((Conversation) msg.obj);
                break;
            case REMOVE_CONVERSATION:
                ConversationManager.getInstance().remove((Conversation) msg.obj);
                break;
            case READ_CONVERSATION:
                ConversationManager.getInstance().read((Conversation) msg.obj);
                break;
        }

    }
}
