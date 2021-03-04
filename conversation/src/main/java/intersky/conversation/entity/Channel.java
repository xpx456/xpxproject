package intersky.conversation.entity;

import android.app.NotificationManager;
import android.os.Build;

import java.util.HashMap;

import intersky.appbase.entity.Register;
import intersky.conversation.ConversationManager;

public class Channel{
    public String name = "";
    public String id = "";
    public int messageid = 0;
    public int desid = 0;
    public String moduleid = "";
    public String registername = "";
    public int type = Register.CONVERSATION_COLLECT_TYPE_BY_TYPE;
    public HashMap<String,Integer> messageGropId = new HashMap<String,Integer>();
    public int leave = NotificationManager.IMPORTANCE_HIGH;

    public int getNid(String detialid) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if(detialid.length() == 0)
            {
                return messageid;
            }
            else
            {
                return getNotificationGropId(detialid);
            }
        }
        else
        {
            return messageid;
        }
    }

    private int getNotificationGropId(String detial) {
        if(!messageGropId.containsKey(detial)) {
            int id = messageid+desid;
            messageGropId.put(detial,id);
            desid++;
        }
        return messageGropId.get(detial);
    }

}