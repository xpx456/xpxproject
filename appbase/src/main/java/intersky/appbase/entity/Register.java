package intersky.appbase.entity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class Register {

    public static final int CONVERSATION_COLLECT_TYPE_BY_TYPE = 0;
    public static final int CONVERSATION_COLLECT_TYPE_BY_DETIAL = 1;
    public static final int CONVERSATION_COLLECT_TYPE_BY_NULL = 2;
    public String typeName = "";
    public String typeRealName = "";
    public String moduleId = "";
    public int registerType = CONVERSATION_COLLECT_TYPE_BY_TYPE;
    public int mPriority = 0;
    public int mPic = 0;
    public ConversationFunctions conversationFunctions;
    public int mNotivicationleavel = NotificationManager.IMPORTANCE_HIGH;
    public Register(String typeName,int registerType,int mPriority)
    {
        this.typeName = typeName;
        this.registerType = registerType;
        this.mPriority = mPriority;
    }

    public interface ConversationFunctions {
        void Open(Intent intent);
        void Open(Conversation conversation);
        void Read(String type,String id);
        void Asks(Handler handler, Intent intent);
    }
}
